<template>
<!--    <p>入库管理</p>-->
<!--    <p>入库单退货</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>入库单完善</v-toolbar-title>

            <template v-slot:extension>
                <v-tabs v-model="tab">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="browse">浏览</v-tab>
                    <v-tab key="detail" :disabled="currentTableRow === null">详细情况</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>

        <v-tabs-items v-model="tab">

            <v-tab-item key="browse">
                <InboundQueryDisplayComponent
                    displayMode="return"
                    @tableClick="tableClickAction">
                </InboundQueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
<!--                todo-->
            </v-tab-item>

        </v-tabs-items>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Return_In",
    components: {
        InboundQueryDisplayComponent: () => import('../../components/InboundEntryComponents/InboundQueryDisplayComponent'),
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
                shippingCost: 0, shippingCostType: '',
                shippingQuantity: 0, shippingNumber: '',
                shippingMethodID: -1, relevantCompanyName: '',
                inboundProducts: [],
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
            this.currentTableRow.inboundProducts.forEach(item => {
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
