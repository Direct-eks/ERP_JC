import Vue from 'vue'
import Vuex from 'vuex'

import state from './state'
import getters from './getters'
import mutations from "./mutations";
import actions from './actions'

Vue.use(Vuex)

/*----------------Vuex-----------------*/
const store = new Vuex.Store({
    strict: process.env.NODE_ENV === 'development',
    state,
    getters,
    mutations,
    actions,
})

export default store
