<template>
    <v-card>
        <QueryConditions :queries.sync="queries"
                         @clear="clear">
        </QueryConditions>

    </v-card>
</template>

<script>
export default {
    name: "PaymentLedgerComponent",
    components: {
        QueryConditions: () => import('~/components/QueryComponents/QueryConditions')
    },
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

            queries: {
                companyID: -1,
                companyName: '',
            },
        }
    },
    methods: {
        clear() {

        }
    }
}
</script>

<style scoped>

</style>
