<template>
    <v-card>
        <v-form ref="form">
            <v-row>
                <v-col cols="auto">
                    <DatePicker label="日期"
                                v-model="form.paymentDate"
                                :disabled="displayMode || modifyMode">
                    </DatePicker>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.drawer"
                                  label="开单人"
                                  hide-details="auto"
                                  outlined
                                  readonly
                                  dense
                                  style="width: 150px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.bankAccountID"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              label="银行"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-select v-model="form.bankAccountID"
                              :rules="rules.bankAccountID"
                              :items="bankAccountOptions"
                              item-value="bankAccountID"
                              item-text="name"
                              label="银行"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 250px">
                    </v-select>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.paymentNumber"
                                  label="号码"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  :readonly="form.paymentMethod === '现金' ||
                                                form.paymentMethod === '' || displayMode"
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="form.paymentAmount"
                                  label="金额"
                                  hide-details="auto"
                                  type="number"
                                  @change="handlePaymentAmountChange"
                                  outlined
                                  :readonly="displayMode"
                                  dense
                                  style="width: 160px">
                    </v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="auto">
                    <v-select v-model="form.departmentID"
                              :rules="rules.departmentID"
                              :items="departmentOptions"
                              item-value="departmentID"
                              item-text="name"
                              label="部门"
                              hide-details="auto"
                              :readonly="displayMode"
                              outlined dense
                              style="width: 180px">
                    </v-select>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-textarea v-model="form.remark"
                                label="备注"
                                hide-details="auto"
                                :readonly="displayMode"
                                outlined
                                dense
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-col>
            </v-row>
        </v-form>
    </v-card>
</template>

<script>
export default {
    name: "FeesComponent",
    components: {
        DatePicker: () => import("~/components/DatePicker")
    },
    props: {
        mode: {
            type: String,
            required: true
        },
        paramForm: {
            type: Object,
            required: false
        }
    },
    watch: {
        paramForm: {
            handler: function(val) {

            },
            deep: true,
            immediate: true
        }
    },
    beforeMount() {
        switch (this.mode) {
        case 'create':
            this.createMode = true
            break
        case 'display':
            this.displayMode = true
            break
        case 'modify':
            this.modifyMode = true
            break
        }

        this.$store.dispatch('getDepartmentOptions')
        this.$store.dispatch('getBankAccounts')
    },
    data() {
        return {
            createMode: false,
            displayMode: false,
            modifyMode: false,

            form: {

            },

            rules: {
                departmentID: [v => !!v || '请选择部门'],
                bankAccountID: [v => !!v &&
                    (this.form.paymentMethod !== '现金' || this.form.paymentMethod !== '')
                    || '请选择银行'],
            },
        }
    },
    computed: {
        bankAccountOptions() {
            return this.$store.state.visibleBankAccounts
        },
        departmentOptions() {
            const options = this.$store.state.departmentOptions
            for (const item of options) {
                if (item.isDefault === 1) {
                    this.form.departmentID = item.departmentID
                    break
                }
            }
            return options
        }
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
