<template>
    <!--  <p>生产管理</p>-->
    <!--  <p>拆组装单查询</p>-->
    <v-card>
        <v-toolbar flat>
            <v-toolbar-title>拆组装单查询</v-toolbar-title>
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
                <QueryComponent displayMode="display"
                                type="assemblyEntry"
                                prefix="拆装"
                                @tableClick="tableClickAction">
                </QueryComponent>
            </v-tab-item>

            <v-tab-item key="detail" :eager="true">
                <EntryComponent editMode="display"
                                type="assemblyEntry"
                                prefix="拆" :paramFormsArray="forms">
                </EntryComponent>
            </v-tab-item>

        </v-tabs-items>

    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "AE_Query",
    components: {
        EntryComponent: () => import('~/components/ProductionComponents/DuelEntry'),
        QueryComponent: () => import('~/components/ProductionComponents/QueryDisplayComponent')
    },
    beforeMount() {
        this.originalForms = JSON.parse(JSON.stringify(this.forms[0]))
    },
    data() {
        return {
            mdiArrowLeft,
            tab: null,
            currentTableRow: null,

            forms: [{
                entryDate: '', creationDate: '', totalAmount: '', drawer: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '', classification: '',
                entryProducts: []
            }, {
                entryDate: '', creationDate: '', totalAmount: '', drawer: '',
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '', classification: '',
                entryProducts: []
            }],
            originalForms: {}
        }
    },
    methods: {
        handleTabChange(val) {
            if (val === 0) {
                this.currentTableRow = null
                Object.assign(this.forms[0], this.originalForms) // reset form
                Object.assign(this.forms[1], this.originalForms) // reset form
            }
        },
        tableClickAction(val) {
            this.currentTableRow = val
            Object.assign(this.forms[0], this.currentTableRow[0])
            Object.assign(this.forms[1], this.currentTableRow[1])
        }
    }
}
</script>

<style scoped>

</style>
