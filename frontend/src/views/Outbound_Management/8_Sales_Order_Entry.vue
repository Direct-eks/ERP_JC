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
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Sales_Order_Entry",
    components: {
        OutboundEntryCreateComponent: () => import('../../components/OutboundEntryComponents/EntryCreateComponent'),
        SnackMessage
    }
}
</script>

<style scoped>

</style>
