<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>销售价差统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            销售价差统计
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <QueryConditions :queries.sync="queries"
                             @clear="clear">
            </QueryConditions>
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table :headers="headers"
                              :items="tableData"
                              :loading="loading"
                              calculate-widths
                              height="60vh"
                              fixed-header
                              locale="zh-cn"
                              dense>
                </v-data-table>

                <div class="d-flex">
                    <div class="my-2">
                        <strong>销售总额：</strong>
                    </div>
                    <div class="my-2 mr-5">
                        <strong>{{ stat.totalPriceWithoutTax }}</strong>
                    </div>
                    <div class="my-2">
                        <strong>含税总额：</strong>
                    </div>
                    <div class="my-2 mr-5">
                        <strong>{{ stat.totalPriceWithTax }}</strong>
                    </div>
                    <div class="my-2">
                        <strong>毛利合计：</strong>
                    </div>
                    <div class="my-2 mr-5">
                        <strong>{{ stat.totalGrossProfit }}</strong>
                    </div>
                    <div class="my-2">
                        <strong>总毛利率：</strong>
                    </div>
                    <div class="my-2 mr-5">
                        <strong>{{ stat.totalGrossProfitRate }}</strong>
                    </div>
                    <div class="my-2">
                        <strong>服务费合计：</strong>
                    </div>
                    <div class="my-2 mr-5">
                        <strong>{{ stat.totalServiceFee }}</strong>
                    </div>
                    <div class="my-2">
                        <strong>扣服务费毛利率：</strong>
                    </div>
                    <div class="my-2 mr-5">
                        <strong>{{ stat.totalGrossProfitRateWithoutServiceFee }}</strong>
                    </div>
                </div>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Price_Diff_Stats",
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
            },

            stat: {
                totalPriceWithoutTax: '0',
                totalPriceWithTax: '0',
                totalGrossProfit: '0',
                totalGrossProfitRate: '0%',
                totalServiceFee: '0',
                totalGrossProfitRateWithoutServiceFee: '0%',
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
        }
    },
    computed: {

    },
    methods: {
        clear() {
            this.tableData = []
        },
        search() {
            this.$getRequest(this.$api.diffStat, {
                companyID: this.queries.companyID,
                startDate: this.queries.dateRange[0],
                endDate: this.queries.dateRange[1],
                categoryID: this.queries.treeSelection.categoryID,
                factoryBrand: this.queries.factoryBrand,
                departmentID: this.queries.departmentID
            }).then(data => {
                this.tableData = data
                this.totalCalculation()
            })
        },
        totalCalculation() {
            let totalPriceWithoutTax = 0
            let totalPriceWithTax = 0
            let totalGrossProfit = 0
            // let totalServiceFee = 0
            for (const item of this.tableData) {
                const price = item.unitPriceWithoutTax * item.quantity
                totalPriceWithoutTax += price
                totalPriceWithTax += item.unitPriceWithTax * item.quantity
                const cost = item.stockUnitPrice * item.quantity
                totalGrossProfit += price - cost
                // totalServiceFee += item.serviceFee
            }

            this.stat.totalPriceWithoutTax = totalPriceWithoutTax.toFixed(2)
            this.stat.totalPriceWithTax = totalPriceWithTax.toFixed(2)
            this.stat.totalGrossProfit = totalGrossProfit.toFixed(2)
            this.stat.totalGrossProfitRate = (totalGrossProfit / totalPriceWithoutTax).toFixed(2)
        }
    }
}
</script>

<style scoped>

</style>
