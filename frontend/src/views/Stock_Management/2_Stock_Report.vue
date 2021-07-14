<template>
<!--  <p>库存管理</p>-->
<!--  <p>库存报表</p>-->
    <v-card>
        <v-card-title class="d-flex">
            库存报表
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="accent" @click="searchByCategory">
                分类统计
            </v-btn>
            <v-btn class="mr-3" color="primary" @click="exportExcel">
                导出Excel
            </v-btn>
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
                <v-data-table :headers="headers"
                              :items="tableData"
                              item-key="warehouseStockID"
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
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Stock_Report",
    components: {
        QueryConditions: () => import('~/components/StockManagementComponents/QueryConditions')
    },
    data() {
        return {
            mdiArrowLeft,
            loading: false,

            queries: {
                treeSelection: {label: '', categoryID: -1, children: []},
                code: '',
                warehouseID: -1,
                factoryBrand: '',
            },

            headers: [
                { text: '所在分类', value: 'categoryCode', width: '100px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '库存数量', value: 'stockQuantity', width: '90px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '100px' },
                { text: '库房名称', value: 'warehouseName', width: '100px' },
            ],
            tableData: [],
        }
    },
    methods: {
        clear() {
            this.treeSelection = {label: '', categoryID: -1, children: []}
            this.code = ''
            this.factoryBrand = ''
            this.warehouseID = -1
        },
        treeSelectionAction(val) {
            this.treeSelection = val
        },
        search() {
            this.$getRequest(this.$api.warehouseStockReport, {
                categoryID: this.treeSelection.categoryID,
                warehouseID: this.warehouseID,
                factoryBrand: this.factoryBrand,
                code: this.code
            }).then(data => {
                this.tableData = data
            }).catch(() => {})
        },
        searchByCategory() {

        },
        exportExcel() {
            this.$store.commit('setOverlay', true)
            this.$getFileRequest(this.$api.exportStockReport).then(data => {
                this.$store.commit('setOverlay', false)
                let href = window.URL.createObjectURL(new Blob([data]));
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = href;
                link.setAttribute('download',  '库存报表.xlsx')
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(href)
            }).catch(()=>{})
        }
    }
}
</script>

<style scoped>

</style>
