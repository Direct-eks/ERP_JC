<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>应付款明细</p>-->
    <v-card>
        <v-card-title class="d-flex">
            应付款明细
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <PaymentDetail mode="in"></PaymentDetail>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Pay_Detail",
    components: {
        PaymentDetail: () => import('~/components/QueryComponents/PaymentDetailComponent')
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
