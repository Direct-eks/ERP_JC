<template>
    <v-container>
        <v-form ref="form">
            <v-row>
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
                    <DatePicker label="入库日期"
                                :entryDate.sync="form.entryDate">
                    </DatePicker>
                </v-col>
            </v-row>

            <v-row>
                <v-col cols="auto">
                    <v-text-field v-model="form.classification"
                                  label="入库类别"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 120px">
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
                                  style="width: 200px"
                                  @keydown.enter="triggerSimpleSearch = true">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyAbbreviatedName"
                                  label="供货单位简称"
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
            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="form.companyFullName"
                                  label="供货单位全称"
                                  hide-details="auto"
                                  outlined
                                  readonly
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
                            <v-btn color="accent" v-on="on"
                                   :disabled="tableData.length !== 0">
                                单位助选
                            </v-btn>
                        </template>
                        <CompanySearch
                            @fullSearchChoose="fullSearchChooseAction">
                        </CompanySearch>
                    </v-dialog>
                </v-col>
            </v-row>

            <v-row v-if="form.partnerCompanyID !== -1 && form.companyRemark !== ''" dense>
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

            <v-row v-if="inboundEntryMode">
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
                                  outlined
                                  dense
                                  style="width: 80px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row dense>
                <v-col cols="auto" v-if="inboundEntryMode">
                    <v-radio-group v-model="form.shippingCostType"
                                   hide-details="auto"
                                   @change="handleTotalChange"
                                   class="mt-0"
                                   row dense>
                        <v-radio label="无运费" value="无"></v-radio>
                        <v-radio label="自付运费" value="自付"></v-radio>
                        <v-radio label="代垫运费" value="代垫"></v-radio>
                    </v-radio-group>
                </v-col>
                <v-col cols="auto" v-if="inboundEntryMode && form.shippingCostType !== '无'">
                    <v-text-field v-model.number="form.shippingCost"
                                  :rules="rules.shippingCost"
                                  label="运费"
                                  @change="handleTotalChange"
                                  hide-details="auto"
                                  type="number"
                                  outlined
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
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row class="my-2" dense>
            <v-col v-if="inboundEntryMode">
                <v-dialog v-model="purchaseOrderPanelOpen"
                          :eager="true"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent"
                               v-on="on"
                               :disabled="purchaseOrderPanelOpen || form.partnerCompanyID === -1">
                            查询该单位采购订单
                        </v-btn>
                    </template>
                    <InboundImportPurchaseEntry
                        :companyID="form.partnerCompanyID"
                        @purchaseOrderChoose="purchaseOrderChooseAction">
                    </InboundImportPurchaseEntry>
                </v-dialog>
            </v-col>
            <v-col>
                <v-dialog v-model="modelSearchPanelOpen"
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
                        :companyID="form.partnerCompanyID"
                        @modelSearchClose="modelSearchCloseAction"
                        @modelSearchChoose="modelSearchChooseAction">
                    </ModelSearch>
                </v-dialog>
            </v-col>
            <v-col>
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
            <v-col v-if="inboundEntryMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为入库单后新增</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsInboundEntry(true)">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="inboundEntryMode">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为入库单</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsInboundEntry(false)">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="purchaseOrderMode">
                <v-dialog v-model="submitPopup3" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为采购订单</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup3 = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsPurchaseOrder()">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>

        <v-data-table v-model="tableCurrRows"
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="skuID"
                      height="45vh"
                      calculate-widths
                      disable-sort
                      show-select
                      @click:row="tableSelect"
                      @item-selected="tableSelect2"
                      fixed-header
                      disable-pagination
                      hide-default-footer
                      locale="zh-cn">
            <template v-if="enableEditing" v-slot:item.quantity="{ item }">
                <v-edit-dialog :return-value="item.quantity"
                               @save="handleQuantityChange(item)"
                               @cancel="handleQuantityChange(item)"
                               @close="handleQuantityChange(item)">
                    {{ item.quantity }}
                    <template v-slot:input>
                        <v-text-field v-model="item.quantity" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-if="enableEditing" v-slot:item.unitPriceWithTax="{ item }">
                <v-edit-dialog :return-value="item.unitPriceWithTax"
                               @save="handlePriceWithTaxChange(item)"
                               @cancel="handlePriceWithTaxChange(item)"
                               @close="handlePriceWithTaxChange(item)">
                    {{ item.unitPriceWithTax }}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPriceWithTax" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-if="enableEditing" v-slot:item.unitPriceWithoutTax="{ item }">
                <v-edit-dialog :return-value="item.unitPriceWithoutTax"
                               @save="handlePriceWithoutTaxChange(item)"
                               @cancel="handlePriceWithoutTaxChange(item)"
                               @close="handlePriceWithoutTaxChange(item)">
                    {{ item.unitPriceWithoutTax }}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPriceWithoutTax" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.remark="{ item }">
                <v-edit-dialog :return-value="item.remark">
                    {{ item.remark }}
                    <template v-slot:input>
                        <v-text-field v-model="item.remark" single-line
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
import { mdiPercentOutline } from '@mdi/js'

export default {
    name: "InboundEntryAndPurchaseOrderComponent",
    components: {
        DatePicker: () => import(/* webpackChunkName: "CompanySearchSimpleComponent" */
            "~/components/DatePicker"),
        CompanySearchSimple: () => import(/* webpackChunkName: "CompanySearchSimpleComponent" */
            "~/components/CompanySearchSimple"),
        CompanySearch: () => import(/* webpackChunkName: "CompanySearchComponent" */
            "~/components/CompanySearch"),
        ModelSearch: () => import(/* webpackChunkName: "InboundModelSearchComponent" */
            "~/components/InboundEntryComponents/ModelSearch"),
        RelativeCompanySearch: () => import(/* webpackChunkName: "RelativeCompanySearchComponent" */
            "~/components/RelativeCompanySearch"),
        InboundImportPurchaseEntry: () => import(/* webpackChunkName: "ImportPurchaseEntryComponent" */
            "~/components/InboundEntryComponents/ImportPurchaseEntry")
    },
    props: {
        editMode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        switch (this.editMode) {
        case 'inboundEntry':
            this.inboundEntryMode = true
            break
        case 'purchaseOrder':
            this.purchaseOrderMode = true
            break
        }

        this.editPermitted = this.$store.getters.currentUserIsPermitted('inboundEntry:Creation:changePrice')

        this.$store.dispatch('getWarehouseOptions')
        this.$store.dispatch('getDepartmentOptions')
        this.$store.dispatch('getTaxRateOptions')
        this.$store.dispatch('getAllSuppliers')
    },
    data() {
        return {
            inboundEntryMode: false,
            purchaseOrderMode: false,

            triggerSimpleSearch: false,
            fullSearchPanelOpen: false,
            relativeCompanySearchPanelOpen: false,
            modelSearchPanelOpen: false,
            purchaseOrderPanelOpen: false,

            mdiPercentOutline,

            form: {
                entryDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                totalCost: '0.0', invoiceType: '', taxRate: 0,
                drawer: this.$store.getters.currentUser,
                partnerCompanyID: -1, companyRemark: '',
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: '购入',
                executionStatus: '执行',
                shippingCost: 0, shippingCostType: '无',
                shippingQuantity: 0, shippingNumber: '',
                shippingMethodID: -1, relevantCompanyName: '',
                inboundProducts: [],
                purchaseOrderProducts: []
            },
            rules: {
                warehouseID: [v => !!v || '请选择仓库'],
                departmentID: [v => !!v || '请选择部门'],
                taxRate: [v => !!v || '请选择税率'],
                invoiceType: [v => !!v || ' 请选择单据类型'], // no need to validate if is purchase order
                company: [v => !!v || '请选择单位'],
                shippingCostType: [v => !!v || '请选择运费类型'],
                shippingCost: [v => (this.form.shippingCostType !== '无' ? v !== '' : true) || '请填写运费' ]
            },

            tableHeaders: [
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '90px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '含税单价', value: 'unitPriceWithTax', width: '100px' },
                { text: '无税单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '无税金额', value: 'totalWithoutTax', width: '100px' },
                { text: '税率', value: 'taxRate', width: '65px' },
                { text: '税额', value: 'totalTax', width: '90px' },
                { text: '备注', value: 'remark', width: '180px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
            ],
            tableData: [],

            deleteTableRowPopup: false,
            tableCurrRows: [],
            submitPopup: false,
            submitPopup2: false,
            submitPopup3: false,

            suppliers: [],
            editPermitted: false,

            tax: '0',
            sumWithTax: '0',
            sumWithoutTax: '0',
        }
    },
    computed: {
        enableEditing() {
            // 如果是资源单位，则只有拥有改价权限才能修改价格
            for (const supplier of this.suppliers) {
                if (supplier.supplierID === this.form.partnerCompanyID) {
                    return this.editPermitted
                }
            }
            // 如果不是资源单位，所有人都可改价
            return true
        },
        warehouseOptions() {
            const options = this.$store.state.warehouseOptions
            for (const item of options) {
                if (item.isInDefault === 1) {
                    this.form.warehouseID = item.warehouseID
                    break
                }
            }
            return options
        },
        departmentOptions() {
            const options = this.$store.state.departmentOptions
            for (const item of options) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
            return options
        },
        taxRateOptions() {
            return this.$store.state.taxRateOptions
        },
        invoiceTypeOptions() {
            return this.$store.state.invoiceTypeOptions
        },
    },
    methods: {
        /* ------- simple name/phone company search -------*/
        simpleSearchChooseAction(val) {
            if (val) {
                this.form.companyAbbreviatedName = val.abbreviatedName
                this.form.companyFullName = val.fullName
                this.form.companyPhone = val.phone
                this.form.partnerCompanyID = val.companyID
                this.form.companyRemark = val.remark
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
                this.form.companyRemark = val.remark
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
        /* ------- purchase order import -------*/
        purchaseOrderChooseAction(val) {
            if (val) {
                const purchaseOrder = JSON.parse(JSON.stringify(val))
                // todo entryDate
                this.form.invoiceType = purchaseOrder.entry.invoiceType
                this.form.taxRate = purchaseOrder.entry.taxRate
                this.form.departmentID = purchaseOrder.entry.departmentID
                this.form.warehouseID = purchaseOrder.entry.warehouseID
                this.tableData = purchaseOrder.products
                this.tableData.forEach(p => {
                    this.handleQuantityChange(p)
                })
            }
            this.purchaseOrderPanelOpen = false
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
            let newVal = JSON.parse(JSON.stringify(val))
            newVal.taxRate = this.form.taxRate
            this.tableData.push(newVal)

            this.$store.commit('setSnackbar', {
                message: '添加成功', color: 'success'
            })
            this.tableData.forEach(row => {
                this.handlePriceWithoutTaxChange(row)
            })
        },
        tableSelect(row) {
            if (this.tableCurrRows.indexOf(row) !== -1) {
                this.tableCurrRows.splice(this.tableCurrRows.indexOf(row), 1)
            }
            else {
                this.tableCurrRows.push(row)
            }
        },
        tableSelect2(row) {
            if (!row.value) {
                this.tableCurrRows.splice(this.tableCurrRows.indexOf(row.item), 1)
            }
            else {
                this.tableCurrRows.push(row.item)
            }
        },
        /* ----- number calculation ----- */
        handleTotalChange() {
            // for all products
            let tempSumWithTax = this.$Big('0')
            let tempSumWithoutTax = this.$Big('0')
            this.tableData.forEach((item) => {
                // calculate for total
                const itemQuantity = this.$Big(item.quantity)
                tempSumWithTax = tempSumWithTax.add(itemQuantity.times(item.unitPriceWithTax))
                tempSumWithoutTax = tempSumWithoutTax.add(itemQuantity.times(item.unitPriceWithoutTax))
            })
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
            // calculate for each row
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
        changeTaxRate() {
            this.tableData.forEach(row => {
                row.taxRate = this.form.taxRate
                this.handlePriceWithoutTaxChange(row)
            })
        },
        /* ------- table & entry submission -------*/
        handleDeleteRow() {
            this.deleteTableRowPopup = false
            if (this.tableCurrRows.length !== 0) {
                for (const item of this.tableCurrRows) {
                    this.tableData.splice(this.tableData.indexOf(item), 1)
                }
                this.tableCurrRows = []
            }
            else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        saveAsInboundEntry(bool) {
            if (this.$refs.form.validate()) {
                // products data for transfer
                this.form.inboundProducts = this.tableData

                this.$store.commit('setOverlay', true)
                this.$putRequest(this.$api.createInboundEntry, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    if (bool) { // continue to add without exit, reset fields
                        this.form.shippingCost = '0.0'
                        this.form.shippingCostType = '无运费'
                        this.form.shippingQuantity = '0'
                        this.form.shippingNumber = ''
                        this.form.shippingMethodID = -1
                        this.form.relevantCompanyName = ''
                        this.form.totalCost = '0.0'
                        this.form.remark = ''
                        this.form.inboundProducts = []

                        this.tableData = []
                        this.tax = '0.0'
                        this.sumWithoutTax = '0.0'
                        this.sumWithTax = '0.0'
                        this.tableCurrRows = []
                    } else {
                        this.$router.replace('/inbound_management')
                    }
                }).catch(() => {})
            }
            this.submitPopup = false
            this.submitPopup2 = false
        },
        saveAsPurchaseOrder() {
            if (this.$refs.form.validate()) {
                // products data for transfer
                this.form.purchaseOrderProducts = this.tableData
                this.$store.commit('setOverlay', true)

                this.$putRequest(this.$api.createPurchaseOrder, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/inbound_management')
                }).catch(() => {})
            }
            this.submitPopup3 = false
        }
    },
}
</script>

<style scoped>

</style>
