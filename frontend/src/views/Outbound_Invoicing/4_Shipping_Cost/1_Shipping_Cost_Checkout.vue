<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>付运费结账</p>-->
    <v-card outlined>
        <v-card-title>付运费结账</v-card-title>
        <v-card-subtitle>出库代垫运费结账</v-card-subtitle>

        <ShippingCostComponent mode="create"
                               :isInbound="false">
        </ShippingCostComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "Out_Cost_Bill",
    components: {
        ShippingCostComponent: () => import("~/components/InvoiceComponents/ShippingCostComponent"),
    },
}
</script>

<style scoped>

</style>
