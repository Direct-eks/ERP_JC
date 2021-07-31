<template>
    <v-container>
        <v-form ref="form">
            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="单位简称"
                                  hide-details="auto"
                                  outlined
                                  :rules="rules.company"
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
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
                <v-col cols="auto">
                    <DatePicker label="办理日期"
                                v-model="form.entryDate">
                    </DatePicker>
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
            </v-row>
            <v-row dense>
                <v-col cols="auto">
                    <v-select v-model="form.type"
                              :rules="rules.type"
                              :items="typeOptions"
                              label="汇票类型"
                              hide-details="auto"
                              outlined dense
                              style="width: 100px">
                    </v-select>
                </v-col>
                <v-col v-if="!isInbound" cols="auto">
                    <v-select v-model="form.source"
                              :rules="rules.source"
                              :items="sourceOptions"
                              label="汇票来源"
                              hide-details="auto"
                              outlined dense
                              style="width: 120px">
                    </v-select>
                </v-col>
                <v-col v-if="!isInbound && form.source === '本单位'" cols="auto">
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
                <v-col v-if="!isInbound && form.source === '外单位'" cols="auto">
                    <v-text-field v-model="form.number"
                                  label="单号"
                                  hide-details="auto"
                                  outlined
                                  :rules="rules.number"
                                  @keyup.enter="trigger"
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col v-if="!isInbound && form.source === '外单位'" cols="auto">
                    <v-dialog v-model="importPopup" max-width="300px">
                        <template v-slot:activator="{ on }">
                            <v-btn color="accent" v-on="on" @click="trigger">检索</v-btn>
                        </template>
                        <EntryImport :number="form.number"
                                     :trigger="triggerSearch"
                                     @importEntry="importEntry">
                        </EntryImport>
                    </v-dialog>
                </v-col>
            </v-row>
            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="form.amount"
                                  label="金额"
                                  hide-details="auto"
                                  type="number"
                                  @change="handleAmountChange"
                                  outlined
                                  :rules="rules.amount"
                                  dense
                                  style="width: 140px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.number"
                                  label="承兑票号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <DatePicker label="出票日期"
                                v-model="form.issueDate">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <DatePicker label="到期日期"
                                v-model="form.expirationDate"
                                :allowFutureDates="true">
                    </DatePicker>
                </v-col>
            </v-row>
            <v-row dense>
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
            <v-col cols="auto">
                <v-dialog v-model="deletePopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="warning" v-on="on">删除</v-btn>
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
            <v-col cols="auto">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存新单据</v-btn>
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
            <v-col cols="auto">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="accent" v-on="on">修改</v-btn>
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
    name: "AcceptanceBillComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker"),
        CompanySearch: () => import("~/components/CompanySearch"),
        EntryImport: () => import("~/components/AccountsManagementComponents/AcceptanceImportByNumberComponent")
    },
    props: {
        isInbound: {
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
    },
    data() {
        return {
            fullSearchPanelOpen: false,
            importPopup: false,
            triggerSearch: false,
            deletePopup: false,
            submitPopup: false,
            submitPopup2: false,

            typeOptions: ['银行', '商业'],
            sourceOptions: ['本单位', '外单位'],

            form: {
                acceptanceEntrySerial: '',
                partnerCompanyID: -1, companyAbbreviatedName: '',
                entryDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                departmentID: -1, departmentName: '',
                source: '',
                bankAccountID: -1, bankAccountName: '',
                sourceSerial: '',
                amount: '', number: '',
                issueDate: '', expirationDate: '',
                type: '银行', drawer: this.$store.getters.currentUser,
                remark: '', classification: '', status: 0,
            },
            rules: {
                company: [v => !!v || '请选择单位'],
                departmentID: [v => v !== -1 || '请选择部门'],
                type: [v => !!v || '请选择类型'],
                source: [v => !!v || '请选择来源'],
                bankAccountID: [v => !!v && this.form.source === '本公司' || '请选择银行'],
                amount: [v => v !== '' || '金额不正确'],
            },
        }
    },
    computed: {
        departmentOptions() {
            const options = this.$store.state.departmentOptions
            for (const item of options) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
            return options
        },
        bankAccountOptions() {
            return this.$store.state.visibleBankAccounts
        },
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
        trigger() {
            this.triggerSearch = true
            this.importPopup = true
        },
        importEntry(val) {
            if (val) {

            }
            this.triggerSearch = false
            this.importPopup = false
        },
        handleAmountChange(item) {
            this.form.amount = this.$validateFloat(item, true)
        },
        handleDelete() {

        },
        saveNew() {
            this.submitPopup = false

            if (this.form.acceptanceEntrySerial !== '') {
                this.$store.commit('setSnackbar', {
                    message: '不能新增', color: 'warning'
                })
                return
            }

            if (this.$refs.form.validate()) {
                this.$putRequest(this.$api.createAcceptanceEntry, this.form, {
                    isInbound: this.isInbound
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '保存成功', color: 'success'
                    })
                }).catch(() => {})
            }
        },
        saveChange() {

        }
    }

}
</script>

<style scoped>

</style>
