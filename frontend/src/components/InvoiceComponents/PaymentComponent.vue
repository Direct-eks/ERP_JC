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
                    <DatePicker label="结账日期"
                                v-model="form.paymentDate"
                                :disabled="displayMode || modifyMode">
                    </DatePicker>
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
                <v-col cols="auto" v-if="createMode">
                    <v-dialog v-model="fullSearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent" v-on="on">
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
                              label="付款方式"
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
                                  outlined
                                  :readonly="displayMode"
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

            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.paymentIndication"
                              :items="paymentIndicationOptions"
                              item-value="value"
                              item-text="label"
                              label="付款类型"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 120px">
                    </v-select>
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

        <v-row v-if="createMode">
            <v-col cols="auto" class="pr-0">
                <v-radio-group v-model="companyResetIndicator"
                               hide-details="auto"
                               class="mt-0"
                               row dense>
                    <v-radio label="保持单位不变" :value=0></v-radio>
                    <v-radio label="清空" :value=1></v-radio>
                </v-radio-group>
            </v-col>
            <v-col cols="auto">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存后新增</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="createEntry(false)">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="createEntry(true)">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>
        <v-row v-if="modifyMode">
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <v-dialog v-model="submitPopup3" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup3 = false">取消</v-btn>
                            <v-btn color="success" @click="modifyEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: "PaymentComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker"),
        CompanySearch: () => import("~/components/CompanySearch"),
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
        paramForm: {
            type: Object,
            required: false
        }
    },
    watch: {
        paramForm: {
            handler: function(val) {
                // restore mode, in case changed before
                switch (this.mode) {
                case 'display':
                    this.modifyMode = false
                    this.displayMode = true
                    break
                case 'modify':
                    this.displayMode = false
                    this.modifyMode = true
                    break
                }

                if (this.displayMode) {
                    this.form = val
                }
                // check if this money entry is bind with checkout entry,
                // if so, forbid changes to be made
                if (this.modifyMode) {
                    this.form = val
                    if (this.form.checkoutSerial !== '') {
                        this.modifyMode = false
                        this.displayMode = true
                    }
                }
            },
            deep: true,
            immediate: true
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'create':
            this.createMode = true
            break
        case 'display':
            this.displayMode = true
            break
        case 'modify':
            this.modifyMode = true
            break
        }

        this.$store.dispatch('getDepartmentOptions')
        this.$store.dispatch('getBankAccounts')
    },
    data() {
        return {
            createMode: false,
            displayMode: false,
            modifyMode: false,

            fullSearchPanelOpen: false,
            submitPopup: false,
            submitPopup2: false,
            submitPopup3: false,

            companyResetIndicator: 0,

            form: {
                moneyEntrySerial: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '',
                companyFullName: '', companyRemark: '',
                paymentIndication: '正常',
                paymentMethod: '', paymentNumber: '', paymentAmount: '',
                bankAccountID: -1, bankAccountName: '',
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                paymentDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutSerial: '',
                departmentID: -1, departmentName: '',
                isModified: 0,
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
            paymentIndicationOptions: [
                { value: '正常', label: '正常' },
                { value: '退款', label: '退款' }
            ],
        }
    },
    computed: {
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
        handlePaymentAmountChange() {
            this.form.paymentAmount = this.$validateFloat(this.form.paymentAmount, true)
        },
        createEntry(exitBool) {
            this.submitPopup = false
            this.submitPopup2 = false
            if (!this.$refs.form.validate()) return

            this.$store.commit('setOverlay', true)
            this.$putRequest(this.$api.createMoneyEntry, this.form, {
                isInbound: this.isInbound,
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$store.commit('setOverlay', false)

                if (!exitBool) {
                    if (this.companyResetIndicator === 1) {
                        this.form.partnerCompanyID = -1
                        this.form.companyAbbreviatedName = ''
                        this.form.companyPhone = ''
                        this.form.companyFullName = ''
                    }
                    this.form.paymentIndication = '正常'
                    this.form.paymentMethod = ''
                    this.form.paymentNumber = ''
                    this.form.paymentAmount = ''
                    this.form.bankAccountID = -1
                    this.form.bankAccountName = ''
                    this.form.remark = ''
                    this.form.creationDate = new Date().format('yyyy-MM-dd').substr(0, 10)
                    this.form.paymentDate = new Date().format('yyyy-MM-dd').substr(0, 10)
                    for (const item of this.departmentOptions) {
                        if (item.isDefault === 1) {
                            this.form.departmentID = item.departmentID
                            break
                        }
                    }
                } else {
                    this.$router.replace('/inbound_invoicing')
                }

            }).catch(() => {})
        },
        modifyEntry() {
            this.submitPopup3 = false
            if (!this.$refs.form.validate()) return

            this.$store.commit('setOverlay', true)
            this.$patchRequest(this.$api.modifyMoneyEntry, this.form).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$store.commit('setOverlay', false)

                this.$router.replace('/inbound_invoicing')
            }).catch(() => {})
        },
    }
}
</script>

<style scoped>

</style>
