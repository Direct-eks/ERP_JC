<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>出库结账单开票录入</p>-->
    <v-card outlined>
        <v-card-title>出库结账单开票录入</v-card-title>

        <InvoiceComponent mode="invoiceEntry"
                          :isInbound="false">
        </InvoiceComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "Out_Check_Inv_Entry",
    components: {
        InvoiceComponent: () => import('~/components/InvoiceComponents/InvoiceComponent'),
    },
}
</script>

<style scoped>

</style>
