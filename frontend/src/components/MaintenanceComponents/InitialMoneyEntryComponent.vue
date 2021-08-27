<template>
    <v-container>
        <v-row class="d-flex">
            <QueryConditions :queries.sync="queries">
            </QueryConditions>
            <v-spacer></v-spacer>
            <v-row>
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
            </v-row>
        </v-row>

        <v-divider class="mt-5"></v-divider>

        <v-data-table v-model="currentRow"
                      :headers="headers"
                      :items="tableData"
                      item-key="initialMoneyEntrySerial"
                      calculate-widths
                      height="75vh"
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
            <template v-slot:item.debitOrCredit="{ item }">
                <v-select v-model="item.debitOrCredit"
                          :items="options"
                          hide-details="auto"
                          dense/>
            </template>
            <template v-slot:item.balance="{ item }">
                <v-edit-dialog :return-value="item.balance"
                               @save="saveBalance(item)"
                               @cancel="saveBalance(item)"
                               @close="saveBalance(item)">
                    {{ item.balance }}
                    <template v-slot:input>
                        <v-text-field v-model="item.balance" single-line
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
        </v-data-table>

    </v-container>
</template>

<script>
export default {
    name: "InitialMoneyEntryComponent",
    components: {
        QueryConditions: () => import('~/components/QueryComponents/QueryConditions')
    },
    beforeMount() {
        if (this.mode === 'pay') {
            this.isInbound = true
        }
        else if (this.mode === 'recv') {
            this.isInbound = false
        }

        this.$getRequest(this.$api.initialMoneyEntries, {
            isInbound: this.isInbound
        }).then(data => {
            this.tableData = data
        })
    },
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            isInbound: false,
            queries: {
                companyID: -1,
                companyName: '',
            },

            headers: [
                { text: '单据号', value: 'initialMoneyEntrySerial', width: '140px' },
                { text: '开单人', value: 'drawer', width: '90px' },
                { text: '单位简称', value: 'abbreviatedName', width: '180px' },
                { text: '借贷', value: 'debitOrCredit', width: '70px' },
                { text: '余额', value: 'balance', width: '100px' },
                { text: '说明', value: 'remark', width: '180px' },
            ],
            currentRow: [],
            tableData: [],
            newRowSerial: '',

            options: ['借', '贷']
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
            if (this.queries.companyID === -1) {
                this.$store.commit('setSnackbar', {
                    message: '请先选择往来单位', color: 'warning'
                })
                return
            }
            const item = {
                initialMoneyEntrySerial: this.newRowSerial,
                entryDate: '',
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                drawer: this.$store.getters.currentUser,
                companyID: this.queries.companyID,
                abbreviatedName: this.queries.companyName,
                debitOrCredit: '',
                balance: '',
                remark: '',
            }
            this.newRowSerial += ' '
            this.tableData.push(item)
        },
        deleteRow() {
            if (this.currentRow.length === 0) return
            this.tableData.splice(this.tableData.indexOf(this.currentRow[0]), 1)
            this.currentRow = []
        },
        saveBalance(item) {
            item.balance = this.$validateFloat(item.balance, true)
        },
        save() {
            this.$postRequest(this.$api.updateInitialMoneyEntries, this.tableData, {
                isInbound: this.isInbound
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/maintenance')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
