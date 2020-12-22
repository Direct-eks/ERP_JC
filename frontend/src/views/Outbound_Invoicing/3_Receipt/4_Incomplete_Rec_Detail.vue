<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>出库未结账明细</p>-->
    <div id="all">
        <el-row>
            <strong>出库未结账明细</strong>
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
                <el-button type="info">导出</el-button>
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

    </div>
</template>

<script>
    export default {
        name: "Incomplete_Rec_Detail",
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
