<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>报价单修改</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>报价单修改</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_management">
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

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Quota_Modify",
    components: {
        OutboundQueryDisplayComponent: () => import('~/components/OutboundEntryComponents/QueryDisplayComponent'),
        OutboundEntryDisplayAndModifyComponent: () => import('~/components/OutboundEntryComponents/EntryDisplayAndModifyComponent'),
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
                quoteEntryID: '',
                creationDate: '',
                totalAmount: '',
                invoiceType: '', taxRate: 0,
                drawer: '',
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '',
                companyFullName: '', companyRemark: '',
                remark: '',
                quoteProducts: []
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
        }
    }
}
</script>

<style scoped>

</style>
