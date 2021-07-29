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
            <v-row dense>
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

            <v-row v-if="form.companyRemark !== ''" dense>
                <v-col>
                    <v-textarea v-model.number="form.companyRemark"
                                label="重要提示"
                                hide-details="auto"
                                background-color="red accent-2"
                                outlined
                                dense
                                readonly
                                auto-grow
                                rows="1">
                    </v-textarea>
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
                            <v-btn color="accent" v-on="on">选择</v-btn>
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

            <v-row dense>
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
                                  @change="handleShippingCostChange"
                                  type="number"
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
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalAmount"
                                  label="总金额"
                                  hide-details="auto"
                                  filled
                                  dense
                                  readonly
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <v-textarea v-model="form.remark"
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

        <v-row class="my-2" dense>
            <v-spacer></v-spacer>
            <v-col>
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="primary" @click="saveShippingInfoChange">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
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
        </v-data-table>

        <div class="d-flex">
            <v-spacer></v-spacer>
            <div class="my-2">
                <strong>税额合计：</strong>
            </div>
            <div class="my-2 mr-5">
                <strong>{{ tax }}</strong>
            </div>
            <div class="my-2">
                <strong>不含税合计：</strong>
            </div>
            <div class="my-2 mr-5">
                <strong>{{ sumWithoutTax }}</strong>
            </div>
            <div class="my-2">
                <strong>价税合计：</strong>
            </div>
            <div class="my-2 mr-5">
                <strong>{{ sumWithTax }}</strong>
            </div>
        </div>
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
    data() {
        return {
            relativeCompanySearchPanelOpen: false,
            submitPopup: false,

            tableHeaders: [
                {text: '代号', value: 'code', width: '100px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '出库数量', value: 'quantity', width: '80px'},
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

            deliveryMethodOptions: [
                {value: '自提', label: '自提'},
                {value: '送货', label: '送货'},
                {value: '代办发货', label: '代办发货'},
                {value: '发货代收款', label: '发货代收款'}
            ],

            tax: '0.0',
            sumWithTax: '0.0',
            sumWithoutTax: '0.0'
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
        handleShippingCostChange() {
            this.form.shippingCost = this.$validateFloat(this.form.shippingCost, true)
            this.handleTotalChange()
        },
        handleTotalChange() {
            // for all products
            let tempSumWithTax = this.$Big('0')
            let tempSumWithoutTax = this.$Big('0')
            this.form.inboundProducts.forEach((item) => {
                // calculate for total
                const itemQuantity = this.$Big(item.quantity)
                tempSumWithTax = tempSumWithTax.add(itemQuantity.times(item.unitPriceWithTax))
                tempSumWithoutTax = tempSumWithoutTax.add(itemQuantity.times(item.unitPriceWithoutTax))
            })
            this.sumWithTax = tempSumWithTax.toString()
            this.sumWithoutTax = tempSumWithoutTax.toString()
            this.tax = tempSumWithTax.minus(tempSumWithoutTax).toString()

            // plus shipping
            let sum = this.$Big(this.sumWithTax)
            switch (this.form.shippingCostType) {
            case "无":
            case '自付':
                break
            case '代垫':
                sum = sum.plus(this.form.shippingCost === '' ? '0' : this.form.shippingCost)
                break
            }
            this.form.totalCost = sum.toString()
        },
        saveShippingInfoChange() {
            this.form.totalAmount = this.totalAmount

            this.$store.commit('setOverlay', true)
            this.$patchRequest(this.$api.completeOutboundEntry, this.form).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$store.commit('setOverlay', false)

                this.$router.replace('/outbound_management')
            }).catch(() => {})
        }
    },
    watch: {
        form: {
            handler() {
                this.handleTotalChange()
            },
            deep: true,
            immediate: true
        }
    },
}
</script>

<style scoped>

</style>
