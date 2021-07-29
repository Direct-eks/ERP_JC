<template>
    <div>
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
        <v-row class="mt-2" dense>
            <v-col cols="auto">
                <v-btn color="primary" @click="query">查询</v-btn>
            </v-col>
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <v-dialog v-model="deletePopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="warning" v-on="on">删除</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="deletePopup = false">取消</v-btn>
                            <v-btn color="success" @click="handleDelete">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-spacer></v-spacer>
        </v-row>
    </div>
</template>

<script>
export default {
    name: "AcceptanceBillQueryComponent",
    data() {
        return {
            deletePopup: false,

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
        tableClick(row) {
            if (this.queryTableCurrentRow.includes(row)) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [row]
                this.$emit('tableClick', row)
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [row.item]
                this.$emit('tableClick', row.item)
            }
        },
        handleDelete() {

        }
    }
}
</script>

<style scoped>

</style>
