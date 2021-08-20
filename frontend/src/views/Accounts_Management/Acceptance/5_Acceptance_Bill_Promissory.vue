<template>
    <!--    <p>账目管理</p>-->
    <!--    <p>承兑汇票承付</p>-->
    <v-card>
        <v-card-title class="d-flex">
            承兑汇票承付
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/accounts_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "5_Acceptance_Bill_Promissory",
    data() {
        return {
            mdiArrowLeft
        }
    }
}
</script>

<style scoped>

</style>
