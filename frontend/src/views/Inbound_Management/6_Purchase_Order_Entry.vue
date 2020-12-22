<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>采购订单录入</p>-->
    <v-card outlined>
        <v-card-title>采购订单录入</v-card-title>

        <InboundEntryDisplayComponent editMode="purchase_entry">
        </InboundEntryDisplayComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
    import SnackMessage from "~/components/SnackMessage";

    export default {
        name: "Purchase_Order_Entry",
        components: {
            InboundEntryDisplayComponent: () => import('../../components/InboundEntryDisplayComponent'),
            SnackMessage
        }
    }
</script>

<style scoped>

</style>
