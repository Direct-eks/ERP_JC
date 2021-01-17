<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>收运费结账</p>-->
    <v-card outlined>
        <v-card-title>
            收运费结账
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_invoicing">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-subtitle>出库代垫运费结账</v-card-subtitle>

        <ShippingCostComponent mode="create"
                               :isInbound="false">
        </ShippingCostComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Out_Cost_Bill",
    components: {
        ShippingCostComponent: () => import("~/components/InvoiceComponents/ShippingCostComponent"),
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,
        }
    }
}
</script>

<style scoped>

</style>
