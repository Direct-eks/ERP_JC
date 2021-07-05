<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单开票查询</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>入库结账单开票查询</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_invoicing">
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
                    displayMode="query"
                    :isInbound="true"
                    @tableClick="tableClickAction">
                </QueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <InvoiceComponent
                    :paramForm="form"
                    mode="display"
                    :isInbound="true">
                </InvoiceComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: 'In_Check_Inv_Query',
    components: {
        QueryDisplayComponent: () => import(
            /* webpackChunkName: "InboundInvoiceQueryDisplayComponent" */
            '~/components/InvoiceComponents/InvoiceQueryDisplayComponent'
        ),
        InvoiceComponent: () => import('~/components/InvoiceComponents/InvoiceComponent'),
    },
    data() {
        return {
            mdiArrowLeft,
            tab: null,
            currentTableRow: null,

            form: {
                invoiceEntrySerial: null,
                partnerCompanyID: -1, companyFullName: '',
                invoiceType: '', invoiceNumber: '',
                totalAmount: '', invoiceAmount: '',
                invoiceIndication: '', isFollowUpIndication: 0,
                remark: '', drawer: this.$store.getters.currentUser,
                creationDate: '',
                checkoutDate: '',
                inOrOut: '',
                invoiceDate: '',
                invoiceNumberDate: '',
                isModified: 0,

                inboundInvoiceProducts: [],
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
            this.currentTableRow.inboundInvoiceProducts.forEach(item => {
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
