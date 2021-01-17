<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>出库结账单开票录入</p>-->
    <v-card outlined>
        <v-card-title>
            出库结账单开票录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_invoicing">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <InvoiceComponent mode="invoiceEntry"
                          :isInbound="false">
        </InvoiceComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Out_Check_Inv_Entry",
    components: {
        InvoiceComponent: () => import('~/components/InvoiceComponents/InvoiceComponent'),
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
