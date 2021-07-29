<template>
    <!--    <p>账目管理</p>-->
    <!--    <p>收承兑汇票</p>-->
    <v-card>
        <v-card-title class="d-flex">
            收承兑汇票
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/accounts_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>

            <AcceptanceBillComponent :isInbound="true"
                                     :paramForm="{}">
            </AcceptanceBillComponent>

            <v-divider class="my-2"></v-divider>

            <AcceptanceBillQueryComponent>
            </AcceptanceBillQueryComponent>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Acceptance_Bill_Recv",
    components: {
        AcceptanceBillComponent: () => import(/* webpackChunkName: "AcceptanceBillComponent" */
            '~/components/AccountsManagementComponents/AcceptanceBillComponent'),
        AcceptanceBillQueryComponent: () => import(/* webpackChunkName: "AcceptanceBillQueryComponent" */
            '~/components/AccountsManagementComponents/AcceptanceBillQueryComponent')
    },
    data() {
        return {
            mdiArrowLeft,
        }
    }
}
</script>

<style scoped>

</style>
