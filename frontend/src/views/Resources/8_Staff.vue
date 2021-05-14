<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>系统及人员信息</p>-->
    <v-card>
        <v-card-title class="d-flex">
            系统及人员信息
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/resources">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
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
    name: "Staff",
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
