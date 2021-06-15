<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>商品型号</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品型号
            <v-spacer></v-spacer>
            <v-text-field
                v-model="searchField"
                :append-icon="mdiMagnify"
                label="搜索"
                single-line
                hide-details/>
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
                                  :sort-by="['sequenceNumber', 'code']"
                                  :search="searchField"
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
                <v-row>
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
import { mdiArrowLeft, mdiChevronUp, mdiChevronDown, mdiMagnify } from "@mdi/js";

export default {
    name: "Models",
    beforeMount() {
        this.canCreate = this.$store.getters.currentUserIsPermitted("system:models:create")
        this.canUpdate = this.$store.getters.currentUserIsPermitted("system:models:update")
        this.canRemove = this.$store.getters.currentUserIsPermitted("system:models:remove")

        this.$getRequest(this.$api.allUnits).then(data => {
            this.units = data
        }).catch(() => {})

        this.$getRequest(this.$api.allFactoryBrands).then(data => {
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
            this.treeData = this.$createTree(data, true)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,
            mdiChevronUp,
            mdiChevronDown,
            mdiMagnify,

            canCreate: false,
            canUpdate: false,
            canRemove: false,

            addNewDialog: false,
            newCode: '',
            newUnitID: -1,

            treeData: [],
            treeLevelID: -1,

            modelTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '95px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '单位', value: 'unitID', width: '120px' },
            ],
            modelTableData: [],
            modelTableCurrentRow: [],
            newItemIndex: -1,

            searchField: '',

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

            if (data.length === 0) return;

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
            if (this.modelTableData.length === 0 || this.treeLevelID === -1) return
            this.addNewDialog = false
            this.modelTableData.push({
                modelID: this.newItemIndex--,
                sequenceNumber: this.modelTableData.length + 1,
                code: this.newCode,
                categoryID: this.treeLevelID,
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
