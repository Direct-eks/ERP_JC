<template>
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>{{ title }}</v-toolbar-title>

            <v-spacer></v-spacer>
            <v-btn color="primary" @click="querySummary">查询</v-btn>

            <v-spacer></v-spacer>
            <v-btn color="accent"
                   :to="returnPath">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>

            <template v-slot:extension>
                <v-tabs v-model="tab" @change="handleTabChange">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="summary">总账</v-tab>
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
                                  show-select
                                  single-select
                                  checkbox-color="accent"
                                  @click:row="summaryTableClick"
                                  @item-selected="summaryTableClick2"
                                  height="75vh"
                                  calculate-widths
                                  disable-sort
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                    <div class="d-flex">
                        <v-spacer></v-spacer>
                        <div class="my-2 mr-5">
                            <strong>总计：</strong>
                        </div>
                        <div class="my-2 mr-5">
                            <strong>{{ totalSum }}</strong>
                        </div>
                    </div>
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
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-container>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "NotCheckoutQueryComponent",
    props: {
        isInbound: {
            type: Boolean,
            required: true,
        }
    },
    data() {
        return {
            title: this.isInbound ? '入库未结账明细' : '出库未结账明细',
            returnPath: this.isInbound ? '/inbound_invoicing' : '/outbound_invoicing',

            mdiArrowLeft,
            tab: null,
            totalSum: '0.0',

            summaryTableHeaders: [
                { text: '单位简称', value: 'companyAbbreviatedName', width: '150px' },
                { text: '单位全称', value: 'companyFullName', width: '300px' },
                { text: '未结账总金额', value: 'totalAmount', width: '150px' },
            ],
            summaryTableData: [],
            summaryTableCurrentRow: [],

            detailTableHeaders: [
                {
                    text: this.isInbound ? '入库单号' : '出库单号',
                    value: this.isInbound ? 'inboundEntryID' : 'outboundEntryID',
                    width: '150px'
                },
                { text: '代号', value: 'code', width: '100px' },
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
                this.$getRequest(this.$api.inboundNotYetCheckoutSummary).then((data) => {
                    this.summaryTableData = data

                    let sum = this.$Big('0')
                    this.summaryTableData.forEach((item) => {
                        sum = sum.add(item.totalAmount)
                    })
                    this.totalSum = sum.toString()
                }).catch(() => {})
            }
            else {
                this.$getRequest(this.$api.outboundNotYetCheckoutSummary).then((data) => {
                    this.summaryTableData = data

                    let sum = this.$Big('0')
                    this.summaryTableData.forEach((item) => {
                        sum = sum.add(item.totalAmount)
                    })
                    this.totalSum = sum.toString()
                }).catch(() => {})
            }
        },
        summaryTableClick(val) {
            this.detailTableData = []

            if (this.summaryTableCurrentRow.includes(val)) {
                this.summaryTableCurrentRow = []
            }
            else {
                this.summaryTableCurrentRow = [val]
                if (this.isInbound) {
                    this.$getRequest(this.$api.inboundNotYetCheckoutDetail +
                        encodeURI(val.companyID)).then((data) => {
                        this.detailTableData = data
                    }).catch(() => {})
                } else {
                    this.$getRequest(this.$api.outboundNotYetCheckoutDetail +
                        encodeURI(val.companyID)).then((data) => {
                        this.detailTableData = data
                    }).catch(() => {})
                }
            }
        },
        summaryTableClick2(row) {
            this.detailTableData = []
            if (!row.value) {
                this.summaryTableCurrentRow = []
            }
            else {
                this.summaryTableClick(row.item)
            }
        }
    }
}
</script>

<style scoped>

</style>
