<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>出库单录入</p>-->
    <v-card outlined>
        <v-card-title>出库单录入</v-card-title>

        <OutboundEntryCreateComponent editMode="outboundEntry">
        </OutboundEntryCreateComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Entry_Out",
    components: {
        OutboundEntryCreateComponent: () => import('../../components/OutboundEntryComponents/EntryCreateComponent'),
        SnackMessage
    }
}
</script>

<style scoped>

</style>
