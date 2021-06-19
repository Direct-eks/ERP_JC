<template>
    <v-container>
        <v-form ref="form">
            <v-row v-if="!quotaDisplayMode && !quotaModifyMode">
                <v-col cols="auto">
                    <v-select v-if="salesOrderModifyMode"
                              v-model="form.warehouseID"
                              :rules="rules.warehouseID"
                              :items="warehouseOptions"
                              item-value="warehouseID"
                              item-text="name"
                              label="仓库"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.warehouseName"
                                  label="仓库"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-if="outboundEntryModifyMode || salesOrderModifyMode"
                              v-model="form.departmentID"
                              :rules="rules.departmentID"
                              :items="departmentOptions"
                              item-value="departmentID"
                              item-text="name"
                              label="部门"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.departmentName"
                                  label="部门"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shipmentDate"
                                  label="出库日期"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense>
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto" v-if="!quotaDisplayMode && !quotaModifyMode">
                    <v-text-field v-if="outboundEntryModifyMode || outboundEntryDisplayMode"
                                  v-model="form.classification"
                                  label="出库类别"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                    <v-text-field v-else-if="salesOrderDisplayMode"
                                  v-model="form.executionStatus"
                                  label="状态"
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
                    <v-select v-if="outboundEntryModifyMode || salesOrderModifyMode"
                              v-model="form.invoiceType"
                              :rules="rules.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="预计单据类型"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.invoiceType"
                                  label="预计单据类型"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyPhone"
                                  label="电话"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 200px"
                                  @keydown.enter="triggerSimpleSearch = true">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="购货单位简称"
                                  hide-details="auto"
                                  outlined
                                  :rules="rules.company"
                                  dense
                                  style="width: 200px"
                                  @keydown.enter="triggerSimpleSearch = true">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="购货单位全称"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row v-if="outboundEntryDisplayMode || outboundEntryModifyMode">
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

            <v-row>
                <v-col cols="auto" v-if="outboundEntryDisplayMode || outboundEntryModifyMode">
                    <v-radio-group v-model="form.shippingCostType"
                                   hide-details="auto"
                                   class="mt-0"
                                   readonly
                                   row dense>
                        <v-radio label="无运费" value="无"></v-radio>
                        <v-radio label="自付运费" value="自付"></v-radio>
                        <v-radio label="代垫运费" value="代垫"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto" v-if="outboundEntryDisplayMode || outboundEntryModifyMode">
                    <v-text-field v-model.number="form.shippingCost"
                                  label="运费"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="outboundEntryDisplayMode || outboundEntryModifyMode">
                    <v-text-field v-model.number="form.deliveryMethod"
                                  label="提货方式"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="form.totalAmount"
                                  label="总金额"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  readonly
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-textarea v-model.number="form.remark"
                                label="备注"
                                hide-details="auto"
                                outlined
                                dense
                                auto-grow
                                :readonly="(!outboundEntryReturnMode && outboundEntryDisplayMode)
                                            || salesOrderDisplayMode || quotaDisplayMode"
                                rows="1"
                                counter="200">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row dense>
            <v-col v-if="outboundEntryModifyMode || salesOrderModifyMode || quotaModifyMode">
                <v-dialog v-model="deleteTableRowPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="red lighten-1"
                               v-on="on">
                            删除
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-title>
                            确认删除？
                        </v-card-title>
                        <v-card-text>
                            {{rowDeletionConfirm}}
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary"
                                   @click="deleteTableRowPopup = false">
                                取消
                            </v-btn>
                            <v-btn color="success"
                                   @click="handleDeleteRow">
                                确认
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="outboundEntryModifyMode">
                <v-btn color="primary"
                       @click="saveEntryModification()">
                    保存修改
                </v-btn>
            </v-col>
            <v-col v-if="salesOrderModifyMode">
                <v-btn color="primary"
                       @click="saveOrderModification()">
                    保存修改
                </v-btn>
            </v-col>
            <v-col v-if="quotaModifyMode">
                <v-btn color="primary"
                       @click="saveQuotaModification()">
                    保存修改
                </v-btn>
            </v-col>
            <v-col v-if="outboundEntryReturnMode">
                <v-btn color="primary"
                       @click="saveEntryReturn()">
                    保存修改
                </v-btn>
            </v-col>
        </v-row>

        <v-data-table v-if="outboundEntryModifyMode || salesOrderModifyMode || quotaModifyMode"
                      v-model="tableRowsSelectedForDeletion"
                      :headers="tableHeaders"
                      :items="outboundEntryModifyMode ? form.outboundProducts :
                        salesOrderModifyMode ? form.salesOrderProducts : form.quotaProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{outboundEntryModifyMode ? form.outboundProducts.indexOf(item) + 1 :
                salesOrderModifyMode ? form.salesOrderProducts.indexOf(item) + 1 :
                    form.quotaProducts.indexOf(item) + 1 }}
            </template>
            <template v-slot:item.quantity="{ item }">
                <v-edit-dialog :return-value.sync="item.quantity"
                               persistent
                               large
                               save-text="确认"
                               cancel-text="取消"
                               @save="handleQuantityChange(item)">
                    {{item.quantity}}
                    <template v-slot:input>
                        <v-text-field v-model="item.quantity"
                                      single-line
                                      counter="8">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.unitPriceWithTax="{ item }">
                <v-edit-dialog :return-value.sync="item.unitPriceWithTax"
                               persistent
                               large
                               save-text="确认"
                               cancel-text="取消"
                               @save="handlePriceWithTaxChange(item)">
                    {{item.unitPriceWithTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPriceWithTax"
                                      single-line
                                      counter="12">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.unitPriceWithoutTax="{ item }">
                <v-edit-dialog :return-value.sync="item.unitPriceWithoutTax"
                               persistent
                               large
                               save-text="确认"
                               cancel-text="取消"
                               @save="handlePriceWithoutTaxChange(item)">
                    {{item.unitPriceWithoutTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPriceWithoutTax"
                                      single-line
                                      counter="12">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.remark="{ item }">
                <v-edit-dialog :return-value.sync="item.remark"
                               persistent
                               large
                               save-text="确认"
                               cancel-text="取消">
                    {{item.remark}}
                    <template v-slot:input>
                        <v-text-field v-model="item.remark"
                                      single-line
                                      counter="200">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>

        <v-data-table v-else-if="outboundEntryReturnMode"
                      :headers="returnTableHeaders"
                      :items="form.outboundProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{ form.outboundProducts.indexOf(item) + 1 }}
            </template>
            <template v-slot:item.returnQuantity="{ item }">
                <v-edit-dialog :return-value="item.returnQuantity"
                               persistent
                               large
                               save-text="确认"
                               cancel-text="取消"
                               @save="handleReturnQuantityChange(item)">
                    {{item.returnQuantity}}
                    <template v-slot:input>
                        <v-text-field v-model="item.returnQuantity"
                                      single-line
                                      counter="8">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>

        <v-data-table v-else
                      :headers="tableHeaders"
                      :items="outboundEntryDisplayMode ? form.outboundProducts :
                        salesOrderDisplayMode ? form.salesOrderProducts : form.quotaProducts"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{outboundEntryDisplayMode ? form.outboundProducts.indexOf(item) + 1 :
                salesOrderDisplayMode ? form.salesOrderProducts.indexOf(item) + 1 :
                    form.quotaProducts.indexOf(item) + 1 }}
            </template>
        </v-data-table>

        <v-row>
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <span>税额合计</span>
            </v-col>
            <v-col cols="auto">
                {{tax}}
            </v-col>
            <v-col cols="auto">
                <span>不含税合计</span>
            </v-col>
            <v-col cols="auto">
                {{sumWithoutTax}}
            </v-col>
            <v-col cols="auto">
                <span>价税合计</span>
            </v-col>
            <v-col cols="auto">
                {{sumWithTax}}
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
function validateFloat(value) {
    value = value.replace(/[^\d.]/g, "")  // 清除“数字”和“.”以外的字符
    value = value.replace(/\.{2,}/g, ".") // 只保留第一个. 清除多余的
    value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".")
    value = value.replace(/^(-)*(\d+)\.(\d\d).*$/, '$1$2.$3') // 只能输入两个小数
    if (value.indexOf(".") < 0 && value !== "") // 如果没有小数点，首位不能为0
        value = parseFloat(value)
    console.log('float', value)
    return value
}

export default {
    name: "OutboundEntryDisplayComponent",
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
    watch: {
        form: {
            handler(newVal) {
                let tax = 0.0
                let sumWithTax = 0.0
                let sumWithoutTax = 0.0
                if (this.outboundEntryDisplayMode || this.outboundEntryModifyMode || this.outboundEntryReturnMode) {
                    for (let item of newVal.outboundProducts) {
                        tax += (item.unitPriceWithTax - item.unitPriceWithoutTax) * item.quantity
                        sumWithTax += item.unitPriceWithTax * item.quantity
                        sumWithoutTax += item.unitPriceWithoutTax * item.quantity
                    }
                }
                else if (this.salesOrderDisplayMode || this.salesOrderModifyMode) {
                    for (let item of newVal.salesOrderProducts) {
                        tax += (item.unitPriceWithTax - item.unitPriceWithoutTax) * item.quantity
                        sumWithTax += item.unitPriceWithTax * item.quantity
                        sumWithoutTax += item.unitPriceWithoutTax * item.quantity
                    }
                }
                else if (this.quotaDisplayMode || this.quotaModifyMode) {
                    for (let item of newVal.quotaProducts) {
                        tax += (item.unitPriceWithTax - item.unitPriceWithoutTax) * item.quantity
                        sumWithTax += item.unitPriceWithTax * item.quantity
                        sumWithoutTax += item.unitPriceWithoutTax * item.quantity
                    }
                }
                this.tax = tax.toFixed(2)
                this.sumWithTax = sumWithTax.toFixed(2)
                this.sumWithoutTax = sumWithoutTax.toFixed(2)
            },
            deep: true,
            immediate: true
        }
    },
    beforeMount() {
        switch (this.displayMode) {
        case 'outboundEntryModify':
            this.outboundEntryModifyMode = true
            break
        case 'outboundEntryDisplay':
            this.outboundEntryDisplayMode = true
            break
        case 'quotaModify':
            this.quotaModifyMode = true
            break
        case 'quotaDisplay':
            this.quotaDisplayMode = true
            break
        case 'salesOrderModify':
            this.salesOrderModifyMode = true
            break
        case 'salesOrderDisplay':
            this.salesOrderDisplayMode = true
            break
        case 'outboundEntryReturn':
            this.outboundEntryReturnMode = true
            this.outboundEntryDisplayMode = true
            break
        }

        if (this.outboundEntryModifyMode || this.salesOrderModifyMode) {
            this.$getRequest(this.$api.departmentOptions).then((data) => {
                console.log(data)
                this.departmentOptions = data
                for (let item of this.departmentOptions)
                    if (item.isDefault === 1)
                        this.form.departmentID = item.id
            }).catch(() => {})
        }
        if (this.salesOrderModifyMode) {
            this.$getRequest(this.$api.warehouseOptions).then((data) => {
                console.log(data)
                this.warehouseOptions = data
                for (let item of this.warehouseOptions)
                    if (item.isDefault === 1)
                        this.form.warehouseID = item.id
            }).catch(() => {})
        }
    },
    data() {
        return {
            outboundEntryDisplayMode: false,
            outboundEntryModifyMode: false,
            salesOrderDisplayMode: false,
            salesOrderModifyMode: false,
            quotaDisplayMode: false,
            quotaModifyMode: false,
            outboundEntryReturnMode: false,

            rules: {
                warehouseID: [v => !!v || '请选择仓库'],
                departmentID: [v => !!v || '请选择部门'],
                invoiceType: [v => !!v || ' 请选择单据类型'], //no need to validate if is purchase order
            },

            warehouseOptions: [],
            departmentOptions: [],
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
                {text: '序号', value: 'index', width: '60px'},
                {text: '代号', value: 'code', width: '100px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '出库数量', value: 'quantity', width: '80px'},
                {text: '单位', value: 'unitName', width: '60px'},
                {text: '含税单价', value: 'unitPriceWithTax', width: '80px'},
                {text: '无税单价', value: 'unitPriceWithoutTax', width: '80px'},
                {text: '无税金额', value: 'totalWithoutTax', width: '80px'},
                {text: '税率', value: 'taxRate', width: '65px'},
                {text: '税额', value: 'totalTax', width: '80px'},
                {text: '备注', value: 'remark', width: '120px'},
                {text: '库存数量', value: 'stockQuantity', width: '120px'},
                {text: '库存单价', value: 'stockUnitPrice', width: '120px'}
            ],

            returnTableHeaders: [
                {text: '序号', value: 'index', width: '60px'},
                {text: '代号', value: 'code', width: '100px'},
                {text: '厂牌', value: 'factoryCode', width: '65px'},
                {text: '出库数量', value: 'originalQuantity', width: '80px'},
                {text: '退货数量', value: 'returnQuantity', width: '80px'},
                {text: '现有数量', value: 'quantity', width: '80px'},
                {text: '单位', value: 'unitName', width: '60px'},
                {text: '含税单价', value: 'unitPriceWithTax', width: '80px'},
                {text: '无税单价', value: 'unitPriceWithoutTax', width: '80px'},
                {text: '无税金额', value: 'totalWithoutTax', width: '80px'},
                {text: '税率', value: 'taxRate', width: '65px'},
                {text: '税额', value: 'totalTax', width: '80px'},
                {text: '备注', value: 'remark', width: '120px'},
            ],

            deleteTableRowPopup: false,
            tableRowsSelectedForDeletion: [],

            tax: 0.0,
            sumWithTax: 0.0,
            sumWithoutTax: 0.0
        }
    },
    methods: {
        /* ----- number calculation ----- */
        handleQuantityChange(row) {
            //calculate for each row
            row.quantity = row.quantity.toString().replace(/[^\d]/g, "")
            row.totalWithoutTax = (row.quantity * row.unitPriceWithoutTax).toFixed(2)
            row.totalTax = (row.quantity * row.unitPriceWithTax - row.totalWithoutTax).toFixed(2)

            let tempSumWithTax = 0
            if (this.outboundEntryModifyMode || this.outboundEntryReturnMode) {
                this.form.outboundProducts.forEach((item) => {
                    tempSumWithTax += item.unitPriceWithTax * item.quantity
                })
            }
            else if (this.salesOrderModifyMode) {
                this.form.salesOrderProducts.forEach((item) => {
                    tempSumWithTax += item.unitPriceWithTax * item.quantity
                })
            }
            else {
                this.form.quotaProducts.forEach((item) => {
                    tempSumWithTax += item.unitPriceWithTax * item.quantity
                })
            }

            this.form.totalAmount = (this.form.shippingCostType === '代垫' ?
                tempSumWithTax + this.form.shippingCost : tempSumWithTax).toFixed(2)
        },
        handlePriceWithTaxChange(row) {
            row.unitPriceWithTax = validateFloat(row.unitPriceWithTax.toString())
            row.unitPriceWithoutTax = (row.unitPriceWithTax / 1.16).toFixed(2)
            this.handleQuantityChange(row)
        },
        handlePriceWithoutTaxChange(row) {
            row.unitPriceWithoutTax = validateFloat(row.unitPriceWithoutTax.toString())
            row.unitPriceWithTax = (row.unitPriceWithoutTax * 1.16).toFixed(2)
            this.handleQuantityChange(row)
        },
        handleReturnQuantityChange(row) {
            row.originalQuantity = row.originalQuantity.toString().replace(/[^\d]/g, "")
            row.quantity = Number(row.originalQuantity) - Number(row.returnQuantity)
            this.handleQuantityChange(row)
        },
        /*------- table & entry submission -------*/
        handleDeleteRow() {
            this.deleteTableRowPopup = false
            if (this.tableRowsSelectedForDeletion.length !== 0) {
                for (let item of this.tableRowsSelectedForDeletion) {
                    if (this.outboundEntryModifyMode) {
                        this.form.outboundProducts.splice(this.form.outboundProducts.indexOf(item), 1)
                    }
                    else if (this.salesOrderModifyMode) {
                        this.form.salesOrderProducts.splice(this.form.salesOrderProducts.indexOf(item), 1)
                    }
                    else if (this.quotaModifyMode) {
                        this.form.quotaProducts.splice(this.form.quotaProducts.indexOf(item), 1)
                    }
                }
                this.tableRowsSelectedForDeletion = []
            } else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
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

                this.$patchRequest(this.$api.modifyOutboundEntry, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$router.replace('/outbound_management')
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

                this.$patchRequest(this.$api.modifySalesOrder, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$router.replace('/outbound_management')
                }).catch(() => {})
            }
        },
        saveQuotaModification() {
            if (this.$refs.form.validate()) {
                //change drawer name for modification
                this.form.drawer = this.$store.getters.currentUser

                this.$patchRequest(this.$api.modifyQuote, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$router.replace('/outbound_management')
                }).catch(() => {})
            }
        },
        saveEntryReturn() {
            this.$postRequest(this.$api.returnOutboundEntry, this.form).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$router.replace('/outbound_management')
            }).catch(() => {})
        }
    },
    computed: {
        rowDeletionConfirm() {
            let result = '确认删除以下序号的行: '
            this.tableRowsSelectedForDeletion.forEach(item => {
                result += (this.outboundEntryModifyMode ?
                    this.form.outboundProducts.indexOf(item) + 1
                    : this.salesOrderModifyMode ?
                        this.form.salesOrderProducts.indexOf(item) + 1
                        : this.form.quotaProducts.indexOf(item) + 1).toString() + ' '
            })
            return result
        },
    },
}
</script>

<style scoped>

</style>
