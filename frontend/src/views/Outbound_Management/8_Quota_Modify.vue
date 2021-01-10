<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>报价单修改</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>报价单修改</v-toolbar-title>

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
                <OutboundQueryDisplayComponent
                    displayMode="quota"
                    @tableClick="tableClickAction">
                </OutboundQueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <OutboundEntryDisplayAndModifyComponent
                    :form="form"
                    displayMode="quotaModify">
                </OutboundEntryDisplayAndModifyComponent>
            </v-tab-item>

        </v-tabs-items>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "8_Quota_Modify",
    components: {
        OutboundQueryDisplayComponent: () => import('../../components/OutboundEntryComponents/QueryDisplayComponent'),
        OutboundEntryDisplayAndModifyComponent: () => import('../../components/OutboundEntryComponents/EntryDisplayAndModifyComponent'),
        SnackMessage,
    },
    data() {
        return {
            tab: null,
            currentTableRow: null,

            form: {
                shipmentDate: '',
                creationDate: '',
                totalAmount: 0.0, invoiceType: '',
                drawer: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                remark: '',
                quotaProducts: []
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
            this.currentTableRow.outboundProducts.forEach(item => {
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
