<template>
    <v-container>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <v-select v-if="enabledFields.warehouse"
                              v-model="form.warehouseID"
                              :rules="rules.warehouseID"
                              :items="warehouseOptions"
                              item-value="id"
                              item-text="name"
                              label="仓库"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.warehouse"
                                  label="仓库"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense>
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-if="enabledFields.department"
                              v-model="form.departmentID"
                              :rules="rules.departmentID"
                              :items="departmentOptions"
                              item-value="id"
                              item-text="name"
                              label="部门"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.department"
                                  label="部门"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense>
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-menu v-if="enabledFields.entryDate"
                            close-on-content-click
                            :nudge-right="40"
                            transition="scale-transition"
                            offset-y>
                        <template v-slot:activator="{on}">
                            <v-text-field v-model="form.entryDate"
                                          :rules="rules.entryDate"
                                          v-on="on"
                                          label="入库日期"
                                          hide-details="auto"
                                          outlined
                                          readonly
                                          dense>
                            </v-text-field>
                        </template>
                        <v-date-picker v-model="form.entryDate"
                                       no-title
                                       :first-day-of-week="0"
                                       locale="zh-cn">
                        </v-date-picker>
                    </v-menu>
                    <v-text-field v-else
                                  v-model="form.entryDate"
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
                    <v-text-field v-model="form.entryType"
                                  label="入库类别"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
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
                    <v-select v-if="enabledFields.expectedInvoiceType"
                              v-model="form.expectedInvoiceType"
                              :rules="rules.expectedInvoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="预计单据类型"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                    <v-text-field v-else
                                  v-model="form.expectedInvoiceType"
                                  label="预计单据类型"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyPhone"
                                  label="电话"
                                  hide-details="auto"
                                  outlined
                                  :readonly="!enabledFields.company"
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col v-if="enabledFields.company" cols="auto">
                    <CompanySearchSimple
                            v-bind:simpleSearchField="form.companyPhone"
                            simpleSearchMethod="phone"
                            @simpleSearchChoose="simpleSearchChooseAction">
                    </CompanySearchSimple>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="供货单位简称"
                                  hide-details="auto"
                                  outlined
                                  :rules="rules.company"
                                  :readonly="!enabledFields.company"
                                  dense
                                  style="width: 200px">
                    </v-text-field>
                </v-col>
                <v-col v-if="enabledFields.company" cols="auto">
                    <CompanySearchSimple
                            v-bind:simpleSearchField="form.companyAbbreviatedName"
                            simpleSearchMethod="name"
                            @simpleSearchChoose="simpleSearchChooseAction">
                    </CompanySearchSimple>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="供货单位全称"
                                  hide-details="auto"
                                  outlined
                                  :readonly="!enabledFields.company"
                                  dense
                                  style="width: 300px">
                    </v-text-field>
                </v-col>
                <v-col v-if="enabledFields.company" cols="auto">
                    <v-dialog v-model="fullSearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="80vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent"
                                   v-on="on"
                                   :disabled="fullSearchPanelOpen">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch
                                @fullSearchClose="fullSearchCloseAction"
                                @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingMethod"
                                  label="运输方式"
                                  hide-details="auto"
                                  :rules="rules.shippingMethodID"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
                <v-col v-if="enabledFields.shipping" cols="auto">
                    <v-dialog v-model="relativeCompanySearchPanelOpen"
                              persistent
                              scrollable
                              no-click-animation
                              width="75vw">
                        <template v-slot:activator="{on}">
                            <v-btn color="accent"
                                   v-on="on"
                                   :disabled="relativeCompanySearchPanelOpen">
                                选择
                            </v-btn>
                        </template>
                        <RelativeCompanySearch
                                @relativeCompanyClose="relativeCompanyCloseAction"
                                @relativeCompanyChoose="relativeCompanyChooseAction">
                        </RelativeCompanySearch>
                    </v-dialog>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingNumber"
                                  label="运单号"
                                  hide-details="auto"
                                  outlined
                                  :readonly="!enabledFields.shipping"
                                  dense
                                  style="width: 220px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingQuantity"
                                  label="件数"
                                  hide-details="auto"
                                  type="number"
                                  @change="handleShippingQuantityChange"
                                  outlined
                                  :readonly="!enabledFields.shipping"
                                  dense
                                  style="width: 80px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-radio-group v-model="form.shippingCostType"
                                   hide-details="auto"
                                   :readonly="!enabledFields.shipping"
                                   class="mt-0"
                                   row dense>
                        <v-radio label="无运费" value="无运费"></v-radio>
                        <v-radio label="自付运费" value="自付运费"></v-radio>
                        <v-radio label="代垫运费" value="代垫运费"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="form.shippingCost"
                                  label="运费"
                                  @change="handleShippingCostChange"
                                  hide-details="auto"
                                  outlined
                                  :readonly="!enabledFields.shipping"
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model.number="totalSumPlusShippingCost"
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
                                :readonly="!enabledFields.remark"
                                dense
                                auto-grow
                                rows="1"
                                counter="200">
                    </v-textarea>
                </v-col>
            </v-row>
            <v-row v-if="enabledFields.remarkForModification">
                <v-col>
                    <v-textarea v-model.number="form.remarkForModification"
                                label="修改备注"
                                hide-details="auto"
                                outlined
                                :readonly="!enabledFields.remarkForModification"
                                dense
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row dense>
            <v-col v-if="enabledFields.entrySave">
                <v-dialog v-model="purchaseOrderPanelOpen"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="purchaseOrderPanelOpen">
                            查询该单位采购订单
                        </v-btn>
                    </template>
                    <InboundQueryPurchaseEntry
                            :companyID="form.companyID"
                            @purchaseOrderClose="purchaseOrderCloseAction"
                            @purchaseOrderChoose="purchaseOrderChooseAction">
                    </InboundQueryPurchaseEntry>
                </v-dialog>
            </v-col>
            <v-col v-if="enabledFields.entrySave || enabledFields.editSave || enabledFields.purchaseSave">
                <v-dialog v-model="modelSearchPanelOpen"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="modelSearchPanelOpen">
                            型号助选
                        </v-btn>
                    </template>
                    <ModelSearch
                            @modelSearchClose="modelSearchCloseAction"
                            @modelSearchChoose="modelSearchChooseAction">
                    </ModelSearch>
                </v-dialog>
            </v-col>
            <v-col v-if="enabledFields.entrySave || enabledFields.editSave || enabledFields.purchaseSave">
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
            <v-col v-if="enabledFields.purchaseSave">
                <v-btn color="primary"
                       @click="saveAsPurchaseOrder">
                    存为采购订单
                </v-btn>
            </v-col>
            <v-col v-if="enabledFields.entrySave">
                <v-btn color="primary"
                       @click="saveAsEntry(true)">
                    存为入库单后新增
                </v-btn>
            </v-col>
            <v-col v-if="enabledFields.entrySave">
                <v-btn color="primary"
                       @click="saveAsEntry(false)">
                    存为入库单
                </v-btn>
            </v-col>
            <v-col v-if="enabledFields.completionSave || enabledFields.editSave || enabledFields.returnSave">
                <v-btn color="primary"
                       @click="saveModification">
                    保存修改
                </v-btn>
            </v-col>
        </v-row>

        <v-data-table v-if="enabledFields.productsShow"
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="id"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{form.products.indexOf(item) + 1}}
            </template>
        </v-data-table>

        <v-data-table v-else-if="enabledFields.returnSave"
                      v-model="tableRowsSelectedForDeletion"
                      :headers="returnTableHeaders"
                      :items="tableData"
                      item-key="id"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{form.products.indexOf(item) + 1}}
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
                                      counter="12">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>

        <v-data-table v-else
                      v-model="tableRowsSelectedForDeletion"
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="id"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-slot:item.index="{ item }">
                {{tableData.indexOf(item) + 1}}
            </template>
            <template v-slot:item.quantity="{ item }">
                <v-edit-dialog :return-value="item.quantity"
                               persistent
                               large
                               save-text="确认"
                               cancel-text="取消"
                               @save="handleQuantityChange(item)">
                    {{item.quantity}}
                    <template v-slot:input>
                        <v-text-field v-model="item.quantity"
                                      single-line
                                      counter="12">
                        </v-text-field>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.unitPriceWithTax="{ item }">
                <v-edit-dialog :return-value="item.unitPriceWithTax"
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
                <v-edit-dialog :return-value="item.unitPriceWithoutTax"
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
        name: "InboundEntryDisplayComponent",
        components: {
            CompanySearchSimple: () => import("~/components/CompanySearchSimple"),
            CompanySearch: () => import("~/components/CompanySearch"),
            ModelSearch: () => import("~/components/InboundModelSearch"),
            RelativeCompanySearch: () => import("~/components/RelativeCompanySearch"),
            InboundQueryPurchaseEntry: () => import("~/components/InboundQueryPurchaseEntry")
        },
        props: {
            editMode: {
                type: String,
                required: true
            },
            chosenEntryForDetail: {
                type: Object,
                default: () => {return null}
            }
        },
        watch: {
            chosenEntryForDetail: {
                handler: function (val, oldVal) {
                    if (this.chosenEntryForDetail === null) return
                    console.log('updated', this.chosenEntryForDetail.entryID)
                    this.form = Object.assign(this.form, val)
                    this.tableData = val.products
                },
                immediate: true
            }
        },
        beforeMount() {
            switch (this.editMode) {
                case 'entry':
                case 'purchase_entry':
                    this.$getRequest(this.$api.warehouseOptions).then((res) => {
                        console.log(res.data)
                        this.warehouseOptions = res.data
                        for (let item of this.warehouseOptions)
                            if (item.isDefault === 1)
                                this.form.warehouseID = item.id
                    }).catch((error) => this.$ajaxErrorHandler(error))

                    this.$getRequest(this.$api.departmentOptions).then((res) => {
                        console.log(res.data)
                        this.departmentOptions = res.data
                        for (let item of this.departmentOptions)
                            if (item.isDefault === 1)
                                this.form.departmentID = item.id
                    }).catch((error) => this.$ajaxErrorHandler(error))

                    this.enabledFields.warehouse = true
                    this.enabledFields.department = true
                    this.enabledFields.entryDate = true
                    this.enabledFields.expectedInvoiceType = true
                    this.enabledFields.company = true
                    this.enabledFields.shipping = true
                    this.enabledFields.remark = true

                    this.form.entryDate = new Date().format("yyyy-MM-dd").substr(0,10)
                    this.form.drawer = this.$store.getters.currentUser

                    if (this.editMode === 'entry') {
                        this.enabledFields.entrySave = true
                        this.form.entryType = '购入'
                        this.form.shippingCostType = '无运费'
                    }
                    else {
                        this.enabledFields.purchaseSave = true
                        this.form.entryType = '采订'
                    }
                    break
                case 'completion':
                    this.enabledFields.shipping = true
                    this.enabledFields.remark = true
                    this.enabledFields.completionSave = true
                    this.enabledFields.productsShow = true
                    break
                case 'modify':
                    this.$getRequest(this.$api.departmentOptions).then((res) => {
                        console.log(res.data)
                        this.departmentOptions = res.data
                        for (let item of this.departmentOptions)
                            if (item.isDefault === 1)
                                this.form.departmentID = item.id
                    }).catch((error) => this.$ajaxErrorHandler(error))

                    this.enabledFields.department = true
                    this.enabledFields.expectedInvoiceType = true
                    this.enabledFields.company = true
                    this.enabledFields.shipping = true
                    this.enabledFields.remark = true
                    this.enabledFields.remarkForModification = true
                    this.enabledFields.editSave = true
                    break
                case 'return':
                    this.enabledFields.returnSave = true
                    break
                case 'query':
                case 'purchase_query':
                    this.enabledFields.productsShow = true
                    break
            }
        },
        data() {
            return {
                fullSearchPanelOpen: false,
                relativeCompanySearchPanelOpen: false,
                modelSearchPanelOpen: false,
                purchaseOrderPanelOpen: false,

                enabledFields: {
                    warehouse: false,
                    department: false,
                    entryDate: false,
                    // entryType: true,
                    // drawer: true,
                    expectedInvoiceType: false,
                    company: false,
                    shipping: false, //relative company
                    // totalCost: true,
                    remark: false,
                    remarkForModification: false,
                    //todo
                    entrySave: false,
                    completionSave: false,
                    editSave: false,
                    returnSave: false,
                    purchaseSave: false,
                    productsShow: false
                },
                form: {
                    entryID: '',
                    serial: '',
                    warehouse: '',
                    warehouseID: -1,
                    department: '',
                    departmentID: -1,
                    entryDate: null,
                    entryType: '',
                    drawer: '',
                    expectedInvoiceType: '',
                    companyPhone: '',
                    companyAbbreviatedName: '',
                    companyFullName: '',
                    companyID: -1,
                    shippingMethod: '', //relative company
                    shippingMethodID: -1,
                    shippingNumber: '',
                    shippingQuantity: 0,
                    shippingCostType: '',
                    shippingCost: 0,
                    totalCost: '',
                    remark: '',
                    remarkForModification: '',
                    products: [],
                },
                rules: {
                    warehouseID: [v => !!v || '请选择仓库'],
                    departmentID: [v => !!v || '请选择部门'],
                    entryDate: [v => !!v || '请选择日期'],
                    expectedInvoiceType: [v => (!!v || this.enabledFields.purchaseSave) || ' 请选择单据类型'], //no need to validate if is purchase order
                    company: [v => !!v || '请选择单位'],
                    shippingMethodID: [v => (!!v || this.enabledFields.completionSave) || '请选择运输方式'],
                    shippingCostType: [v => (!!v || this.enabledFields.completionSave) || '请选择运费类型'],
                },

                warehouseOptions: [],
                departmentOptions: [],
                invoiceTypeOptions: [
                    {
                        value: '增值税票',
                        label: '增值税票'
                    }, {
                        value: '普票',
                        label: '普票'
                    }, {
                        value: '收据',
                        label: '收据'
                    }
                ],

                tableHeaders: [
                    {
                        text: '序号',
                        value: 'index',
                        width: '60px'
                    }, {
                        text: '新代号',
                        value: 'newModelCode',
                        width: '100px'
                    }, {
                        text: '旧代号',
                        value: 'oldModelCode',
                        width: '100px'
                    }, {
                        text: '厂牌',
                        value: 'factoryBrandCode',
                        width: '65px'
                    }, {
                        text: '入库数量',
                        value: 'quantity',
                        width: '85px'
                    }, {
                        text: '单位',
                        value: 'unitName',
                        width: '60px'
                    }, {
                        text: '含税单价',
                        value: 'unitPriceWithTax',
                        width: '100px'
                    }, {
                        text: '不含税单价',
                        value: 'unitPriceWithoutTax',
                        width: '100px'
                    }, {
                        text: '不含税金额',
                        value: '',
                        width: '100px'
                    }, {
                        text: '税率',
                        value: '',
                        width: '120px'
                    }, {
                        text: '税额',
                        value: '',
                        width: '120px'
                    }, {
                        text: '备注',
                        value: 'remark',
                        width: '120px'
                    }, {
                        text: '库存数量',
                        value: '',
                        width: '120px'
                    }, {
                        text: '库存单价',
                        value: '',
                        width: '120px'
                    }
                ],
                returnTableHeaders: [
                    {
                        text: '序号',
                        value: 'index',
                        width: '60px'
                    }, {
                        text: '新代号',
                        value: 'newModelCode',
                        width: '100px'
                    }, {
                        text: '旧代号',
                        value: 'oldModelCode',
                        width: '100px'
                    }, {
                        text: '厂牌',
                        value: 'factoryBrandCode',
                        width: '65px'
                    }, {
                        text: '原入库数',
                        value: 'quantity',
                        width: '85px'
                    }, {
                        text: '退货数',
                        value: 'returnQuantity',
                        width: '85px'
                    }, {
                        text: '现入库数',
                        value: '',
                        width: '85px'
                    }, {
                        text: '单位',
                        value: 'unitName',
                        width: '60px'
                    }, {
                        text: '含税单价',
                        value: 'unitPriceWithTax',
                        width: '100px'
                    }, {
                        text: '不含税单价',
                        value: 'unitPriceWithoutTax',
                        width: '100px'
                    }, {
                        text: '不含税金额',
                        value: '',
                        width: '100px'
                    }, {
                        text: '税率',
                        value: '',
                        width: '120px'
                    }, {
                        text: '税额',
                        value: '',
                        width: '120px'
                    }, {
                        text: '备注',
                        value: 'remark',
                        width: '120px'
                    }, {
                        text: '库存数量',
                        value: '',
                        width: '120px'
                    }, {
                        text: '库存单价',
                        value: '',
                        width: '120px'
                    }
                ],
                tableData: [],

                deleteTableRowPopup: false,
                tableRowsSelectedForDeletion: [],

                tax: 0,
                sumWithTax: 0,
                sumWithoutTax: 0,
            }
        },
        methods: {
            /*------- simple name/phone company search -------*/
            simpleSearchChooseAction(val) {
                if (val) {
                    this.form.companyAbbreviatedName = val.hasOwnProperty('companyAbbreviatedName') ?
                        val.companyAbbreviatedName : ''
                    this.form.companyFullName = val.hasOwnProperty('companyFullName') ?
                        val.companyFullName : ''
                    this.form.companyPhone = val.hasOwnProperty('phone') ?
                        val.phone : ''
                    this.form.companyID = val.hasOwnProperty('companyID') ?
                        val.companyID : -1
                }
            },
            /*------- full name company search -------*/
            fullSearchCloseAction() {
                this.fullSearchPanelOpen = false
            },
            fullSearchChooseAction(val) {
                this.form.companyAbbreviatedName = val.hasOwnProperty('companyAbbreviatedName') ?
                    val.companyAbbreviatedName : ''
                this.form.companyFullName = val.hasOwnProperty('companyFullName') ?
                    val.companyFullName : ''
                this.form.companyPhone = val.hasOwnProperty('phone') ?
                    val.phone : ''
                this.form.companyID = val.hasOwnProperty('companyID') ?
                    val.companyID : -1
                this.fullSearchPanelOpen = false
            },
            /*------- relative company search -------*/
            relativeCompanyCloseAction() {
                this.relativeCompanySearchPanelOpen = false
            },
            relativeCompanyChooseAction(val) {
                this.form.shippingMethod = val.hasOwnProperty('relativeCompanyAbbreviatedName') ?
                    val.relativeCompanyAbbreviatedName : ''
                this.form.shippingMethodID = val.hasOwnProperty('relativeCompanyID') ?
                    val.relativeCompanyID : -1
                this.relativeCompanySearchPanelOpen = false
            },
            /*------- purchase order query -------*/
            purchaseOrderCloseAction() {
                this.purchaseOrderPanelOpen = false
            },
            purchaseOrderChooseAction(val) {
                //todo
                this.tableData = val
                this.purchaseOrderPanelOpen = false
            },
            /*------- model search -------*/
            modelSearchCloseAction() {
                this.modelSearchPanelOpen = false
            },
            modelSearchChooseAction(val) {
                let max_id = this.tableData.length
                this.tableData.forEach(item => { // find max id in current tableData
                    max_id = item.id >= max_id ? item.id + 1 : max_id // add 1 to get new max value
                })
                //add attribute to the object
                // this id is used for v-data-table to uniquely identify a row
                val['id'] = max_id
                this.tableData.push(val)

                this.$store.commit('setSnackbar', {
                    message: '添加成功', color: 'success'
                })
            },
            /*------- table & entry submission -------*/
            handleDeleteRow() {
                this.deleteTableRowPopup = false
                if (this.tableRowsSelectedForDeletion.length !== 0) {
                    for (let item of this.tableRowsSelectedForDeletion)
                        this.tableData.splice(this.tableData.indexOf(item), 1)
                    this.tableRowsSelectedForDeletion = []
                }
                else {
                    this.$store.commit('setSnackbar', {
                        message: '未选中任何行', color: 'error'
                    })
                }
            },
            generateEntryID() {
                return new Promise((resolve) => {
                    this.$postRequest(this.$api.newEntryID, {
                        entryDate: this.form.entryDate,
                    }).then((res) => {
                        console.log('received', res.data)
                        resolve(res.data)
                    }).catch((error) => this.$ajaxErrorHandler(error))
                })
            },
            saveAsEntry(bool) {
                if (this.$refs.form.validate()) {
                    // generate entryID and serial
                    this.generateEntryID().then(entryID => {
                        console.log('entry id: ', entryID)
                        if (!entryID) return
                        this.form.entryID = entryID
                        this.form.serial = '购入' + entryID

                        //products data for transfer
                        for (let item of this.tableData) {
                            this.form.products.push({
                                //todo
                                stockID: item.stockID,
                                quantity: item.quantity,
                                warehouseID: this.form.warehouseID,
                                taxRate: item.taxRate,
                                unitPriceWithoutTax: item.unitPriceWithoutTax,
                                unitPriceWithTax: item.unitPriceWithTax,
                            })
                        }

                        this.$putRequest(this.$api.newInboundEntry, this.form).then((res) => {
                            this.$store.commit('setSnackbar', {
                                message: '提交成功', color: 'success'
                            })

                            if (bool) { // continue add without exit
                                for (let item of this.warehouseOptions)
                                    if (item.isDefault === 1) this.form.warehouse = item.name
                                for (let item of this.departmentOptions)
                                    if (item.isDefault === 1) this.form.department = item.name
                                this.form.entryType = ''
                                this.form.expectedInvoiceType = ''
                                this.form.companyID = -1
                                this.form.companyPhone = ''
                                this.form.companyAbbreviatedName = ''
                                this.form.companyFullName = ''
                                this.form.entryType = '购入'
                                this.form.shippingMethod = ''
                                this.form.shippingMethodID = -1
                                this.form.shippingNumber = ''
                                this.form.shippingQuantity = 0
                                this.form.shippingCostType = '无运费'
                                this.form.shippingCost = 0
                                this.form.totalCost = 0
                                this.form.remark = ''

                                this.tableData = []
                                this.tax = 0
                                this.sumWithoutTax = 0
                                this.sumWithTax = 0

                                this.tableRowsSelectedForDeletion = []
                            }
                            else {
                                this.$router.replace('/home')
                            }
                        })
                    })
                }
            },
            saveAsPurchaseOrder() {
                if (this.$refs.form.validate()) {
                    // generate entryID and serial
                    this.generateEntryID().then(entryID => {
                        console.log('entry id: ', entryID)
                        if (!entryID) return
                        this.form.entryID = entryID
                        this.form.serial = '采订' + entryID

                        //products data for transfer
                        for (let item of this.tableData) {
                            this.form.products.push({
                                //todo
                                stockID: item.stockID,
                                quantity: item.quantity,
                                warehouseID: this.form.warehouseID,
                                taxRate: item.taxRate,
                                unitPriceWithoutTax: item.unitPriceWithoutTax,
                                unitPriceWithTax: item.unitPriceWithTax,
                            })
                        }

                        this.$putRequest(this.$api.newInboundEntry, this.form).then((res) => {
                            this.$store.commit('setSnackbar', {
                                message: '提交成功', color: 'success'
                            })

                            this.$router.replace('/home')
                        })
                    })
                }
            },
            saveModification() {
                if (this.form.entryID === '') return
                const submissionForm = {}
                switch (this.editMode) {
                    case 'completion':
                        submissionForm.entryID = this.form.entryID
                        submissionForm.shippingMethodID = this.form.shippingMethodID
                        submissionForm.shippingQuantity = this.form.shippingQuantity
                        submissionForm.shippingCostType = this.form.shippingCostType
                        submissionForm.shippingNumber = this.form.shippingNumber
                        submissionForm.shippingCost = this.form.shippingCost
                        submissionForm.totalCost = this.form.totalCost
                        submissionForm.remark = this.form.remark
                        break
                    case 'modify':
                        submissionForm.products = []
                        for (let item of this.tableData) {
                            submissionForm.products.push({
                                entryProductID: item.hasOwnProperty('entryProductID') ?
                                    item.entryProductID : -1,
                                stockID: item.stockID,
                                quantity: item.quantity,
                                warehouseID: this.form.warehouseID,
                                taxRate: item.taxRate,
                                unitPriceWithoutTax: item.unitPriceWithoutTax,
                                unitPriceWithTax: item.unitPriceWithTax,
                            })
                        }
                        submissionForm.entryID = this.form.entryID
                        submissionForm.departmentID = this.form.departmentID
                        submissionForm.expectedInvoiceType = this.form.expectedInvoiceType
                        submissionForm.companyID = this.form.companyID
                        submissionForm.shippingMethodID = this.form.shippingMethodID
                        submissionForm.shippingQuantity = this.form.shippingQuantity
                        submissionForm.shippingCostType = this.form.shippingCostType
                        submissionForm.shippingNumber = this.form.shippingNumber
                        submissionForm.shippingCost = this.form.shippingCost
                        submissionForm.totalCost = this.form.totalCost
                        submissionForm.remark = this.form.remark
                        submissionForm.remarkForModification = this.form.remarkForModification
                        break
                    case 'return':
                        submissionForm.products = []
                        for (let item of this.tableData) {
                            if (item.returnQuantity !== 0) {
                                submissionForm.products.push({
                                    entryProductID: item.entryProductID,
                                    returnQuantity: item.returnQuantity
                                })
                            }
                        }
                        break
                }

                this.$patchRequest(this.$api.updateEntryInfo, submissionForm).then((res) => {
                    console.log()

                    this.$router.replace('/home')
                }).catch(error => this.$ajaxErrorHandler(error))
            },
            handleReturnQuantityChange(row) {
                //todo
            },
            /* ----- number calculation ----- */
            handleShippingQuantityChange() {
                this.form.shippingQuantity = this.form.shippingQuantity.toString().replace(/[^\d]/g, "")
            },
            handleShippingCostChange() {
                this.form.shippingCost = validateFloat(this.form.shippingCost.toString())
            },
            handleQuantityChange(row) {
                row.quantity = row.quantity.toString().replace(/[^\d]/g, "")

                let tempSumWithTax = 0, tempSumWithoutTax = 0
                this.tableData.forEach((item) => {
                    tempSumWithTax += item.unitPriceWithTax * item.quantity
                    tempSumWithoutTax += item.unitPriceWithoutTax * item.quantity
                })
                this.sumWithTax = tempSumWithTax.toFixed(2)
                this.sumWithoutTax = tempSumWithoutTax.toFixed(2)
                this.tax = (tempSumWithTax - tempSumWithoutTax).toFixed(2)
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
            }
        },
        computed: {
            rowDeletionConfirm() {
                let result = '确认删除以下序号的行: '
                this.tableRowsSelectedForDeletion.forEach(item => {
                    result += (this.tableData.indexOf(item) + 1).toString() + ' '
                })
                return result
            },
            totalSumPlusShippingCost() {
                let sum = parseFloat(this.sumWithTax)
                switch (this.form.shippingCostType) {
                    case "无运费":
                        break
                    case '自付运费':
                        break
                    case '代垫运费':
                        sum += parseFloat(this.form.shippingCost)
                        break
                }
                this.form.totalCost = sum
                return sum
            }
        }
    }
</script>

<style scoped>

</style>
