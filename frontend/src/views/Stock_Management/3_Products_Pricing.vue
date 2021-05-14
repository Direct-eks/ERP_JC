<template>
<!--  <p>库存管理</p>-->
<!--  <p>商品定价</p>-->
    <v-card>
        <v-card-title>
            商品定价
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
            <v-col cols="auto">
                <v-btn color="success"
                       @click="saveChanges">
                    保存修改
                </v-btn>
            </v-col>
        </v-card-title>

        <v-row>
            <v-col cols="auto" class="ml-3">
                <v-text-field v-model="treeSelectedLevel"
                              label="已选分类"
                              hide-details="auto"
                              outlined
                              readonly
                              dense
                              style="width: 180px">
                </v-text-field>
            </v-col>
            <v-col cols="auto">
                <v-text-field v-model="factoryBrandSelected"
                              label="已选厂牌"
                              hide-details="auto"
                              outlined
                              readonly
                              dense
                              style="width: 120px">
                </v-text-field>
            </v-col>
            <v-col cols="auto">
                <v-btn color="warning"
                       @click="clearSearchFields">
                    清空
                </v-btn>
                <v-btn class="ml-2"
                       color="accent"
                       @click="searchSku">
                    查询
                </v-btn>
            </v-col>
        </v-row>

        <div class="d-flex">

            <v-card outlined>
                <v-responsive height="30vh"
                              style="overflow: auto">
                    <v-treeview v-model="treeSelection"
                                :items="treeData"
                                item-text="label"
                                item-key="categoryID"
                                return-object
                                selectable
                                activatable
                                @input="treeSelect"
                                @update:open="treeSelect"
                                @update:active="treeSelect"
                                selection-type="independent"
                                color="primary"
                                open-on-click
                                dense>
                    </v-treeview>
                </v-responsive>
            </v-card>

            <v-card outlined>
                <v-responsive height="30vh" width="20vw"
                              style="overflow: auto">
                    <v-data-table v-model="factoryBrandCurrentRow"
                                  :headers="factoryBrandTableHeaders"
                                  :items="factoryBrandTableData"
                                  item-key="factoryBrandID"
                                  height="30vh"
                                  calculate-widths
                                  disable-sort
                                  disable-pagination
                                  single-select
                                  @click:row="factoryBrandSelect"
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-responsive>
            </v-card>

            <v-card outlined>
                <v-responsive height="30vh" width="45vw"
                              style="overflow: auto">
                    <v-data-table v-model="supplierTableCurrentRow"
                                  :headers="supplierTableHeaders"
                                  :items="supplierTableData"
                                  item-key="partnerCompanyID"
                                  height="25vh"
                                  calculate-widths
                                  disable-sort
                                  single-select
                                  fixed-header
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-responsive>
            </v-card>

        </div>

        <v-data-table :headers="tableHeaders"
                      :items="tableData"
                      item-key="index"
                      calculate-widths
                      height="45vh"
                      disable-sort
                      fixed-header
                      locale="zh-cn"
                      :footer-props="{'items-per-page-options': [5,20,50]}">
            <template v-slot:item.index="{ item }">
                {{tableData.indexOf(item) + 1}}
            </template>
            <template v-slot:item.factoryPriceWithoutTax="{ item }">
                <v-edit-dialog :return-value="item.factoryPriceWithoutTax"
                               persistent large save-text="确认" cancel-text="取消"
                               @save="saveEditing(item)">
                    {{item.factoryPriceWithoutTax}}
                    <template v-slot:input>
                        <v-text-field v-model="item.factoryPriceWithoutTax" single-line/>
                    </template>
                </v-edit-dialog>
            </template>
            <template v-slot:item.priceBaseReference="{ item }">
                <v-select v-model="item.priceBaseReference" :items="priceBaseOptions"
                          hide-details="auto" dense style="width: 120px"
                          @change="saveEditing(item)"/>
            </template>
        </v-data-table>

    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Product_Pricing",
    beforeMount() {
        const creatTree = (data) => {
            const tree = [];
            for (let item of data) {
                if (item.treeLevel.length === 1) { //first level object
                    tree.push({label: item.code, children: [],
                        categoryID: item.modelCategoryID, treeLevel: item.treeLevel})
                }
            }
            console.log(tree)
            for (let index in tree) {
                creatTreeHelper(tree, index, data, tree[index].treeLevel, 1)
            }
            return tree
        }
        const creatTreeHelper = (tree, lastLevelIndex, data, prefix, depth) => {
            let count = 0;
            for (let item of data) {
                if (item.treeLevel.startsWith(prefix + '-') &&
                    item.treeLevel.length === depth * 2 + 1) {
                    tree[lastLevelIndex].children.push({
                        label: item.code, children: [],
                        categoryID: item.modelCategoryID, treeLevel: item.treeLevel
                    })
                    count++
                }
            }
            if (count === 0) return

            for (let index in tree[lastLevelIndex].children) {
                creatTreeHelper(tree[lastLevelIndex].children, index, data, tree[lastLevelIndex].children[index].treeLevel, depth + 1)
            }
        }

        this.$getRequest(this.$api.allFactoryBrands).then((data) => {
            console.log('received', data)
            this.factoryBrandTableData = data
        }).catch(() => {})

        let result = this.$store.getters.productList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.modelCategories).then((data) => {
            console.log('received', data)
            this.treeData = creatTree(data)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,

            treeData: [],
            treeSelection: [],
            treeSelectedLevel: '',

            factoryBrandTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '65px' },
                { text: '厂牌代号', value: 'code', width: '80px' },
                { text: '厂牌描述', value: 'remark', width: '100px' },
            ],
            factoryBrandTableData: [],
            factoryBrandCurrentRow: [],
            factoryBrandSelected: '',

            supplierTableHeaders: [
                { text: '供应商简称', value: 'companyAbbreviatedName', width: '100px' },
                { text: '厂牌', value: 'factoryBrand', width: '80px' },
                { text: '含税厂价', value: 'factoryPriceWithTax', width: '65px' },
                { text: '下浮率', value: 'floatDownRate', width: '100px' },
                { text: '无税结算价', value: 'settlementPriceWithoutTax', width: '80px' },
                { text: '数量', value: 'quantity', width: '65px' },
                { text: '无税厂价', value: 'factoryPriceWithoutTax', width: '65px' },
            ],
            supplierTableData: [],
            supplierTableCurrentRow: [],

            tableHeaders: [
                { text: '序号', value: 'index', width: '65px' },
                { text: '商品分类', value: '', width: '80px' },
                { text: '代号', value: 'code', width: '120px' },
                { text: '厂牌', value: 'factoryCode', width: '65px' },
                { text: '无税厂价', value: 'factoryPriceWithoutTax', width: '80px' },
                { text: '含税厂价', value: 'factoryPriceWithTax', width: '80px' },
                { text: '无税结算价', value: 'settlementPriceWithoutTax', width: '95px' },
                { text: '加价基准', value: 'priceBaseReference', width: '80px' },
                { text: '批加', value: 'wholesalePriceDiscount', width: '65px' },
                { text: '批发价', value: 'wholesalePrice', width: '80px' },
                { text: '零加', value: 'retailPriceDiscount', width: '65px' },
                { text: '零售价', value: 'retailPrice', width: '80px' },
                { text: '数量', value: 'stockQuantity', width: '65px' },
                { text: '下限', value: 'stockLowerLimit', width: '65px' },
                { text: '上限', value: 'stockUpperLimit', width: '65px' },
            ],
            tableData: [],
            modifiedTableData: [],

            priceBaseOptions: [
                '无税厂价',
                '含税厂价',
                '无税结算价'
            ]
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) return
            let val = data[data.length - 1]
            this.treeSelection = [val]
            this.treeSelectedLevel = val.label
        },
        factoryBrandSelect(data) {
            console.log(data)
            this.factoryBrandCurrentRow = [data]
            this.factoryBrandSelected = data.code
        },
        clearSearchFields() {
            this.treeSelection = []
            this.treeSelectedLevel = ''
            this.factoryBrandCurrentRow = []
            this.factoryBrandSelected = ''
        },

        searchSku() {
            this.$getRequest(this.$api.skuByCategoryAndFactoryBrand, {
                modelCategoryID: this.treeSelection[0].categoryID,
                factoryBrandID: this.factoryBrandCurrentRow.length === 0 ? -1 :
                    this.factoryBrandCurrentRow[0].factoryBrandID
            }).then((data) => {
                console.log('received', data)
                this.tableData = data
            }).catch(() => {})
        },
        saveEditing(item) {
            // todo validate fields
            if (this.modifiedTableData.indexOf(item) === -1) {
                this.modifiedTableData.push(item)
            }
            console.log(this.modifiedTableData)
        },
        saveChanges() {
            this.$postRequest(this.$api.modifySkuPricing, {
                elements: this.modifiedTableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/stock_management')
            }).catch(() => {})
        }
    },
}
</script>

<style scoped>

</style>
