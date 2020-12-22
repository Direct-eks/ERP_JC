<template>
    <v-container>
        <v-form @keyup.enter.native="query">
            <v-row>
                <v-col>
                    <DateRangePicker @chooseDate="chooseDateAction">
                    </DateRangePicker>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="companyName"
                                  label="单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-dialog v-model="companySearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="primary"
                                   v-on="on"
                                   :disabled="companySearchPanelOpen">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch
                                @fullSearchClose="companySearchCloseAction"
                                @fullSearchChoose="companySearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
                <v-col cols="auto" v-if="!isPurchaseQuery">
                    <v-select v-model="category"
                              :items="categoryOptions"
                              item-value="value"
                              item-text="label"
                              label="类别"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-btn color="warning"
                           @click="clearSearchFields">
                        清空
                    </v-btn>
                    <v-btn color="primary"
                           @click="query">
                        查询
                    </v-btn>
                </v-col>
            </v-row>
        </v-form>

        <v-data-table v-if="!isPurchaseQuery"
                      v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="entryID"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
        </v-data-table>

        <v-data-table v-else
                      v-model="queryTableCurrentRow"
                      :headers="purchaseQueryTableHeaders"
                      :items="queryTableData"
                      item-key="entryID"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
        </v-data-table>
    </v-container>
</template>

<script>
    import DateRangePicker from "~/components/DateRangePicker";

    export default {
        name: "InboundQueryDisplayComponent",
        components: {
            CompanySearch: () => import('~/components/CompanySearch'),
            DateRangePicker,
        },
        props: {
            displayMode: {
                type: String,
                required: true
            }
        },
        beforeMount() {
            switch (this.displayMode) {
                case 'completion':
                case 'query':
                    break
                case 'purchase_query':
                    this.isPurchaseQuery = true
                    break
            }
        },
        data() {
            return {
                isPurchaseQuery: false,

                dateRange: [
                    new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                    new Date().format("yyyy-MM-dd").substr(0,10)
                ],

                companyName: '',
                companySearchPanelOpen: false,

                category: '购入',
                categoryOptions: [{
                    value: '购入',
                    label: '购入'
                }, {
                    value: '退货',
                    label: '退货'
                }],

                queryTableHeaders: [
                    {
                        text: '入库单号',
                        value: 'entryID',
                        width: '60px'
                    }, {
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
                        text: '入库类别',
                        value: 'entryType',
                        width: '85px'
                    }, {
                        text: '预计单据类型',
                        value: '',
                        width: '60px'
                    }, {
                        text: '总金额',
                        value: 'totalCost',
                        width: '100px'
                    }, {
                        text: '运输方式',
                        value: 'shippingMethod',
                        width: '100px'
                    }, {
                        text: '运单号',
                        value: 'shippingNumber',
                        width: '100px'
                    }, {
                        text: '运费',
                        value: 'shippingCost',
                        width: '120px'
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
                        text: '运费标志',
                        value: 'shippingCostType',
                        width: '120px'
                    }, {
                        text: '对应单据',
                        value: '',
                        width: '120px'
                    }
                ],
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
            }
        },
        methods: {
            chooseDateAction(val) {
                this.dateRange = val
            },
            companySearchCloseAction() {
                this.companySearchPanelOpen = false
            },
            companySearchChooseAction(val) {
                this.companyName = val.hasOwnProperty('companyFullName') ?
                    val.companyFullName : ''
            },
            clearSearchFields() {
                this.category = ''
                this.companyName = ''
            },
            query() {
                if (this.isPurchaseQuery) {
                    console.log(this.dateRange)
                    this.$postRequest(this.$api.purchaseOrderByDateRange, {
                        entryDate: this.dateRange[0],
                        entryDate2: this.dateRange[1]
                    }).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
                else if (this.category) {
                    console.log(this.dateRange)
                    this.$postRequest(this.$api.entryByDateRange, {
                        entryDate: this.dateRange[0],
                        entryDate2: this.dateRange[1]
                    }).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
            },
            tableClick(val) {
                this.queryTableCurrentRow = [val]
                this.$emit('tableClick', val)
            },
        }
    }
</script>

<style scoped>

</style>
