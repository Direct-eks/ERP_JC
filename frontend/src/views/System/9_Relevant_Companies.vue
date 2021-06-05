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
                    <v-treeview :items="treeData"
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
                <v-responsive>
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
            tableHeaders: [
                {text: '单位简称', value: 'abbreviatedName', width: '120px'},
                {text: '电话', value: 'phone', width: '120px'},
                {text: '联系人', value: 'contactPerson', width: '120px', filterable: false},
                {text: '联系电话', value: 'contactNumber', width: '120px', filterable: false},
                {text: '地址', value: 'address', width: '120px', filterable: false},
                {text: '传真', value: 'fax', width: '120px', filterable: false}
            ],
            name: '',
            tableData: [],
            currentRow: []
        }
    },
    methods: {
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
        },
        treeSelect(data) {
            let val = data[0]
            if (val.children.length === 0) {
                let result = this.$store.getters.relevantCompanies(val.categoryID)
                if (result) {
                    this.tableData = result
                    return
                }
                this.$getRequest(this.$api.relevantCompaniesByCategory +
                    encodeURI(val.categoryID)).then((data) => {
                    console.log('received', data)
                    this.tableData = data
                    this.$store.commit('modifyRelevantCompanies', {key: val.categoryID, value: data})
                }).catch(() => {})
            }
        },
    }
}
</script>

<style scoped>

</style>
