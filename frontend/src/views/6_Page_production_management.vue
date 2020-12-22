<template>
    <v-row justify="center" dense>
        <v-col md="auto">
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
               md="10"
               lg="8">
            <router-view></router-view>
        </v-col>
    </v-row>
</template>

<script>
    import nav from "~/utils/nav";

    export default {
        name: "Page_production_management",
        data() {
            return {
                navItem: nav.production_management_nav
            }
        }
    }
</script>

<style scoped>

</style>
