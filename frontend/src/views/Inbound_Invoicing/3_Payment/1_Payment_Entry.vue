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
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Pay_Entry",
    components: {
        PaymentComponent: () => import('~/components/InboundInvoiceComponents/PaymentComponent'),
        SnackMessage
    }
}
</script>

<style scoped>

</style>
