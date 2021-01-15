<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>收运费修改</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>收运费修改</v-toolbar-title>
            <v-card-subtitle>出库代垫运费修改</v-card-subtitle>

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
                <QueryDisplayComponent
                    displayMode="modify"
                    :isInbound="false"
                    @tableClick="tableClickAction">
                </QueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <ShippingCostComponent
                    :paramForm="form"
                    mode="modify"
                    :isInbound="false">
                </ShippingCostComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
export default {
    name: "Out_Cost_Modify",
    components: {
        QueryDisplayComponent: () => import('~/components/InvoiceComponents/ShippingCostQueryDisplayComponent'),
        ShippingCostComponent: () => import("~/components/InvoiceComponents/ShippingCostComponent"),
    },
    data() {
        return {
            tab: null,
            currentTableRow: null,

            form: {
                shippingCostEntrySerial: null,
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyFullName: '', companyPhone: '',
                isTaxDeduction: -1, invoiceNumber: '',
                totalAmount: 0.0, invoiceAmount: '',
                shippingCostType: '',
                remark: '', drawer: '',
                creationDate: '',
                checkoutDate: '',
                inOrOut: '',
                invoiceDate: '',
                isModified: 0,

                inboundEntries: [],
                outboundEntries: [],
            },
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
            this.form = Object.assign(this.form, this.currentTableRow)
        }
    }
}
</script>

<style scoped>

</style>
