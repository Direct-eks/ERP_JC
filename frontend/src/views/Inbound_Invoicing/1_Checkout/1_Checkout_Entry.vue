<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单录入</p>-->
    <v-card outlined>
        <v-card-title>
            入库结账单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_invoicing">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <InboundCheckoutComponent mode="create"
                                  :isInbound="true">
        </InboundCheckoutComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "In_Checkout_Entry",
    components: {
        InboundCheckoutComponent: () => import(
            /* webpackChunkName: "CheckoutComponent" */
            '~/components/InvoiceComponents/CheckoutComponent'
        ),
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
