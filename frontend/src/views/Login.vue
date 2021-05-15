<template>
    <v-row align="center"
           justify="center">
        <v-col cols="12"
               sm="8"
               md="4">
            <v-card class="elevation-12">
                <v-toolbar color="primary"
                           dark
                           flat>
                    <v-toolbar-title>登录认证</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-tooltip bottom>
                        <template v-slot:activator="{ on }">
                            <v-btn icon
                                   large
                                   target="_blank"
                                   v-on="on">
                                <v-icon>{{mdiCodeTagsPath}}</v-icon>
                            </v-btn>
                        </template>
                        <span>Source</span>
                    </v-tooltip>
                </v-toolbar>

                <v-card-text>
                    <v-form ref="form" @keyup.enter.native="login">
                        <v-autocomplete v-model="form.username"
                                        :rules="rules.username"
                                        :items="userList"
                                        hide-details="auto"
                                        auto-select-first
                                        :prepend-icon="mdiAccountPath"
                                        dense
                                        label="登录名">
                        </v-autocomplete>

                        <v-text-field v-model="form.password"
                                      :rules="rules.password"
                                      id="password"
                                      label="密码"
                                      clearable
                                      name="password"
                                      :prepend-icon="mdiLockPath"
                                      type="password">
                        </v-text-field>
                    </v-form>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary"
                           :loading="loading"
                           @click="login">登录</v-btn>
                </v-card-actions>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
import {mdiCodeTags} from '@mdi/js'
import {mdiAccount} from '@mdi/js'
import {mdiLock} from '@mdi/js'

// import {sha256} from 'utility'

export default {
    name: 'Login',
    beforeMount() {
        this.$getRequest(this.$api.userNameList).then((data) => {
            console.log('received', data)
            this.userList = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiCodeTagsPath: mdiCodeTags,
            mdiAccountPath: mdiAccount,
            mdiLockPath: mdiLock,

            userList: [],

            form: {
                username: '',
                password: ''
            },
            rules: {
                username: [
                    v => !!v || '请填写用户名',
                ],
                password: [
                    v => !!v || '请填写密码',
                ]
            },
            loading: false,
        }
    },
    methods: {
        login() {
            if (this.$refs.form.validate()) {
                this.loading = true;
                // console.log(sha256(this.form.password))
                this.$postRequest(this.$api.userAuthentication, {
                    username: this.form.username,
                    // password: sha256(this.form.password),
                    password: this.form.password
                }).then((data) => {
                    console.log('received', data)
                    this.$store.commit('setSnackbar', {
                        message: '登录成功', color: 'success'
                    })

                    this.$store.commit('modifyCurrentUser', data.username)
                    this.$store.commit('modifyCurrentUserRole', data.role)
                    this.$store.commit('modifyCurrentUserPermissions', data.permissions)
                    // use vuex to store user information
                    sessionStorage.setItem('userName', data.username)
                    sessionStorage.setItem('userToken', data.sessionID)
                    sessionStorage.setItem('userRole', data.role)
                    sessionStorage.setItem('userPermissions', JSON.stringify(data.permissions))
                    sessionStorage.setItem('isAuthenticated', 'true')

                    this.username = ''
                    this.password = ''
                    this.loading = false;
                    this.$router.replace('/home').then(() => {}).catch(() => {})
                }).catch(() => {this.loading = false})

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
