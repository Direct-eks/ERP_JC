<template>
    <v-card>

    </v-card>
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
        if (this.mode === 'supplier')
            this.customerMode = false
        else if (this.mode === 'customer')
            this.customerMode = true

    },
    data() {
        return {
            customerMode: true,

        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
