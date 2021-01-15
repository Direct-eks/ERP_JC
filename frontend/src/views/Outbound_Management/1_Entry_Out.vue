<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>出库单录入</p>-->
    <v-card outlined>
        <v-card-title>出库单录入</v-card-title>

        <OutboundEntryCreateComponent editMode="outboundEntry">
        </OutboundEntryCreateComponent>

    </v-card>
</template>

<script>
export default {
    name: "Entry_Out",
    components: {
        OutboundEntryCreateComponent: () => import(
            /* webpackChunkName: "OutboundEntryCreateComponent" */
            '~/components/OutboundEntryComponents/EntryCreateComponent'
        ),
    }
}
</script>

<style scoped>

</style>
