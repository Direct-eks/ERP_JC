<template>
    <!--  <p>出库结账管理</p>-->
    <!--  <p>出库结账单修改</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>出库结账单修改</v-toolbar-title>

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
                <CheckoutComponent
                    :paramForm="form"
                    mode="modify"
                    :isInbound="false">
                </CheckoutComponent>
            </v-tab-item>

        </v-tabs-items>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
export default {
    name: "Out_Check_Modify",
    components: {
        QueryDisplayComponent: () => import('~/components/InvoiceComponents/CheckoutQueryDisplayComponent'),
        CheckoutComponent: () => import('~/components/InvoiceComponents/CheckoutComponent'),
    },
    data() {
        return {
            tab: null,
            currentTableRow: null,

            form: {
                checkoutEntrySerial: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                invoiceType: '',
                paymentMethod: '', paymentNumber: '', paymentAmount: '',
                bankAccountID: -1, bankAccountName: '',
                totalAmount: '', isRounded: 0, roundedAmount: '',
                debt: '', serviceFee: '',
                remark: '', drawer: '',
                creationDate: '',
                checkoutDate: '',
                moneyEntrySerial: '',
                returnSerial: '',
                departmentID: -1, departmentName: '',
                isVerified: 0,
                isModified: 0,
                outboundCheckoutProducts: [],
                invoiceEntry: null
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
            //create missing fields and calculate values
            this.currentTableRow.outboundCheckoutProducts.forEach(item => {
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
