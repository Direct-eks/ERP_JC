<template>
    <v-card>
        <v-card-title>
            <v-toolbar dense flat>
                <v-toolbar-title>采购订单查询</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="queryTableCurrentRow"
                          :headers="purchaseQueryTableHeaders"
                          :items="queryTableData"
                          item-key="entryID"
                          @click:row="tableClick"
                          height="25vh"
                          calculate-widths
                          disable-sort
                          show-select
                          single-select
                          fixed-header
                          hide-default-footer
                          locale="zh-cn">
            </v-data-table>

            <v-data-table :headers="entryProductsTableHeaders"
                          :items="entryProductsData"
                          item-key="id"
                          height="35vh"
                          calculate-widths
                          disable-sort
                          fixed-header
                          disable-pagination
                          hide-default-footer
                          locale="zh-cn">
                <template v-slot:item.index="{ item }">
                    {{entryProductsData.indexOf(item) + 1}}
                </template>
            </v-data-table>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="choose">
                选择
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    export default {
        name: "InboundQueryPurchaseEntry",
        props: {
            companyID: {
                type: Number,
                required: true,
                default: -1,
            }
        },
        watch: {
            companyID: {
                handler: function (val, oldVal) {
                    if (val === -1) return
                    this.$postRequest(this.$api.purchaseOrderByCompany, {
                        companyID: val
                    }).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch((error) => this.$ajaxErrorHandler(error))
                },
                immediate: true
            }
        },
        data() {
            return {
                purchaseQueryTableHeaders: [
                    {
                        text: '入库单号',
                        value: 'entryID',
                        width: '60px'
                    }, {
                        text: '订单状态',
                        value: '',
                        width: '100px'
                    },{
                        text: '单位简称',
                        value: 'companyAbbreviatedName',
                        width: '100px'
                    }, {
                        text: '仓库',
                        value: 'warehouse',
                        width: '100px'
                    }, {
                        text: '部门',
                        value: 'department',
                        width: '65px'
                    }, {
                        text: '预计单据类型',
                        value: '',
                        width: '60px'
                    }, {
                        text: '总金额',
                        value: 'totalCost',
                        width: '100px'
                    }, {
                        text: '备注',
                        value: 'remark',
                        width: '120px'
                    }, {
                        text: '开单人',
                        value: 'drawer',
                        width: '120px'
                    }, {
                        text: '开单日期',
                        value: 'entryDate',
                        width: '120px'
                    }, {
                        text: '对应单据',
                        value: '',
                        width: '120px'
                    }
                ],
                queryTableData: [],
                queryTableCurrentRow: [],

                entryProductsTableHeaders: [
                    {
                        text: '序号',
                        value: 'index',
                        width: '60px'
                    }, {
                        text: '新代号',
                        value: 'newModelCode',
                        width: '100px'
                    }, {
                        text: '旧代号',
                        value: 'oldModelCode',
                        width: '100px'
                    }, {
                        text: '厂牌',
                        value: 'factoryBrandCode',
                        width: '65px'
                    }, {
                        text: '入库数量',
                        value: 'quantity',
                        width: '85px'
                    }, {
                        text: '单位',
                        value: 'unitName',
                        width: '60px'
                    }, {
                        text: '含税单价',
                        value: 'unitPriceWithTax',
                        width: '100px'
                    }, {
                        text: '不含税单价',
                        value: 'unitPriceWithoutTax',
                        width: '100px'
                    }, {
                        text: '不含税金额',
                        value: '',
                        width: '100px'
                    }, {
                        text: '税率',
                        value: '',
                        width: '120px'
                    }, {
                        text: '税额',
                        value: '',
                        width: '120px'
                    }, {
                        text: '备注',
                        value: 'remark',
                        width: '120px'
                    }, {
                        text: '库存数量',
                        value: '',
                        width: '120px'
                    }, {
                        text: '库存单价',
                        value: '',
                        width: '120px'
                    }
                ],
                entryProductsData: []
            }
        },
        methods: {
            close() {
                this.$emit('purchaseOrderClose')
            },
            tableClick(val) {
                this.queryTableCurrentRow = [val]
                this.$postRequest(this.$api.entryProductByEntry, {
                    entryID: this.queryTableCurrentRow[0].entryID
                }).then((res) => {
                    console.log('received', res.data)
                    this.entryProductsData = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
            },
            choose() {
                this.$emit('purchaseOrderChoose', this.entryProductsData)
            }
        }
    }
</script>

<style scoped>

</style>
