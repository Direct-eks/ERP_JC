<template>
    <v-app>
        <v-navigation-drawer
                app
                v-model="navDrawer"
                stateless
                disable-resize-watcher>
            <v-list dense nav>
                <v-list-item
                        v-for="(item, i) in nav"
                        :key="i"
                        :to="item.url"
                        @click.stop="navDrawer = !navDrawer">
                    <v-list-item-content>
                        <v-list-item-title>
                            {{ item.name }}
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>

        <v-app-bar app
                   dense
                   flat
                   color="white">

            <v-app-bar-nav-icon @click.stop="navDrawer = !navDrawer"></v-app-bar-nav-icon>

            <v-toolbar-title>精诚轴承</v-toolbar-title>
            <v-spacer></v-spacer>

            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon to="/home"
                           v-bind="attrs"
                           v-on="on">
                        <v-icon>{{ mdiHome }}</v-icon>
                    </v-btn>
                </template>
                <span>主页</span>
            </v-tooltip>

            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon
                           v-bind="attrs"
                           v-on="on">
                        <v-icon @click="logout">{{ mdiLogout }}</v-icon>
                    </v-btn>
                </template>
                <span>登出</span>
            </v-tooltip>

            <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon
                           v-bind="attrs"
                           v-on="on">
                        <v-icon @click="shutdown">{{ mdiPower }}</v-icon>
                    </v-btn>
                </template>
                <span>关闭后台服务器</span>
            </v-tooltip>

        </v-app-bar>

        <v-main class="grey lighten-3">

            <v-container fluid>
                <router-view></router-view>
            </v-container>

            <SnackMessage></SnackMessage>
        </v-main>

        <v-bottom-navigation v-show="navBottomShow" color="primary">
            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon to="/inbound_management/entry_in"
                           v-bind="attrs"
                           v-on="on">
                        <v-icon>{{ mdiArchiveArrowDown }}</v-icon>
                    </v-btn>
                </template>
                <span>入库单录入</span>
            </v-tooltip>

            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon to="/outbound_management/entry_out"
                           v-bind="attrs"
                           v-on="on">
                        <v-icon>{{ mdiArchiveArrowUp }}</v-icon>
                    </v-btn>
                </template>
                <span>出库单录入</span>
            </v-tooltip>

            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon to="/inbound_invoicing/inbound_checkout_entry"
                           v-bind="attrs"
                           v-on="on">
                        <v-icon>{{ mdiArchiveArrowDownOutline }}</v-icon>
                    </v-btn>
                </template>
                <span>入库结账单录入</span>
            </v-tooltip>

            <v-tooltip top>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn icon to="/outbound_invoicing/outbound_check_entry"
                           v-bind="attrs"
                           v-on="on">
                        <v-icon>{{ mdiArchiveArrowUpOutline }}</v-icon>
                    </v-btn>
                </template>
                <span>出库结账单录入</span>
            </v-tooltip>
        </v-bottom-navigation>

    </v-app>
</template>

<script>
import { mdiLogout, mdiHome, mdiPower,
        mdiArchiveArrowDown, mdiArchiveArrowDownOutline,
        mdiArchiveArrowUp, mdiArchiveArrowUpOutline  } from '@mdi/js'
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
    components: {
        SnackMessage
    },
    data() {
        return {
            mdiHome,
            mdiLogout,
            mdiPower,
            mdiArchiveArrowUp,
            mdiArchiveArrowDown,
            mdiArchiveArrowDownOutline,
            mdiArchiveArrowUpOutline,

            nav: nav.items,
            navDrawer: false,
            navBottomShow: true
        }
    },
    watch: {
        $route(to, from) {
            this.navBottomShow = to.path === '/home'
        },
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
                this.$store.commit('modifyCurrentUserRole', null)
                this.$store.commit('modifyCurrentUserPermissions', [])
                sessionStorage.clear()
                this.$router.replace('/login')
            }).catch(() => {})
        },
        shutdown() {
            this.$postRequest('/actuator/shutdown').then(() => {
                console.log('shutdown success')
            }).catch(() => {})
        },
    }
}
</script>

<style scoped>

</style>
