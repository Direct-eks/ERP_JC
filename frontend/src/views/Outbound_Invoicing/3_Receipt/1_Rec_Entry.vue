<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>收款单录入</p>-->
    <v-card outlined>
        <v-card-title>
            收款单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_invoicing">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <PaymentComponent mode="create"
                          :isInbound="false">
        </PaymentComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Rec_Entry",
    components: {
        PaymentComponent: () => import('~/components/InvoiceComponents/PaymentComponent'),
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
