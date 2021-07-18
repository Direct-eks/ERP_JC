<template>
    <v-menu :close-on-content-click="false"
            :disabled="disabled"
            :nudge-right="40"
            transition="scale-transition"
            offset-y>
        <template v-slot:activator="{on}">
            <v-text-field v-model="localEntryDate"
                          v-on="on"
                          :label="label"
                          hide-details="auto"
                          outlined dense
                          readonly
                          style="width: 140px">
            </v-text-field>
        </template>
        <v-date-picker v-model="localEntryDate"
                       no-title
                       :max="allowedMaxDate"
                       :first-day-of-week="0"
                       locale="zh-cn">
        </v-date-picker>
    </v-menu>
</template>

<script>
export default {
    name: "DatePicker",
    model: {
        prop: 'entryDate',
        event: 'changeDate'
    },
    props: {
        label: {
            type: String,
            required: true
        },
        entryDate: {
            type: String,
            required: true
        },
        disabled: {
            type: Boolean,
            required: false,
            default: false
        }
    },
    watch: {
        entryDate(val) {
            this.localEntryDate = val
        },
        localEntryDate() {
            this.$emit('changeDate', this.localEntryDate)
        }
    },
    data() {
        return {
            localEntryDate: this.entryDate,
            allowedMaxDate: new Date().format('yyyy-MM-dd').substr(0, 10),
        }
    },
}
</script>

<style scoped>

</style>
