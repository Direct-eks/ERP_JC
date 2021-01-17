<template>
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>{{ title }}</v-toolbar-title>

            <v-spacer></v-spacer>
            <v-btn color="primary" @click="querySummary">查询</v-btn>

            <v-spacer></v-spacer>
            <v-btn color="accent"
                   :to="returnPath">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>

            <template v-slot:extension>
                <v-tabs v-model="tab" @change="handleTabChange">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="summary">汇总</v-tab>
                    <v-tab key="detail" :disabled="summaryTableCurrentRow.length === 0">明细</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>

        <v-tabs-items v-model="tab">

            <v-tab-item key="summary">
                <v-container>
                    <v-data-table v-model="summaryTableCurrentRow"
                                  :headers="summaryTableHeaders"
                                  :items="summaryTableData"
                                  item-key="companyID"
                                  @click:row="summaryTableClick"
                                  height="75vh"
                                  calculate-widths
                                  disable-sort
                                  show-select
                                  single-select
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn">
                    </v-data-table>
                    <v-row>
                        <v-spacer></v-spacer>
                        <v-col cols="auto">
                            <span>总计</span>
                        </v-col>
                        <v-col cols="auto">
                            {{totalSum}}
                        </v-col>
                    </v-row>
                </v-container>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <v-container>
                    <v-data-table :headers="detailTableHeaders"
                                  :items="detailTableData"
                                  :item-key="isInbound ? 'inboundProductID' : 'outboundProductID'"
                                  height="75vh"
                                  calculate-widths
                                  disable-sort
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn">
                    </v-data-table>
                </v-container>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "NotInvoiceQueryComponent",
    props: {
        isInbound: {
            type: Boolean,
            required: true,
        }
    },
    data() {
        return {
            title: this.isInbound ? '入库结账未开票查询' : '出库结账未开票查询',
            returnPath: this.isInbound ? '/inbound_invoicing' : '/outbound_invoicing',

            mdiArrowLeftPath: mdiArrowLeft,
            tab: null,
            totalSum: 0.0,

            summaryTableHeaders: [
                { text: '单位简称', value: 'companyAbbreviatedName', width: '150px' },
                { text: '单位全称', value: 'companyFullName', width: '300px' },
                { text: '未开票总金额', value: 'totalAmount', width: '150px' },
            ],
            summaryTableData: [],
            summaryTableCurrentRow: [],

            detailTableHeaders: [
                {
                    text: this.isInbound ? '入库单号' : '出库单号',
                    value: this.isInbound ? 'inboundEntryID' : 'outboundEntryID',
                    width: '150px'
                },
                { text: '结账单号', value: 'checkoutEntrySerial', width: '150px' },
                { text: '新代号', value: 'newCode', width: '100px' },
                { text: '旧代号', value: 'oldCode', width: '100px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '80px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '80px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '80px' },
                { text: '无税金额', value: 'totalWithoutTax', width: '80px' },
                { text: '税率', value: 'taxRate', width: '65px' },
                { text: '税额', value: 'totalTax', width: '80px' },
                { text: '备注', value: 'remark', width: '120px' },
            ],
            detailTableData: [],

        }
    },
    methods: {
        handleTabChange(val) {
            if (val === 0) {
                this.currentTableRow = []
            }
        },
        querySummary() {
            if (this.isInbound) {
                this.$getRequest(this.$api.inboundNotYetInvoiceSummary).then((data) => {
                    console.log('received', data)

                    this.summaryTableData = data

                    this.totalSum = 0.0
                    this.summaryTableData.forEach((item) => {
                        this.totalSum += Number(item.totalAmount)
                    })

                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.outboundNotYetInvoiceSummary).then((data) => {
                    console.log('received', data)

                    this.summaryTableData = data

                    this.totalSum = 0.0
                    this.summaryTableData.forEach((item) => {
                        this.totalSum += Number(item.totalAmount)
                    })

                }).catch(() => {})
            }

        },
        summaryTableClick(val) {
            this.summaryTableCurrentRow = [val]
            this.detailTableData = []

            if (this.isInbound) {
                this.$getRequest(this.$api.inboundNotYetInvoiceDetail +
                    encodeURI(val.companyID)).then((data) => {
                    console.log('received', data)

                    this.detailTableData = data
                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.outboundNotYetInvoiceDetail +
                    encodeURI(val.companyID)).then((data) => {
                    console.log('received', data)

                    this.detailTableData = data
                }).catch(() => {})
            }

        }
    }
}
</script>

<style scoped>

</style>
