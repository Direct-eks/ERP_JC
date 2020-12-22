<template>
<!--    <p>入库管理</p>-->
<!--    <p>入库单退货</p>-->
    <v-card outlined>
        <v-card-title>入库单退货</v-card-title>

        <v-form @submit.native.prevent @keyup.enter.native="search">
            <v-row dense>
                <v-col cols="auto" class="mt-0">
                    <v-subheader>请输入单据号：</v-subheader>
                </v-col>
                <v-col cols="auto">
                    <v-chip class="mt-1" label>
                        {{searchEntryType}}
                    </v-chip>
                </v-col>
                <v-col cols="auto">
                    <v-text-field v-model="searchEntrySerial"
                                  hide-details="auto"
                                  outlined
                                  dense
                                  style="width: 130px">
                    </v-text-field>
                </v-col>
            </v-row>
        </v-form>

        <v-divider></v-divider>

        <InboundEntryDisplayComponent v-if="showDetailPanel"
                                      editMode="return"
                                      :chosenEntryForDetail="entryDetails">
        </InboundEntryDisplayComponent>

        <SnackMessage></SnackMessage>
    </v-card>
</template>

<script>
    import SnackMessage from "~/components/SnackMessage";

    export default {
        name: "Return_In",
        components: {
            InboundEntryDisplayComponent: () => import('../../components/InboundEntryDisplayComponent'),
            SnackMessage
        },
        data() {
            return {
                searchEntryType: '购入',
                searchEntrySerial: '',
                showDetailPanel: false,
                entryDetails: null
            }
        },
        methods: {
            search() {
                const pattern = new RegExp(/[0-9]{6}-[0-9]{3}/)
                if (!pattern.test(this.searchEntrySerial)) {
                    this.$store.commit('setSnackbar', {
                        message: '单据格式错误', color: 'error'
                    })
                    return
                }
                this.$postRequest(this.$api.entryBySerial, {
                    serial: this.searchEntryType + this.searchEntrySerial
                }).then((res) => {
                    console.log('received', res.data)
                    this.showDetailPanel = true
                    this.entryDetails = res.data
                }).catch(error => this.$ajaxErrorHandler(error))
            }
        }
    }
</script>

<style scoped>

</style>
