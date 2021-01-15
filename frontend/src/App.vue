<template>
    <v-app>
        <v-navigation-drawer
                app
                v-model="navDrawer"
                :clipped="$vuetify.breakpoint.mdAndUp">
            <v-list dense nav>
                <v-list-item
                        v-for="(item, i) in nav"
                        :key="i"
                        :to="item.url"
                        link
                        @click.stop="navDrawer = !navDrawer">
                    <v-list-item-content>
                        <v-list-item-title>
                            {{ item.name }}
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar app dense :clipped-left="$vuetify.breakpoint.mdAndUp">

            <v-app-bar-nav-icon @click.stop="navDrawer = !navDrawer"></v-app-bar-nav-icon>

            <v-toolbar-title>精诚轴承</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon to="/home">
                <v-icon>{{mdiHomePath}}</v-icon>
            </v-btn>

            <v-btn icon>
                <v-icon @click="logout">{{ mdiLogoutPath }}</v-icon>
            </v-btn>

        </v-app-bar>

        <v-main>

            <v-container fluid>
                <router-view></router-view>
            </v-container>

            <SnackMessage></SnackMessage>
        </v-main>

    </v-app>
</template>

<script>
import { mdiLogout, mdiHome } from '@mdi/js'
import nav from "~/utils/nav";
import SnackMessage from "~/components/SnackMessage";

// eslint-disable-next-line no-extend-native,func-names
Date.prototype.format = function (fmt) {
    const o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        S: this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (`${this.getFullYear()}`).substr(4 - RegExp.$1.length));
    }
    for (const k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : ((`00${o[k]}`).substr((`${o[k]}`).length)));
        }
    }
    return fmt;
}

export default {
    data() {
        return {
            mdiHomePath: mdiHome,
            mdiLogoutPath: mdiLogout,

            nav: nav.items,
            navDrawer: null
        }
    },
    components: {
        SnackMessage
    },
    methods: {
        logout() {
            if (this.$store.getters.currentUser === ''
                    || !sessionStorage.getItem('isAuthenticated')
                    || sessionStorage.getItem('isAuthenticated') === 'false') {
                this.$store.commit('setSnackbar', {
                    message: '未登录，不能登出', color: 'error'
                })
                return
            }
            this.$postRequest(this.$api.userLogout, {}).then(() => {
                this.$store.commit('modifyCurrentUser', null)
                this.$router.replace('/login').then(() => {}).catch(() => {})
            }).catch(error => this.$ajaxErrorHandler(error))
        }
    }
}
</script>

<style scoped>

</style>
