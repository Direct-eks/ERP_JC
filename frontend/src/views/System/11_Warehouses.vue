<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>仓库</p>-->
    <v-card>
        <v-card-title class="d-flex">
            仓库
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="newRow">
                新增仓库
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
                              item-key="warehouseID"
                              calculate-widths
                              disable-sort
                              single-select
                              show-select
                              checkbox-color="accent"
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
                    <template v-slot:item.location="{ item }">
                        <v-edit-dialog :return-value="item.location"
                                       persistent large save-text="确认" cancel-text="取消">
                            {{item.location}}
                            <template v-slot:input>
                                <v-text-field v-model="item.location" single-line/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.isInDefault="{ item }">
                        <v-select v-model="item.isInDefault"
                                  :items="options"
                                  item-text="name"
                                  item-value="value"
                                  hide-details="auto"
                                  dense/>
                    </template>
                    <template v-slot:item.isOutDefault="{ item }">
                        <v-select v-model="item.isOutDefault"
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
    name: "Warehouses",
    beforeMount() {
        this.$getRequest(this.$api.warehouseOptions).then(data => {
            console.log('received', data)
            this.tableData = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            headers: [
                { text: '序号', value: 'index', width: '70px' },
                { text: '仓库名称', value: 'name', width: '100px' },
                { text: '仓库位置', value: 'location', width: '100px' },
                { text: '默认为入库仓库', value: 'isInDefault', width: '120px' },
                { text: '默认为出库仓库', value: 'isOutDefault', width: '120px' },
                { text: '允许入/出库商品分类', value: 'permittedCategory', width: '150px' },
            ],
            currentRow: [],
            tableData: [],
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
                warehouseID: this.newRowIndex--,
                name: '',
                location: '',
                isDefault: 0
            })
        },
        saveChanges() {
            this.$postRequest(this.$api.updateWarehouses, {
                elements: this.tableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/system')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
