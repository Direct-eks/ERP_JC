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
            <v-row>
                <v-col cols="auto">
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
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
                    <v-text-field v-model="factoryBrand"
                                  label="厂牌代号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  class="ml-2"
                                  style="max-width: 100px">
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
            </v-row>
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
        DateRangePicker: () => import("~/components/DateRangePicker"),
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

            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                new Date().format("yyyy-MM-dd").substr(0,10)
            ],

            treeSelection: {label: '', categoryID: -1, children: []},
            modelPanel: false,

            factoryBrand: '',
            warehouseID: -1,
            warehouseOptions: [],

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
        clear() {
            this.treeSelection = {label: '', categoryID: -1, children: []}
            this.factoryBrand = ''
            this.warehouseID = -1
        },
        chooseDateAction(val) {
            this.dateRange = val
        },
        treeSelectionAction(val) {
            this.treeSelection = val
        },
        search() {

        },
    }
}
</script>

<style scoped>

</style>
