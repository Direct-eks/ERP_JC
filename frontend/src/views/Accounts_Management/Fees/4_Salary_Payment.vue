<template>
    <!--    <p>账目管理</p>-->
    <!--    <p>支付工资</p>-->
    <v-card>
        <v-card-title class="d-flex">
            支付工资
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/accounts_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>

            <FeeComponent mode="salary"></FeeComponent>

            <v-divider class="my-2"></v-divider>

            <FeeQueryComponent prefix=""></FeeQueryComponent>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Salary_Payment",
    components: {
        FeeComponent: () => import('~/components/AccountsManagementComponents/FeesComponent'),
        FeeQueryComponent: () => import('~/components/AccountsManagementComponents/FeeQueryComponent')
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
