<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账未开票查询</p>-->
    <div style="text-align: left">

        <strong>入库结账未开票查询</strong>

        <el-tabs type="border-card">
            <el-tab-pane label="入库结账未开票按单位汇总查询">
                <el-row>
                    <el-button type="info">导出汇总</el-button>
                    <el-button type="info">导出全部未开票</el-button>
                    <el-button type="info">查询</el-button>
                </el-row>

                <el-table
                        :data="tableData"
                        stripe
                        highlight-current-row
                        style="width: 100%"
                        height="300">
                    <el-table-column fixed prop="date" label="序号" width="150"></el-table-column>
                    <el-table-column prop="name" label="单位简称" width="120"></el-table-column>
                    <el-table-column prop="province" label="单位全称" width="120"></el-table-column>
                    <el-table-column prop="city" label="未开票金额" width="120"></el-table-column>
                </el-table>
            </el-tab-pane>

            <el-tab-pane label="入库结账未开票按单位明细查询">

            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "Incomplete_Invoice_Query"
    }
</script>

<style scoped>

</style>
