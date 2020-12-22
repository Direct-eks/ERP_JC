import Vue from 'vue'
import Vuetify from 'vuetify/lib'

Vue.use(Vuetify)

const opts = {
    theme: {
        themes: {
            light: {
                primary: '#2196f3',
                secondary: '#03a9f4',
                accent: '#00bcd4',
                error: '#f44336',
                warning: '#ff9800',
                info: '#3f51b5',
                success: '#4caf50'
            }
        }
    }
    // components: {
    //     VApp,
    //     VNavigationDrawer,
    //     VList,
    //     VListItem,
    //     VListItemContent,
    //     VListItemTitle,
    //     VAppBar,
    //     VAppBarNavIcon,
    //     VToolbarTitle,
    //     VBtn,
    //     VSpacer,
    //     VIcon,
    //     VMain,
    //     VContainer
    // }
}

export default new Vuetify(opts)
