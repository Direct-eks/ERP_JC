<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单开票查询</p>-->
    <div id="all">
        <el-row>
            <strong>入库结账单开票查询</strong>
        </el-row>

        <el-form>
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

                <label style="margin-right: 5px; margin-left: 10px">开票日期</label>
                <el-date-picker
                        v-model="value2"
                        type="date"
                        placeholder="选择日期"
                        :picker-options="pickerOptions"
                        format="yyyy-MM-dd"
                        style="width: 250px">
                </el-date-picker>
            </el-form-item>

            <el-form-item>
                <label style="margin-right: 5px">单位简称</label>
                <el-input style="width: 400px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">补票选项</label>
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

            <el-form-item>
                <label style="margin-right: 5px">发票号码</label>
                <el-input style="width: 400px"
                          placeholder="wh"
                          v-model="value1">
                </el-input>

                <label style="margin-right: 5px; margin-left: 10px">发票类型</label>
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
        </el-form>

        <el-button type="danger">查询</el-button>

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
        name: "In_Check_Inv_Query"
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
