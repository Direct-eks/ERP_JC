<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.departmentID"
                              :rules="rules.departmentID"
                              :items="departmentOptions"
                              item-value="departmentID"
                              item-text="name"
                              label="部门"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-menu close-on-content-click
                            :nudge-right="40"
                            transition="scale-transition"
                            offset-y>
                        <template v-slot:activator="{on}">
                            <v-text-field v-model="form.checkoutDate"
                                          :rules="rules.checkoutDate"
                                          v-on="on"
                                          label="结账日期"
                                          hide-details="auto"
                                          outlined
                                          readonly
                                          dense>
                            </v-text-field>
                        </template>
                        <v-date-picker v-model="form.checkoutDate"
                                       no-title
                                       :max="allowedMaxDate"
                                       :first-day-of-week="0"
                                       locale="zh-cn">
                        </v-date-picker>
                    </v-menu>
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
                    <v-dialog v-model="fullSearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent"
                                   v-on="on"
                                   :disabled="fullSearchPanelOpen || form.checkoutProducts.length !== 0">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch
                            @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
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
                                  :rules="rules.companyID"
                                  label="单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="单位全称"
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
                    <v-select v-model="form.paymentMethod"
                              :rules="rules.paymentMethod"
                              :items="paymentMethodOptions"
                              item-value="value"
                              item-text="label"
                              label="付款方式"
                              hide-details="auto"
                              outlined dense
                              style="width: 120px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.paymentNumber"
                                  label="号码"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  :readonly="form.paymentMethod === '现金' || form.paymentMethod === ''"
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.paymentAmount"
                                  label="金额"
                                  hide-details="auto"
                                  @change="handlePaymentAmountChange"
                                  outlined
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="form.paymentMethod !== '现金' && form.paymentMethod !== ''">
                    <v-select v-model="form.bankAccountName"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              label="银行"
                              hide-details="auto"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="结账类型"
                              :readonly="form.checkoutProducts.length !== 0"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto" class="pr-0">
                    <v-radio-group v-model="form.isRounded"
                                   hide-details="auto"
                                   class="mt-0"
                                   @change="isRoundedChange"
                                   row dense>
                        <v-radio label="不抹零" :value=0></v-radio>
                        <v-radio label="抹零" :value=1></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-show="form.isRounded === 1"
                                  v-model.number="form.roundedAmount"
                                  label="抹零金额"
                                  hide-details="auto"
                                  outlined
                                  @change="roundedAmountChange"
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="form.totalAmount"
                                  label="应付总金额"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="form.debt"
                                  label="余额"
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

        <v-expand-transition>
            <v-card v-show="invoicePanelOpen"
                    outlined>
                <InboundInvoiceComponent mode="checkoutEntry"
                                         :paramCheckoutDate="form.checkoutDate"
                                         :paramCompanyInfo="{
                                             partnerCompanyID: form.partnerCompanyID,
                                             companyFullName: form.companyFullName}"
                                         :paramInvoiceType="form.invoiceType"
                                         :paramTotalAmount="form.totalAmount"
                                         @passiveUpdateInvoiceEntry="passiveUpdateInvoiceEntryAction">
                </InboundInvoiceComponent>
            </v-card>
        </v-expand-transition>

        <v-row>
            <v-col>
                <v-dialog v-model="productsChoosePanelOpen"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="productsChoosePanelOpen || form.partnerCompanyID === -1">
                            根据单位助选入库产品
                        </v-btn>
                    </template>
                    <InboundCheckoutProductsChoose :companyID="form.partnerCompanyID"
                                                   :invoiceType="form.invoiceType"
                                                   @productsChoose="productsChooseAction">
                    </InboundCheckoutProductsChoose>
                </v-dialog>
            </v-col>
            <v-spacer></v-spacer>
            <v-col>
                <v-btn color="accent"
                       @click="triggerInvoice(true)">
                    开票
                </v-btn>
                <v-btn color="accent"
                       @click="triggerInvoice(false)">
                    不开票
                </v-btn>
            </v-col>
            <v-spacer></v-spacer>
            <v-col>
                <v-btn color="primary"
                       @click="createCheckoutEntry">
                    保存
                </v-btn>
            </v-col>
        </v-row>

        <v-data-table v-model="tableRowsSelectedForDeletion"
                      :headers="tableHeaders"
                      :items="form.checkoutProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{form.checkoutProducts.indexOf(item) + 1}}
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
    name: "InboundCheckoutComponent",
    components: {
        CompanySearch: () => import("~/components/CompanySearch"),
        InboundInvoiceComponent: () => import("~/components/InboundInvoiceComponent"),
        InboundCheckoutProductsChoose: () => import("~/components/InboundCheckoutProductsChoose")
    },
    props: {
        mode: {
            type: String,
            required: true,
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'create':
            this.creationMode = true
            break
        case 'display':
            this.displayMode = true
            break
        case 'modify':
            this.modifyMode = true
            break
        }

        this.$getRequest(this.$api.departmentOptions).then((res) => {
            console.log(res.data)
            this.departmentOptions = res.data
            for (const item of this.departmentOptions) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
        })
        this.$getRequest(this.$api.visibleBankAccounts).then((res) => {
            console.log(res.data)
            this.bankAccountOptions = res.data
        })
    },
    data() {
        return {
            creationMode: false,
            displayMode: false,
            modifyMode: false,

            fullSearchPanelOpen: false,
            productsChoosePanelOpen: false,
            invoicePanelOpen: false,

            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),

            form: {
                checkoutEntrySerial: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                invoiceType: '增值税票',
                paymentMethod: '', paymentNumber: '', paymentAmount: '',
                bankAccountID: -1, bankAccountName: '',
                totalAmount: '0.0', isRounded: 0, roundedAmount: '',
                debt: '0.0', serviceFee: '',
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                moneyEntrySerial: '',
                returnSerial: '',
                departmentID: -1, departmentName: '',
                isVerified: 0,
                isModified: 0,
                checkoutProducts: [],
                invoiceEntry: null
            },

            rules: {
                departmentID: [v => !!v || '请选择部门'],
                checkoutDate: [v => !!v || '请选择结账日期'],
                companyID: [v => !!v || this.form.partnerCompanyID !== -1 || '请选择单位'],
                paymentMethod: [v => !!v || '请选择付款方式'],
                bankAccountID: [v => !!v &&
                    (this.form.paymentMethod !== '现金' || this.form.paymentMethod !== '')
                    || '请选择银行'],
            },

            paymentMethodOptions: [
                { value: '现金', label: '现金' },
                { value: '转支', label: '转支' },
                { value: '电汇', label: '电汇' },
                { value: '汇票', label: '汇票' },
                { value: '其他', label: '其他' },
            ],
            invoiceTypeOptions: [
                { value: '增值税票', label: '增值税票' },
                { value: '普票', label: '普票' },
                { value: '收据', label: '收据' }
            ],
            bankAccountOptions: [],
            departmentOptions: [],

            tableHeaders: [
                { text: '序号', value: 'index', width: '60px' },
                { text: '新代号', value: 'newCode', width: '100px' },
                { text: '旧代号', value: 'oldCode', width: '100px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '80px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '80px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '80px' },
                { text: '无税金额', value: 'totalWithoutTax', width: '80px' },
                { text: '税率', value: 'taxRate', width: '65px' },
                { text: '税额', value: 'totalTax', width: '80px' },
                { text: '备注', value: 'remark', width: '120px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
            ],

            deleteTableRowPopup: false,
            tableRowsSelectedForDeletion: [],

            tax: 0,
            sumWithTax: 0,
            sumWithoutTax: 0,
        }
    },
    methods: {
        /* ------- full name company search -------*/
        fullSearchChooseAction(val) {
            if (val) {
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.companyFullName = val.fullName
                this.form.companyPhone = val.phone
                this.form.partnerCompanyID = val.companyID
            }
            this.fullSearchPanelOpen = false
        },
        productsChooseAction(val) {
            if (val) {
                this.form.checkoutProducts = val

                let tempTax = 0.0
                let tempSumWithTax = 0.0
                let tempSumWithoutTax = 0.0
                for (let item of this.form.checkoutProducts) {
                    const itemTotalTax = (item.unitPriceWithTax - item.unitPriceWithoutTax) * item.quantity
                    const itemTotalWithoutTax = item.unitPriceWithoutTax * item.quantity
                    tempSumWithTax += item.unitPriceWithTax * item.quantity
                    tempTax += itemTotalTax
                    tempSumWithoutTax += itemTotalWithoutTax
                    item.totalTax = itemTotalTax.toFixed(2)
                    item.totalWithoutTax = itemTotalWithoutTax.toFixed(2)
                }
                this.tax = tempTax.toFixed(2)
                this.sumWithTax = tempSumWithTax.toFixed(2)
                this.sumWithoutTax = tempSumWithoutTax.toFixed(2)

                this.form.totalAmount = tempSumWithTax.toFixed(2)
                this.form.debt = (tempSumWithTax - Number(this.form.paymentAmount)).toFixed(2)
            }
            this.productsChoosePanelOpen = false
        },
        /* ----- number calculation ----- */
        handlePaymentAmountChange() {
            this.form.paymentAmount = validateFloat(this.form.paymentAmount.toString())
            if (this.form.isRounded === 1) {
                this.form.debt = (Number(this.form.totalAmount) - Number(this.form.paymentAmount)
                    - Number(this.form.roundedAmount)).toFixed(2)
            }
            else {
                this.form.debt = (Number(this.form.totalAmount) - Number(this.form.paymentAmount)).toFixed(2)
            }
        },
        isRoundedChange() {
            if (this.form.isRounded === 1) {
                this.form.debt = (Number(this.form.totalAmount) - Number(this.form.paymentAmount)
                    - Number(this.form.roundedAmount)).toFixed(2)
            }
            else {
                this.form.debt = (Number(this.form.totalAmount) - Number(this.form.paymentAmount)).toFixed(2)
            }
        },
        roundedAmountChange() {
            this.form.roundedAmount = validateFloat(this.form.roundedAmount.toString())
            if (this.form.isRounded === 1) {
                this.form.debt = (Number(this.form.totalAmount) - Number(this.form.paymentAmount)
                    - Number(this.form.roundedAmount)).toFixed(2)
            }
        },
        /* ----- invoice entry ----- */
        triggerInvoice(bool) {
            if (bool) {
                this.invoicePanelOpen = true;
            }
            else {
                this.invoicePanelOpen = false
                this.form.invoiceEntry = null
            }
        },
        passiveUpdateInvoiceEntryAction(val) {
            this.form.invoiceEntry = val
        },

        createCheckoutEntry() {
            console.log(this.form)
            if (!this.invoicePanelOpen) {
                this.form.invoiceEntry = null
            }

            if (this.form.checkoutProducts.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '请录入结账商品', color: 'warning'
                })
                return
            }
            if (this.$refs.form.validate()) {
                this.$putRequest(this.$api.createCheckoutEntry, this.form).then((res) => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })

                    this.$router.replace('/inbound_invoicing')
                })
            }
        }
    }
}
</script>

<style scoped>

</style>
