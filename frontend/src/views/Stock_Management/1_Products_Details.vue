<template>
<!--  <p>库存管理</p>-->
<!--  <p>商品明细</p>-->
    <v-card>
        <v-card-title class="d-flex">
            明细统计
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row>
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
                                   dense
                                   row>
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
                <v-col cols="auto">
                    <v-btn class="mr-8"
                           color="accent"
                           @click="productDetail">
                        商品明细
                    </v-btn>
                </v-col>
            </v-row>
            <v-row class="d-flex">

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
                                </v-data-table>
                            </v-responsive>
                        </v-card>
                    </div>

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

                </div>

            </v-row>

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
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Detail_Stats",
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,

            modelCode: '',
            modelSearchName: '',
            modelSearchMethod: 'prefix',

            treeData: [],

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

            warehouseStockTableHeaders: [
                {text: '仓库', value: 'warehouseName', width: '120px'},
                {text: '架位', value: '', width: '120px'},
                {text: '库存数量', value: 'stockQuantity', width: '120px'},
                {text: '库存无税价', value: 'stockUnitPriceWithoutTax', width: '120px'}
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
                { text: '出入库单号', value: 'entryID', width: '150px' },
                { text: '摘要', value: '', width: '120px' },
                { text: '入库数量', value: 'quantity', width: '80px' },
                { text: '无税价', value: 'unitPriceWithoutTax', width: '80px' },
                { text: '类型', value: 'classification', width: '80px' },
            ],
            detailTableData: [],
        }
    },
    beforeMount() {
        let result = this.$store.getters.productList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.modelCategories).then((data) => {
            console.log('received', data)
            this.treeData = this.$createTree(data)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})
    },
    methods: {
        modelSearch() {
            this.$getRequest(this.$api.modelsByName, {
                name: this.modelSearchName,
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

            this.$getRequest(this.$api.productsByWarehouseStock +
                encodeURI(val.warehouseStockID)).then((data) => {
                console.log('received', data)
                this.detailTableData = data
            }).catch(() => {})
        },
        productDetail() {

        },
    }
}
</script>

<style scoped>

</style>
