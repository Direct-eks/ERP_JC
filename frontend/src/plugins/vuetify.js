import Vue from 'vue'
import Vuetify from 'vuetify/lib'

Vue.use(Vuetify)

import zhHans from 'vuetify/es5/locale/zh-Hans'

if (process.env.NODE_ENV === "production") {
    Vuetify.config.silent = true
}

export default new Vuetify({
    icons: {
        iconfont: 'mdiSvg'
    },
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
    },
    lang: {
        locales: {zhHans},
        current: 'zhHans'
    }
})
