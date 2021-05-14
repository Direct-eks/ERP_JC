<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>仓库/计量单位</p>-->
    <v-card>
        <v-card-title class="d-flex">
            仓库/计量单位
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
    name: "MeasurementUnits",
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
