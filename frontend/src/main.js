import Vue from 'vue'
import router from '~/utils/router'
import store from '~/store/index'

import vuetify from '~/plugins/vuetify' // path to vuetify export

import App from './App.vue'

import {getRequest} from './utils/axios'
import {postRequest} from './utils/axios'
import {deleteRequest} from './utils/axios'
import {putRequest} from './utils/axios'
import {patchRequest} from './utils/axios'
import api from './utils/api'

Vue.prototype.$getRequest = getRequest
Vue.prototype.$postRequest = postRequest
Vue.prototype.$deleteRequest = deleteRequest
Vue.prototype.$putRequest = putRequest
Vue.prototype.$patchRequest = patchRequest
Vue.prototype.$api = api


if (process.env.NODE_ENV === "development") {

}

if (process.env.NODE_ENV === "production") {
    Vue.config.productionTip = false
}

export default new Vue({
    el: '#app',
    store,
    router,
    vuetify,
    render: h => h(App),
    components: { App },
})
