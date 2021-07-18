<template>
    <v-container>
        <v-form ref="form">
            <v-row>
<!--                <v-col cols="auto">-->
<!--                    <v-text-field v-model="form.classification"-->
<!--                                  label="开票单号"-->
<!--                                  hide-details="auto"-->
<!--                                  outlined-->
<!--                                  readonly-->
<!--                                  dense-->
<!--                                  style="width: 150px">-->
<!--                    </v-text-field>-->
<!--                </v-col>-->
                <v-col cols="auto">
                    <DatePicker label="开票日期"
                                v-model="form.invoiceDate"
                                :disabled="displayMode || modifyMode">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.invoiceNumber"
                                  label="发票号码"
                                  hide-details="auto"
                                  outlined
                                  :readonly="displayMode"
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.invoiceAmount"
                                  label="开票金额"
                                  type="number"
                                  hide-details="auto"
                                  @change="handleInvoiceAmountChange"
                                  :readonly="displayMode"
                                  outlined
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalAmount"
                                  label="结账金额"
                                  hide-details="auto"
                                  filled
                                  readonly
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="结账类型"
                              :readonly="!invoiceEntryMode || isInbound ? form.inboundInvoiceProducts.length !== 0 :
                                                          form.outboundInvoiceProducts.length !== 0"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="供货单位简称"
                                  hide-details="auto"
                                  readonly
                                  outlined
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="!displayMode">
                    <v-dialog v-model="fullSearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent" v-on="on"
                                   :disabled="fullSearchPanelOpen">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch
                            @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
            </v-row>

            <v-row v-if="!checkoutEntryMode && form.partnerCompanyID !== -1 && form.companyRemark !== ''" dense>
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
                    <v-select v-model="form.invoiceIndication"
                              :items="invoiceIndicationOptions"
                              :rules="rules.invoiceIndication"
                              item-value="value"
                              item-text="label"
                              label="开票标志"
                              hide-details="auto"
                              :readonly="!checkoutEntryMode && !invoiceEntryMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.isFollowUpIndication"
                              :items="followUpIndicationOptions"
                              item-value="value"
                              item-text="label"
                              label="补票标志"
                              hide-details="auto"
                              :readonly="displayMode || modifyMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <DatePicker label="发票开具日期"
                                v-model="form.invoiceNumberDate"
                                :disabled="displayMode">
                    </DatePicker>
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

        <v-row v-if="invoiceEntryMode" class="my-2" dense>
            <v-col cols="auto">
                <v-dialog v-model="productsChoosePanelOpen"
                          :eager="true"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent" v-on="on"
                               :disabled="productsChoosePanelOpen || form.partnerCompanyID === -1">
                            根据单位助选入库产品
                        </v-btn>
                    </template>
                    <InboundCheckoutProductsChoose mode="invoice"
                                                   :isInbound="true"
                                                   :companyID="form.partnerCompanyID"
                                                   :invoiceType="form.invoiceType"
                                                   @productsChoose="productsChooseAction">
                    </InboundCheckoutProductsChoose>
                </v-dialog>
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="auto" class="d-flex">
                <v-radio-group v-model="entryIDType"
                               hide-details="auto"
                               class="mt-0"
                               row dense>
                    <v-radio v-if="isInbound" label="入结" value="入结"></v-radio>
                    <v-radio v-if="isInbound" label="出退" value="出退"></v-radio>
                    <v-radio v-if="!isInbound" label="出结" value="出结"></v-radio>
                    <v-radio v-if="!isInbound" label="入退" value="入退"></v-radio>
                </v-radio-group>
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
            <v-col cols="auto">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="primary" @click="createInvoiceEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>
        <v-row v-if="modifyMode" class="my-2" dense>
            <v-col cols="auto">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="primary" @click="modifyEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>

        <v-data-table v-if="!checkoutEntryMode"
                      :headers="tableHeaders"
                      :items="isInbound ? form.inboundInvoiceProducts:
                                    form.outboundInvoiceProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
        </v-data-table>

        <div v-if="!checkoutEntryMode" class="d-flex">
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
    name: "InboundInvoiceComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker"),
        CompanySearch: () => import("~/components/CompanySearch"),
        InboundCheckoutProductsChoose: () => import("~/components/InvoiceComponents/CheckoutProductsChoose")
    },
    props: {
        mode: {
            type: String,
            required: true
        },
        isInbound: {
            type: Boolean,
            required: true
        },
        params: {
            type: Object,
            require: false,
            default: null
        },
        paramForm: {
            type: Object,
            required: false,
        }
    },
    watch: {
        params: {
            handler: function(val) {
                if (this.checkoutEntryMode) {
                    this.form.checkoutDate = val.checkoutDate
                    this.form.partnerCompanyID = val.partnerCompanyID
                    this.form.companyFullName = val.companyFullName
                    this.form.invoiceType = val.invoiceType
                    this.form.totalAmount = val.totalAmount
                }
            },
            deep: true,
            immediate: true
        },
        paramForm: {
            handler: function(val) {
                if (!this.displayMode && !this.modifyMode) return
                this.form = val
                this.calculateSums()
            },
            deep: true,
            immediate: true,
        },
        /* ------- passive binding -------*/
        form: {
            handler: function (val) {
                if (this.checkoutEntryMode) {
                    this.$emit('passiveUpdateInvoiceEntry', val)
                }
            },
            deep: true,
            immediate: true
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'checkoutEntry':
            this.checkoutEntryMode = true
            break
        case 'invoiceEntry':
            this.invoiceEntryMode = true
            break
        case 'display':
            this.displayMode = true
            break
        case 'modify':
            this.modifyMode = true
            break
        }
    },
    data() {
        return {
            checkoutEntryMode: false,
            invoiceEntryMode: false,
            displayMode: false,
            modifyMode: false,

            fullSearchPanelOpen: false,
            productsChoosePanelOpen: false,
            submitPopup: false,
            submitPopup2: false,

            entryIDType: this.isInbound ? '入结' : '出结',
            entryIDSearchField: '',

            form: {
                invoiceEntrySerial: '',
                partnerCompanyID: -1, companyAbbreviatedName: '', companyRemark: '',
                invoiceType: '增值税票', invoiceNumber: '',
                totalAmount: '0.0', invoiceAmount: '',
                invoiceIndication: '', isFollowUpIndication: 0,
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: '',
                inOrOut: this.isInbound ? 1 : 0,
                invoiceDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                invoiceNumberDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                isModified: 0,

                inboundInvoiceProducts: [],
                outboundInvoiceProducts: [],
            },

            rules: {
                invoiceIndication: [v => !!v || '请选择开票标志']
            },

            invoiceIndicationOptions: [
                { value: '正常', label: '正常'},
                { value: '红冲', label: '红冲'}
            ],
            followUpIndicationOptions: [
                { value: 0, label: '普通'},
                { value: 1, label: '补票'}
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
                { text: '备注', value: 'remark', width: '120px' },
                { text: '结账单号', value: 'checkoutSerial', width: '120px' },
            ],

            tax: '0',
            sumWithTax: '0',
            sumWithoutTax: '0',
        }
    },
    computed: {
        invoiceTypeOptions() {
            return this.$store.state.invoiceTypeOptions
        }
    },
    methods: {
        /* ------- full name company search -------*/
        fullSearchChooseAction(val) {
            if (val) {
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.partnerCompanyID = val.companyID
                this.form.companyRemark = val.remark
            }
            this.fullSearchPanelOpen = false
        },
        importProductsByEntry() {
            this.$store.commit('setOverlay', true)
            if (this.isInbound) {
                this.$getRequest(this.$api.inboundProductsCheckoutAndNotInvoicedByEntryID, {
                    entryID: '入结' + this.entryIDSearchField,
                    invoiceType: this.form.invoiceType
                }).then(data => {
                    this.$store.commit('setOverlay', false)

                    this.form.inboundCheckoutProducts = data
                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.outboundProductsCheckoutAndNotInvoicedByEntryID, {
                    entryID: '出结' + this.entryIDSearchField,
                    invoiceType: this.form.invoiceType
                }).then(data => {
                    this.$store.commit('setOverlay', false)

                    this.form.inboundCheckoutProducts = data
                }).catch(() => {})
            }
        },
        productsChooseAction(val) {
            if (val) {
                if (this.isInbound) {
                    this.form.inboundInvoiceProducts = val
                }
                else {
                    this.form.outboundInvoiceProducts = val
                }
                this.calculateSums()
                this.form.totalAmount = this.sumWithTax
            }
            this.productsChoosePanelOpen = false
        },
        calculateSums() {
            let tempSumWithoutTax = this.$Big('0')
            let tempSumWithTax = this.$Big('0')
            let tempTax = this.$Big('0')

            let products = this.isInbound ? this.form.inboundInvoiceProducts : this.form.outboundInvoiceProducts
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
        },
        handleInvoiceAmountChange() {
            this.form.invoiceAmount = this.form.invoiceAmount === '' ? '0' :
                this.$validateFloat(this.form.invoiceAmount.toString())
        },
        createInvoiceEntry() {
            if (this.$refs.form.validate()) {
                this.$store.commit('setOverlay', true)
                this.$putRequest(this.$api.createInvoiceEntry, this.form, {
                    isInbound: this.isInbound,
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/inbound_invoicing')
                }).catch(() => {})
            }
            this.submitPopup = false
        },
        modifyEntry() {
            if (this.$refs.form.validate()) {
                this.$store.commit('setOverlay', true)
                this.$patchRequest(this.$api.modifyInvoiceEntry, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/inbound_invoicing')
                }).catch(() => {})
            }
            this.submitPopup2 = false
        }
    }
}
</script>

<style scoped>

</style>
