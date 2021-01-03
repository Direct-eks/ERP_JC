<template>
<!--    <p>入库管理</p>-->
<!--    <p>采购订单查询</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>采购订单查询</v-toolbar-title>

            <template v-slot:extension>
                <v-tabs v-model="tab" @change="handleTabChange">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="browse">浏览</v-tab>
                    <v-tab key="detail" :disabled="currentTableRow === null">详细情况</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>

        <v-tabs-items v-model="tab">

            <v-tab-item key="browse">
                <InboundQueryDisplayComponent
                        @tableClick="tableClickAction"
                        displayMode="purchaseOrder">
                </InboundQueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <InboundEntryDisplayAndModifyComponent
                    :form="form"
                    displayMode="purchaseOrderDisplay">
                </InboundEntryDisplayAndModifyComponent>
            </v-tab-item>

        </v-tabs-items>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Purchase_Order_Query",
    components: {
        InboundQueryDisplayComponent: () => import('../../components/InboundQueryDisplayComponent'),
        InboundEntryDisplayAndModifyComponent: () => import('../../components/InboundEntryDisplayAndModifyComponent'),
        SnackMessage,
    },
    data() {
        return {
            tab: null,
            currentTableRow: null,

            form: {
                entryDate: '',
                creationDate: '',
                totalCost: 0.0, invoiceType: '',
                drawer: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: '',
                executionStatus: '',
                shippingCost: 0, shippingCostType: '',
                shippingQuantity: 0, shippingNumber: '',
                shippingMethodID: -1, relevantCompanyName: '',
                inboundProducts: [],
                purchaseOrderProducts: []
            }
        }
    },
    methods: {
        handleTabChange(val) {
            if (val === 0) {
                this.currentTableRow = null
            }
        },
        tableClickAction(val) {
            this.currentTableRow = val
            //create missing fields and calculate values
            this.currentTableRow.purchaseOrderProducts.forEach(item => {
                item['totalWithoutTax'] = (item.quantity * item.unitPriceWithoutTax).toFixed(2)
                item['totalTax'] = (item.quantity * item.unitPriceWithTax - item.totalWithoutTax).toFixed(2)
            })
            this.form = Object.assign(this.form, this.currentTableRow)
        }
    }
}
</script>

<style scoped>

</style>
