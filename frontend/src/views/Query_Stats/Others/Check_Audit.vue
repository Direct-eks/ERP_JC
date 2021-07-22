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
                                    :disabled="auditTableData.length !== 0"
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
                            <v-btn color="primary" @click="search(true)">查询入库</v-btn>
                            <v-btn color="primary" @click="search(false)">查询出库</v-btn>
                            <v-btn color="warning" @click="clear">清空</v-btn>
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
                                        <v-btn color="success" @click="audit">确认</v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-data-table :headers="auditTableHeaders"
                                      :items="auditTableData"
                                      item-key="productID"
                                      height="75vh"
                                      calculate-widths
                                      disable-sort
                                      fixed-header
                                      disable-pagination
                                      group-by="checkoutEntrySerial"
                                      group-desc
                                      show-group-by
                                      :loading="loading"
                                      dense
                                      locale="zh-cn">
                        </v-data-table>
                    </v-row>
                </v-container>
            </v-tab-item>

            <v-tab-item key="query">
                <v-container>
                <v-row>
                    <v-spacer></v-spacer>
                    <v-col cols="auto">
                        <v-btn color="primary" @click="queryAuditMonths">查询已审核月份</v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-dialog v-model="deletePopup" max-width="300px">
                            <template v-slot:activator="{ on }">
                                <v-btn color="warning" v-on="on">删除选择月份</v-btn>
                            </template>
                            <v-card>
                                <v-card-title>确认删除？</v-card-title>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" @click="deletePopup = false">取消</v-btn>
                                    <v-btn color="success" @click="deleteMonth">确认</v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-col>
                    <v-spacer></v-spacer>
                </v-row>
                <v-row>
                    <v-data-table v-model="queryTableCurrentRow"
                                  :headers="queryTableHeaders"
                                  :items="queryTableData"
                                  item-key="index"
                                  height="75vh"
                                  calculate-widths
                                  disable-sort
                                  fixed-header
                                  disable-pagination
                                  show-select
                                  single-select
                                  @click:row="queryTableClick"
                                  dense
                                  locale="zh-cn">
                    </v-data-table>
                </v-row>
                </v-container>
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

            /* ----- first tab data ----- */

            month: new Date().format('yyyy-MM'),
            allowedMaxMonth: new Date().format('yyyy-MM'),
            isInbound: true,
            loading: false,
            confirmDialog: false,

            auditTableHeaders: [
                { text: '结账单号', value: 'checkoutEntrySerial', width: '140px', groupable: true },
                { text: '代号', value: 'code', width: '180px', groupable: false },
                { text: '厂牌', value: 'factoryCode', width: '65px', groupable: false },
                { text: '数量', value: 'quantity', width: '90px', groupable: false },
                { text: '单位', value: 'unitName', width: '60px', groupable: false },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px', groupable: false },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px', groupable: false },
                { text: '无税金额', value: 'totalWithoutTax', width: '100px', groupable: false },
                { text: '税率', value: 'taxRate', width: '65px', groupable: false },
                { text: '税额', value: 'totalTax', width: '90px', groupable: false },
                { text: '备注', value: 'remark', width: '180px', groupable: false },
                { text: '库存数量', value: 'stockQuantity', width: '120px', groupable: false },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px', groupable: false }
            ],
            auditTableData: [],

            /* ----- second tab data ----- */
            deletePopup: false,

            queryTableHeaders: [
                { text: '月份', value: 'propertyValue', width: '120px' },
                { text: '出/入', value: 'propertyValue2', width: '120px' },
            ],
            queryTableData: [],
            queryTableCurrentRow: [],

        }
    },
    methods: {
        search(inbound) {
            this.isInbound = inbound
            this.loading = true
            this.$getRequest(this.$api.getAuditEntries, {
                month: this.month, isInbound: inbound
            }).then(data => {
                this.loading = false
                this.auditTableData = data
            }).catch(() => {})
        },
        clear() {
            this.auditTableData = []
        },
        audit() {
            this.$postRequest(this.$api.auditEntries, {}, {
                month: this.month, isInbound: this.isInbound
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '审核成功', color: 'success'
                })
                this.$router.replace('/query_stats')
            }).catch(() => {})
        },

        queryAuditMonths() {
            this.$getRequest(this.$api.auditMonths).then(data => {
                let index = 0
                for (const item of data) {
                    this.queryTableData.push(Object.assign({index: index++}, item))
                }
            }).catch(() => {})
        },
        queryTableClick(row) {
            if (this.queryTableCurrentRow.includes(row)) {
                this.queryTableCurrentRow = []
            }
            else {
                this.queryTableCurrentRow = [row]
            }
        },
        deleteMonth() {
            if (this.queryTableCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '未选择月份', color: 'warning'
                })
                return
            }

            this.$deleteRequest(this.$api.deleteAuditMonth, {
                month: this.queryTableCurrentRow[0].propertyValue,
                value: this.queryTableCurrentRow[0].propertyValue2
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '删除成功', color: 'success'
                })
                this.queryTableData.splice(this.queryTableData.indexOf(this.queryTableCurrentRow[0]), 1)
                this.queryTableCurrentRow = []
            })
        }
    }
}
</script>

<style scoped>

</style>
