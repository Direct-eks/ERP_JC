<template>
<!--    <p>入库管理</p>-->
<!--    <p>入库单查询</p>-->
    <v-card outlined>
        <v-toolbar flat>
            <v-toolbar-title>入库单查询</v-toolbar-title>

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
                <InboundQueryDisplayComponent
                        displayMode="query"
                        @tableClick="tableClickAction">
                </InboundQueryDisplayComponent>
            </v-tab-item>

            <v-tab-item key="详细情况">
                <InboundEntryDisplayComponent
                        :chosenEntryForDetail="currentTableRow"
                        editMode="query">
                </InboundEntryDisplayComponent>
            </v-tab-item>

        </v-tabs-items>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
    import SnackMessage from "~/components/SnackMessage";

    export default {
        name: "Query_In",
        components: {
            InboundEntryDisplayComponent: () => import('../../components/InboundEntryDisplayComponent'),
            InboundQueryDisplayComponent: () => import('../../components/InboundQueryDisplayComponent'),
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
