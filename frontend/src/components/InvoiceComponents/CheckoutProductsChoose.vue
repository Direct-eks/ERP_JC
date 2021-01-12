<template>
    <v-card height="85vh">
        <v-card-title>
            {{ panelTitle }}
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <v-text-field v-model="modelCode"
                              label="代号搜索"
                              hide-details="auto"
                              clearable
                              style="width: 400px">
                </v-text-field>
            </v-col>
            <v-btn class="mr-8"
                   color="primary"
                   @click="choose">
                选择
            </v-btn>
            <v-btn icon @click="close">
                <v-icon>{{mdiClosePath}}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="queryTableCurrentRows"
                          :headers="tableHeaders"
                          :items="queryTableData"
                          :item-key="isInbound ? 'inboundProductID' : 'outboundProductID'"
                          @click:row="tableChoose"
                          :search="modelCode"
                          height="65vh"
                          hide-default-footer
                          calculate-widths
                          disable-sort
                          disable-pagination
                          show-select
                          fixed-header
                          locale="zh-cn"
                          dense>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "InboundCheckoutProductsChoose",
    props: {
        mode: {
            type: String,
            required: true,
        },
        isInbound: {
            type: Boolean,
            required: true,
        },
        companyID: {
            type: Number,
            required: true,
        },
        invoiceType: {
            type: String,
            required: true,
        }
    },
    watch: {
        companyID: {
            handler: function (val) {
                if (val === -1) return
                this.queryProducts()
            },
            immediate: true
        },
        invoiceType: {
            handler: function () {
                if (this.companyID === -1) return
                this.queryProducts()
            },
            immediate: true
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'checkout':
            this.checkoutMode = true
            break
        case 'invoice':
            this.invoiceMode = true
            break
        }

        if (this.isInbound) {
            this.panelTitle = this.checkoutMode ? '入库商品助选' : '入库未结账商品助选'
        }
        else {
            this.panelTitle = this.isInbound ? '出库商品助选' : '出库未结账商品助选'
        }
    },
    data() {
        return {
            mdiClosePath: mdiClose,

            checkoutMode: false,
            invoiceMode: false,
            panelTitle: '',

            modelCode: '',

            tableHeaders: [
                { text: '序号', value: 'index', width: '60px', filterable: false },
                {
                    text: this.isInbound ? '入库单序号' : '出库单序号',
                    value: this.isInbound ? 'inboundEntryID' : 'outboundEntryID',
                    width: '120px',
                    filterable: false
                },
                { text: '新代号', value: 'newCode', width: '100px' },
                { text: '旧代号', value: 'oldCode', width: '100px' },
                { text: '厂牌', value: 'factoryCode', width: '65px', filterable: false },
                {
                    text: this.isInbound ? '入库数量' : '出库数量',
                    value: 'quantity', width: '80px',
                    filterable: false
                },
                { text: '单位', value: 'unitName', width: '60px', filterable: false },
                { text: '含税单价', value: 'unitPriceWithTax', width: '80px', filterable: false },
                { text: '不含税单价', value: 'unitPriceWithoutTax', width: '80px', filterable: false },
                { text: '备注', value: 'remark', width: '120px', filterable: false },
            ],
            queryTableData: [],
            queryTableCurrentRows: [],
        }
    },
    methods: {
        queryProducts() {
            if (this.isInbound) {
                if (this.checkoutMode) {
                    this.$getRequest(this.$api.inboundProductsNotCheckedOut, {
                        companyID: this.companyID, invoiceType: this.invoiceType
                    }).then((res) => {
                        console.log(res.data)
                        this.queryTableData = res.data
                    })
                }
                else if (this.invoiceMode) {
                    this.$getRequest(this.$api.inboundProductsCheckoutAndNotInvoiced, {
                        companyID: this.companyID, invoiceType: this.invoiceType
                    }).then((res) => {
                        console.log(res.data)
                        this.queryTableData = res.data
                    })
                }
            }
            else {
                if (this.checkoutMode) {
                    this.$getRequest(this.$api.outboundProductsNotCheckedOut, {
                        companyID: this.companyID, invoiceType: this.invoiceType
                    }).then((res) => {
                        console.log(res.data)
                        this.queryTableData = res.data
                    })
                }
                else if (this.invoiceMode) {
                    this.$getRequest(this.$api.outboundProductsCheckoutAndNotInvoiced, {
                        companyID: this.companyID, invoiceType: this.invoiceType
                    }).then((res) => {
                        console.log(res.data)
                        this.queryTableData = res.data
                    })
                }
            }
        },
        tableChoose(val) {
            if (this.queryTableCurrentRows.includes(val))
                this.queryTableCurrentRows.slice(this.queryTableCurrentRows.indexOf(val), 1)
            else
                this.queryTableCurrentRows.push(val)
        },
        close() {
            this.$emit('productsChoose', null)
        },
        choose() {
            this.$emit('productsChoose', this.queryTableCurrentRows)
        }
    }
}
</script>

<style scoped>

</style>
