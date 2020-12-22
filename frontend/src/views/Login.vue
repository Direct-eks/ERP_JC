<template>
    <!--    <el-card id="login">-->
    <!--        <div slot="header" class="clearfix">-->
    <!--            <span>登录认证</span>-->
    <!--        </div>-->
    <!--        <el-form-->
    <!--                :model="ruleForm"-->
    <!--                method="post"-->
    <!--                :rules="rules"-->
    <!--                ref="ruleForm"-->
    <!--                @keyup.enter.native="login('ruleForm')">-->
    <!--            <el-form-item label="用户名" prop="username">-->
    <!--                <el-input-->
    <!--                        v-model="ruleForm.username"-->
    <!--                        type="text"-->
    <!--                        id="username"-->
    <!--                        placeholder="请输入用户名"-->
    <!--                        clearable>-->
    <!--                </el-input>-->
    <!--            </el-form-item>-->
    <!--            <el-form-item label="密码" prop="password">-->
    <!--                <el-input-->
    <!--                        v-model="ruleForm.password"-->
    <!--                        type="password"-->
    <!--                        id="password"-->
    <!--                        placeholder="请输入密码"-->
    <!--                        show-password>-->
    <!--                </el-input>-->
    <!--            </el-form-item>-->
    <!--            <el-alert v-if="loginFailed"-->
    <!--                    :title="errorMessage"-->
    <!--                    type="error"-->
    <!--                    effect="dark"-->
    <!--                      show-icon center>-->
    <!--            </el-alert>-->
    <!--            <el-form-item>-->
    <!--                <el-button type="primary" style="margin-top: 20px"-->
    <!--                           v-loading.fullscreen.lock="loading"-->
    <!--                           @click="login('ruleForm')">-->
    <!--                    登录-->
    <!--                </el-button>-->
    <!--            </el-form-item>-->
    <!--        </el-form>-->
    <!--    </el-card>-->

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
                                <v-icon>mdi-code-tags</v-icon>
                            </v-btn>
                        </template>
                        <span>Source</span>
                    </v-tooltip>
                </v-toolbar>
                <v-card-text>
                    <v-form ref="form" @keyup.enter.native="login">
                        <v-text-field v-model="ruleForm.username"
                                      :rules="rules.username"
                                      required
                                      label="登录名"
                                      name="login"
                                      prepend-icon="mdi-account"
                                      type="text">
                        </v-text-field>

                        <v-text-field v-model="ruleForm.password"
                                      :rules="rules.password"
                                      required
                                      id="password"
                                      label="密码"
                                      name="password"
                                      prepend-icon="mdi-lock"
                                      type="password">
                        </v-text-field>
                    </v-form>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" @click="login">Login</v-btn>
                </v-card-actions>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
    export default {
        name: 'Login',
        data() {
            return {
                ruleForm: {
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
                loginFailed: false,
                errorMessage: '',
            }
        },
        methods: {
            login: function (formName) {
                if (this.$refs.form.validate()) {
                    this.loading = true;
                    this.$postRequest(this.$api.userAuthentication, {
                        username: this.ruleForm.username,
                        password: this.ruleForm.password
                    }).then((res) => {
                        console.log('received', res.data)

                        // use vuex to store user information
                        sessionStorage.setItem('userName', this.ruleForm.username)
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
                        //todo
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
