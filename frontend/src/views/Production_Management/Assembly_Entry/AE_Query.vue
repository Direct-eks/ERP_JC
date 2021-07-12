<template>
    <!--  <p>生产管理</p>-->
    <!--  <p>拆组装单查询</p>-->
    <v-card>
        <v-card-title class="d-flex">
            拆组装单查询
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
    name: "AE_Query",
    data() {
        return {
            mdiArrowLeft
        }
    }
}
</script>

<style scoped>

</style>
