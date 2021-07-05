<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单开票录入</p>-->
    <v-card outlined>
        <v-card-title>
            入库结账单开票录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_invoicing">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <InboundInvoiceComponent mode="invoiceEntry"
                                 :isInbound="true">
        </InboundInvoiceComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: 'In_Check_Inv_Entry',
    components: {
        InboundInvoiceComponent: () => import(
            /* webpackChunkName: "InboundInvoiceComponent" */
            '~/components/InvoiceComponents/InvoiceComponent'
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
