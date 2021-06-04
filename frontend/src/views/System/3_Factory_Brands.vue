<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>生产厂</p>-->
    <v-card>
        <v-card-title class="d-flex">
            生产厂
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="newRow">
                新增厂牌
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
                <v-col cols="auto">
                    <v-data-table v-model="currentRow"
                                  :headers="headers"
                                  :items="tableData"
                                  item-key="factoryBrandID"
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
                        <template v-slot:item.code="{ item }">
                            <v-edit-dialog :return-value="item.code"
                                           persistent large save-text="确认" cancel-text="取消">
                                {{item.code}}
                                <template v-slot:input>
                                    <v-text-field v-model="item.code" single-line/>
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
                </v-col>
                <v-col cols="auto">
                    <v-row class="ma-4">
                        <v-btn color="accent" fab small elevation="0" outlined
                               @click="moveItem(true)">
                            <v-icon>{{ mdiChevronUp }}</v-icon>
                        </v-btn>
                    </v-row>
                    <v-row class="ma-4">
                        <v-btn color="accent" fab small elevation="0" outlined
                               @click="moveItem(false)">
                            <v-icon>{{ mdiChevronDown }}</v-icon>
                        </v-btn>
                    </v-row>
                </v-col>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiArrowLeft, mdiChevronUp, mdiChevronDown } from "@mdi/js";

export default {
    name: "FactoryBrands",
    beforeMount() {
        this.$getRequest(this.$api.allFactoryBrands).then(data => {
            console.log('received', data)
            this.tableData = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,
            mdiChevronUp,
            mdiChevronDown,

            headers: [
                { text: '序号', value: 'sequenceNumber', width: '70px' },
                { text: '厂牌代号', value: 'code', width: '90px' },
                { text: '描述', value: 'remark', width: '180px' },
            ],
            currentRow: [],
            tableData: [],
            newRowIndex: -1,

            isDefaultOptions: [
                {option: '是', value: 0},
                {option: '否', value: 1}
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
            }
            else {
                this.currentRow = [row.item]
            }
        },
        newRow() {
            this.tableData.push({
                factoryBrandID: this.newRowIndex--,
                sequenceNumber: this.tableData.length + 1,
                code: '',
                remark: ''
            })
        },
        moveItem(up) {
            if (this.tableData.length === 0) return
            let index = this.tableData.indexOf(this.currentRow[0])
            if (up) {
                if (index === 0) return
                let item = this.tableData[index]
                let item2 = this.tableData[index - 1]
                item.sequenceNumber = index
                item2.sequenceNumber = index + 1
                this.tableData[index - 1] =
                    this.tableData.splice(index, 1, this.tableData[index - 1])[0]
            }
            else {
                if (index === this.tableData.length - 1) return
                let item = this.tableData[index]
                let item2 = this.tableData[index + 1]
                item.sequenceNumber = index + 2
                item2.sequenceNumber = index + 1
                this.tableData[index + 1] =
                    this.tableData.splice(index, 1, this.tableData[index + 1])[0]
            }
        },
        saveChanges() {
            this.$postRequest(this.$api.updateFactoryBrands, {
                elements: this.tableData
            }).then(data => {
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
