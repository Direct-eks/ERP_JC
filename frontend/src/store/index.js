import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

/*----------------Vuex-----------------*/
const store = new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state: {
        /*  ------- global user data ------- */
        currentUser: null, // current username
        currentUserRole: null,
        currentUserPermissions: [],

        /* ------- global data for snack bar -------*/
        snackbar: {
            message: '',
            color: '',
        },

        /* ------- company data -------*/
        companyCategoryList: null, // stores the tree structure of company category
        companies: new Map(), // key: categoryID, value: companies

        /* ------- relative company data -------*/
        relevantCompanyCategoryList: null,
        relevantCompanies: new Map(), // key: categoryID, value: relevant companies

        /* ------- model data -------*/
        modelCategoryList: null,
        models: new Map(), // key: categoryID, value: companies
    },
    getters: {
        currentUser(state) {
            if (state.currentUser == null) {
                return sessionStorage.getItem('userName') != null ?
                    sessionStorage.getItem('userName') : ''
            }
            return state.currentUser
        },
        currentUserRole(state) {
            if (state.currentUserRole == null) {
                return sessionStorage.getItem('userRole') != null ?
                    sessionStorage.getItem('userRole') : ''
            }
            return state.currentUserRole
        },
        currentUserPermissions(state) {
            if (state.currentUserPermissions.length === 0) {
                return sessionStorage.getItem('userPermissions') != null ?
                    JSON.parse(sessionStorage.getItem('userPermissions')) : []
            }
            return state.currentUserPermissions
        },
        currentUserIsPermitted(state) {
            return (permission) => {
                return state.currentUserPermissions.includes(permission)
            }
        },

        /*----------- company data ------------*/
        companyCategoryList(state) {
            return state.companyCategoryList
        },
        companies(state) {
            return (id) => {
                return state.companies.has(id) ? state.companies.get(id) : null
            }
        },

        /* ------- relative company data -------*/
        relevantCompanyCategoryList(state) {
            return state.relevantCompanyCategoryList
        },
        relevantCompanies(state) {
            return (id) => {
                return state.relevantCompanies.has(id) ? state.relevantCompanies.get(id) : null
            }
        },

        /*----------- model data ------------*/
        productList(state) {
            return state.modelCategoryList
        },
        models(state) {
            return (id) => {
                return state.models.has(id) ? state.models.get(id) : null
            }
        },
    },
    mutations: {
        modifyCurrentUser(state, username) {
            if (username) {
                state.currentUser = username
            }
            else { // if is null, then logout
                sessionStorage.clear()
                state.currentUser = null
            }
        },
        modifyCurrentUserRole(state, userRole) {
            state.currentUserRole = userRole
        },
        modifyCurrentUserPermissions(state, userPermissions) {
            state.currentUserPermissions = userPermissions
        },

        /*------- global data for snack bar -------*/
        setSnackbar(state, payload) {
            state.snackbar.message = payload.message
            state.snackbar.color = payload.color
        },

        /*----------- company data ------------*/
        modifyCompanyList(state, list) {
            state.companyCategoryList = list
        },
        modifyCompanies(state, pair) {
            state.companies.set(pair.key, pair.value)
        },
        clearCompanyData(state) {
            state.companyCategoryList = null
            state.companies.clear()
        },

        /*----------- relevant company data ------------*/
        modifyRelevantCompanyList(state, list) {
            state.relevantCompanyCategoryList = list
        },
        modifyRelevantCompanies(state, pair) {
            state.relevantCompanies.set(pair.key, pair.value)
        },
        clearRelevantCompanyData(state) {
            state.relevantCompanyCategoryList = null
            state.relevantCompanies.clear()
        },

        /*----------- product & model data ------------*/
        modifyModelList(state, list) {
            state.modelCategoryList = list
        },
        modifyModels(state, pair) {
            state.models.set(pair.key, pair.value)
        },
        clearModelData(state) {
            state.modelCategoryList = null
            state.models.clear()
        },

    },
    actions: {

    }
})

export default store
