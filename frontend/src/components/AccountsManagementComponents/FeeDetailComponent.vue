<template>
    <v-card>
        <v-card-title>
            新增费用明细
            <v-spacer></v-spacer>
            <v-btn color="accent"
                   class="mr-6"
                   @click="exportNew">
                新增
            </v-btn>
            <v-btn icon @click="close">
                <v-icon>{{ mdiClose }}</v-icon>
            </v-btn>
        </v-card-title>
        <v-card-text class="d-flex">
            <v-responsive max-width="300px"
                          height="500px"
                          style="overflow: auto">
                <v-treeview v-model="treeSelection"
                            :items="treeData"
                            item-text="label"
                            item-key="categoryID"
                            activatable
                            open-on-click
                            return-object
                            @update:active="treeSelect"
                            @update:open="treeSelect"
                            @input="treeSelect"
                            color="primary"
                            dense>
                </v-treeview>
            </v-responsive>

            <div class="ml-5 mt-5">
                <v-row class="ma-2">
                    <v-textarea v-model="form.remark"
                                label="摘要"
                                hide-details="auto"
                                outlined
                                auto-grow
                                rows="1">
                    </v-textarea>
                </v-row>
                <v-row class="ma-2">
                    <v-text-field v-model="form.amount"
                                  type="number"
                                  label="金额"
                                  hide-details="auto"
                                  @change="handleAmountChange"
                                  outlined
                                  style="width: 120px">
                    </v-text-field>
                </v-row>
            </div>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiClose } from '@mdi/js'

export default {
    name: "FeeDetailComponent",
    beforeMount() {
        this.$store.dispatch('getFeeCategoryList')
    },
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            mdiClose,

            treeSelection: [],

            newItemIndex: -1,
            form: {
                feeDetailEntryID: -1, feeEntryID: -1,
                feeCategoryID: -1, feeCategoryName: '',
                remark: '', amount: ''
            },
            emptyForm: {
                feeDetailEntryID: -1, feeEntryID: -1,
                feeCategoryID: -1, feeCategoryName: '',
                remark: '', amount: ''
            }

        }
    },
    computed: {
        treeData() {
            const data = []
            for (const item of this.$store.state.feeCategoryList) {
                if (this.mode === 'income' && item.classification === '收入') {
                    data.push(item)
                }
                else if (this.mode === 'expenditure' && item.classification === '支出') {
                    data.push(item)
                }
            }
            return this.$createTree(data, 'fee')
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) return
            const val = data[data.length - 1]
            this.treeSelection = [val]

            if (val.children.length === 0) { // end node
                this.form.feeCategoryID = val.categoryID
                this.form.feeCategoryName = val.label
            }
        },
        handleAmountChange(item) {
            this.form.amount = this.$validateFloat(item, true)
        },
        close() {
            this.$emit('createDetail')
        },
        exportNew() {
            if (this.form.feeCategoryID === -1 || this.form.remark === '' || this.form.amount === '') {
                this.$store.commit('setSnackbar', {
                    message: '请填写信息', color: 'warning'
                })
                return
            }
            this.$emit('createDetail', JSON.parse(JSON.stringify(this.form)))
            this.emptyForm.feeCategoryID = this.form.feeCategoryID // preserve selection
            this.emptyForm.feeCategoryName = this.form.feeCategoryName
            Object.assign(this.form, this.emptyForm)
            this.form.feeDetailEntryID = --this.newItemIndex
        }
    }
}
</script>

<style scoped>

</style>
