<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>费用收入类别</p>-->
    <v-card>
        <v-card-title class="d-flex">
            费用收入类别
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
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
    name: "Fees",
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
