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
            <template v-slot:item.remark="{ item }">
                <v-edit-dialog :return-value="item.remark">
                    {{ item.remark }}
                    <template v-slot:input>
                        <v-text-field v-model="item.remark" single-line/>
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
                { text: '借贷', value: 'borrowOrLend', width: '70px' },
                { text: '余额', value: 'balance', width: '100px' },
                { text: '说明', value: 'remark', width: '180px' },
            ],
            currentRow: [],
            tableData: [],
            newRowSerial: '-',

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
            const item = {
                initialMoneyEntrySerial: this.newRowSerial,
                drawer: this.$store.getters.currentUser,
                companyID: -1,
                abbreviatedName: '',
                borrowOrLend: '',
                balance: '',
                remark: '',
            }
        },
        deleteRow() {

        },
        save() {

        }
    }
}
</script>

<style scoped>

</style>
