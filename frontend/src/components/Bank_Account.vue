<template>
    <v-card>
        <v-card-title>
            <v-col cols="auto">
                <v-btn class="mr-3" color="primary" @click="newRow">
                    新增
                </v-btn>
            </v-col>
            <v-col cols="auto">
                <v-btn class="mr-3" color="warning" @click="deleteRow">
                    删除
                </v-btn>
            </v-col>
            <v-col cols="auto">
                <v-btn class="mr-3" color="success" @click="save">
                    保存(修改)
                </v-btn>
            </v-col>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="currentRow"
                          :headers="headers"
                          :items="tableData"
                          item-key="bankAccountID"
                          calculate-widths
                          height="55vh"
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
                <template v-slot:item.accountNumber="{ item }">
                    <v-edit-dialog :return-value="item.accountNumber">
                        {{ item.accountNumber }}
                        <template v-slot:input>
                            <v-text-field v-model="item.accountNumber" single-line
                                          @focus="$event.target.setSelectionRange(0, 100)"/>
                        </template>
                    </v-edit-dialog>
                </template>
                <template v-slot:item.remark="{ item }">
                    <v-edit-dialog :return-value="item.remark">
                        {{ item.remark }}
                        <template v-slot:input>
                            <v-text-field v-model="item.remark" single-line
                                          @focus="$event.target.setSelectionRange(0, 100)"/>
                        </template>
                    </v-edit-dialog>
                </template>
                <template v-slot:item.isVisible="{ item }">
                    <v-select v-model="item.isVisible"
                              :items="options2"
                              item-text="label"
                              item-value="value"
                              hide-details="auto"
                              dense/>
                </template>
                <template v-slot:item.inUse="{ item }">
                    <v-select v-model="item.inUse"
                              :items="options2"
                              item-text="label"
                              item-value="value"
                              hide-details="auto"
                              dense/>
                </template>
                <template v-slot:item.initialBalance="{ item }">
                    <v-edit-dialog :return-value="item.initialBalance"
                                   @save="saveBalance(item)"
                                   @cancel="saveBalance(item)"
                                   @close="saveBalance(item)">
                        {{ item.initialBalance }}
                        <template v-slot:input>
                            <v-text-field v-model="item.initialBalance" single-line
                                          @focus="$event.target.setSelectionRange(0, 100)"/>
                        </template>
                    </v-edit-dialog>
                </template>
                <template v-slot:item.inOrOut="{ item }">
                    <v-select v-model="item.inOrOut"
                              :items="options"
                              hide-details="auto"
                              dense/>
                </template>
                <template v-slot:item.initialRemark="{ item }">
                    <v-edit-dialog :return-value="item.initialRemark">
                        {{ item.initialRemark }}
                        <template v-slot:input>
                            <v-text-field v-model="item.initialRemark" single-line
                                          @focus="$event.target.setSelectionRange(0, 100)"/>
                        </template>
                    </v-edit-dialog>
                </template>
                <template v-slot:item.initialDate="{ item }">
                    <v-edit-dialog :return-value="item.initialDate">
                        {{ item.initialDate }}
                        <template v-slot:input>
                            <v-text-field v-model="item.initialDate" single-line
                                          @focus="$event.target.setSelectionRange(0, 100)"/>
                        </template>
                    </v-edit-dialog>
                </template>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
export default {
    name: "Bank_Account",
    beforeMount() {
        this.$getRequest(this.$api.allBankAccounts).then(data => {
            this.tableData = data
        }).catch(() => {})
    },
    data() {
        return {
            headers: [
                { text: '名称', value: 'name', width: '200px' },
                { text: '账号', value: 'accountNumber', width: '200px' },
                { text: '说明', value: 'remark', width: '180px' },
                { text: '可见', value: 'isVisible', width: '75px' },
                { text: '启用', value: 'inUse', width: '75px' },
                { text: '余额', value: 'initialBalance', width: '120px' },
                { text: '收支', value: 'inOrOut', width: '75px' },
                { text: '期初说明', value: 'initialRemark', width: '180px' },
                { text: '期初日期', value: 'initialDate', width: '120px' },
            ],
            currentRow: [],
            tableData: [],
            newRowSerial: '',

            options: ['收', '支'],
            options2: [
                { label: '是', value: 1 },
                { label: '否', value: 0 }
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
            const item = {

            }
            this.tableData.push(item)
        },
        deleteRow() {
            if (this.currentRow.length === 0) return
            this.tableData.splice(this.tableData.indexOf(this.currentRow[0]), 1)
            this.currentRow = []
        },
        saveBalance(item) {
            item.initialBalance = this.$validateFloat(item.initialBalance, true)
        },
        save() {
            this.$postRequest(this.$api.updateBankAccounts, {
                elements: this.tableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/maintenance')
            })
        }
    }
}
</script>

<style scoped>

</style>
