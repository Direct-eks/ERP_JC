<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单开票录入</p>-->
    <v-card outlined>
        <v-card-title>入库结账单开票录入</v-card-title>

        <InboundInvoiceComponent mode="invoiceEntry"
                                 :isInbound="true">
        </InboundInvoiceComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: 'In_Check_Inv_Entry',
    components: {
        InboundInvoiceComponent: () => import(
            /* webpackChunkName: "InboundInvoiceComponent" */
            '~/components/InvoiceComponents/InvoiceComponent'
        ),
    },

}
</script>

<style scoped>

</style>
