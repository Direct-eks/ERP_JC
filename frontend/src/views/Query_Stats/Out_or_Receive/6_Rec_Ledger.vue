<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>应收款总账</p>-->
    <v-card>
        <v-card-title class="d-flex">
            应收款总账
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <PaymentLedger mode="out"></PaymentLedger>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Rec_Ledger",
    components: {
        PaymentLedger: () => import('~/components/QueryComponents/PaymentLedgerComponent')
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
