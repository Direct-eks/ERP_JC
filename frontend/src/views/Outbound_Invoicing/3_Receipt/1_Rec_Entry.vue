<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>收款单录入</p>-->
    <v-card outlined>
        <v-card-title>收款单录入</v-card-title>

        <PaymentComponent mode="create"
                          :isInbound="false">
        </PaymentComponent>

    </v-card>
</template>

<script>
export default {
    name: "Rec_Entry",
    components: {
        PaymentComponent: () => import('~/components/InvoiceComponents/PaymentComponent'),
    }
}
</script>

<style scoped>

</style>
