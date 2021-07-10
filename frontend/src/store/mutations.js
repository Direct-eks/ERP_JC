const mutations = {
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
        state.snackbar.timeout = payload.hasOwnProperty('timeout') ?
            payload.timeout : 1500
    },
    /* ------- global data for overlay -------*/
    setOverlay(state, payload) {
        state.overlay = payload
    },
    /* ------- warehouse data ------------*/
    modifyWarehouseOptions(state, payload) {
        state.warehouseOptions = payload
    },
    /* ------- department data ------------*/
    modifyDepartmentOptions(state, payload) {
        state.departmentOptions = payload
    },
    /* ------- tax rate data -------*/
    modifyTaxRateOptions(state, payload) {
        state.taxRateOptions = payload
    },
    /* ------ /bankAccount ------*/
    modifyBankAccounts(state, payload) {
        state.allBankAccounts = payload
        const arr = []
        for (const item of payload) {
            if (item.isVisible === 1) {
                arr.push(item)
            }
        }
        state.visibleBankAccounts = arr
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
    /* ------- all supplier data -------*/
    modifySupplierData(state, payload) {
        state.suppliers = payload
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
    /* ------- sku data -------*/

    /* ------- factory brand data -------*/
    modifyFactoryBrands(state, payload) {
        state.factoryBrands = payload
    },
    /* ------- measurement unit data -------*/
    modifyMeasurementUnits(state, payload) {
        state.measurementUnits = payload
    }

}

export default mutations
