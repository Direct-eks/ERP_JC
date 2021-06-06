<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>往来单位</p>-->
    <v-card>
        <v-card-title class="d-flex">
            往来单位
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text class="d-flex">
            <v-card outlined>
                <v-responsive height="80vh" max-width="15vw"
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
            </v-card>
            <v-card outlined>
                <v-responsive height="80vh" max-width="65vw"
                              style="overflow: auto">
                    <v-data-table v-model="currentRow"
                                  :headers="headers"
                                  :items="tableData"
                                  item-key="companyID"
                                  @click:row="tableClick"
                                  @item-selected="tableClick2"
                                  height="70vh"
                                  disable-sort
                                  show-select
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                        <template
                    </v-data-table>
                </v-responsive>
            </v-card>
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
            this.treeData = this.$createTree(data)
            console.log(this.treeData)
            this.$store.commit('modifyCompanyList', this.treeData)
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            treeData: [],

            headers: [
                { text: '单位简称', value: 'abbreviatedName', width: '120px'},
                { text: '电话', value: 'phone', width: '120px'},
                { text: '传真', value: 'fax', width: '120px'},
                { text: '重要提示', value: 'remark', width: '120px'},
                { text: '单位类别', value: 'classification', width: '120px'},
                { text: '联系人', value: 'contactPerson', width: '120px'},
                { text: '联系人电话', value: 'contactNumber', width: '120px'},
                { text: '地址', value: 'address', width: '120px'},
                { text: '邮政编码', value: 'zipcode', width: '120px'},
                { text: '单位全称', value: 'fullName', width: '120px'},
            ],
            tableData: [],
            currentRow: [],

        }
    },
    methods: {
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
        },
        tableClick(row) {
            this.currentRow = [row]
        },
        tableClick2(row) {
            if (row.value) {
                this.currentRow = []
            }
            else {
                this.currentRow = [row.item]
            }
        }
    }
}
</script>

<style scoped>

</style>
