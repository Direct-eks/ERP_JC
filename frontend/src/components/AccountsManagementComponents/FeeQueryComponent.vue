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
                      height="40vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      :footer-props="{'items-per-page-options': [20,50,-1]}"
                      locale="zh-cn"
                      dense>
            <template v-slot:item.feeEntryID="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.feeEntryID }}
                </v-chip>
            </template>
        </v-data-table>
    </div>
</template>

<script>
import DateRangePicker from "~/components/DateRangePicker";

export default {
    name: "FeeQueryComponent",
    components: {
        DateRangePicker
    },
    props: {
        prefix: {
            type: String,
            required: true
        }
    },
    watch: {
        prefix: {
            handler: function (val) {

            },
            immediate: true,
        }
    },
    data() {
        return {
            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                new Date().format("yyyy-MM-dd").substr(0,10)
            ],

            queryTableHeaders: [
                { text: '单据号', value: 'feeEntryID', width: '140px' },
                { text: '日期', value: 'entryDate', width: '110px' },
                { text: '部门', value: 'departmentName', width: '140px' },
                { text: '收款去向', value: 'destinationAccountName', width: '250px' },
                { text: '票号', value: 'number', width: '140px' },
                { text: '金额', value: 'amount', width: '110px' },
                { text: '经办人', value: 'drawer', width: '75px' },
                { text: '费用类别', value: 'feeCategoryName', width: '80px' },
                { text: '记账', value: 'isBookKeeping', width: '80px' },
                { text: '审核', value: 'isVerified', width: '80px' },
            ],
            queryTableData: [],
            queryTableCurrentRow: [],

        }
    },
    methods: {
        query() {
            this.$getRequest(this.$api.getFeeEntriesInDateRange, {
                startDate: this.dateRange[0],
                endDate: this.dateRange[1],
                prefix: this.prefix
            }).then(data => {
                this.queryTableData = data
            }).catch(() => {})
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
