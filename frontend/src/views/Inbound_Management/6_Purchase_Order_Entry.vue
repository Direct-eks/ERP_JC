<template>
    <!--  <p>入库管理</p>-->
    <!--  <p>采购订单录入</p>-->
    <v-card outlined>
        <v-card-title>
            采购订单录入
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/inbound_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <InboundEntryCreateComponent editMode="purchaseOrder">
        </InboundEntryCreateComponent>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Purchase_Order_Entry",
    components: {
        InboundEntryCreateComponent: () => import('~/components/InboundEntryComponents/EntryCreateComponent'),
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
