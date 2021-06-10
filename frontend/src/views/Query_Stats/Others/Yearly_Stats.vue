<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>年度出入库统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            年度出入库统计
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
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
    name: "Yearly_Stats",
    data() {
        return {
            mdiArrowLeft,
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
