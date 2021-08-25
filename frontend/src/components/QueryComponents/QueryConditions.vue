<template>
    <div> <!--remove when upgrading to v3-->
    <v-row dense>
        <v-col cols="auto">
            <v-text-field v-model="queries.companyName"
                          label="单位简称"
                          hide-details="auto"
                          outlined
                          dense
                          readonly
                          style="width: 200px">
            </v-text-field>
        </v-col>
        <v-col cols="auto">
            <v-dialog v-model="companySearchPanelOpen"
                      persistent
                      scrollable
                      no-click-animation
                      width="80vw">
                <template v-slot:activator="{on}">
                    <v-btn color="accent" v-on="on">
                        单位助选
                    </v-btn>
                </template>
                <CompanySearch :classificationToShow="queries.companyClassificationToShow"
                               @fullSearchChoose="companySearchChooseAction">
                </CompanySearch>
            </v-dialog>
        </v-col>
        <v-col v-if="queries.dateRange !== undefined" cols="auto">
            <DateRangePicker @chooseDate="chooseDateAction">
            </DateRangePicker>
        </v-col>
        <v-spacer></v-spacer>
        <v-col cols="auto">
            <v-btn color="warning" @click="clear">清空</v-btn>
        </v-col>
    </v-row>
    <v-row dense>
        <v-col v-if="queries.treeSelection !== undefined" cols="auto">
            <v-text-field v-model="queries.treeSelection.label"
                          label="商品分类"
                          hide-details="auto"
                          outlined
                          readonly
                          dense
                          style="max-width: 180px">
            </v-text-field>
        </v-col>
        <v-col v-if="queries.treeSelection !== undefined" cols="auto">
            <v-dialog v-model="modelPanel"
                      persistent
                      scrollable
                      no-click-animation
                      max-width="300px">
                <template v-slot:activator="{on}">
                    <v-btn color="accent" v-on="on">选择分类</v-btn>
                </template>
                <v-card>
                    <v-card-text>
                        <ModelTree height="40vh" max-width=""
                                   :select-for-search="false"
                                   :select-for-level="true"
                                   :show-select="true"
                                   @treeSelectionObject="treeSelectionAction">
                        </ModelTree>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="accent" @click="modelPanel = false">确认</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </v-col>
        <v-col v-if="queries.factoryBrand !== undefined" cols="auto">
            <v-text-field v-model="queries.factoryBrand"
                          label="厂牌代号"
                          hide-details="auto"
                          outlined
                          dense
                          style="max-width: 100px">
            </v-text-field>
        </v-col>
        <v-col v-if="queries.departmentID !== undefined" cols="auto">
            <v-select v-model="queries.departmentID"
                      :items="departmentOptions"
                      item-value="warehouseID"
                      item-text="name"
                      label="部门"
                      hide-details="auto"
                      outlined dense
                      style="width: 150px">
            </v-select>
        </v-col>
        <v-col v-if="queries.warehouseID !== undefined" cols="auto">
            <v-select v-model="queries.warehouseID"
                      :items="warehouseOptions"
                      item-value="warehouseID"
                      item-text="name"
                      label="仓库"
                      hide-details="auto"
                      outlined dense
                      style="width: 150px">
            </v-select>
        </v-col>
    </v-row>
    </div>
</template>

<script>
export default {
    name: "QueryConditions",
    components: {
        DateRangePicker: () => import("~/components/DateRangePicker"),
        CompanySearch: () => import("~/components/CompanySearch"),
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.$store.dispatch('getWarehouseOptions')
        this.$store.dispatch('getDepartmentOptions')
    },
    props: {
        queries: {
            type: Object,
            required: true
        },
    },
    data() {
        return {
            companySearchPanelOpen: false,
            modelPanel: false,
        }
    },
    computed: {
        warehouseOptions() {
            return this.$store.state.warehouseOptions
        },
        departmentOptions() {
            return this.$store.state.departmentOptions
        }
    },
    methods: {
        companySearchChooseAction(val) {
            if (val) {
                this.queries.companyName = val.abbreviatedName
                this.queries.companyID = val.companyID
            }
            this.companySearchPanelOpen = false
        },
        chooseDateAction(val) {
            if (val) {
                this.queries.dateRange = val
            }
        },
        treeSelectionAction(val) {
            this.queries.treeSelection = val
        },
        clear() {
            if (this.queries.treeSelection !== undefined) this.queries.treeSelection = {label: '', categoryID: -1, children: []}
            if (this.queries.departmentID !== undefined) this.queries.departmentID = -1
            if (this.queries.warehouseID !== undefined) this.queries.warehouseID = -1
            if (this.queries.factoryBrand !== undefined) this.queries.factoryBrand = ''
            this.$emit('clear')
        }
    }
}
</script>

<style scoped>

</style>
