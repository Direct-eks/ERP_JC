<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>收款单查询</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>收款单查询</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_invoicing">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
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
                <PaymentComponent
                    :paramForm="form"
                    mode="modify"
                    :isInbound="false">
                </PaymentComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Rec_Query",
    components: {
        QueryDisplayComponent: () => import('~/components/InvoiceComponents/PaymentQueryDisplayComponent'),
        PaymentComponent: () => import('~/components/InvoiceComponents/PaymentComponent'),
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,
            tab: null,
            currentTableRow: null,

            form: {
                moneyEntrySerial: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                paymentIndication: '',
                paymentMethod: '', paymentNumber: '', paymentAmount: '',
                bankAccountID: -1, bankAccountName: '',
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: '',
                paymentDate: '',
                checkoutSerial: '',
                departmentID: -1, departmentName: '',
                isModified: 0,
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
