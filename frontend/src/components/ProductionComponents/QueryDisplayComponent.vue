<template>
    <v-container>
        <v-form @keyup.enter="query">
            <v-row>
                <v-col cols="auto">
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto" class="d-flex">
                    <v-btn class="mr-2" color="primary" @click="query">查询{{prefix}}单</v-btn>
                    <v-btn color="accent" @click="queryModificationRecord">查询修改记录</v-btn>
                </v-col>
            </v-row>
        </v-form>

        <v-data-table v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="warehouseEntryID"
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
            <template v-slot:item.warehouseEntryID="{ item }">
                <v-chip :color="chipColor(item)">
                    {{ item.warehouseEntryID }}
                </v-chip>
            </template>
        </v-data-table>
        <div>
            <strong class="red--text">红色：</strong>修改
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
    name: "QueryDisplayComponent",
    components: {
        DateRangePicker
    },
    props: {
        displayMode: {
            type: String,
            required: true
        },
        type: {
            type: String,
            required: true
        },
        prefix: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        switch (this.displayMode) {
        case 'modify':
        case 'query':
            break
        }

        switch (this.type) {
        case 'materialApply':
        case 'productionEntry':
        case 'inventoryLoss':
        case 'inventoryProfit':
        case 'scrapEntry':
            break
        case 'assemblyEntry':
        case 'transferEntry':
            this.isDuelMode = true
            break
        }
    },
    data() {
        return {
            isDuelMode: false,

            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0, 10),
                new Date().format("yyyy-MM-dd").substr(0, 10)
            ],

            queryTableHeaders: [
                {text: '单号', value: 'warehouseEntryID', width: '140px'},
                {text: '类别', value: 'classification', width: '60px'},
                {text: '总金额', value: 'totalAmount', width: '85px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '仓库', value: 'warehouseName', width: '65px'},
                {text: '部门', value: 'departmentName', width: '80px'},
                {text: '开单人', value: 'drawer', width: '60px'},
                {text: '开单日期', value: 'entryDate', width: '110px'},
            ],
            queryTableData: [],
            originalData: [],
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
        query() {
            if (this.isDuelMode) {
                this.$getRequest(this.$api.duelEntriesInDateRange, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    type: this.prefix.substr(0, 1)
                }).then(data => {
                    const list = []
                    for (const item of data) {
                        list.push(item[0])
                        list.push(item[1])
                    }
                    this.queryTableData = list
                    this.originalData = data
                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.duelEntriesInDateRange, {
                    startDate: this.dateRange[0],
                    endDate: this.dateRange[1],
                    type: this.prefix
                }).then(data => {
                    this.queryTableData = data
                }).catch(() => {})
            }
        },
        chipColor(item) {
            return item.isModified === 1 ? 'red' : null
        },
        tableClick(val) {
            this.modificationRecords = []
            if (this.queryTableCurrentRow.indexOf(val) !== -1) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [val]
                if (this.isDuelMode) {
                    const index = Math.floor(this.queryTableData.indexOf(val) / 2)
                    this.$emit('tableClick', this.originalData[index])
                    return
                }
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
                if (this.isDuelMode) {
                    const index = Math.floor(this.queryTableData.indexOf(row.item) / 2)
                    this.$emit('tableClick', this.originalData[index])
                    return
                }
                this.$emit('tableClick', row.item)
            }
        },
        queryModificationRecord() {
            if (this.queryTableCurrentRow.length === 0) return
            this.$getRequest(this.$api.modificationRecordsBySerial +
                encodeURI(this.queryTableCurrentRow[0].warehouseEntryID)).then((data) => {
                this.modificationRecords = data
            }).catch(() => {})
        },
    }
}
</script>

<style scoped>

</style>
