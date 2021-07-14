<template>
<!--  <p>库存管理</p>-->
<!--  <p>明细统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            明细统计
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <QueryConditions :queries.sync="queries">
            </QueryConditions>
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table :headers="headers"></v-data-table>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Product_Detail",
    components: {
        QueryConditions: () => import('~/components/StockManagementComponents/QueryConditions')
    },
    data() {
        return {
            mdiArrowLeft,

            queries: {
                dateRange: [
                    new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                    new Date().format("yyyy-MM-dd").substr(0,10)
                ],
                treeSelection: {label: '', categoryID: -1, children: []},
                factoryBrand: '',
                warehouseID: -1,
            },

            headers: [
                { text: '商品分类', value: '', width: '100px', sortable: false },
                { text: '代号', value: '', width: '180px', sortable: false },
                { text: '厂牌', value: '', width: '65px', sortable: false },
                { text: '单位', value: '', width: '60px', sortable: false },
                { text: '入库数量', value: '', width: '100px', sortable: true },
                { text: '无税金额', value: '', width: '110px', sortable: true },
                { text: '出库数量', value: '', width: '100px', sortable: true },
                { text: '无税金额', value: '', width: '110px', sortable: true },
                { text: '库存数量', value: '', width: '100px', sortable: true },
                { text: '库存无税价', value: '', width: '120px', sortable: true },
                { text: '无税金额', value: '', width: '110px', sortable: true },
                { text: '仓库', value: '', width: '100px', sortable: false },
            ]
        }
    },
    methods: {
        search() {

        },
    }
}
</script>

<style scoped>

</style>
