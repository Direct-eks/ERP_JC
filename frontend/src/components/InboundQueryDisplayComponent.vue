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
                        <CompanySearch @fullSearchChoose="companySearchChooseAction">
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
                    <v-btn class="mr-4"
                           color="warning"
                           @click="clearSearchFields">
                        清空
                    </v-btn>
                    <v-btn class="mr-4"
                           color="primary"
                           @click="query">
                        查询
                    </v-btn>
                    <v-btn color="accent"
                           @click="queryModificationRecord">
                        查询修改记录
                    </v-btn>
                </v-col>
            </v-row>
        </v-form>

        <v-data-table v-if="!isPurchaseQuery"
                      v-model="queryTableCurrentRow"
                      :headers="queryTableHeaders"
                      :items="queryTableData"
                      item-key="inboundEntryID"
                      @click:row="tableClick"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      single-select
                      fixed-header
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.inboundEntryID="{ item }">
                <v-chip :color="item.isModified === 1 ? 'red' : null">
                    {{ item.inboundEntryID }}
                </v-chip>
            </template>
        </v-data-table>

        <v-data-table v-else
                      v-model="queryTableCurrentRow"
                      :headers="purchaseQueryTableHeaders"
                      :items="queryTableData"
                      item-key="purchaseOrderEntryID"
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

        <v-divider></v-divider>

        <v-data-table :headers="modificationRecordTableHeader"
                      :items="modificationRecords"
                      disable-sort
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
                case 'modify':
                case 'query':
                    break
                case 'purchaseOrder':
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

                companyID: -1,
                companyName: '',
                companySearchPanelOpen: false,

                category: '购入',
                categoryOptions: [
                    {value: '购入', label: '购入'},
                    {value: '出退', label: '出退'}
                ],

                queryTableHeaders: [
                    {text: '入库单号', value: 'inboundEntryID', width: '80px'},
                    {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                    {text: '仓库', value: 'warehouseName', width: '65'},
                    {text: '部门', value: 'departmentName', width: '80px'},
                    {text: '入库类别', value: 'classification', width: '60px'},
                    {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                    {text: '总金额', value: 'totalCost', width: '85px'},
                    {text: '运输方式', value: 'relevantCompanyName', width: '100px'},
                    {text: '运单号', value: 'shippingNumber', width: '100px'},
                    {text: '运费', value: 'shippingCost', width: '65px'},
                    {text: '备注', value: 'remark', width: '120px'},
                    {text: '开单人', value: 'drawer', width: '60px'},
                    {text: '开单日期', value: 'entryDate', width: '80px'},
                    {text: '运费标志', value: 'shippingCostType', width: '60px'}
                ],
                purchaseQueryTableHeaders: [
                    {text: '采购订单', value: 'purchaseOrderEntryID', width: '80px'},
                    {text: '订单状态', value: 'executionStatus', width: '65px'},
                    {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                    {text: '仓库', value: 'warehouseName', width: '65px'},
                    {text: '部门', value: 'departmentName', width: '80px'},
                    {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                    {text: '总金额', value: 'totalCost', width: '85px'},
                    {text: '备注', value: 'remark', width: '120px'},
                    {text: '开单人', value: 'drawer', width: '60px'},
                    {text: '开单日期', value: 'entryDate', width: '80px'},
                    {text: '创建日期', value: 'creationDate', width: '80px'}
                ],
                queryTableData: [],
                queryTableCurrentRow: [],

                modificationRecordTableHeader: [
                    {text: '修改日期', value: 'modificationDate', width: '100px'},
                    {text: '修改明细', value: '', width: ''}
                ],
                modificationRecords: []
            }
        },
        methods: {
            chooseDateAction(val) {
                this.dateRange = val
            },
            companySearchChooseAction(val) {
                if (val) {
                    this.companyName = val.hasOwnProperty('fullName') ?
                        val.fullName : ''
                    this.companyID = val.hasOwnProperty('companyID') ?
                        val.companyID : ''
                }
                this.companySearchPanelOpen = false
            },
            clearSearchFields() {
                this.category = ''
                this.companyName = ''
            },
            query() {
                if (this.isPurchaseQuery) {
                    console.log(this.dateRange)
                    this.$getRequest(this.$api.purchaseOrdersInDateRangeByCompanyID +
                        'startDate=' + encodeURI(this.dateRange[0]) + '&endDate=' +
                        encodeURI(this.dateRange[1]) + '&companyID=' +
                        encodeURI(String(this.companyID))
                    ).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
                else {
                    console.log(this.dateRange)
                    this.$getRequest(this.$api.entriesInDateRange +
                        'startDate=' + encodeURI( this.dateRange[0]) + '&endDate=' +
                        encodeURI(this.dateRange[1]) + '&type=' + encodeURI(this.category) +
                        '&companyID=' + encodeURI(String(this.companyID)) + '&forModify=false'
                    ).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch(error => this.$ajaxErrorHandler(error))
                }
            },
            queryModificationRecord() {
                let url = this.$api.modificationRecordsBySerial + encodeURI(this.queryTableCurrentRow[0].inboundEntryID)
                console.log(url)
                this.$getRequest(url
                ).then((res) => {
                    console.log('received', res.data)
                    this.modificationRecords = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
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
