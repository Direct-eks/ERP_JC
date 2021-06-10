<template>
    <v-card>
        <v-card-title>
            商品型号选择
            <v-spacer></v-spacer>
            <v-text-field v-model="treeSelectedLevel"
                          label="已选分类"
                          hide-details="auto"
                          outlined
                          readonly
                          dense
                          class="ml-4"
                          style="max-width: 180px">
            </v-text-field>
            <v-text-field v-model="brandSelected"
                          label="已选厂牌"
                          hide-details="auto"
                          outlined
                          readonly
                          dense
                          class="ml-2"
                          style="max-width: 100px">
            </v-text-field>
            <v-spacer></v-spacer>
            <v-btn color="primary" :loading="isQuerying" @click="importSku">
                导入
            </v-btn>
            <v-btn class="ml-3" icon @click="closePanel">
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text class="d-flex">
            <v-responsive max-height="65vh" style="overflow: auto">
                <v-treeview v-model="treeSelection"
                            :items="treeData"
                            item-text="label"
                            item-key="categoryID"
                            return-object
                            activatable
                            selectable
                            @input="treeSelect"
                            @update:open="treeSelect"
                            @update:active="treeSelect"
                            selection-type="independent"
                            color="primary"
                            open-on-click
                            dense>
                </v-treeview>
            </v-responsive>
            <v-card outlined>
                <v-data-table v-model="brandTableCurrentRow"
                              :headers="brandTableHeaders"
                              :items="brandTableData"
                              item-key="factoryBrandID"
                              calculate-widths
                              height="65vh"
                              disable-sort
                              show-select
                              single-select
                              @click:row="brandTableChoose"
                              @item-selected="brandTableChoose2"
                              checkbox-color="accent"
                              fixed-header
                              disable-pagination
                              hide-default-footer
                              locale="zh-cn"
                              dense>
                </v-data-table>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiClose} from "@mdi/js";

export default {
    name: "BulkSkuSearch",
    beforeMount() {
        this.$getRequest(this.$api.allFactoryBrands).then(data => {
            this.brandTableData = data
        }).catch(() => {})

        let result = this.$store.getters.productList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.modelCategories).then((data) => {
            this.treeData = this.$createTree(data, true)
            this.$store.commit('modifyModelList', this.treeData)
        }).catch(() => {})
    },
    props: {
        supplierID: { type: Number, required: true, default: -1 }
    },
    data() {
        return {
            mdiClose,
            isQuerying: false,

            treeData: [],
            treeSelection: [],
            treeSelectedLevel: '',

            brandTableHeaders: [
                { text: '序号', value: 'sequenceNumber', width: '70px' },
                { text: '厂牌代号', value: 'code', width: '90px' },
                { text: '描述', value: 'remark', width: '180px' },
            ],
            brandTableData: [],
            brandTableCurrentRow: [],
            brandSelected: '',
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) return
            let val = data[data.length - 1]
            this.treeSelection = [val]
            this.treeSelectedLevel = val.label
        },
        brandTableChoose(row) {
            this.brandTableCurrentRow = [row]
            this.brandSelected = row.code
        },
        brandTableChoose2(row) {
            if (!row.value) {
                this.brandTableCurrentRow = []
                this.brandSelected = ''
            }
            else {
                this.brandTableCurrentRow = [row.item]
                this.brandSelected = row.item.code
            }
        },
        closePanel() {
            this.$emit('importSkus')
        },
        importSku() {
            if (this.treeSelection.length === 0 || this.brandSelected.length === 0) return
            if (this.treeSelection[0].children.length !== 0) return
            if (this.supplierID === -1) return
            this.isQuerying = true
            this.$getRequest(this.$api.resourcesByCategoryAndFactoryBrand, {
                modelCategoryID: this.treeSelection[0].categoryID,
                factoryBrandID: this.brandTableCurrentRow[0].factoryBrandID,
                supplierID: this.supplierID
            }).then((data) => {
                this.$store.commit('setSnackbar', {
                    message: '导入成功', color: 'success'
                })
                this.isQuerying = false
                this.$emit('importSkus', data)
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
