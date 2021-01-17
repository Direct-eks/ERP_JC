<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>出库未结账明细</p>-->
    <NotCheckoutQueryComponent :isInbound="false">
    </NotCheckoutQueryComponent>
</template>

<script>
export default {
    name: "Incomplete_Rec_Detail",
    components: {
        NotCheckoutQueryComponent: () => import('~/components/InvoiceComponents/NotCheckoutQueryComponent'),
    },
}
</script>

<style scoped>

</style>
