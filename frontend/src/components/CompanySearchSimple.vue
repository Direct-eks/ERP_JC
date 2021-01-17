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
                   :disabled="phoneSearchField === ''">
                单位检索
            </v-btn>
        </template>
        <v-card>
            <v-card-title>
                <v-toolbar dense flat>
                    <v-toolbar-title>重复单位检查</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon @click="simpleSearchClose">
                        <v-icon>{{mdiClosePath}}</v-icon>
                    </v-btn>
                </v-toolbar>
            </v-card-title>
            <v-card-text>
                <v-data-table v-model="simpleSearchCurrentRow"
                              :headers="simpleSearchTableHeaders"
                              :items="simpleSearchTable"
                              item-key="companyID"
                              @click:row="handleTableClick"
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
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" @click="simpleSearchChoose">
                    选择
                </v-btn>
            </v-card-actions>
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
            mdiClosePath: mdiClose,

            simpleSearchPanelOpen: false,
            simpleSearchTableHeaders: [
                {text: '单位简称', value: 'abbreviatedName', width: '220px'},
                {text: '电话', value: 'phone', width: '200px'},
                {text: '单位全称', value: 'fullName', width: '280px'},
                {text: '重要提示', value: 'remark'}
            ],
            simpleSearchCurrentRow: [],
            simpleSearchTable: [],
        }
    },
    methods: {
        simpleSearch() {
            this.$getRequest(this.$api.companyFuzzySearch, {
                phone: this.phoneSearchField,
                name: this.companyNameSearchField
            }).then((data) => {
                console.log('received', data)
                this.simpleSearchTable = data
                // open panel
                this.simpleSearchPanelOpen = true
            }).catch(() => {})
        },
        handleTableClick(val) {
            this.simpleSearchCurrentRow = [val]
        },
        simpleSearchClose() {
            this.$emit('simpleSearchChoose',null)
            this.simpleSearchPanelOpen = false
        },
        simpleSearchChoose() {
            this.$emit('simpleSearchChoose', this.simpleSearchCurrentRow[0])
            this.simpleSearchPanelOpen = false
        }
    }
}
</script>

<style scoped>

</style>
