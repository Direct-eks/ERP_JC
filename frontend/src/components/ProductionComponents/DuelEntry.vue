<template>
    <v-container>

        <v-row class="mb-1" dense>
            <v-col cols="auto">
                <v-select v-if="creationMode && isAssemblyMode"
                          v-model="assemblyMode"
                          :items="assemblyOptions"
                          label="拆/装形式"
                          hide-details="auto"
                          outlined dense
                          style="width: 110px">
                </v-select>
            </v-col>
            <v-spacer></v-spacer>
            <v-col v-if="creationMode">
                <v-dialog v-model="submitPopup" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup = false">取消</v-btn>
                            <v-btn color="success" @click="saveEntry">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <v-col v-if="modificationMode">
                <v-dialog v-model="submitPopup2" max-width="300px">
                    <template v-slot:activator="{ on }">
                        <v-btn color="primary" v-on="on">保存修改</v-btn>
                    </template>
                    <v-card>
                        <v-card-title>确认提交？</v-card-title>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="primary" @click="submitPopup2 = false">取消</v-btn>
                            <v-btn color="success" @click="saveChanges">确认</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>

        <v-card outlined>
            <EntryComponent ref="entry1"
                            :editMode="editMode"
                            :type="type + 'Out'"
                            :prefix="prefix + '出'"
                            :paramForm.sync="form1">
            </EntryComponent>
        </v-card>
        <v-card outlined>
            <EntryComponent ref="entry2"
                            :editMode="editMode"
                            :type="type + 'In'"
                            :prefix="prefix + '入'"
                            :paramForm="form2">
            </EntryComponent>
        </v-card>

    </v-container>
</template>

<script>
export default {
    name: "DuelEntry",
    components: {
        EntryComponent: () => import('~/components/ProductionComponents/WarehouseEntryForDuel')
    },
    beforeMount() {
        switch (this.editMode) {
        case 'creation':
            this.creationMode = true
            break
        case 'modification':
            this.modificationMode = true
            break
        case 'display':
            this.displayMode = true
            break
        }

        switch (this.type) {
        case 'assemblyEntry':
            this.isAssemblyMode = true
            break
        case 'transferEntry':
            break
        }

        this.$store.dispatch('getWarehouseOptions')
        this.$store.dispatch('getDepartmentOptions')
    },
    props: {
        editMode: {
            type: String,
            required: true
        },
        type: {
            type: String,
            required: true
        },
        prefix: {
            type: String,
            required: true
        },
        paramFormsArray: {
            type: Array,
            required: false,
        }
    },
    watch: {
        paramFormsArray: {
            handler: function (val) {
                if (this.creationMode) return
                this.form1 = val[0]
                this.form2 = val[1]
            },
            deep: true
        },
        form1: {
            handler: function() {
                if (!this.creationMode) return
                this.form2.entryDate = this.form1.entryDate
                if (!this.isAssemblyMode) {
                    this.form2.entryProducts = this.form1.entryProducts
                }
            },
            deep: true
        }
    },
    data() {
        return {
            creationMode: false,
            modificationMode: false,
            displayMode: false,
            isAssemblyMode: false,

            assemblyMode: '拆',
            assemblyOptions: [
                '拆', '装', '组合'
            ],

            submitPopup: false,
            submitPopup2: false,

            form1: {
                entryDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                creationDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                totalAmount: '0',
                drawer: this.$store.getters.currentUser,
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: this.prefix + '出',
                entryProducts: []
            },
            form2: {
                entryDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                creationDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                totalAmount: '0',
                drawer: this.$store.getters.currentUser,
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: this.prefix + '入',
                entryProducts: []
            },
        }
    },
    methods: {
        verifyAssemblyMode() {
            if (!this.isAssemblyMode) return true

            switch (this.assemblyMode) {
            case "拆":
                if (this.$refs.entry1.form.entryProducts.length === 1 &&
                    this.$refs.entry2.form.entryProducts.length > 1) {
                    return true
                }
                break
            case "装":
                if (this.$refs.entry1.form.entryProducts.length > 1 &&
                    this.$refs.entry2.form.entryProducts.length === 1) {
                    return true
                }
                break
            case "组合":
                if (this.$refs.entry1.form.entryProducts.length >= 1 &&
                    this.$refs.entry2.form.entryProducts.length >= 1) {
                    return true
                }
                break
            }
            this.$store.commit('setSnackbar', {
                message: '拆/（组）装 形式错误', color: 'warning'
            })
            return false
        },
        saveEntry() {
            this.submitPopup = false
            if (!this.verifyAssemblyMode()) return
            if (!this.$refs.entry1.$refs.form.validate() || !this.$refs.entry2.$refs.form.validate()) return

            let api = ''
            switch (this.type) {
            case 'assemblyEntry':
                api = this.$api.createAssemblyEntry
                break
            case 'transferEntry':
                api = this.$api.createTransferEntry
                break
            }

            this.$putRequest(api, {
                elements: [this.$refs.entry1.form, this.$refs.entry2.form]
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$router.replace('/production_management')
            }).catch(() => {})
        },
        saveChanges() {
            this.submitPopup2 = false
            if (!this.$refs.entry1.$refs.form.validate() || !this.$refs.entry2.$refs.form.validate()) return

            let api = ''
            switch (this.type) {
            case 'assemblyEntry':
                api = this.$api.modifyAssemblyEntry
                break
            case 'transferEntry':
                api = this.$api.modifyTransferEntry
                break
            }

            this.$patchRequest(api, {
                elements: [this.$refs.entry1.form, this.$refs.entry2.form]
            }).then(() => {
                this.$store.commit('setSnackbar', {
                    message: '提交成功', color: 'success'
                })
                this.$router.replace('/production_management')
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>

</style>
