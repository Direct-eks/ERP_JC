<template>
    <div>
        <v-row class="mb-2" dense>
            <v-col cols="auto">
                <DateRangePicker @chooseDate="chooseDateAction">
                </DateRangePicker>
            </v-col>
            <v-col cols="auto">
                <v-btn color="primary" @click="query">查询</v-btn>
            </v-col>
            <v-col cols="auto">
                <v-btn color="accent" @click="exportEntry">查看/修改</v-btn>
            </v-col>
            <v-spacer></v-spacer>
        </v-row>

        <v-data-table v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="checkoutEntrySerial"
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
        </v-data-table>
    </div>
</template>

<script>
import DateRangePicker from "~/components/DateRangePicker";

export default {
    name: "AcceptanceBillQueryComponent",
    components: {
        DateRangePicker
    },
    data() {
        return {
            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                new Date().format("yyyy-MM-dd").substr(0,10)
            ],

            queryTableHeaders: [
                { text: '单据号', value: 'acceptanceEntrySerial', width: '120px' },
                { text: this.isInbound ? '付款单位' : '收款单位', value: 'companyAbbreviatedName', width: '140px' },
                { text: '总金额', value: 'amount', width: '110px' },
                { text: '承兑票号', value: 'number', width: '140px' },
                { text: '收到日期', value: 'entryDate', width: '110px' },
                { text: '出票日期', value: 'issueDate', width: '110px' },
                { text: '到期日期', value: 'expirationDate', width: '110px' },
                { text: '类型', value: 'type', width: '60px' },
                { text: '开单人', value: 'drawer', width: '75px' },
                { text: '标志', value: 'classification', width: '60px' },
            ],
            queryTableData: [],
            queryTableCurrentRow: [],

        }
    },
    methods: {
        query() {

        },
        chooseDateAction(val) {
            this.dateRange = val
        },
        exportEntry() {
            if (this.queryTableCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '未选择单据', color: 'warning'
                })
                return
            }
            this.$emit('entryClick', this.queryTableCurrentRow[0])
        },
        tableClick(row) {
            if (this.queryTableCurrentRow.includes(row)) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [row]
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [row.item]
            }
        },
    }
}
</script>

<style scoped>

</style>
