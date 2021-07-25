<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>商品分类</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品分类
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text class="d-flex">
            <v-card outlined>
                <v-responsive height="80vh"
                              style="overflow: auto">
                    <v-treeview v-model="treeSelection"
                                :items="treeData"
                                item-text="label"
                                item-key="categoryID"
                                activatable
                                return-object
                                selectable
                                selection-type="independent"
                                @input="treeSelect"
                                @update:active="treeSelect"
                                @update:open="treeSelect"
                                color="primary"
                                open-on-click
                                dense>
                    </v-treeview>
                </v-responsive>
            </v-card>
            <v-card outlined>
                <v-responsive max-width="60vw"
                              style="overflow: auto">
                    <v-data-table v-model="currRow"
                                  :headers="headers"
                                  :items="tableData"
                                  item-key="modelCategoryID"
                                  height="80vh"
                                  sort-by="treeLevel"
                                  must-sort
                                  disable-pagination
                                  hide-default-footer
                                  checkbox-color="accent"
                                  show-select
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                        <template v-slot:item.code="{ item }">
                            <v-edit-dialog :return-value="item.code">
                                {{item.code}}
                                <template v-slot:input>
                                    <v-text-field v-model="item.code" single-line
                                                  @focus="$event.target.setSelectionRange(0, 100)"/>
                                </template>
                            </v-edit-dialog>
                        </template>
                        <template v-slot:item.name="{ item }">
                            <v-edit-dialog :return-value="item.name">
                                {{item.name}}
                                <template v-slot:input>
                                    <v-text-field v-model="item.name" single-line
                                                  @focus="$event.target.setSelectionRange(0, 100)"/>
                                </template>
                            </v-edit-dialog>
                        </template>
                        <template v-slot:item.remark="{ item }">
                            <v-edit-dialog :return-value="item.remark">
                                {{item.remark}}
                                <template v-slot:input>
                                    <v-text-field v-model="item.remark" single-line
                                                  @focus="$event.target.setSelectionRange(0, 100)"/>
                                </template>
                            </v-edit-dialog>
                        </template>
                    </v-data-table>
                </v-responsive>
            </v-card>
            <v-row>
                <v-col cols="auto">
                    <v-row class="ml-1 mt-2">
                        <v-dialog v-model="addNewDialog"
                                  width="35vw">
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn color="accent" class="ml-3"
                                       :disabled="treeSelection.length === 0"
                                       v-bind="attrs"
                                       v-on="on">
                                    在当前层级下新增
                                </v-btn>
                            </template>
                            <v-card outlined>
                                <v-card-text>
                                    <v-row class="mt-2">
                                        <v-col cols="auto">
                                            <v-text-field v-model="newCode"
                                                          label="分类名称"
                                                          single-line
                                                          hide-details="auto"
                                                          outlined dense
                                                          style="width: 110px">
                                            </v-text-field>
                                        </v-col>
                                        <v-col cols="auto">
                                            <v-text-field v-model="newName"
                                                          label="分类描述"
                                                          single-line
                                                          hide-details="auto"
                                                          outlined dense
                                                          style="width: 110px">
                                            </v-text-field>
                                        </v-col>
                                        <v-col cols="auto" class="ml-2">
                                            <v-btn color="primary" @click="addNewItem">
                                                确认
                                            </v-btn>
                                        </v-col>
                                    </v-row>
                                    <v-row class="mt-2">
                                        <v-col cols="auto">
                                            <v-text-field v-model="newRemark"
                                                          label="备注"
                                                          single-line
                                                          hide-details="auto"
                                                          outlined dense
                                                          style="width: 200px">
                                            </v-text-field>
                                        </v-col>
                                    </v-row>
                                </v-card-text>
                            </v-card>
                        </v-dialog>
                    </v-row>
                    <v-row class="ml-1 mt-5">
                        <v-btn color="accent" class="ml-3"
                               :disabled="treeSelection.length === 0"
                               @click="removeItem">
                            删除
                        </v-btn>
                    </v-row>
                    <v-row class="ml-1 mt-5">
                        <v-btn color="primary" class="ml-3"
                               @click="previewChanges">
                            预览
                        </v-btn>
                    </v-row>
                    <v-row class="ml-1 mt-5">
                        <v-btn color="primary" class="ml-3"
                               @click="saveChanges">
                            保存
                        </v-btn>
                    </v-row>
                </v-col>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiArrowLeft } from "@mdi/js";

export default {
    name: "ProductCategories",
    created() {
        this.$store.watch(state => state.modelCategoryList, () => {
            this.tableData = JSON.parse(JSON.stringify(this.$store.state.modelCategoryList))
            this.treeData = this.$createTree(this.tableData, true)
        })
    },
    beforeMount() {
        this.$store.dispatch('getModelCategoryList')
    },
    data() {
        return {
            mdiArrowLeft,

            addNewDialog: false,
            newCode: '',
            newName: '',
            newRemark: '',

            treeData: this.$createTree(JSON.parse(JSON.stringify(this.$store.state.modelCategoryList)), true),
            treeSelection: [],

            headers: [
                { text: '序号', value: 'sequenceNumber', width: '75px' },
                { text: '分类名称', value: 'code', width: '110px' },
                { text: '分类描述', value: 'name', width: '110px' },
                { text: '备注', value: 'remark', width: '120px' },
                { text: '所在层次', value: 'treeLevel', width: '110px' },
            ],
            tableData: JSON.parse(JSON.stringify(this.$store.state.modelCategoryList)),
            currRow: [],
            newItemIndex: -1
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) return
            let val = data[data.length - 1]
            this.treeSelection = [val]
            this.tableData.forEach(item => {
                if (item.modelCategoryID === val.categoryID) {
                    this.currRow = [item]
                }
            })
        },
        addNewItem() {
            if (this.treeSelection.length === 0) return
            this.addNewDialog = false
            const treeLevel = this.treeSelection[0].treeLevel
            let numberOfChildren = 0
            for (const item of this.tableData) {
                if (item.treeLevel.startsWith(treeLevel) &&
                    item.treeLevel.length === treeLevel.length + 2) {
                    numberOfChildren++
                }
            }
            const nextLevelNumber = numberOfChildren + 1
            let newLevel
            if (nextLevelNumber < 10) {
                newLevel = treeLevel + "-" + String(nextLevelNumber)
            }
            else {
                const endLevel = String.fromCharCode(String('a').charCodeAt(0) + nextLevelNumber - 10)
                newLevel = treeLevel + "-" + endLevel
                // console.log('new level: ', newLevel)
            }

            this.tableData.push({
                modelCategoryID: this.newItemIndex--,
                code: this.newCode,
                name: this.newName,
                remark: this.newRemark,
                treeLevel: newLevel
            })
        },
        previewChanges() {
            this.treeData = this.$createTree(this.tableData, true)
        },
        removeItem() {
            if (this.treeSelection.length === 0) return
            if (this.treeSelection[0].children.length !== 0) {
                this.$store.commit('setSnackbar', {
                    message: '只能删除末尾类别', color: 'warning'
                })
                return
            }
            const treeLevel = this.treeSelection[0].treeLevel
            for (const item of this.tableData) {
                if (item.treeLevel === treeLevel) {
                    this.tableData.splice(this.tableData.indexOf(item), 1)
                    break
                }
            }
        },
        saveChanges() {
            this.$postRequest(this.$api.updateModelCategories, {
                elements: this.tableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$store.commit('clearModelData')
                this.$router.replace('/system')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
