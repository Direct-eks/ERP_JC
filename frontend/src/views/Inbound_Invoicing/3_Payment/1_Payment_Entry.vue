<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付款单录入</p>-->
    <v-card outlined>
        <v-card-title>付款单录入</v-card-title>

        <PaymentComponent mode="create">
        </PaymentComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: 'Pay_Entry',
    components: {
        PaymentComponent: () => import(
            /* webpackChunkName: "InboundPaymentComponent" */
            '~/components/InboundInvoiceComponents/PaymentComponent'
        ),
    }
}
</script>

<style scoped>

</style>
