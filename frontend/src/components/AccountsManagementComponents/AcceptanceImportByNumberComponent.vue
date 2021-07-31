<template>
    <v-card>
        <v-card-title>
            票号检索
            <v-spacer></v-spacer>
            <v-btn icon @click="importPopup = false">
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-data-table v-model="importTableCurrentRow"
                          :headers="importTableHeaders"
                          :items="importTableData"
                          item-key="modelID"
                          @click:row="modelTableChoose"
                          @item-selected="modelTableChoose2"
                          :search="modelCode"
                          height="65vh"
                          hide-default-footer
                          calculate-widths
                          disable-sort
                          disable-pagination
                          single-select
                          show-select
                          checkbox-color="accent"
                          fixed-header
                          locale="zh-cn"
                          dense>
            </v-data-table>
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="success" @click="importEntry">确认</v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
export default {
    name: "AcceptanceImportByNumberComponent",
    props: {
        number: {
            type: String,
            required: true
        }
    },
    data() {

    },
    methods: {

    }
}
</script>

<style scoped>

</style>
