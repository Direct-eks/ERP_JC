<template>
        <v-card height="85vh">
            <v-card-title>
                型号助选
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="modelCode"
                                  label="代号筛选"
                                  hide-details="auto"
                                  clearable
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="modelSearchName"
                                  label="代号搜索"
                                  hide-details="auto"
                                  clearable
                                  style="width: 120px"
                                  @keydown.enter.native="modelSearch">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-radio-group v-model="modelSearchCategory"
                                   hide-details="auto"
                                   class="mt-0"
                                   dense>
                        <v-radio label="新代号" value="newCode"></v-radio>
                        <v-radio label="旧代号" value="oldCode"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-radio-group v-model="modelSearchMethod"
                                   hide-details="auto"
                                   class="mt-0"
                                   dense>
                        <v-radio label="前匹配" value="prefix"></v-radio>
                        <v-radio label="模糊" value="infix"></v-radio>
                        <v-radio label="后匹配" value="suffix"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="accent"
                           @click="modelSearch">
                        查询
                    </v-btn>
                </v-col>
                <v-spacer></v-spacer>
                <v-btn class="mr-8"
                       color="primary"
                       @click="chooseHandle">
                    选择
                </v-btn>
                <v-btn icon @click="close">
                    <v-icon>{{mdiClosePath}}</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text>
                <div class="d-flex">
                    <v-card outlined>
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
                    </v-card>
                    <v-card outlined>
                        <v-data-table v-model="modelTableCurrentRow"
                                      :headers="modelTableHeaders"
                                      :items="modelTableData"
                                      item-key="modelID"
                                      @click:row="modelTableChoose"
                                      :search="modelCode"
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
                    </v-card>

                    <div class="d-flex flex-column">
                        <div class="d-flex">
                            <v-card outlined>
                                <v-responsive max-height="25vh" style="overflow: auto">
                                    <v-data-table v-model="skuTableCurrentRow"
                                                  :headers="skuTableHeaders"
                                                  :items="skuTableData"
                                                  item-key="skuID"
                                                  @click:row="skuTableChoose"
                                                  height="20vh"
                                                  calculate-widths
                                                  disable-sort
                                                  single-select
                                                  fixed-header
                                                  hide-default-footer
                                                  locale="zh-cn"
                                                  dense>
                                    </v-data-table>
                                </v-responsive>
                            </v-card>
                            <v-card outlined>
                                <v-responsive max-height="25vh"
                                              max-width="35vw"
                                              style="overflow: auto">
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
                                </v-responsive>
                            </v-card>
                        </div>

                        <v-row>
                            <v-col cols="auto">
                                <v-btn class="mr-8"
                                       color="accent"
                                       @click="addNewHandle">
                                    新增商品
                                </v-btn>
                            </v-col>
                        </v-row>

                    </div>
                </div>

            </v-card-text>
        </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "ModelSearch",
    props: {
        warehouseID: {type: Number, required: true, default: -1}
    },
    data() {
        return {
            mdiClosePath: mdiClose,

            modelCode: '',
            modelSearchName: '',
            modelSearchCategory: 'newCode',
            modelSearchMethod: 'prefix',

            treeData: [],

            modelTableHeaders: [
                {text: '新代号', value: 'newCode', width: '180px'},
                {text: '原代号', value: 'oldCode', width: '180px'}
            ],
            modelTableData: [],
            modelTableCurrentRow: [],

            skuTableHeaders: [
                {text: '生产厂', value: 'factoryCode', width: '80px'}
            ],
            skuTableData: [],
            skuTableCurrentRow: [],

            warehouseStockTableHeaders: [
                {text: '仓库', value: 'warehouseName', width: '120px'},
                {text: '架位', value: '', width: '120px'},
                {text: '库存数量', value: 'stockQuantity', width: '120px'},
                {text: '库存无税价', value: 'stockUnitPriceWithoutTax', width: '120px'}
            ],
            warehouseStockTableData: [],
            warehouseStockCurrentRow: []
        }
    },
    beforeMount() {
        const creatTree = (data) => {
            const tree = [];
            for (let item of data) {
                if (item.treeLevel.length === 1) { //first level object
                    tree.push({label: item.code, children: [],
                        categoryID: item.modelCategoryID, treeLevel: item.treeLevel})
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
                        label: item.code, children: [],
                        categoryID: item.modelCategoryID, treeLevel: item.treeLevel
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
        this.$getRequest(this.$api.modelCategories).then((data) => {
            console.log('received', data)
            this.treeData = creatTree(data)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})
    },
    methods: {
        close() {
            this.$emit('modelSearchClose')
        },
        addNewHandle() {

        },
        modelSearch() {
            this.$getRequest(this.$api.modelsByName, {
                name: this.modelSearchName,
                category: this.modelSearchCategory,
                method: this.modelSearchMethod
            }).then((data) => {
                console.log('received', data)
                this.modelTableData = data
            }).catch(() => {})
        },
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []

            let val = data[0]
            if (val.children.length === 0) { // end node
                console.log(val.categoryID)
                let result = this.$store.getters.models(val.categoryID)
                if (result) {
                    this.modelTableData = result
                    return
                }
                this.$getRequest(this.$api.modelsByCategory +
                        encodeURI(val.categoryID)).then((data) => {
                    console.log('received', data)
                    this.modelTableData = data
                    this.$store.commit('modifyModels', { key: val.categoryID, value: data })
                }).catch(() => {})
            }
        },
        modelTableChoose(val) {
            this.modelTableCurrentRow = [val]
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []

            this.$getRequest(this.$api.fullSkuByModel +
                encodeURI(val.modelID)).then((data) => {
                console.log('received', data)
                this.skuTableData = data
            }).catch(() => {})
        },
        skuTableChoose(val) {
            this.skuTableCurrentRow = [val]
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []

            this.$getRequest(this.$api.warehouseStockBySKu +
                encodeURI(val.skuID)).then((data) => {
                console.log('received', data)
                this.warehouseStockTableData = data
            }).catch(() => {})
        },
        chooseHandle() {
            //check if sku is chosen
            if (this.modelTableCurrentRow.length !== 0 &&
                    this.skuTableCurrentRow.length !== 0) {

                let stockQuantity = 0, warehouseStockID = -1, stockUnitPrice = 0.0
                for (let item of this.warehouseStockTableData) {
                    if (item.warehouseID === this.warehouseID) {
                        stockQuantity = item.stockQuantity
                        warehouseStockID = item.warehouseStockID
                        stockUnitPrice = item.stockUnitPriceWithTax
                        break
                    }
                }

                this.$emit('modelSearchChoose', {
                    skuID: this.skuTableCurrentRow[0].skuID,
                    newCode: this.skuTableCurrentRow[0].newCode,
                    oldCode: this.skuTableCurrentRow[0].oldCode,
                    unitName: this.skuTableCurrentRow[0].unitName,
                    factoryCode: this.skuTableCurrentRow[0].factoryCode,
                    quantity: 0,
                    stockQuantity: stockQuantity,
                    remark: '',
                    warehouseStockID: warehouseStockID,
                    warehouseID: this.warehouseID,
                    taxRate: 0.16, //todo
                    unitPriceWithoutTax: 0.0,
                    unitPriceWithTax: 0.0,
                    stockUnitPrice: stockUnitPrice,
                    //statistic fields
                    totalWithoutTax: 0.0,
                    totalTax: 0.0
                })
                this.modelTableCurrentRow = []
                this.skuTableCurrentRow = []
            }
        }
    }
}
</script>

<style scoped lang="sass">

</style>

