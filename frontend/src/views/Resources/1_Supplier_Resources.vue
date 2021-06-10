<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>供应商资源</p>-->
    <v-card>
        <v-card-title class="d-flex">
            供应商资源
            <v-spacer></v-spacer>
            <v-dialog v-model="resourceCompanySearchPanel"
                      persistent
                      scrollable
                      no-click-animation
                      max-width="45vw">
                <template v-slot:activator="{on}">
                    <v-btn color="primary" v-on="on"
                           :disabled="resourceCompanySearchPanel">
                        查所有资源单位
                    </v-btn>
                </template>
                <SupplierResourceQuery :enableEdit="true"
                                       @supplierChoose="resourceCompanySearchPanel = false">
                </SupplierResourceQuery>
            </v-dialog>

            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/resources">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <div class="d-flex">
                <div class="ma-2">
                    <v-text-field v-model="resourceCompany.phone"
                                  label="电话"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </div>
                <div class="ma-2">
                    <v-text-field v-model="resourceCompany.abbreviatedName"
                                  label="单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </div>
                <div class="ma-2">
                    <v-text-field v-model="resourceCompany.fullName"
                                  label="单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 300px">
                    </v-text-field>
                </div>
            </div>
            <div class="d-flex">
                <div class="ma-2">
                    <v-text-field v-model="resourceCompany.remark"
                                  label="资源单位备注"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 400px">
                    </v-text-field>
                </div>
                <div class="ma-2">
                    <v-dialog v-model="fullSearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent"
                                   v-on="on"
                                   :disabled="fullSearchPanelOpen">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </div>
            </div>
            <div class="d-flex">
                <v-dialog v-model="bulkSkuImportPanel"
                          persistent
                          scrollable
                          no-click-animation
                          :eager="true"
                          width="55vw">
                    <template v-slot:activator="{on}">
                        <v-btn class="ma-1" color="accent"
                               v-on="on">
                            批量导入型号
                        </v-btn>
                    </template>
                    <BulkSkuSearch :supplierID="resourceCompany.supplierID"
                                   @importSkus="importSkusAction"/>
                </v-dialog>
                <v-dialog v-model="skuSearchPanel"
                          persistent
                          scrollable
                          no-click-animation
                          :eager="true"
                          max-width="50vw">
                    <template v-slot:activator="{on}">
                        <v-btn class="ma-1" color="accent"
                               v-on="on">
                            型号助选
                        </v-btn>
                    </template>
                    <SkuSearch :supplierID="resourceCompany.supplierID"
                               @skuSearchChoose="skuSearchChooseAction"/>
                </v-dialog>
                <v-btn class="ma-1" color="warning" @click="removeRows">删除</v-btn>
                <v-spacer></v-spacer>
                <div class="ma-1">
                    <v-select v-model="taxRateBase"
                              :items="taxRateBaseOptions"
                              label="价格基准"
                              hide-details="auto"
                              outlined dense
                              style="width: 140px"/>
                </div>
                <div class="ma-1">
                    <v-select v-model="taxRate"
                              :items="taxRateOptions"
                              @change="changeTaxRate"
                              label="更改税率"
                              hide-details="auto"
                              :append-icon="mdiPercentOutline"
                              outlined dense
                              style="width: 110px"/>
                </div>
                <v-spacer></v-spacer>
                <v-dialog v-model="dialogSave" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn class="ma-1" color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认保存？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="warning" @click="dialogSave = false">取消</v-btn>
                            <v-btn color="success" @click="saveChanges">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </div>
            <v-card outlined>
                <v-data-table v-model="tableCurrentRow"
                              :headers="tableHeaders"
                              :items="tableData"
                              item-key="skuID"
                              calculate-widths
                              height="60vh"
                              sort-by="code"
                              :footer-props="{
                                  'items-per-page-options': [10, 30, 50],
                                  'show-current-page': true,
                                  'show-first-last-page': true
                              }"
                              show-select
                              @click:row="tableSelect"
                              @item-selected="tableSelect2"
                              fixed-header
                              show-expand
                              locale="zh-cn">
<!--                    <template v-slot:item.index="{ item }">-->
<!--                        {{tableData.indexOf(item) + 1}}-->
<!--                    </template>-->
                    <template v-slot:item.factoryPriceWithoutTax="{ item }">
                        <v-edit-dialog :return-value="item.factoryPriceWithoutTax"
                                       @save="savePriceWithoutTax(item)"
                                       @cancel="savePriceWithoutTax(item)"
                                       @close="savePriceWithoutTax(item)">
                            {{item.factoryPriceWithoutTax}}
                            <template v-slot:input>
                                <v-text-field v-model="item.factoryPriceWithoutTax" single-line
                                              @focus="$event.target.setSelectionRange(0, item.factoryPriceWithoutTax.length)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.factoryPriceWithTax="{ item }">
                        <v-edit-dialog :return-value="item.factoryPriceWithTax"
                                       @save="savePriceWithTax(item)"
                                       @cancel="savePriceWithTax(item)"
                                       @close="savePriceWithTax(item)">
                            {{item.factoryPriceWithTax}}
                            <template v-slot:input>
                                <v-text-field v-model="item.factoryPriceWithTax" single-line
                                              @focus="$event.target.setSelectionRange(0, item.factoryPriceWithTax.length)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.floatDownRate="{ item }">
                        <v-edit-dialog :return-value="item.floatDownRate"
                                       @save="saveFloatDownRate(item)"
                                       @cancel="saveFloatDownRate(item)"
                                       @close="saveFloatDownRate(item)">
                            {{item.floatDownRate}}
                            <template v-slot:input>
                                <v-text-field v-model="item.floatDownRate" single-line
                                              @focus="$event.target.setSelectionRange(0, item.floatDownRate.length)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.settlementPriceWithoutTax="{ item }">
                        <v-edit-dialog :return-value="item.settlementPriceWithoutTax"
                                       @save="saveSettlementPrice(item)"
                                       @cancel="saveSettlementPrice(item)"
                                       @close="saveSettlementPrice(item)">
                            {{item.settlementPriceWithoutTax}}
                            <template v-slot:input>
                                <v-text-field v-model="item.settlementPriceWithoutTax" single-line
                                              @focus="$event.target.setSelectionRange(0,item.settlementPriceWithoutTax.length)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.quantityPerBox="{ item }">
                        <v-edit-dialog :return-value="item.quantityPerBox">
                            {{item.quantityPerBox}}
                            <template v-slot:input>
                                <v-text-field v-model="item.quantityPerBox" single-line
                                              @focus="$event.target.setSelectionRange(0, item.quantityPerBox.length)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:expanded-item="{ item }">
                        <td :colspan="item.history.length">{{ item.history }}</td>
                    </template>
                </v-data-table>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiArrowLeft, mdiPercentOutline } from "@mdi/js";

export default {
    name: "SupplierResources",
    components: {
        SupplierResourceQuery: () => import("~/components/SupplierResourceQuery"),
        CompanySearch: () => import("~/components/CompanySearch"),
        BulkSkuSearch: () => import("~/components/ResourcesComponents/BulkSkuSearch"),
        SkuSearch: () => import("~/components/ResourcesComponents/SkuSearch"),
    },
    beforeMount() {
        this.$getRequest(this.$api.allTaxRates).then((data) => {
            this.taxRateOptions = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,
            mdiPercentOutline,
            dialogSave: false,

            taxRate: '16',
            taxRateOptions: [],
            taxRateBase: '无税厂价',
            taxRateBaseOptions: ['无税厂价', '含税厂价'],

            resourceCompanySearchPanel: false,
            fullSearchPanelOpen: false,
            bulkSkuImportPanel: false,
            skuSearchPanel: false,

            resourceCompany: {
                supplierID: -1,
                phone: '',
                abbreviatedName: '',
                fullName: '',
                remark: '',
            },

            tableHeaders: [
                // { text: '序号', value: 'index', width: '65px' },
                // { text: '商品分类', value: '', width: '80px' },
                { text: '代号', value: 'code', width: '120px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '无税厂价', value: 'factoryPriceWithoutTax', width: '80px' },
                { text: '含税厂价', value: 'factoryPriceWithTax', width: '80px' },
                { text: '下浮率', value: 'floatDownRate', width: '95px' },
                { text: '无税结算价', value: 'settlementPriceWithoutTax', width: '95px' },
                { text: '装箱数', value: 'quantityPerBox', width: '90px' },
                { text: '录入时间', value: 'quoteDate', width: '100px' }
            ],
            tableData: [],
            tableCurrentRow: [],
        }
    },
    methods: {
        fullSearchChooseAction(val) {
            if (val) {
                this.resourceCompany.supplierID = val.companyID
                this.resourceCompany.abbreviatedName = val.abbreviatedName
                this.resourceCompany.fullName = val.fullName
                this.resourceCompany.phone = val.phone
                this.$getRequest(this.$api.supplierInfo +
                    encodeURI(val.companyID)).then(data => {
                    this.resourceCompany.remark = data.remark
                }).catch(() => {})
                this.$getRequest(this.$api.resourceBySupplier +
                    encodeURI(val.companyID)).then(data => {
                    this.tableData = data
                }).catch(() => {})
            }
            this.fullSearchPanelOpen = false
        },
        importSkusAction(data) {
            if (data) {
                for (const item of data) {
                    let found = false
                    for (const row of this.tableData) {
                        if (row.skuID === item.skuID) {
                            found = true
                            break
                        }
                    }
                    if (!found) this.tableData.push(item)
                }
            }
            this.bulkSkuImportPanel = false
        },
        skuSearchChooseAction(val) {
            if (val) {
                let found = false
                for (const row of this.tableData) {
                    if (row.skuID === val.skuID) {
                        found = true
                        break
                    }
                }
                if (!found) this.tableData.push(val)
            }
            this.skuSearchPanel = false
        },
        tableSelect(row) {
            if (this.tableCurrentRow.indexOf(row) !== -1) {
                this.tableCurrentRow.splice(this.tableCurrentRow.indexOf(row), 1)
            }
            else {
                this.tableCurrentRow.push(row)
            }
        },
        tableSelect2(row) {
            if (!row.value) {
                this.tableCurrentRow.splice(this.tableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.tableSelect(row.item)
            }
        },
        savePriceWithTax(item) {
            let withTax = this.$Big(this.$validateFloat(item.factoryPriceWithTax))
            item.factoryPriceWithTax = withTax.toString()
            item.factoryPriceWithoutTax = withTax.div(this.$Big(this.taxRate).div('100').add('1')).toString()
            let withoutTax = this.$Big(item.factoryPriceWithoutTax)
            item.settlementPriceWithoutTax = withoutTax.times(this.$Big('1').sub(this.$Big(item.floatDownRate).div('100'))).toString()
        },
        savePriceWithoutTax(item) {
            let withoutTax = this.$Big(this.$validateFloat(item.factoryPriceWithoutTax))
            item.factoryPriceWithoutTax = withoutTax.toString()
            item.factoryPriceWithTax = withoutTax.times(this.$Big(this.taxRate).div('100').add('1')).toString()
            item.settlementPriceWithoutTax = withoutTax.times(this.$Big('1').sub(this.$Big(item.floatDownRate).div('100'))).toString()
        },
        saveFloatDownRate(item) {
            let rate = this.$Big(this.$validateNumber(item.floatDownRate))
            item.floatDownRate = rate.toString()
            let withoutTax = this.$Big(item.factoryPriceWithoutTax)
            item.settlementPriceWithoutTax = withoutTax.times(this.$Big('1').sub(rate.div('100'))).toString()
        },
        saveSettlementPrice(item) {
            item.settlementPriceWithoutTax = this.$validateFloat(item.settlementPriceWithoutTax)
        },
        changeTaxRate() {
            this.tableData.forEach(item => {
                if (this.taxRateBase === '无税厂价')
                    this.savePriceWithoutTax(item)
                else
                    this.savePriceWithTax(item)
                this.saveFloatDownRate(item)
            })
        },
        removeRows() {
            if (this.tableCurrentRow.length === 0) return
            for (const row of this.tableCurrentRow) {
                this.tableData.splice(this.tableData.indexOf(row), 1)
            }
        },
        saveChanges() {
            if (this.resourceCompany.supplierID === -1) return
            if (this.tableData.length === 0) return
            if (this.taxRate === '0') return

            this.$postRequest(this.$api.createSupplierWithResources, {
                elements: this.tableData
            }, {
                supplierID: this.resourceCompany.supplierID,
                remark: this.resourceCompany.remark
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/resources')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
