<template>
        <v-card height="85vh">
            <v-card-title>
                <v-toolbar dense flat>
                    <v-toolbar-title>型号助选</v-toolbar-title>
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
                            <v-text-field v-model="newModelCode"
                                          label="新代号"
                                          outlined
                                          dense
                                          clearable
                                          style="width: 200px">
                            </v-text-field>
                        </v-col>
                        <v-col cols="auto">
                            <v-text-field v-model="oldModelCode"
                                          label="原代号"
                                          outlined
                                          dense
                                          clearable
                                          style="width: 200px">
                            </v-text-field>
                        </v-col>
                        <v-col cols="auto">
                            <v-radio-group v-model="matchChoice"
                                           row dense class="mt-0">
                                <v-radio label="前匹配" value="1"></v-radio>
                                <v-radio label="模糊" value="2"></v-radio>
                                <v-radio label="后匹配" value="3"></v-radio>
                            </v-radio-group>
                        </v-col>
                        <v-col cols="auto" class="mr-auto">
                            <v-btn color="primary"
                                   @click="search">
                                查询
                            </v-btn>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn color="primary"
                                   @click="addNewHandle">
                                新增商品
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

                <v-row>
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
                    <v-col cols="auto" md="3">
                        <v-data-table v-model="modelTableCurrentRow"
                                      :headers="modelTableHeaders"
                                      :items="modelTableData"
                                      item-key="modelID"
                                      @click:row="modelTableChoose"
                                      height="65vh"
                                      hide-default-footer
                                      calculate-widths
                                      disable-sort
                                      disable-pagination
                                      single-select
                                      fixed-header
                                      locale="zh-cn"
                                      dense>
                        </v-data-table>
                    </v-col>
                    <v-col cols="auto" md="6">
                        <v-row>
                            <v-col cols="auto" md="2" class="pt-0">
                                <v-data-table v-model="stockTableCurrentRow"
                                              :headers="stockTableHeaders"
                                              :items="stockTableData"
                                              item-key="stockID"
                                              @click:row="stockTableChoose"
                                              height="20vh"
                                              calculate-widths
                                              disable-sort
                                              single-select
                                              fixed-header
                                              hide-default-footer
                                              locale="zh-cn"
                                              dense>
                                </v-data-table>
                            </v-col>
                            <v-col cols="auto" md="10" class="pt-0">
                                <v-data-table v-model="warehouseStockCurrentRow"
                                              :headers="warehouseStockTableHeaders"
                                              :items="warehouseStockTableData"
                                              item-key="warehouseStockID"
                                              height="20vh"
                                              calculate-widths
                                              disable-sort
                                              single-select
                                              fixed-header
                                              hide-default-footer
                                              locale="zh-cn"
                                              dense>
                                </v-data-table>
                            </v-col>
                        </v-row>
                        <v-divider></v-divider>
                        <v-row>
                            <v-col>
                                <v-radio-group></v-radio-group>
                            </v-col>
                        </v-row>
                        <v-divider></v-divider>
                        <v-row>
                            <v-col>
                                <v-data-table v-model="supplierCurrentRow"
                                              :headers="supplierTableHeaders"
                                              :items="supplierTableData"
                                              item-key=""
                                              height="25vh"
                                              calculate-widths
                                              disable-sort
                                              single-select
                                              fixed-header
                                              hide-default-footer
                                              locale="zh-cn"
                                              dense>
                                </v-data-table>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>

            </v-card-text>
        </v-card>
</template>

<script>
    export default {
        name: "ModelSearch",
        data() {
            return {
                newModelCode: '',
                oldModelCode: '',
                matchChoice: '1',

                treeData: [],

                modelTableHeaders: [
                    {
                        text: '新代号',
                        value: 'newModelCode',
                        width: '160px'
                    }, {
                        text: '原代号',
                        value: 'oldModelCode',
                        width: '140px'
                    }
                ],
                modelTableData: [],
                modelTableCurrentRow: [],

                stockTableHeaders: [
                    {
                        text: '生产厂',
                        value: 'factoryBrandCode',
                        width: '80px'
                    }
                ],
                stockTableData: [],
                stockTableCurrentRow: [],

                warehouseStockTableHeaders: [
                    {
                        text: '仓库',
                        value: 'warehouseName',
                        width: '120px'
                    }, {
                        text: '架位',
                        value: '',
                        width: '120px'
                    }, {
                        text: '库存数量',
                        value: 'quantity',
                        width: '120px'
                    }, {
                        text: '库存无税价',
                        value: '',
                        width: '120px'
                    }
                ],
                warehouseStockTableData: [],
                warehouseStockCurrentRow: [],

                supplierTableHeaders: [
                    {
                        text: '新代号',
                        value: '',
                        width: '160px'
                    }, {
                        text: '原代号',
                        value: '',
                        width: '140px'
                    }
                ],
                supplierTableData: [],
                supplierCurrentRow: []
            }
        },
        beforeMount() {
            const creatTree = (data) => {
                const tree = [];
                for (let item of data) {
                    if (item.treeLevel.length === 1) { //first level object
                        tree.push({label: item.categoryCode, children: [],
                            categoryID: item.categoryID, treeLevel: item.treeLevel})
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
                            label: item.categoryCode, children: [],
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

            let result = this.$store.getters.productList
            if (result) {
                this.treeData = result
                return
            }
            this.$getRequest(this.$api.productCategoryList).then((res) => {
                console.log('received', res.data)
                this.treeData = creatTree(res.data)
                this.$store.commit('modifyProductList', this.treeData)
            }).catch(error => this.$ajaxErrorHandler(error))
        },
        methods: {
            close() {
                this.$emit('modelSearchClose')
            },
            addNewHandle() {

            },
            search() {
                this.modelTableCurrentRow = [] //reset model table
                this.stockTableCurrentRow = [] //reset stock table

                if (this.newModelCode === '' && this.oldModelCode === '') {
                    this.$store.commit('setSnackbar', {
                        message: '请填写代号', color: 'error'
                    })
                }

                this.$postRequest(this.$api.modelByModelCode, {
                    newModelCode: this.newModelCode,
                    oldModelCode: this.oldModelCode,
                    matchMode: parseInt(this.matchChoice)
                }).then((res) => {
                    console.log('received', res.data)
                    this.modelTableData = res.data
                    if (this.modelTableData.length === 0) {
                        this.$store.commit('setSnackbar', {
                            message: '无结果', color: 'warning'
                        })
                    }
                }).catch(error => this.$ajaxErrorHandler(error))
            },
            treeSelect(data) {
                this.modelTableCurrentRow = [] //reset model table
                this.stockTableCurrentRow = [] //reset stock table

                let val = data[0]
                if (val.children.length === 0) { // end node
                    console.log(val.categoryID)
                    let result = this.$store.getters.models(val.categoryID)
                    if (result) {
                        this.modelTableData = result
                        return
                    }
                    this.$postRequest(this.$api.modelByCategory, {categoryID: val.categoryID}).then((res) => {
                        console.log('received', res.data)
                        this.modelTableData = res.data
                        this.$store.commit('modifyModels', {key: val.categoryID, value: res.data})
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
            },
            modelTableChoose(val) {
                this.modelTableCurrentRow = [val]
                this.stockTableCurrentRow = [] //reset stock table

                this.$postRequest(this.$api.stockByModel, val).then((res) => {
                    console.log('received', res.data)
                    this.stockTableData = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
            },
            stockTableChoose(val) {
                this.stockTableCurrentRow = [val]

                this.$postRequest(this.$api.warehouseStockByStock, val).then((res) => {
                    console.log('received', res.data)
                    this.warehouseStockTableData = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
            },
            chooseHandle() {
                if (this.modelTableCurrentRow.length !== 0 &&
                    this.stockTableCurrentRow.length !== 0) {
                    this.$emit('modelSearchChoose', {
                        newModelCode: this.modelTableCurrentRow[0].newModelCode,
                        oldModelCode: this.modelTableCurrentRow[0].oldModelCode,
                        stockID: this.stockTableCurrentRow[0].stockID,
                        factoryBrandCode: this.stockTableCurrentRow[0].factoryBrandCode,
                        unitName: this.modelTableCurrentRow[0].unitName,
                        /*-- field initialization --*/
                        quantity: 0,
                        taxRate: 0.16,
                        unitPriceWithTax: 0.0,
                        unitPriceWithoutTax: 0.0,
                        remark: ''
                    })
                    this.modelTableCurrentRow = []
                    this.stockTableCurrentRow = []
                }
            },
        }
    }
</script>

<style scoped lang="sass">

</style>

