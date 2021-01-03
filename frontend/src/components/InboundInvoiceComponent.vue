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
                                          dense>
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
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.invoiceAmount"
                                  label="开票金额"
                                  hide-details="auto"
                                  @change="handleInvoiceAmountChange"
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
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="供货单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 300px">
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
                    <v-select v-model="form.invoiceIndication"
                              :items="invoiceIndicationOptions"
                              :rules="rules.invoiceIndication"
                              item-value="value"
                              item-text="label"
                              label="开票标志"
                              hide-details="auto"
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
                                outlined
                                dense
                                auto-grow
                                rows="1"
                                counter="200">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>
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
        CompanySearch: () => import("~/components/CompanySearch")
    },
    props: {
        mode: {
            type: String,
            required: true
        },
        paramCheckoutDate: {
            type: String,
            required: false,
            default: ''
        },
        paramCompanyInfo: {
            type: Object,
            require: false,
            default() {return {partnerCompanyID: -1, companyFullName: '',}}
        },
        paramInvoiceType: {
            type: String,
            require: false,
        },
        paramTotalAmount: {
            type: String,
            required: false,
            default: '0.0'
        }
    },
    watch: {
        paramCheckoutDate(val, oldVal) {
            if (this.checkoutEntryMode) {
                this.form.checkoutDate = val
            }
        },
        paramCompanyInfo(val, oldVal) {
            if (this.checkoutEntryMode) {
                this.form.partnerCompanyID = val.partnerCompanyID
                this.form.companyFullName = val.companyFullName
            }
        },
        paramTotalAmount(val, oldVal) {
            if (this.checkoutEntryMode) {
                this.form.totalAmount = val
            }
        },
        paramInvoiceType(val, oldVal) {
            if (this.checkoutEntryMode) {
                this.form.invoiceType = val
            }
        },
        /* ------- duelWay binding -------*/
        form: {
            handler: function (val, oldVal) {
                this.$emit('passiveUpdateInvoiceEntry', val)
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
        }
    },
    data() {
        return {
            checkoutEntryMode: false,
            invoiceEntryMode: false,

            fullSearchPanelOpen: false,

            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),

            form: {
                invoiceEntrySerial: null,
                partnerCompanyID: -1, companyFullName: '',
                invoiceType: '', invoiceNumber: '',
                totalAmount: '0.0', invoiceAmount: '',
                invoiceIndication: '', isFollowUpIndication: 0,
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: '',
                inOrOut: '入',
                invoiceDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                invoiceNumberDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                isModified: 0
            },

            rules: {
                invoiceDate: [v => !!v || '请选择日期'],
                invoiceNumberDate: [v => !!v || '请选择日期'],
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
        }
    },
    methods: {
        /* ------- full name company search -------*/
        fullSearchChooseAction(val) {
            if (val) {
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.partnerCompanyID = val.companyID
            }
            this.fullSearchPanelOpen = false
        },

        handleInvoiceAmountChange() {
            this.form.invoiceAmount = validateFloat(this.form.invoiceAmount.toString())
        },
    }
}
</script>

<style scoped>

</style>
