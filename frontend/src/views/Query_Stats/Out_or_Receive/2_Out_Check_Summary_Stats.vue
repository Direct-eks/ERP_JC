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
                <div class="my-2">
                    <strong class="red--text">此处单价和总价不完全精确</strong>
                </div>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-btn color="primary" @click="queryForParentCategory">商品大类</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="primary" @click="queryForSubCategory">子分类</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="primary" @click="queryForBrand">厂牌</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="primary" @click="queryForCompany">往来单位</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="primary" @click="search">查询</v-btn>
                </v-col>
            </v-row>
            <QueryConditions :queries.sync="queries">
            </QueryConditions>
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table v-if="showTable1"
                              :headers="headers"
                              :items="tableData"
                              :loading="loading"
                              calculate-widths
                              disable-filtering
                              disable-pagination
                              hide-default-footer
                              height="60vh"
                              fixed-header
                              locale="zh-cn"
                              dense>
                </v-data-table>
                <v-data-table v-else
                              :headers="headers2"
                              :items="tableData2"
                              :loading="loading"
                              calculate-widths
                              disable-filtering
                              disable-pagination
                              hide-default-footer
                              height="60vh"
                              fixed-header
                              locale="zh-cn"
                              dense>
                </v-data-table>
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
            showTable1: true,

            queries: {
                companyID: -1,
                companyName: '',
                dateRange: [
                    new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                    new Date().format("yyyy-MM-dd").substr(0,10)
                ],
                treeSelection: {label: '', categoryID: -1, treeLevel: '', children: []},
                factoryBrand: '',
                departmentID: -1,
                warehouseID: -1,
            },

            headers: [
                { text: '单据序号', value: 'entryID', width: '140px' },
                { text: '客户', value: 'companyAbbreviatedName', width: '200px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '90px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px' },
                { text: '含税金额', value: 'totalPrice', width: '100px' },
            ],
            tableData: [],

            headers2: [],
            tableData2: []
        }
    },
    methods: {
        clear() {
            this.showTable1 = true
            this.tableData = []
            this.tableData2 = []
        },
        search() {
            this.loading = true
            this.showTable1 = true

            this.$getRequest(this.$api.checkoutSummary, {
                isInbound: false,
                companyID: this.queries.companyID,
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                categoryID: this.queries.treeSelection.categoryID,
                factoryBrand: this.queries.factoryBrand,
                warehouseID: this.queries.warehouseID,
                departmentID: this.queries.departmentID
            }).then(data => {
                this.loading = false
                this.tableData = data
            }).catch(() => {this.loading = false})
        },
        queryForParentCategory() {
            this.showTable1 = false
            this.headers2 = [
                { text: '分类名称', value: 'categoryCode', width: '140px' },
                { text: '总价', value: 'totalPrice', width: '140px' },
                { text: '占比', value: 'percentage', width: '140px' },
            ]
            this.loading = true
            this.$getRequest(this.$api.checkoutSummaryByParams, {
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                type: 'parentCategory'
            }).then(data => {
                this.loading = false
                this.tableData2 = data
            }).catch(() => {this.loading = false})
        },
        queryForSubCategory() {
            if (this.queries.treeSelection.categoryID === -1 ||
                this.queries.treeSelection.treeLevel.length !== 1) {
                this.$store.commit('setSnackbar', {
                    message: '请选择正确的商品大类', color: 'warning'
                })
                return
            }
            this.showTable1 = false
            this.headers2 = [
                { text: '分类名称', value: 'categoryCode', width: '140px' },
                { text: '分类', value: 'categoryName', width: '140px' },
                { text: '总价', value: 'totalPrice', width: '140px' },
                { text: '占比', value: 'percentage', width: '140px' },
            ]
            this.$getRequest(this.$api.checkoutSummaryByParams, {
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                type: 'subCategory',
                id: this.queries.treeSelection.categoryID
            }).then(data => {
                this.loading = false
                this.tableData2 = data
            }).catch(() => {this.loading = false})
        },
        queryForBrand() {
            this.showTable1 = false
            this.headers2 = [
                { text: '厂牌', value: 'factoryBrand', width: '80px' },
                { text: '总价', value: 'totalPrice', width: '140px' },
                { text: '占比', value: 'percentage', width: '140px' },
            ]
            this.$getRequest(this.$api.checkoutSummaryByParams, {
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                type: 'brand'
            }).then(data => {
                this.loading = false
                this.tableData2 = data
            }).catch(() => {this.loading = false})
        },
        queryForCompany() {
            this.showTable1 = false
            this.headers2 = [
                { text: '单位简称', value: 'abbreviatedName', width: '140px' },
                { text: '总价', value: 'totalPrice', width: '140px' },
                { text: '占比', value: 'percentage', width: '140px' },
            ]
            this.$getRequest(this.$api.checkoutSummaryByParams, {
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                type: 'company'
            }).then(data => {
                this.loading = false
                this.tableData2 = data
            }).catch(() => {this.loading = false})
        },
    }
}
</script>

<style scoped>

</style>
