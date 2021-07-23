<template>
    <v-row justify="center" dense>
        <v-col md="auto"
               v-show="showStatus">
            <v-card>
                <v-list>
                    <v-list-item-group v-model="active">
                        <v-list-item v-for="(item, i) in navItem"
                                     :key="i"
                                     :to="item.url">
                            <v-sheet :color="item.color" min-height="100%" min-width="100%">
                                {{item.name}}
                            </v-sheet>
                        </v-list-item>
                    </v-list-item-group>
                </v-list>
            </v-card>
        </v-col>

        <v-col cols="12"
               md="10"
               lg="9"
               xl="8">
            <router-view></router-view>
        </v-col>
    </v-row>
</template>

<script>
import nav from "~/utils/nav";

export default {
    name: "Page_maintenance",
    beforeMount() {
        let navItems = JSON.parse(JSON.stringify(nav.maintenance_nav))
        let itemsToBeRemoved = []
        for (const item of navItems) {
            if (!this.$store.getters.currentUserIsPermitted(item.requiredPermission)) {
                itemsToBeRemoved.push(item)
            }
        }
        itemsToBeRemoved.forEach(item => {
            navItems.splice(navItems.indexOf(item), 1)
        })
        this.navItem = navItems

        if (this.$route.path !== '/maintenance') {
            this.showStatus = false
        }
    },
    data() {
        return {
            showStatus: true,
            navItem: [],
            active: 0
        }
    },
    watch: {
        $route(to, from) {
            this.showStatus = to.path === '/maintenance';
        },
    },
}
</script>

<style scoped>

</style>
