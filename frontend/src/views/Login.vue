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
                        <v-text-field v-model="form.username"
                                      :rules="rules.username"
                                      label="登录名"
                                      clearable
                                      name="login"
                                      :prepend-icon="mdiAccountPath"
                                      type="text">
                        </v-text-field>

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

import sha256 from 'crypto-js/sha256';
import Hex from 'crypto-js/enc-hex'

export default {
    name: 'Login',
    data() {
        return {
            mdiCodeTagsPath: mdiCodeTags,
            mdiAccountPath: mdiAccount,
            mdiLockPath: mdiLock,

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
                console.log(sha256(this.form.password).toString(Hex))
                this.$postRequest(this.$api.userAuthentication, {
                    username: this.form.username,
                    password: sha256(this.form.password).toString(Hex),
                }).then((res) => {
                    console.log('received', res.data)

                    // use vuex to store user information
                    sessionStorage.setItem('userName', this.form.username)
                    sessionStorage.setItem('userToken', res.data)
                    sessionStorage.setItem('isAuthenticated', 'true')
                    // this.$store.dispatch('userLoginSuccess', res.data.username)

                    this.username = ''
                    this.password = ''
                    this.loading = false;
                    this.$router.replace('/home').then(t => {
                    }).catch(t => {
                    })
                }).catch((reject) => {
                    this.loading = false
                });

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
