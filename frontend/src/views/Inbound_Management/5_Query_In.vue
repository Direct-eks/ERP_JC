<template>
<!--    <p>入库管理</p>-->
<!--    <p>入库单查询</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>入库单查询</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_management">
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
                <InboundQueryDisplayComponent
                        displayMode="query"
                        @tableClick="tableClickAction">
                </InboundQueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <InboundEntryDisplayAndModifyComponent
                    :form="form"
                    displayMode="inboundEntryDisplay">
                </InboundEntryDisplayAndModifyComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Query_In",
    components: {
        InboundQueryDisplayComponent: () => import('~/components/InboundEntryComponents/QueryDisplayComponent'),
        InboundEntryDisplayAndModifyComponent: () => import('~/components/InboundEntryComponents/EntryDisplayAndModifyComponent'),
    },
    beforeMount() {
        this.originalForm = JSON.parse(JSON.stringify(this.form))
    },
    data() {
        return {
            mdiArrowLeft,
            tab: null,
            currentTableRow: null,

            form: {
                inboundEntryID: '',
                entryDate: '', creationDate: '',
                totalCost: '', invoiceType: '', taxRate: '',
                drawer: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: '',
                shippingCost: '', shippingCostType: '',
                shippingQuantity: 0, shippingNumber: '',
                shippingMethodID: -1, relevantCompanyName: '',
                inboundProducts: []
            },
            originalForm: {}
        }
    },
    methods: {
        handleTabChange(val) {
            if (val === 0) {
                this.currentTableRow = null
                this.form = Object.assign(this.form, this.originalForm) // reset form
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
