<template>
    <v-card>
        <v-card-title>
            往来单位
            <v-spacer></v-spacer>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="phone"
                              label="电话"
                              hide-details="auto"
                              clearable
                              @keydown.enter.native="searchCompanies">
                </v-text-field>
            </div>
            <div class="mr-3" style="width: 160px">
                <v-text-field v-model="name"
                              label="简称"
                              hide-details="auto"
                              clearable
                              @keydown.enter.native="searchCompanies">
                </v-text-field>
            </div>
            <v-btn color="primary"
                   @click="searchCompanies">
                搜索
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   class="mr-6"
                   @click="chooseHandle">
                选择
            </v-btn>
            <v-btn icon @click="close">
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>

        <v-card-text class="d-flex">
            <CompanyTree height="65vh" max-width="230px"
                         @treeSelectionResult="treeSelect">
            </CompanyTree>
            <v-responsive max-width="60vw">
                <v-data-table v-model="currentRow"
                              :headers="headers"
                              :items="tableData"
                              item-key="companyID"
                              height="65vh"
                              calculate-widths
                              disable-sort
                              disable-pagination
                              hide-default-footer
                              show-select
                              single-select
                              @click:row="tableClick"
                              @item-selected="tableClick2"
                              @dblclick:row="directChoose"
                              checkbox-color="accent"
                              fixed-header
                              locale="zh-cn"
                              dense>
                </v-data-table>
            </v-responsive>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiClose } from '@mdi/js'

export default {
    name: "CompanySearch",
    components: {
        CompanyTree: () => import("~/components/CompanyTree")
    },
    data() {
        return {
            mdiClose,

            phone: '',
            name: '',

            headers: [
                { text: '单位简称', value: 'abbreviatedName', width: '200px' },
                { text: '电话', value: 'phone', width: '180px' },
                { text: '传真', value: 'fax', width: '180px' },
                { text: '重要提示', value: 'remark', width: '250px' },
                { text: '单位类别', value: 'classification', width: '90px' },
                { text: '联系人', value: 'contactPerson', width: '90px' },
                { text: '联系人电话', value: 'contactNumber', width: '120px' },
                { text: '地址', value: 'address', width: '220px' },
                { text: '邮政编码', value: 'zipcode', width: '90px' },
                { text: '单位全称', value: 'fullName', width: '220px' },
            ],
            tableData: [],
            currentRow: [],
        }
    },
    methods: {
        close() {
            this.$emit('fullSearchChoose')
        },
        treeSelect(data) {
            this.currentRow = []
            this.tableData = data
        },
        searchCompanies() {
            if (this.phone === '' && this.name === '') return
            this.$getRequest(this.$api.companyFuzzySearch, {
                name: this.name,
                phone: this.phone,
            }).then((data) => {
                this.tableData = data
            }).catch(() => {})
        },
        tableClick(row) {
            if (this.currentRow.indexOf(row) === -1) {
                this.currentRow = [row]
            }
            else {
                this.currentRow = []
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
        directChoose(_, row) {
            this.$emit('fullSearchChoose', row.item)
        },
        chooseHandle() {
            if (this.currentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '请选择要导入的单位', color: 'warning'
                })
                return
            }
            this.$emit('fullSearchChoose', this.currentRow[0])
        },
    }
}
</script>

<style scoped>
</style>
