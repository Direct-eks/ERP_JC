<template>
    <v-card outlined>
        <v-card-title>
            资源单位
            <v-spacer></v-spacer>
            <v-btn icon @click="close">
                <v-icon>{{mdiClosePath}}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="supplierTableCurrentRow"
                          :headers="supplierTableHeaders"
                          :items="supplierTableData"
                          item-key="supplierID"
                          @click:row="tableChoose"
                          @item-selected="tableChoose2"
                          show-select
                          checkbox-color="accent"
                          height="65vh"
                          hide-default-footer
                          calculate-widths
                          disable-sort
                          disable-pagination
                          single-select
                          fixed-header
                          locale="zh-cn"
                          dense>
            </v-data-table>
        </v-card-text>
        <v-card-actions>
            <v-btn v-if="enableEdit" color="warning"
                   @click="deleteHandle" :loading="btnLoading">
                删除选择单位所有资源
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn v-if="!enableEdit" color="info"
                   @click="chooseHandle" :loading="btnLoading">
                导入
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
import {mdiClose} from '@mdi/js'

export default {
    name: "SupplierResourceQuery",
    beforeMount() {
        this.$getRequest(this.$api.allSuppliers).then(data => {
            this.supplierTableData = data
        })
    },
    props: {
        enableEdit: { type: Boolean, default: false }
    },
    data() {
        return {
            mdiClosePath: mdiClose,
            btnLoading: false,

            supplierTableHeaders: [
                { text: '供应商简称', value: 'supplierAbbreviatedName', width: '110px' },
                { text: '备注', value: 'remark', width: '200px' },
            ],
            supplierTableData: [],
            supplierTableCurrentRow: [],
        }
    },
    methods: {
        tableChoose(data) {
            this.supplierTableCurrentRow = [data]
        },
        tableChoose2(row) {
            if (!row.value) {
                this.supplierTableCurrentRow = []
            }
            else {
                this.supplierTableCurrentRow = [row.item]
            }
        },
        close() {
            this.$emit('supplierChoose')
        },
        chooseHandle() {
            if (this.supplierTableCurrentRow.length === 0) return
            this.btnLoading = true
            this.$getRequest(this.$api.resourceBySupplier +
                encodeURI(this.supplierTableCurrentRow[0].supplierID)).then(data => {
                console.log('received', data)
                this.btnLoading = false
                this.$emit('supplierChoose', {resources: data})
            })
        },
        deleteHandle() {
            if (this.supplierTableCurrentRow.length === 0) return
            this.btnLoading = true
            this.$deleteRequest(this.$api.deleteResourcesBySupplierID +
                encodeURI(this.supplierTableCurrentRow[0].supplierID)).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '删除成功', color: 'success'
                })
                this.btnLoading = false
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
