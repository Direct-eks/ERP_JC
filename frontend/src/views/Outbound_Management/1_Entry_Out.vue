<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>出库单录入</p>-->
    <v-card outlined>
        <v-card-title>
            出库单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <OutboundEntryCreateComponent editMode="outboundEntry">
        </OutboundEntryCreateComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Entry_Out",
    components: {
        OutboundEntryCreateComponent: () => import(
            /* webpackChunkName: "OutboundEntryCreateComponent" */
            '~/components/OutboundEntryComponents/EntryCreateComponent'
        ),
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
