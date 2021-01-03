<template>
    <div class="row">
        <p>Empty Start Page</p>
        <v-btn>logout</v-btn>
    </div>
</template>

<script>
export default {
    name: "Start",
    methods: {
        logout() {
            this.$store.commit('modifyCurrentUser', null)
            this.$router.replace('/login').then(()=>{}).catch(()=>{})
        }
    }
}
</script>

<style scoped>

</style>
