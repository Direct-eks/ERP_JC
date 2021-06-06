<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>往来单位</p>-->
    <v-card>
        <v-card-title>
            往来单位
            <v-spacer></v-spacer>
            <div class="mr-3" style="width: 200px">
                <v-text-field v-model="searchField"
                              label="电话/简称"
                              hide-details="auto"
                              clearable>
                </v-text-field>
            </div>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <v-card-text class="d-flex">
            <v-responsive height="65vh" max-width="15vw"
                          style="overflow: auto">
                <v-treeview :items="treeData"
                            item-text="label"
                            item-key="areaID"
                            activatable
                            return-object
                            @update:active="treeSelect"
                            color="primary"
                            open-on-click
                            dense>
                </v-treeview>
            </v-responsive>
            <v-responsive max-width="60vw"
                          style="overflow: auto">
                <v-data-table v-model="currentRow"
                              :headers="headers"
                              :items="tableData"
                              item-key="companyID"
                              @click:row="handleTableClick"
                              height="65vh"
                              disable-sort
                              disable-pagination
                              :search="searchField"
                              hide-default-footer
                              show-select
                              single-select
                              fixed-header
                              locale="zh-cn"
                              dense>
                </v-data-table>
            </v-responsive>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "PartnerCompanies",
    beforeMount() {
        const result = this.$store.getters.companyCategoryList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.companyAreas).then((data) => {
            console.log('received', data)
            this.treeData = this.$createTree(data, false)
            this.$store.commit('modifyCompanyList', this.treeData)
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            searchField: '',
            headers: [
                { text: '单位简称', value: 'abbreviatedName', width: '120px' },
                { text: '电话', value: 'phone', width: '120px' },
                { text: '传真', value: 'fax', width: '120px', filterable: false },
                { text: '重要提示', value: 'remark', width: '120px', filterable: false },
                { text: '单位类别', value: 'classification', width: '120px', filterable: false },
                { text: '联系人', value: 'contactPerson', width: '120px', filterable: false },
                { text: '联系人电话', value: 'contactNumber', width: '120px', filterable: false },
                { text: '地址', value: 'address', width: '120px', filterable: false },
                { text: '邮政编码', value: 'zipcode', width: '120px', filterable: false },
                { text: '单位全称', value: 'fullName', width: '120px', filterable: false },
            ],
            tableData: [],
            currentRow: [],

            treeData: []
        }
    },
    methods: {
        searchCompanies() {
            this.$getRequest(this.$api.companyFuzzySearch, {
                name: this.name,
                phone: this.phone,
            }).then((data) => {
                console.log('receive', data)
                this.tableData = data
            }).catch(() => {})
        },
        handleTableClick(val) {
            this.currentRow = [val]
        },
        chooseHandle() {
            if (this.currentRow.length !== 0) this.$emit('fullSearchChoose', this.currentRow[0])
        },
        treeSelect(data) {
            const val = data[0]
            if (val.children.length === 0) { // end node
                // console.log(data)
                const result = this.$store.getters.companies(val.areaID)
                if (result) {
                    this.tableData = result
                    return
                }
                this.$getRequest(this.$api.companiesByCategory
                    + encodeURI(val.areaID)).then((data) => {
                    console.log('received', data)
                    this.tableData = data
                    this.$store.commit('modifyCompanies', { key: val.areaID, value: data })
                }).catch(() => {})
            }
        }
    }
}
</script>

<style scoped>

</style>
