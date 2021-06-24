<template>
    <v-row justify="center" class="red" dense>
        <v-col cols="auto" v-show="showStatus">
            <v-card>
                <v-list expand>
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

        <v-col cols="12" md="11" lg="9" xl="8">
            <transition name="fade">
                <router-view></router-view>
            </transition>
        </v-col>
    </v-row>
</template>

<script>
import nav from "~/utils/nav";

export default {
    name: "Inbound_Management_Page",
    beforeMount() {
        let navItems = JSON.parse(JSON.stringify(nav.inbound_management_nav))
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

        if (this.$route.path !== '/inbound_management') {
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
            this.showStatus = to.path === '/inbound_management';
        },
    },
}
</script>

<style scoped>

</style>
