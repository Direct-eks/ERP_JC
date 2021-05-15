<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>商品型号</p>-->
    <v-card>
        <v-card-title class="d-flex">
            商品型号
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/resources">
                <v-icon>{{ mdiArrowLeftPath }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <div class="d-flex">
                <v-card outlined>
                    <v-responsive height="65vh"
                                  style="overflow: auto">
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
                    </v-responsive>
                </v-card>
                <v-card outlined>
                    <v-data-table v-model="modelTableCurrentRow"
                                  :headers="modelTableHeaders"
                                  :items="modelTableData"
                                  item-key="modelID"
                                  @click:row="modelTableChoose"
                                  height="65vh"
                                  hide-default-footer
                                  calculate-widths
                                  disable-sort
                                  disable-pagination
                                  single-select
                                  fixed-header
                                  locale="zh-cn"
                                  dense>
                        <template v-slot:item.unitID="{ item }">
                            <v-select v-model="item.unitID"
                                      :items="units"
                                      item-text="unitName"
                                      item-value="unitID"
                                      hide-details="auto"
                                      dense
                                      style="width: 120px"/>
                        </template>
                    </v-data-table>
                </v-card>
            </div>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Models",
    beforeMount() {
        const creatTree = (data) => {
            const tree = [];
            for (let item of data) {
                if (item.treeLevel.length === 1) { //first level object
                    tree.push({label: item.code, children: [],
                        categoryID: item.modelCategoryID, treeLevel: item.treeLevel})
                }
            }
            console.log(tree)
            for (let index in tree) {
                creatTreeHelper(tree, index, data, tree[index].treeLevel, 1)
            }
            return tree
        }
        const creatTreeHelper = (tree, lastLevelIndex, data, prefix, depth) => {
            let count = 0;
            for (let item of data) {
                if (item.treeLevel.startsWith(prefix + '-') &&
                    item.treeLevel.length === depth * 2 + 1) {
                    tree[lastLevelIndex].children.push({
                        label: item.code, children: [],
                        categoryID: item.modelCategoryID, treeLevel: item.treeLevel
                    })
                    count++
                }
            }
            if (count === 0) return

            for (let index in tree[lastLevelIndex].children) {
                creatTreeHelper(tree[lastLevelIndex].children, index, data, tree[lastLevelIndex].children[index].treeLevel, depth + 1)
            }
        }

        let result = this.$store.getters.productList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.modelCategories).then((data) => {
            console.log('received', data)
            this.treeData = creatTree(data)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})

        // todo measurement units query
    },
    data() {
        return {
            mdiArrowLeftPath: mdiArrowLeft,

            treeData: [],

            modelTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '180px' },
                { text: '代号', value: 'code', width: '180px' },
                { text: '单位', value: 'unitID', width: '180px' },
                { text: '分类', value: 'categoryID', width: '180px' },
            ],
            modelTableData: [],
            modelTableCurrentRow: [],

            units: [
                {
                    unitID: 4,
                    unitName: '个'
                },
                {
                    unitID: 5,
                    unitName: '件'
                }
            ],
        }
    },
    methods: {
        treeSelect(data) {
            this.modelTableData = []
            this.modelTableCurrentRow = [] //reset model table

            let val = data[0]
            if (val.children.length === 0) { // end node
                console.log(val.categoryID)
                let result = this.$store.getters.models(val.categoryID)
                if (result) {
                    this.modelTableData = result
                    return
                }
                this.$getRequest(this.$api.modelsByCategory +
                    encodeURI(val.categoryID)).then((data) => {
                    console.log('received', data)
                    this.modelTableData = data
                    this.$store.commit('modifyModels', { key: val.categoryID, value: data })
                }).catch(() => {})
            }
        },
        modelTableChoose() {

        },
    }
}
</script>

<style scoped>

</style>
