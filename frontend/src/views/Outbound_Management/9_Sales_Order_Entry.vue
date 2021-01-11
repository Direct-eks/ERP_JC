<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>销售订单录入</p>-->
    <v-card outlined>
        <v-card-title>销售订单录入</v-card-title>

        <OutboundEntryCreateComponent editMode="salesOrder">
        </OutboundEntryCreateComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "Sales_Order_Entry",
    components: {
        OutboundEntryCreateComponent: () => import('../../components/OutboundEntryComponents/EntryCreateComponent'),
    }
}
</script>

<style scoped>

</style>
