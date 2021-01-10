<template>
    <v-container>
        <v-form ref="form">
            <v-row v-if="!quotaEntryMode">
                <v-col cols="auto">
                    <!--does not allow changes to be made after products are imported-->
                    <v-select v-model="form.warehouseID"
                              :rules="rules.warehouseID"
                              :items="warehouseOptions"
                              item-value="warehouseID"
                              item-text="name"
                              label="仓库"
                              hide-details="auto"
                              outlined dense
                              :readonly="tableData.length !== 0"
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
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-menu close-on-content-click
                            :nudge-right="40"
                            transition="scale-transition"
                            offset-y>
                        <template v-slot:activator="{on}">
                            <v-text-field v-model="form.shipmentDate"
                                          :rules="rules.shipmentDate"
                                          v-on="on"
                                          label="出库日期"
                                          hide-details="auto"
                                          outlined
                                          readonly
                                          dense>
                            </v-text-field>
                        </template>
                        <v-date-picker v-model="form.shipmentDate"
                                       no-title
                                       :max="allowedMaxDate"
                                       :first-day-of-week="0"
                                       locale="zh-cn">
                        </v-date-picker>
                    </v-menu>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto" v-if="outboundEntryMode">
                    <v-text-field v-model="form.classification"
                                  label="出库类别"
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
                    <v-select v-model="form.invoiceType"
                              :rules="rules.invoiceType"
                              :items="invoiceTypeOptions"
                              item-value="value"
                              item-text="label"
                              label="预计单据类型"
                              hide-details="auto"
                              outlined dense
                              style="width: 180px">
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
                <v-col cols="auto">
                    <CompanySearchSimple
                        :phoneSearchField="form.companyPhone"
                        :companyNameSearchField="form.companyAbbreviatedName"
                        :triggerSearchPanel="triggerSimpleSearch"
                        @simpleSearchChoose="simpleSearchChooseAction">
                    </CompanySearchSimple>
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
                <v-col cols="auto">
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
                            @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
            </v-row>

            <v-row v-if="outboundEntryMode">
                <v-col cols="auto">
                    <v-text-field v-model="form.relevantCompanyName"
                                  label="运输方式"
                                  hide-details="auto"
                                  :rules="rules.shippingMethodID"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
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
                            @relativeCompanyChoose="relativeCompanyChooseAction">
                        </RelativeCompanySearch>
                    </v-dialog>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingNumber"
                                  label="运单号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 220px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.shippingQuantity"
                                  label="件数"
                                  hide-details="auto"
                                  type="number"
                                  @change="handleShippingQuantityChange"
                                  outlined
                                  dense
                                  style="width: 80px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto" v-if="outboundEntryMode">
                    <v-radio-group v-model="form.shippingCostType"
                                   hide-details="auto"
                                   class="mt-0"
                                   row dense>
                        <v-radio label="无运费" value="无"></v-radio>
                        <v-radio label="自付运费" value="自付"></v-radio>
                        <v-radio label="代垫运费" value="代垫"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto" v-if="outboundEntryMode">
                    <v-text-field v-model.number="form.shippingCost"
                                  label="运费"
                                  @change="handleShippingCostChange"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto" v-if="outboundEntryMode">
                    <v-select v-model="form.deliveryMethod"
                              :rules="rules.deliveryMethod"
                              :items="deliveryMethodOptions"
                              item-value="value"
                              item-text="label"
                              label="提货方式"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
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
                                dense
                                auto-grow
                                rows="1"
                                counter="200">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row dense>
            <v-col v-if="outboundEntryMode">
                <v-dialog v-model="salesOrderPanelOpen"
                          :eager="true"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="salesOrderPanelOpen || form.partnerCompanyID === -1">
                            查询该单位销售订单
                        </v-btn>
                    </template>
                    <OutboundImportSalesOrder>
<!--                        :companyID="form.partnerCompanyID"-->
<!--                        @purchaseOrderChoose="purchaseOrderChooseAction">-->
                    </OutboundImportSalesOrder>
                </v-dialog>
            </v-col>
            <v-col v-if="outboundEntryMode">
                <v-dialog v-model="quotaPanelOpen"
                          :eager="true"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="quotaPanelOpen || form.partnerCompanyID === -1">
                            查询该单位报价单
                        </v-btn>
                    </template>
                    <OutboundImportSalesOrder>
                        <!--                        :companyID="form.partnerCompanyID"-->
                        <!--                        @purchaseOrderChoose="purchaseOrderChooseAction">-->
                    </OutboundImportSalesOrder>
                </v-dialog>
            </v-col>
            <v-col>
                <v-dialog v-model="modelSearchPanelOpen"
                          fullscreen
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="modelSearchPanelOpen || form.partnerCompanyID === -1">
                            型号助选
                        </v-btn>
                    </template>
                    <ModelSearch
                        :warehouseID="form.warehouseID"
                        @modelSearchClose="modelSearchCloseAction"
                        @modelSearchChoose="modelSearchChooseAction">
                    </ModelSearch>
                </v-dialog>
            </v-col>
            <v-col>
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
            <v-col v-if="outboundEntryMode">
                <v-btn color="primary"
                       @click="saveAsOutboundEntry(true)">
                    存为出库单后新增
                </v-btn>
            </v-col>
            <v-col v-if="outboundEntryMode">
                <v-btn color="primary"
                       @click="saveAsOutboundEntry(false)">
                    存为出库单
                </v-btn>
            </v-col>
            <v-col v-if="salesOrderMode">
                <v-btn color="primary"
                       @click="saveAsSalesOrder()">
                    存为采购订单
                </v-btn>
            </v-col>
            <v-col v-if="quotaEntryMode">
                <v-btn color="primary"
                       @click="saveAsQuota()">
                    存为报价单
                </v-btn>
            </v-col>
        </v-row>

        <v-data-table v-model="tableRowsSelectedForDeletion"
                      :headers="tableHeaders"
                      :items="tableData"
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
                                      counter="8">
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
            <template v-slot:item.remark="{ item }">
                <v-edit-dialog :return-value="item.remark"
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
    let val = value.replace(/[^\d.]/g, "") // 清除“数字”和“.”以外的字符
    val = val.replace(/\.{2,}/g, ".") // 只保留第一个. 清除多余的
    val = val.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".")
    val = val.replace(/^(-)*(\d+)\.(\d\d).*$/, '$1$2.$3') // 只能输入两个小数
    if (val.indexOf(".") < 0 && val !== "") { // 如果没有小数点，首位不能为0
        val = parseFloat(val)
    }
    console.log('float', val)
    return val
}

export default {
    name: "EntryCreateComponent",
    components: {
        CompanySearchSimple: () => import("~/components/CompanySearchSimple"),
        CompanySearch: () => import("~/components/CompanySearch"),
        ModelSearch: () => import("~/components/OutboundEntryComponents/ModelSearch"),
        RelativeCompanySearch: () => import("~/components/RelativeCompanySearch"),
        OutboundImportSalesOrder: () => import("~/components/OutboundEntryComponents/ImportSalesOrder")
    },
    props: {
        editMode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        switch (this.editMode) {
        case 'outboundEntry':
            this.outboundEntryMode = true
            break
        case 'salesOrder':
            this.salesOrderMode = true
            break
        case 'quotaEntry':
            this.quotaEntryMode = true
        }

        this.$getRequest(this.$api.warehouseOptions).then((res) => {
            console.log(res.data)
            this.warehouseOptions = res.data
            for (const item of this.warehouseOptions) {
                if (item.isDefault === 1) {
                    this.form.warehouseID = item.warehouseID
                    break
                }
            }
        }).catch((error) => this.$ajaxErrorHandler(error))

        this.$getRequest(this.$api.departmentOptions).then((res) => {
            console.log(res.data)
            this.departmentOptions = res.data
            for (const item of this.departmentOptions) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
        }).catch((error) => this.$ajaxErrorHandler(error))
    },
    data() {
        return {
            outboundEntryMode: false,
            salesOrderMode: false,
            quotaEntryMode: false,

            triggerSimpleSearch: false,
            fullSearchPanelOpen: false,
            relativeCompanySearchPanelOpen: false,
            modelSearchPanelOpen: false,
            salesOrderPanelOpen: false,
            quotaPanelOpen: false,

            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),

            form: {
                shipmentDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                totalAmount: 0.0, deliveryMethod: '', invoiceType: '',
                drawer: this.$store.getters.currentUser,
                partnerCompanyID: -1,
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: '购入',
                executionStatus: '执行',
                shippingCost: 0, shippingCostType: '无',
                shippingQuantity: 0, shippingNumber: '',
                shippingMethodID: -1, relevantCompanyName: '',
                outboundProducts: [],
                salesOrderProducts: [],
                quotaProducts: []
            },
            rules: {
                warehouseID: [v => !!v || '请选择仓库'],
                departmentID: [v => !!v || '请选择部门'],
                shipmentDate: [v => !!v || '请选择日期'],
                invoiceType: [v => !!v || ' 请选择单据类型'], // no need to validate if is purchase order
                company: [v => !!v || '请选择单位'],
                shippingCostType: [v => !!v || '请选择运费类型'],
                deliveryMethod: [v => !!v || '请选择提货方式']
            },

            warehouseOptions: [],
            departmentOptions: [],
            invoiceTypeOptions: [
                { value: '增值税票', label: '增值税票' },
                { value: '普票', label: '普票' },
                { value: '收据', label: '收据' }
            ],
            deliveryMethodOptions: [
                { value: '自提', label: '自提' },
                { value: '送货', label: '送货' },
                { value: '代办发货', label: '代办发货' },
                { value: '发货代收款', label: '发货代收款' }
            ],

            tableHeaders: [
                { text: '序号', value: 'index', width: '60px' },
                { text: '新代号', value: 'newCode', width: '100px' },
                { text: '旧代号', value: 'oldCode', width: '100px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '80px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '80px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '80px' },
                { text: '无税金额', value: 'totalWithoutTax', width: '80px' },
                { text: '税率', value: 'taxRate', width: '65px' },
                { text: '税额', value: 'totalTax', width: '80px' },
                { text: '备注', value: 'remark', width: '120px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
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
        /* ------- simple name/phone company search -------*/
        simpleSearchChooseAction(val) {
            if (val) {
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.companyFullName = val.fullName
                this.form.companyPhone = val.phone
                this.form.partnerCompanyID = val.companyID
            }
            this.triggerSimpleSearch = false // reset status
        },
        /* ------- full name company search -------*/
        fullSearchChooseAction(val) {
            if (val) {
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.companyFullName = val.fullName
                this.form.companyPhone = val.phone
                this.form.partnerCompanyID = val.companyID
            }
            this.fullSearchPanelOpen = false
        },
        /* ------- relative company search -------*/
        relativeCompanyChooseAction(val) {
            if (val) {
                this.form.relevantCompanyName = val.abbreviatedName
                this.form.shippingMethodID = val.companyID
            }
            this.relativeCompanySearchPanelOpen = false
        },
        /* ------- sales order import -------*/
        salesOrderChooseAction(val) {
            // todo
            if (val) {
                this.tableData = val
            }
            this.salesOrderPanelOpen = false
        },
        /* ------- model search -------*/
        modelSearchCloseAction() {
            this.modelSearchPanelOpen = false
        },
        modelSearchChooseAction(val) {
            for (const item of this.tableData) {
                if (item.skuID === val.skuID) {
                    this.$store.commit('setSnackbar', {
                        message: '已添加改商品', color: 'warning'
                    })
                    return
                }
            }
            this.tableData.push(val)

            this.$store.commit('setSnackbar', {
                message: '添加成功', color: 'success'
            })
        },
        /* ----- number calculation ----- */
        handleShippingQuantityChange() {
            this.form.shippingQuantity = this.form.shippingQuantity.toString().replace(/[^\d]/g, "")
        },
        handleShippingCostChange() {
            this.form.shippingCost = validateFloat(this.form.shippingCost.toString())
        },
        handleQuantityChange(row) {
            // calculate for each row
            row.quantity = row.quantity.toString().replace(/[^\d]/g, "")
            row.totalWithoutTax = (row.quantity * row.unitPriceWithoutTax).toFixed(2)
            row.totalTax = (row.quantity * row.unitPriceWithTax - row.totalWithoutTax).toFixed(2)

            let tempSumWithTax = 0
            let tempSumWithoutTax = 0
            this.tableData.forEach((item) => {
                // calculate for total
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
        },
        /* ------- table & entry submission -------*/
        handleDeleteRow() {
            this.deleteTableRowPopup = false
            if (this.tableRowsSelectedForDeletion.length !== 0) {
                for (const item of this.tableRowsSelectedForDeletion) {
                    this.tableData.splice(this.tableData.indexOf(item), 1)
                }
                this.tableRowsSelectedForDeletion = []
            } else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        saveAsOutboundEntry(bool) {

        },
        saveAsSalesOrder() {

        },
        saveAsQuota() {

        }
    },
    computed: {
        rowDeletionConfirm() {
            let result = '确认删除以下序号的行: '
            this.tableRowsSelectedForDeletion.forEach(item => {
                result += `${(this.tableData.indexOf(item) + 1).toString()} `
            })
            return result
        },
        totalSumPlusShippingCost() {
            let sum = parseFloat(this.sumWithTax)
            switch (this.form.shippingCostType) {
            case "无":
            case '自付':
                break
            case '代垫':
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
