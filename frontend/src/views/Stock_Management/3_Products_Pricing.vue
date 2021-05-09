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
        </v-card-title>

        <v-row>
            <v-col cols="auto" class="ml-3">
                <v-text-field v-model="treeSelectedLevel[0].name"
                              label="已选分类"
                              hide-details="auto"
                              outlined
                              readonly
                              dense
                              style="width: 180px">
                </v-text-field>
            </v-col>
            <v-col cols="auto">
                <v-text-field v-model="factoryBrandCurrentRow[0].code"
                              label="已选厂牌"
                              hide-details="auto"
                              outlined
                              readonly
                              dense
                              style="width: 120px">
                </v-text-field>
            </v-col>
            <v-col cols="auto">
                <v-text-field v-model="warehouseTableCurrentRow[0].name"
                              label="已选仓库"
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
                <v-responsive height="25vh"
                              style="overflow: auto">
                    <v-treeview :items="treeData"
                                item-text="label"
                                item-key="categoryID"
                                activatable
                                return-object
                                @update:active="treeSelect"
                                color="primary"
                                open-on-click
                                dense>
                    </v-treeview>
                </v-responsive>
            </v-card>

            <v-card outlined>
                <v-responsive height="25vh" width="20vw"
                              style="overflow: auto">
                    <v-data-table v-model="factoryBrandCurrentRow"
                                  :headers="factoryBrandTableHeaders"
                                  :items="factoryBrandTableData"
                                  item-key="factoryBrandID"
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

            <v-card outlined>
                <v-responsive height="25vh" width="18vw"
                              style="overflow: auto">
                    <v-data-table v-model="warehouseTableCurrentRow"
                                  :headers="warehouseTableHeaders"
                                  :items="warehouseTableData"
                                  item-key="warehouseID"
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

            <v-card outlined>
                <v-responsive height="25vh" width="30vw"
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

        <v-data-table
                      :headers="tableHeaders"
                      :items="tableData"
                      item-key="index"
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
            <template v-slot:item.storagePlace="{ item }">
                <v-select v-model="item.storagePlace"
                          :items="storagePlaceOptions"
                          hide-details="auto"
                          multiple
                          @change="saveChoice(item)"
                          dense
                          style="width: 150px">
                </v-select>
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
            treeSelectedLevel: [{name: ''}],

            factoryBrandTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '65px' },
                { text: '厂牌代号', value: 'code', width: '80px' },
                { text: '厂牌描述', value: 'remark', width: '200px' },
            ],
            factoryBrandTableData: [],
            factoryBrandCurrentRow: [{code: ''}],

            warehouseTableHeaders: [
                { text: '仓库名称', value: 'name', width: '100px' },
                { text: '位置', value: 'location', width: '80px' },
                { text: '默认', value: 'isDefault', width: '65px' },
            ],
            warehouseTableData: [],
            warehouseTableCurrentRow: [{name: ''}],

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

            storagePlaceOptions: ['a', 'b', 'c', 'd', 'e'],
            storagePlaceChoose: [],
            tableHeaders: [
                { text: '序号', value: 'index', width: '65px' },
                { text: '架位', value: 'storagePlace', width: '65px' },
                { text: '选择', value: 'choice', width: '65px' },
            ],
            tableData: [
                {storagePlace: [], choice: ''},
                {storagePlace: [], choice: ''},
                {storagePlace: [], choice: ''}
            ],
        }
    },
    methods: {
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = [] //reset stock table
            this.warehouseStockTableData = []
            this.warehouseStockCurrentRow = []

            let val = data[0]
            if (val.children.length === 0) { // end node
                console.log(val.categoryID)
                let result = this.$store.getters.models(val.categoryID)
                if (result) {
                    this.modelTableData = result
                    return
                }
                this.$getRequest(this.$api.modelsByCategory +
                    encodeURI(val.categoryID)).then((data) => {
                    console.log('received', data)
                    this.modelTableData = data
                    this.$store.commit('modifyModels', { key: val.categoryID, value: data })
                }).catch(() => {})
            }
        },

        clearSearchFields() {
            this.treeSelectedLevel = [{name: ''}]
            this.factoryBrandCurrentRow = [{code: ''}]
            this.warehouseTableCurrentRow = [{name: ''}]
        },

        //temp test method
        saveChoice(item) {
            console.log(item.storagePlace)
            item.choice = item.storagePlace.toString()
        },

        searchSku() {

        }
    }
}
</script>

<style scoped>

</style>
