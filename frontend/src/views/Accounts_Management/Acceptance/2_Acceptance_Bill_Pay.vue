<template>
    <!--    <p>账目管理</p>-->
    <!--    <p>付承兑汇票</p>-->
    <v-card>
        <v-card-title class="d-flex">
            付承兑汇票
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/accounts_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>

            <AcceptanceBillComponent :isInbound="false"
                                     :paramForm="form"
                                     @reset="handleReset">
            </AcceptanceBillComponent>

            <v-divider class="my-2"></v-divider>

            <AcceptanceQueryComponent prefix="承付"
                                          @entryClick="handleTableClick">
            </AcceptanceQueryComponent>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Acceptance_Bill_Pay",
    components: {
        AcceptanceBillComponent: () => import('~/components/AccountsManagementComponents/AcceptanceBillComponent'),
        AcceptanceQueryComponent: () => import('~/components/AccountsManagementComponents/AcceptanceQueryComponent')
    },
    beforeMount() {
        Object.assign(this.emptyForm, this.form)
    },
    data() {
        return {
            mdiArrowLeft,

            form: {
                acceptanceEntrySerial: '',
                partnerCompanyID: -1, companyAbbreviatedName: '',
                entryDate: '',
                departmentID: -1, departmentName: '',
                source: '',
                bankAccountID: -1, bankAccountName: '',
                sourceSerial: '',
                amount: '', number: '',
                issueDate: '', expirationDate: '',
                type: '', drawer: '',
                remark: '', classification: '', status: 0,
            },
            emptyForm: {}
        }
    },
    methods: {
        handleTableClick(val) {
            Object.assign(this.form, val)
        },
        handleReset() {
            Object.assign(this.form, this.emptyForm)
        }
    }
}
</script>

<style scoped>

</style>
