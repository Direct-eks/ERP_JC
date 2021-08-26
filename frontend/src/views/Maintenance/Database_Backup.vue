<template>
    <!--  <p>系统维护</p>-->
    <!--  <p>数据库备份</p>-->
    <v-card>
        <v-card-title class="d-flex">
            数据库备份
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/maintenance">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row>
                <v-col cols="auto" class="d-flex">
                    <p>上次自动备份时间：</p>
                    <strong>{{ lastBackupTime }}</strong>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <p class="mt-2">自动备份：{{ autoBackupStatus === 1 ? '开' : '关' }}</p>
                </v-col>
                <v-switch v-model="autoBackupStatus"
                          :true-value="1"
                          :false-value="0"
                          @change="handleSwitchChange"
                          :loading="loading"
                          hide-details="auto"
                          dense>
                </v-switch>
            </v-row>
            <v-row>
                <v-btn class="ml-3" color="primary" @click="backup">
                    备份
                </v-btn>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Database_Backup",
    beforeMount() {
        this.$getRequest(this.$api.lastBackupTime).then(data => {
            this.lastBackupTime = data
        }).catch(() => {})
        this.$getRequest(this.$api.getAutoBackupStatus).then(data => {
            this.autoBackupStatus = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            lastBackupTime: '',
            autoBackupStatus: 0,
            loading: false,
        }
    },
    methods: {
        handleSwitchChange(val) {
            this.loading = true
            this.$postRequest(this.$api.updateAutoBackupStatus, null, {
                status: val
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '更改成功', color: 'success'
                })
                this.loading = false
            }).catch(() => {this.loading = false})
        },
        backup() {
            this.$postRequest(this.$api.backupDatabase).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '备份成功', color: 'success'
                })
                this.$router.replace('/maintenance')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
