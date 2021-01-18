<template>
    <v-row justify="center" dense>
        <v-col cols="auto"
               v-show="showStatus">
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

        <v-col cols="12"
               md="11"
               lg="10">
            <router-view></router-view>
        </v-col>
    </v-row>
</template>

<script>
import nav from "~/utils/nav";

export default {
    name: "Page_stock_management",
    beforeMount() {
        // const userPermissions = this.$store.getters.currentUserPermissions
        let navItems = JSON.parse(JSON.stringify(nav.stock_management_nav))
        // let itemsToBeRemoved = []
        // for (const item of navItems) {
        //     if (!userPermissions.includes(item.requiredPermission)) {
        //         itemsToBeRemoved.push(item)
        //     }
        // }
        // itemsToBeRemoved.forEach(item => {
        //     navItems.splice(navItems.indexOf(item), 1)
        // })
        this.navItem = navItems
    },
    data() {
        return {
            navItem: [],
            showStatus: true,
        }
    },
    watch: {
        $route(to, from) {
            this.showStatus = to.path === '/stock_management';
        },
    },
}
</script>

<style scoped>

</style>
