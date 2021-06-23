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
            <v-responsive height="75vh" max-width="230px" style="overflow: auto">
                <v-treeview :items="treeData"
                            item-text="label"
                            item-key="areaID"
                            activatable
                            return-object
                            @update:active="treeSelect"
                            color="primary"
                            open-on-click
                            dense>
                </v-treeview>
            </v-responsive>
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
                              height="75vh"
                              disable-sort
                              disable-pagination
                              hide-default-footer
                              fixed-header
                              :search="filterField"
                              locale="zh-cn"
                              dense>
                    <template v-slot:item.phone="{ item }">
                        <v-edit-dialog :return-value="item.phone">
                            {{item.phone}}
                            <template v-slot:input>
                                <v-text-field v-model="item.phone" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.fax="{ item }">
                        <v-edit-dialog :return-value="item.fax">
                            {{item.fax}}
                            <template v-slot:input>
                                <v-text-field v-model="item.fax" single-line
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
                    <template v-slot:item.classification="{ item }">
                        <v-select v-model="item.classification"
                                  :items="classificationOptions"
                                  hide-details="auto"
                                  dense>
                        </v-select>
                    </template>
                    <template v-slot:item.contactPerson="{ item }">
                        <v-edit-dialog :return-value="item.contactPerson">
                            {{item.contactPerson}}
                            <template v-slot:input>
                                <v-text-field v-model="item.contactPerson" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.contactNumber="{ item }">
                        <v-edit-dialog :return-value="item.contactNumber">
                            {{item.contactNumber}}
                            <template v-slot:input>
                                <v-text-field v-model="item.contactNumber" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.address="{ item }">
                        <v-edit-dialog :return-value="item.address">
                            {{item.address}}
                            <template v-slot:input>
                                <v-text-field v-model="item.address" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.zipcode="{ item }">
                        <v-edit-dialog :return-value="item.zipcode">
                            {{item.zipcode}}
                            <template v-slot:input>
                                <v-text-field v-model="item.zipcode" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                    <template v-slot:item.fullName="{ item }">
                        <v-edit-dialog :return-value="item.fullName">
                            {{item.fullName}}
                            <template v-slot:input>
                                <v-text-field v-model="item.fullName" single-line
                                              @focus="$event.target.setSelectionRange(0, 100)"/>
                            </template>
                        </v-edit-dialog>
                    </template>
                </v-data-table>
            </v-responsive>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "PartnerCompanies",
    beforeMount() {
        const result = this.$store.getters.companyCategoryList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.companyAreas).then((data) => {
            console.log('received', data)
            this.treeData = this.$createTree(data, false)
            this.$store.commit('modifyCompanyList', this.treeData)
        }).catch(() => {})
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
            currentRow: [],

            treeData: []
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
            if (this.currentRow.indexOf(val) !== -1) {
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
        treeSelect(data) {
            this.currentRow = []
            if (data.length === 0) return
            const val = data[0]
            if (val.children.length === 0) { // end node
                const result = this.$store.getters.companies(val.areaID)
                if (result) {
                    this.tableData = result
                    return
                }
                this.$getRequest(this.$api.companiesByAreaID
                    + encodeURI(val.areaID)).then((data) => {
                    this.tableData = data
                    this.$store.commit('modifyCompanies', { key: val.areaID, value: data })
                }).catch(() => {})
            }
        }
    }
}
</script>

<style scoped>

</style>
