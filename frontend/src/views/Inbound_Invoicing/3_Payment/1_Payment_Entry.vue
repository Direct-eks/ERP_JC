<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付款单录入</p>-->
    <v-card outlined>
        <v-card-title>
            付款单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_invoicing">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <PaymentComponent mode="create"
                          :isInbound="true">
        </PaymentComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: 'Pay_Entry',
    components: {
        PaymentComponent: () => import(
            /* webpackChunkName: "InboundPaymentComponent" */
            '~/components/InvoiceComponents/PaymentComponent'
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
