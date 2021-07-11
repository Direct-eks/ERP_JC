<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>部门</p>-->
    <v-card>
        <v-card-title class="d-flex">
            部门
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="newRow">
                新增部门
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
                              item-key="departmentID"
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
                    <template v-slot:item.name="{ item }">
                        <v-edit-dialog :return-value="item.name"
                                       persistent large save-text="确认" cancel-text="取消">
                            {{item.name}}
                            <template v-slot:input>
                                <v-text-field v-model="item.name" single-line/>
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
                    <template v-slot:item.isDefault="{ item }">
                        <v-select v-model="item.isDefault"
                                  :items="options"
                                  item-text="name"
                                  item-value="value"
                                  hide-details="auto"
                                  dense/>
                    </template>
                </v-data-table>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Departments",
    created() {
        this.$store.watch(state => state.departmentOptions, () => {
            this.tableData = JSON.parse(JSON.stringify(this.$store.state.departmentOptions))
        })
    },
    beforeMount() {
        this.$store.dispatch('getDepartmentOptions')
    },
    data() {
        return {
            mdiArrowLeft,

            headers: [
                { text: '序号', value: 'index', width: '70px' },
                { text: '部门名称', value: 'name', width: '100px' },
                { text: '备注', value: 'remark', width: '100px' },
                { text: '默认部门', value: 'isDefault', width: '100px' },
            ],
            currentRow: [],
            tableData: JSON.parse(JSON.stringify(this.$store.state.departmentOptions)),
            newRowIndex: -1,

            options: [
                { name: '否', value: 0 },
                { name: '是', value: 1 }
            ]
        }
    },
    methods: {
        tableSelect(row) {
            this.currentRow = [row]
        },
        tableSelect2(row) {
            if (!row.value) {
                this.currentRow = []
            } else {
                this.currentRow = [row.item]
            }
        },
        newRow() {
            this.tableData.push({
                departmentID: this.newRowIndex--,
                name: '',
                remark: '',
                isDefault: 0
            })
        },
        saveChanges() {
            this.$postRequest(this.$api.updateDepartments, {
                elements: this.tableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })

                this.$store.commit('clearDepartmentOptions')
                this.$router.replace('/system')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
