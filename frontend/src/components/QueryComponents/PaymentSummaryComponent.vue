<template>
    <div>
        <v-row>
            <v-col cols="auto">
                <v-text-field v-model="companyName"
                              label="单位简称"
                              hide-details="auto"
                              outlined
                              dense
                              readonly
                              style="width: 200px">
                </v-text-field>
            </v-col>
            <v-col cols="auto">
                <v-dialog v-model="companySearchPanelOpen"
                          persistent
                          scrollable
                          no-click-animation
                          width="80vw">
                    <template v-slot:activator="{on}">
                        <v-btn color="accent" v-on="on">
                            单位助选
                        </v-btn>
                    </template>
                    <CompanySearch @fullSearchChoose="companySearchChooseAction">
                    </CompanySearch>
                </v-dialog>
            </v-col>
        </v-row>
        <v-divider class="my-2"></v-divider>
        <v-card outlined>
            <v-data-table></v-data-table>
        </v-card>
    </div>
</template>

<script>
export default {
    name: "PaymentQueryComponent",
    components: {
        CompanySearch: () => import("~/components/CompanySearch"),
    },
    props: {
        mode: {
            type: String,
            required: true
        }
    },
    beforeMount() {
        if (this.mode === 'in')
            this.inboundMode = true
        else if (this.mode === 'out')
            this.inboundMode = false

    },
    data() {
        return {
            inboundMode: true,

            companyID: -1,
            companyName: '',
            companySearchPanelOpen: false,
        }
    },
    methods: {
        companySearchChooseAction(val) {
            if (val) {
                this.companyName = val.abbreviatedName
                this.companyID = val.companyID
            }
            this.companySearchPanelOpen = false
        },
    }
}
</script>

<style scoped>

</style>
