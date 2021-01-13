<template>
    <v-card>
        <v-card-title>
            {{ panelTitle }}
            <v-spacer></v-spacer>
            <v-btn class="mr-8"
                   color="primary"
                   @click="choose">
                选择
            </v-btn>
            <v-btn icon @click="close">
                <v-icon>{{ mdiClosePath }}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="tableSelection"
                          :headers="tableHeaders"
                          :items="tableData"
                          :item-key="isInbound ? 'inboundEntryID' : 'outboundEntryID'"
                          @click:row="tableChoose"
                          height="45vh"
                          calculate-widths
                          show-select
                          disable-sort
                          fixed-header
                          disable-pagination
                          hide-default-footer
                          locale="zh-cn"
                          dense>
                <template v-slot:item.index="{ item }">
                    {{ tableData.indexOf(item) + 1 }}
                </template>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "ShippingCostEntryChoose",
    props: {
        isInbound: {
            type: Boolean,
            required: true
        },
        companyID: {
            type: Number,
            required: true
        },
        shippingCostType: {
            type: String,
            required: true
        },
    },
    watch: {
        companyID(val) {
            if (val === -1) return
            if (this.shippingCostType === '') return
            this.queryEntries()
        },
        shippingCostType(val) {
            if (this.companyID === -1) return
            if (val === '') return
            this.queryEntries()
        }
    },
    data() {
        return {
            mdiClosePath: mdiClose,

            panelTitle: this.isInbound ? '入库单运费选择' : '出库单运费选择',

            tableHeaders: [
                { text: '序号', value: 'index', width: '60px' },
                {
                    text: this.isInbound ? '入库单号' : '出库单号',
                    value: this.isInbound ? 'inboundEntryID' : 'outboundEntryID',
                    width: '100px'
                },
                { text: '单位', value: 'companyAbbreviatedName', width: '100px' },
                { text: '运费', value: 'shippingCost', width: '80px' },
            ],
            tableSelection: [],
            tableData: [],
        }
    },
    methods: {
        queryEntries() {
            if (this.isInbound) {
                this.$getRequest(this.$api.inboundEntriesByCompanyAndShippingCostType, {
                    companyID: this.companyID,
                    shippingCostType: this.shippingCostType,
                }).then((res) => {
                    console.log('received', res.data)
                    this.tableData = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
            }
            else {
                this.$getRequest(this.$api.outboundEntriesByCompanyAndShippingCostType, {
                    companyID: this.companyID,
                    shippingCostType: this.shippingCostType
                }).then((res) => {
                    console.log('received', res.data)
                    this.tableData = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
            }

        },
        tableChoose(val) {
            if (this.tableSelection.includes(val))
                this.tableSelection.slice(this.tableSelection.indexOf(val), 1)
            else
                this.tableSelection.push(val)
        },
        close() {
            if (this.isInbound)
                this.$emit('inboundEntryChoose', null)
            else
                this.$emit('outboundEntryChoose', null)
        },
        choose() {
            if (this.isInbound)
                this.$emit('inboundEntryChoose', this.tableSelection)
            else
                this.$emit('outboundEntryChoose', this.tableSelection)
        },
    }
}
</script>

<style scoped>

</style>
