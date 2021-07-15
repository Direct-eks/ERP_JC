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

            <v-row dense>
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
        
        </v-row>

    </v-container>
</template>

<script>
export default {
    name: "WarehouseEntry",
    components: {
        DatePicker: () => import('~/components/DatePicker')
    },
    beforeMount() {
        switch (this.mode) {
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

        this.$store.dispatch('getWarehouseOptions')
        this.$store.dispatch('getDepartmentOptions')
    },
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            creationMode: false,
            modificationMode: false,
            displayMode: false,

            form: {
                entryDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                creationDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                totalAmount: '0',
                drawer: this.$store.getters.currentUser,
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: '产入',
                products: []
            },
            rules: {
                warehouseID: [v => !!v || '请选择仓库'],
                departmentID: [v => !!v || '请选择部门'],
            },

            tableHeaders: [
                { text: '代号', value: 'code', width: '180px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '入库数量', value: 'quantity', width: '90px' },
                { text: '单位', value: 'unitName', width: '60px' },
                { text: '单价', value: 'unitPriceWithoutTax', width: '100px' },
                { text: '金额', value: 'totalWithoutTax', width: '100px' },
                { text: '备注', value: 'remark', width: '180px' },
                { text: '库存数量', value: 'stockQuantity', width: '120px' },
                { text: '库存单价', value: 'stockUnitPrice', width: '120px' }
            ],
            tableData: [],

            submitPopup: false,
            submitPopup2: false,
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

    }
}
</script>

<style scoped>

</style>
