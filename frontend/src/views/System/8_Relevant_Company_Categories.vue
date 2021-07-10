<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>相关单位分类</p>-->
    <v-card>
        <v-card-title class="d-flex">
            相关单位分类
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text class="d-flex">
            <v-card outlined>
                <v-responsive height="65vh"
                              style="overflow: auto">
                    <v-treeview v-model="treeSelection"
                                :items="treeData"
                                item-text="label"
                                item-key="categoryID"
                                selectable
                                selection-type="independent"
                                activatable
                                return-object
                                @input="treeSelect"
                                @update:active="treeSelect"
                                @update:open="treeSelect"
                                color="primary"
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
                                  item-key="categoryID"
                                  height="65vh"
                                  disable-pagination
                                  hide-default-footer
                                  checkbox-color="accent"
                                  show-select
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
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
                                       v-bind="attrs"
                                       v-on="on">
                                    新增
                                </v-btn>
                            </template>
                            <v-card outlined>
                                <v-card-text>
                                    <v-row class="my-2">
                                        <v-col cols="auto">
                                            <v-text-field v-model="newName"
                                                          label="名称"
                                                          single-line
                                                          hide-details="auto"
                                                          outlined dense
                                                          style="width: 120px">
                                            </v-text-field>
                                        </v-col>
                                        <v-col cols="auto">
                                            <v-text-field v-model="newRemark"
                                                          label="描述"
                                                          single-line
                                                          hide-details="auto"
                                                          outlined dense
                                                          style="width: 120px">
                                            </v-text-field>
                                        </v-col>
                                        <v-col cols="auto" class="ml-2">
                                            <v-btn color="primary" @click="addNewItem">
                                                确认
                                            </v-btn>
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
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "RelevantCompanyCategories",
    created() {
        this.$store.watch(state => state.relevantCompanyCategoryList, () => {
            const data = this.$store.state.relevantCompanyCategoryList
            this.tableData = JSON.parse(JSON.stringify(data))
            this.treeData = this.$createOneLevelTree(data)
        })
    },
    beforeMount() {
        this.$store.dispatch('getRelevantCompanyCategoryList')
    },
    data() {
        return {
            mdiArrowLeft,

            addNewDialog: false,
            newName: '',
            newRemark: '',

            treeData: this.$createOneLevelTree(JSON.parse(JSON.stringify(this.$store.state.relevantCompanyCategoryList))),
            treeSelection: [],

            headers: [
                { text: '区划名称', value: 'name', width: '160px' },
                { text: '区划描述', value: 'remark', width: '200px' },
            ],
            tableData: JSON.parse(JSON.stringify(this.$store.state.relevantCompanyCategoryList)),
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
                if (item.categoryID === val.categoryID) {
                    this.currRow = [val]
                }
            })
        },
        addNewItem() {
            this.addNewDialog = false
            this.tableData.push({
                categoryID: this.newItemIndex--,
                name: this.newName,
                remark: this.newRemark,
            })
        },
        previewChanges() {
            this.treeData = this.$createOneLevelTree(this.tableData)
        },
        removeItem() {
            if (this.treeSelection.length === 0) return
            const treeLevel = this.treeSelection[0].categoryID
            for (const item of this.tableData) {
                if (item.categoryID === treeLevel) {
                    this.tableData.splice(this.tableData.indexOf(item), 1)
                    break
                }
            }
        },
        saveChanges() {
            this.$postRequest(this.$api.updateRelevantCompanyCategories, {
                elements: this.tableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })

                this.$store.commit('clearRelevantCompanyData')
                this.$router.replace("/system")
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
