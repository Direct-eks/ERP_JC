<template>
    <!--  <p>系统维护</p>-->
    <!--  <p>期初应收款录入</p>-->
    <v-card>
        <v-card-title class="d-flex">
            期初应收款录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/maintenance">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <entryComponent mode="recv">
            </entryComponent>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Initial_receivable",
    components: {
        entryComponent: () => import('~/components/MaintenanceComponents/InitialMoneyEntryComponent')
    },
    data() {
        return {
            mdiArrowLeft,
        }
    }
}
</script>

<style scoped>

</style>
