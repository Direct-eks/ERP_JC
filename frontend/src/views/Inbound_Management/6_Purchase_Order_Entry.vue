<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>采购订单录入</p>-->
    <v-card outlined>
        <v-card-title>采购订单录入</v-card-title>

        <InboundEntryCreateComponent editMode="purchaseOrder">
        </InboundEntryCreateComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "Purchase_Order_Entry",
    components: {
        InboundEntryCreateComponent: () => import('~/components/InboundEntryComponents/EntryCreateComponent'),
    }
}
</script>

<style scoped>

</style>
