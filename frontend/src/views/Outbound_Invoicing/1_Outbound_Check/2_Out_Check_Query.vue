<template>
    <!--  <p>出库结账管理</p>-->
    <!--  <p>出库结账单查询</p>-->
    <div style="text-align: left">
        <el-row style="margin: 10px">
            <strong>出库结账单查询</strong>
        </el-row>

        <el-tabs type="border-card">
            <el-tab-pane label="浏览">
                <el-form>
                    <el-form-item>
                        <label style="margin-right: 5px">选择日期</label>
                        <el-date-picker style="width: 250px"
                                        v-model="value"
                                        type="daterange"
                                        readonly
                                        name="选择日期"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        format="yyyy-MM-dd"
                                        :default-time="['00:00:00', '23:59:59']">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="info" @click="fullSearch()" :loading="fullSearchLoading"
                                   :disabled="fullSearchPanelOpen === true">选择单位</el-button>
                        <el-input
                                style="width: 400px"
                                placeholder="wh"
                                v-model="value1">
                        </el-input>
                        <label style="margin-right: 5px; margin-left: 10px;">结账类型</label>
                        <el-select v-model="value" placeholder="请选择" style="width: 150px">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                        <el-button type="danger">清空</el-button>
                    </el-form-item>

                    <el-collapse-transition>
                        <company-search v-if="fullSearchPanelOpen" @searchCloseAction="fullSearchPanelCloseAction"
                                        @chooseAction="fullSearchChoose"></company-search>
                    </el-collapse-transition>

                </el-form>
                <el-row>
                    <el-button type="primary">查询</el-button>
                    <el-button type="primary">检查</el-button>
                </el-row>

                <el-table
                        :data="tableData"
                        stripe
                        highlight-current-row
                        style="width: 100%"
                        height="300">
                    <el-table-column fixed prop="date" label="入库单号" width="80"></el-table-column>
                    <el-table-column prop="name" label="单位简称" width="120"></el-table-column>
                    <el-table-column prop="province" label="仓库" width="120"></el-table-column>
                    <el-table-column prop="city" label="部门" width="120"></el-table-column>
                    <el-table-column prop="address" label="入库类别" width="120"></el-table-column>
                    <el-table-column prop="zip" label="预计单据类型" width="120"></el-table-column>
                    <el-table-column prop="zip" label="总金额" width="120"></el-table-column>
                    <el-table-column prop="zip" label="运输方式" width="120"></el-table-column>
                    <el-table-column prop="zip" label="运单号" width="120"></el-table-column>
                    <el-table-column prop="zip" label="运费" width="120"></el-table-column>
                    <el-table-column prop="zip" label="备注" width="120"></el-table-column>
                    <el-table-column prop="zip" label="开单人" width="120"></el-table-column>
                    <el-table-column prop="zip" label="开单日期" width="120"></el-table-column>
                    <el-table-column prop="zip" label="运费标志" width="120"></el-table-column>
                    <el-table-column prop="zip" label="对应单据" width="120"></el-table-column>
                </el-table>
            </el-tab-pane>

            <el-tab-pane label="详细情况">

            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "Out_Check_Query",
        components: {
            CompanySearch: () => import('~/components/CompanySearch')
        },
        data() {
            return {
                value: ['2019-09-21', '2020-5-21'],

                fullSearchField: '',
                fullSearchLoading: false,
                fullSearchPanelOpen: false,
            }
        },
        methods: {
            fullSearch() {
                this.fullSearchLoading = true;
                setTimeout(() => {
                    this.abbreviatedSearchPanelOpen = false //close the other search panel
                    this.fullSearchPanelOpen = true
                    this.fullSearchLoading = false
                }, 300)
            },
            fullSearchPanelCloseAction() {
                this.fullSearchPanelOpen = false;
            },
            fullSearchChoose() {

            }
        }
    }
</script>

<style scoped>

</style>
