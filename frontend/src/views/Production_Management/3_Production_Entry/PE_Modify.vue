<template>
    <!--  <p>生产管理</p>-->
    <!--  <p>产成品入库单修改</p>-->
    <v-card>
        <v-toolbar flat>
            <v-toolbar-title>产成品入库单修改</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/production_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>

            <template v-slot:extension>
                <v-tabs v-model="tab" @change="handleTabChange">
                    <v-tabs-slider></v-tabs-slider>
                    <v-tab key="browse">浏览</v-tab>
                    <v-tab key="detail" :disabled="currentTableRow === null">详细情况</v-tab>
                </v-tabs>
            </template>
        </v-toolbar>

        <v-tabs-items v-model="tab">

            <v-tab-item key="browse">
                <QueryComponent displayMode="modification"
                                type="productionEntry"
                                prefix="产入"
                                @tableClick="tableClickAction">
                </QueryComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <EntryComponent editMode="modification"
                                type="productionEntry"
                                prefix="产入" :paramForm="form">
                </EntryComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "WE_Modify",
    components: {
        EntryComponent: () => import('~/components/ProductionComponents/WarehouseEntry'),
        QueryComponent: () => import('~/components/ProductionComponents/QueryDisplayComponent')
    },
    beforeMount() {
        this.originalForm = JSON.parse(JSON.stringify(this.form))
    },
    data() {
        return {
            mdiArrowLeft,
            tab: null,
            currentTableRow: null,

            form: {
                entryDate: '', creationDate: '',
                totalAmount: '', drawer: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '', classification: '',
                entryProducts: []
            },
            originalForm: {}
        }
    },
    methods: {
        handleTabChange(val) {
            if (val === 0) {
                this.currentTableRow = null
                Object.assign(this.form, this.originalForm) // reset form
            }
        },
        tableClickAction(val) {
            this.currentTableRow = val
            Object.assign(this.form, this.currentTableRow)
        }
    }
}
</script>

<style scoped>

</style>
