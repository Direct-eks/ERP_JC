<template>
    <!--    <p>账目管理</p>-->
    <!--    <p>银行存取款</p>-->
    <v-card>
        <v-card-title class="d-flex">
            银行存取款
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/accounts_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>

            <FeeComponent :paramForm="form"
                          mode="bank"
                          @reset="handleReset">
            </FeeComponent>

            <v-divider class="my-2"></v-divider>

            <FeeQueryComponent prefix="bank"
                               @entryClick="handleTableClick">
            </FeeQueryComponent>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Bank_Draw_And_Deposit",
    components: {
        FeeComponent: () => import(/* webpackChunkName: "FeesComponent" */
            '~/components/AccountsManagementComponents/FeesComponent'),
        FeeQueryComponent: () => import(/* webpackChunkName: "FeeQueryComponent" */
            '~/components/AccountsManagementComponents/FeeQueryComponent')
    },
    beforeMount() {
        Object.assign(this.emptyForm, this.form)
    },
    data() {
        return {
            mdiArrowLeft,

            form: {
                feeEntryID: '',
                entryDate: '', creationDate: '',
                drawer: '',
                departmentID: -1, departmentName: '',
                sourceAccountID: -1, sourceAccountName: '',
                destinationAccountID: -1, destinationAccountName: '',
                amount: '', number: '', remark: '',
                isBookKeeping: 0, isVerified: 0,
                feeDetails: [],
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
