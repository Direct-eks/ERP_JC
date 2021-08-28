<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <DatePicker label="办理日期"
                                v-model="form.entryDate">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="searchNumber"
                                  label="汇票查询"
                                  hide-details="auto"
                                  outlined
                                  @keyup.enter="trigger"
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="importPopup" :eager="true">
                        <template v-slot:activator="{ on }">
                            <v-btn color="accent" v-on="on" @click="trigger">检索</v-btn>
                        </template>
                        <EntryImport :number="searchNumber"
                                     :trigger="triggerSearch"
                                     @importEntry="importEntry">
                        </EntryImport>
                    </v-dialog>
                </v-col>
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
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.type"
                                  label="汇票类型"
                                  hide-details="auto"
                                  readonly
                                  outlined
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.number"
                                  label="承兑票号"
                                  hide-details="auto"
                                  readonly
                                  outlined
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row dense>
                <v-col cols="auto">
                    <v-select v-model="form.bankAccountID"
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
                <v-col cols="auto">
                    <v-text-field v-model="form.amount"
                                  label="金额"
                                  hide-details="auto"
                                  type="number"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 140px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <DatePicker label="出票日期"
                                disabled
                                v-model="form.issueDate">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <DatePicker label="到期日期"
                                disabled
                                v-model="form.expirationDate"
                                :allowFutureDates="true">
                    </DatePicker>
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
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row>
            <v-spacer></v-spacer>
            <v-col v-if="modificationMode" cols="auto">
                <v-dialog v-model="deletePopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="warning" v-on="on">
                            删除此单据
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认删除？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="deletePopup = false">取消</v-btn>
                            <v-btn color="success" @click="handleDelete">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="!modificationMode" cols="auto">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">
                            保存新单据
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="saveNew">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="modificationMode" cols="auto">
                <v-btn color="accent" @click="reset">
                    放弃修改
                </v-btn>
            </v-col>
            <v-col v-if="modificationMode" cols="auto">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">
                            修改
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="saveChange">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: "AcceptancePayComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker"),
        EntryImport: () => import("~/components/AccountsManagementComponents/AcceptanceImportByNumberComponent")
    },
    props: {
        isSolutionPay: {
            type: Boolean,
            required: true
        },
        paramForm: {
            type: Object,
            required: true
        }
    },
    watch: {
        paramForm: {
            handler: function(val) {
                if (!val.hasOwnProperty('acceptanceEntrySerial')) return
                if (val.acceptanceEntrySerial === '') return
                this.form = JSON.parse(JSON.stringify(val))
            },
            deep: true,
            immediate: false
        }
    },
    beforeMount() {
        this.$store.dispatch('getDepartmentOptions')
        this.$store.dispatch('getBankAccounts')
        Object.assign(this.emptyForm, this.form)
    },
    data() {
        return {
            importPopup: false,
            triggerSearch: false,
            deletePopup: false,
            submitPopup: false,
            submitPopup2: false,

            searchNumber: '',

            form: {
                acceptanceEntrySerial: '',
                partnerCompanyID: -1, companyAbbreviatedName: '',
                entryDate: this.$todayDateString(),
                creationDate: this.$todayDateString(),
                departmentID: -1, departmentName: '',
                source: '',
                bankAccountID: -1, bankAccountName: '',
                sourceSerial: '',
                amount: '', number: '',
                issueDate: '', expirationDate: '',
                type: '', drawer: this.$store.getters.currentUser,
                remark: '', classification: '', status: 0,
            },
            emptyForm: {},

            rules: {
                bankAccountID: [v => v !== -1 || '请选择银行'],
            },

        }
    },
    computed: {
        departmentOptions() {
            return this.$store.state.departmentOptions
        },
        bankAccountOptions() {
            return this.$store.state.visibleBankAccounts
        },
        modificationMode() {
            return this.form.acceptanceEntrySerial !== ''
        }
    },
    methods: {
        trigger() {
            this.triggerSearch = true
            this.importPopup = true
        },
        importEntry(val) {
            if (val) {
                this.form.partnerCompanyID = val.partnerCompanyID
                this.form.companyAbbreviatedName = val.companyAbbreviatedName
                this.form.type = val.type
                this.form.number = val.number
                this.form.amount = val.amount
                this.form.issueDate = val.issueDate
                this.form.expirationDate = val.expirationDate
            }
            this.triggerSearch = false
            this.importPopup = false
        },
        handleDelete() {
            this.deletePopup = false
            // todo
        },
        saveNew() {
            this.submitPopup = false

            if (this.$refs.form.validate()) {
                this.$putRequest(this.$api.createPayEntry, this.form, {
                    isSolutionPay: this.isSolutionPay
                }).then(() => {
                    this.$saveSuccessMessage()
                }).catch(() => {})
            }
        },
        saveChange() {
            this.submitPopup2 = false

            if (this.$refs.form.validate()) {
                // todo
            }
        },
        reset() {
            Object.assign(this.form, this.emptyForm)
            this.$emit('reset')
        }
    }
}
</script>

<style scoped>

</style>
