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
                    <v-menu close-on-content-click
                            :disabled="displayMode || modifyMode"
                            :nudge-right="40"
                            transition="scale-transition"
                            offset-y>
                        <template v-slot:activator="{on}">
                            <v-text-field v-model="form.invoiceDate"
                                          :rules="rules.invoiceDate"
                                          v-on="on"
                                          label="开票日期"
                                          hide-details="auto"
                                          outlined
                                          readonly
                                          dense
                                          style="width: 150px">
                            </v-text-field>
                        </template>
                        <v-date-picker v-model="form.invoiceDate"
                                       no-title
                                       :max="allowedMaxDate"
                                       :first-day-of-week="0"
                                       locale="zh-cn">
                        </v-date-picker>
                    </v-menu>
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
                                  hide-details="auto"
                                  @change="handleInvoiceAmountChange"
                                  :readonly="displayMode"
                                  outlined
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalAmount"
                                  label="结账金额"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-select v-if="invoiceEntryMode"
                              v-model="form.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="结账类型"
                              :readonly="form.inboundInvoiceProducts.length !== 0"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.invoiceType"
                                  label="结账类型"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="供货单位全称"
                                  hide-details="auto"
                                  readonly
                                  outlined
                                  dense
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="!displayMode">
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
                        <CompanySearch
                            @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
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
                    <v-select v-if="checkoutEntryMode || invoiceEntryMode"
                              v-model="form.invoiceIndication"
                              :items="invoiceIndicationOptions"
                              :rules="rules.invoiceIndication"
                              item-value="value"
                              item-text="label"
                              label="开票标志"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.invoiceIndication"
                                  label="开票标志"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
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
                    <v-menu close-on-content-click
                            :disabled="displayMode"
                            :nudge-right="40"
                            transition="scale-transition"
                            offset-y>
                        <template v-slot:activator="{on}">
                            <v-text-field v-model="form.invoiceNumberDate"
                                          :rules="rules.invoiceNumberDate"
                                          v-on="on"
                                          label="发票开具日期"
                                          hide-details="auto"
                                          outlined
                                          readonly
                                          dense>
                            </v-text-field>
                        </template>
                        <v-date-picker v-model="form.invoiceNumberDate"
                                       no-title
                                       :max="allowedMaxDate"
                                       :first-day-of-week="0"
                                       locale="zh-cn">
                        </v-date-picker>
                    </v-menu>
                </v-col>
            </v-row>

            <v-row>
                <v-col>
                    <v-textarea v-model.number="form.remark"
                                label="备注"
                                hide-details="auto"
                                :readonly="displayMode"
                                outlined
                                dense
                                auto-grow
                                rows="1"
                                counter="200">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row v-if="invoiceEntryMode">
            <v-col>
                <v-dialog v-model="productsChoosePanelOpen"
                          :eager="true"
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
                    <InboundCheckoutProductsChoose mode="invoice"
                                                   :companyID="form.partnerCompanyID"
                                                   :invoiceType="form.invoiceType"
                                                   @productsChoose="productsChooseAction">
                    </InboundCheckoutProductsChoose>
                </v-dialog>
            </v-col>
            <v-spacer></v-spacer>
            <v-col>
                <v-btn color="primary"
                       @click="createInvoiceEntry()">
                    保存
                </v-btn>
            </v-col>
        </v-row>
        <v-row v-if="modifyMode">
            <v-col>
                <v-btn color="primary"
                       @click="modifyEntry()">
                    保存修改
                </v-btn>
            </v-col>
        </v-row>

        <v-data-table v-if="!checkoutEntryMode"
                      :headers="tableHeaders"
                      :items="form.inboundInvoiceProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{ form.inboundInvoiceProducts.indexOf(item) + 1 }}
            </template>
        </v-data-table>

        <v-row v-if="!checkoutEntryMode">
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
    name: "InboundInvoiceComponent",
    components: {
        CompanySearch: () => import("~/components/CompanySearch"),
        InboundCheckoutProductsChoose: () => import("~/components/InboundInvoiceComponents/CheckoutProductsChoose")
    },
    props: {
        mode: {
            type: String,
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
            handler: function(val, oldVal) {
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
            handler: function(val, oldVal) {
                if (!this.displayMode && !this.modifyMode) return
                this.form = val
                this.calculateSums()
            },
            deep: true,
            immediate: true,
        },
        /* ------- passive binding -------*/
        form: {
            handler: function (val, oldVal) {
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

            companyResetIndicator: 0,
            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),

            form: {
                invoiceEntrySerial: null,
                partnerCompanyID: -1, companyFullName: '',
                invoiceType: '增值税票', invoiceNumber: '',
                totalAmount: '0.0', invoiceAmount: '',
                invoiceIndication: '', isFollowUpIndication: 0,
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: '',
                inOrOut: '入',
                invoiceDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                invoiceNumberDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                isModified: 0,

                inboundInvoiceProducts: [],
            },

            rules: {
                invoiceDate: [v => !!v || '请选择日期'],
                invoiceNumberDate: [v => !!v || '请选择日期'],
                invoiceIndication: [v => !!v || '请选择开票标志']
            },

            invoiceTypeOptions: [
                { value: '增值税票', label: '增值税票' },
                { value: '普票', label: '普票' },
                { value: '收据', label: '收据' }
            ],
            invoiceIndicationOptions: [
                { value: '正常', label: '正常'},
                { value: '红冲', label: '红冲'}
            ],
            followUpIndicationOptions: [
                { value: 0, label: '普通'},
                { value: 1, label: '补票'}
            ],


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
                { text: '结账单号', value: 'checkoutSerial', width: '120px' },
            ],

            tax: 0,
            sumWithTax: 0,
            sumWithoutTax: 0,
        }
    },
    methods: {
        /* ------- full name company search -------*/
        fullSearchChooseAction(val) {
            if (val) {
                this.form.companyFullName = val.fullName
                this.form.partnerCompanyID = val.companyID
            }
            this.fullSearchPanelOpen = false
        },

        handleInvoiceAmountChange() {
            this.form.invoiceAmount = validateFloat(this.form.invoiceAmount.toString())
        },
        productsChooseAction(val) {
            if (val) {
                this.form.inboundInvoiceProducts = val
                this.calculateSums()
                this.form.totalAmount = this.sumWithTax
            }
            this.productsChoosePanelOpen = false
        },
        calculateSums() {
            let tempTax = 0.0
            let tempSumWithTax = 0.0
            let tempSumWithoutTax = 0.0
            for (let item of this.form.inboundInvoiceProducts) {
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
        },
        createInvoiceEntry() {
            if (this.$refs.form.validate()) {
                this.$putRequest(this.$api.createInvoiceEntry, this.form, {
                    isInbound: true,
                }).then((res) => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })

                    this.$router.replace('/inbound_invoicing')
                }).catch(error => this.$ajaxErrorHandler(error))
            }
        },
        modifyEntry() {
            if (this.$refs.form.validate()) {
                this.$patchRequest(this.$api.modifyInvoiceEntry, this.form).then((res) => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })

                    this.$router.replace('/inbound_invoicing')
                }).catch(error => this.$ajaxErrorHandler(error))
            }
        }
    }
}
</script>

<style scoped>

</style>
