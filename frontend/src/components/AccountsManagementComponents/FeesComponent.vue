<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <DatePicker label="日期"
                                v-model="form.entryDate"
                                :disabled="modificationMode">
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
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-select v-model="form.isBookKeeping"
                              :items="bookKeepingOptions"
                              item-value="value"
                              item-text="label"
                              label="记账"
                              hide-details="auto"
                              outlined dense
                              style="width: 90px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.isVerified"
                              :items="verificationOptions"
                              item-value="value"
                              item-text="label"
                              label="标志"
                              hide-details="auto"
                              outlined dense
                              style="width: 90px">
                    </v-select>
                </v-col>
                <v-spacer></v-spacer>
            </v-row>
            <v-row dense>
                <v-col v-if="!hideSourceBank" cols="auto">
                    <v-select v-model="form.sourceAccountID"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              :label="bankFieldName[0]"
                              hide-details="auto"
                              :readonly="modificationMode"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
                <v-col v-if="!hideDestinationBank" cols="auto">
                    <v-select v-model="form.destinationAccountID"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              :label="bankFieldName[1]"
                              hide-details="auto"
                              :readonly="modificationMode"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.amount"
                                  label="金额"
                                  hide-details="auto"
                                  type="number"
                                  @change="handlePaymentAmountChange"
                                  outlined
                                  :readonly="!hideDetail"
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.number"
                                  label="号码"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row dense>
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

        <v-divider class="my-2"></v-divider>

        <div class="d-flex">
            <v-card v-if="!hideDetail" outlined>
                <v-data-table v-model="tableCurrentRow"
                              :headers="tableHeaders"
                              :items="form.feeDetails"
                              item-key="feeDetailEntryID"
                              show-select
                              single-select
                              checkbox-color="accent"
                              @click:row="tableClick"
                              @item-selected="tableClick2"
                              height="20vh"
                              calculate-widths
                              disable-sort
                              fixed-header
                              hide-default-footer
                              locale="zh-cn"
                              dense>
                    <template v-if="modificationMode" v-slot:item.remark="{ item }">
                        <v-edit-dialog :return-value="item.remark">
                            {{ item.remark }}
                            <template v-slot:input>
                                <v-text-field v-model="item.remark" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)">
                                </v-text-field>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-if="modificationMode" v-slot:item.amount="{ item }">
                        <v-edit-dialog :return-value="item.amount"
                                       @close="handleAmountChange(item)"
                                       @cancel="handleAmountChange(item)"
                                       @submit="handleAmountChange(item)">
                            {{ item.amount }}
                            <template v-slot:input>
                                <v-text-field v-model="item.amount" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)">
                                </v-text-field>
                            </template>
                        </v-edit-dialog>
                    </template>
                </v-data-table>
            </v-card>

            <v-row class="ml-6">
                <v-spacer v-if="hideDetail"></v-spacer>
                <v-col v-if="!hideDetail" cols="12">
                    <v-dialog v-model="addNewPopup" max-width="600px">
                        <template v-slot:activator="{ on }">
                            <v-btn color="primary" v-on="on">新增条目</v-btn>
                        </template>
                        <FeeDetailComponent :mode="mode" @createDetail="addNew">
                        </FeeDetailComponent>
                    </v-dialog>
                </v-col>
                <v-col v-if="!hideDetail" cols="12">
                    <v-dialog v-model="deletePopup" max-width="300px">
                        <template v-slot:activator="{ on }">
                            <v-btn :disabled="tableCurrentRow.length === 0" color="warning" v-on="on">
                                删除条目
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
                <v-col v-if="!modificationMode" :cols="hideDetail ? 'auto' : '12'">
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
                <v-col v-if="modificationMode" :cols="hideDetail ? 'auto' : '12'">
                    <v-dialog v-model="submitPopup2" max-width="300px">
                        <template v-slot:activator="{ on }">
                            <v-btn color="accent" v-on="on">
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
                <v-col v-if="modificationMode" :cols="hideDetail ? 'auto' : '12'">
                    <v-btn color="accent" @click="reset">
                        放弃修改
                    </v-btn>
                </v-col>
            </v-row>
        </div>
    </v-container>
</template>

<script>
export default {
    name: "FeesComponent",
    components: {
        DatePicker: () => import('~/components/DatePicker'),
        FeeDetailComponent: () => import(/* webpackChunkName: "FeeDetailComponent" */
            '~/components/AccountsManagementComponents/FeeDetailComponent')
    },
    props: {
        mode: {
            type: String,
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
                if (!val.hasOwnProperty('feeEntryID')) return
                if (val.feeEntryID === '') return
                this.form = JSON.parse(JSON.stringify(val))
            },
            deep: true,
            immediate: false
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'bank':
            break
        case 'income':
            this.hideSourceBank = true
            this.hideDetail = false
            break
        case 'expenditure':
            this.hideDestinationBank = true
            this.hideDetail = false
            break
        case 'salary':
            this.hideDestinationBank = true
            break
        }

        this.$store.dispatch('getDepartmentOptions')
        this.$store.dispatch('getBankAccounts')
        Object.assign(this.emptyForm, this.form)
    },
    data() {
        return {
            hideSourceBank: false,
            hideDestinationBank: false,
            hideDetail: true,

            addNewPopup: false,
            deletePopup: false,
            submitPopup: false,
            submitPopup2: false,

            bookKeepingOptions: [
                { value: 0, label: '保存' },
                { value: 1, label: '审核' }
            ],
            verificationOptions: [
                { value: 0, label: '否' },
                { value: 1, label: '是' }
            ],

            form: {
                feeEntryID: '',
                entryDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                drawer: this.$store.getters.currentUser,
                departmentID: -1, departmentName: '',
                sourceAccountID: -1, destinationAccountID: -1,
                amount: '0', number: '', remark: '',
                isBookKeeping: 0, isVerified: 0,
                feeDetails: [],
            },
            emptyForm: {},

            rules: {
                departmentID: [v => v > 0 || '请选择部门'],
                bankAccountID: [v => v > 0 || '请选择银行'],
            },

            tableHeaders: [
                { text: '类别', value: 'feeCategoryName', width: '180px' },
                { text: '摘要', value: 'remark', width: '260px' },
                { text: '金额', value: 'amount', width: '110px' },
            ],
            tableCurrentRow: [],
        }
    },
    computed: {
        bankAccountOptions() {
            return this.$store.state.visibleBankAccounts
        },
        departmentOptions() {
            return  this.$store.state.departmentOptions
        },
        bankFieldName() {
            switch (this.mode) {
            case 'bank': return ['转出账户', '转入账户']
            case 'income': return ['', '收款去向']
            case 'expenditure': return ['账户名称', '']
            case 'salary': return ['付款来源', '']
            default: return ['', '']
            }
        },
        modificationMode() {
            return this.form.feeEntryID !== ''
        }
    },
    methods: {
        handlePaymentAmountChange(item) {
            this.form.amount = this.$validateFloat(item, true)
        },
        tableClick(row) {
            if (this.tableCurrentRow.includes(row)) {
                this.tableCurrentRow = []
            }
            else {
                this.tableCurrentRow = [row]
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.tableCurrentRow = []
            }
            else {
                this.tableCurrentRow = [row.item]
            }
        },
        calculateTotalPrice() {
            let total = this.$Big('0')
            for (const item of this.form.feeDetails) {
                total = total.add(item.amount)
            }
            this.form.amount = total.toString()
        },
        handleAmountChange(item) {
            item.amount = this.$validateFloat(item.amount, true)
            this.calculateTotalPrice()
        },
        addNew(val) {
            if (val) {
                this.form.feeDetails.push(val)
                this.calculateTotalPrice()
            }
            this.addNewPopup = false
        },
        handleDelete() {
            this.deletePopup = false
            if (this.tableCurrentRow.length === 0) return
            for (const item of this.tableCurrentRow) {
                this.form.feeDetails.splice(this.form.feeDetails.indexOf(item), 1)
            }
            this.calculateTotalPrice()
            this.tableCurrentRow = []
        },
        saveNew() {
            this.submitPopup = false

            if (this.form.feeEntryID !== '') {
                this.$warningMessage('不能新增')
                return
            }

            if (!this.hideDetail && this.form.feeDetails.length === 0) {
                this.$warningMessage('明细不能为空')
                return
            }

            if (this.$refs.form.validate()) {
                this.$putRequest(this.$api.createFeeEntry, this.form, {
                    prefix: this.mode
                }).then(() => {
                    this.$saveSuccessMessage()
                }).catch(() => {})
            }
        },
        saveChange() {
            this.submitPopup2 = false

            if (!this.hideDetail && this.form.feeDetails.length === 0) {
                this.$warningMessage('明细不能为空')
                return
            }

            if (this.$refs.form.validate()) {
                this.$patchRequest(this.$api.updateFeeEntry, this.form, {
                    prefix: this.mode
                }).then(() => {
                    this.$saveSuccessMessage()
                }).catch(() => {})
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
