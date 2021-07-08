<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>出库结账单汇总统计</p>-->
    <v-card>
        <v-card-title class="d-flex">
            出库结账单汇总统计
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
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="companyName"
                                  label="单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="companySearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent" v-on="on">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch @fullSearchChoose="companySearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-btn color="primary" @click="search">查询一</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="primary" @click="search2">查询二</v-btn>
                </v-col>
            </v-row>
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
                <v-col cols="auto">
                    <v-select v-model="departmentID"
                              :items="departmentOptions"
                              item-value="departmentID"
                              item-text="name"
                              label="部门"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-btn color="warning" @click="clear">清空</v-btn>
                </v-col>
            </v-row>
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table></v-data-table>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Out_Check_Summary_Stats",
    components: {
        CompanySearch: () => import("~/components/CompanySearch"),
        DateRangePicker: () => import("~/components/DateRangePicker"),
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.$getRequest(this.$api.warehouseOptions).then((data) => {
            this.warehouseOptions = data
        }).catch(() => {})

        this.$getRequest(this.$api.departmentOptions).then((data) => {
            this.departmentOptions = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                new Date().format("yyyy-MM-dd").substr(0,10)
            ],

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,

            treeSelection: {label: '', categoryID: -1, children: []},
            modelPanel: false,

            factoryBrand: '',
            warehouseID: -1,
            warehouseOptions: [],
            departmentID: -1,
            departmentOptions: [],
        }
    },
    methods: {
        clear() {
            this.companyID = -1
            this.companyName = ''
            this.category = '销售'
            this.treeSelection = {label: '', categoryID: -1, children: []}
            this.factoryBrand = ''
            this.warehouseID = -1
            this.departmentID = -1
        },
        chooseDateAction(val) {
            this.dateRange = val
        },
        companySearchChooseAction(val) {
            if (val) {
                this.companyName = val.abbreviatedName
                this.companyID = val.companyID
            }
            this.companySearchPanelOpen = false
        },
        treeSelectionAction(val) {
            this.treeSelection = val
        },
        search() {

        },
        search2() {

        }
    }
}
</script>

<style scoped>

</style>
