<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付运费结账</p>-->
    <v-card outlined>
        <v-card-title>付运费结账</v-card-title>
        <v-card-subtitle>入库自付/代垫，出库自付运费结账</v-card-subtitle>

        <ShippingCostComponent mode="create"
                               :isInbound="true">
        </ShippingCostComponent>

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
