<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>仓库/计量单位</p>-->
    <v-card>
        <v-card-title class="d-flex">
            仓库/计量单位
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/resources">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="currentRow"
                          :headers="headers"
                          :items="tableData"
                          item-key="unitID"
                          height="60vh"
                          calculate-widths
                          disable-sort
                          single-select
                          show-select
                          @click:row="tableSelect"
                          @item-selected="tableSelect"
                          fixed-header
                          hide-default-footer
                          locale="zh-cn"
                          dense>
                <template v-slot:item.index="{ item }">
                    {{tableData.indexOf(item) + 1}}
                </template>
                <template v-slot:item.unitName="{ item }">
                    <v-edit-dialog :return-value="item.unitName"
                                   persistent large save-text="确认" cancel-text="取消">
                        {{item.unitName}}
                        <template v-slot:input>
                            <v-text-field v-model="item.unitName" single-line/>
                        </template>
                    </v-edit-dialog>
                </template>
                <template v-slot:item.remark="{ item }">
                    <v-edit-dialog :return-value="item.remark"
                                   persistent large save-text="确认" cancel-text="取消">
                        {{item.remark}}
                        <template v-slot:input>
                            <v-text-field v-model="item.remark" single-line/>
                        </template>
                    </v-edit-dialog>
                </template>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "MeasurementUnits",
    beforeMount() {
        this.$getRequest(this.$api.allUnits).then(data => {
            console.log('received', data)
            this.tableData = data
        })
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,

            headers: [
                { text: '序号', value: 'index', width: '70px' },
                { text: '计量单位', value: 'unitName', width: '70px' },
                { text: '描述', value: 'remark', width: '180px' },
            ],
            currentRow: [],
            tableData: []
        }
    },
    methods: {
        tableSelect(row) {

        }
    }
}
</script>

<style scoped>

</style>
