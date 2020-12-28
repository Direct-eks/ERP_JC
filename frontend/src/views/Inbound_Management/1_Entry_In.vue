<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>入库单录入</p>-->
    <v-card outlined>
        <v-card-title>入库单录入</v-card-title>

        <InboundEntryAndPurchaseOrderComponent editMode="inboundEntry">
        </InboundEntryAndPurchaseOrderComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
    import SnackMessage from '~/components/SnackMessage'

    export default {
        name: "Entry_In",
        components: {
            InboundEntryAndPurchaseOrderComponent: () => import('../../components/InboundEntryAndPurchaseOrderComponent'),
            SnackMessage
        }
    }
</script>

<style scoped>

</style>
