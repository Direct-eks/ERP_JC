<template>
    <!--  <p>生产管理</p>-->
    <!--  <p>产成品入库单查询</p>-->
    <v-card>
        <v-card-title class="d-flex">
            产成品入库单查询
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/production_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "WE_Query",
    data() {
        return {
            mdiArrowLeft
        }
    }
}
</script>

<style scoped>

</style>
