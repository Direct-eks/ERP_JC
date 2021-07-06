<template>
    <v-container>
        <v-form @keyup.enter="query">
            <v-row>
                <v-col>
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
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
                            <v-btn color="primary" v-on="on">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch @fullSearchChoose="companySearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
                <v-col cols="auto">
                    <v-btn class="mr-2" color="warning" @click="clearSearchFields">清空</v-btn>
                    <v-btn class="mr-2" color="primary" @click="query">查询</v-btn>
                    <v-btn color="accent" @click="queryModificationRecord">查询修改记录</v-btn>
                </v-col>
            </v-row>
        </v-form>

        <v-data-table v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="shippingCostEntrySerial"
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
                      locale="zh-cn">
            <template v-slot:item.shippingCostEntrySerial="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.shippingCostEntrySerial }}
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
    name: "ShippingCostQueryDisplayComponent",
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

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,

            queryTableHeaders: [
                { text: '运费单号', value: 'shippingCostEntrySerial', width: '120px' },
                { text: '单位简称', value: 'companyAbbreviatedName', width: '120px' },
                { text: '运费总金额', value: 'totalAmount', width: '85px' },
                { text: '开票金额', value: 'invoiceAmount', width: '850px' },
                { text: '开单人', value: 'drawer', width: '80px' },
                { text: '开单日期', value: 'checkoutDate', width: '100px' },
                { text: '开票号码', value: 'invoiceNumber', width: '100px' },
                { text: '运费标志', value: 'shippingCostType', width: '85px' },
                { text: '抵扣', value: 'isTaxDeduction', width: '65px' },
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
                this.companyName = val.fullName
                this.companyID = val.companyID
            }
            this.companySearchPanelOpen = false
        },
        clearSearchFields() {
            this.companyName = ''
            this.companyID = -1
        },
        query() {
            this.$getRequest(this.$api.shippingCostEntryInDateRange, {
                isInbound: this.isInbound,
                startDate: this.dateRange[0],
                endDate: this.dateRange[1],
                companyID: this.companyID,
                forModify: this.isModify,
            }).then((data) => {
                this.queryTableData = data
            }).catch(() => {})
        },
        queryModificationRecord() {
            if (this.queryTableCurrentRow.length === 0) return
            this.$getRequest(this.$api.modificationRecordsBySerial
                + encodeURI(this.queryTableCurrentRow[0].shippingCostEntrySerial)
            ).then((data) => {
                this.modificationRecords = data
            }).catch(() => {})
        },
        tableClick(val) {
            this.modificationRecords = []
            if (this.queryTableCurrentRow.includes(val)) {
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
