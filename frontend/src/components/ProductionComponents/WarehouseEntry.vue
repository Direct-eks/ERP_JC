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
                              :readonly="form.products.length !== 0 || !creationMode"
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
                              :readonly="displayMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <DatePicker :label="prefix + '日期'"
                                :disabled="!creationMode"
                                v-model="form.entryDate">
                    </DatePicker>
                </v-col>
                <v-spacer></v-spacer>
                <v-col cols="auto">
                    <v-text-field v-model="form.totalAmount"
                                  label="总金额"
                                  hide-details="auto"
                                  filled dense readonly
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
            </v-row>

            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="form.classification"
                                  :label="isInbound ? '入库类别' : '出库类别'"
                                  hide-details="auto"
                                  outlined readonly dense
                                  style="width: 100px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined readonly dense
                                  style="width: 120px">
                    </v-text-field>
                </v-col>
                <v-col>
                    <v-textarea v-model="form.remark"
                                label="备注"
                                hide-details="auto"
                                outlined dense auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>

        <v-row class="my-2" dense>
            <v-col v-if="creationMode">
                <v-dialog v-model="modelSearchPanelOpen"
                          persistent
                          scrollable
                          no-click-animation
                          max-width="85vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent" v-on="on"
                               :disabled="form.warehouseID === -1">
                            型号助选
                        </v-btn>
                    </template>
                    <ModelSearch :warehouseID="form.warehouseID"
                                 :isInbound="isInbound"
                                 @modelSearchClose="modelSearchCloseAction"
                                 @modelSearchChoose="modelSearchChooseAction">
                    </ModelSearch>
                </v-dialog>
            </v-col>
            <v-col v-if="!displayMode">
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
            <v-col v-if="creationMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="saveEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="modificationMode">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="saveChanges">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>

        <v-data-table v-model="tableCurrRows"
                      :headers="tableHeaders"
                      :items="form.products"
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
            <template v-if="!displayMode" v-slot:item.quantity="{ item }">
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
            <template v-if="!displayMode && enablePriceChange" v-slot:item.unitPrice="{ item }">
                <v-edit-dialog :return-value="item.unitPrice"
                               @save="handlePriceChange(item)"
                               @cancel="handlePriceChange(item)"
                               @close="handlePriceChange(item)">
                    {{ item.unitPrice }}
                    <template v-slot:input>
                        <v-text-field v-model="item.unitPrice" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-if="!displayMode" v-slot:item.remark="{ item }">
                <v-edit-dialog :return-value="item.remark">
                    {{ item.remark }}
                    <template v-slot:input>
                        <v-text-field v-model="item.remark" single-line
                                      @focus="$event.target.setSelectionRange(0, 100)"/>
                    </template>
                </v-edit-dialog>
            </template>
        </v-data-table>

    </v-container>
</template>

<script>
export default {
    name: "WarehouseEntry",
    components: {
        DatePicker: () => import('~/components/DatePicker'),
        ModelSearch: () => import("~/components/ProductionComponents/ModelSearch"),
    },
    beforeMount() {
        switch (this.editMode) {
        case 'creation':
            this.creationMode = true
            break
        case 'modification':
            this.modificationMode = true
            break
        case 'display':
            this.displayMode = true
            break
        }

        switch (this.type) {
        case 'materialApply':
        case 'inventoryLoss':
        case 'scrapEntry':
            this.isInbound = false
            this.enablePriceChange = false
            break
        case 'productionEntry':
        case 'inventoryProfit':
            this.isInbound = true
            this.enablePriceChange = true
            break
        }

        this.$store.dispatch('getWarehouseOptions')
        this.$store.dispatch('getDepartmentOptions')
    },
    props: {
        editMode: {
            type: String,
            required: true
        },
        type: {
            type: String,
            required: true
        },
        prefix: {
            type: String,
            required: true
        },
        paramForm: {
            type: Object,
            required: false,
        }
    },
    watch: {
        paramForm: {
            handler: function (val) {
                if (this.creationMode) return
                this.form = val
                this.handleTotalChange()
            },
            deep: true
        }
    },
    data() {
        return {
            creationMode: false,
            modificationMode: false,
            displayMode: false,

            isInbound: false,
            enablePriceChange: false,

            modelSearchPanelOpen: false,
            deleteTableRowPopup: false,
            submitPopup: false,
            submitPopup2: false,

            form: {
                entryDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                creationDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                totalAmount: '0',
                drawer: this.$store.getters.currentUser,
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: this.prefix,
                products: []
            },
            rules: {
                warehouseID: [v => v !== -1 || '请选择仓库'],
                departmentID: [v => v !== -1 || '请选择部门'],
            },

            tableHeaders: [
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: this.prefix + '数量', value: 'quantity', width: '90px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '单价', value: 'unitPrice', width: '100px' },
                { text: '金额', value: 'total', width: '100px' },
                { text: '备注', value: 'remark', width: '180px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
            ],
            tableCurrRows: [],

        }
    },
    computed: {
        warehouseOptions() {
            return this.$store.state.warehouseOptions
        },
        departmentOptions() {
            return this.$store.state.departmentOptions
        },
    },
    methods: {
        /* ------- model search -------*/
        modelSearchCloseAction() {
            this.modelSearchPanelOpen = false
        },
        modelSearchChooseAction(val) {
            for (const item of this.form.products) {
                if (item.skuID === val.skuID) {
                    this.$store.commit('setSnackbar', {
                        message: '已添加改商品', color: 'warning'
                    })
                    return
                }
            }
            let newVal = JSON.parse(JSON.stringify(val))
            this.form.products.push(newVal)

            this.$store.commit('setSnackbar', {
                message: '添加成功', color: 'success'
            })
            this.form.products.forEach(row => {
                this.handlePriceChange(row)
            })
        },
        /* ------- table select -------*/
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
            let tempSum = this.$Big('0')
            this.form.products.forEach(item => {
                const itemQuantity = this.$Big(item.quantity)
                tempSum = tempSum.add(itemQuantity.times(item.unitPrice))
            })
            this.form.totalAmount = tempSum.toString()
        },
        handleQuantityChange(item) {
            item.quantity = this.$validateNumber(item.quantity)
            const tempQuantity = this.$Big(item.quantity)
            item.total = tempQuantity.times(item.unitPrice).toString()
            this.handleTotalChange()
        },
        handlePriceChange(item) {
            item.unitPrice = this.$validateFloat(item.unitPrice)
            this.handleQuantityChange(item)
        },
        /* ------- table & entry submission -------*/
        handleDeleteRow() {
            this.deleteTableRowPopup = false
            if (this.tableCurrRows.length !== 0) {
                for (const item of this.tableCurrRows) {
                    this.form.products.splice(this.form.products.indexOf(item), 1)
                }
                this.tableCurrRows = []
            }
            else {
                this.$store.commit('setSnackbar', {
                    message: '未选中任何行', color: 'error'
                })
            }
        },
        saveEntry() {
            if (this.$refs.form.validate()) {
                let api = ''
                switch (this.type) {
                case 'materialApply':
                    api = this.$api.createMaterialApplyEntry
                    break
                case 'productionEntry':
                    api = this.$api.createProductionEntry
                    break
                case 'inventoryLoss':
                    api = this.$api.createInventoryLossEntry
                    break
                case 'inventoryProfit':
                    api = this.$api.createInventoryProfitEntry
                    break
                case 'scrapEntry':
                    api = this.$api.createScrapEntry
                    break
                }

                this.$putRequest(api, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$router.replace('/production_management')
                }).catch(() => {})
            }
            this.submitPopup = false
        },
        saveChanges() {
            if (this.$refs.form.validate()) {
                let api = ''
                switch (this.type) {
                case 'materialApply':
                    api = this.$api.modifyMaterialApplyEntry
                    break
                case 'productionEntry':
                    api = this.$api.modifyProductionEntry
                    break
                case 'inventoryLoss':
                    api = this.$api.modifyInventoryLossEntry
                    break
                case 'inventoryProfit':
                    api = this.$api.modifyInventoryProfitEntry
                    break
                case 'scrapEntry':
                    api = this.$api.modifyScrapEntry
                    break
                }

                this.$patchRequest(api, this.form).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '提交成功', color: 'success'
                    })
                    this.$router.replace('/production_management')
                }).catch(() => {})
            }
            this.submitPopup2 = false
        }
    }
}
</script>

<style scoped>

</style>
