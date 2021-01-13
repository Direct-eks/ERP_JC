<template>
<!--  <p>出库结账管理</p>-->
<!--  <p>付运费结账</p>-->
    <div id="all">

    </div>
</template>

<script>
    export default {
        name: "Out_Cost_Bill",
        components: {
            CompanySearch: () => import("~/components/CompanySearch")
        },
        data() {
            return {
                // 单位助选 data
                fullSearchField: '',
                fullSearchLoading: false,
                fullSearchPanelOpen: false,
            }
        },
        updated: function () {
            this.$nextTick(function () {
                // Code that will run only after the
                // entire view has been re-rendered
                this.abbreviatedSearchLoading = false
                this.fullSearchLoading = false
                this.modelSearchLoading = false
                console.log('deactivate loading')
            })
        },
        methods: {
            fullSearch() {
                this.fullSearchLoading = true;
                this.abbreviatedSearchPanelOpen = false //close the other search panel
                this.fullSearchPanelOpen = true
            },
            fullSearchPanelCloseAction() {
                this.fullSearchPanelOpen = false
            },
            fullSearchChooseAction() {

            },
        }
    }
</script>

<style scoped>

</style>
