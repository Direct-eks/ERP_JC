<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>报价单录入</p>-->
    <v-card outlined>
        <v-card-title>报价单录入</v-card-title>

        <OutboundEntryCreateComponent editMode="quotaEntry">
        </OutboundEntryCreateComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Quote_Entry",
    components: {
        OutboundEntryCreateComponent: () => import('../../components/OutboundEntryComponents/EntryCreateComponent'),
        SnackMessage
    }
}
</script>

<style scoped>

</style>
