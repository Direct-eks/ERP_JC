<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>应收款查询</p>-->
    <v-card>
        <v-toolbar flat>
            <v-toolbar-title>应收款查询</v-toolbar-title>
            <v-spacer></v-spacer>
            <QueryConditions :queries.sync="queries"
                             @clear="clear">
            </QueryConditions>
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

                <v-tab-item key="summary" :eager="true">
                    <PaymentSummary mode="customer"
                                    @changeCompany="changeCompany"
                                    @changeCompanyAndSwitch="changeCompanyAndSwitch">
                    </PaymentSummary>
                </v-tab-item>

                <v-tab-item key="detail" :eager="true">
                    <PaymentDetail mode="customer" :companyID="queries.companyID"/>
                </v-tab-item>

                <v-tab-item key="ledger">
                    <PaymentLedger mode="customer"></PaymentLedger>
                </v-tab-item>

            </v-tabs-items>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Rec_Summary_Stats",
    components: {
        QueryConditions: () => import('~/components/QueryComponents/QueryConditions'),
        PaymentSummary: () => import('~/components/QueryComponents/PaymentSummaryComponent'),
        PaymentDetail: () => import('~/components/QueryComponents/PaymentDetailComponent'),
        PaymentLedger: () => import('~/components/QueryComponents/PaymentLedgerComponent')
    },
    data() {
        return {
            mdiArrowLeft,

            tab: null,

            queries: {
                companyID: -1,
                companyName: '',
            },
        }
    },
    methods: {
        clear() {
            this.queries.companyID = -1
            this.queries.companyName = ''
        },
        changeCompany(item) {
            if (item !== null) {
                this.queries.companyID = item.companyID
                this.queries.companyName = item.companyName
            }
            else {
                this.clear()
            }
        },
        changeCompanyAndSwitch(item) {
            this.queries.companyID = item.companyID
            this.queries.companyName = item.companyName
            this.tab = 1
        }
    }
}
</script>

<style scoped>

</style>
