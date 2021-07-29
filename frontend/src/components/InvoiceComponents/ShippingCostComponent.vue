<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <DatePicker label="结账日期"
                                v-model="form.checkoutDate"
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
                                  style="width: 120px">
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

            <v-row dense>
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
                                   :disabled="fullSearchPanelOpen || disableCompanySearch
                                                || form.inboundEntries.length !== 0
                                                || form.outboundEntries.length !== 0">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch
                            @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
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
                    <DatePicker label="发票开具日期"
                                v-model="form.invoiceDate"
                                :disabled="displayMode">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.isTaxDeduction"
                              :items="taxDeductionOptions"
                              :rules="rules.taxDeduction"
                              item-value="value"
                              item-text="label"
                              label="抵扣标志"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 120px">
                    </v-select>
                </v-col>
            </v-row>

            <v-row dense>
                <v-col cols="auto">
                    <v-select v-model="form.shippingCostType"
                              :items="shippingCostTypeOptions"
                              :rules="rules.shippingCostType"
                              item-value="value"
                              item-text="label"
                              label="运费标志"
                              :disabled="!createMode || !isInbound"
                              :readonly="table1Selection.length !== 0 || table2Selection.length !== 0"
                              hide-details="auto"
                              outlined dense
                              style="width: 120px">
                    </v-select>
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
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalAmount"
                                  label="运费总金额"
                                  hide-details="auto"
                                  filled
                                  readonly
                                  dense
                                  style="width: 120px">
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

        <v-row dense>
            <v-spacer></v-spacer>
            <v-col v-if="createMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="primary" @click="createEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="modifyMode">
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

        <v-row class="d-flex" dense>
            <v-col v-if="showInboundTable">
                <v-card outlined>
                    <v-card-title class="text-subtitle-1 pt-1 pb-0">入库运费</v-card-title>
                    <v-data-table v-model="table1Selection"
                                  :headers="tableHeaders1"
                                  :items="form.inboundEntries"
                                  item-key="inboundEntryID"
                                  :show-select="!displayMode"
                                  checkbox-color="accent"
                                  @click:row="table1Click"
                                  height="35vh"
                                  calculate-widths
                                  disable-sort
                                  fixed-header
                                  disable-pagination
                                  hide-default-footer
                                  locale="zh-cn">
                    </v-data-table>

                    <v-card-actions v-if="createMode || modifyMode">
                        <v-dialog v-model="inboundEntryPanel"
                                  :eager="true"
                                  max-width="50vw"
                                  no-click-animation
                                  persistent>
                            <template v-slot:activator="{ on }">
                                <v-btn class="mr-2" color="accent" v-on="on">增加</v-btn>
                            </template>
                            <EntryChoose :isInbound="true"
                                         :companyID="form.partnerCompanyID"
                                         :shippingCostType="form.shippingCostType"
                                         @inboundEntryChoose="inboundEntryChooseHandle">
                            </EntryChoose>
                        </v-dialog>

                        <v-dialog v-model="deleteTable1RowPopup" max-width="300px">
                            <template v-slot:activator="{ on }">
                                <v-btn color="warning" v-on="on">删除</v-btn>
                            </template>
                            <v-card>
                                <v-card-title>确认删除选中行？</v-card-title>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" @click="deleteTable1RowPopup = false">取消</v-btn>
                                    <v-btn color="success" @click="handleDeleteTable1Row">确认</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col v-if="showOutboundTable">
                <v-card outlined>
                    <v-card-title class="text-subtitle-1 pt-1 pb-0">出库运费</v-card-title>
                    <v-data-table v-model="table2Selection"
                                  :headers="tableHeaders2"
                                  :items="form.outboundEntries"
                                  item-key="outboundEntryID"
                                  :show-select="!displayMode"
                                  checkbox-color="accent"
                                  @click:row="table2Click"
                                  height="35vh"
                                  calculate-widths
                                  disable-sort
                                  fixed-header
                                  disable-pagination
                                  hide-default-footer
                                  locale="zh-cn">
                    </v-data-table>

                    <v-card-actions v-if="createMode || modifyMode">
                        <v-dialog v-model="outboundEntryPanel"
                                  :eager="true"
                                  max-width="50vw"
                                  no-click-animation
                                  persistent>
                            <template v-slot:activator="{ on }">
                                <v-btn class="mr-2" color="accent" v-on="on">增加</v-btn>
                            </template>
                            <EntryChoose :isInbound="false"
                                         :companyID="form.partnerCompanyID"
                                         :shippingCostType="form.shippingCostType"
                                         @outboundEntryChoose="outboundEntryChooseHandle">
                            </EntryChoose>
                        </v-dialog>
                        <v-dialog v-model="deleteTable2RowPopup" max-width="300px">
                            <template v-slot:activator="{ on }">
                                <v-btn color="warning" v-on="on">删除</v-btn>
                            </template>
                            <v-card>
                                <v-card-title>确认删除选中行？</v-card-title>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" @click="deleteTable2RowPopup = false">取消</v-btn>
                                    <v-btn color="success" @click="handleDeleteTable2Row">确认</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>

    </v-container>
</template>

<script>
export default {
    name: "ShippingCostComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker"),
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
        paramForm: {
            handler: function (val) {
                if (this.createMode) return
                this.form = val
            },
            deep: true,
        },
        'form.shippingCostType'(val) {
            if (val === '自付') {
                this.disableCompanySearch = true
                this.$getRequest(this.$api.selfCompany).then((data) => {
                    this.form.partnerCompanyID = data.companyID
                    this.form.companyAbbreviatedName = data.abbreviatedName
                    this.form.companyFullName = data.fullName
                    this.form.companyPhone = data.phone
                    this.form.companyRemark = data.remark
                }).catch(() => {})
            }
            else if (val === '代垫') {
                this.disableCompanySearch = false
            }
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'create':
            this.createMode = true
            if (!this.isInbound) this.form.shippingCostType = '代垫'
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

            disableCompanySearch: false,

            fullSearchPanelOpen: false,
            inboundEntryPanel: false,
            outboundEntryPanel: false,
            deleteTable1RowPopup: false,
            deleteTable2RowPopup: false,
            submitPopup: false,
            submitPopup2: false,

            form: {
                shippingCostEntrySerial: null,
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyFullName: '',
                companyPhone: '', companyRemark: '',
                isTaxDeduction: -1, invoiceNumber: '',
                totalAmount: 0.0, invoiceAmount: '',
                shippingCostType: '',
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                checkoutDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                inOrOut: this.isInbound ? 1 : 0,
                invoiceDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                isModified: 0,

                inboundEntries: [],
                outboundEntries: [],
            },

            rules: {
                company: [v => !!v || '请选择单位'],
                taxDeduction: [v => v !== -1 || '请选择抵扣类型'],
                shippingCostType: [v => !!v || '请选择运费标志'],
            },

            shippingCostTypeOptions: [
                { value: '自付', label: '自付' },
                { value: '代垫', label: '代垫' }
            ],
            taxDeductionOptions: [
                { value: 0, label: '否' },
                { value: 1, label: '抵扣' }
            ],


            tableHeaders1: [
                { text: '入库单号', value: 'inboundEntryID', width: '140px' },
                { text: '单位', value: 'companyAbbreviatedName', width: '120px' },
                { text: '运费', value: 'shippingCost', width: '80px' },
            ],
            tableHeaders2: [
                { text: '出库单号', value: 'outboundEntryID', width: '140px' },
                { text: '单位', value: 'companyAbbreviatedName', width: '120px' },
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
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.companyFullName = val.fullName
                this.form.companyPhone = val.phone
                this.form.partnerCompanyID = val.companyID
                this.form.companyRemark = val.remark
            }
            this.fullSearchPanelOpen = false
        },
        handleInvoiceAmountChange() {
            this.form.invoiceAmount = this.$validateFloat(this.form.invoiceAmount, true)
        },
        inboundEntryChooseHandle(val) {
            if (val) {
                if (this.createMode) {
                    this.form.inboundEntries = JSON.parse(JSON.stringify(val))
                }
                if (this.modifyMode) {
                    val.forEach((item) => {
                        this.form.inboundEntries.push(JSON.parse(JSON.stringify(item)))
                    })
                }
                this.calculateShippingCost()
            }
            this.inboundEntryPanel = false
        },
        outboundEntryChooseHandle(val) {
            if (val) {
                if (this.createMode) {
                    this.form.outboundEntries = JSON.parse(JSON.stringify(val))
                }
                if (this.modifyMode) {
                    val.forEach((item) => {
                        this.form.outboundEntries.push(JSON.parse(JSON.stringify(item)))
                    })
                }
                this.calculateShippingCost()
            }
            this.outboundEntryPanel = false
        },
        calculateShippingCost() {
            let tempAmount = this.$Big('0')
            this.form.inboundEntries.forEach((item) => {
                tempAmount = tempAmount.add(item.shippingCost)
            })
            this.form.outboundEntries.forEach((item) => {
                tempAmount = tempAmount.add(item.shippingCost)
            })
            this.form.totalAmount = tempAmount.toString()
        },
        table1Click(row) {
            if (this.table1Selection.includes(row)) {
                this.table1Selection.splice(this.table1Selection.indexOf(row), 1)
            }
            else {
                this.table1Selection.push(row)
            }
        },
        table2Click(row) {
            if (this.table2Selection.includes(row)) {
                this.table2Selection.splice(this.table2Selection.indexOf(row), 1)
            }
            else {
                this.table2Selection.push(row)
            }
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
            if (this.$refs.form.validate()) {
                this.$putRequest(this.$api.createShippingCostEntry, this.form, {
                    isInbound: this.isInbound
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    if (this.isInbound) {
                        this.$router.replace('/inbound_invoicing')
                    }
                    else {
                        this.$router.replace('/outbound_invoicing')
                    }
                }).catch(() => {})
            }
            this.submitPopup = false
        },
        modifyEntry() {
            if (this.$refs.form.validate()) {
                this.$patchRequest(this.$api.modifyShippingCostEntry, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    if (this.isInbound) {
                        this.$router.replace('/inbound_invoicing')
                    }
                    else {
                        this.$router.replace('/outbound_invoicing')
                    }
                }).catch(() => {})
            }
            this.submitPopup2 = false
        },
    },
    computed: {
        showInboundTable() {
            return this.isInbound
        },
        showOutboundTable() {
            return (this.isInbound && this.form.shippingCostType === '自付') ||
                    (!this.isInbound && this.form.shippingCostType === '代垫')
        },
    }
}
</script>

<style scoped>

</style>
