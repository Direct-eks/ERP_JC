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

            <FeeComponent mode="bank"></FeeComponent>

            <v-divider class="my-2"></v-divider>

            <FeeQueryComponent prefix=""></FeeQueryComponent>

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
    data() {
        return {
            mdiArrowLeft,
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
