<template>
    <v-card>
        <v-card-title>
            往来单位
            <v-spacer></v-spacer>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="phone"
                              label="电话"
                              hide-details="auto"
                              clearable
                              @keydown.enter.native="searchCompanies">
                </v-text-field>
            </div>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="name"
                              label="简称"
                              hide-details="auto"
                              clearable
                              @keydown.enter.native="searchCompanies">
                </v-text-field>
            </div>
            <v-btn color="primary"
                   @click="searchCompanies">
                搜索
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   class="mr-6"
                   @click="chooseHandle">
                选择
            </v-btn>
            <v-btn icon @click="close">
                <v-icon>{{ mdiClose }}</v-icon>
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
            <v-responsive max-width="64vw"
                          style="overflow: auto">
                <v-data-table v-model="currentRow"
                              :headers="headers"
                              :items="tableData"
                              item-key="companyID"
                              @click:row="handleTableClick"
                              height="65vh"
                              disable-sort
                              disable-pagination
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
import { mdiClose } from '@mdi/js'

export default {
    data() {
        return {
            mdiClose,

            phone: '',
            name: '',

            headers: [
                { text: '单位简称', value: 'abbreviatedName', width: '120px' },
                { text: '电话', value: 'phone', width: '120px' },
                { text: '传真', value: 'fax', width: '120px' },
                { text: '重要提示', value: 'remark', width: '120px' },
                { text: '单位类别', value: 'classification', width: '120px' },
                { text: '联系人', value: 'contactPerson', width: '120px' },
                { text: '联系人电话', value: 'contactNumber', width: '120px' },
                { text: '地址', value: 'address', width: '120px' },
                { text: '邮政编码', value: 'zipcode', width: '120px' },
                { text: '单位全称', value: 'fullName', width: '120px' },
            ],
            tableData: [],
            currentRow: [],

            treeData: []
        }
    },
    name: "CompanySearch",
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
    methods: {
        close() {
            this.$emit('fullSearchChoose', null)
        },
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
                this.$getRequest(this.$api.companiesByAreaID
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
