<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>商品型号明细</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品型号明细
            <v-spacer></v-spacer>
            <v-radio-group v-model="choiceType"
                           hide-details="auto"
                           class="mt-0"
                           row dense
                           @change="modelTableCurrentRow = []; multiSkus = [];
                                    skuTableData = []; skuTableCurrentRow = [];">
                <v-radio label="单选" value="single"></v-radio>
                <v-radio label="多选" value="multi"></v-radio>
            </v-radio-group>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <div class="d-flex">
                <v-card outlined>
                    <v-responsive height="80vh"
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
                    <v-data-table v-model="modelTableCurrentRow"
                                  :headers="modelTableHeaders"
                                  :items="modelTableData"
                                  item-key="modelID"
                                  @click:row="modelTableChoose"
                                  @item-selected="modelTableChoose2"
                                  height="80vh"
                                  hide-default-footer
                                  calculate-widths
                                  disable-sort
                                  disable-pagination
                                  :single-select="choiceType === 'single'"
                                  show-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-card>
                <v-card outlined>
                    <v-data-table v-model="skuTableCurrentRow"
                                  :headers="skuTableHeaders"
                                  :items="skuTableData"
                                  item-key="skuID"
                                  calculate-widths
                                  height="80vh"
                                  disable-sort
                                  show-select
                                  @click:row="skuTableChoose"
                                  @item-selected="skuTableChoose2"
                                  fixed-header
                                  disable-pagination
                                  hide-default-footer
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-card>
                <v-row>
                    <v-col cols="auto">
                        <v-row class="ma-3">
                            <v-btn color="accent"
                                   @click="addBrands">
                                新增
                            </v-btn>
                        </v-row>
                        <v-row class="ma-3">
                            <v-btn color="accent"
                                   @click="">
                                删除
                            </v-btn>
                        </v-row>
                        <v-row class="ma-3">
                            <v-btn color="accent"
                                   @click="modifyBrand">
                                修改
                            </v-btn>
                        </v-row>
                        <v-row class="ma-3">
                            <v-btn color="success"
                                   @click="saveChanges">
                                保存
                            </v-btn>
                        </v-row>
                        <v-card outlined>
                            <v-card-subtitle>所有厂牌</v-card-subtitle>
                            <v-data-table v-model="brandTableCurrentRow"
                                          :headers="brandTableHeaders"
                                          :items="brandTableData"
                                          item-key="factoryBrandID"
                                          calculate-widths
                                          height="45vh"
                                          disable-sort
                                          show-select
                                          @click:row="brandTableChoose"
                                          @item-selected="brandTableChoose2"
                                          fixed-header
                                          disable-pagination
                                          hide-default-footer
                                          locale="zh-cn"
                                          dense>
                            </v-data-table>
                        </v-card>
                    </v-col>
                </v-row>
            </div>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Sku",
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

        this.$getRequest(this.$api.allFactoryBrands).then(data => {
            console.log('received', data)
            this.brandTableData = data
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
            mdiArrowLeft,
            choiceType: 'single',

            treeData: [],
            treeLevelID: -1,

            modelTableHeaders: [
                { text: '代号', value: 'code', width: '220px' },
            ],
            modelTableData: [],
            modelTableCurrentRow: [],

            skuTableHeaders: [
                { text: '厂牌代号', value: 'factoryCode', width: '100px' },
            ],
            skuTableData: [],
            skuTableCurrentRow: [],
            newItemIndex: -1,

            brandTableHeaders: [
                { text: '厂牌代号', value: 'code', width: '85px' },
            ],
            brandTableData: [],
            brandTableCurrentRow: [],
        }
    },
    methods: {
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table
            this.skuTableData = []
            this.skuTableCurrentRow = []

            let val = data[0]
            if (val.children.length === 0) { // end node
                console.log(val.categoryID)
                this.treeLevelID = val.categoryID
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
        modelTableChoose(row) {
            this.skuTableData = []
            this.skuTableCurrentRow = []

            if (this.choiceType === 'multi') {
                if (this.modelTableCurrentRow.indexOf(row) !== -1) {
                    this.modelTableCurrentRow.splice(this.modelTableCurrentRow.indexOf(row), 1)
                }
                else {
                    this.modelTableCurrentRow.push(row)
                }
                return
            }

            this.modelTableCurrentRow = [row]
            this.$getRequest(this.$api.fullSkuByModel +
                encodeURI(row.modelID)).then((data) => {
                console.log('received', data)
                this.skuTableData = data
            }).catch(() => {})
        },
        modelTableChoose2(row) {
            this.skuTableData = []
            this.skuTableCurrentRow = []

            if (!row.value) {
                this.modelTableCurrentRow.splice(this.modelTableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.modelTableCurrentRow.push(row.item)
            }
        },
        skuTableChoose(row) {
            if (this.skuTableCurrentRow.indexOf(row) !== -1) {
                this.skuTableCurrentRow.splice(this.skuTableCurrentRow.indexOf(row), 1)
            }
            else {
                this.skuTableCurrentRow.push(row)
            }
        },
        skuTableChoose2(row) {
            if (!row.value) {
                this.skuTableCurrentRow.splice(this.skuTableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.skuTableCurrentRow.push(row.item)
            }
        },
        brandTableChoose(row) {
            if (this.brandTableCurrentRow.indexOf(row) !== -1) {
                this.brandTableCurrentRow.splice(this.brandTableCurrentRow.indexOf(row), 1)
            }
            else {
                this.brandTableCurrentRow.push(row)
            }
        },
        brandTableChoose2(row) {
            if (!row.value) {
                this.brandTableCurrentRow.splice(this.brandTableCurrentRow.indexOf(row.item), 1)
            }
            else {
                this.brandTableCurrentRow.push(row.item)
            }
        },
        addBrands() {
            if (this.brandTableCurrentRow.length === 0) return

            for (const brand of this.brandTableCurrentRow) {
                let found = false
                for (const sku of this.skuTableData) {
                    if (brand.factoryBrandID === sku.factoryBrandID) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    this.skuTableData.push({
                        skuID: this.newItemIndex--,
                        factoryBrandID: brand.factoryBrandID,
                        modelID: this.modelTableCurrentRow[0].modelID,
                        unitID: this.modelTableCurrentRow[0].unitID,
                    })
                }
            }
        },
        modifyBrand() {
            if (this.skuTableData.length === 0) return
            if (this.skuTableCurrentRow.length === 0) return
            if (this.brandTableCurrentRow.length === 0) return

            this.skuTableCurrentRow[0].factoryBrandID = this.brandTableCurrentRow[0].factoryBrandID
            this.skuTableCurrentRow[0].factoryCode = this.brandTableCurrentRow[0].code
        },
        saveChanges() {
            if (this.choiceType === 'single') {
                this.$postRequest(this.$api.updateSku, {
                    elements: this.skuTableData
                }, {
                    modelID: this.modelTableCurrentRow[0].modelID
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '保存成功', color: 'success'
                    })
                }).catch(() => {})
            }
            else {
                if (this.modelTableCurrentRow.length === 0) return
                let modelIDArray = "" + this.modelTableCurrentRow[0].modelID
                this.modelTableCurrentRow.forEach(value => {
                    modelIDArray += "," + value.modelID
                })

                this.$postRequest(this.$api.updateSkuBulk, {
                    elements: this.skuTableData
                }, {
                    modelIDs: modelIDArray
                }).then(() => {
                    this.$store.commit('setSnackbar', {
                        message: '保存成功', color: 'success'
                    })
                }).catch(() => {})
            }
        }
    }
}
</script>

<style scoped>

</style>
