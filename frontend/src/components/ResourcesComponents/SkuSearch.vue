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
                                  :search="modelCode"
                                  height="65vh"
                                  hide-default-footer
                                  calculate-widths
                                  disable-sort
                                  disable-pagination
                                  single-select
                                  show-select
                                  @click:row="modelTableChoose"
                                  @item-selected="modelTableChoose2"
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-card>

                <div class="d-flex flex-column">
                    <v-card outlined>
                        <v-responsive max-height="25vh" style="overflow: auto">
                            <v-data-table v-model="skuTableCurrentRow"
                                          :headers="skuTableHeaders"
                                          :items="skuTableData"
                                          item-key="skuID"
                                          height="20vh"
                                          calculate-widths
                                          disable-sort
                                          single-select
                                          show-select
                                          @click:row="skuTableChoose"
                                          @item-selected="skuTableChoose2"
                                          fixed-header
                                          hide-default-footer
                                          locale="zh-cn"
                                          dense>
                            </v-data-table>
                        </v-responsive>
                    </v-card>
                </div>
            </div>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiClose} from "@mdi/js";

export default {
    name: "SkuSearch",
    beforeMount() {
        let result = this.$store.getters.productList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.modelCategories).then((data) => {
            console.log('received', data)
            this.treeData = this.$createTree(data, true)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})
    },
    props: {
        supplierID: { type: Number, required: true, default: -1 }
    },
    data() {
        return {
            mdiClosePath: mdiClose,

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
        }
    },
    methods: {
        close() {
            this.$emit('skuSearchChoose')
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
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table

            if (data.length === 0) return

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
            this.modelTableCurrentRow = [val]
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table

            this.$getRequest(this.$api.fullSkuByModel +
                encodeURI(val.modelID)).then((data) => {
                this.skuTableData = data
            }).catch(() => {})
        },
        modelTableChoose2(row) {
            this.skuTableData = []
            this.skuTableCurrentRow = []
            if (!row.value) {
                this.modelTableCurrentRow.splice(this.modelTableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.modelTableCurrentRow.push(row.item)
                this.$getRequest(this.$api.fullSkuByModel +
                    encodeURI(row.item.modelID)).then((data) => {
                    this.skuTableData = data
                }).catch(() => {})
            }
        },
        skuTableChoose(val) {
            this.skuTableCurrentRow = [val]
        },
        skuTableChoose2(row) {
            if (!row.value) {
                this.skuTableCurrentRow.splice(this.skuTableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.skuTableCurrentRow.push(row.item)
            }
        },
        chooseHandle() {
            //check if sku is chosen
            if (this.modelTableCurrentRow.length !== 0 &&
                this.skuTableCurrentRow.length !== 0 && this.supplierID !== -1) {

                this.$getRequest(this.$api.supplierResourcesBySku +
                    encodeURI(String(this.supplierID))).then(data => {
                    console.log(data)
                    const sku = {
                        supplierID: 0,
                        skuID: this.skuTableCurrentRow[0].skuID,
                        code: this.skuTableCurrentRow[0].code,
                        factoryCode: this.skuTableCurrentRow[0].factoryCode,
                        quantityRecord: '',
                        factoryPriceWithoutTax: '0',
                        factoryPriceWithTax: '0',
                        floatDownRate: '0',
                        settlementPriceWithoutTax: '0',
                        quantityPerBox: '0',
                        remark: '',
                        history: '',
                        quoteDate: ''
                    }
                    for (const item of data) {
                        if (item.supplierID === this.supplierID) {
                            Object.assign(sku, item)
                            break
                        }
                    }
                    this.$emit('skuSearchChoose', sku)
                    this.modelTableCurrentRow = []
                    this.skuTableCurrentRow = []
                })
            }
        }
    }
}
</script>

<style scoped>

</style>
