<template>
<!--  <p>库存管理</p>-->
<!--  <p>库存盘点</p>-->
    <v-card>
        <v-card-title class="d-flex">
            库存盘点
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/stock_management">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <QueryConditions :queries.sync="queries">
            </QueryConditions>
            <v-divider class="my-2"></v-divider>
        </v-card-text>
    </v-card>
</template>

<script>
import {mdiArrowLeft} from "@mdi/js";

export default {
    name: "Inventory",
    components: {
        QueryConditions: () => import('~/components/QueryComponents/QueryConditions')
    },
    data() {
        return {
            mdiArrowLeft,
            loading: false,

            queries: {
                treeSelection: {label: '', categoryID: -1, children: []},
                code: '',
                warehouseID: -1,
                factoryBrand: '',
            },

        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
