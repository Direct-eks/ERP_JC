<template>
    <v-card>

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
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
