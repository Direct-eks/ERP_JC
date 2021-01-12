<template>
    <v-card>
        <v-card-title>
            <v-toolbar dense flat>
                <v-toolbar-title>{{ title }}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                    <v-icon>{{ mdiClosePath }}</v-icon>
                </v-btn>
            </v-toolbar>
        </v-card-title>
        <v-card-text>
            <v-data-table v-if="isSalesOrderMode"
                          v-model="queryTableCurrentRow"
                          :headers="isSalesOrderMode ? salesQueryTableHeaders : quotaQueryTableHeaders"
                          :items="queryTableData"
                          :item-key="isSalesOrderMode ? 'salesOrderEntryID' : 'quotaEntryID'"
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
                    {{ entryProductsData.indexOf(item) + 1 }}
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
import {mdiClose} from "@mdi/js";

export default {
    name: "ImportSalesOrder",
    props: {
        mode: {
            type: String,
            required: true,
        },
        companyID: {
            type: Number,
            required: true,
            default: -1,
        }
    },
    watch: {
        companyID: {
            handler: function (val) {
                if (val === -1) return
                if (this.isSalesOrderMode) {
                    this.$getRequest(this.$api.salesOrdersByCompanyID +
                        encodeURI(String(this.companyID))
                    ).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch((error) => this.$ajaxErrorHandler(error))
                }
                else if (this.isQuotaMode) {
                    this.$getRequest(this.$api.quotaByCompanyID +
                        encodeURI(String(this.companyID))
                    ).then((res) => {
                        console.log('received', res.data)
                        this.queryTableData = res.data
                    }).catch((error) => this.$ajaxErrorHandler(error))
                }
            },
            immediate: true
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'salesOrder':
            this.title = '销售订单查询'
            this.isSalesOrderMode = true
            break
        case 'quota':
            this.title = '报价单查询'
            this.isQuotaMode = true
            break
        }
    },
    data() {
        return {
            title: '',
            mdiClosePath: mdiClose,

            isSalesOrderMode: false,
            isQuotaMode: false,

            salesQueryTableHeaders: [
                {text: '销售订单', value: 'salesOrderEntryID', width: '80px'},
                {text: '订单状态', value: 'executionStatus', width: '65px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '仓库', value: 'warehouseName', width: '65px'},
                {text: '部门', value: 'departmentName', width: '80px'},
                {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                {text: '总金额', value: 'totalAmount', width: '85px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '开单人', value: 'drawer', width: '60px'},
                {text: '开单日期', value: 'entryDate', width: '80px'},
                {text: '创建日期', value: 'creationDate', width: '80px'}
            ],
            quotaQueryTableHeaders: [
                {text: '销售订单', value: 'salesOrderEntryID', width: '80px'},
                {text: '单位简称', value: 'companyAbbreviatedName', width: '120px'},
                {text: '预计单据类型', value: 'invoiceType', width: '60px'},
                {text: '总金额', value: 'totalAmount', width: '85px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '开单人', value: 'drawer', width: '60px'},
                {text: '开单日期', value: 'entryDate', width: '80px'},
                {text: '创建日期', value: 'creationDate', width: '80px'}
            ],

            queryTableData: [],
            queryTableCurrentRow: [],

            entryProductsTableHeaders: [
                {text: '序号', value: 'index', width: '60px'},
                {text: '新代号', value: 'newCode', width: '100px'},
                {text: '旧代号', value: 'oldCode', width: '100px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '入库数量', value: 'quantity', width: '80px'},
                {text: '单位', value: 'unitName', width: '60px'},
                {text: '含税单价', value: 'unitPriceWithTax', width: '80px'},
                {text: '不含税单价', value: 'unitPriceWithoutTax', width: '80px'},
                {text: '不含税金额', value: 'totalWithoutTax', width: '80px'},
                {text: '税率', value: 'taxRate', width: '60px'},
                {text: '税额', value: 'totalTax', width: '80px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '库存数量', value: 'stockQuantity', width: '70px'},
                {text: '库存单价', value: 'stockUnitPrice', width: '70px'}
            ],
            entryProductsData: []
        }
    },
    methods: {
        close() {
            if (this.isSalesOrderMode) this.$emit('salesOrderChoose', null)
            else if (this.isQuotaMode) this.$emit('quotaOrder', null)
        },
        tableClick(val) {
            this.queryTableCurrentRow = [val]

            let tempProducts = val.purchaseOrderProducts
            for (let item of tempProducts) {
                item.totalWithoutTax = (item.quantity * item.unitPriceWithoutTax).toFixed(2)
                item.totalTax = (item.quantity * (item.unitPriceWithTax - item.totalWithoutTax)).toFixed(2)
            }
            this.entryProductsData = tempProducts
        },
        choose() {
            if (this.isSalesOrderMode) this.salesOrderChoose()
            else if (this.isQuotaMode) this.quotaChoose()
        },
        salesOrderChoose() {
            this.$emit('salesOrderChoose', this.entryProductsData)
        },
        quotaChoose() {
            this.$emit('quotaChoose', this.entryProductsData)
        }
    }
}
</script>

<style scoped>

</style>
