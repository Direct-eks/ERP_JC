<template>
    <v-container>

        <v-card outlined>
            <EntryComponent ref="entry1"
                            editMode="creation"
                            :type="type + 'In'"
                            :prefix="prefix + '入'" :form.sync="form1">
            </EntryComponent>
        </v-card>
        <v-card outlined>
            <EntryComponent ref="entry2"
                            editMode="creation"
                            :type="type + 'Out'"
                            :prefix="prefix + '出'" :form.sync="form2">
            </EntryComponent>
        </v-card>

        <v-row class="my-2" dense>
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
        case 'transferEntry':
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
        paramForms: {
            type: Object,
            required: false,
        }
    },
    watch: {
        paramForms: {
            handler: function (val) {
                if (this.creationMode) return
                this.form = val
                this.handleTotalChange()
            },
            deep: true
        },
        form1: {
            handler: function (val) {
                console.log('aaa')
            },
            deep: true
        }
    },
    data() {
        return {
            creationMode: false,
            modificationMode: false,
            displayMode: false,

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
                classification: this.prefix + '入',
                products: []
            },
            form2: {
                entryDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                creationDate: new Date().format('yyyy-MM-dd').substr(0, 10),
                totalAmount: '0',
                drawer: this.$store.getters.currentUser,
                departmentID: -1, departmentName: '',
                warehouseID: -1, warehouseName: '',
                remark: '',
                classification: this.prefix + '出',
                products: []
            },
        }
    },
    methods: {
        saveEntry() {
            if (this.$refs.entry1.validate() && this.$refs.entry2.validate()) {
                let api = ''
                switch (this.type) {
                case 'assemblyEntry':
                    api = this.$api.createAssemblyEntry
                    break
                case 'transferEntry':
                    api = this.$api.createTransferEntry
                    break
                }

                this.$putRequest(api, this.form, {

                }).then(() => {

                }).catch(() => {})
            }
            this.submitPopup = false
        },
        saveChanges() {
            if (this.$refs.entry1.validate() && this.$refs.entry2.validate()) {
                let api = ''
                switch (this.type) {
                case 'assemblyEntry':
                    api = this.$api.modifyAssemblyEntry
                    break
                case 'transferEntry':
                    api = this.$api.modifyTransferEntry
                    break
                }
            }
            this.submitPopup2 = false
        }
    }
}
</script>

<style scoped>

</style>
