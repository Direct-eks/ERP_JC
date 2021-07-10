<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>商品型号</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品型号
            <v-spacer></v-spacer>
            <v-col cols="auto">
                <v-text-field v-model="modelSearchName"
                              label="代号搜索"
                              hide-details="auto"
                              clearable
                              style="width: 120px"
                              @keydown.enter.native="modelSearch">
                </v-text-field>
            </v-col>
            <v-col cols="auto">
                <v-radio-group v-model="modelSearchMethod"
                               hide-details="auto"
                               class="mt-0"
                               dense>
                    <v-radio label="前匹配" value="prefix"></v-radio>
                    <v-radio label="模糊" value="infix"></v-radio>
                </v-radio-group>
            </v-col>
            <v-col cols="auto">
                <v-btn color="accent"
                       @click="modelSearch">
                    查询
                </v-btn>
            </v-col>
            <v-spacer></v-spacer>
            <v-btn class="mr-3" color="primary"
                   @click="exportExcel">
                导出Excel
            </v-btn>
            <v-btn class="mr-3" color="success"
                   :disabled="!enableModification"
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
                    <ModelTree height="80vh" max-width=""
                               :select-for-level="true"
                               @treeSelectionResult="treeSelectionResult"
                               @treeSelectionObject="treeSelectionObject">
                    </ModelTree>
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
                                  :sort-by="['sequenceNumber', 'code']"
                                  disable-pagination
                                  single-select
                                  show-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                        <template v-if="canUpdate" v-slot:item.code="{ item }">
                            <v-edit-dialog :return-value="item.code"
                                           persistent large save-text="确认" cancel-text="取消">
                                {{ item.code }}
                                <template v-slot:input>
                                    <v-text-field v-model="item.code" single-line/>
                                </template>
                            </v-edit-dialog>
                        </template>
                        <template v-if="canUpdate" v-slot:item.unitID="{ item }">
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
                <v-row v-if="enableModification">
                    <v-col cols="auto">
                        <v-row class="ma-3">
                            <v-btn v-if="canUpdate" color="accent" fab small elevation="0" outlined
                                   @click="moveItem(true)">
                                <v-icon>{{ mdiChevronUp }}</v-icon>
                            </v-btn>
                            <v-dialog v-if="canCreate"
                                      v-model="addNewDialog"
                                      width="35vw">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-btn class="ml-3" color="accent"
                                           v-bind="attrs"
                                           v-on="on">
                                        新增
                                    </v-btn>
                                </template>
                                <v-card outlined>
                                    <v-card-text>
                                        <v-row class="my-2">
                                            <v-col cols="auto">
                                                <v-text-field v-model="newCode"
                                                              label="代号"
                                                              single-line
                                                              hide-details="auto"
                                                              outlined dense
                                                              style="width: 120px">
                                                </v-text-field>
                                            </v-col>
                                            <v-col cols="auto">
                                                <v-select v-model="newUnitID"
                                                          :items="units"
                                                          item-text="unitName"
                                                          item-value="unitID"
                                                          label="计量单位"
                                                          hide-details
                                                          outlined
                                                          dense
                                                          style="width: 120px">
                                                </v-select>
                                            </v-col>
                                            <v-col cols="auto" class="ml-2">
                                                <v-btn color="primary" @click="newRow">
                                                    确认
                                                </v-btn>
                                            </v-col>
                                        </v-row>
                                    </v-card-text>
                                </v-card>
                            </v-dialog>
                        </v-row>
                        <v-row class="ma-3">
                            <v-btn v-if="canUpdate" color="accent" fab small elevation="0" outlined
                                   @click="moveItem(false)">
                                <v-icon>{{ mdiChevronDown }}</v-icon>
                            </v-btn>
                            <v-btn v-if="canRemove" class="ml-3" color="accent"
                                   @click="removeItem">
                                删除
                            </v-btn>
                        </v-row>
                        <v-row class="ma-3">
                            <v-dialog v-if="canUpdate"
                                      v-model="changeCategoryPanel"
                                      persistent
                                      scrollable
                                      no-click-animation
                                      width="40vw">
                                <template v-slot:activator="{on}">
                                    <v-btn color="accent" v-on="on"
                                           :disabled="modelTableCurrentRow.length === 0">
                                        更改分类
                                    </v-btn>
                                </template>
                                <v-card>
                                    <v-card-text>
                                        <ModelTree height="60vh" max-width=""
                                                   :select-for-search="false"
                                                   :select-for-level="true"
                                                   @treeSelectionObject="changeCategorySelect">
                                        </ModelTree>
                                    </v-card-text>
                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="warning" @click="changeCategoryPanel = false">
                                            取消
                                        </v-btn>
                                        <v-btn color="accent" @click="changeCategory">
                                            确认并保存
                                        </v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
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
    </v-card>
</template>

<script>
import { mdiArrowLeft, mdiChevronUp, mdiChevronDown } from "@mdi/js";

export default {
    name: "Models",
    components: {
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.canCreate = this.$store.getters.currentUserIsPermitted("system:models:create")
        this.canUpdate = this.$store.getters.currentUserIsPermitted("system:models:update")
        this.canRemove = this.$store.getters.currentUserIsPermitted("system:models:remove")

        this.$store.dispatch('getMeasurementUnits')
        this.$store.dispatch('getFactoryBrands')
    },
    data() {
        return {
            mdiArrowLeft,
            mdiChevronUp,
            mdiChevronDown,

            canCreate: false,
            canUpdate: false,
            canRemove: false,

            addNewDialog: false,
            newCode: '',
            newUnitID: -1,
            changeCategoryPanel: false,
            changeCategorySelection: {label: '', categoryID: -1, children: []},

            treeLevelObject: {label: '', categoryID: -1, children: []},

            modelTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '95px' },
                { text: '分类', value: 'categoryCode', width: '110px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '单位', value: 'unitID', width: '120px' },
            ],
            modelTableData: [],
            modelTableCurrentRow: [],
            newItemIndex: -1,

            modelSearchName: '',
            modelSearchMethod: 'prefix',

            brandTableHeaders: [
                { text: '厂牌代号', value: 'code', width: '90px' },
            ],
            brandTableCurrentRow: [],

            enableModification: true,
        }
    },
    computed: {
        units() {
            return this.$store.state.measurementUnits
        },
        brandTableData() {
            return this.$store.state.factoryBrands
        }
    },
    methods: {
        modelSearch() {
            this.enableModification = false
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table

            this.$getRequest(this.$api.modelsByName, {
                name: this.modelSearchName,
                method: this.modelSearchMethod
            }).then((data) => {
                this.modelTableData = data
            }).catch(() => {})
        },
        treeSelectionResult(data) {
            this.modelTableData = data
            this.modelTableCurrentRow = [] //reset model table
        },
        treeSelectionObject(data) {
            this.treeLevelObject = data
        },
        modelTableChoose(row) {
            if (this.modelTableCurrentRow.indexOf(row) !== -1) {
                this.modelTableCurrentRow = []
            }
            else {
                this.modelTableCurrentRow = [row]
            }
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
            if (this.modelTableData.length === 0 || this.treeLevelObject.categoryID === -1) return
            this.addNewDialog = false
            this.modelTableData.push({
                modelID: this.newItemIndex--,
                sequenceNumber: this.modelTableData.length + 1,
                code: this.newCode,
                categoryID: this.treeLevelObject.categoryID,
                unitID: this.newUnitID
            })
            this.newCode = ''
            this.newUnitID = -1
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
        removeItem() {
            if (this.modelTableCurrentRow.length === 0) return
            // update sequence number
            const index = this.modelTableData.indexOf(this.modelTableCurrentRow[0])
            for (let i = index + 1; i < this.modelTableData.length; ++i) {
                this.modelTableData[i].sequenceNumber--
            }
            this.modelTableData.splice(index, 1)
        },
        changeCategory() {
            if (this.modelTableCurrentRow.length === 0) return
            if (this.changeCategorySelection.categoryID === -1) {
                this.$store.commit('setSnackbar', {
                    message: '未选择分类', color: 'error'
                })
                return
            }
            if (this.changeCategorySelection.children.length !== 0) {
                this.$store.commit('setSnackbar', {
                    message: '只能选择最末端分类', color: 'error'
                })
                return
            }
            this.changeCategoryPanel = false

            this.$postRequest(this.$api.updateCategoryOfModel, {}, {
                modelID: this.modelTableCurrentRow[0].modelID,
                oldCategoryID: this.modelTableCurrentRow[0].categoryID,
                newCategoryID: this.changeCategorySelection.categoryID
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '操作成功', color: 'success'
                })
                this.$store.commit('clearModelData')
                this.modelTableData.splice(this.modelTableData.indexOf(this.modelTableCurrentRow[0]), 1)
            }).catch(() => {})
        },
        changeCategorySelect(data) {
            this.changeCategorySelection = data
        },
        saveChanges() {
            if (this.modelTableData.length === 0 || this.treeLevelObject.categoryID === -1) return

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
                categoryID: this.treeLevelObject.categoryID,
                brands: brandsString
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/system')
            }).catch(() => {})
        },
        exportExcel() {
            this.$store.commit('setOverlay', true)
            this.$getFileRequest(this.$api.exportModels).then(data => {
                console.log("received!")
                this.$store.commit('setOverlay', false)
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
