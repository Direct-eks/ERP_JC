<template>
    <v-card>
        <v-card-title>
            <v-toolbar dense flat>
                <v-toolbar-title>往来单位</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
        </v-card-title>

        <v-card-text>
            <v-form @keyup.enter.native="search">
                <v-row>
                    <v-col cols="auto">
                        <v-text-field v-model="phone"
                                      label="电话"
                                      outlined
                                      dense
                                      style="width: 200px">
                        </v-text-field>
                    </v-col>
                    <v-col cols="auto">
                        <v-text-field v-model="name"
                                      label="简称"
                                      outlined
                                      dense
                                      style="width: 200px">
                        </v-text-field>
                    </v-col>
                    <v-col cols="auto" class="mr-auto">
                        <v-btn color="accent"
                               @click="search">
                            模糊查询
                        </v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn color="primary"
                               @click="chooseHandle">
                            选择
                        </v-btn>
                    </v-col>
                </v-row>
            </v-form>

            <v-row dense>
                <v-col cols="auto">
                    <v-responsive height="65vh"
                                  style="overflow: auto">
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
                </v-col>
                <v-col cols="auto" md="9">

                    <v-data-table v-model="currentRow"
                                  :headers="headers"
                                  :items="tableData"
                                  item-key="companyID"
                                  @click:row="handleTableClick"
                                  height="65vh"
                                  calculate-widths
                                  disable-sort
                                  show-select
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-col>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
    export default {
        data() {
            return {
                phone: '',
                name: '',
                headers: [
                    {
                        text: '单位简称',
                        value: 'companyAbbreviatedName',
                        width: '120px'
                    }, {
                        text: '电话',
                        value: 'phone',
                        width: '120px'
                    }, {
                        text: '传真',
                        value: 'fax',
                        width: '120px'
                    }, {
                        text: '重要提示',
                        value: 'remark',
                        width: '120px'
                    }, {
                        text: '单位类别',
                        value: 'classification',
                        width: '120px'
                    }, {
                        text: '联系人',
                        value: 'contactPerson',
                        width: '120px'
                    }, {
                        text: '联系人电话',
                        value: 'contactNumber',
                        width: '120px'
                    }, {
                        text: '地址',
                        value: 'address',
                        width: '120px'
                    }, {
                        text: '邮政编码',
                        value: 'zip',
                        width: '120px'
                    }, {
                        text: '单位全称',
                        value: 'companyFullName',
                        width: '120px'
                    }
                ],
                tableData: [],
                currentRow: [],

                treeData: []
            }
        },
        name: "CompanySearch",
        beforeMount() {
            const creatTree = (data) => {
                const tree = [];
                for (let item of data) {
                    if (item.treeLevel.length === 1) { //first level object
                        tree.push({
                            label: item.categoryName, children: [],
                            categoryID: item.categoryID, treeLevel: item.treeLevel
                        })
                    }
                }
                console.log(tree)
                for (let index in tree) {
                    creatTreeHelper(tree, index, data, tree[index].treeLevel, 1)
                }
                return tree
            }
            const creatTreeHelper = (tree, lastLevelIndex, data, prefix, depth) => {
                let count = 0;
                for (let item of data) {
                    if (item.treeLevel.startsWith(prefix + '-') &&
                        item.treeLevel.length === depth * 2 + 1) {
                        tree[lastLevelIndex].children.push({
                            label: item.categoryName, children: [],
                            categoryID: item.categoryID, treeLevel: item.treeLevel
                        })
                        count++
                    }
                }
                if (count === 0) return

                for (let index in tree[lastLevelIndex].children) {
                    creatTreeHelper(tree[lastLevelIndex].children, index, data, tree[lastLevelIndex].children[index].treeLevel, depth + 1)
                }
            }

            let result = this.$store.getters.companyList
            if (result) {
                this.treeData = result
                return
            }
            this.$getRequest(this.$api.companyList).then((res) => {
                console.log('received', res.data)
                this.treeData = creatTree(res.data)
                console.log(this.treeData)
                this.$store.commit('modifyCompanyList', this.treeData)
            })
        },
        beforeDestroy() {
            console.log('company search to be destroyed')
            this.tableData = null
            this.treeData = null
        },
        methods: {
            close() {
                this.$emit('fullSearchClose')
            },
            handleTableClick(val) {
                this.currentRow = [val]
            },
            chooseHandle() {
                if (this.currentRow.length !== 0)
                    this.$emit('fullSearchChoose', this.currentRow[0])
            },
            treeSelect(data) {
                let val = data[0]
                if (val.children.length === 0) { // end node
                    // console.log(data)
                    let result = this.$store.getters.companies(val.categoryID)
                    if (result) {
                        this.tableData = result
                        return
                    }
                    this.$postRequest(this.$api.companyByCategory, {categoryID: val.categoryID}).then((res) => {
                        console.log('received', res.data)
                        this.tableData = res.data
                        this.$store.commit('modifyCompanies', {key: val.categoryID, value: res.data})
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
            },
            search() {
                if (this.phone !== '' && this.name !== '') {
                    this.$postRequest(this.$api.companyByNameAndPhone,
                        {phone: this.phone, companyAbbreviatedName: this.name}).then((res) => {
                        console.log('received', res.data)
                        this.tableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                } else if (this.phone !== '') {
                    this.$postRequest(this.$api.companyByPhone, {phone: this.phone}).then((res) => {
                        console.log('received', res.data)
                        this.tableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                } else if (this.name !== '') {
                    this.$postRequest(this.$api.companyByName, {companyAbbreviatedName: this.name}).then((res) => {
                        console.log('received', res.data)
                        this.tableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
            }
        }
    }
</script>

<style scoped>
</style>
