<template>
    <v-container>
        <v-form @keyup.enter.native="query">
            <v-row>
                <v-col>
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="companyName"
                                  label="单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="companySearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="primary"
                                   v-on="on"
                                   :disabled="companySearchPanelOpen">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch @fullSearchChoose="companySearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
                <v-col cols="auto" v-if="!isSalesOrder && !isQuote">
                    <v-select v-model="category"
                              :items="categoryOptions"
                              item-value="value"
                              item-text="label"
                              label="类别"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="warning"
                           @click="clearSearchFields">
                        清空
                    </v-btn>
                    <v-btn color="primary"
                           @click="query">
                        查询
                    </v-btn>
                    <v-btn color="accent"
                           @click="queryModificationRecord">
                        查询修改记录
                    </v-btn>
                </v-col>
            </v-row>
        </v-form>

        <v-data-table v-if="!isSalesOrder && !isQuote"
                      v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="outboundEntryID"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.outboundEntryID="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.outboundEntryID }}
                </v-chip>
            </template>
        </v-data-table>

        <v-data-table v-else-if="isSalesOrder"
                      v-model="queryTableCurrentRow"
                      :headers="salesQueryTableHeaders"
                      :items="queryTableData"
                      item-key="salesOrderEntryID"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.salesOrderEntryID="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.salesOrderEntryID }}
                </v-chip>
            </template>
        </v-data-table>

        <v-data-table v-else
                      v-model="queryTableCurrentRow"
                      :headers="quoteQueryTableHeaders"
                      :items="queryTableData"
                      item-key="quoteEntryID"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.quoteEntryID="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.quoteEntryID }}
                </v-chip>
            </template>
        </v-data-table>

        <v-divider></v-divider>

        <v-data-table :headers="modificationRecordTableHeader"
                      :items="modificationRecords"
                      item-key="modificationRecordID"
                      disable-sort
                      hide-default-footer
                      locale="zh-cn">
        </v-data-table>

    </v-container>
</template>

<script>
import DateRangePicker from "~/components/DateRangePicker";

export default {
    name: "OutboundQueryDisplayComponent",
    components: {
        CompanySearch: () => import('~/components/CompanySearch'),
        DateRangePicker,
    },
    props: {
        displayMode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        switch (this.displayMode) {
        case 'completion':
        case 'query':
        case 'return':
            break
        case 'modify':
            this.isModify = true
            break
        case 'salesOrder':
            this.isSalesOrder = true
            break
        case 'quote':
            this.isQuote = true
            break
        }
    },
    data() {
        return {
            isModify: false,
            isSalesOrder : false,
            isQuote: false,

            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0, 10),
                new Date().format("yyyy-MM-dd").substr(0, 10)
            ],

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,

            category: '销出',
            categoryOptions: [
                { value: '销出', label: '销出' },
                { value: '入退', label: '入退' }
            ],

            queryTableHeaders: [
                {text: '出库单号', value: 'outboundEntryID', width: '80px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '仓库', value: 'warehouseName', width: '65'},
                {text: '部门', value: 'departmentName', width: '80px'},
                {text: '出库类别', value: 'classification', width: '60px'},
                {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                {text: '总金额', value: 'totalAmount', width: '85px'},
                {text: '提货方式', value: 'deliveryMethod', width: '85px'},
                {text: '运输方式', value: 'relevantCompanyName', width: '100px'},
                {text: '运单号', value: 'shippingNumber', width: '100px'},
                {text: '运费', value: 'shippingCost', width: '65px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '开单人', value: 'drawer', width: '60px'},
                {text: '开单日期', value: 'shipmentDate', width: '80px'},
                {text: '运费标志', value: 'shippingCostType', width: '60px'}
            ],
            salesQueryTableHeaders: [
                {text: '销订单号', value: 'salesOrderEntryID', width: '80px'},
                {text: '订单状态', value: 'executionStatus', width: '65px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '仓库', value: 'warehouseName', width: '65'},
                {text: '部门', value: 'departmentName', width: '80px'},
                {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                {text: '总金额', value: 'totalAmount', width: '85px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '开单人', value: 'drawer', width: '60px'},
                {text: '开单日期', value: 'shipmentDate', width: '80px'},
                {text: '创建日期', value: 'creationDate', width: '80px'},
            ],
            quoteQueryTableHeaders: [
                {text: '报价单号', value: 'quoteEntryID', width: '80px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                {text: '总金额', value: 'totalAmount', width: '85px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '开单人', value: 'drawer', width: '60px'},
                {text: '开单日期', value: 'shipmentDate', width: '80px'},
                {text: '创建日期', value: 'creationDate', width: '80px'},
            ],

            queryTableData: [],
            queryTableCurrentRow: [],

            modificationRecordTableHeader: [
                {text: '修改日期', value: 'modificationDate', width: '180px'},
                {text: '修改明细', value: 'recordContent', width: ''}
            ],
            modificationRecords: []
        }
    },
    methods: {
        chooseDateAction(val) {
            this.dateRange = val
        },
        companySearchChooseAction(val) {
            if (val) {
                this.companyName = val.hasOwnProperty('fullName') ?
                    val.fullName : ''
                this.companyID = val.hasOwnProperty('companyID') ?
                    val.companyID : ''
            }
            this.companySearchPanelOpen = false
        },
        clearSearchFields() {
            this.category = ''
            this.companyID = -1
            this.companyName = ''
        },
        query() {
            if (this.isSalesOrder) {
                console.log(this.dateRange)
                this.$getRequest(this.$api.salesOrdersInDateRangeByCompanyID, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    companyID: this.companyID,
                }).then((data) => {
                    console.log('received', data)
                    this.queryTableData = data
                }).catch(() => {})
            }
            else if (this.isQuote) {
                console.log(this.dateRange)
                this.$getRequest(this.$api.quotesInDateRangeByCompanyID, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    companyID: this.companyID,
                }).then((data) => {
                    console.log('received', data)
                    this.queryTableData = data
                }).catch(() => {})
            }
            else {
                console.log(this.dateRange)
                this.$getRequest(this.$api.outboundEntriesInDateRange, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    companyID: this.companyID,
                    type: this.category,
                    forModify: false,
                }).then((data) => {
                    console.log('received', data)
                    this.queryTableData = data
                }).catch(() => {})
            }
        },
        queryModificationRecord() {
            let url
            if (this.isSalesOrder) {
                url = encodeURI(this.queryTableCurrentRow[0].salesOrderEntryID)
            }
            else if (this.isQuote) {
                url = encodeURI(this.queryTableCurrentRow[0].quoteEntryID)
            }
            else {
                url = encodeURI(this.queryTableCurrentRow[0].outboundEntryID)
            }

            this.$getRequest(this.$api.modificationRecordsBySerial + url).then((data) => {
                console.log('received', data)
                this.modificationRecords = data
            }).catch(() => {})
        },
        tableClick(val) {
            this.modificationRecords = []
            this.queryTableCurrentRow = [val]
            this.$emit('tableClick', val)
        },
    }
}
</script>

<style scoped>

</style>
