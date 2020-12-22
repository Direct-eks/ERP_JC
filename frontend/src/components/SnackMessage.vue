<template>
    <v-snackbar
            top
            v-model="visible"
            :timeout="timeout"
            :color="color">

        {{ message }}
        <template v-slot:action="{ attrs }">
            <v-btn icon
                   v-bind="attrs"
                   @click="visible = false">
                <v-icon>mdi-close</v-icon>
            </v-btn>
        </template>
    </v-snackbar>
</template>

<script>
    export default {
        data() {
            return {
                visible: false,
                timeout: 1500,
                message: '',
                color: ''
            }
        },
        created() {
            this.$store.watch(state => state.snackbar.message, () => {
                let msg = this.$store.state.snackbar.message
                if (msg !== '') {
                    this.message = msg
                    this.color = this.$store.state.snackbar.color
                    this.visible = true
                    this.$store.commit('setSnackbar', {
                        message: '', color: ''
                    })
                }
            })
        }
    }
</script>

<style scoped>

</style>
