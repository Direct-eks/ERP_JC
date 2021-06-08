<template>
    <!--  <p>资源录入</p>-->
    <!--  <p>供应商资源</p>-->
    <v-card>
        <v-card-title class="d-flex">
            供应商资源
            <v-spacer></v-spacer>
            <v-dialog v-model="resourceCompanySearchPanel"
                      persistent
                      scrollable
                      no-click-animation
                      max-width="55vw">
                <template v-slot:activator="{on}">
                    <v-btn color="primary" v-on="on"
                           :disabled="resourceCompanySearchPanel">
                        查所有资源单位
                    </v-btn>
                </template>
                <SupplierResourceQuery :enableEdit="true"
                                       @supplierChoose="resourceCompanySearchPanel = false">
                </SupplierResourceQuery>
            </v-dialog>

            <v-spacer></v-spacer>
            <v-btn color="accent"
                   to="/resources">
                <v-icon>{{ mdiArrowLeft }}</v-icon>
                返回
            </v-btn>
        </v-card-title>
        <v-card-text>
            <v-card outlined>
                <div class="d-flex">
                    <div class="ma-2">
                        <v-text-field v-model="company.companyPhone"
                                      label="电话"
                                      hide-details="auto"
                                      outlined
                                      dense
                                      readonly
                                      style="width: 200px">
                        </v-text-field>
                    </div>
                    <div class="ma-2">
                        <v-text-field v-model="company.companyAbbreviatedName"
                                      label="单位简称"
                                      hide-details="auto"
                                      outlined
                                      dense
                                      readonly
                                      style="width: 200px">
                        </v-text-field>
                    </div>
                </div>
                <div class="d-flex">
                    <div class="ma-2">
                        <v-text-field v-model="company.companyFullName"
                                      label="单位全称"
                                      hide-details="auto"
                                      outlined
                                      dense
                                      readonly
                                      style="width: 300px">
                        </v-text-field>
                    </div>
                    <div class="ma-2">
                        <v-dialog v-model="fullSearchPanelOpen"
                                  persistent
                                  scrollable
                                  no-click-animation
                                  width="80vw">
                            <template v-slot:activator="{on}">
                                <v-btn color="accent"
                                       v-on="on"
                                       :disabled="fullSearchPanelOpen">
                                    单位助选
                                </v-btn>
                            </template>
                            <CompanySearch
                                @fullSearchChoose="fullSearchChooseAction">
                            </CompanySearch>
                        </v-dialog>
                    </div>
                </div>
            </v-card>
        </v-card-text>
    </v-card>
</template>

<script>
import { mdiArrowLeft, mdiClose } from "@mdi/js";

export default {
    name: "SupplierResources",
    components: {
        SupplierResourceQuery: () => import("~/components/SupplierResourceQuery"),
        CompanySearch: () => import("~/components/CompanySearch")
    },
    beforeMount() {

    },
    data() {
        return {
            mdiArrowLeft,
            mdiClose,

            resourceCompanySearchPanel: false,

            fullSearchPanelOpen: false,
            company: {
                companyPhone: '',
                companyAbbreviatedName: '',
                companyFullName: '',
            }
        }
    },
    methods: {
        fullSearchChooseAction(val) {

        }
    }
}
</script>

<style scoped>

</style>
