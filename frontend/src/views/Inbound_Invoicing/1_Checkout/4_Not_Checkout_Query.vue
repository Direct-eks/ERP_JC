<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库未结账查询</p>-->
    <NotCheckoutQueryComponent :isInbound="true">
    </NotCheckoutQueryComponent>
</template>

<script>
export default {
    name: "Not_Checkout_Query",
    components: {
        NotCheckoutQueryComponent: () => import(
            /* webpackChunkName: "NotCheckoutQueryComponent" */
            '~/components/InvoiceComponents/NotCheckoutQueryComponent'
        )
    },
}
</script>

<style scoped>

</style>
