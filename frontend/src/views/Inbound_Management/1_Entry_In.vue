<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>入库单录入</p>-->
    <v-card outlined>
        <v-card-title>
            入库单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <InboundEntryCreateComponent editMode="inboundEntry">
        </InboundEntryCreateComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Entry_In",
    components: {
        InboundEntryCreateComponent: () => import(
            /* webpackChunkName: "InboundEntryCreateComponent" */
            '~/components/InboundEntryComponents/EntryCreateComponent'
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
