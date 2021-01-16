<template>
    <div class="row">
        <p>Empty Start Page</p>
        <v-btn @click="logout">local logout</v-btn>
    </div>
</template>

<script>
export default {
    name: "Start",
    methods: {
        logout() {
            this.$store.commit('modifyCurrentUser', null)
            this.$store.commit('modifyCurrentUserRole', null)
            this.$store.commit('modifyCurrentPermissions', [])

            sessionStorage.clear()
            this.$router.replace('/login')
        }
    }
}
</script>

<style scoped>

</style>
