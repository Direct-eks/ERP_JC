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
                                     :paramForm="{}">
            </AcceptanceBillComponent>

            <AcceptanceBillQueryComponent>
            </AcceptanceBillQueryComponent>

        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Acceptance_Bill_Pay",
    components: {
        AcceptanceBillComponent: () => import('~/components/AccountsManagementComponents/AcceptanceBillComponent'),
        AcceptanceBillQueryComponent: () => import('~/components/AccountsManagementComponents/AcceptanceBillQueryComponent')
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
