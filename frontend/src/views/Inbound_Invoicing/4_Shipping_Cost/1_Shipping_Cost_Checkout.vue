<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付运费结账</p>-->
    <v-card outlined>
        <v-card-title>
            付运费结账
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_invoicing">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-subtitle>自付(入库/出库)，代垫(入库) 运费结账</v-card-subtitle>

        <ShippingCostComponent mode="create"
                               :isInbound="true">
        </ShippingCostComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "In_Cost_Bill",
    components: {
        ShippingCostComponent: () => import(
            /* webpackChunkName: "ShippingCostComponent" */
            "~/components/InvoiceComponents/ShippingCostComponent"
        ),
    },
    data() {
        return {
            mdiArrowLeft,
        }
    }
}
</script>

<style scoped>

</style>
