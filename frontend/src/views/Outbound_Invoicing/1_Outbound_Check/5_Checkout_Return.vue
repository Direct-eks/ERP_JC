<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>出库结账后退货</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>出库结账后退货</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_invoicing">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>

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
                    mode="return"
                    :isInbound="false">
                </CheckoutComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Out_Check_Inv_Return",
    components: {
        QueryDisplayComponent: () => import('~/components/InvoiceComponents/CheckoutQueryDisplayComponent'),
        CheckoutComponent: () => import('~/components/InvoiceComponents/CheckoutComponent'),
    },
    data() {
        return {
            mdiArrowLeft,
            tab: null,
            currentTableRow: null,

            form: {
                checkoutEntrySerial: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '',
                companyFullName: '', companyRemark: '',
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
                inboundCheckoutProducts: [],
                outboundCheckoutProducts: [],
                invoiceEntry: null
            },
            originalForm: {}
        }
    },
    methods: {
        handleTabChange(val) {
            if (val === 0) {
                this.currentTableRow = null
                this.form = Object.assign(this.form, this.originalForm)
            }
        },
        tableClickAction(val) {
            this.currentTableRow = val
            this.form = Object.assign(this.form, this.currentTableRow)
            this.form.paymentMethod = ''
            this.form.paymentNumber = ''
            this.form.paymentAmount = '0'
            this.form.bankAccountID = -1
            this.form.bankAccountName = ''
            this.form.totalAmount = '0'
            this.form.isRounded = 0
            this.form.roundedAmount = '0'
            this.form.debt = '0'
            this.form.serviceFee = '0'
            this.form.remark = ''
            this.form.departmentID = -1
            this.form.departmentName = ''
            this.form.invoiceEntry = null
        }
    }
}
</script>

<style scoped>

</style>
