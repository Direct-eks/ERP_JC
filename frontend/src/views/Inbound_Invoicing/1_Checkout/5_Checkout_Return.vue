<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账后退货</p>-->
    <v-card>
        <v-card-title class="d-flex">
            入库结账后退货
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_invoicing">
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
    name: "In_Check_Inv_Return",
    components: {

    },
    data() {
        return {
            mdiArrowLeft
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
