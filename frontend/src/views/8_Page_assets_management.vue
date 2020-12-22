<template>
    <v-row justify="center" dense>
        <v-col md="auto">
            <v-card>
                <v-list>
                    <v-list-item-group v-model="active" color="primary">
                        <v-list-item v-for="(item, i) in navItem"
                                     :key="i"
                                     :to="item.url">
                            <v-list-item-content>{{item.name}}</v-list-item-content>
                        </v-list-item>
                    </v-list-item-group>
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
        name: "Page_assets_management",
        data() {
            return {
                navItem: nav.assets_management_nav,
                active: 0
            }
        }
    }
</script>

<style scoped>

</style>
