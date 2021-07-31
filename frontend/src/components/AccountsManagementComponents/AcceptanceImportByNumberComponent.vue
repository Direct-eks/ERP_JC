<template>
    <v-card>
        <v-card-title>
            票号检索
            <v-spacer></v-spacer>
            <v-btn icon @click="close">
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="tableCurrentRow"
                          :headers="tableHeaders"
                          :items="tableData"
                          item-key="acceptanceEntrySerial"
                          @click:row="tableChoose"
                          @dblclick:row="directChoose"
                          height="65vh"
                          hide-default-footer
                          calculate-widths
                          disable-sort
                          disable-pagination
                          single-select
                          show-select
                          checkbox-color="accent"
                          fixed-header
                          locale="zh-cn"
                          dense>
            </v-data-table>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="success" @click="importEntry">确认</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "AcceptanceImportByNumberComponent",
    props: {
        number: {
            type: String,
            required: true
        },
        trigger: {
            type: Boolean,
            required: true,
        }
    },
    watch: {
        trigger: {
            handler: function (val) {
                if (val) {
                    if (this.number === '') {
                        this.$store.commit('setSnackbar', {
                            message: '检索单号不能为空', color: 'warning'
                        })
                        return
                    }
                    this.$getRequest(this.$api.getAcceptanceEntryByNumber, {
                        number: this.number
                    }).then(data => {
                        this.tableData = data
                    }).catch(() => {})
                }
            }
        }
    },
    data() {
        return {
            mdiClose,

            tableHeaders: [
                { text: '单据号', value: 'acceptanceEntrySerial', width: '140px' },
                { text: '单位', value: 'companyAbbreviatedName', width: '140px' },
                { text: '总金额', value: 'amount', width: '110px' },
                { text: '承兑票号', value: 'number', width: '140px' },
                { text: '收到日期', value: 'entryDate', width: '110px' },
                { text: '出票日期', value: 'issueDate', width: '110px' },
                { text: '到期日期', value: 'expirationDate', width: '110px' },
                { text: '类型', value: 'type', width: '60px' },
                { text: '开单人', value: 'drawer', width: '75px' },
                { text: '标志', value: 'classification', width: '60px' },
            ],
            tableData: [],
            tableCurrentRow: [],

        }
    },
    methods: {
        close() {
            this.$emit('importEntry')
        },
        tableChoose(row) {
            if (!this.tableCurrentRow.includes(row)) {
                this.tableCurrentRow = []
            }
            else {
                this.tableCurrentRow = [row]
            }
        },
        directChoose(_, row) {
            this.$emit('importEntry', row.item)
        },
        importEntry() {
            if (this.tableCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '请选择单据', color: 'warning'
                })
                return
            }
            this.$emit('importEntry', this.tableCurrentRow[0])
        }
    }
}
</script>

<style scoped>

</style>
