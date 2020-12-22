<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>付运费修改</p>-->
    <div style="text-align: left">
        <strong>付运费修改</strong>
        <el-tabs type="border-card">
            <el-tab-pane label="浏览">
                <el-form>
                    <el-form-item>
                        <label style="margin-right: 5px">选择日期</label>
                        <el-date-picker style="width: 250px"
                                        v-model="value"
                                        type="daterange"
                                        name="选择日期"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        format="yyyy-MM-dd"
                                        :default-time="['00:00:00', '23:59:59']">
                        </el-date-picker>

                        <label style="margin-right: 5px">供货单位全称</label>
                        <el-input style="width: 300px"
                                  placeholder="全称"
                                  v-model="fullSearchField">
                        </el-input>

                        <el-button @click="fullSearch()" :loading="fullSearchLoading" type="info"
                                   :disabled="fullSearchPanelOpen === true">
                            单位助选
                        </el-button>

                        <el-button type="primary">查询</el-button>
                    </el-form-item>

                    <el-collapse-transition>
                        <company-search v-if="fullSearchPanelOpen"
                                        @fullSearchPanelClose="fullSearchPanelCloseAction"
                                        @fullSearchChoose="fullSearchChooseAction">
                        </company-search>
                    </el-collapse-transition>
                </el-form>

                <el-table
                        :data="tableData"
                        stripe
                        highlight-current-row
                        style="width: 100%"
                        height="300">
                    <el-table-column fixed prop="date" label="序号" width="150"></el-table-column>
                    <el-table-column prop="name" label="新代号" width="120"></el-table-column>
                    <el-table-column prop="province" label="旧代号" width="120"></el-table-column>
                    <el-table-column prop="city" label="厂牌" width="120"></el-table-column>
                    <el-table-column prop="address" label="入库数量" width="300"></el-table-column>
                    <el-table-column prop="zip" label="单位" width="120"></el-table-column>
                    <el-table-column prop="zip" label="含税单价" width="120"></el-table-column>
                    <el-table-column prop="zip" label="不含税单价" width="120"></el-table-column>
                    <el-table-column prop="zip" label="不含税金额" width="120"></el-table-column>
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
            </el-tab-pane>

            <el-tab-pane label="详细情况">
                <el-form>
                    <el-form-item>
                        <label style="margin-right: 5px">结账日期</label>
                        <el-date-picker
                                v-model="value2"
                                type="date"
                                placeholder="选择日期"
                                readonly
                                :picker-options="pickerOptions">
                        </el-date-picker>

                        <label style="margin-right: 5px; margin-left: 10px;">开单人</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1"
                                  disabled>
                        </el-input>

                        <label style="margin-right: 5px; margin-left: 10px;">单据运费金额</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1">
                        </el-input>
                    </el-form-item>

                    <el-form-item>
                        <label style="margin-right: 5px">电话</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1"
                                  disabled>
                        </el-input>

                        <label style="margin-right: 5px; margin-left: 10px;">单位简称</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1"
                                  disabled>
                        </el-input>

                        <label style="margin-right: 5px; margin-left: 10px;">单位全称</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1"
                                  disabled>
                        </el-input>
                    </el-form-item>

                    <el-form-item>
                        <label style="margin-right: 5px">承运单位</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1"
                                  disabled>
                        </el-input>

                        <label style="margin-right: 5px; margin-left: 10px;">开票号码</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1"
                                  disabled>
                        </el-input>

                        <label style="margin-right: 5px; margin-left: 10px;">开票日期</label>
                        <el-date-picker
                                v-model="value2"
                                type="date"
                                placeholder="选择日期"
                                readonly
                                :picker-options="pickerOptions">
                        </el-date-picker>

                        <label style="margin-right: 5px; margin-left: 10px;">抵扣</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1">
                        </el-input>
                    </el-form-item>

                    <el-form-item>
                        <label style="margin-right: 5px">运费标志</label>
                        <el-select v-model="value" placeholder="请选择" style="width: 150px">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>

                        <label style="margin-right: 5px; margin-left: 10px;">运费开票金额</label>
                        <el-input style="width: 150px"
                                  placeholder="wh"
                                  v-model="value1">
                        </el-input>

                        <label style="margin-right: 5px; margin-left: 10px;">说明</label>
                        <el-input style="width: 300px"
                                  placeholder="wh"
                                  v-model="value1">
                        </el-input>
                    </el-form-item>
                </el-form>

                <el-row>
                    <el-col :span="12">
                        <el-card>
                            <el-table
                                    :data="tableData"
                                    stripe
                                    highlight-current-row
                                    style="width: 100%"
                                    height="300">
                                <el-table-column fixed prop="date" label="序号" width="150"></el-table-column>
                                <el-table-column prop="name" label="新代号" width="120"></el-table-column>
                                <el-table-column prop="province" label="旧代号" width="120"></el-table-column>
                                <el-table-column prop="city" label="厂牌" width="120"></el-table-column>
                            </el-table>
                        </el-card>
                    </el-col>

                    <el-col :span="12">
                        <el-card>
                            <el-table
                                    :data="tableData"
                                    stripe
                                    highlight-current-row
                                    style="width: 100%"
                                    height="300">
                                <el-table-column fixed prop="date" label="序号" width="150"></el-table-column>
                                <el-table-column prop="name" label="新代号" width="120"></el-table-column>
                                <el-table-column prop="province" label="旧代号" width="120"></el-table-column>
                                <el-table-column prop="city" label="厂牌" width="120"></el-table-column>
                            </el-table>
                        </el-card>
                    </el-col>
                </el-row>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "Cost_Modify",
        components: {
            CompanySearch: () => import("~/components/CompanySearch"),
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

</style>
