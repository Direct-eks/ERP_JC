<template>
    <v-card>
        <v-card-title>
            <v-toolbar dense flat>
                <v-toolbar-title>相关单位</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
        </v-card-title>

        <v-card-text>
            <v-row>
                <v-col cols="auto">
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
                </v-col>
                <v-divider vertical></v-divider>
                <v-col cols="auto" md="10">
                    <v-data-table v-model="currentRow"
                                  :headers="tableHeaders"
                                  :items="tableData"
                                  item-key="relativeCompanyID"
                                  @click:row="handleTableClick"
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
                </v-col>
            </v-row>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="chooseHandle">
                选择
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    export default {
        name: "RelativeCompanySearch",
        data() {
            return {
                treeData: [],
                tableHeaders: [
                    {
                        text: '单位简称',
                        value: 'relativeCompanyAbbreviatedName',
                        width: '120px'
                    }, {
                        text: '电话',
                        value: 'phone',
                        width: '120px'
                    }, {
                        text: '联系人',
                        value: 'contactPerson',
                        width: '120px'
                    }, {
                        text: '联系电话',
                        value: 'contactNumber',
                        width: '120px'
                    }, {
                        text: '地址',
                        value: 'address',
                        width: '120px'
                    }, {
                        text: '传真',
                        value: 'fax',
                        width: '120px'
                    }
                ],
                tableData: [],
                currentRow: []
            }
        },
        beforeMount() {
            const creatTree = (data => {
                const tree = [];
                data.forEach(item => {
                    tree.push({label: item.categoryName, children: [], categoryID: item.categoryID})
                })
                return tree
            })

            let result = this.$store.getters.companyList
            if (result) {
                this.treeData = result
                return
            }
            this.$getRequest(this.$api.relativeCompanyList).then((res) => {
                console.log('received', res.data)
                this.treeData = creatTree(res.data)
                this.$store.commit('modifyRelativeCompanyList', this.treeData)
            }).catch(error => this.$ajaxErrorHandler(error))
        },
        methods: {
            close() {
                this.$emit('relativeCompanyClose')
            },
            chooseHandle() {
                if (this.currentRow.length !== 0)
                    this.$emit('relativeCompanyChoose', this.currentRow[0])
            },
            handleTableClick(val) {
                this.currentRow = [val]
            },
            treeSelect(data) {
                let val = data[0]
                if (val.children.length === 0) {
                    let result = this.$store.getters.relativeCompanies(val.categoryID)
                    if (result) {
                        this.tableData = result
                        return
                    }
                    this.$postRequest(this.$api.relativeCompanyByCategory, {categoryID: val.categoryID}).then((res) => {
                        console.log('received', res.data)
                        this.tableData = res.data
                        this.$store.commit('modifyRelativeCompanies', {key: val.categoryID, value: res.data})
                    })
                }
            }
        } // end methods
    }
</script>

<style scoped>

</style>
