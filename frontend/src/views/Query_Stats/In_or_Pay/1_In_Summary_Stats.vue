<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>入库单汇总统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            入库单汇总统计
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
                              label="入库类别"
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
                <v-col cols="auto">
                    <v-btn color="primary" @click="search" :loading="loading">查询</v-btn>
                </v-col>
            </v-row>
            <QueryConditions :queries.sync="queries"
                             @clear="clear">
            </QueryConditions>
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table :headers="headers"
                              :items="tableData"
                              :loading="loading"
                              calculate-widths
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
    name: "Inbound_Summary",
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

            category: '购入',
            categoryOptions: [
                {value: '购入', label: '购入'},
                {value: '采订', label: '采订'},
                {value: '出退', label: '出退'},
                {value: '产入', label: '产入'},
                {value: '折入', label: '折入'},
                {value: '调入', label: '调入'},
                {value: '盘盈', label: '盘盈'},
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
        }
    },
    methods: {
        clear() {
            this.tableData = []
        },
        search() {
            this.loading = true
            let api = ''
            switch (this.category) {
            case '购入':
            case '出退':
                api = this.$api.inboundSummary
                break
            case '采订':
                api = this.$api.purchaseSummary
                break
            case '产入':
            case '折入':
            case '调入':
            case '盘盈':
                api = this.$api.warehouseEntrySummary
                break
            }

            this.$getRequest(api, {
                type: this.category,
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                categoryID: this.queries.treeSelection.categoryID,
                factoryBrand: this.queries.factoryBrand,
                warehouseID: this.queries.warehouseID,
                departmentID: this.queries.departmentID
            }).then(data => {
                this.loading = false
                this.tableData = data
            }).catch(() => {})
        },
    }
}
</script>

<style scoped>

</style>
