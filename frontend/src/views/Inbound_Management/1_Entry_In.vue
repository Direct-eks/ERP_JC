<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>入库单录入</p>-->
    <v-card outlined>
        <v-card-title>入库单录入</v-card-title>

        <InboundEntryCreateComponent editMode="inboundEntry">
        </InboundEntryCreateComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>

export default {
    name: "Entry_In",
    components: {
        InboundEntryCreateComponent: () => import(
            /* webpackChunkName: "InboundEntryCreateComponent" */
            '../../components/InboundEntryComponents/EntryCreateComponent'
        ),
    }
}
</script>

<style scoped>

</style>
