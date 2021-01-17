<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>结账未开票查询</p>-->
    <NotInvoiceQueryComponent :isInbound="true">
    </NotInvoiceQueryComponent>
</template>

<script>
export default {
    name: "Incomplete_Pay_Ledger",
    components: {
        NotInvoiceQueryComponent: () => import(
            /* webpackChunkName: "NotInvoiceQueryComponent" */
            '~/components/InvoiceComponents/NotInvoiceQueryComponent'
        )
    }
}
</script>

<style scoped>

</style>
