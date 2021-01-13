<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <v-menu close-on-content-click
                            :disabled="displayMode || modifyMode"
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
                                          dense
                                          style="width: 150px">
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
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="供货单位简称"
                                  hide-details="auto"
                                  outlined
                                  :rules="rules.company"
                                  readonly
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyPhone"
                                  label="电话"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 200px">
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
                <v-col cols="auto" v-if="createMode">
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
                <v-col cols="auto">
                    <v-select v-if="createMode || modifyMode"
                              v-model="form.invoiceIndication"
                              :items="taxDeductionOptions"
                              :rules="rules.taxDeduction"
                              item-value="value"
                              item-text="label"
                              label="抵扣标志"
                              hide-details="auto"
                              outlined dense
                              style="width: 120px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.invoiceIndication"
                                  label="开票标志"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
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
                                  label="运费总金额"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-if="createMode"
                              v-model="form.shippingCostType"
                              :items="shippingCostTypeOptions"
                              :rules="rules.shippingCostType"
                              item-value="value"
                              item-text="label"
                              label="运费标志"
                              :readonly="table1Selection.length !== 0 || table2Selection.length !== 0"
                              hide-details="auto"
                              outlined dense
                              style="width: 120px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.shippingCostType"
                                  label="运费标志"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
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

        <v-row v-if="createMode">
            <v-spacer></v-spacer>
            <v-col>
                <v-btn color="primary"
                       @click="createEntry()">
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

        <v-row class="d-flex">
            <v-col>
                <v-card outlined>
                    <v-data-table v-model="table1Selection"
                                  :headers="tableHeaders1"
                                  :items="form.inboundEntries"
                                  item-key="inboundEntryID"
                                  height="30vh"
                                  calculate-widths
                                  :show-select="!displayMode"
                                  disable-sort
                                  fixed-header
                                  disable-pagination
                                  hide-default-footer
                                  locale="zh-cn">
                        <template v-slot:item.index="{ item }">
                            {{ form.inboundEntries.indexOf(item) + 1 }}
                        </template>
                    </v-data-table>
                </v-card>
                <v-row>
                    <v-col cols="auto">
                        <v-dialog v-model="inboundEntryPanel"
                                  :eager="true"
                                  max-width="50vw"
                                  no-click-animation
                                  persistent>
                            <template v-slot:activator="{ on }">
                                <v-btn color="accent" v-on="on">增加</v-btn>
                            </template>
                            <EntryChoose :isInbound="true"
                                         :companyID="form.partnerCompanyID"
                                         :shippingCostType="form.shippingCostType"
                                         @inboundEntryChoose="inboundEntryChooseHandle">
                            </EntryChoose>
                        </v-dialog>
                    </v-col>
                    <v-col cols="auto">
                        <v-dialog v-model="deleteTable1RowPopup"
                                  max-width="300px"
                                  persistent>
                            <template v-slot:activator="{ on }">
                                <v-btn color="warning" v-on="on">删除</v-btn>
                            </template>
                            <v-card>
                                <v-card-title>确认删除？</v-card-title>
                                <v-card-text>{{rowDeletionConfirm1}}</v-card-text>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" @click="deleteTable1RowPopup = false">
                                        取消
                                    </v-btn>
                                    <v-btn color="success" @click="handleDeleteTable1Row">
                                        确认
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-col>
                </v-row>
            </v-col>
            <v-col>
                <v-card outlined>
                    <v-data-table v-model="table2Selection"
                                  :headers="tableHeaders2"
                                  :items="form.outboundEntries"
                                  item-key="outboundEntryID"
                                  height="30vh"
                                  calculate-widths
                                  :show-select="!displayMode"
                                  disable-sort
                                  fixed-header
                                  disable-pagination
                                  hide-default-footer
                                  locale="zh-cn">
                        <template v-slot:item.index="{ item }">
                            {{ form.outboundEntries.indexOf(item) + 1 }}
                        </template>
                    </v-data-table>
                </v-card>
                <v-row>
                    <v-col cols="auto">
                        <v-dialog v-model="outboundEntryPanel"
                                  :eager="true"
                                  max-width="50vw"
                                  no-click-animation
                                  persistent>
                            <template v-slot:activator="{ on }">
                                <v-btn color="accent" v-on="on">增加</v-btn>
                            </template>
                            <EntryChoose :isInbound="false"
                                         :companyID="form.partnerCompanyID"
                                         :shippingCostType="form.shippingCostType"
                                         @outboundEntryChoose="outboundEntryChooseHandle">
                            </EntryChoose>
                        </v-dialog>
                    </v-col>
                    <v-col cols="auto">
                        <v-dialog v-model="deleteTable2RowPopup"
                                  max-width="300px"
                                  persistent>
                            <template v-slot:activator="{ on }">
                                <v-btn color="warning" v-on="on">删除</v-btn>
                            </template>
                            <v-card>
                                <v-card-title>确认删除？</v-card-title>
                                <v-card-text>{{rowDeletionConfirm2}}</v-card-text>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" @click="deleteTable2RowPopup = false">
                                        取消
                                    </v-btn>
                                    <v-btn color="success" @click="handleDeleteTable2Row">
                                        确认
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-col>
                </v-row>
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
    name: "ShippingCostComponent",
    components: {
        CompanySearch: () => import("~/components/CompanySearch"),
        EntryChoose: () => import("~/components/InvoiceComponents/ShippingCostEntryChoose")
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
            required: false,
        }
    },
    watch: {

    },
    beforeMount() {
        switch (this.mode) {
        case 'create':
            this.createMode = true
            break
        case 'modify':
            this.modifyMode = true
            break
        case 'display':
            this.displayMode = true
            break
        }
    },
    data() {
        return {
            createMode: false,
            displayMode: false,
            modifyMode: false,

            fullSearchPanelOpen: false,
            inboundEntryPanel: false,
            outboundEntryPanel: false,
            deleteTable1RowPopup: false,
            deleteTable2RowPopup: false,

            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),

            form: {
                shippingCostEntrySerial: null,
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyFullName: '', companyPhone: '',
                isTaxDeduction: -1, invoiceNumber: '',
                totalAmount: 0.0, invoiceAmount: '',
                shippingCostType: '',
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                inOrOut: this.isInbound ? '入' : '出',
                invoiceDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                isModified: 0,

                inboundEntries: [],
                outboundEntries: [],
            },

            rules: {
                company: [v => !!v || '请选择单位'],
                taxDeduction: [v => !!v || '请选择抵扣类型'],
                shippingCostType: [v => !!v || '请选择运费标志'],
            },

            shippingCostTypeOptions: [
                { value: '自付', label: '自付' },
                { value: '代垫', label: '代垫' }
            ],
            taxDeductionOptions: [
                { value: '否', label: '否' },
                { value: '抵扣', label: '抵扣' }
            ],


            tableHeaders1: [
                { text: '序号', value: 'index', width: '60px' },
                { text: '入库单号', value: 'inboundEntryID', width: '100px' },
                { text: '单位', value: 'companyAbbreviatedName', width: '100px' },
                { text: '运费', value: 'shippingCost', width: '80px' },
            ],
            tableHeaders2: [
                { text: '序号', value: 'index', width: '60px' },
                { text: '出库单号', value: 'outboundEntryID', width: '100px' },
                { text: '单位', value: 'companyAbbreviatedName', width: '100px' },
                { text: '运费', value: 'shippingCost', width: '80px' },
            ],
            table1Selection: [],
            table2Selection: [],

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

        inboundEntryChooseHandle(val) {
            if (val) this.form.inboundEntries = JSON.parse(JSON.stringify(val))
            this.inboundEntryPanel = false
            this.calculateShippingCost()
        },
        outboundEntryChooseHandle(val) {
            if (val) this.form.outboundEntries = JSON.parse(JSON.stringify(val))
            this.outboundEntryPanel = false
            this.calculateShippingCost()
        },
        calculateShippingCost() {
            this.totalAmount = 0
            this.form.inboundEntries.forEach((item) => {
                this.totalAmount += Number(item.shippingCost)
            })
            this.form.outboundEntries.forEach((item) => {
                this.totalAmount += Number(item.shippingCost)
            })
        },
        handleDeleteTable1Row() {
            this.deleteTable1RowPopup = false
            if (this.table1Selection.length !== 0) {
                for (const item of this.table1Selection) {
                    this.form.inboundEntries.splice(this.form.inboundEntries.indexOf(item), 1)
                }
                this.table1Selection = []
            } else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        handleDeleteTable2Row() {
            this.deleteTable2RowPopup = false
            if (this.table2Selection.length !== 0) {
                for (const item of this.table2Selection) {
                    this.form.outboundEntries.splice(this.form.outboundEntries.indexOf(item), 1)
                }
                this.table2Selection = []
            } else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },

        createEntry() {

        },
        modifyEntry() {

        },
    },
    computed: {
        rowDeletionConfirm1() {
            let result = '确认删除以下序号的行: '
            this.table1Selection.forEach(item => {
                result += `${(this.form.inboundEntries.indexOf(item) + 1).toString()} `
            })
            return result
        },
        rowDeletionConfirm2() {
            let result = '确认删除以下序号的行: '
            this.table2Selection.forEach(item => {
                result += `${(this.form.outboundEntries.indexOf(item) + 1).toString()} `
            })
            return result
        },
    }
}
</script>

<style scoped>

</style>
