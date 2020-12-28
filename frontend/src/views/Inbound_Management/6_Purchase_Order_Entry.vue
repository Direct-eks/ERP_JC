<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>采购订单录入</p>-->
    <v-card outlined>
        <v-card-title>采购订单录入</v-card-title>

        <InboundEntryAndPurchaseOrderComponent editMode="purchaseOrder">
        </InboundEntryAndPurchaseOrderComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
    import SnackMessage from "~/components/SnackMessage";

    export default {
        name: "Purchase_Order_Entry",
        components: {
            InboundEntryAndPurchaseOrderComponent: () => import('../../components/InboundEntryAndPurchaseOrderComponent'),
            SnackMessage
        }
    }
</script>

<style scoped>

</style>
