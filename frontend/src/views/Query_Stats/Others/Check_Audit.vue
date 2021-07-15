<template>
    <!--    <p>查询统计</p>-->
    <!--    <p>结账单审核</p>-->
    <v-card>
        <v-toolbar flat>
            <v-toolbar-title>结账单审核</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/query_stats">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>

            <template v-slot:extension>
                <v-tabs v-model="tab">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="audit">审核</v-tab>
                    <v-tab key="query">查询</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>

        <v-tabs-items v-model="tab">

            <v-tab-item key="audit">
                <v-container>
                    <v-row>
                        <v-spacer></v-spacer>
                        <v-col cols="auto">
                            <v-menu :close-on-content-click="true"
                                    :nudge-right="40"
                                    transition="scale-transition"
                                    offset-y>
                                <template v-slot:activator="{on}">
                                    <v-text-field v-model="month"
                                                  v-on="on"
                                                  label="月份"
                                                  hide-details="auto"
                                                  outlined dense
                                                  readonly
                                                  style="width: 110px">
                                    </v-text-field>
                                </template>
                                <v-date-picker v-model="month"
                                               no-title type="month"
                                               :max="allowedMaxMonth"
                                               locale="zh-cn">
                                </v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-col cols="auto">
                            <v-btn color="primary" @click="search">查询</v-btn>
                        </v-col>
                        <v-spacer></v-spacer>
                        <v-col cols="auto">
                            <v-dialog v-model="confirmDialog" max-width="300px">
                                <template v-slot:activator="{ on }">
                                    <v-btn color="success" v-on="on">审核</v-btn>
                                </template>
                                <v-card>
                                    <v-card-title>确认审核？</v-card-title>
                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="primary" @click="confirmDialog = false">取消</v-btn>
                                        <v-btn color="primary" @click="audit">确认</v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-data-table :headers="tableHeaders"
                                      :items="tableData"
                                      item-key="entryID"
                                      height="75vh"
                                      calculate-widths
                                      disable-sort
                                      fixed-header
                                      disable-pagination
                                      dense
                                      locale="zh-cn">
                        </v-data-table>
                    </v-row>
                </v-container>
            </v-tab-item>

            <v-tab-item key="query">

            </v-tab-item>

        </v-tabs-items>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "In_Check_Audit",
    data() {
        return {
            mdiArrowLeft,
            tab: null,

            month: new Date().format('yyyy-MM'),
            allowedMaxMonth: new Date().format('yyyy-MM'),

            confirmDialog: false,

            tableHeaders: [
                { text: '单号', value: 'entryID', width: '140px' },
                { text: '供应商', value: '', width: '90px' },
                { text: '结账类型', value: '', width: '90px' },
                { text: '代号', value: '', width: '180px' },
                { text: '厂牌', value: '', width: '65px' },
                { text: '数量', value: '', width: '90px' },
                { text: '单位', value: '', width: '60px' },
                { text: '无税单价', value: '', width: '90px' },
                { text: '含税单价', value: '', width: '90px' },
                { text: '含税金额', value: '', width: '90px' },
                { text: '服务费', value: '', width: '90px' },
                { text: '付款单号', value: '', width: '90px' },
                { text: '方式', value: '', width: '90px' },
                { text: '付款金额', value: '', width: '90px' },
            ],
            tableData: [],
        }
    },
    methods: {
        search() {
            this.$getRequest(this.$api.getAuditEntries + encodeURI(this.month)).then(data => {
                this.tableData = data
            }).catch(() => {})
        },
        audit() {
            this.$postRequest(this.$api.auditEntries, {}, {
                month: this.month
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '审核成功', color: 'success'
                })
                this.$router.replace('/stockManagement')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
