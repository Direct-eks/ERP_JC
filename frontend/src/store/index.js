import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

/*----------------Vuex-----------------*/
const store = new Vuex.Store({
    state: {
        currentUser: null, // current username

        /*------- global data for snack bar -------*/
        snackbar: {
            message: '',
            color: ''
        },

        /*----------- company data ------------*/
        companyList: null, // stores the tree structure of company category
        companies: new Map(), // stores queried companies according to category, key: categoryID, value: xx

        /*----------- relative company data ------------*/
        relativeCompanyList: null,
        relativeCompanies: new Map(),

        /*----------- product & model data ------------*/
        productList: null,
        models: new Map(),
    },
    getters: {
        currentUser(state) {
            if (state.currentUser == null) {
                return sessionStorage.getItem('userName') != null ?
                    sessionStorage.getItem('userName') : ''
            }
            return state.currentUser
        },
        /*----------- company data ------------*/
        companyList(state) { return state.companyList },
        companies(state) { return (id) => {
            return state.companies.has(id) ? state.companies.get(id) : null
        } },

        /*----------- product & model data ------------*/
        productList(state) { return state.productList },
        models(state) { return (id) => {
            return state.models.has(id) ? state.models.get(id) : null
        } },

        /*----------- relative company data ------------*/
        relativeCompanyList(state) { return state.relativeCompanyList },
        relativeCompanies(state) { return (id) => {
            return state.relativeCompanies.has(id) ? state.relativeCompanies.get(id) : null
        } },
    },
    mutations: {
        modifyCurrentUser(state, username) {
            if (username) {
                state.currentUser = username
            }
            else { // if is null, then logout
                sessionStorage.setItem('userName', null)
                sessionStorage.setItem('userToken', null)
                sessionStorage.setItem('isAuthenticated', 'false')
                state.currentUser = null
            }
        },
        /*------- global data for snack bar -------*/
        setSnackbar(state, payload) {
            state.snackbar.message = payload.message
            state.snackbar.color = payload.color
        },

        /*----------- company data ------------*/
        modifyCompanyList(state, list) { state.companyList = list },
        modifyCompanies(state, pair) {
            if (!state.companies.has(pair.key)) {
                state.companies.set(pair.key, pair.value)
            }
        },
        updateCompanyData(state) {
            state.companyList = null
            state.companies.clear()
        },

        /*----------- product & model data ------------*/
        modifyProductList(state, list) { state.productList = list },
        modifyModels(state, pair) {
            if (!state.models.has(pair.key)) {
                state.models.set(pair.key, pair.value)
            }
        },
        updateProductData(state) {
            state.productList = null
            state.models.clear()
        },

        /*----------- relative company data ------------*/
        modifyRelativeCompanyList(state, list) { state.relativeCompanyList = list },
        modifyRelativeCompanies(state, pair) {
            if (!state.relativeCompanies.has(pair.key)) {
                state.relativeCompanies.set(pair.key, pair.value)
            }
        },
        updateRelativeCompanyData(state) {
            state.relativeCompanyList = null
            state.relativeCompanies.clear()
        }
    },
    actions: {

    }
})

export default store
