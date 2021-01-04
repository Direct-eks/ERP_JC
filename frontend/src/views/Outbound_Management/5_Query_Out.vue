<template>
    <!--  <p>出库管理</p>-->
    <!--  <p>出库单查询</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>出库单查询</v-toolbar-title>

            <template v-slot:extension>
                <v-tabs v-model="tab">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="浏览">浏览</v-tab>
                    <v-tab key="详细情况">详细情况</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>

        <v-tabs-items v-model="tab">

            <v-tab-item key="浏览">
                <OutboundQueryDisplayComponent
                    displayMode="query"
                    @tableClick="tableClickAction">
                </OutboundQueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="详细情况">
                <OutboundEntryDisplayComponent
                    :chosenEntryForDetail="currentTableRow"
                    editMode="query">
                </OutboundEntryDisplayComponent>
            </v-tab-item>

        </v-tabs-items>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
import SnackMessage from "~/components/SnackMessage";

export default {
    name: "Query_Out",
    components: {
        OutboundEntryDisplayComponent: () => import('../../components/OutboundEntryComponents/OutboundEntryDisplayComponent'),
        OutboundQueryDisplayComponent: () => import('../../components/OutboundEntryComponents/OutboundQueryDisplayComponent'),
        SnackMessage,
    },
    data() {
        return {
            tab: null,
            currentTableRow: null
        }
    },
    methods: {
        tableClickAction(val) {
            //todo query missing fields and products
            if (val.shippingMethod === -1) { // check if shipping method is null, if null, no need to query
                this.$postRequest(this.$api.entryProductByEntry, {
                    entryID: val.entryID
                }).then((res) => {
                    console.log('received', res.data)
                    val.products = res.data

                    this.currentTableRow = val
                })
            }
            else {
                this.$postRequest(this.$api.shippingInfoByEntry, {
                    shippingMethodID: val.shippingMethodID
                }).then((res) => {
                    console.log('received', res.data)
                    val.shippingMethod = res.data

                    this.$postRequest(this.$api.entryProductByEntry, {
                        entryID: val.entryID
                    }).then((res) => {
                        console.log('received', res.data)
                        val.products =  res.data

                        this.currentTableRow = val
                    })

                })
            }
        }
    }
}
</script>

<style scoped>

</style>
