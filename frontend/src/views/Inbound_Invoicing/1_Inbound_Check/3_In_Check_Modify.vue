<template>
    <!--  <p>入库结账管理</p>-->
    <!--  <p>入库结账单修改</p>-->
    <div style="text-align: left">

    </div>
</template>

<script>
    export default {
        name: "In_Check_Modify",
        components: {
            CompanySearch: () => import('~/components/CompanySearch')
        },
        data() {
            return {
                value: ['2019-09-21', '2020-5-21'],

                fullSearchField: '',
                fullSearchLoading: false,
                fullSearchPanelOpen: false,
            }
        },
        methods: {
            fullSearch() {
                this.fullSearchLoading = true;
                setTimeout(() => {
                    this.abbreviatedSearchPanelOpen = false //close the other search panel
                    this.fullSearchPanelOpen = true
                    this.fullSearchLoading = false
                }, 300)
            },
            fullSearchPanelCloseAction() {
                this.fullSearchPanelOpen = false;
            },
            fullSearchChoose() {

            }
        }
    }
</script>

<style scoped>

</style>
