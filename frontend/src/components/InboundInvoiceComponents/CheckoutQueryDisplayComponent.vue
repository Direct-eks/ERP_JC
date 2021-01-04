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
                <v-col cols="auto">
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
                    <v-btn class="mr-4"
                           color="warning"
                           @click="clearSearchFields">
                        清空
                    </v-btn>
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
                      item-key="checkoutEntrySerial"
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
                    {{ item.checkoutEntrySerial }}
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
    name: "InboundCheckoutQueryDisplayComponent",
    components: {
        CompanySearch: () => import('~/components/CompanySearch'),
        DateRangePicker
    },
    props: {
        displayMode: {
            type: String,
            required: true,
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

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,

            category: '增值税票',
            categoryOptions: [
                { value: '增值税票', label: '增值税票' },
                { value: '普票', label: '普票' },
                { value: '收据', label: '收据' },
            ],

            queryTableHeaders: [
                {text: '入库结账单号', value: 'checkoutEntrySerial', width: '80px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '结账类型', value: 'invoiceType', width: '65'},
                {text: '入库单运费合计', value: '', width: '80px'},
                {text: '应付总金额', value: 'totalAmount', width: '60px'},
                {text: '余额', value: 'debt', width: '60px'},
                {text: '付款方式', value: 'paymentMethod', width: '85px'},
                {text: '付款金额', value: 'paymentAmount', width: '100px'},
                {text: '付款号码', value: 'paymentNumber', width: '100px'},
                {text: '开单人', value: 'drawer', width: '65px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '开单日期', value: 'checkoutDate', width: '60px'},
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
            this.companyName = ''
        },
        query() {
            this.$getRequest(this.$api.checkoutEntriesInDateRange, {
                startDate: this.dateRange[0],
                endDate: this.dateRange[1],
                invoiceType: this.category,
                companyID: this.companyID,
                forModify: this.isModify,
            }).then((res) => {
                console.log(res.data)
                this.queryTableData = res.data
            })
        },
        queryModificationRecord() {
            this.$getRequest(this.$api.modificationRecordsBySerial
                + encodeURI(this.queryTableCurrentRow[0].checkoutEntrySerial)
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
