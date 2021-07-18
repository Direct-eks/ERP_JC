<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>出库结账单汇总统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            出库结账单汇总统计
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row dense>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-btn color="primary" @click="search">查询一</v-btn>
                </v-col>
            </v-row>
            <QueryConditions :queries.sync="queries">
            </QueryConditions>
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table></v-data-table>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Out_Check_Summary_Stats",
    components: {
        QueryConditions: () => import('~/components/QueryComponents/QueryConditions')
    },
    data() {
        return {
            mdiArrowLeft,
            loading: false,

            queries: {
                companyID: -1,
                companyName: '',
                dateRange: [
                    new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                    new Date().format("yyyy-MM-dd").substr(0,10)
                ],
                treeSelection: {label: '', categoryID: -1, children: []},
                factoryBrand: '',
                departmentID: -1,
                warehouseID: -1,
            },

            headers: [
                { text: '单据序号', value: 'entryID', width: '140px' },
                { text: '购入单位', value: 'companyAbbreviatedName', width: '200px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '90px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px' },
                { text: '含税金额', value: 'totalPrice', width: '100px' },
            ],
            tableData: [],
        }
    },
    methods: {
        search() {
            this.loading = true

        },
    }
}
</script>

<style scoped>

</style>
