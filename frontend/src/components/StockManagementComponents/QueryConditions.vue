<template>
    <v-row dense>
        <v-col v-if="queries.dateRange !== undefined" cols="auto">
            <DateRangePicker @chooseDate="chooseDateAction">
            </DateRangePicker>
        </v-col>
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
                      width="40vw">
                <template v-slot:activator="{on}">
                    <v-btn color="accent" v-on="on">选择分类</v-btn>
                </template>
                <v-card>
                    <v-card-text>
                        <ModelTree height="60vh" max-width=""
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
        <v-col v-if="queries.code !== undefined" cols="auto">
            <v-text-field v-model="queries.code"
                          label="代号"
                          hide-details="auto"
                          outlined
                          dense
                          style="max-width: 180px">
            </v-text-field>
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
        <v-col v-if="queries.factoryBrand !== undefined" cols="auto">
            <v-text-field v-model="queries.factoryBrand"
                          label="厂牌代号"
                          hide-details="auto"
                          outlined
                          dense
                          style="max-width: 100px">
            </v-text-field>
        </v-col>
        <v-spacer></v-spacer>
        <v-col cols="auto">
            <v-btn color="warning" @click="clear">清空</v-btn>
        </v-col>
    </v-row>
</template>

<script>
export default {
    name: "QueryConditions",
    components: {
        DateRangePicker: () => import("~/components/DateRangePicker"),
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.$store.dispatch('getWarehouseOptions')
    },
    props: {
        queries: {
            type: Object,
            required: true
        },
    },
    data() {
        return {
            modelPanel: false,
        }
    },
    computed: {
        warehouseOptions() {
            return this.$store.state.warehouseOptions
        }
    },
    methods: {
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
            if (this.queries.code !== undefined) this.queries.code = ''
            if (this.queries.factoryBrand !== undefined) this.queries.factoryBrand = ''
            if (this.queries.warehouseID !== undefined) this.queries.warehouseID = -1
        }
    }
}
</script>

<style scoped>

</style>
