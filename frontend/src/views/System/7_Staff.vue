<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>系统及人员信息</p>-->
    <v-card>
        <v-card-title class="d-flex">
            系统及人员信息
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="isCreatingNewUser = true" :disabled="isCreatingNewUser">
                新增用户
            </v-btn>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row v-show="!isCreatingNewUser">
                <v-data-table v-model="userTableCurrentRow"
                              :headers="userTableHeaders"
                              :items="userTableData"
                              item-key="userID"
                              calculate-widths
                              disable-sort
                              fixed-header
                              single-select
                              show-select
                              @click:row="tableSelect"
                              hide-default-footer
                              locale="zh-cn"
                              dense>
                    <template v-slot:item.index="{ item }">
                        {{userTableData.indexOf(item) + 1}}
                    </template>
                </v-data-table>
            </v-row>
            <v-form ref="form">
                <v-row v-if="userTableCurrentRow.length !== 0 || isCreatingNewUser" class="mt-5 mb-5">
                    <v-col cols="auto">
                        <v-text-field v-model="form.username"
                                      :rules="rules.username"
                                      label="用户名"
                                      hide-details="auto"
                                      outlined
                                      dense
                                      style="width: 150px">
                        </v-text-field>
                    </v-col>
                    <v-col v-if="isCreatingNewUser" cols="auto">
                        <v-text-field v-model="form.password"
                                      :rules="rules.password"
                                      label="密码"
                                      hide-details="auto"
                                      outlined
                                      dense
                                      type="password"
                                      style="width: 150px">
                        </v-text-field>
                    </v-col>
                    <v-col cols="auto">
                        <v-select v-model="form.role"
                                  :rules="rules.role"
                                  :items="allRoles"
                                  item-value="role_id"
                                  item-text="role"
                                  label="级别"
                                  hide-details="auto"
                                  outlined dense
                                  style="width: 150px">
                        </v-select>
                    </v-col>
                    <v-col cols="auto">
                        <v-text-field v-model="form.remark"
                                      label="备注"
                                      hide-details="auto"
                                      outlined
                                      dense
                                      style="width: 180px">
                        </v-text-field>
                    </v-col>
                </v-row>
            </v-form>
            <v-row v-if="userTableCurrentRow.length !== 0 || isCreatingNewUser">
                <v-tabs v-model="tab" vertical>
                    <v-tab key="inboundEntry">入库</v-tab>
                    <v-tab key="outboundEntry">出库</v-tab>
                    <v-tab key="inboundCheckout">入库结账</v-tab>
                    <v-tab key="outboundCheckout">出库结账</v-tab>
                    <v-tab key="management">库存管理</v-tab>
                    <v-tab key="resources">资源录入</v-tab>

                    <v-tabs-items v-model="tab">
                        <v-tab-item key="inboundEntry">
                            <v-row class="ml-10">
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="入库单录入" value="inboundEntry:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="入库单完善" value="inboundEntry:Completion"/>
                                    <v-checkbox v-model="form.permissions" label="入库单修改" value="inboundEntry:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="入库单退货" value="inboundEntry:Return"/>
                                    <v-checkbox v-model="form.permissions" label="入库单查询" value="inboundEntry:Query"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="采购单录入" value="purchaseOrder:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="采购单修改" value="purchaseOrder:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="采购单查询" value="purchaseOrder:Query"/>
                                </v-col>
                            </v-row>
                        </v-tab-item>
                        <v-tab-item key="outboundEntry">
                            <v-row class="ml-10">
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="出库单录入" value="outboundEntry:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="出库单完善" value="outboundEntry:Completion"/>
                                    <v-checkbox v-model="form.permissions" label="出库单修改" value="outboundEntry:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="出库单退货" value="outboundEntry:Return"/>
                                    <v-checkbox v-model="form.permissions" label="出库单查询" value="outboundEntry:Query"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="销售单录入" value="salesOrder:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="销售单修改" value="salesOrder:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="销售单查询" value="salesOrder:Query"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="报价单录入" value="quota:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="报价单修改" value="quota:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="报价单查询" value="quota:Query"/>
                                </v-col>
                            </v-row>
                        </v-tab-item>
                        <v-tab-item key="inboundCheckout">
                            <v-row class="ml-10">
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="入库结账单录入" value="inboundCheckout:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="入库结账单查询" value="inboundCheckout:Query"/>
                                    <v-checkbox v-model="form.permissions" label="入库结账单修改" value="inboundCheckout:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="入库未结账查询" value="inboundCheckout:NotCheckoutQuery"/>
                                    <v-checkbox v-model="form.permissions" label="入库结账单退货" value="inboundCheckout:Return"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="入库开票单录入" value="inboundInvoice:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="入库开票单查询" value="inboundInvoice:Query"/>
                                    <v-checkbox v-model="form.permissions" label="入库开票单修改" value="inboundInvoice:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="入库未开票查询" value="inboundInvoice:NotInvoiceQuery"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="入库结账单录入" value="inboundPayment:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="入库结账单查询" value="inboundPayment:Query"/>
                                    <v-checkbox v-model="form.permissions" label="入库结账单修改" value="inboundPayment:Modification"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="入库运费单录入" value="inboundShippingCost:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="入库运费单查询" value="inboundShippingCost:Query"/>
                                    <v-checkbox v-model="form.permissions" label="入库运费单修改" value="inboundShippingCost:Modification"/>
                                </v-col>
                            </v-row>
                        </v-tab-item>
                        <v-tab-item key="outboundCheckout">
                            <v-row class="ml-10">
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="出库结账单录入" value="outboundCheckout:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="出库结账单查询" value="outboundCheckout:Query"/>
                                    <v-checkbox v-model="form.permissions" label="出库结账单修改" value="outboundCheckout:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="出库未结账查询" value="outboundCheckout:NotCheckoutQuery"/>
                                    <v-checkbox v-model="form.permissions" label="出库结账单退货" value="outboundCheckout:Return"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="出库开票单录入" value="outboundInvoice:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="出库开票单查询" value="outboundInvoice:Query"/>
                                    <v-checkbox v-model="form.permissions" label="出库开票单修改" value="outboundInvoice:Modification"/>
                                    <v-checkbox v-model="form.permissions" label="出库未开票查询" value="outboundInvoice:NotInvoiceQuery"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="出库结账单录入" value="outboundPayment:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="出库结账单查询" value="outboundPayment:Query"/>
                                    <v-checkbox v-model="form.permissions" label="出库结账单修改" value="outboundPayment:Modification"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="出库运费单录入" value="outboundShippingCost:Creation"/>
                                    <v-checkbox v-model="form.permissions" label="出库运费单查询" value="outboundShippingCost:Query"/>
                                    <v-checkbox v-model="form.permissions" label="出库运费单修改" value="outboundShippingCost:Modification"/>
                                </v-col>
                            </v-row>
                        </v-tab-item>
                        <v-tab-item key="management">
                            <v-row class="ml-10">
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="商品明细" value="management:ProductsDetails"/>
                                    <v-checkbox v-model="form.permissions" label="库存报表" value="management:StockReport"/>
                                    <v-checkbox v-model="form.permissions" label="商品定价" value="management:ProductsPricing"/>
                                    <v-checkbox v-model="form.permissions" label="库存报警" value="management:StockAlert"/>
                                    <v-checkbox v-model="form.permissions" label="明细统计" value="management:DetailStats"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="库存盘点" value="management:Inventory"/>
                                    <v-checkbox v-model="form.permissions" label="预销售资源" value="management:PresalesQuery"/>
                                    <v-checkbox v-model="form.permissions" label="库存资源" value="management:StockResources"/>
                                    <v-checkbox v-model="form.permissions" label="架位设置" value="management:StoragePlace"/>
                                </v-col>
                            </v-row>
                        </v-tab-item>
                        <v-tab-item key="resources">
                            <v-row class="ml-10">
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="商品分类" value="system:productCategories"/>
                                    <v-checkbox v-model="form.permissions" label="商品型号" value="system:models"/>
                                    <v-checkbox v-model="form.permissions" label="商品型号：增加" value="system:models:create"/>
                                    <v-checkbox v-model="form.permissions" label="商品型号：修改" value="system:models:update"/>
                                    <v-checkbox v-model="form.permissions" label="商品型号：删除" value="system:models:remove"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="生产厂" value="system:factoryBrands"/>
                                    <v-checkbox v-model="form.permissions" label="商品型号明细" value="system:sku"/>
                                    <v-checkbox v-model="form.permissions" label="往来单位区划" value="system:partnerCompanyCategories"/>
                                    <v-checkbox v-model="form.permissions" label="往来单位" value="system:partnerCompanies"/>
                                    <v-checkbox v-model="form.permissions" label="系统及人员信息" value="system:staff"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-checkbox v-model="form.permissions" label="相关单位分类" value="system:relevantCompanyCategories"/>
                                    <v-checkbox v-model="form.permissions" label="相关单位" value="system:relevantCompanies"/>
                                    <v-checkbox v-model="form.permissions" label="计量单位" value="system:measurementUnits"/>
                                    <v-checkbox v-model="form.permissions" label="仓库" value="system:warehouses"/>
                                    <v-checkbox v-model="form.permissions" label="部门" value="system:departments"/>
                                    <v-checkbox v-model="form.permissions" label="费用收入类别" value="system:fees"/>
                                </v-col>
                            </v-row>
                        </v-tab-item>
                    </v-tabs-items>
                </v-tabs>
            </v-row>
        </v-card-text>
        <v-card-actions v-if="userTableCurrentRow.length !== 0 || isCreatingNewUser">
            <v-dialog v-if="!isCreatingNewUser" v-model="deletePopup" max-width="300px">
                <template v-slot:activator="{ on }">
                    <v-btn color="red lighten-1 mr-2" v-on="on">删除用户</v-btn>
                </template>
                <v-card>
                    <v-card-title>确认删除？</v-card-title>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" @click="deletePopup = false">取消</v-btn>
                        <v-btn color="success" @click="deleteUser">确认</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
            <v-btn color="accent"
                   @click="chooseAllPermission">
                全选（包括非本页）权限
            </v-btn>
            <v-btn color="success"
                   @click="saveChanges">
                保存修改
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Staff",
    beforeMount() {
        this.$getRequest(this.$api.allUsers).then(data => {
            console.log('received', data)
            this.userTableData = data
        })
        this.$getRequest(this.$api.allRoles).then(data => {
            console.log('received', data)
            this.allRoles = data
        })
        this.$getRequest(this.$api.allPermissions).then(data => {
            console.log('received', data)
            this.allPermissions = data
        })
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,

            userTableHeaders: [
                { text: '序号', value: 'index', width: '65px' },
                { text: '用户', value: 'username', width: '110px' },
                { text: '级别', value: 'role.role', width: '70px' },
                { text: '备注', value: 'remark', width: '180px' },
            ],
            userTableData: [],
            userTableCurrentRow: [],

            form: {
                userID: -1,
                username: '',
                password: '',
                remark: '',
                role: '',
                permissions: [],
            },
            rules: {
                username: [v => !!v || '名字不能为空'],
                password: [v => this.isCreatingNewUser || !!v || '密码不能为空'],
                role: [v => !!v || '级别不能为空'],
            },

            isCreatingNewUser: false,

            allRoles: [],
            allPermissions: [],
            tab: null,
            deletePopup: false,
        }
    },
    methods: {
        tableSelect(row) {
            this.userTableCurrentRow = [row]
            Object.assign(this.form, row)
        },
        chooseAllPermission() {
            this.form.permissions = JSON.parse(JSON.stringify(this.allPermissions))
        },
        saveChanges() {
            if (this.isCreatingNewUser) {
                if (this.$refs.form.validate()) {
                    this.$putRequest(this.$api.createNewUser, this.form).then(() => {
                        this.$store.commit('setSnackbar', {
                            message: '保存成功', color: 'success'
                        })
                    }).catch(() => {})
                }
                this.$router.replace('/system')
                return
            }
            if (this.$refs.form.validate()) {
                this.$postRequest(this.$api.updateUser, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '保存成功', color: 'success'
                    })
                }).catch(() => {})
                // refresh curr user permissions
                if (this.form.username === this.$store.getters.currentUser) {
                    this.$store.commit('modifyCurrentUserPermissions', this.form.permissions)
                    sessionStorage.setItem('userPermissions', JSON.stringify(this.form.permissions))
                }
            }
        },
        deleteUser() {
            this.$deleteRequest(this.$api.deleteUser, {
                userID: this.form.userID
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '删除成功', color: 'success'
                })
                this.$router.replace('/system')
            }).catch(() => {})
        },
    }
}
</script>

<style scoped>

</style>
