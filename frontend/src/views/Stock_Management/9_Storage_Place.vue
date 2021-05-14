<template>
    <v-data-table
        :headers="tableHeaders"
        :items="tableData"
        item-key="index"
        height="45vh"
        calculate-widths
        disable-sort
        show-select
        fixed-header
        disable-pagination
        hide-default-footer
        locale="zh-cn">
        <template v-slot:item.index="{ item }">
            {{tableData.indexOf(item) + 1}}
        </template>
        <template v-slot:item.storagePlace="{ item }">
            <v-select v-model="item.storagePlace"
                      :items="storagePlaceOptions"
                      hide-details="auto"
                      multiple
                      @change="saveChoice(item)"
                      dense
                      style="width: 150px">
            </v-select>
        </template>
    </v-data-table>
</template>

<script>
export default {
    name: "9_Storage_Place",
    data() {
        return {
            storagePlaceOptions: ['a', 'b', 'c', 'd', 'e'],
            storagePlaceChoose: [],
            tableHeaders: [
                { text: '序号', value: 'index', width: '65px' },
                { text: '架位', value: 'storagePlace', width: '65px' },
                { text: '选择', value: 'choice', width: '65px' },
            ],
            tableData: [
                {storagePlace: [], choice: ''},
                {storagePlace: [], choice: ''},
                {storagePlace: [], choice: ''}
            ],
        }
    },
    methods: {
        saveChoice(item) {
            console.log(item.storagePlace)
            item.choice = item.storagePlace.toString()
        },
    }
}
</script>

<style scoped>

</style>
