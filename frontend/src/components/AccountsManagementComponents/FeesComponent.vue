<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <DatePicker label="日期"
                                v-model="form.entryDate"
                                :disabled="displayMode">
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
                              :readonly="displayMode"
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
                              :readonly="displayMode"
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
                              :readonly="displayMode"
                              outlined dense
                              style="width: 90px">
                    </v-select>
                </v-col>
                <v-spacer></v-spacer>
            </v-row>
            <v-row dense>
                <v-col v-if="!hideSourceBank" cols="auto">
                    <v-select v-model="form.source_account_id"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              :label="bankFieldName[0]"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
                <v-col v-if="!hideDestinationBank" cols="auto">
                    <v-select v-model="form.destination_account_id"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              :label="bankFieldName[1]"
                              hide-details="auto"
                              :readonly="displayMode"
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
                                  :readonly="displayMode"
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
                                  :readonly="displayMode"
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row dense>
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
        <v-divider class="mt-3 mb-1"></v-divider>
        <v-data-table v-if="!hideDetail"
                      v-model="tableCurrentRow"
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="feeDetailEntryID"
                      show-select
                      single-select
                      checkbox-color="accent"
                      @click:row="tableClick"
                      @item-selected="tableClick2"
                      height="25vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      hide-default-footer
                      locale="zh-cn"
                      dense>
        </v-data-table>
    </v-container>
</template>

<script>
export default {
    name: "FeesComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker")
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

            },
            deep: true,
            immediate: true
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
    },
    data() {
        return {
            displayMode: false,
            hideSourceBank: false,
            hideDestinationBank: false,
            hideDetail: true,

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
                departmentID: -1,
                source_account_id: -1, destination_account_id: -1,
                amount: '0', number: '', remark: '',
                isBookKeeping: 0, isVerified: 0,
                feeDetails: [],
            },

            rules: {
                departmentID: [v => v > 0 || '请选择部门'],
                bankAccountID: [v => v > 0 || '请选择银行'],
            },

            tableHeaders: [
                { text: '类别', value: 'feeCategoryName', width: '110px' },
                { text: '摘要', value: 'remark', width: '180px' },
                { text: '金额', value: 'amount', width: '110px' },
            ],
            tableData: [],
            tableCurrentRow: [],
            newItemIndex: -1,
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
        addNew() {
            this.tableData.push({
                feeDetailEntryID: this.newItemIndex--,
                feeEntryID: -1,
                feeCategoryID: -1,
                feeCategoryName: '',
                remark: '',
                amount: '',
            })
        }
    }
}
</script>

<style scoped>

</style>
