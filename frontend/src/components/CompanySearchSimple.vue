<template>
    <v-dialog v-model="simpleSearchPanelOpen"
              persistent
              scrollable
              no-click-animation
              width="70vw">
        <template v-slot:activator="{on}">
            <v-btn color="accent"
                   v-on="on"
                   @click="simpleSearch"
                   :disabled="phoneSearchField === '' && companyNameSearchField === ''">
                单位检索
            </v-btn>
        </template>
        <v-card>
            <v-card-title>
                重复单位检查
                <v-spacer></v-spacer>
                <v-btn class="mr-3" color="primary" @click="simpleSearchChoose">
                    选择
                </v-btn>
                <v-btn icon @click="simpleSearchClose">
                    <v-icon>{{ mdiClose }}</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text>
                <v-data-table v-model="simpleSearchCurrentRow"
                              :headers="simpleSearchTableHeaders"
                              :items="simpleSearchTable"
                              item-key="companyID"
                              @click:row="tableClick"
                              @item-selected="tableClick2"
                              @dblclick:row="directChoose"
                              disable-sort
                              height="50vh"
                              disable-pagination
                              hide-default-footer
                              show-select
                              single-select
                              fixed-header
                              locale="zh-cn"
                              dense>
                </v-data-table>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "CompanySearchSimple",
    props: {
        phoneSearchField: {type: String, required: true, default: ''},
        companyNameSearchField: {type: String, required: false, default: ''},
        triggerSearchPanel: {type: Boolean, required: true, default: false}
    },
    watch: {
        triggerSearchPanel(val) {
            if (val === true) {
                this.simpleSearchPanelOpen = true
                this.simpleSearch();
            }
        }
    },
    data() {
        return {
            mdiClose,

            simpleSearchPanelOpen: false,
            simpleSearchTableHeaders: [
                {text: '单位简称', value: 'abbreviatedName', width: '200px'},
                {text: '电话', value: 'phone', width: '180px'},
                {text: '单位全称', value: 'fullName', width: '220px'},
                {text: '重要提示', value: 'remark', width: '250px'}
            ],
            simpleSearchCurrentRow: [],
            simpleSearchTable: [],
        }
    },
    methods: {
        simpleSearch() {
            if (this.phone === '' && this.name === '') return
            this.$getRequest(this.$api.companyFuzzySearch, {
                phone: this.phoneSearchField,
                name: this.companyNameSearchField
            }).then((data) => {
                this.simpleSearchTable = data
                // open panel
                this.simpleSearchPanelOpen = true
            }).catch(() => {})
        },
        tableClick(row) {
            if (this.simpleSearchCurrentRow.indexOf(row) === -1) {
                this.simpleSearchCurrentRow = [row]
            }
            else {
                this.simpleSearchCurrentRow = [row]
            }
        },
        tableClick2(row) {
            if (!row.value) {
                this.simpleSearchCurrentRow = []
            }
            else {
                this.simpleSearchCurrentRow = [row.item]
            }
        },
        simpleSearchClose() {
            this.$emit('simpleSearchChoose')
            this.simpleSearchPanelOpen = false
        },
        simpleSearchChoose() {
            if (this.simpleSearchCurrentRow.length === 0) {
                this.$store.commit('setSnackbar', {
                    message: '请选择要导入的单位', color: 'warning'
                })
                return
            }
            this.$emit('simpleSearchChoose', this.simpleSearchCurrentRow[0])
            this.simpleSearchPanelOpen = false
        },
        directChoose(_, row) {
            this.$emit('simpleSearchChoose', row.item)
            this.simpleSearchPanelOpen = false
        },
    }
}
</script>

<style scoped>

</style>
