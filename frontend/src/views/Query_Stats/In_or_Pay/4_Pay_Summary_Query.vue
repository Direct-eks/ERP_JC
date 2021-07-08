<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>应付款汇总查询</p>-->
    <v-card>
        <v-card-title class="d-flex">
            应付款汇总查询
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <PaymentQuery mode="in"></PaymentQuery>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Pay_summary",
    components: {
        PaymentQuery: () => import('~/components/QueryComponents/PaymentQueryComponent')
    },
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
