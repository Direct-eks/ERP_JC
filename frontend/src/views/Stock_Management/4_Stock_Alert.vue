<template>
<!--  <p>库存管理</p>-->
<!--  <p>库存报警</p>-->
    <v-card>
        <v-card-title class="d-flex">
            库存报警
            <v-spacer></v-spacer>
            <v-btn color="primary"
                   @click="search">
                查询
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="exportExcel">
                导出Excel
            </v-btn>
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
                <template v-slot:item.stockQuantity="{ item }">
                    <v-chip :color="chipColor(item)">
                        {{ item.stockQuantity }}
                    </v-chip>
                </template>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Stock_Alert",
    data() {
        return {
            mdiArrowLeft,

            tableHeaders: [
                { text: '分类', value: 'categoryCode', width: '120px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '单位', value: 'unitName', width: '65px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '100px' },
                { text: '库存数量', value: 'stockQuantity', width: '100px' },
                { text: '库存下限', value: 'lowerLimit', width: '90px' },
                { text: '库存上限', value: 'upperLimit', width: '90px' },
                { text: '差异数量', value: 'diff', width: '90px' },
            ],
            tableData: []
        }
    },
    methods: {
        chipColor(item) {
            let color = null
            if (item.lowerLimit !== -1 && item.stockQuantity < item.lowerLimit) {
                color = 'red'
            }
            if (item.upperLimit !== -1 && item.stockQuantity > item.upperLimit) {
                color = 'green'
            }
            return color
        },
        search() {
            this.$getRequest(this.$api.stockAlert).then(data => {
                this.tableData = data
            }).catch(() => {})
        },
        exportExcel() {

        }
    }
}
</script>

<style scoped>

</style>
