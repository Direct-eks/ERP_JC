<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>销售订单录入</p>-->
    <v-card outlined>
        <v-card-title>
            销售订单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/outbound_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <OutboundEntryCreateComponent editMode="salesOrder">
        </OutboundEntryCreateComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Sales_Order_Entry",
    components: {
        OutboundEntryCreateComponent: () => import('~/components/OutboundEntryComponents/EntryCreateComponent'),
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,
        }
    }
}
</script>

<style scoped>

</style>
