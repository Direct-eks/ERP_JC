const mutations = {
    modifyCurrentUser(state, user) {
        if (user) {
            state.currentUser = user.username
            state.currentUserRole = user.userRole
            state.currentUserPermissions = user.userPermissions
            state.permittedRoundingAmount = user.amount
            // use sessionStorage to store user information
            sessionStorage.setItem('userName', user.username)
            sessionStorage.setItem('userToken', user.sessionID)
            sessionStorage.setItem('userRole', user.userRole)
            sessionStorage.setItem('userPermissions', JSON.stringify(user.userPermissions))
            sessionStorage.setItem('isAuthenticated', 'true')
        }
        else { // if is null, then logout
            state.currentUser = null
            state.currentUserRole = ''
            state.currentUserPermissions = []
            state.permittedRoundingAmount = ''
            sessionStorage.clear()
        }
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
    clearWarehouseOptions(state) {
        state.warehouseOptions = []
    },
    /* ------- department data ------------*/
    modifyDepartmentOptions(state, payload) {
        state.departmentOptions = payload
    },
    clearDepartmentOptions(state) {
        state.departmentOptions = []
    },
    /* ------- tax rate data -------*/
    modifyTaxRateOptions(state, payload) {
        state.taxRateOptions = payload
    },
    clearTaxRateOptions(state) {
        state.taxRateOptions = []
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
    clearBankAccounts(state) {
        state.allBankAccounts = []
        state.visibleBankAccounts = []
    },
    /*----------- company data ------------*/
    modifyCompanyList(state, list) {
        state.companyCategoryList = list
    },
    modifyCompanies(state, pair) {
        state.companies.set(pair.key, pair.value)
    },
    clearCompanyData(state) {
        state.companyCategoryList = []
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
        state.relevantCompanyCategoryList = []
        state.relevantCompanies.clear()
    },
    /* ------- all supplier data -------*/
    modifySupplierData(state, payload) {
        state.suppliers = payload
    },
    clearSupplierData(state) {
        state.suppliers = []
    },
    /*----------- product & model data ------------*/
    modifyModelList(state, list) {
        state.modelCategoryList = list
    },
    modifyModels(state, pair) {
        state.models.set(pair.key, pair.value)
    },
    clearModelData(state) {
        state.modelCategoryList = []
        state.models.clear()
    },
    /* ------- sku data -------*/

    /* ------- factory brand data -------*/
    modifyFactoryBrands(state, payload) {
        state.factoryBrands = payload
    },
    clearFactoryBrands(state) {
        state.factoryBrands = []
    },
    /* ------- measurement unit data -------*/
    modifyMeasurementUnits(state, payload) {
        state.measurementUnits = payload
    },
    clearMeasurementUnits(state) {
        state.measurementUnits = []
    }

}

export default mutations
