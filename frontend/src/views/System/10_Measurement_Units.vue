<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>计量单位</p>-->
    <v-card>
        <v-card-title class="d-flex">
            计量单位
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="newRow">
                新增计量单位
            </v-btn>
            <v-btn class="mr-3" color="success"
                   @click="saveChanges">
                保存修改
            </v-btn>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row>
                <v-data-table v-model="currentRow"
                              :headers="headers"
                              :items="tableData"
                              item-key="unitID"
                              calculate-widths
                              disable-sort
                              single-select
                              show-select
                              @click:row="tableSelect"
                              @item-selected="tableSelect2"
                              fixed-header
                              disable-pagination
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
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "MeasurementUnits",
    created() {
        this.$store.watch(state => state.taxRateOptions, () => {
            this.tableData = JSON.parse(JSON.stringify(this.$store.state.taxRateOptions))
        })
    },
    beforeMount() {
        this.$store.dispatch('getMeasurementUnits')
    },
    data() {
        return {
            mdiArrowLeft,

            headers: [
                { text: '序号', value: 'index', width: '70px' },
                { text: '计量单位', value: 'unitName', width: '70px' },
                { text: '描述', value: 'remark', width: '180px' },
            ],
            currentRow: [],
            tableData: JSON.parse(JSON.stringify(this.$store.state.taxRateOptions)),
            newRowIndex: -1
        }
    },
    methods: {
        tableSelect(row) {
            this.currentRow = [row]
        },
        tableSelect2(row) {
            if (!row.value) {
                this.currentRow = []
            }
            else {
                this.currentRow = [row.item]
            }
        },
        newRow() {
            this.tableData.push({
                unitID: this.newRowIndex--,
                name: '',
                remark: ''
            })
        },
        saveChanges() {
            this.$postRequest(this.$api.updateUnits, {
                elements: this.tableData
            }).then(data => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })

                this.$store.commit('clearMeasurementUnits')
                this.$router.replace('/system')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
