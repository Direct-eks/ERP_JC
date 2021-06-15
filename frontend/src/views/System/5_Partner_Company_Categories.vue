<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>往来单位区划</p>-->
    <v-card>
        <v-card-title class="d-flex">
            往来单位区划
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
                                item-key="areaID"
                                selectable
                                selection-type="independent"
                                activatable
                                return-object
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
                                  item-key="areaID"
                                  height="65vh"
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
                        <template v-if="canUpdate" v-slot:item.name="{ item }">
                            <v-edit-dialog :return-value="item.name">
                                {{item.name}}
                                <template v-slot:input>
                                    <v-text-field v-model="item.name" single-line/>
                                </template>
                            </v-edit-dialog>
                        </template>
                        <template v-if="canUpdate" v-slot:item.remark="{ item }">
                            <v-edit-dialog :return-value="item.remark">
                                {{item.remark}}
                                <template v-slot:input>
                                    <v-text-field v-model="item.remark" single-line/>
                                </template>
                            </v-edit-dialog>
                        </template>
                    </v-data-table>
                </v-responsive>
            </v-card>
            <v-row>
                <v-col cols="auto">
                    <v-row class="ml-1 mt-2">
                        <v-dialog v-if="canCreate"
                                  v-model="addNewDialog"
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
                        <v-btn v-if="canRemove" color="accent" class="ml-3"
                               :disabled="treeSelection.length === 0"
                               @click="removeItem">
                            删除
                        </v-btn>
                    </v-row>
                    <v-row class="ml-1 mt-5">
                        <v-btn v-if="canCreate" color="primary" class="ml-3"
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
    name: "PartnerCompanyCategories",
    beforeMount() {
        this.canCreate = this.$store.getters.currentUserIsPermitted("system:partnerCompanyCategories:create")
        this.canUpdate = this.$store.getters.currentUserIsPermitted("system:partnerCompanyCategories:update")
        this.canRemove = this.$store.getters.currentUserIsPermitted("system:partnerCompanyCategories:remove")

        this.$getRequest(this.$api.companyAreas).then((data) => {
            this.tableData = data
            this.treeData = this.$createTree(data, false)
            this.$store.commit('modifyCompanyList', this.treeData)
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            canCreate: false,
            canUpdate: false,
            canRemove: false,
            addNewDialog: false,
            newName: '',
            newRemark: '',

            treeData: [],
            treeSelection: [],

            headers: [
                { text: '区划名称', value: 'name', width: '160px' },
                { text: '所在层次', value: 'treeLevel', width: '100px' },
                { text: '区划描述', value: 'remark', width: '200px' },
            ],
            tableData: [],
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
                if (item.areaID === val.areaID) {
                    this.currRow = [val]
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
                console.log('new level: ', newLevel)
            }

            this.tableData.push({
                areaID: this.newItemIndex--,
                name: this.newName,
                remark: this.newRemark,
                treeLevel: newLevel
            })
        },
        previewChanges() {
            this.treeData = this.$createTree(this.tableData, false)
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
            this.$postRequest(this.$api.updateCompanyAreas, {
                elements: this.tableData
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })
                this.$router.replace('/system')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
