<template>
    <v-container>
        <v-form @keyup.enter.native="query">
            <v-row>
                <v-col cols="auto">
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
                <v-col cols="auto">
                    <v-menu close-on-content-click
                            :nudge-right="40"
                            transition="scale-transition"
                            offset-y>
                        <template v-slot:activator="{on}">
                            <v-text-field v-model="invoiceNumberDate"
                                          v-on="on"
                                          label="发票开具日期"
                                          hide-details="auto"
                                          outlined
                                          readonly
                                          dense
                                          style="width: 150px">
                            </v-text-field>
                        </template>
                        <v-date-picker v-model="invoiceNumberDate"
                                       no-title
                                       :first-day-of-week="0"
                                       locale="zh-cn">
                        </v-date-picker>
                    </v-menu>
                </v-col>
                <v-col>
                    <v-btn color="warning" @click="clearSearchFields1">
                        清空
                    </v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="companyName"
                                  label="单位简称"
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
                <v-col cols="auto">
                    <v-select v-model="isFollowUpIndication"
                              :items="followUpIndicationOptions"
                              item-value="value"
                              item-text="label"
                              label="补票标志"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col>
                    <v-btn color="warning" @click="clearSearchFields2">
                        清空
                    </v-btn>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="invoiceNumber"
                                  label="发票号码"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="发票类别"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-btn class="mr-4"
                           color="warning"
                           @click="clearSearchFields3">
                        清空
                    </v-btn>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-btn class="mr-4"
                           color="primary"
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

        <v-data-table v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="invoiceEntrySerial"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.checkoutEntrySerial="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.invoiceEntrySerial }}
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
    name: "InvoiceQueryDisplayComponent",
    components: {
        CompanySearch: () => import('~/components/CompanySearch'),
        DateRangePicker
    },
    props: {
        displayMode: {
            type: String,
            required: true,
        },
        isInbound: {
            type: Boolean,
            required: true
        },
    },
    beforeMount() {
        switch (this.displayMode) {
        case 'query':
            break
        case 'modify':
            this.isModify = true
            break
        }
    },
    data() {
        return {
            isModify: false,

            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                new Date().format("yyyy-MM-dd").substr(0,10)
            ],
            invoiceNumberDate: '',

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,
            isFollowUpIndication: -1,
            followUpIndicationOptions: [
                { value: 0, label: '普通'},
                { value: 1, label: '补票'}
            ],

            invoiceNumber: '',
            invoiceType: '',
            invoiceTypeOptions: [
                { value: '增值税票', label: '增值税票' },
                { value: '普票', label: '普票' },
                { value: '收据', label: '收据' },
            ],

            queryTableHeaders: [
                { text: '开票单号', value: 'checkoutEntrySerial', width: '120px' },
                { text: '开票单位', value: 'companyFullName', width: '120px' },
                { text: '开票类型', value: 'invoiceType', width: '65' },
                { text: '发票日期', value: 'invoiceNumberDate', width: '80px' },
                { text: '发票号码', value: 'invoiceNumber', width: '60px' },
                { text: '开票金额', value: 'invoiceAmount', width: '60px' },
                { text: '结账金额', value: 'totalAmount', width: '85px' },
                { text: '开单人', value: 'drawer', width: '65px' },
                { text: '备注', value: 'remark', width: '120px' },
                { text: '开单日期', value: 'invoiceDate', width: '120px' },
            ],
            queryTableData: [],
            queryTableCurrentRow: [],

            modificationRecordTableHeader: [
                { text: '修改日期', value: 'modificationDate', width: '180px' },
                { text: '修改明细', value: 'recordContent', width: '' }
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
                this.companyName = val.hasOwnProperty('abbreviatedName') ?
                    val.abbreviatedName : ''
                this.companyID = val.hasOwnProperty('companyID') ?
                    val.companyID : ''
            }
            this.companySearchPanelOpen = false
        },
        clearSearchFields1() {
            this.invoiceNumberDate = ''
        },
        clearSearchFields2() {
            this.companyID = -1
            this.companyName = ''
            this.isFollowUpIndication = -1
        },
        clearSearchFields3() {
            this.invoiceNumber = ''
            this.invoiceType = ''
        },
        query() {
            this.$getRequest(this.$api.invoiceEntriesInDateRange, {
                isInbound: this.isInbound,
                startDate: this.dateRange[0],
                endDate: this.dateRange[1],
                invoiceNumberDate: this.invoiceNumberDate,
                companyID: this.companyID,
                isFollowUpIndication: this.isFollowUpIndication,
                invoiceNumber: this.invoiceNumber,
                invoiceType: this.invoiceType,
                forModify: this.isModify,
            }).then((res) => {
                console.log(res.data)
                this.queryTableData = res.data
            }).catch(error => this.$ajaxErrorHandler(error))
        },
        queryModificationRecord() {
            this.$getRequest(this.$api.modificationRecordsBySerial
                + encodeURI(this.queryTableCurrentRow[0].invoiceEntrySerial)
            ).then((res) => {
                console.log('received', res.data)
                this.modificationRecords = res.data
            }).catch(error => this.$ajaxErrorHandler(error))
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
