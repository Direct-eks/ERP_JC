<template>
    <v-card>
        <v-data-table v-model="tableCurrentRow"
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="companyID"
                      height="65vh"
                      calculate-widths
                      disable-sort
                      disable-pagination
                      hide-default-footer
                      fixed-header
                      locale="zh-cn"
                      dense>
        </v-data-table>
    </v-card>
</template>

<script>
export default {
    name: "PaymentLedgerComponent",
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        if (this.mode === 'supplier')
            this.customerMode = false
        else if (this.mode === 'customer')
            this.customerMode = true

        this.$getRequest(this.customerMode ? this.$api.receivableLedger :
            this.$api.payableLedger).then(data => {
            this.tableData = data
        })
    },
    data() {
        return {
            customerMode: true,

            tableHeaders: [
                { text: '单位简称', value: 'companyAbbreviatedName', width: '110px' },
                { text: '单位全称', value: 'companyFullName', width: '110px' },
                { text: '借贷', value: 'debitOrCredit', width: '110px' },
                { text: '结账金额', value: 'checkoutAmount', width: '110px' },
                { text: '开票金额', value: 'invoicedAmount', width: '110px' },
            ],
            tableData: [],
            tableCurrentRow: [],
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
