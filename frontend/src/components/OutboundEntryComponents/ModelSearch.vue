<template>
    <v-card>
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
                <v-radio-group v-model="modelSearchMethod"
                               hide-details="auto"
                               class="mt-0"
                               dense>
                    <v-radio label="前匹配" value="prefix"></v-radio>
                    <v-radio label="模糊" value="infix"></v-radio>
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
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <div class="d-flex">
                <v-card outlined>
                    <ModelTree height="65vh" max-width="230px"
                               @treeSelectionResult="treeSelectionResult">
                    </ModelTree>
                </v-card>
                <v-card outlined>
                    <v-data-table class="flex-shrink-1"
                                  v-model="modelTableCurrentRow"
                                  :headers="modelTableHeaders"
                                  :items="modelTableData"
                                  item-key="modelID"
                                  @click:row="modelTableChoose"
                                  @item-selected="modelTableChoose2"
                                  :search="modelCode"
                                  height="65vh"
                                  hide-default-footer
                                  calculate-widths
                                  disable-sort
                                  disable-pagination
                                  single-select
                                  show-select
                                  checkbox-color="accent"
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
                                          @item-selected="skuTableChoose2"
                                          height="30vh"
                                          calculate-widths
                                          disable-sort
                                          single-select
                                          show-select
                                          checkbox-color="accent"
                                          fixed-header
                                          hide-default-footer
                                          locale="zh-cn"
                                          dense>
                            </v-data-table>
                        </v-card>
                        <v-card outlined>
                            <v-responsive height="30vh" max-width="55vw"
                                          style="overflow: auto">
                                <v-data-table :headers="warehouseStockTableHeaders"
                                              :items="warehouseStockTableData"
                                              item-key="warehouseStockID"
                                              height="20vh"
                                              calculate-widths
                                              disable-sort
                                              fixed-header
                                              hide-default-footer
                                              locale="zh-cn"
                                              dense>
                                </v-data-table>
                            </v-responsive>
                        </v-card>
                        <v-card outlined>
                            <v-row dense class="mt-1">
                                <v-col cols="auto">
                                    <v-text-field v-model="currSku.wholesalePrice"
                                                  label="批发价"
                                                  hide-details="auto"
                                                  outlined
                                                  readonly
                                                  dense
                                                  style="width: 150px">
                                    </v-text-field>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="currSku.wholesalePriceDiscount"
                                                  label="批发折扣"
                                                  @change="handleWholesaleDiscountChange"
                                                  type="Number"
                                                  hide-details="auto"
                                                  outlined
                                                  :readonly="!editPermitted"
                                                  dense
                                                  style="width: 100px">
                                    </v-text-field>
                                </v-col>
                            </v-row>
                            <v-row dense>
                                <v-col cols="auto">
                                    <v-text-field v-model="currSku.retailPrice"
                                                  label="零售价"
                                                  hide-details="auto"
                                                  outlined
                                                  readonly
                                                  dense
                                                  style="width: 150px">
                                    </v-text-field>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="currSku.retailPriceDiscount"
                                                  label="零售折扣"
                                                  @change="handleRetailDiscountChange"
                                                  type="Number"
                                                  hide-details="auto"
                                                  outlined
                                                  :readonly="!editPermitted"
                                                  dense
                                                  style="width: 100px">
                                    </v-text-field>
                                </v-col>
                            </v-row>
                            <v-row dense>
                                <v-col cols="auto">
                                    <v-text-field v-model="currSku.unitPriceWithoutTax"
                                                  label="销售价格"
                                                  @focus="$event.target.setSelectionRange(0, 100)"
                                                  @change="handlePriceChange"
                                                  hide-details="auto"
                                                  outlined
                                                  :readonly="!editPermitted"
                                                  dense
                                                  style="width: 150px">
                                    </v-text-field>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="currSku.quantity"
                                                  label="销售数量"
                                                  type="Number"
                                                  hide-details="auto"
                                                  outlined
                                                  dense
                                                  style="width: 100px">
                                    </v-text-field>
                                </v-col>
                            </v-row>
                        </v-card>
                    </div>

<!--                    <v-card outlined class="text-h6">-->
<!--                        <p>{{flattenedTreePosition}}</p>-->
<!--                    </v-card>-->
                    <v-card outlined>
                        <v-data-table :headers="resourceTableHeader"
                                      :items="resourceTableData"
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

export default {
    name: "ModelSearch",
    components: {
        ModelTree: () => import('~/components/ModelTree'),
    },
    props: {
        warehouseID: {type: Number, required: true, default: -1}
    },
    beforeMount() {
        this.emptySku = Object.assign(this.emptySku, this.currSku)
        this.editPermitted = this.$store.getters.currentUserIsPermitted('inboundEntry:Creation:changePrice')
    },
    data() {
        return {
            mdiClose,

            modelCode: '',
            modelSearchName: '',
            modelSearchMethod: 'prefix',
            flattenedTreePosition: '',

            editPermitted: false,

            modelTableHeaders: [
                {text: '代号', value: 'code', width: '180px'},
            ],
            modelTableData: [],
            modelTableCurrentRow: [],

            skuTableHeaders: [
                {text: '生产厂', value: 'factoryCode', width: '80px'}
            ],
            skuTableData: [],
            skuTableCurrentRow: [],
            currSku: {
                wholesalePrice: '', wholesalePriceDiscount: '',
                retailPrice: '', retailPriceDiscount: '',
                unitPriceWithoutTax: '', quantity: 0,
            },
            emptySku: {},

            warehouseStockTableHeaders: [
                {text: '仓库', value: 'warehouseName', width: '75px'},
                // {text: '架位', value: 'storagePlaceID', width: '80px'},
                {text: '库存数', value: 'stockQuantity', width: '80px'},
            ],
            warehouseStockTableData: [],
            // warehouseStockCurrentRow: [],

            resourceTableHeader: [
                { text: '摘要', value: '', width: '' },
                { text: '入库数量', value: '', width: '' },
                { text: '无税价', value: '', width: '' },
                { text: '类型', value: '', width: '' },
            ],
            resourceTableData: [],

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
    methods: {
        close() {
            this.$emit('modelSearchClose')
        },
        productDetail() {

        },
        modelSearch() {
            this.$getRequest(this.$api.modelsByName, {
                name: this.modelSearchName,
                method: this.modelSearchMethod
            }).then((data) => {
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
        treeSelectionResult(data) {
            this.modelTableData = data
            this.modelTableCurrentRow = [] // reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] // reset stock table
            this.currSku = Object.assign(this.currSku, this.emptySku)
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []
            this.resourceTableData = []
        },
        modelTableChoose(val) {
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.currSku = Object.assign(this.currSku, this.emptySku)
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []
            this.resourceTableData = []

            if (this.modelTableCurrentRow.indexOf(val) !== -1) {
                this.modelTableCurrentRow = []
            }
            else {
                this.modelTableCurrentRow = [val]
                this.$getRequest(this.$api.fullSkuByModel +
                    encodeURI(val.modelID)).then((data) => {
                    this.skuTableData = data
                }).catch(() => {})
            }
        },
        modelTableChoose2(row) {
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.currSku = Object.assign(this.currSku, this.emptySku)
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []
            this.resourceTableData = []

            if (!row.value) {
                this.modelTableCurrentRow = []
            }
            else {
                this.modelTableChoose(row.item)
            }
        },
        skuTableChoose(val) {
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []
            this.resourceTableData = []

            if (this.skuTableCurrentRow.indexOf(val) !== -1) {
                this.skuTableCurrentRow = []
                this.currSku = Object.assign(this.currSku, this.emptySku)
            }
            else {
                this.skuTableCurrentRow = [val]
                this.currSku = Object.assign(this.currSku, val)

                this.$getRequest(this.$api.warehouseStockBySKu +
                    encodeURI(val.skuID)).then((data) => {
                    this.warehouseStockTableData = data
                }).catch(() => {})
            }
        },
        skuTableChoose2(row) {
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []
            this.resourceTableData = []

            if (!row.value) {
                this.skuTableCurrentRow = []
                this.currSku = Object.assign(this.currSku, this.emptySku)
            }
            else {
                this.skuTableChoose(row.item)
            }
        },
        // warehouseStockTableChoose(val) {
        //     this.resourceTableData = []
        //
        //     if (this.warehouseStockCurrentRow.indexOf(val) !== -1) {
        //         this.warehouseStockCurrentRow = []
        //     }
        //     else {
        //         this.warehouseStockCurrentRow = [val]
        //     }
        // },
        // warehouseStockTableChoose2(row) {
        //     this.resourceTableData = []
        //
        //     if (!row.value) {
        //         this.warehouseStockCurrentRow = []
        //     }
        //     else {
        //         this.warehouseStockCurrentRow = [row.item]
        //     }
        // },
        handleWholesaleDiscountChange(value) {
            if (value === '') return
            const discount = 100 - value
            const wholesalePrice = this.$Big(this.currSku.wholesalePrice)
            this.currSku.unitPriceWithoutTax = wholesalePrice.times(this.$Big(discount).div(100))
        },
        handleRetailDiscountChange(value) {
            if (value === '') return
            const discount = 100 - value
            const retailPrice = this.$Big(this.currSku.retailPrice)
            this.currSku.unitPriceWithoutTax = retailPrice.times(this.$Big(discount).div(100))
        },
        handlePriceChange(value) {
            if (value === '') return
            this.currSku.unitPriceWithoutTax = this.$validateFloat(value)
        },
        chooseHandle() {
            //check if sku is chosen
            if (this.modelTableCurrentRow.length === 0 ||
                    this.skuTableCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '请选择型号与厂牌', color: 'warning'
                })
                return
            }

            let found = false
            let stockQuantity = 0, warehouseStockID = -1, stockUnitPrice = '0'

            for (const item of this.warehouseStockTableData) {
                if (item.warehouseID === this.warehouseID) {
                    found = true
                    stockQuantity = item.stockQuantity
                    warehouseStockID = item.warehouseStockID
                    stockUnitPrice = item.stockUnitPriceWithTax
                    break
                }
            }
            if (!found) {
                this.$store.commit('setSnackbar', {
                    message: '出库仓库无所选商品库存记录', color: 'warning'
                })
                return
            }

            this.$emit('modelSearchChoose', {
                skuID: this.skuTableCurrentRow[0].skuID,
                code: this.skuTableCurrentRow[0].code,
                unitName: this.skuTableCurrentRow[0].unitName,
                factoryCode: this.skuTableCurrentRow[0].factoryCode,
                quantity: this.currSku.quantity === '' ? '0' : this.currSku.quantity,
                stockQuantity: stockQuantity,
                remark: '',
                warehouseStockID: warehouseStockID,
                warehouseID: this.warehouseID,
                taxRate: 0,
                unitPriceWithoutTax: this.currSku.unitPriceWithoutTax === '' ? '0' : this.currSku.unitPriceWithoutTax,
                unitPriceWithTax: '0',
                stockUnitPrice: stockUnitPrice,
                //statistic fields
                totalWithoutTax: '',
                totalTax: '',
            })
        }
    }
}
</script>

<style scoped>

</style>
