<template>
    <v-card>
        <v-data-table :headers="customerMode ? tableHeaders1 : tableHeaders2"
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
                <v-sheet :color="customerMode ? 'green lighten-3':'blue lighten-2'"
                         min-height="100%" min-width="100%">
                    {{ header.text }}
                </v-sheet>
            </template>
            <template v-slot:header.creditorAmount="{ header }">
                <v-sheet :color="customerMode ? 'blue lighten-2':'green lighten-3'"
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
    props: {
        mode: {
            type: String,
            required: true
        },
        companyID: {
            type: Number,
            required: true
        },
    },
    watch: {
        companyID (val) {
            if (val === -1) {
                if (this.tableData.length === 0) return
                // clear existing data
                this.tableData = []
                return
            }
            this.loading = true
            this.$getRequest(this.customerMode ? this.$api.receivableDetail :
                this.$api.payableDetail, {
                companyID: val
            }).then(data => {
                this.$querySuccessMessage(data)
                this.tableData = data
                this.loading = false
            }).catch(() => { this.loading = false })
        }
    },
    beforeMount() {
        if (this.mode === 'supplier')
            this.customerMode = false
        else if (this.mode === 'customer')
            this.customerMode = true
    },
    data() {
        return {
            customerMode: true,
            loading: false,

            tableHeaders1: [
                { text: '日期', value: 'entryDate', width: '110px' },
                { text: '单据号码', value: 'entryID', width: '140px' },
                { text: '摘要', value: 'explanation', width: '200px' },
                { text: '借方金额', value: 'debtorAmount', width: '140px' },
                { text: '贷方金额', value: 'creditorAmount', width: '110px' },
                { text: '核对金额', value: 'auditAmount', width: '110px' },
                { text: '借贷', value: 'debitOrCredit', width: '60px' },
                { text: '余额', value: 'balance', width: '110px' }
            ],
            tableHeaders2: [
                { text: '日期', value: 'entryDate', width: '110px' },
                { text: '单据号码', value: 'entryID', width: '140px' },
                { text: '摘要', value: 'explanation', width: '200px' },
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

    }
}
</script>

<style scoped>

</style>
