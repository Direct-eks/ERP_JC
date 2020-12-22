<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库未结账总账</p>-->
    <div id="all">
        <el-row>
            <strong>入库未结账总账</strong>
        </el-row>

        <el-row>
            <el-button type="info">查询</el-button>
            <el-button type="info">导出汇总</el-button>
            <el-button type="info">导出明细汇总</el-button>
        </el-row>

        <el-table
                :data="tableData"
                stripe
                highlight-current-row
                style="width: 100%"
                height="300">
            <el-table-column fixed prop="date" label="序号" width="100"></el-table-column>
            <el-table-column prop="name" label="购入单位简称" width="120"></el-table-column>
            <el-table-column prop="province" label="购入单位全称" width="120"></el-table-column>
            <el-table-column prop="city" label="入库未结账价税合计总计" width="120"></el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        name: "Incomplete_Pay_Ledger"
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
