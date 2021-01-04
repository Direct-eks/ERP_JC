<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单录入</p>-->
    <v-card outlined>
        <v-card-title>入库结账单录入</v-card-title>

        <InboundCheckoutComponent mode="create">
        </InboundCheckoutComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "In_Checkout_Entry",
    components: {
        InboundCheckoutComponent: () => import("../../../components/InboundInvoiceComponents/InboundCheckoutComponent"),
        SnackMessage
    }
}
</script>

<style scoped>

</style>
