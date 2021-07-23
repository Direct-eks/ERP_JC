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
            <p>上次自动备份：</p>
            <strong>{{ lastBackupTime }}</strong>
            <v-btn color="primary" @click="backup">
                备份
            </v-btn>
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
    },
    data() {
        return {
            mdiArrowLeft,

            lastBackupTime: '',
        }
    },
    methods: {
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
