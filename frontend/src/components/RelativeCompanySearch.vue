<template>
    <v-card>
        <v-card-title>
            相关单位
            <v-spacer></v-spacer>
            <v-text-field v-model="name"
                          label="搜索"
                          hide-details="auto"
                          clearable
                          style="width: 400px">
            </v-text-field>
            <v-spacer></v-spacer>
            <v-btn class="mr-8" color="primary" @click="chooseHandle">
                选择
            </v-btn>
            <v-btn icon @click="close">
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>

        <v-card-text>
            <v-row>
                <v-col cols="auto">
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
                </v-col>
                <v-divider vertical></v-divider>
                <v-col cols="auto" md="10">
                    <v-data-table v-model="currentRow"
                                  :headers="tableHeaders"
                                  :items="tableData"
                                  item-key="companyID"
                                  @click:row="handleTableClick"
                                  :search="name"
                                  disable-sort
                                  disable-pagination
                                  hide-default-footer
                                  height="55vh"
                                  show-select
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                    </v-data-table>
                </v-col>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "RelativeCompanySearch",
    beforeMount() {
        this.$store.dispatch('getRelevantCompanyCategoryList')
    },
    data() {
        return {
            mdiClose,

            tableHeaders: [
                {text: '单位简称', value: 'abbreviatedName', width: '120px'},
                {text: '电话', value: 'phone', width: '120px'},
                {text: '联系人', value: 'contactPerson', width: '120px', filterable: false},
                {text: '联系电话', value: 'contactNumber', width: '120px', filterable: false},
                {text: '地址', value: 'address', width: '120px', filterable: false},
                {text: '传真', value: 'fax', width: '120px', filterable: false}
            ],
            name: '',
            tableData: [],
            currentRow: []
        }
    },
    computed: {
        treeData() {
            const data = this.$store.state.relevantCompanyCategoryList
            return this.$createOneLevelTree(data)
        }
    },
    methods: {
        close() {
            this.$emit('relativeCompanyChoose', null)
        },
        chooseHandle() {
            if (this.currentRow.length !== 0)
                this.$emit('relativeCompanyChoose', this.currentRow[0])
        },
        handleTableClick(val) {
            this.currentRow = [val]
        },
        treeSelect(data) {
            let val = data[0]
            if (val.children.length === 0) {
                let result = this.$store.getters.relevantCompanies(val.categoryID)
                if (result) {
                    this.tableData = result
                    return
                }
                this.$getRequest(this.$api.relevantCompaniesByCategory +
                    encodeURI(val.categoryID)).then((data) => {
                    this.tableData = data
                    this.$store.commit('modifyRelevantCompanies', {key: val.categoryID, value: data})
                }).catch(() => {})
            }
        }
    } // end methods
}
</script>

<style scoped>

</style>
