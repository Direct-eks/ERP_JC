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
    name: "ModelTree",
    beforeMount() {
        this.$store.dispatch('getModelCategoryList')
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
        },
    },
    data() {
        return {
            treeSelection: [],
        }
    },
    computed: {
        treeData() {
            const data = this.$store.state.modelCategoryList
            return this.$createTree(data, true)
        }
    },
    methods: {
        treeSelect(data) {
            if (data.length === 0) { // de-select
                this.treeSelection = []
                if (this.selectForLevel) {
                    this.$emit('treeSelectionObject', {label: '', categoryID: -1, treeLevel: '', children: []})
                }
                if (this.selectForSearch) {
                    this.$emit('treeSelectionResult', [])
                }
                return
            }

            let val = data[data.length - 1] // choose newest active item
            this.treeSelection = [val]

            if (this.selectForLevel) {
                this.$emit('treeSelectionObject', val)
            }

            if (!this.selectForSearch) return

            if (val.children.length === 0) { // end node
                let result = this.$store.getters.models(val.categoryID)
                if (result) {
                    this.sendResult(result)
                    return
                }
                this.$getRequest(this.$api.modelsByCategory +
                    encodeURI(val.categoryID)).then((data) => {
                    this.sendResult(data)
                    this.$store.commit('modifyModels', { key: val.categoryID, value: data })
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
