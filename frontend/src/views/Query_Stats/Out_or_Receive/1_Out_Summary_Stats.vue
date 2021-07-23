<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>出库单汇总统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            出库单汇总统计
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row dense>
                <v-col cols="auto">
                    <v-select v-model="category"
                              :items="categoryOptions"
                              item-value="value"
                              item-text="label"
                              label="出库类别"
                              hide-details="auto"
                              outlined dense
                              style="width: 100px">
                    </v-select>
                </v-col>
                <v-spacer></v-spacer>
                <div class="my-2">
                    <strong class="red--text">此处单价和总价不完全精确</strong>
                </div>
                <v-spacer></v-spacer>
                <v-col v-if="category === '销售'" cols="auto">
                    <v-btn color="primary" @click="queryForParentCategory">商品大类</v-btn>
                </v-col>
                <v-col v-if="category === '销售'" cols="auto">
                    <v-btn color="primary" @click="queryForSubCategory">子分类</v-btn>
                </v-col>
                <v-col v-if="category === '销售'" cols="auto">
                    <v-btn color="primary" @click="queryForBrand">厂牌</v-btn>
                </v-col>
                <v-col v-if="category === '销售'" cols="auto">
                    <v-btn color="primary" @click="queryForCompany">往来单位</v-btn>
                </v-col>
                <v-col v-if="category === '销售'" cols="auto">
                    <v-btn color="primary" @click="queryForCompanyByMonth">往来单位按月</v-btn>
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
    name: "Out_Summary_Stats",
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

            category: '销售',
            categoryOptions: [
                {value: '销售', label: '销售'},
                {value: '销订', label: '销订'},
                {value: '入退', label: '入退'},
                {value: '领料', label: '领料'},
                {value: '拆出', label: '拆出'},
                {value: '调出', label: '调出'},
                {value: '盘亏', label: '盘亏'},
                {value: '报废', label: '报废'},
            ],

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

            headers2: [],
            tableData2: []
        }
    },
    methods: {
        search() {
            this.loading = true
            this.showTable1 = true
            let api = ''
            switch (this.category) {
            case '销售':
            case '入退':
                api = this.$api.purchaseSummary
                break
            case '销订':
                api = this.$api.salesSummary
                break
            case '领料':
            case '拆出':
            case '调出':
            case '盘亏':
            case '报废':
                api = this.$api.warehouseEntrySummary
                break
            }

            this.$getRequest(api, {
                type: this.category,
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
            this.$getRequest(this.$api.outboundSummaryByParams, {
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
            this.loading = true
            this.$getRequest(this.$api.outboundSummaryByParams, {
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
            this.loading = true
            this.$getRequest(this.$api.outboundSummaryByParams, {
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
            this.loading = true
            this.$getRequest(this.$api.outboundSummaryByParams, {
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                type: 'company'
            }).then(data => {
                this.loading = false
                this.tableData2 = data
            }).catch(() => {this.loading = false})
        },
        queryForCompanyByMonth() {
            this.showTable1 = false
            this.headers2 = [
                { text: '单位简称', value: 'abbreviatedName', width: '140px' },
                { text: '总价', value: 'totalPrice', width: '140px' },
                { text: '占比', value: 'percentage', width: '100px' },
                { text: '一月', value: 'jan', width: '90px' },
                { text: '二月', value: 'feb', width: '90px' },
                { text: '三月', value: 'mar', width: '90px' },
                { text: '四月', value: 'apr', width: '90px' },
                { text: '五月', value: 'may', width: '90px' },
                { text: '六月', value: 'jun', width: '90px' },
                { text: '七月', value: 'jul', width: '90px' },
                { text: '八月', value: 'aug', width: '90px' },
                { text: '九月', value: 'sep', width: '90px' },
                { text: '十月', value: 'oct', width: '90px' },
                { text: '十一月', value: 'nov', width: '90px' },
                { text: '十二月', value: 'dec', width: '90px' },
            ]
            this.loading = true
            this.$getRequest(this.$api.outboundSummaryByParams, {
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                type: 'companyByMonth'
            }).then(data => {
                this.loading = false
                this.tableData2 = data
            }).catch(() => {this.loading = false})
        }
    }
}
</script>

<style scoped>

</style>
