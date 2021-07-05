<template>
    <v-container>
        <v-form @keyup.enter="query">
            <v-row>
                <v-col>
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
            </v-row>
            <v-row class="my-2">
                <v-col cols="auto">
                    <v-text-field v-model="companyName"
                                  label="单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="companySearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="primary" v-on="on">
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
                <v-col cols="auto" class="d-flex">
                    <v-btn class="mr-2" color="warning" @click="clearSearchFields">清空</v-btn>
                    <v-btn class="mr-2" color="primary" @click="query">查询</v-btn>
                    <v-btn color="accent" @click="queryModificationRecord">查询修改记录</v-btn>
                </v-col>
            </v-row>
        </v-form>

        <v-data-table v-model="queryTableCurrentRow"
                      :headers="isSalesOrder ? salesQueryTableHeaders :
                                    isQuote ? quoteQueryTableHeaders : queryTableHeaders"
                      :items="queryTableData"
                      :item-key="isSalesOrder ? 'salesOrderEntryID' :
                                    isQuote ? 'quoteEntryID' : 'outboundEntryID'"
                      show-select
                      single-select
                      checkbox-color="accent"
                      @click:row="tableClick"
                      @item-selected="tableClick2"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      hide-default-footer
                      locale="zh-cn"
                      dense>
            <template v-if="!isSalesOrder && !isQuote" v-slot:item.outboundEntryID="{ item }">
                <v-chip :color="chipColor(item)">
                    {{ item.outboundEntryID }}
                </v-chip>
            </template>
            <template v-else-if="isSalesOrder" v-slot:item.salesOrderEntryID="{ item }">
                <v-chip :color="chipColor(item)">
                    {{ item.salesOrderEntryID }}
                </v-chip>
            </template>
            <template v-else-if="isQuote" v-slot:item.quoteEntryID="{ item }">
                <v-chip :color="chipColor(item)">
                    {{ item.quoteEntryID }}
                </v-chip>
            </template>
        </v-data-table>
        <div>
            <strong class="red--text">红色：</strong>修改，
            <strong class="blue--text">蓝色：</strong>退货，
            <strong class="orange--text">橘色：</strong>退货并且修改
        </div>
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
                {text: '出库单号', value: 'outboundEntryID', width: '120px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '仓库', value: 'warehouseName', width: '65px'},
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
                this.companyName = val.abbreviatedName
                this.companyID = val.companyID
            }
            this.companySearchPanelOpen = false
        },
        clearSearchFields() {
            this.category = ''
            this.companyID = -1
            this.companyName = ''
        },
        query() {
            if (this.category === '') return
            if (this.isSalesOrder) {
                this.$getRequest(this.$api.salesOrdersInDateRangeByCompanyID, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    companyID: this.companyID,
                }).then((data) => {
                    this.queryTableData = data
                }).catch(() => {})
            }
            else if (this.isQuote) {
                this.$getRequest(this.$api.quotesInDateRangeByCompanyID, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    companyID: this.companyID,
                }).then((data) => {
                    this.queryTableData = data
                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.outboundEntriesInDateRange, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    companyID: this.companyID,
                    type: this.category,
                    forModify: false,
                }).then((data) => {
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
                this.modificationRecords = data
            }).catch(() => {})
        },
        chipColor(item) {
            let color = null
            if (item.isModified === 1) {
                color = 'red'
                if (item.returnDate !== '') color = 'orange'
            }
            else if (item.returnDate !== '') {
                color = 'blue'
            }
            return color
        },
        tableClick(val) {
            this.modificationRecords = []
            if (this.queryTableCurrentRow.indexOf(val) !== -1) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [val]
                this.$emit('tableClick', val)
            }
        },
        tableClick2(row) {
            this.modificationRecords = []
            if (!row.value) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [row.item]
                this.$emit('tableClick', row.item)
            }
        }
    }
}
</script>

<style scoped>

</style>
