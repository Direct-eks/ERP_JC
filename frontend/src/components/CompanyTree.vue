<template>
    <v-responsive :height="height"
                  :max-width="maxWidth"
                  style="overflow: auto">
        <v-treeview v-model="treeSelection"
                    :items="treeData"
                    item-text="label"
                    item-key="categoryID"
                    activatable
                    :open-on-click="!showSelect"
                    :selectable="showSelect"
                    return-object
                    @update:active="treeSelect"
                    @update:open="treeSelect"
                    @input="treeSelect"
                    selection-type="independent"
                    color="primary"
                    dense>
        </v-treeview>
    </v-responsive>
</template>

<script>
export default {
    name: "CompanyTree",
    beforeMount() {
        this.$store.dispatch('getCompanyList')
    },
    props: {
        showSelect: {
            type: Boolean,
            required: false,
            default: false
        },
        selectForSearch: {
            type: Boolean,
            required: false,
            default: true
        },
        selectForLevel: {
            type: Boolean,
            required: false,
            default: false
        },
        height: {
            type: String,
            required: true,
            default: '65vh'
        },
        maxWidth: {
            type: String,
            required: true,
            default: '230px'
        }
    },
    data() {
        return {
            treeSelection: [],
        }
    },
    computed: {
        treeData() {
            const data = this.$store.state.companyCategoryList
            return this.$createTree(data, 'company')
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) {
                this.treeSelection = []
                if (this.selectForLevel) {
                    this.$emit('treeSelectionObject', {label: '', categoryID: -1, children: []})
                }
                if (this.selectForSearch) {
                    this.$emit('treeSelectionResult', [])
                }
                return
            }

            const val = data[data.length - 1]
            this.treeSelection = [val]

            if (this.selectForLevel) {
                this.$emit('treeSelectionObject', val)
            }

            if (!this.selectForSearch) return

            if (val.children.length === 0) { // end node
                const result = this.$store.getters.companies(val.areaID)
                if (result) {
                    this.sendResult(result)
                    return
                }
                this.$getRequest(this.$api.companiesByAreaID
                    + encodeURI(val.categoryID)).then((data) => {
                    this.sendResult(data)
                    this.$store.commit('modifyCompanies', { key: val.categoryID, value: data })
                }).catch(() => {})
            }
        },
        sendResult(data) {
            this.$emit('treeSelectionResult', data)
        }
    }
}
</script>

<style scoped>

</style>
