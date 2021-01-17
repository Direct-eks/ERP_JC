<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>出库未结账总账</p>-->
    <NotInvoiceQueryComponent :isInbound="false">
    </NotInvoiceQueryComponent>
</template>

<script>
export default {
    name: "Incomplete_Rec_Ledger",
    components: {
        NotInvoiceQueryComponent: () => import('~/components/InvoiceComponents/NotInvoiceQueryComponent')
    }
}
</script>

<style scoped>

</style>
