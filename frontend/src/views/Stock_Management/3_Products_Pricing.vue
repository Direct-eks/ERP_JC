<template>
<!--  <p>库存管理</p>-->
<!--  <p>商品定价</p>-->
    <v-card>
        <v-card-title>
            商品定价
            <v-text-field v-model="treeSelection.label"
                          label="已选分类"
                          hide-details="auto"
                          outlined
                          readonly
                          dense
                          class="ml-4"
                          style="max-width: 180px">
            </v-text-field>
            <v-text-field v-model="factoryBrandSelected"
                          label="已选厂牌"
                          hide-details="auto"
                          outlined
                          readonly
                          dense
                          class="ml-2"
                          style="max-width: 100px">
            </v-text-field>
            <v-btn class="ml-2"
                   color="warning"
                   @click="clearSearchFields">
                清空
            </v-btn>
            <v-btn class="ml-2"
                   color="accent"
                   :loading="isQuerying"
                   @click="searchSku">
                查询
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn class="mr-4"
                   color="success"
                   @click="saveChanges">
                保存修改
            </v-btn>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <div class="d-flex" v-if="!isHiding">
            <v-card outlined>
                <ModelTree height="65vh" max-width=""
                           :show-select="true"
                           :select-for-search="false"
                           @treeSelectionObject="treeSelect">
                </ModelTree>
            </v-card>
            <v-card outlined>
                <v-responsive height="30vh" width="20vw"
                              style="overflow: auto">
                    <v-data-table v-model="factoryBrandCurrentRow"
                                  :headers="factoryBrandTableHeaders"
                                  :items="factoryBrandTableData"
                                  item-key="factoryBrandID"
                                  height="30vh"
                                  calculate-widths
                                  disable-sort
                                  disable-pagination
                                  single-select
                                  @click:row="factoryBrandSelect"
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-responsive>
            </v-card>
            <v-card outlined height="30vh">
                <v-responsive width="45vw"
                              style="overflow: auto">
                    <v-data-table v-model="supplierTableCurrentRow"
                                  :headers="supplierTableHeaders"
                                  :items="supplierTableData"
                                  item-key="supplierID"
                                  height="23vh"
                                  calculate-widths
                                  disable-sort
                                  single-select
                                  @click:row="data => this.supplierTableCurrentRow = [data]"
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-responsive>
                <v-card-actions  class="d-flex">
                    <v-btn class="ml-4"
                           color="accent"
                           @click="importPrice">
                        导入
                    </v-btn>
                    <v-dialog v-model="supplierDialog"
                              :eager="true"
                              persistent
                              scrollable
                              no-click-animation
                              max-width="55vw">
                        <template v-slot:activator="{on}">
                            <v-btn class="ml-4"
                                   color="accent"
                                   v-on="on">
                                送资源价格
                            </v-btn>
                        </template>
                        <SupplierResourceQuery @supplierChoose="importSupplierResource">
                        </SupplierResourceQuery>
                    </v-dialog>
                </v-card-actions>
            </v-card>
        </div>

        <v-data-table v-model="tableCurrentRow"
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="skuID"
                      :loading="isQuerying"
                      calculate-widths
                      :height="tableHeight"
                      disable-sort
                      show-select
                      single-select
                      @click:row="tableSelect"
                      @item-selected="tableSelect2"
                      fixed-header
                      locale="zh-cn"
                      :footer-props="{'items-per-page-options': [10,20,30]}">
            <template v-slot:item.index="{ item }">
                {{tableData.indexOf(item) + 1}}
            </template>
            <template v-slot:item.factoryPriceWithoutTax="{ item }">
                <v-edit-dialog :return-value="item.factoryPriceWithoutTax"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.factoryPriceWithoutTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.factoryPriceWithoutTax" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.factoryPriceWithTax="{ item }">
                <v-edit-dialog :return-value="item.factoryPriceWithTax"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.factoryPriceWithTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.factoryPriceWithTax" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.settlementPriceWithoutTax="{ item }">
                <v-edit-dialog :return-value="item.settlementPriceWithoutTax"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.settlementPriceWithoutTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.settlementPriceWithoutTax" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.priceBaseReference="{ item }">
                <v-select v-model="item.priceBaseReference" :items="priceBaseOptions"
                          hide-details="auto" dense style="width: 120px"
                          @change="saveEditing(item)"/>
            </template>
            <template v-slot:item.wholesalePriceDiscount="{ item }">
                <v-edit-dialog :return-value="item.wholesalePriceDiscount"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.wholesalePriceDiscount}}
                    <template v-slot:input>
                        <v-text-field v-model="item.wholesalePriceDiscount" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.wholesalePrice="{ item }">
                <v-edit-dialog :return-value="item.wholesalePrice"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.wholesalePrice}}
                    <template v-slot:input>
                        <v-text-field v-model="item.wholesalePrice" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.retailPriceDiscount="{ item }">
                <v-edit-dialog :return-value="item.retailPriceDiscount"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.retailPriceDiscount}}
                    <template v-slot:input>
                        <v-text-field v-model="item.retailPriceDiscount" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.retailPrice="{ item }">
                <v-edit-dialog :return-value="item.retailPrice"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.retailPrice}}
                    <template v-slot:input>
                        <v-text-field v-model="item.retailPrice" single-line/>
                    </template>
                </v-edit-dialog>
            </template>

            <template v-slot:footer.prepend>
                <v-btn color="accent"
                       @click="hideTop">
                    隐藏上半部分
                </v-btn>
            </template>
        </v-data-table>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Product_Pricing",
    components: {
        ModelTree: () => import('~/components/ModelTree'),
        SupplierResourceQuery: () => import("~/components/SupplierResourceQuery")
    },
    beforeMount() {
        this.$getRequest(this.$api.allFactoryBrands).then((data) => {
            console.log('received', data)
            this.factoryBrandTableData = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,
            supplierDialog: false,
            isQuerying: false,

            treeSelection: {label: '', categoryID: -1, children: []},

            factoryBrandTableHeaders: [
                { text: '厂牌代号', value: 'code', width: '80px' },
                { text: '厂牌描述', value: 'remark', width: '100px' },
            ],
            factoryBrandTableData: [],
            factoryBrandCurrentRow: [],
            factoryBrandSelected: '',

            supplierTableHeaders: [
                { text: '供应商简称', value: 'supplierAbbreviatedName', width: '100px' },
                { text: '厂牌', value: 'factoryCode', width: '80px' },
                { text: '含税厂价', value: 'factoryPriceWithTax', width: '65px' },
                { text: '下浮率', value: 'floatDownRate', width: '100px' },
                { text: '无税结算价', value: 'settlementPriceWithoutTax', width: '80px' },
                { text: '数量', value: 'quantity', width: '65px' },
                { text: '无税厂价', value: 'factoryPriceWithoutTax', width: '65px' },
            ],
            supplierTableData: [],
            supplierTableCurrentRow: [],

            tableHeaders: [
                { text: '序号', value: 'index', width: '65px' },
                { text: '商品分类', value: '', width: '80px' },
                { text: '代号', value: 'code', width: '120px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '无税厂价', value: 'factoryPriceWithoutTax', width: '80px' },
                { text: '含税厂价', value: 'factoryPriceWithTax', width: '80px' },
                { text: '无税结算价', value: 'settlementPriceWithoutTax', width: '95px' },
                { text: '加价基准', value: 'priceBaseReference', width: '80px' },
                { text: '批加', value: 'wholesalePriceDiscount', width: '65px' },
                { text: '批发价', value: 'wholesalePrice', width: '80px' },
                { text: '零加', value: 'retailPriceDiscount', width: '65px' },
                { text: '零售价', value: 'retailPrice', width: '80px' },
                { text: '数量', value: 'stockQuantity', width: '65px' },
                { text: '下限', value: 'stockLowerLimit', width: '65px' },
                { text: '上限', value: 'stockUpperLimit', width: '65px' },
            ],
            tableData: [],
            tableCurrentRow: [],
            modifiedTableData: [],
            tableHeight: '40vh',
            isHiding: false,

            priceBaseOptions: [
                '无税厂价',
                '含税厂价',
                '无税结算价'
            ]
        }
    },
    methods: {
        saveChanges() {
            this.$postRequest(this.$api.modifySkuPricing, {
                elements: this.modifiedTableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/stock_management')
            }).catch(() => {})
        },
        treeSelect(data) {
            this.treeSelection = data
        },
        factoryBrandSelect(data) {
            this.factoryBrandCurrentRow = [data]
            this.factoryBrandSelected = data.code
        },
        clearSearchFields() {
            this.treeSelection = []
            this.factoryBrandCurrentRow = []
            this.factoryBrandSelected = ''
            this.supplierTableData = []
            this.supplierTableCurrentRow = []
            this.tableData = []
            this.tableCurrentRow = []
            this.modifiedTableData = []
        },
        searchSku() {
            if (this.treeSelection.label === '') return
            this.isQuerying = true
            this.$getRequest(this.$api.skuByCategoryAndFactoryBrand, {
                modelCategoryID: this.treeSelection[0].categoryID,
                factoryBrandID: this.factoryBrandCurrentRow.length === 0 ? -1 :
                    this.factoryBrandCurrentRow[0].factoryBrandID
            }).then((data) => {
                console.log('received', data)
                this.tableData = data
                this.isQuerying = false
            }).catch(() => {})
        },
        tableSelect(row) {
            this.tableCurrentRow = [row]
            this.supplierTableData = []
            this.supplierTableCurrentRow = []

            this.$getRequest(this.$api.supplierResourcesBySku +
                encodeURI(row.skuID)).then(data => {
                console.log('received', data)
                this.supplierTableData = data
            })
        },
        tableSelect2(row) {
            if (!row.value) {
                this.tableCurrentRow = []
            }
            else {
                this.tableCurrentRow = [row.item]
                this.$getRequest(this.$api.supplierResourcesBySku +
                    encodeURI(row.item.skuID)).then(data => {
                    console.log('received', data)
                    this.supplierTableData = data
                })
            }
            this.supplierTableData = []
            this.supplierTableCurrentRow = []
        },
        hideTop() {
            if (this.isHiding) {
                this.isHiding = false
                this.tableHeight = '40vh'
            }
            else {
                this.isHiding = true
                this.tableHeight = '75vh'
            }
        },
        saveEditing(item) {
            // todo validate fields
            if (this.modifiedTableData.indexOf(item) === -1) {
                this.modifiedTableData.push(item)
            }
            console.log(this.modifiedTableData)
        },
        importPrice() {
            if (this.supplierTableCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '未选中供应商', color: 'warning'
                })
                return
            }
            let resource = this.supplierTableCurrentRow[0]
            let sku = this.tableCurrentRow[0]
            sku.factoryPriceWithoutTax = resource.factoryPriceWithoutTax
            sku.factoryPriceWithTax = resource.factoryPriceWithTax
            sku.settlementPriceWithoutTax = resource.settlementPriceWithoutTax
            this.saveEditing(sku)
        },
        importSupplierResource(data) {
            if (data) {
                // match resources with current table rows
                for (const sku of this.tableData) {
                    for (const resource of data.resources) {
                        if (resource.skuID === sku.skuID) {
                            sku.factoryPriceWithoutTax = resource.factoryPriceWithoutTax
                            sku.factoryPriceWithTax = resource.factoryPriceWithTax
                            sku.settlementPriceWithoutTax = resource.settlementPriceWithoutTax
                            this.saveEditing(sku)
                            break
                        }
                    }
                }
            }
            this.supplierDialog = false
        }
    },
}
</script>

<style scoped>

</style>
