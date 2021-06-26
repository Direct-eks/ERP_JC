<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>相关单位</p>-->
    <v-card>
        <v-card-title class="d-flex">
            相关单位
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text class="d-flex">
            <v-card outlined>
                <v-responsive>
                    <v-treeview v-model="treeSelection"
                                :items="treeData"
                                item-text="label"
                                item-key="categoryID"
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
                <v-responsive max-width="65vw">
                    <v-data-table v-model="currentRow"
                                  :headers="tableHeaders"
                                  :items="tableData"
                                  item-key="companyID"
                                  @click:row="tableClick"
                                  @item-selected="tableClick2"
                                  disable-sort
                                  disable-pagination
                                  hide-default-footer
                                  height="55vh"
                                  show-select
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-responsive>
                <v-row class="mt-1">
                    <v-col cols="auto">
                        <v-btn color="primary" class="ml-3"
                               @click="">
                            新增
                        </v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn color="warning" class="ml-3"
                               @click="">
                            删除
                        </v-btn>
                    </v-col>
                    <v-spacer></v-spacer>
                    <v-col cols="auto">
                        <v-btn color="success" class="mr-3"
                               @click="">
                            保存修改
                        </v-btn>
                    </v-col>
                </v-row>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "RelevantCompanies",
    beforeMount() {
        const creatTree = (data => {
            const tree = [];
            data.forEach(item => {
                tree.push({label: item.name, children: [], categoryID: item.categoryID})
            })
            return tree
        })

        let result = this.$store.getters.relevantCompanyCategoryList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.relevantCompanyCategories).then((data) => {
            console.log('received', data)
            this.treeData = creatTree(data)
            this.$store.commit('modifyRelevantCompanyList', this.treeData)
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            treeData: [],
            treeSelection: [],
            tableHeaders: [
                { text: '单位简称', value: 'abbreviatedName', width: '120px' },
                { text: '单位全称', value: 'fullName', width: '120px', filterable: false },
                { text: '电话', value: 'phone', width: '120px' },
                { text: '传真', value: 'fax', width: '120px', filterable: false },
                { text: '重要提示', value: 'remark', width: '120px', filterable: false },
                { text: '单位类别', value: 'classification', width: '120px', filterable: false },
                { text: '联系人', value: 'contactPerson', width: '120px', filterable: false },
                { text: '联系人电话', value: 'contactNumber', width: '120px', filterable: false },
                { text: '地址', value: 'address', width: '120px', filterable: false },
                { text: '邮政编码', value: 'zipcode', width: '120px', filterable: false },
            ],
            name: '',
            tableData: [],
            currentRow: []
        }
    },
    methods: {
        tableClick(row) {
            if (this.currentRow.indexOf(row) !== -1) {
                this.currentRow = []
            }
            else {
                this.currentRow = [row]
            }
        },
        tableClick2(row) {
            if (row.value) {
                this.currentRow = []
            }
            else {
                this.currentRow = [row.item]
            }
        },
        treeSelect(data) {
            this.currentRow = []
            if (data.length === 0) return
            let val = data[0]
            if (val.children.length === 0) {
                let result = this.$store.getters.relevantCompanies(val.categoryID)
                if (result) {
                    this.tableData = result
                    return
                }
                this.$getRequest(this.$api.relevantCompaniesByCategory +
                    encodeURI(val.categoryID)).then((data) => {
                    this.tableData = data
                    this.$store.commit('modifyRelevantCompanies', {key: val.categoryID, value: data})
                }).catch(() => {})
            }
        },
        addNewItem() {

        },
        removeItem() {

        },
        saveChanges() {

        }
    }
}
</script>

<style scoped>

</style>
