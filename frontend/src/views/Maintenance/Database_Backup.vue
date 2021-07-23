<template>
    <!--  <p>系统维护</p>-->
    <!--  <p>数据库备份</p>-->
    <v-card>
        <v-card-title class="d-flex">
            数据库备份
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/maintenance">
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
    name: "Database_Backup",
    data() {
        return {
            mdiArrowLeft,
        }
    }
}
</script>

<style scoped>

</style>
