<template>
    <v-card>
        <v-card-title class="d-flex">
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
            <div class="text-body-1">
                折扣价基准
            </div>
            <v-radio-group v-model="discountBase"
                           hide-details="auto"
                           class="mt-0 ml-2"
                           dense>
                <v-radio label="批发价" value="批发价"></v-radio>
                <v-radio label="零售价" value="零售价"></v-radio>
            </v-radio-group>
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
                    <v-responsive height="65vh" max-width="230px"
                                  style="overflow: auto">
                        <v-treeview :items="treeData"
                                    item-text="label"
                                    item-key="categoryID"
                                    activatable
                                    return-object
                                    @update:active="treeSelect"
                                    color="primary"
                                    open-on-click
                                    hoverable
                                    dense>
                        </v-treeview>
                    </v-responsive>
                </v-card>

                <v-card outlined>
                    <v-data-table class="flex-shrink-1"
                                  v-model="modelTableCurrentRow"
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
                            <v-data-table v-model="skuTableCurrentRow"
                                          :headers="skuTableHeaders"
                                          :items="skuTableData"
                                          item-key="skuID"
                                          @click:row="skuTableChoose"
                                          height="30vh"
                                          calculate-widths
                                          disable-sort
                                          single-select
                                          fixed-header
                                          hide-default-footer
                                          locale="zh-cn"
                                          dense>
                            </v-data-table>
                        </v-card>
                        <v-card outlined>
                            <v-responsive height="30vh" max-width="55vw"
                                          style="overflow: auto">
                                <v-data-table v-model="warehouseStockCurrentRow"
                                              :headers="warehouseStockTableHeaders"
                                              :items="warehouseStockTableData"
                                              item-key="warehouseStockID"
                                              @click:row="warehouseStockTableChoose"
                                              height="20vh"
                                              calculate-widths
                                              disable-sort
                                              single-select
                                              fixed-header
                                              hide-default-footer
                                              locale="zh-cn"
                                              dense>
                                    <template v-slot:item.wholesalePriceDiscount="{ item }">
                                        <v-edit-dialog :return-value.sync="item.wholesalePriceDiscount"
                                                       persistent
                                                       large
                                                       save-text="确认"
                                                       cancel-text="取消"
                                                       @save="handleDiscountChange(item)">
                                            {{item.wholesalePriceDiscount}}
                                            <template v-slot:input>
                                                <v-text-field v-model="item.wholesalePriceDiscount"
                                                              single-line
                                                              counter="8">
                                                </v-text-field>
                                            </template>
                                        </v-edit-dialog>
                                    </template>
                                    <template v-slot:item.retailPriceDiscount="{ item }">
                                        <v-edit-dialog :return-value.sync="item.retailPriceDiscount"
                                                       persistent
                                                       large
                                                       save-text="确认"
                                                       cancel-text="取消"
                                                       @save="handleDiscountChange(item)">
                                            {{item.retailPriceDiscount}}
                                            <template v-slot:input>
                                                <v-text-field v-model="item.retailPriceDiscount"
                                                              single-line
                                                              counter="8">
                                                </v-text-field>
                                            </template>
                                        </v-edit-dialog>
                                    </template>
                                    <template v-slot:item.unitPriceWithTax="{ item }">
                                        <v-edit-dialog :return-value.sync="item.unitPriceWithTax"
                                                       persistent
                                                       large
                                                       save-text="确认"
                                                       cancel-text="取消"
                                                       @save="handlePriceChange(item)">
                                            {{item.unitPriceWithTax}}
                                            <template v-slot:input>
                                                <v-text-field v-model="item.unitPriceWithTax"
                                                              single-line
                                                              counter="20">
                                                </v-text-field>
                                            </template>
                                        </v-edit-dialog>
                                    </template>
                                    <template v-slot:item.quantity="{ item }">
                                        <v-edit-dialog :return-value.sync="item.quantity"
                                                       persistent
                                                       large
                                                       save-text="确认"
                                                       cancel-text="取消">
                                            {{item.quantity}}
                                            <template v-slot:input>
                                                <v-text-field v-model="item.quantity"
                                                              single-line
                                                              counter="8">
                                                </v-text-field>
                                            </template>
                                        </v-edit-dialog>
                                    </template>
                                </v-data-table>
                            </v-responsive>
                        </v-card>
                    </div>

                    <v-card outlined class="text-h6">
                        {{flattenedTreePosition}}
                    </v-card>
                    <v-card outlined>
                        <v-data-table :headers="supplierTableHeaders"
                                      :items="supplierTableData"
                                      item-key=""
                                      height="20vh"
                                      calculate-widths
                                      disable-sort
                                      fixed-header
                                      hide-default-footer
                                      locale="zh-cn"
                                      dense>
                        </v-data-table>
                    </v-card>

                    <v-row>
                        <v-col cols="auto">
                            <v-btn class="mr-8"
                                   color="accent"
                                   @click="productDetail">
                                商品明细
                            </v-btn>
                        </v-col>
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

            <v-card outlined>
                    <v-data-table :headers="detailTableHeaders"
                                  :items="detailTableData"
                                  item-key=""
                                  height="20vh"
                                  calculate-widths
                                  disable-sort
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
            </v-card>>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

function validateFloat(value) {
    let val = value.replace(/[^\d.]/g, "") // 清除“数字”和“.”以外的字符
    val = val.replace(/\.{2,}/g, ".") // 只保留第一个. 清除多余的
    val = val.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".")
    val = val.replace(/^(-)*(\d+)\.(\d\d).*$/, '$1$2.$3') // 只能输入两个小数
    if (val.indexOf(".") < 0 && val !== "") { // 如果没有小数点，首位不能为0
        val = parseFloat(val)
    }
    console.log('float', val)
    return val
}

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
            modelSearchMethod: 'prefix',
            discountBase: '零售价',
            flattenedTreePosition: '',

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
                {text: '仓库', value: 'warehouseName', width: '75px'},
                // {text: '架位', value: 'storagePlaceID', width: '80px'},
                {text: '库存数', value: 'stockQuantity', width: '80px'},
                {text: '批发价', value: 'wholesalePrice', width: '80px'},
                {text: '折扣', value: 'wholesalePriceDiscount', width: '65px'},
                {text: '零售价', value: 'retailPrice', width: '80px'},
                {text: '折扣', value: 'retailPriceDiscount', width: '65px'},
                {text: '销售价', value: 'unitPriceWithTax', width: '80px'},
                {text: '销售数量', value: 'quantity', width: '85px'},
            ],
            warehouseStockTableData: [],
            warehouseStockCurrentRow: [],

            supplierTableHeaders: [
                { text: '摘要', value: '', width: '' },
                { text: '入库数量', value: '', width: '' },
                { text: '无税价', value: '', width: '' },
                { text: '类型', value: '', width: '' },
            ],
            supplierTableData: [],

            detailTableHeaders: [
                { text: '出入库单号', value: '', width: '' },
                { text: '摘要', value: '', width: '' },
                { text: '入库数量', value: '', width: '' },
                { text: '无税价', value: '', width: '' },
                { text: '类型', value: '', width: '' },
            ],
            detailTableData: [],
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
        productDetail() {

        },
        addNewHandle() {

        },
        modelSearch() {
            this.$getRequest(this.$api.modelsByName, {
                name: this.modelSearchName,
                method: this.modelSearchMethod
            }).then((data) => {
                console.log('received', data)
                this.modelTableData = data
            }).catch(() => {})
        },
        refreshFlattenedTreePosition(val) {
            let label = val.label
            let id = val.categoryID
            let nodes = []
            for (const item of this.treeData) {
                if (this.refreshFlattenedTreePositionHelper(item, id, nodes))
                    break
                nodes = []
            }

            this.flattenedTreePosition = ''
            nodes.forEach(node => this.flattenedTreePosition += node + ' > ')
            this.flattenedTreePosition += label
        },
        refreshFlattenedTreePositionHelper(root, id, nodes) {
            if (root.children.length === 0) return false
            nodes.push(root.label)
            for (const item of root.children) {
                if (item.categoryID === id) {
                    return true
                }
            }
            let bool = false
            for (const item of root.children) {
                if (this.refreshFlattenedTreePositionHelper(item, id, nodes)) {
                    bool = true
                }
            }
            if (bool === false) {
                nodes.pop()
            }
            return bool
        },
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []
            if (data.length === 0) return

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
            this.refreshFlattenedTreePosition(val)
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
                data.forEach(row => {
                    row.unitPriceWithTax = ''
                    row.quantity = ''
                })
                this.warehouseStockTableData = data
            }).catch(() => {})
        },
        warehouseStockTableChoose(val) {
            this.warehouseStockCurrentRow = [val]
        },
        handleDiscountChange(item) {
            let discount = 0.0
            switch (this.discountBase) {
            case '零售价':
                discount = Number(item.retailPriceDiscount) / 100
                item.unitPriceWithTax = (item.retailPrice * (1 - discount)).toFixed(2)
                break
            case '批发价':
                discount = Number(item.wholesalePriceDiscount) / 100
                item.unitPriceWithTax = (item.wholesalePrice * (1 - discount)).toFixed(2)
                break
            }
            console.log(item)
            console.log(this.warehouseStockTableData)
        },
        handlePriceChange(item) {
            item.unitPriceWithTax = validateFloat(item.unitPriceWithTax)
        },
        chooseHandle() {
            //check if sku is chosen
            if (this.modelTableCurrentRow.length !== 0 &&
                this.skuTableCurrentRow.length !== 0) {

                if (this.warehouseStockCurrentRow.length === 0 ||
                    this.warehouseStockCurrentRow[0].warehouseID !== this.warehouseID) {
                    this.$store.commit('setSnackbar', {
                        message: '选择的仓库与出库单仓库不符', color: 'warning'
                    })
                    return
                }

                let stockQuantity = 0, warehouseStockID = -1, stockUnitPrice = 0.0
                for (let item of this.warehouseStockTableData) {
                    if (item.warehouseID === this.warehouseID) {
                        stockQuantity = item.stockQuantity
                        warehouseStockID = item.warehouseStockID
                        stockUnitPrice = item.stockUnitPriceWithTax
                        break
                    }
                }

                const quantity = Number(this.warehouseStockCurrentRow[0].quantity)
                const unitPriceWithTax = Number(this.warehouseStockCurrentRow[0].unitPriceWithTax)
                const unitPriceWithoutTax = Number(this.warehouseStockCurrentRow[0].unitPriceWithTax) / 1.16
                const totalWithoutTax = unitPriceWithoutTax * quantity
                const totalTax = (unitPriceWithTax - unitPriceWithoutTax) * quantity
                this.$emit('modelSearchChoose', {
                    skuID: this.skuTableCurrentRow[0].skuID,
                    newCode: this.skuTableCurrentRow[0].newCode,
                    oldCode: this.skuTableCurrentRow[0].oldCode,
                    unitName: this.skuTableCurrentRow[0].unitName,
                    factoryCode: this.skuTableCurrentRow[0].factoryCode,
                    quantity: quantity,
                    stockQuantity: stockQuantity,
                    remark: '',
                    warehouseStockID: warehouseStockID,
                    warehouseID: this.warehouseID,
                    taxRate: 0.16, //todo
                    unitPriceWithoutTax: unitPriceWithoutTax.toFixed(2),
                    unitPriceWithTax: unitPriceWithTax.toFixed(2),
                    stockUnitPrice: stockUnitPrice,
                    //statistic fields
                    totalWithoutTax: totalWithoutTax.toFixed(2),
                    totalTax: totalTax.toFixed(2),
                })
                this.modelTableCurrentRow = []
                this.skuTableCurrentRow = []
                this.warehouseStockCurrentRow = []
            }
        }
    }
}
</script>

<style scoped>

</style>
