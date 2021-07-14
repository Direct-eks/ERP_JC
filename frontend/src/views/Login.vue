<template>
    <v-row align="center" justify="center">
        <v-col cols="auto">
            <v-card class="elevation-12" width="300px">
                <v-toolbar color="primary" dark flat>
                    <v-toolbar-title>登录认证</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon @click="refresh(true)">
                        <v-icon>{{ mdiRefresh }}</v-icon>
                    </v-btn>
                </v-toolbar>

                <v-card-text>
                    <v-form ref="form" @keyup.enter.native="login">
                        <v-autocomplete v-model="form.username"
                                        :rules="rules.username"
                                        :items="userList"
                                        hide-details="auto"
                                        auto-select-first
                                        :prepend-icon="mdiAccount"
                                        dense
                                        label="登录名">
                        </v-autocomplete>
                        <v-text-field v-model="form.password"
                                      :rules="rules.password"
                                      id="password"
                                      label="密码"
                                      clearable
                                      name="password"
                                      :prepend-icon="mdiLock"
                                      type="password">
                        </v-text-field>
                    </v-form>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" :loading="loading" @click="login">
                        登录
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
import {mdiAccount, mdiLock, mdiRefresh} from '@mdi/js'

export default {
    name: 'Login',
    beforeMount() {
        this.refresh(false)
    },
    data() {
        return {
            mdiAccount,
            mdiLock,
            mdiRefresh,

            userList: [],

            form: {
                username: '',
                password: ''
            },
            rules: {
                username: [v => !!v || '请填写用户名'],
                password: [v => !!v || '请填写密码']
            },
            loading: false,
        }
    },
    methods: {
        refresh(showMsg) {
            this.$getRequest(this.$api.userNameList).then(data => {
                if (showMsg) {
                    this.$store.commit('setSnackbar', {
                        message: '刷新成功', color: 'success'
                    })
                }
                this.userList = data
            }).catch(() => {})
        },
        login() {
            if (this.$refs.form.validate()) {
                this.loading = true;
                this.$postRequest(this.$api.userAuthentication, {
                    username: this.form.username,
                    password: this.form.password
                }).then((data) => {
                    this.$store.commit('setSnackbar', {
                        message: '登录成功', color: 'success'
                    })

                    // use vuex to store user information
                    this.$store.commit('modifyCurrentUser', {
                        username: data.username,
                        userRole: data.role,
                        userPermissions: data.permissions,
                        amount: data.permittedRoundingAmount,
                        sessionID: data.sessionID
                    })

                    this.username = ''
                    this.password = ''
                    this.loading = false;
                    this.$router.replace('/home')
                }).catch(() => {
                    this.loading = false
                })

                /*------ for debug only ------*/
                // sessionStorage.setItem('userName', this.ruleForm.username)
                // sessionStorage.setItem('userToken', 'token')
                // this.$store.dispatch('userLoginSuccess',
                //     {username: this.ruleForm.username, token: 'token'})
                // this.username = ''
                // this.password = ''
                // this.loading = false;
                // this.$router.push('/home')
            }
        }
    } // end method
}
</script>

<style scoped>

</style>
