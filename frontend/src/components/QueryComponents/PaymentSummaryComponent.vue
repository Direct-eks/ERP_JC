<template>
    <v-card>
        <v-data-table v-model="tableCurrentRow"
                      :headers="customerMode ? tableHeaders2 : tableHeaders1"
                      :items="tableData"
                      :loading="loading"
                      item-key="companyID"
                      height="65vh"
                      calculate-widths
                      disable-sort
                      disable-pagination
                      hide-default-footer
                      show-select
                      single-select
                      @click:row="tableClick"
                      @item-selected="tableClick2"
                      @dblclick:row="doubleClick"
                      checkbox-color="accent"
                      fixed-header
                      locale="zh-cn"
                      dense>
        </v-data-table>
    </v-card>
</template>

<script>
export default {
    name: "PaymentQueryComponent",
    props: {
        mode: {
            type: String,
            required: true
        },
    },
    beforeMount() {
        if (this.mode === 'supplier')
            this.customerMode = false
        else if (this.mode === 'customer')
            this.customerMode = true

        this.$getRequest(this.customerMode ? this.$api.receivableSummary :
            this.$api.payableSummary).then(data => {
            this.$querySuccessMessage(data)
            this.tableData = data
            this.loading = false
        }).catch(() => { this.loading = false })
    },
    data() {
        return {
            customerMode: true,
            loading: true,

            tableHeaders1: [
                { text: '单位简称', value: 'companyName', width: '140px' },
                { text: '应付款', value: 'payableAmount', width: '110px' },
                { text: '未结账金额', value: 'notCheckoutAmount', width: '110px' },
                { text: '小计', value: 'subtotal', width: '110px' },
            ],
            tableHeaders2: [
                { text: '单位简称', value: 'companyName', width: '140px' },
                { text: '应收款', value: 'receivableAmount', width: '110px' },
                { text: '未结账金额', value: 'notCheckoutAmount', width: '110px' },
                { text: '小计', value: 'subtotal', width: '110px' },
            ],
            tableData: [],
            tableCurrentRow: [],
        }
    },
    methods: {
        clearCompany() {
            this.$emit('changeCompany', null)
        },
        chooseCompany(item) {
            this.$emit('changeCompany', {
                companyID: item.companyID,
                companyName: item.companyName
            })
        },
        tableClick(row) {
            if (!this.tableCurrentRow.includes(row)) {
                this.tableCurrentRow = [row]
                this.chooseCompany(row)
            }
            else {
                this.tableCurrentRow = []
                this.clearCompany()
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.tableCurrentRow = []
                this.clearCompany()
            }
            else {
                this.tableCurrentRow = [row.item]
                this.chooseCompany(row.item)
            }
        },
        doubleClick(_, row) {
            this.$emit('changeCompanyAndSwitch', {
                companyID: row.item.companyID,
                companyName: row.item.companyName
            })
        }
    }
}
</script>

<style scoped>

</style>
