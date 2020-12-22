<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付款单查询</p>-->
    <div id="all">
        <el-row>
            <strong>付款单查询</strong>
        </el-row>

        <el-form>
            <el-form-item>
                <label style="margin-right: 5px">供货单位全称</label>
                <el-input style="width: 300px"
                          placeholder="全称"
                          v-model="fullSearchField">
                </el-input>

                <el-button @click="fullSearch()" :loading="fullSearchLoading" type="info"
                           :disabled="fullSearchPanelOpen === true">
                    单位助选
                </el-button>

                <el-button type="info">查询</el-button>
                <el-button type="info">清空</el-button>
            </el-form-item>

            <el-collapse-transition>
                <company-search v-if="fullSearchPanelOpen"
                                @fullSearchPanelClose="fullSearchPanelCloseAction"
                                @fullSearchChoose="fullSearchChooseAction">
                </company-search>
            </el-collapse-transition>

            <el-form-item>
                <label style="margin-right: 5px">记账日期范围</label>
                <el-date-picker style="width: 250px"
                                v-model="value"
                                type="daterange"
                                name="选择日期"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                format="yyyy-MM-dd"
                                :default-time="['00:00:00', '23:59:59']">
                </el-date-picker>

                <label style="margin-right: 5px; margin-left: 10px;">付款方式</label>
                <el-select v-model="value" placeholder="请选择" style="width: 150px">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <label style="margin-right: 5px; margin-left: 10px;">银行</label>
                <el-select v-model="value" placeholder="请选择" style="width: 150px">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>

        <el-divider></el-divider>

        <el-form>
            <el-form-item>
                <label style="margin-right: 5px">付款</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>
                <el-button type="info">查询</el-button>
            </el-form-item>
        </el-form>

        <el-table
                :data="tableData"
                stripe
                highlight-current-row
                style="width: 100%"
                height="300">
            <el-table-column fixed prop="date" label="付款单号" width="150"></el-table-column>
            <el-table-column prop="name" label="单位" width="120"></el-table-column>
            <el-table-column prop="province" label="付款" width="120"></el-table-column>
            <el-table-column prop="city" label="金额" width="120"></el-table-column>
            <el-table-column prop="address" label="号码" width="300"></el-table-column>
            <el-table-column prop="zip" label="银行" width="120"></el-table-column>
            <el-table-column prop="zip" label="对应单据" width="120"></el-table-column>
            <el-table-column prop="zip" label="部门" width="120"></el-table-column>
            <el-table-column prop="zip" label="说明" width="120"></el-table-column>
            <el-table-column prop="zip" label="备注" width="120"></el-table-column>
            <el-table-column prop="zip" label="开单人" width="120"></el-table-column>
            <el-table-column prop="zip" label="付款日期" width="120"></el-table-column>
        </el-table>

        <el-row>
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage4"
                    :page-sizes="[100, 200, 300, 400]"
                    :page-size="100"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="400"
                    style="width: 50%">
            </el-pagination>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Pay_Query",
        components: {
            CompanySearch: () => import("~/components/CompanySearch")
        },
        data() {
            return {
                // 单位助选 data
                fullSearchField: '',
                fullSearchLoading: false,
                fullSearchPanelOpen: false,
            }
        },
        updated: function () {
            this.$nextTick(function () {
                // Code that will run only after the
                // entire view has been re-rendered
                this.abbreviatedSearchLoading = false
                this.fullSearchLoading = false
                this.modelSearchLoading = false
                console.log('deactivate loading')
            })
        },
        methods: {
            fullSearch() {
                this.fullSearchLoading = true;
                this.abbreviatedSearchPanelOpen = false //close the other search panel
                this.fullSearchPanelOpen = true
            },
            fullSearchPanelCloseAction() {
                this.fullSearchPanelOpen = false
            },
            fullSearchChooseAction() {

            },
        }
    }
</script>

<style scoped>
    #all {
        text-align: left;
        padding: 10px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
    }

    .el-row {
        margin-bottom: 20px;
    }
</style>
