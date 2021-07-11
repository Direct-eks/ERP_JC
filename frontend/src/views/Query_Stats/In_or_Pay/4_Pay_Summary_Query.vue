<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>应付款汇总查询</p>-->
    <v-card>
        <v-toolbar flat>
            <v-toolbar-title>应付款查询</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>

            <template v-slot:extension>
                <v-tabs v-model="tab">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="summary">汇总查询</v-tab>
                    <v-tab key="detail">明细</v-tab>
                    <v-tab key="ledger">总账</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>
        <v-card-text>
            <v-tabs-items v-model="tab">

                <v-tab-item key="summary">
                    <PaymentQuery mode="in"></PaymentQuery>
                </v-tab-item>

                <v-tab-item>
                    <PaymentDetail mode="in"></PaymentDetail>
                </v-tab-item>

                <v-tab-item>
                    <PaymentLedger mode="in"></PaymentLedger>
                </v-tab-item>

            </v-tabs-items>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Pay_summary",
    components: {
        PaymentQuery: () => import('~/components/QueryComponents/PaymentQueryComponent'),
        PaymentDetail: () => import('~/components/QueryComponents/PaymentDetailComponent'),
        PaymentLedger: () => import('~/components/QueryComponents/PaymentLedgerComponent')
    },
    data() {
        return {
            mdiArrowLeft,

            tab: null
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
