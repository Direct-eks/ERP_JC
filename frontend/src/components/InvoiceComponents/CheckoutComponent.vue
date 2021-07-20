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
                              :readonly="displayMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <DatePicker :label="checkoutDateTitle"
                                v-model="form.checkoutDate"
                                :disabled="!creationMode">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="creationMode">
                    <v-dialog v-model="fullSearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent" v-on="on"
                                   :disabled="isInbound ? form.inboundCheckoutProducts.length !== 0 :
                                                   form.outboundCheckoutProducts.length !== 0">
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

            <v-row v-if="form.partnerCompanyID !== -1 && form.companyRemark !== ''" dense>
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
                    <v-select v-model="form.paymentMethod"
                              :rules="rules.paymentMethod"
                              :items="paymentMethodOptions"
                              item-value="value"
                              item-text="label"
                              :label="paymentMethodTitle"
                              hide-details="auto"
                              :readonly="displayMode"
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
                                  :readonly="form.paymentMethod === '现金' ||
                                                form.paymentMethod === '' || displayMode"
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.paymentAmount"
                                  label="金额"
                                  hide-details="auto"
                                  type="number"
                                  @change="handlePaymentAmountChange"
                                  :readonly="displayMode"
                                  outlined
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="form.paymentMethod !== '现金' && form.paymentMethod !== ''">
                    <v-select v-model="form.bankAccountID"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              label="银行"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
            </v-row>

            <v-row dense>
                <v-col cols="auto">
                    <v-select v-model="form.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="结账类型"
                              :readonly="displayMode || returnMode ||
                                            isInbound ? form.inboundCheckoutProducts.length !== 0 :
                                                form.outboundCheckoutProducts.length !== 0"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.serviceFee"
                                  label="服务费"
                                  hide-details="auto"
                                  type="number"
                                  outlined
                                  @change="calculateSums"
                                  :readonly="displayMode"
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" class="pr-0">
                    <v-radio-group v-model="form.isRounded"
                                   hide-details="auto"
                                   class="mt-0"
                                   @change="handlePaymentAmountChange"
                                   :readonly="displayMode"
                                   row dense>
                        <v-radio label="不抹零" :value=0></v-radio>
                        <v-radio label="抹零" :value=1></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-show="form.isRounded === 1"
                                  v-model="form.roundedAmount"
                                  label="抹零金额"
                                  hide-details="auto"
                                  type="number"
                                  outlined
                                  @change="handlePaymentAmountChange"
                                  :readonly="displayMode"
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model.number="form.totalAmount"
                                  label="应付总金额"
                                  hide-details="auto"
                                  filled
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.debt"
                                  label="余额"
                                  hide-details="auto"
                                  filled
                                  dense
                                  readonly
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <v-textarea v-model="form.remark"
                                label="备注"
                                hide-details="auto"
                                :readonly="displayMode"
                                outlined
                                dense
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-expand-transition>
            <v-card v-if="creationMode || returnMode"
                    v-show="invoicePanelOpen"
                    class="my-2" outlined>
                <InvoiceComponent mode="checkoutEntry"
                                  :isInbound="isInbound"
                                  :params="{
                                        checkoutDate: form.checkoutDate,
                                        partnerCompanyID: form.partnerCompanyID,
                                        companyFullName: form.companyFullName,
                                        invoiceType: form.invoiceType,
                                        totalAmount: form.totalAmount
                                  }"
                                  @passiveUpdateInvoiceEntry="passiveUpdateInvoiceEntryAction">
                </InvoiceComponent>
            </v-card>
        </v-expand-transition>

        <v-row class="my-2" dense>
            <v-col v-if="creationMode">
                <v-dialog v-model="productsChoosePanelOpen"
                          :eager="true"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent" v-on="on"
                               :disabled="productsChoosePanelOpen || form.partnerCompanyID === -1">
                            {{ productChooseButtonTitle }}
                        </v-btn>
                    </template>
                    <CheckoutProductsChoose mode="checkout"
                                            :isInbound="isInbound"
                                            :companyID="form.partnerCompanyID"
                                            :invoiceType="form.invoiceType"
                                            @productsChoose="productsChooseAction">
                    </CheckoutProductsChoose>
                </v-dialog>
            </v-col>
            <v-spacer v-if="creationMode"></v-spacer>
            <v-col v-if="creationMode" cols="auto" class="d-flex">
                <v-text-field v-model="entryIDSearchField"
                              label="根据单据导入"
                              hide-details="auto"
                              @keydown.enter="importProductsByEntry"
                              dense
                              style="width: 110px">
                </v-text-field>
                <v-btn color="info" @click="importProductsByEntry">导入</v-btn>
            </v-col>
            <v-spacer></v-spacer>
            <v-col v-if="creationMode || returnMode" cols="auto">
                <v-btn color="accent"
                       @click="triggerInvoice(true)">
                    开票
                </v-btn>
            </v-col>
            <v-col v-if="creationMode || returnMode" cols="auto">
                <v-btn color="accent"
                       @click="triggerInvoice(false)">
                    不开票
                </v-btn>
            </v-col>
            <v-spacer></v-spacer>
            <v-col v-if="creationMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="createCheckoutEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="modifyMode || returnMode" cols="auto">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="modifyCheckoutEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>

        <v-data-table v-model="tableCurrentRow"
                      :headers="tableHeaders"
                      :items="isInbound ? form.inboundCheckoutProducts :
                                                form.outboundCheckoutProducts"
                      :item-key="isInbound ? 'inboundProductID' : 'outboundProductID'"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      @click:row="tableClick"
                      @item-selected="tableClick2"
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
        </v-data-table>

        <div class="d-flex">
            <v-btn v-if="creationMode" color="error" @click="handleDeleteRow">删除</v-btn>
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
    name: "CheckoutComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker"),
        CompanySearch: () => import("~/components/CompanySearch"),
        InvoiceComponent: () => import("~/components/InvoiceComponents/InvoiceComponent"),
        CheckoutProductsChoose: () => import("~/components/InvoiceComponents/CheckoutProductsChoose")
    },
    props: {
        mode: {
            type: String,
            required: true,
        },
        isInbound: {
            type: Boolean,
            required: true
        },
        paramForm: {
            type: Object,
            required: false
        }
    },
    watch: {
        paramForm: {
            handler: function (val) {
                if (this.creationMode) return
                this.form = val
                this.calculateSums()
            },
            deep: true,
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
        case 'return':
            this.returnMode = true
            break
        }

        this.$store.dispatch('getDepartmentOptions')
        this.$store.dispatch('getBankAccounts')
    },
    data() {
        return {
            creationMode: false,
            displayMode: false,
            modifyMode: false,
            returnMode: false,

            checkoutDateTitle: this.isInbound ? '入库结账日期' : '出库结账日期',
            paymentMethodTitle: this.isInbound ? '付款方式' : '收款方式',
            productChooseButtonTitle: this.isInbound ? '根据单位助选入库产品' : '根据单位助选出库产品',

            fullSearchPanelOpen: false,
            productsChoosePanelOpen: false,
            invoicePanelOpen: false,
            deleteTableRowPopup: false,
            submitPopup: false,
            submitPopup2: false,

            entryIDSearchField: '',

            form: {
                checkoutEntrySerial: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '',
                companyFullName: '', companyRemark: '',
                invoiceType: '增值税票',
                paymentMethod: '', paymentNumber: '', paymentAmount: '',
                bankAccountID: -1, bankAccountName: '',
                totalAmount: '0.0', isRounded: 0, roundedAmount: '',
                debt: '0.0', serviceFee: '0',
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                moneyEntrySerial: '',
                returnSerial: '',
                departmentID: -1, departmentName: '',
                isVerified: 0,
                isModified: 0,
                inboundCheckoutProducts: [],
                outboundCheckoutProducts: [],
                invoiceEntry: null
            },

            rules: {
                departmentID: [v => !!v || '请选择部门'],
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

            tableHeaders: [
                {
                    text: this.isInbound ? '入库单号' : '出库单号',
                    value: this.isInbound ? 'inboundEntryID' : 'outboundEntryID',
                    width: '140px'
                },
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                {
                    text: this.isInbound ? '入库数量' : '出库数量',
                    value: 'quantity',
                    width: '90px'
                },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '无税金额', value: 'totalWithoutTax', width: '100px' },
                { text: '税率', value: 'taxRate', width: '65px' },
                { text: '税额', value: 'totalTax', width: '90px' },
                { text: '备注', value: 'remark', width: '180px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
            ],
            tableCurrentRow: [],

            tax: '0',
            sumWithTax: '0',
            sumWithoutTax: '0',
        }
    },
    computed: {
        invoiceTypeOptions() {
            return this.$store.state.invoiceTypeOptions
        },
        bankAccountOptions() {
            return this.$store.state.visibleBankAccounts
        },
        departmentOptions() {
            const options = this.$store.state.departmentOptions
            for (const item of options) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
            return options
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
                this.form.companyRemark = val.remark
            }
            this.fullSearchPanelOpen = false
        },
        importProductsByEntry() {
            this.$store.commit('setOverlay', true)
            if (this.isInbound) {
                this.$getRequest(this.$api.inboundProductsNotCheckedOutByEntryID, {
                    entryID: '购入' + this.entryIDSearchField,
                    invoiceType: this.form.invoiceType
                }).then(data => {
                    this.$store.commit('setOverlay', false)

                    this.form.inboundCheckoutProducts = data
                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.outboundProductsNotCheckedOutByEntryID, {
                    entryID: '销出' + this.entryIDSearchField,
                    invoiceType: this.form.invoiceType
                }).then(data => {
                    this.$store.commit('setOverlay', false)

                    this.form.outboundCheckoutProducts = data
                }).catch(() => {})
            }
        },
        productsChooseAction(val) {
            if (val) {
                if (this.isInbound) {
                    this.form.inboundCheckoutProducts = val
                }
                else {
                    this.form.outboundCheckoutProducts = val
                }

                this.calculateSums()
                this.handlePaymentAmountChange()
            }
            this.productsChoosePanelOpen = false
        },
        /* ----- number calculation ----- */
        calculateSums() {
            let tempSumWithoutTax = this.$Big('0')
            let tempSumWithTax = this.$Big('0')
            let tempTax = this.$Big('0')

            let products = this.isInbound ? this.form.inboundCheckoutProducts : this.form.outboundCheckoutProducts
            products.forEach((item) => {
                const itemTotalWithoutTax = this.$Big(item.unitPriceWithoutTax).times(item.quantity)
                const itemTotalWithTax = this.$Big(item.unitPriceWithTax).times(item.quantity)
                item.totalTax = itemTotalWithTax.minus(itemTotalWithoutTax).toString()
                item.totalWithoutTax = itemTotalWithoutTax.toString()
                tempSumWithoutTax = tempSumWithoutTax.add(itemTotalWithoutTax)
                tempSumWithTax = tempSumWithTax.add(itemTotalWithoutTax)
                tempTax = tempTax.add(tempSumWithTax.minus(tempSumWithoutTax))
            })

            this.sumWithoutTax = tempSumWithoutTax.toString()
            this.sumWithTax = tempSumWithTax.toString()
            this.tax = tempTax.toString()

            this.form.totalAmount = tempSumWithTax.add(this.form.serviceFee === '' ?
                '0' : this.form.serviceFee).toString()
        },
        handlePaymentAmountChange() {
            this.form.paymentAmount = this.form.paymentAmount === '' ? '0' :
                this.$validateFloat(this.form.paymentAmount)
            this.form.roundedAmount = this.form.roundedAmount === '' ? '0' :
                this.$validateFloat(this.form.roundedAmount)

            if (this.form.isRounded === 1) {
                const r = this.$Big(this.form.roundedAmount)
                const p = JSON.parse(JSON.stringify(this.$store.getters.currentUserPermittedRoundingAmount))
                const temp = r.minus(p).toString()
                if (temp !== '0' && !temp.startsWith('-')) {
                    this.$store.commit('setSnackbar', {
                        message: '超出允许抹零范围', color: 'warning'
                    })
                    this.form.roundedAmount = p
                }
                this.form.debt = this.$Big(this.form.totalAmount).minus(this.form.paymentAmount)
                    .minus(this.form.roundedAmount).toString()
            }
            else {
                this.form.debt = this.$Big(this.form.totalAmount).minus(this.form.paymentAmount).toString()
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
            if (!this.invoicePanelOpen) return // only allow changes to be made when panel is open
            this.form.invoiceEntry = val
        },
        tableClick(row) {
            if (this.tableCurrentRow.includes(row)) {
                this.tableCurrentRow.splice(this.tableCurrentRow.indexOf(row), 1)
            }
            else {
                this.tableCurrentRow.push(row)
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.tableCurrentRow.splice(this.tableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.tableCurrentRow.push(row.item)
            }
        },
        handleDeleteRow() {
            this.deleteTableRowPopup = false
            if (this.tableCurrentRow.length !== 0) {
                for (const item of this.tableCurrentRow) {
                    if (this.isInbound) {
                        this.form.inboundCheckoutProducts.splice(this.form.inboundCheckoutProducts.indexOf(item), 1)
                    }
                    else {
                        this.form.outboundCheckoutProducts.splice(this.form.outboundCheckoutProducts.indexOf(item), 1)
                    }
                }
            }
            else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        createCheckoutEntry() {
            if (this.$refs.form.validate()) {
                if (!this.invoicePanelOpen) {
                    this.form.invoiceEntry = null
                }

                if (this.isInbound ? this.form.inboundCheckoutProducts.length === 0
                    : this.form.outboundCheckoutProducts.length === 0) {
                    this.$store.commit('setSnackbar', {
                        message: '请录入结账商品', color: 'warning'
                    })
                    return
                }

                this.$store.commit('setOverlay', true)
                this.$putRequest(this.$api.createCheckoutEntry, this.form, {
                    isInbound: this.isInbound
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace(this.isInbound ? '/inbound_invoicing' : '/outbound_invoicing')
                }).catch(() => {})
            }
            this.submitPopup = false
        },
        modifyCheckoutEntry() {
            this.submitPopup2 = false
            if (!this.$refs.form.validate()) return

            //change drawer name for modification
            this.form.drawer = this.$store.getters.currentUser
            //fill in department name
            for (const item of this.departmentOptions) {
                if (item.departmentID === this.form.departmentID) {
                    this.form.departmentName = item.name
                    break
                }
            }
            //fill in bankAccountName
            for (const item of this.bankAccountOptions) {
                if (item.bankAccountID === this.form.bankAccountID) {
                    this.form.bankAccountName = item.name
                    break
                }
            }

            this.$store.commit('setOverlay', true)
            if (this.modifyMode) {
                this.$patchRequest(this.$api.modifyCheckoutEntry, this.form, {
                    isInbound: this.isInbound
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace(this.isInbound ? '/inbound_invoicing' : '/outbound_invoicing')
                }).catch(() => {})
            }
            else if (this.returnMode) {
                this.$postRequest(this.$api.returnCheckoutEntry, this.form, {
                    isInbound: this.isInbound
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace(this.isInbound ? '/inbound_invoicing' : '/outbound_invoicing')
                }).catch(() => {})
            }
        },
    }
}
</script>

<style scoped>

</style>
