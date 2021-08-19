<template>
    <v-row justify="center" dense>
        <v-col cols="auto" v-show="showStatus">
            <v-card>
                <v-list>
                    <template v-for="(item, i) in navItem">
                        <v-list-group v-if="item.children"
                                      :key="i"
                                      no-action>
                            <template v-slot:activator>
                                <v-list-item-content>{{item.name}}</v-list-item-content>
                            </template>
                            <v-list-item v-for="(subItem, j) in item.children"
                                         :key="j"
                                         :to="subItem.url">
                                <v-list-item-content>{{subItem.name}}</v-list-item-content>
                            </v-list-item>
                        </v-list-group>
                        <v-list-item v-else
                                     :key="i"
                                     :to="item.url">
                            <v-list-item-content>{{item.name}}</v-list-item-content>
                        </v-list-item>
                    </template>
                </v-list>
            </v-card>
        </v-col>

        <v-col md="12"
               lg="11"
               xl="10">
            <router-view></router-view>
        </v-col>
    </v-row>
</template>

<script>
import nav from "~/utils/nav";

export default {
    name: "Page_accounts_management",
    beforeMount() {
        let navItems = JSON.parse(JSON.stringify(nav.accounts_management_nav))
        for (const item of navItems) {
            let itemsToBeRemoved = []
            for (const subItem of item.children) {
                if (!this.$store.getters.currentUserIsPermitted(subItem.requiredPermission)) {
                    itemsToBeRemoved.push(subItem)
                }
            }
            itemsToBeRemoved.forEach(subItem => {
                item.children.splice(item.children.indexOf(subItem), 1)
            })
        }
        this.navItem = navItems

        if (this.$route.path !== '/accounts_management') {
            this.showStatus = false
        }
    },
    data() {
        return {
            navItem: [],
            showStatus: true,
        }
    },
    watch: {
        $route(to, _) {
            this.showStatus = to.path === '/accounts_management';
        },
    },
}
</script>

<style scoped>

</style>
