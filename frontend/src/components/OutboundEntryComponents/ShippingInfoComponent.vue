<template>
    <v-container>
        <v-form>
            <v-row>
                <v-col>
                    <v-text-field v-model="form.warehouseName"
                                  label="仓库"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col>
                    <v-text-field v-model="form.departmentName"
                                  label="部门"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shipmentDate"
                                  label="出库日期"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense>
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.classification"
                                  label="出库类别"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.invoiceType"
                                  label="预计单据类型"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyPhone"
                                  label="电话"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="购货单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="购货单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.relevantCompanyName"
                                  label="运输方式"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="relativeCompanySearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="75vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent"
                                   v-on="on"
                                   :disabled="relativeCompanySearchPanelOpen">
                                选择
                            </v-btn>
                        </template>
                        <RelativeCompanySearch
                            @relativeCompanyChoose="relativeCompanyChooseAction">
                        </RelativeCompanySearch>
                    </v-dialog>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingNumber"
                                  label="运单号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 220px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingQuantity"
                                  label="件数"
                                  hide-details="auto"
                                  type="number"
                                  outlined
                                  dense
                                  style="width: 80px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-radio-group v-model="form.shippingCostType"
                                   hide-details="auto"
                                   class="mt-0"
                                   row dense>
                        <v-radio label="无运费" value="无"></v-radio>
                        <v-radio label="自付运费" value="自付"></v-radio>
                        <v-radio label="代垫运费" value="代垫"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="form.shippingCost"
                                  label="运费"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.deliveryMethod"
                              :items="deliveryMethodOptions"
                              item-value="value"
                              item-text="label"
                              label="提货方式"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="totalAmount"
                                  label="总金额"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <v-textarea v-model.number="form.remark"
                                label="备注"
                                hide-details="auto"
                                outlined
                                dense
                                auto-grow
                                rows="1"
                                counter="200">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row>
            <v-spacer></v-spacer>
            <v-col>
                <v-btn color="primary"
                       @click="saveShippingInfoChange">
                    保存修改
                </v-btn>
            </v-col>
        </v-row>

        <v-data-table :headers="tableHeaders"
                      :items="form.outboundProducts"
                      item-key="id"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{form.outboundProducts.indexOf(item) + 1}}
            </template>
        </v-data-table>

        <v-row>
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <span>税额合计</span>
            </v-col>
            <v-col cols="auto">
                {{tax}}
            </v-col>
            <v-col cols="auto">
                <span>不含税合计</span>
            </v-col>
            <v-col cols="auto">
                {{sumWithoutTax}}
            </v-col>
            <v-col cols="auto">
                <span>价税合计</span>
            </v-col>
            <v-col cols="auto">
                {{sumWithTax}}
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: "ShippingInfoComponent",
    components: {
        RelativeCompanySearch: () => import("../RelativeCompanySearch"),
    },
    props: {
        form: {
            type: Object,
            required: true
        }
    },
    watch: {
        form: {
            handler(newVal) {
                let tax = 0.0
                let sumWithTax = 0.0
                let sumWithoutTax = 0.0
                for (let item of newVal.outboundProducts) {
                    tax += (item.unitPriceWithTax - item.unitPriceWithoutTax) * item.quantity
                    sumWithTax += item.unitPriceWithTax * item.quantity
                    sumWithoutTax += item.unitPriceWithoutTax * item.quantity
                }
                this.tax = tax.toFixed(2)
                this.sumWithTax = sumWithTax.toFixed(2)
                this.sumWithoutTax = sumWithoutTax.toFixed(2)

                this.totalAmount = Number(this.sumWithTax)
                this.totalAmount += this.form.shippingCostType === '代垫' ? this.form.shippingCost : 0.0
            },
            deep: true,
            immediate: true
        }
    },
    data() {
        return {
            relativeCompanySearchPanelOpen: false,

            tableHeaders: [
                {text: '序号', value: 'index', width: '60px'},
                {text: '新代号', value: 'newCode', width: '100px'},
                {text: '旧代号', value: 'oldCode', width: '100px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '入库数量', value: 'quantity', width: '80px'},
                {text: '单位', value: 'unitName', width: '60px'},
                {text: '含税单价', value: 'unitPriceWithTax', width: '80px'},
                {text: '无税单价', value: 'unitPriceWithoutTax', width: '80px'},
                {text: '无税金额', value: 'totalWithoutTax', width: '80px'},
                {text: '税率', value: 'taxRate', width: '65px'},
                {text: '税额', value: 'totalTax', width: '80px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '库存数量', value: 'stockQuantity', width: '120px'},
                {text: '库存单价', value: 'stockUnitPrice', width: '120px'}
            ],
            //extract to prevent infinite update on form watcher
            totalAmount: 0.0,

            deliveryMethodOptions: [
                {value: '自提', label: '自提'},
                {value: '送货', label: '送货'},
                {value: '代办发货', label: '代办发货'},
                {value: '发货代收款', label: '发货代收款'}
            ],

            tax: 0.0,
            sumWithTax: 0.0,
            sumWithoutTax: 0.0
        }
    },
    methods: {
        /*------- relative company search -------*/
        relativeCompanyChooseAction(val) {
            if (val) {
                this.form.relevantCompanyName = val.abbreviatedName
                this.form.shippingMethodID = val.companyID
            }
            this.relativeCompanySearchPanelOpen = false
        },
        saveShippingInfoChange() {
            this.form.totalAmount = this.totalAmount
            this.$patchRequest(this.$api.completeOutboundEntry, this.form).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$router.replace('/outbound_management')
            }).catch(() => {})
        }
    },
}
</script>

<style scoped>

</style>
