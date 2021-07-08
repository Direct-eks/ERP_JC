<template>
<!--  <p>库存管理</p>-->
<!--  <p>预销售查询</p>-->
    <v-card>
        <v-card-title class="d-flex">
            预销售查询
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
                { text: '出库单序号', value: 'code', width: '140px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '出库数量', value: 'quantity', width: '100px' },
                { text: '库存数量', value: 'stockQuantity', width: '100px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px' },
                { text: '购货单位', value: 'companyAbbreviatedName', width: '160px' },
            ],
            tableData: []
        }
    },
    methods: {
        search() {
            this.$getRequest(this.$api.presaleProducts).then(data => {
                this.tableData = data
            })
        },
        exportExcel() {
            this.$store.commit('setOverlay', true)
            this.$getFileRequest(this.$api.exportPresaleProducts).then(data => {
                this.$store.commit('setOverlay', false)
                let href = window.URL.createObjectURL(new Blob([data]));
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = href;
                link.setAttribute('download',  '预销售型号表.xlsx')
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
