<template>
    <v-card>
        <v-card-title>
            商品型号选择
            <v-spacer></v-spacer>
            <v-text-field v-model="treeSelection.label"
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
            <ModelTree height="65vh" max-width=""
                       :select-for-search="false"
                       @treeSelectionObject="treeSelect">
            </ModelTree>
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
    components: {
        ModelTree: () => import('~/components/ModelTree'),
    },
    beforeMount() {
        this.$getRequest(this.$api.allFactoryBrands).then(data => {
            this.brandTableData = data
        }).catch(() => {})
    },
    props: {
        supplierID: { type: Number, required: true, default: -1 }
    },
    data() {
        return {
            mdiClose,
            isQuerying: false,

            treeSelection: {label: '', categoryID: -1, children: []},

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
            this.treeSelection = data
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
            if (this.treeSelection.label === '' || this.brandSelected.length === 0) return
            if (this.treeSelection.children.length !== 0) return
            if (this.supplierID === -1) return
            this.isQuerying = true
            this.$getRequest(this.$api.resourcesByCategoryAndFactoryBrand, {
                modelCategoryID: this.treeSelection.categoryID,
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
