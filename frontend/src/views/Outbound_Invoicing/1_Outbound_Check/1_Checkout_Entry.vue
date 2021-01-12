<template>
    <!--  <p>出库结账管理</p>-->
    <!--  <p>出库结账单录入</p>-->
    <v-card outlined>
        <v-card-title>出库结账单录入</v-card-title>

        <CheckoutComponent mode="create"
                           :isInbound="false">
        </CheckoutComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "Out_Check_Entry",
    components: {
        CheckoutComponent: () => import('~/components/InvoiceComponents/CheckoutComponent'),
    },
}
</script>

<style scoped>

</style>
