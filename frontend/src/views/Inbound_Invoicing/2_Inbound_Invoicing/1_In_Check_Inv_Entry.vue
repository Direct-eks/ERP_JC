<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单开票录入</p>-->
    <div id="all">
        <el-row>
            <strong>入库结账单开票录入</strong>
        </el-row>
        <el-form>
            <el-form-item>
                <label style="margin-right: 5px">开票单号</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">记账日期</label>
                <el-date-picker
                        v-model="value2"
                        type="date"
                        :disabled="true">
                </el-date-picker>

                <label style="margin-right: 5px; margin-left: 10px">号码</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">开票金额</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>
            </el-form-item>

            <el-form-item>
                <label style="margin-right: 5px">单位简称</label>
                <el-input style="width: 300px"
                          v-model="fullSearchField"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px">单位全称</label>
                <el-input style="width: 300px"
                          v-model="fullSearchField"
                          :disabled="true">
                </el-input>

                <el-button @click="fullSearch()" :loading="fullSearchLoading" type="info"
                           :disabled="fullSearchPanelOpen === true">
                    结账单位
                </el-button>
            </el-form-item>

            <el-collapse-transition>
                <company-search v-if="fullSearchPanelOpen"
                                @fullSearchPanelClose="fullSearchPanelCloseAction"
                                @fullSearchChoose="fullSearchChooseAction">
                </company-search>
            </el-collapse-transition>

            <el-form-item>
                <label style="margin-right: 5px; margin-left: 10px">开票类型</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">开票人</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">开票标志</label>
                <el-input style="width: 100px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">补票标志</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">结账金额</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>
            </el-form-item>

            <el-form-item>
                <label style="margin-right: 5px">请输入入库单据号</label>
                <el-input style="width: 80px"
                          placeholder="入结"
                          v-model="value1">
                </el-input>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>
                <el-radio v-model="value2" label="1" style="margin-right: 5px; margin-left: 10px;">入结</el-radio>
                <el-radio v-model="value2" label="2" style="margin-right: 5px; margin-left: 10px;">出退</el-radio>


                <el-button type="info" size="medium" style="margin-left: 20px"
                           @click="abbreviatedSearchChoose">
                    根据单位助选结账但未开票入库单
                </el-button>
                <el-button type="info" size="medium" style="margin-left: 50px"
                           @click="abbreviatedSearchChoose">
                    查询统计
                </el-button>
            </el-form-item>
        </el-form>

        <el-row>
            <el-button type="danger" size="medium" style="margin-left: 20px"
                       @click="abbreviatedSearchChoose">
                删除
            </el-button>
            <el-button type="primary" size="medium" style="margin-left: 20px"
                       @click="abbreviatedSearchChoose">
                存盘
            </el-button>
            <el-button type="primary" size="medium" style="margin-left: 20px"
                       @click="abbreviatedSearchChoose">
                存盘新增
            </el-button>
        </el-row>

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
            <el-table-column prop="zip" label="税率" width="120"></el-table-column>
            <el-table-column prop="zip" label="税额" width="120"></el-table-column>
            <el-table-column prop="zip" label="备注" width="120"></el-table-column>
            <el-table-column prop="zip" label="库存数量" width="120"></el-table-column>
            <el-table-column prop="zip" label="库存单价" width="120"></el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        name: "In_Check_Inv_Entry",
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
    #all {
        text-align: left;
        padding: 10px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
    }

    .el-row {
        margin-bottom: 20px;
    }
</style>
