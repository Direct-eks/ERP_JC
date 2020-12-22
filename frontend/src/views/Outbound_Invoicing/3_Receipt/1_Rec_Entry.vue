<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>收款单录入</p>-->
    <div id="all">
        <el-row>
            <strong>收款单录入</strong>
        </el-row>

        <el-form>
            <el-form-item>
                <label style="margin-right: 5px">收款日期</label>
                <el-date-picker
                        v-model="value2"
                        type="date"
                        placeholder="选择日期"
                        :picker-options="pickerOptions"
                        format="yyyy-MM-dd"
                        style="width: 250px">
                </el-date-picker>

                <label style="margin-right: 5px; margin-left: 10px">开单人</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>
            </el-form-item>

            <el-form-item>
                <label style="margin-right: 5px">电话</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">单位简称</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">单位全称</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1"
                          :disabled="true">
                </el-input>

                <el-button @click="fullSearch()" :loading="fullSearchLoading" type="info"
                           :disabled="fullSearchPanelOpen === true">
                    单位助选
                </el-button>
            </el-form-item>

            <el-collapse-transition>
                <company-search v-if="fullSearchPanelOpen"
                                @fullSearchPanelClose="fullSearchPanelCloseAction"
                                @fullSearchChoose="fullSearchChooseAction">
                </company-search>
            </el-collapse-transition>

            <el-form-item>
                <label style="margin-right: 5px">收款方式</label>
                <el-select v-model="value" placeholder="请选择" style="width: 150px">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <label style="margin-right: 5px; margin-left: 10px">号码</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">金额</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">银行</label>
                <el-input style="width: 150px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>
            </el-form-item>

            <el-form-item>
                <label style="margin-right: 5px">收款说明</label>
                <el-select v-model="value" placeholder="请选择" style="width: 150px">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>

                <label style="margin-right: 5px; margin-left: 10px">说明</label>
                <el-input style="width: 300px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>

                <label style="margin-right: 5px">部门</label>
                <el-select v-model="value" placeholder="请选择" style="width: 150px">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-col style="border-style: dashed; border-width:1px; width: fit-content; padding: 5px">
                    <el-radio v-model="value2" label="1" style="margin-right: 5px">保持单位不变</el-radio>
                    <el-radio v-model="value2" label="2" style="margin-right: 5px; margin-left: 10px;">新单位</el-radio>
                    <el-button type="info">存盘新增</el-button>
                </el-col>

                <el-button type="info" style="margin: 5px">存盘</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Rec_Entry",
        components: {
            CompanySearch: () => import('~/components/CompanySearch')
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
