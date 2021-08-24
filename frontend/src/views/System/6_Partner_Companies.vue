<template>
    <!--  <p>系统标准</p>-->
    <!--  <p>往来单位</p>-->
    <v-card>
        <v-card-title>
            往来单位
            <v-spacer></v-spacer>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="filterField"
                              label="电话/简称 筛选"
                              hide-details="auto"
                              clearable>
                </v-text-field>
            </div>
            <v-spacer></v-spacer>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="phoneSearchField"
                              label="电话搜索"
                              hide-details="auto"
                              clearable
                              @keydown.enter.native="searchCompanies">
                </v-text-field>
            </div>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="nameSearchField"
                              label="简称搜索"
                              hide-details="auto"
                              clearable
                              @keydown.enter.native="searchCompanies">
                </v-text-field>
            </div>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/system">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>

        <v-card-text class="d-flex">
            <v-card outlined>
                <CompanyTree height="75vh" max-width="230px"
                             :select-for-level="true"
                             @treeSelectionObject="treeSelectionObject"
                             @treeSelectionResult="treeSelectionResult">
                </CompanyTree>
            </v-card>
            <v-card outlined>
                <v-responsive max-width="60vw">
                    <v-data-table v-model="currentRow"
                                  :headers="headers"
                                  :items="tableData"
                                  item-key="companyID"
                                  show-select
                                  single-select
                                  checkbox-color="accent"
                                  @click:row="tableClick"
                                  @item-selected="tableClick2"
                                  height="65vh"
                                  disable-sort
                                  disable-pagination
                                  hide-default-footer
                                  fixed-header
                                  :search="filterField"
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-responsive>
                <v-row class="mt-1">
                    <v-col cols="auto">
                        <v-btn color="primary" class="ml-3"
                               @click="modify(false)">
                            新增
                        </v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn color="accent" class="ml-3"
                               @click="modify(true)">
                            修改选中
                        </v-btn>
                    </v-col>
                    <v-col cols="auto">
                        <v-btn color="warning" class="ml-3"
                               @click="removeItem">
                            删除
                        </v-btn>
                    </v-col>
                    <v-spacer></v-spacer>
                    <v-col cols="auto">
                        <v-btn color="success" class="mr-3"
                               @click="saveChanges">
                            保存修改
                        </v-btn>
                    </v-col>
                </v-row>
                <v-dialog v-model="editPanelOpen" max-width="900px">
                    <v-card>
                        <v-card-title>公司信息</v-card-title>
                        <v-card-text class="d-flex flex-column">
                            <v-row>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.abbreviatedName"
                                                  label="简称" hide-details="auto"
                                                  :readonly="editIndex !== -1"
                                                  outlined dense
                                                  style="width: 220px"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.phone"
                                                  label="电话" hide-details="auto"
                                                  outlined dense
                                                  style="width: 180px"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.fax"
                                                  label="传真" hide-details="auto"
                                                  outlined dense
                                                  style="width: 180px"/>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col>
                                    <v-textarea v-model="companyData.remark"
                                                label="备注" hide-details="auto"
                                                outlined dense auto-grow rows="1"/>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col cols="auto">
                                    <v-select v-model="companyData.classification"
                                              :items="classificationOptions"
                                              label="分类" hide-details="auto"
                                              outlined dense
                                              style="width: 140px">
                                    </v-select>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.contactPerson"
                                                  label="联系人" hide-details="auto"
                                                  outlined dense
                                                  style="width: 100px"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.contactNumber"
                                                  label="联系人电话" hide-details="auto"
                                                  outlined dense
                                                  style="width: 130px"/>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.address"
                                                  label="地址" hide-details="auto"
                                                  outlined dense
                                                  style="width: 300px"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.zipcode"
                                                  label="邮编" hide-details="auto"
                                                  outlined dense
                                                  style="width: 100px"/>
                                </v-col>
                                <v-col cols="auto">
                                    <v-text-field v-model="companyData.fullName"
                                                  label="全称" hide-details="auto"
                                                  outlined dense
                                                  style="width: 300px"/>
                                </v-col>
                            </v-row>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="editPanelOpen = false">取消</v-btn>
                            <v-btn color="success" @click="saveEdit">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "PartnerCompanies",
    components: {
        CompanyTree: () => import('~/components/CompanyTree')
    },
    beforeMount() {
        this.defaultCompanyData = Object.assign({}, this.companyData)
    },
    data() {
        return {
            mdiArrowLeft,

            classificationOptions: [
                '客户', '供应商', '其他应收', '其他应付'
            ],
            filterField: '',
            nameSearchField: '',
            phoneSearchField: '',

            treeLevel: {label: '', categoryID: -1, children: []},

            editPanelOpen: false,
            editIndex: -1,
            companyData: {
                companyID: -1,
                sequenceNumber: 0,
                brandCode: '',
                abbreviatedName: '',
                phone: '',
                address: '',
                fax: '',
                classification: '',
                fullName: '',
                contactPerson: '',
                contactNumber: '',
                remark: '',
                isActive: 1,
                zipcode: '',
                email: '',
                website: ''
            },
            defaultCompanyData: {},
            newItemIndex: -1,

            headers: [
                { text: '单位简称', value: 'abbreviatedName', width: '120px' },
                { text: '电话', value: 'phone', width: '120px' },
                { text: '传真', value: 'fax', width: '120px', filterable: false },
                { text: '重要提示', value: 'remark', width: '120px', filterable: false },
                { text: '单位类别', value: 'classification', width: '120px', filterable: false },
                { text: '联系人', value: 'contactPerson', width: '120px', filterable: false },
                { text: '联系人电话', value: 'contactNumber', width: '120px', filterable: false },
                { text: '地址', value: 'address', width: '120px', filterable: false },
                { text: '邮政编码', value: 'zipcode', width: '120px', filterable: false },
                { text: '单位全称', value: 'fullName', width: '120px', filterable: false },
            ],
            tableData: [],
            currentRow: []
        }
    },
    methods: {
        searchCompanies() {
            this.currentRow = []
            if (this.nameSearchField === '' && this.phoneSearchField === '') return
            this.$getRequest(this.$api.companyFuzzySearch, {
                name: this.nameSearchField,
                phone: this.phoneSearchField,
            }).then((data) => {
                this.tableData = data
            }).catch(() => {})
        },
        tableClick(val) {
            if (this.currentRow.includes(val)) {
                this.currentRow = []
            }
            else {
                this.currentRow = [val]
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.currentRow = []
            }
            else {
                this.currentRow = [row.item]
            }
        },
        treeSelectionResult(data) {
            this.currentRow = []
            this.tableData = JSON.parse(JSON.stringify(data))
        },
        treeSelectionObject(data) {
            this.treeLevel = data
        },
        modify(isOld) {
            if (isOld) {
                if (this.currentRow.length === 0) {
                    this.$store.commit('setSnackbar', {
                        message: '未选中单位', color: 'warning'
                    })
                    return
                }
                this.editPanelOpen = true
                this.editIndex = this.tableData.indexOf(this.currentRow[0])
                Object.assign(this.companyData, this.currentRow[0])
            }
            else {
                if (this.treeLevel.categoryID === -1) {
                    this.$store.commit('setSnackbar', {
                        message: '未选中分类', color: 'warning'
                    })
                    return
                }
                this.editPanelOpen = true
                this.editIndex = -1
                Object.assign(this.companyData, this.defaultCompanyData)
                this.companyData.companyID = this.newItemIndex--
                this.companyData.areaID = this.treeLevel.categoryID
            }
        },
        saveEdit() {
            if (this.editIndex === -1 ) {
                this.tableData.push(Object.assign({}, this.companyData))
            }
            else {
                Object.assign(this.tableData[this.editIndex], this.companyData)
            }
            this.editPanelOpen = false
        },
        removeItem() {
            if (this.currentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '未选中单位', color: 'warning'
                })
                return
            }
            this.tableData.splice(this.tableData.indexOf(this.currentRow[0]), 1)
            this.currentRow = []
        },
        saveChanges() {
            if (this.treeLevel.categoryID === -1 || this.tableData.length === 0) return

            this.$postRequest(this.$api.updateCompaniesWithAreaID, {
                elements: this.tableData
            }, {
                areaID: this.treeLevel.categoryID
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '保存成功', color: 'success'
                })

                this.$store.commit('clearCompanyData')
                this.$router.replace('/system')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
