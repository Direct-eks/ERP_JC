<template>
    <v-dialog v-model="simpleSearchPanelOpen"
              persistent
              scrollable
              no-click-animation
              width="60vw">
        <template v-slot:activator="{on}">
            <v-btn color="accent"
                   v-on="on"
                   @click="simpleSearch"
                   :disabled="simpleSearchField === ''">
                单位检索
            </v-btn>
        </template>
        <v-card>
            <v-card-title>
                <v-toolbar dense flat>
                    <v-toolbar-title>重复单位检查</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon @click="simpleSearchPanelOpen = false">
                        <v-icon>mdi-close</v-icon>
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
    export default {
        name: "CompanySearchSimple",
        props: {
            simpleSearchField: {
                type: String,
                required: true,
                default: ''
            },
            simpleSearchMethod: {
                type: String,
                required: true,
                default: ''
            }
        },
        data() {
            return {
                simpleSearchPanelOpen: false,
                simpleSearchTableHeaders: [
                    {
                        text: '单位简称',
                        value: 'companyAbbreviatedName'
                    }, {
                        text: '电话',
                        value: 'phone'
                    }, {
                        text: '单位全称',
                        value: 'companyFullName'
                    }, {
                        text: '重要提示',
                        value: 'remark'
                    }
                ],
                simpleSearchCurrentRow: [],
                simpleSearchTable: [],
            }
        },
        methods: {
            simpleSearch() {
                if (this.simpleSearchField !== '') {
                    if (this.simpleSearchMethod === 'phone') {
                        this.$postRequest(this.$api.companyByPhone,
                            {phone: this.simpleSearchField}).then((res) => {
                            console.log('received', res.data)
                            this.simpleSearchTable = res.data
                            // open panel
                            this.simpleSearchPanelOpen = true
                        }).catch(error => this.$ajaxErrorHandler(error))
                    }
                    else if (this.simpleSearchMethod === 'name') {
                        this.$postRequest(this.$api.companyByName,
                            {companyAbbreviatedName: this.simpleSearchField}).then((res) => {
                            console.log('received', res.data)
                            this.simpleSearchTable = res.data
                            // open panel
                            this.simpleSearchPanelOpen = true
                        }).catch(error => this.$ajaxErrorHandler(error))
                    }
                }
            },
            handleTableClick(val) {
                this.simpleSearchCurrentRow = [val]
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
