<template>
    <!--  <p>系统维护</p>-->
    <!--  <p>本公司信息</p>-->
    <v-card>
        <v-card-title class="d-flex">
            本公司信息
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/maintenance">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <BankAccountComponent></BankAccountComponent>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";
import BankAccountComponent from '~/components/Bank_Account'

export default {
    name: "Company_Info",
    components: {
        BankAccountComponent
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
