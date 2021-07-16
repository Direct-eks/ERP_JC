<template>
    <!--  <p>生产管理</p>-->
    <!--  <p>领料单录入</p>-->
    <v-card>
        <v-card-title class="d-flex">
            领料单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/production_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <EntryComponent editMode="creation"
                            type="materialApply"
                            prefix="领料">
            </EntryComponent>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "MA_Entry",
    components: {
        EntryComponent: () => import(/* webpackChunkName: "WarehouseEntryComponent" */
            '~/components/ProductionComponents/WarehouseEntry')
    },
    data() {
        return {
            mdiArrowLeft
        }
    }
}
</script>

<style scoped>

</style>
