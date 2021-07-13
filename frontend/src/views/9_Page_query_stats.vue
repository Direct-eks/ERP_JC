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

        <v-col md="10"
               lg="9"
               xl="8">
            <router-view></router-view>
        </v-col>
    </v-row>
</template>

<script>
import nav from "~/utils/nav";

export default {
    name: "Page_query_stats",
    beforeMount() {
        let navItems = JSON.parse(JSON.stringify(nav.query_stats_nav))
        for (const item of navItems) {
            if (this.$store.getters.currentUserIsPermitted(item.requiredPermission)) {
                this.navItem.push(item)
            }
        }

        if (this.$route.path !== '/query_stats') {
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
        $route(to, from) {
            this.showStatus = to.path === '/query_stats';
        },
    },
}
</script>

<style scoped>

</style>
