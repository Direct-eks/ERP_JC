<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付运费结账</p>-->
    <v-card outlined>
        <v-card-title>付运费结账</v-card-title>

        <ShippingCostComponent mode="create"
                               :isInbound="true">
        </ShippingCostComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "In_Cost_Bill",
    components: {
        ShippingCostComponent: () => import(
            /* webpackChunkName: "ShippingCostComponent" */
            "~/components/InvoiceComponents/ShippingCostComponent"
        ),
    },
}
</script>

<style scoped>

</style>
