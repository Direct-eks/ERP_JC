<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>商品型号</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品型号
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="exportExcel">
                导出Excel
            </v-btn>
            <v-btn class="mr-3" color="success"
                   @click="saveChanges">
                保存修改
            </v-btn>
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
                                  single-select
                                  show-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                        <template v-slot:item.code="{ item }">
                            <v-edit-dialog :return-value="item.code"
                                           persistent large save-text="确认" cancel-text="取消">
                                {{ item.code }}
                                <template v-slot:input>
                                    <v-text-field v-model="item.code" single-line/>
                                </template>
                            </v-edit-dialog>
                        </template>
                        <template v-slot:item.unitID="{ item }">
                            <v-select v-model="item.unitID"
                                      :items="units"
                                      item-text="unitName"
                                      item-value="unitID"
                                      hide-details="auto"
                                      dense
                                      style="width: 120px"/>
                        </template>
                    </v-data-table>
                </v-card>
                <v-row>
                    <v-col cols="auto">
                        <v-row class="ma-3">
                            <v-btn color="accent" fab small elevation="0" outlined
                                   @click="moveItem(true)">
                                <v-icon>{{ mdiChevronUp }}</v-icon>
                            </v-btn>
                            <v-btn class="ml-3" color="accent"
                                   @click="newRow">
                                新增
                            </v-btn>
                        </v-row>
                        <v-row class="ma-3">
                            <v-btn color="accent" fab small elevation="0" outlined
                                   @click="moveItem(false)">
                                <v-icon>{{ mdiChevronDown }}</v-icon>
                            </v-btn>
                            <v-btn class="ml-3" color="accent"
                                   @click="">
                                删除
                            </v-btn>
                        </v-row>
                        <v-card outlined>
                            <v-data-table v-model="brandTableCurrentRow"
                                          :headers="brandTableHeaders"
                                          :items="brandTableData"
                                          item-key="factoryBrandID"
                                          calculate-widths
                                          height="60vh"
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

        <v-overlay :value="overlay">
            <v-progress-circular indeterminate size="64" color="accent">
            </v-progress-circular>
        </v-overlay>
    </v-card>
</template>

<script>
import { mdiArrowLeft, mdiChevronUp, mdiChevronDown } from "@mdi/js";

export default {
    name: "Models",
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

        this.$getRequest(this.$api.allUnits).then(data => {
            console.log('received', data)
            this.units = data
        }).catch(() => {})

        this.$getRequest(this.$api.allFactoryBrands).then(data => {
            console.log('received', data)
            this.brandTableData = data
        }).catch(() => {})

        // clear cached model data
        this.$store.commit('clearModelData')

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
            mdiChevronUp,
            mdiChevronDown,

            treeData: [],
            treeLevelID: -1,

            modelTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '70px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '单位', value: 'unitID', width: '120px' },
            ],
            modelTableData: [],
            modelTableCurrentRow: [],
            newItemIndex: -1,

            units: [],

            brandTableHeaders: [
                { text: '厂牌代号', value: 'code', width: '90px' },
            ],
            brandTableData: [],
            brandTableCurrentRow: [],

            overlay: false,
        }
    },
    methods: {
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table

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
            this.modelTableCurrentRow = [row]
        },
        modelTableChoose2(row) {
            if (!row.value) {
                this.modelTableCurrentRow = []
            }
            else {
                this.modelTableCurrentRow = [row.item]
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
                this.brandTableCurrentRow.splice(this.brandTableCurrentRow.indexOf(row), 1)
            }
            else {
                this.brandTableCurrentRow.push(row.item)
            }
        },
        newRow() {
            this.modelTableData.push({
                modelID: this.newItemIndex--,
                sequenceNumber: this.modelTableData.length + 1,
                code: '',
                categoryID: this.treeLevelID,
                unitID: this.units[0].unitID
            })
        },
        moveItem(up) {
            if (this.modelTableData.length === 0) return
            let index = this.modelTableData.indexOf(this.modelTableCurrentRow[0])
            if (up) {
                if (index === 0) return
                let item = this.modelTableData[index]
                let item2 = this.modelTableData[index - 1]
                item.sequenceNumber = index
                item2.sequenceNumber = index + 1
                this.modelTableData[index - 1] =
                    this.modelTableData.splice(index, 1, this.modelTableData[index - 1])[0]
            }
            else {
                if (index === this.modelTableData.length - 1) return
                let item = this.modelTableData[index]
                let item2 = this.modelTableData[index + 1]
                item.sequenceNumber = index + 2
                item2.sequenceNumber = index + 1
                this.modelTableData[index + 1] =
                    this.modelTableData.splice(index, 1, this.modelTableData[index + 1])[0]
            }
        },
        saveChanges() {
            if (this.modelTableData.length === 0) return

            let brandsString = ''
            if (this.brandTableCurrentRow.length !== 0) {
                brandsString += this.brandTableCurrentRow[0].factoryBrandID
                this.brandTableCurrentRow.splice(0, 1)
                for (const b of this.brandTableCurrentRow) {
                    brandsString += "," + b.factoryBrandID
                }
            }

            this.$postRequest(this.$api.updateModelsWithCategory, {
                elements: this.modelTableData
            }, {
                categoryID: this.treeLevelID,
                brands: brandsString
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/system')
            }).catch(() => {})
        },
        exportExcel() {
            this.overlay = true
            this.$getFileRequest(this.$api.exportModels).then(data => {
                console.log("received!")
                this.overlay = false
                let href = window.URL.createObjectURL(new Blob([data]));
                let link = document.createElement('a');
                link.style.display = 'none';
                link.href = href;
                link.setAttribute('download',  '型号表.xlsx')
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(href)
            }).catch(()=>{})
        }
    }
}
</script>

<style scoped>

</style>
