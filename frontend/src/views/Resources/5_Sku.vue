<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>商品型号明细</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品型号明细
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
    name: "Sku",
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
