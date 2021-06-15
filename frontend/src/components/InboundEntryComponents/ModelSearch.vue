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
                    <v-icon>{{mdiClosePath}}</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text class="d-flex">
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

                <div class="d-flex">
                    <v-card outlined>
                        <v-data-table v-model="skuTableCurrentRow"
                                      :headers="skuTableHeaders"
                                      :items="skuTableData"
                                      item-key="skuID"
                                      @click:row="skuTableChoose"
                                      @item-selected="skuTableChoose2"
                                      height="20vh"
                                      calculate-widths
                                      disable-sort
                                      single-select
                                      show-select
                                      fixed-header
                                      checkbox-color="accent"
                                      hide-default-footer
                                      locale="zh-cn"
                                      dense>
                        </v-data-table>
                    </v-card>
                    <v-card outlined>
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
                    </v-card>
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
            modelSearchMethod: 'prefix',

            treeData: [],

            modelTableHeaders: [
                {text: '分类', value: 'categoryCode', width: '110px'},
                {text: '代号', value: 'code', width: '200px'},
            ],
            modelTableData: [],
            modelTableCurrentRow: [],

            skuTableHeaders: [
                {text: '生产厂', value: 'factoryCode', width: '80px'}
            ],
            skuTableData: [],
            skuTableCurrentRow: [],

            warehouseStockTableHeaders: [
                {text: '仓库', value: 'warehouseName', width: '90px'},
                {text: '架位', value: '', width: '100px'},
                {text: '库存数量', value: 'stockQuantity', width: '90px'},
                {text: '库存无税价', value: 'stockUnitPriceWithoutTax', width: '100px'}
            ],
            warehouseStockTableData: [],
        }
    },
    beforeMount() {
        let result = this.$store.getters.productList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.modelCategories).then((data) => {
            this.treeData = this.$createTree(data, true)
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
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.warehouseStockTableData = []

            this.$getRequest(this.$api.modelsByName, {
                name: this.modelSearchName,
                method: this.modelSearchMethod
            }).then((data) => {
                this.modelTableData = data
            }).catch(() => {})
        },
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.warehouseStockTableData = []

            let val = data[0]
            if (val.children.length === 0) { // end node
                let result = this.$store.getters.models(val.categoryID)
                if (result) {
                    this.modelTableData = result
                    return
                }
                this.$getRequest(this.$api.modelsByCategory +
                        encodeURI(val.categoryID)).then((data) => {
                    this.modelTableData = data
                    this.$store.commit('modifyModels', { key: val.categoryID, value: data })
                }).catch(() => {})
            }
        },
        modelTableChoose(val) {
            this.skuTableData = []
            this.skuTableCurrentRow = []
            this.warehouseStockTableData = []

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
            this.skuTableCurrentRow = []
            this.warehouseStockTableData = []

            if (!row.value) {
                this.modelTableCurrentRow = []
            }
            else {
                this.modelTableChoose(row.item)
            }
        },
        skuTableChoose(val) {
            this.warehouseStockTableData = []

            if (this.skuTableCurrentRow.indexOf(val) !== -1) {
                this.skuTableCurrentRow = []
            }
            else {
                this.skuTableCurrentRow = [val]

                this.$getRequest(this.$api.warehouseStockBySKu +
                    encodeURI(val.skuID)).then((data) => {
                    this.warehouseStockTableData = data
                }).catch(() => {})
            }
        },
        skuTableChoose2(row) {
            this.warehouseStockTableData = []

            if (!row.value) {
                this.skuTableCurrentRow = []
            }
            else {
                this.skuTableChoose(row.item)
            }
        },
        chooseHandle() {
            //check if sku is chosen
            if (this.modelTableCurrentRow.length === 0 ||
                    this.skuTableCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '请选择型号与厂牌', color: 'warning'
                })
            }
            else {
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
                    code: this.skuTableCurrentRow[0].code,
                    unitName: this.skuTableCurrentRow[0].unitName,
                    factoryCode: this.skuTableCurrentRow[0].factoryCode,
                    quantity: '0',
                    stockQuantity: stockQuantity,
                    remark: '',
                    warehouseStockID: warehouseStockID,
                    warehouseID: this.warehouseID,
                    taxRate: '',
                    unitPriceWithoutTax: '0',
                    unitPriceWithTax: '0',
                    stockUnitPrice: stockUnitPrice,
                    //statistic fields
                    totalWithoutTax: '',
                    totalTax: ''
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

