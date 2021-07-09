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
            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="treeSelection.label"
                                  label="商品分类"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="max-width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="modelPanel"
                              persistent
                              scrollable
                              no-click-animation
                              width="40vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent" v-on="on">选择分类</v-btn>
                        </template>
                        <v-card>
                            <v-card-text>
                                <ModelTree height="60vh" max-width=""
                                           :select-for-search="false"
                                           :select-for-level="true"
                                           :show-select="true"
                                           @treeSelectionObject="treeSelectionAction">
                                </ModelTree>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="accent" @click="modelPanel = false">确认</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="code"
                                  label="代号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="max-width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="warehouseID"
                              :items="warehouseOptions"
                              item-value="warehouseID"
                              item-text="name"
                              label="仓库"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="factoryBrand"
                                  label="厂牌代号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="max-width: 100px">
                    </v-text-field>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-btn color="accent" @click="search">查询</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="warning" @click="clear">清空</v-btn>
                </v-col>
            </v-row>
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
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.$getRequest(this.$api.warehouseOptions).then((data) => {
            this.warehouseOptions = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,
            loading: false,

            treeSelection: {label: '', categoryID: -1, children: []},
            modelPanel: false,

            code: '',
            warehouseID: -1,
            warehouseOptions: [],
            factoryBrand: '',

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
