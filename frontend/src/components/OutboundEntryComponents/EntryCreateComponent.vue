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
            <v-row dense>
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
                                  outlined
                                  dense
                                  style="width: 80px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row dense>
                <v-col cols="auto" v-if="outboundEntryMode">
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
                <v-col cols="auto" v-if="outboundEntryMode">
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
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalAmount"
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
                    <OutboundImportSalesOrder mode="salesOrder"
                                              :companyID="form.partnerCompanyID"
                                              @salesOrderChoose="salesOrderChooseAction">
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
                    <OutboundImportSalesOrder mode="quota"
                                              :companyID="form.partnerCompanyID"
                                              @quotaChoose="quotaChooseAction">
                    </OutboundImportSalesOrder>
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
            <v-col v-if="outboundEntryMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为出库单后新增</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsOutboundEntry(true)">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="outboundEntryMode">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为出库单</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsOutboundEntry(false)">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="salesOrderMode">
                <v-dialog v-model="submitPopup3" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为采购订单</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup3 = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsSalesOrder()">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="quotaEntryMode">
                <v-dialog v-model="submitPopup4" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">存为报价单</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup4 = false">取消</v-btn>
                            <v-btn color="success" @click="saveAsQuota()">确认</v-btn>
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
            <template v-slot:item.quantity="{ item }">
                <v-edit-dialog :return-value.sync="item.quantity"
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
                <v-edit-dialog :return-value.sync="item.unitPriceWithTax"
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
                <v-edit-dialog :return-value.sync="item.unitPriceWithoutTax"
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
                <v-edit-dialog :return-value.sync="item.remark">
                    {{item.remark}}
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
    name: "EntryCreateComponent",
    components: {
        CompanySearchSimple: () => import("~/components/CompanySearchSimple"),
        CompanySearch: () => import("~/components/CompanySearch"),
        ModelSearch: () => import(/* webpackChunkName: "OutboundModelSearchComponent" */
            "~/components/OutboundEntryComponents/ModelSearch"),
        RelativeCompanySearch: () => import("~/components/RelativeCompanySearch"),
        OutboundImportSalesOrder: () => import(/* webpackChunkName: "ImportOrderComponent" */
            "~/components/OutboundEntryComponents/ImportOrder")
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

        this.editPermitted = this.$store.getters.currentUserIsPermitted('outboundEntry:Creation:changePrice')

        this.$getRequest(this.$api.warehouseOptions).then((data) => {
            this.warehouseOptions = data
            for (const item of this.warehouseOptions) {
                if (item.isOutDefault === 1) {
                    this.form.warehouseID = item.warehouseID
                    break
                }
            }
        }).catch(() => {})

        this.$getRequest(this.$api.departmentOptions).then((data) => {
            this.departmentOptions = data
            for (const item of this.departmentOptions) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
        }).catch(() => {})

        this.$getRequest(this.$api.allTaxRates).then((data) => {
            for (const option of data) {
                this.taxRateOptions.push(Number(option))
            }
        }).catch(() => {})

        this.$getRequest(this.$api.allSuppliers).then(data => {
            this.suppliers = data
        })
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

            mdiPercentOutline,
            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),

            form: {
                shipmentDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                creationDate: new Date().format("yyyy-MM-dd").substr(0, 10),
                totalAmount: '0.0', deliveryMethod: '',
                invoiceType: '', taxRate: 0,
                drawer: this.$store.getters.currentUser,
                partnerCompanyID: -1, companyRemark: '',
                companyAbbreviatedName: '', companyPhone: '', companyFullName: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: '销出',
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
                shippingCost: [v => (this.form.shippingCostType !== '无' ? v !== '' : true) || '请填写运费' ],
                deliveryMethod: [v => !!v || '请选择提货方式']
            },

            warehouseOptions: [],
            departmentOptions: [],
            taxRateOptions: [],
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
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '出库数量', value: 'quantity', width: '90px' },
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
            submitPopup4: false,

            suppliers: [],
            editPermitted: false,

            tax: '0',
            sumWithTax: '0',
            sumWithoutTax: '0',
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
        /* ------- sales order / quota import -------*/
        salesOrderChooseAction(val) {
            // todo
            if (val) {
                this.tableData = JSON.parse(JSON.stringify(val))
                this.handleQuantityChange()
            }
            this.salesOrderPanelOpen = false
        },
        quotaChooseAction(val) {
            // todo
            if (val) {
                this.tableData = JSON.parse(JSON.stringify(val))
                this.handleQuantityChange()
            }
            this.quotaPanelOpen = false
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
            let tempSumWithTax = this.$Big('0')
            let tempSumWithoutTax = this.$Big('0')
            this.tableData.forEach((item) => {
                const itemQuantity = this.$Big(item.quantity)
                tempSumWithTax = tempSumWithTax.add(itemQuantity.times(item.unitPriceWithTax))
                tempSumWithoutTax = tempSumWithoutTax.add(itemQuantity.times(item.unitPriceWithoutTax))
            })
            this.sumWithTax = tempSumWithTax.toString()
            this.sumWithoutTax = tempSumWithoutTax.toString()
            this.tax = tempSumWithTax.minus(tempSumWithoutTax).toString()

            let sum = this.$Big(this.sumWithTax)
            switch (this.form.shippingCostType) {
            case '无':
            case '自付':
                break
            case '代垫':
                sum = sum.plus(this.form.shippingCost === '' ? '0' : this.form.shippingCost)
                break
            }
            this.form.totalAmount = sum.toString()
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
            } else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        saveAsOutboundEntry(bool) {
            if (this.$refs.form.validate()) {
                this.form.outboundProducts = this.tableData

                this.$store.commit('setOverlay', true)
                this.$putRequest(this.$api.createOutboundEntry, this.form).then(() => {
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
                        this.form.totalAmount = '0.0'
                        this.form.remark = ''
                        this.form.inboundProducts = []

                        this.tableData = []
                        this.tax = '0.0'
                        this.sumWithoutTax = '0.0'
                        this.sumWithTax = '0.0'
                        this.tableCurrRows = []
                    } else {
                        this.$router.replace('/outbound_management')
                    }
                }).catch(() => {})
                this.submitPopup = false
                this.submitPopup2 = false
            }
        },
        saveAsSalesOrder() {
            if (this.$refs.form.validate()) {
                this.form.salesOrderProducts = this.tableData

                this.$store.commit('setOverlay', true)
                this.$putRequest(this.$api.createSalesOrder, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/outbound_management')
                }).catch(() => {})
                this.submitPopup3 = false
            }
        },
        saveAsQuota() {
            if (this.$refs.form.validate()) {
                this.form.quotaProducts = this.tableData

                this.$store.commit('setOverlay', true)
                this.$putRequest(this.$api.createQuote, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$store.commit('setOverlay', false)

                    this.$router.replace('/outbound_management')
                }).catch(() => {})
                this.submitPopup4 = false
            }
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
        }
    }
}
</script>

<style scoped>

</style>
