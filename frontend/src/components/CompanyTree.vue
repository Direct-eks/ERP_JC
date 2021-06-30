<template>
    <v-responsive :height="height"
                  :max-width="maxWidth"
                  style="overflow: auto">
        <v-treeview v-model="treeSelection"
                    :items="treeData"
                    item-text="label"
                    item-key="areaID"
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
        const result = this.$store.getters.companyCategoryList
        if (result) {
            this.treeData = result
            return
        }
        this.$getRequest(this.$api.companyAreas).then((data) => {
            this.treeData = this.$createTree(data, false)
            this.$store.commit('modifyCompanyList', this.treeData)
        }).catch(() => {})
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
            treeData: [],
            treeSelection: [],
            treeSelectedLevel: '',
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) return
            const val = data[0]
            this.treeSelection = [val]
            this.treeSelectedLevel = val.label

            if (!this.selectForSearch) {
                this.$emit('treeSelectionLabel', this.treeSelectedLevel)
                return
            }

            if (val.children.length === 0) { // end node
                const result = this.$store.getters.companies(val.areaID)
                if (result) {
                    this.sendResult(result)
                    return
                }
                this.$getRequest(this.$api.companiesByAreaID
                    + encodeURI(val.areaID)).then((data) => {
                    this.sendResult(data)
                    this.$store.commit('modifyCompanies', { key: val.areaID, value: data })
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
