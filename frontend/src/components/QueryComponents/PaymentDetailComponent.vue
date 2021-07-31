<template>
    <v-card>
        <QueryConditions :queries.sync="queries"
                         @clear="clear">
        </QueryConditions>

        <v-data-table :headers="inboundMode ? tableHeaders1 : tableHeaders2"
                      :items="tableData"
                      item-key="entryID"
                      height="65vh"
                      hide-default-footer
                      calculate-widths
                      disable-sort
                      disable-pagination
                      fixed-header
                      locale="zh-cn"
                      dense>
            <template v-slot:header.debtorAmount="{ header }">
                <v-sheet :color="inboundMode ? 'green lighten-3':'blue lighten-2'"
                         min-height="100%" min-width="100%">
                    {{ header.text }}
                </v-sheet>
            </template>
            <template v-slot:header.creditorAmount="{ header }">
                <v-sheet :color="inboundMode ? 'blue lighten-2':'green lighten-3'"
                         min-height="100%" min-width="100%">
                    {{ header.text }}
                </v-sheet>
            </template>
            <template v-slot:header.auditAmount="{ header }">
                <v-sheet color="blue lighten-2" min-height="100%" min-width="100%">
                    {{ header.text }}
                </v-sheet>
            </template>
            <template v-slot:header.balance="{ header }">
                <v-sheet color="purple accent-2" min-height="100%" min-width="100%">
                    {{ header.text }}
                </v-sheet>
            </template>

            <template v-slot:item.auditAmount="{ item }">
                <v-edit-dialog :return-value="item.auditAmount">
                    {{ item.auditAmount }}
                    <template v-slot:input>
                        <v-text-field v-model="item.auditAmount" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>
    </v-card>
</template>

<script>
export default {
    name: "PaymentDetailComponent",
    components: {
        QueryConditions: () => import('~/components/QueryComponents/QueryConditions')
    },
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        if (this.mode === 'in')
            this.inboundMode = true
        else if (this.mode === 'out')
            this.inboundMode = false
    },
    data() {
        return {
            inboundMode: true,

            queries: {
                companyID: -1,
                companyName: '',
            },

            tableHeaders1: [
                { text: '日期', value: 'entryDate', width: '110px' },
                { text: '单据号码', value: 'entryID', width: '140px' },
                { text: '摘要', value: 'explanation', width: '140px' },
                { text: '借方金额', value: 'debtorAmount', width: '140px' },
                { text: '贷方金额', value: 'creditorAmount', width: '110px' },
                { text: '核对金额', value: 'auditAmount', width: '110px' },
                { text: '借贷', value: 'debitOrCredit', width: '60px' },
                { text: '余额', value: 'balance', width: '110px' }
            ],
            tableHeaders2: [
                { text: '日期', value: 'entryDate', width: '110px' },
                { text: '单据号码', value: 'entryID', width: '140px' },
                { text: '摘要', value: 'explanation', width: '140px' },
                { text: '借方金额', value: 'debtorAmount', width: '140px' },
                { text: '核对金额', value: 'auditAmount', width: '110px' },
                { text: '贷方金额', value: 'creditorAmount', width: '110px' },
                { text: '借贷', value: 'debitOrCredit', width: '60px' },
                { text: '余额', value: 'balance', width: '110px' }
            ],
            tableData: []
        }
    },
    methods: {
        clear() {

        }
    }
}
</script>

<style scoped>

</style>
