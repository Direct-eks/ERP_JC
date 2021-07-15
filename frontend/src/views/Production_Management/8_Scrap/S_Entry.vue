<template>
    <!--  <p>生产管理</p>-->
    <!--  <p>报废单录入</p>-->
    <v-card>
        <v-card-title class="d-flex">
            报废单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/production_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <EntryComponent editMode="creation"
                            type="scrapEntry"
                            prefix="报废">
            </EntryComponent>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "S_Entry",
    components: {
        EntryComponent: () => import('~/components/ProductionComponents/ProductionEntry')
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
