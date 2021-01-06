<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单开票录入</p>-->
    <v-card outlined>
        <v-card-title>入库结账单开票录入</v-card-title>

        <InboundInvoiceComponent mode="invoiceEntry">
        </InboundInvoiceComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "In_Check_Inv_Entry",
    components: {
        InboundInvoiceComponent: () => import("~/components/InboundInvoiceComponents/InvoiceComponent"),
        SnackMessage
    },

}
</script>

<style scoped>

</style>
