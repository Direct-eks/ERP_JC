<template>
    <!--  <p>出库结账管理</p>-->
    <!--  <p>出库结账单录入</p>-->
    <v-card outlined>
        <v-card-title>
            出库结账单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_invoicing">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <CheckoutComponent mode="create"
                           :isInbound="false">
        </CheckoutComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Out_Check_Entry",
    components: {
        CheckoutComponent: () => import('~/components/InvoiceComponents/CheckoutComponent'),
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
