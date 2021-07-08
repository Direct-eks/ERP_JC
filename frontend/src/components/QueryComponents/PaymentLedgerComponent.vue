<template>
    <div>
        <v-row>
            <v-col cols="auto">
                <v-btn>查询</v-btn>
            </v-col>
            <v-col cols="auto">
                <v-btn>其他查询</v-btn>
            </v-col>
        </v-row>
        <v-divider class="my-2"></v-divider>
        <v-card outlined>
            <v-data-table></v-data-table>
        </v-card>
    </div>
</template>

<script>
export default {
    name: "PaymentLedgerComponent",
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        if (this.mode === 'in')
            this.inboundMode = true
        else if (this.mode === 'out')
            this.inboundMode = false

    },
    data() {
        return {
            inboundMode: true,

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
