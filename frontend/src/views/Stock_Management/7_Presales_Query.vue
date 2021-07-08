<template>
<!--  <p>库存管理</p>-->
<!--  <p>预销售查询</p>-->
    <v-card>
        <v-card-title class="d-flex">
            预销售查询
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table :headers="tableHeaders"
                          :items="tableData"
                          item-key=""
                          height="75vh"
                          calculate-widths
                          disable-sort
                          fixed-header
                          hide-default-footer
                          locale="zh-cn"
                          dense>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Presales_Query",
    data() {
        return {
            mdiArrowLeft,

            tableHeaders: [
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '90px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '无税金额', value: 'totalWithoutTax', width: '100px' },
                { text: '税率', value: 'taxRate', width: '65px' },
                { text: '税额', value: 'totalTax', width: '90px' },
                { text: '备注', value: 'remark', width: '180px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
            ],
            tableData: []
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
