<template>
    <v-card>
        <v-card-title>
            <v-toolbar dense flat>
                <v-toolbar-title>采购订单查询</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                    <v-icon>{{ mdiClose }}</v-icon>
                </v-btn>
            </v-toolbar>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="queryTableCurrentRow"
                          :headers="purchaseQueryTableHeaders"
                          :items="queryTableData"
                          item-key="purchaseOrderEntryID"
                          @click:row="tableClick"
                          @item-selected="tableClick2"
                          height="25vh"
                          calculate-widths
                          disable-sort
                          show-select
                          single-select
                          fixed-header
                          hide-default-footer
                          locale="zh-cn">
            </v-data-table>

            <v-data-table v-model="entryProductsCurrRow"
                          :headers="entryProductsTableHeaders"
                          :items="entryProductsData"
                          item-key="purchaseOrderProductID"
                          height="35vh"
                          calculate-widths
                          disable-sort
                          fixed-header
                          show-select
                          @click:row="table2Click"
                          @item-selected="table2Click2"
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
import {mdiClose} from '@mdi/js'

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
            handler: function (val) {
                if (val === -1) return
                this.$getRequest(this.$api.purchaseOrdersByCompanyID +
                    encodeURI(String(this.companyID))
                ).then((data) => {
                    this.queryTableData = data
                }).catch(() => {})
            },
            immediate: true
        }
    },
    data() {
        return {
            mdiClose,

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

            entryProductsTableHeaders: [
                {text: '序号', value: 'index', width: '60px'},
                {text: '代号', value: 'code', width: '100px'},
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
            entryProductsData: [],
            entryProductsCurrRow: []
        }
    },
    methods: {
        close() {
            this.$emit('purchaseOrderChoose', null)
        },
        calculateTax(row) {
            let tempProducts = row.purchaseOrderProducts
            for (const item of tempProducts) {
                item['totalWithoutTax'] = this.$Big(item.unitPriceWithoutTax).times(item.quantity).toString()
                item['totalTax'] = this.$Big(item.unitPriceWithTax).times(item.quantity).sub(item.totalWithoutTax).toString()
            }
            this.entryProductsData = tempProducts
        },
        tableClick(row) {
            if (this.queryTableCurrentRow.indexOf(row) !== -1) {
                this.queryTableCurrentRow = []
                this.entryProductsData = []
                this.entryProductsCurrRow = []
            }
            else {
                this.queryTableCurrentRow = [row]
                this.calculateTax(row)
                this.entryProductsCurrRow = []
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.queryTableCurrentRow = []
                this.entryProductsData = []
                this.entryProductsCurrRow = []
            }
            else {
                this.queryTableCurrentRow = [row.item]
                this.calculateTax(row)
                this.entryProductsCurrRow = []
            }
        },
        table2Click(row) {
            if (this.entryProductsCurrRow.indexOf(row) !== -1) {
                this.entryProductsCurrRow.splice(this.entryProductsCurrRow.indexOf(row), 1)
            }
            else {
                this.entryProductsCurrRow.push(row)
            }
        },
        table2Click2(row) {
            if (!row.value) {
                this.entryProductsCurrRow.splice(this.entryProductsCurrRow.indexOf(row.item), 1)
            }
            else {
                this.table2Click(row.item)
            }
        },
        choose() {
            if (this.entryProductsCurrRow.length === 0) return
            this.$emit('purchaseOrderChoose', this.entryProductsCurrRow)
        }
    }
}
</script>

<style scoped>

</style>
