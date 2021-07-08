<template>
<!--  <p>库存管理</p>-->
<!--  <p>库存报表</p>-->
    <v-card>
        <v-card-title class="d-flex">
            库存报表
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-row dense>
                <v-col cols="auto">
                    <v-text-field v-model="treeSelection.label"
                                  label="商品分类"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="max-width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
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
                <v-col cols="auto">
                    <v-text-field v-model="code"
                                  label="代号"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="max-width: 180px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="warehouseID"
                              :items="warehouseOptions"
                              item-value="warehouseID"
                              item-text="name"
                              label="仓库"
                              hide-details="auto"
                              outlined dense
                              style="width: 150px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="factoryBrand"
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
            <v-divider class="my-2"></v-divider>
            <v-card outlined>
                <v-data-table></v-data-table>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiArrowLeft } from '@mdi/js'

export default {
    name: "Stock_Report",
    components: {
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.$getRequest(this.$api.warehouseOptions).then((data) => {
            this.warehouseOptions = data
        }).catch(() => {})
    },
    data() {
        return {
            mdiArrowLeft,

            treeSelection: {label: '', categoryID: -1, children: []},
            modelPanel: false,

            code: '',
            warehouseID: -1,
            warehouseOptions: [],
            factoryBrand: '',
        }
    },
    methods: {
        clear() {
            this.treeSelection = {label: '', categoryID: -1, children: []}
            this.code = ''
            this.factoryBrand = ''
            this.warehouseID = -1
        },
        treeSelectionAction(val) {
            this.treeSelection = val
        },
    }
}
</script>

<style scoped>

</style>
