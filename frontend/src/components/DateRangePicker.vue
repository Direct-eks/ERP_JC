<template>
    <v-menu v-model="menu"
            ref="menu"
            :close-on-content-click="false"
            :return-value.sync="dateRange"
            transition="scale-transition"
            offset-y
            min-width="290px">
        <template v-slot:activator="{ on }">
            <v-text-field
                    v-model="dateRangeText"
                    label="选择日期范围"
                    hide-details="auto"
                    outlined
                    readonly
                    dense
                    v-on="on"
                    style="width: 250px">
            </v-text-field>
        </template>
        <v-card>
            <v-row>
                <v-col>
                    <v-list-item-group v-model="dateRange">
                        <v-list-item v-for="(item, i) in dateRangePickerOptions"
                                     :key="i" :value="item.range">
                            <v-list-item-content>{{item.name}}</v-list-item-content>
                        </v-list-item>
                    </v-list-item-group>
                </v-col>
                <v-col>
                    <v-date-picker v-model="dateRange"
                                   :max="maxDate"
                                   no-title
                                   range
                                   scrollable>
                        <v-spacer></v-spacer>
                        <v-btn text color="primary" @click="menu = false">取消</v-btn>
                        <v-btn text color="primary" @click="choose">确认</v-btn>
                    </v-date-picker>
                </v-col>
            </v-row>
        </v-card>
    </v-menu>
</template>

<script>
export default {
    name: "DateRangePicker",
    data() {
        return {
            menu: false,
            dateRange: [
                new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                new Date().format("yyyy-MM-dd").substr(0,10)
            ],
            maxDate: new Date().format("yyyy-MM-dd").substr(0,10),

            dateRangePickerOptions: [
                {
                    range: [
                        new Date(new Date().setTime(Date.now() - 3600 * 1000 * 24 * 7)).format("yyyy-MM-dd").substr(0,10),
                        new Date().format("yyyy-MM-dd").substr(0,10)
                    ],
                    name: '最近一周'
                },
                {
                    range: [
                        new Date(new Date().setDate(1)).format("yyyy-MM-dd").substr(0,10),
                        new Date().format("yyyy-MM-dd").substr(0,10)
                    ],
                    name: '月初至今'
                },
                {
                    range: [
                        new Date(new Date().setTime(Date.now() - 3600 * 1000 * 24 * 30)).format("yyyy-MM-dd").substr(0,10),
                        new Date().format("yyyy-MM-dd").substr(0,10)
                    ],
                    name: '最近30天'
                },
                {
                    range: [
                        new Date(new Date().setTime(Date.now() - 3600 * 1000 * 24 * 90)).format("yyyy-MM-dd").substr(0,10),
                        new Date().format("yyyy-MM-dd").substr(0,10)
                    ],
                    name: '最近90天'
                },
            ],
        }
    },
    computed: {
        dateRangeText() {
            return '从 ' + this.dateRange.join(' 至 ')
        }
    },
    methods: {
        choose() {
            this.$refs.menu.save(this.dateRange)
            this.$emit('chooseDate', this.dateRange)
        }
    }
}
</script>

<style scoped>

</style>
