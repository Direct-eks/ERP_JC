<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.warehouseID"
                              :rules="rules.warehouseID"
                              :items="warehouseOptions"
                              item-value="warehouseID"
                              item-text="name"
                              label="仓库"
                              hide-details="auto"
                              :readonly="!purchaseOrderModifyMode"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.departmentID"
                              :rules="rules.departmentID"
                              :items="departmentOptions"
                              item-value="departmentID"
                              item-text="name"
                              label="部门"
                              hide-details="auto"
                              :readonly="!inboundEntryModifyMode && !purchaseOrderModifyMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.entryDate"
                                  label="入库日期"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense>
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-if="inboundEntryDisplayMode || inboundEntryModifyMode || inboundEntryReturnMode"
                                  v-model="form.classification"
                                  label="入库类别"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                    <v-select v-else
                              v-model="form.executionStatus"
                              :items="executionStatusOptions"
                              item-value="value"
                              item-text="label"
                              label="状态"
                              hide-details="auto"
                              :readonly="purchaseOrderDisplayMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.invoiceType"
                              :rules="rules.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="预计单据类型"
                              hide-details="auto"
                              :readonly="!inboundEntryModifyMode && !purchaseOrderModifyMode"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col v-if="form.invoiceType === '增值税票'">
                    <v-select v-model="form.taxRate"
                              :rules="rules.taxRate"
                              :items="taxRateOptions"
                              @change="changeTaxRate"
                              label="税率"
                              hide-details="auto"
                              :append-icon="mdiPercentOutline"
                              :readonly="!inboundEntryModifyMode && !purchaseOrderModifyMode"
                              outlined dense
                              style="width: 110px">
                    </v-select>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyPhone"
                                  label="电话"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="供货单位简称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="供货单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row v-if="form.companyRemark !== ''" dense>
                <v-col>
                    <v-textarea v-model.number="form.companyRemark"
                                label="重要提示"
                                hide-details="auto"
                                background-color="red accent-2"
                                outlined
                                dense
                                readonly
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>

            <v-row v-if="inboundEntryDisplayMode || inboundEntryModifyMode || inboundEntryReturnMode">
                <v-col cols="auto">
                    <v-text-field v-model="form.relevantCompanyName"
                                  label="运输方式"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingNumber"
                                  label="运单号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 220px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingQuantity"
                                  label="件数"
                                  hide-details="auto"
                                  type="number"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 80px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row dense>
                <v-col cols="auto" v-if="inboundEntryDisplayMode || inboundEntryModifyMode || inboundEntryReturnMode">
                    <v-radio-group v-model="form.shippingCostType"
                                   hide-details="auto"
                                   class="mt-0"
                                   row dense
                                   readonly>
                        <v-radio label="无运费" value="无"></v-radio>
                        <v-radio label="自付运费" value="自付"></v-radio>
                        <v-radio label="代垫运费" value="代垫"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingCost"
                                  label="运费"
                                  hide-details="auto"
                                  type="number"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalCost"
                                  label="总金额"
                                  hide-details="auto"
                                  filled
                                  dense
                                  readonly
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-textarea v-model="form.remark"
                                label="备注"
                                hide-details="auto"
                                outlined
                                dense
                                :readonly="inboundEntryDisplayMode || purchaseOrderDisplayMode"
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row class="my-2" dense>
            <v-col v-if="inboundEntryModifyMode || purchaseOrderModifyMode">
                <v-dialog v-model="deleteTableRowPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="red lighten-1" v-on="on">删除</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认删除选中行？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="deleteTableRowPopup = false">取消</v-btn>
                            <v-btn color="success" @click="handleDeleteRow">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="inboundEntryModifyMode || purchaseOrderModifyMode || inboundEntryReturnMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="primary" @click="saveModification">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>

        <v-data-table v-if="inboundEntryDisplayMode || purchaseOrderDisplayMode"
                      :headers="tableHeaders"
                      :items="inboundEntryDisplayMode ? form.inboundProducts : form.purchaseOrderProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
        </v-data-table>

        <v-data-table v-else-if="inboundEntryModifyMode || purchaseOrderModifyMode"
                      v-model="tableRowsSelectedForDeletion"
                      :headers="tableHeaders"
                      :items="inboundEntryModifyMode ? form.inboundProducts : form.purchaseOrderProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.quantity="{ item }">
                <v-edit-dialog :return-value="item.quantity"
                               @save="handleQuantityChange(item)"
                               @cancel="handleQuantityChange(item)"
                               @close="handleQuantityChange(item)">
                    {{item.quantity}}
                    <template v-slot:input>
                        <v-text-field v-model="item.quantity" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.unitPriceWithTax="{ item }">
                <v-edit-dialog :return-value="item.unitPriceWithTax"
                               @save="handlePriceWithTaxChange(item)"
                               @cancel="handlePriceWithTaxChange(item)"
                               @close="handlePriceWithTaxChange(item)">
                    {{item.unitPriceWithTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPriceWithTax" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.unitPriceWithoutTax="{ item }">
                <v-edit-dialog :return-value="item.unitPriceWithoutTax"
                               @save="handlePriceWithoutTaxChange(item)"
                               @cancel="handlePriceWithoutTaxChange(item)"
                               @close="handlePriceWithoutTaxChange(item)">
                    {{item.unitPriceWithoutTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPriceWithoutTax" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.remark="{ item }">
                <v-edit-dialog :return-value="item.remark">
                    {{item.remark}}
                    <template v-slot:input>
                        <v-text-field v-model="item.remark" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>

        <v-data-table v-else-if="inboundEntryReturnMode"
                      :headers="returnTableHeaders"
                      :items="form.inboundProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.returnQuantity="{ item }">
                <v-edit-dialog :return-value="item.returnQuantity"
                               @save="handleReturnQuantityChange(item)"
                               @cancel="handleReturnQuantityChange(item)"
                               @close="handleReturnQuantityChange(item)">
                    {{item.returnQuantity}}
                    <template v-slot:input>
                        <v-text-field v-model="item.returnQuantity" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>

        <div class="d-flex">
            <v-spacer></v-spacer>
            <div class="my-2">
                <strong>税额合计：</strong>
            </div>
            <div class="my-2 mr-5">
                <strong>{{ tax }}</strong>
            </div>
            <div class="my-2">
                <strong>不含税合计：</strong>
            </div>
            <div class="my-2 mr-5">
                <strong>{{ sumWithoutTax }}</strong>
            </div>
            <div class="my-2">
                <strong>价税合计：</strong>
            </div>
            <div class="my-2 mr-5">
                <strong>{{ sumWithTax }}</strong>
            </div>
        </div>
    </v-container>
</template>

<script>
import {mdiPercentOutline} from "@mdi/js";

export default {
    name: "InboundEntryDisplayComponent",
    props: {
        displayMode: {
            type: String,
            required: true
        },
        form: {
            type: Object,
            required: true
        }
    },
    beforeMount() {
        switch (this.displayMode) {
        case 'inboundEntryDisplay':
            this.inboundEntryDisplayMode = true
            break
        case 'purchaseOrderDisplay':
            this.purchaseOrderDisplayMode = true
            break
        case 'inboundEntryModify':
            this.inboundEntryModifyMode = true
            break
        case 'purchaseOrderModify':
            this.purchaseOrderModifyMode = true
            break
        case 'inboundEntryReturn':
            this.inboundEntryReturnMode = true
            break
        }

        this.$getRequest(this.$api.departmentOptions).then((data) => {
            this.departmentOptions = data
        }).catch(() => {})

        this.$getRequest(this.$api.warehouseOptions).then((data) => {
            this.warehouseOptions = data
        }).catch(() => {})

        this.$getRequest(this.$api.allTaxRates).then((data) => {
            for (const option of data) {
                this.taxRateOptions.push(Number(option))
            }
        }).catch(() => {})
    },
    data() {
        return {
            mdiPercentOutline,
            inboundEntryDisplayMode: false,
            purchaseOrderDisplayMode: false,
            inboundEntryModifyMode: false,
            purchaseOrderModifyMode: false,
            inboundEntryReturnMode: false,

            rules: {
                warehouseID: [v => !!v || '请选择仓库'],
                departmentID: [v => !!v || '请选择部门'],
                invoiceType: [v => !!v || ' 请选择单据类型'], //no need to validate if is purchase order
                taxRate: [v => !!v || '请选择税率'],
            },

            warehouseOptions: [],
            departmentOptions: [],
            taxRateOptions: [],
            executionStatusOptions: [
                {value: '执行', label: '执行'},
                {value: '中止', label: '中止'}
            ],
            invoiceTypeOptions: [
                {value: '增值税票', label: '增值税票'},
                {value: '普票', label: '普票'},
                {value: '收据', label: '收据'}
            ],

            tableHeaders: [
                {text: '代号', value: 'code', width: '180px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '入库数量', value: 'quantity', width: '90px'},
                {text: '单位', value: 'unitName', width: '60px'},
                {text: '含税单价', value: 'unitPriceWithTax', width: '100px'},
                {text: '无税单价', value: 'unitPriceWithoutTax', width: '100px'},
                {text: '无税金额', value: 'totalWithoutTax', width: '100px'},
                {text: '税率', value: 'taxRate', width: '65px'},
                {text: '税额', value: 'totalTax', width: '90px'},
                {text: '备注', value: 'remark', width: '180px'},
                {text: '库存数量', value: 'stockQuantity', width: '120px'},
                {text: '库存单价', value: 'stockUnitPrice', width: '120px'}
            ],

            returnTableHeaders: [
                {text: '代号', value: 'code', width: '180px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '入库数量', value: 'originalQuantity', width: '90px'},
                {text: '退货数量', value: 'returnQuantity', width: '90px'},
                {text: '现有数量', value: 'quantity', width: '90px'},
                {text: '单位', value: 'unitName', width: '60px'},
                {text: '含税单价', value: 'unitPriceWithTax', width: '100px'},
                {text: '无税单价', value: 'unitPriceWithoutTax', width: '100px'},
                {text: '无税金额', value: 'totalWithoutTax', width: '100px'},
                {text: '税率', value: 'taxRate', width: '65px'},
                {text: '税额', value: 'totalTax', width: '90px'},
                {text: '备注', value: 'remark', width: '120px'},
            ],

            deleteTableRowPopup: false,
            tableRowsSelectedForDeletion: [],
            submitPopup: false,

            tax: '0',
            sumWithTax: '0',
            sumWithoutTax: '0'
        }
    },
    methods: {
        /* ----- number calculation ----- */
        handleTotalChange() {
            let tempSumWithTax = this.$Big('0')
            let tempSumWithoutTax = this.$Big('0')
            if (this.inboundEntryModifyMode || this.inboundEntryReturnMode || this.inboundEntryDisplayMode) {
                this.form.inboundProducts.forEach((item) => {
                    const itemQuantity = this.$Big(item.quantity)
                    tempSumWithTax = tempSumWithTax.add(itemQuantity.times(item.unitPriceWithTax))
                    tempSumWithoutTax = tempSumWithoutTax.add(itemQuantity.times(item.unitPriceWithoutTax))
                })
            }
            else if (this.purchaseOrderModifyMode || this.purchaseOrderDisplayMode) {
                this.form.purchaseOrderProducts.forEach((item) => {
                    const itemQuantity = this.$Big(item.quantity)
                    tempSumWithTax = tempSumWithTax.add(itemQuantity.times(item.unitPriceWithTax))
                    tempSumWithoutTax = tempSumWithoutTax.add(itemQuantity.times(item.unitPriceWithoutTax))
                })
            }
            this.sumWithTax = tempSumWithTax.toString()
            this.sumWithoutTax = tempSumWithoutTax.toString()
            this.tax = tempSumWithTax.minus(tempSumWithoutTax).toString()

            // plus shipping
            let sum = this.$Big(this.sumWithTax)
            switch (this.form.shippingCostType) {
            case "无":
            case '自付':
                break
            case '代垫':
                sum = sum.plus(this.form.shippingCost === '' ? '0' : this.form.shippingCost)
                break
            }
            this.form.totalCost = sum.toString()
        },
        handleQuantityChange(row) {
            //calculate for each row
            row.quantity = this.$validateNumber(row.quantity)
            const tempQuantity = this.$Big(row.quantity)
            row.totalWithoutTax = tempQuantity.times(row.unitPriceWithoutTax)
            row.totalTax = tempQuantity.times(row.unitPriceWithTax).minus(row.totalWithoutTax)
            this.handleTotalChange()
        },
        handlePriceWithTaxChange(row) {
            row.unitPriceWithTax = this.$validateFloat(row.unitPriceWithTax)
            const tempValue = this.$Big(row.unitPriceWithTax)
            row.unitPriceWithoutTax = tempValue.div(this.$Big(this.form.taxRate).div('100').add('1'))
            this.handleQuantityChange(row)
        },
        handlePriceWithoutTaxChange(row) {
            row.unitPriceWithoutTax = this.$validateFloat(row.unitPriceWithoutTax)
            const tempValue = this.$Big(row.unitPriceWithoutTax)
            row.unitPriceWithTax = tempValue.times(this.$Big(this.form.taxRate).div('100').add('1'))
            this.handleQuantityChange(row)
        },
        handleReturnQuantityChange(row) {
            row.returnQuantity = this.$validateNumber(row.returnQuantity)
            if (Number(row.returnQuantity) > Number(row.originalQuantity)) {
                this.$store.commit('setSnackbar', {
                    message: '退货数量错误', color: 'warn'
                })
                return
            }
            row.quantity = Number(row.originalQuantity) - Number(row.returnQuantity)
            this.handleQuantityChange(row)
        },
        changeTaxRate() {
            if (this.inboundEntryModifyMode || this.inboundEntryReturnMode) {
                this.form.inboundProducts.forEach((row) => {
                    row.taxRate = this.form.taxRate
                    this.handlePriceWithoutTaxChange(row)
                })
            }
            else if (this.purchaseOrderModifyMode) {
                this.form.purchaseOrderProducts.forEach((row) => {
                    row.taxRate = this.form.taxRate
                    this.handlePriceWithoutTaxChange(row)
                })
            }
        },
        /*------- table & entry submission -------*/
        handleDeleteRow() {
            this.deleteTableRowPopup = false
            if (this.tableRowsSelectedForDeletion.length !== 0) {
                for (let item of this.tableRowsSelectedForDeletion) {
                    if (this.inboundEntryModifyMode) {
                        this.form.inboundProducts.splice(this.form.inboundProducts.indexOf(item), 1)
                    }
                    else {
                        this.form.purchaseOrderProducts.splice(this.form.purchaseOrderProducts.indexOf(item), 1)
                    }
                }
                this.tableRowsSelectedForDeletion = []
            } else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        saveModification() {
            if (this.inboundEntryModifyMode) {
                this.saveEntryModification()
            }
            else if (this.inboundEntryReturnMode) {
                this.saveEntryReturn()
            }
            else if (this.purchaseOrderModifyMode) {
                this.saveOrderModification()
            }
            this.submitPopup = false
        },
        saveEntryModification() {
            if (this.$refs.form.validate()) {
                //change drawer name for modification
                this.form.drawer = this.$store.getters.currentUser

                //fill in department name
                this.departmentOptions.forEach(item => {
                    if (item.departmentID === this.form.departmentID)
                        this.form.departmentName = item.name
                })

                this.$store.commit('setOverlay', true)
                this.$patchRequest(this.$api.modifyInboundEntry, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/inbound_management')
                }).catch(() => {})
            }
        },
        saveOrderModification() {
            if (this.$refs.form.validate()) {
                //change drawer name for modification
                this.form.drawer = this.$store.getters.currentUser

                //fill in department name
                this.departmentOptions.forEach(item => {
                    if (item.departmentID === this.form.departmentID)
                        this.form.departmentName = item.name
                })
                //fill in warehouse name
                this.warehouseOptions.forEach(item => {
                    if (item.warehouseID === this.form.warehouseID)
                        this.form.warehouseName = item.name
                })

                this.$store.commit('setOverlay', true)
                this.$patchRequest(this.$api.modifyPurchaseOrder, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/inbound_management')
                }).catch(() => {})
            }
        },
        saveEntryReturn() {
            this.$store.commit('setOverlay', true)
            this.$postRequest(this.$api.returnInboundEntry, this.form).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$store.commit('setOverlay', false)

                this.$router.replace('/inbound_management')
            }).catch(() => {})
        }
    },
    watch: {
        form: {
            handler() {
                if (this.inboundEntryDisplayMode || this.inboundEntryModifyMode || this.inboundEntryReturnMode) {
                    this.form.inboundProducts.forEach(p => {
                        this.handleQuantityChange(p)
                    })
                }
                else if (this.purchaseOrderDisplayMode || this.purchaseOrderModifyMode) {
                    this.form.purchaseOrderProducts.forEach(p => {
                        this.handleQuantityChange(p)
                    })
                }
            },
            deep: true,
            immediate: true
        }
    }
}
</script>

<style scoped>

</style>
