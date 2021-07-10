import VueMain from '~/main'

const actions = {
    /* ------- warehouse data -------*/
    getWarehouseOptions(context) {
        if (context.state.warehouseOptions.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.warehouseOptions).then(data => {
            context.commit('modifyWarehouseOptions', data)
        }).catch(() => {})
    },
    /* ------- department data -------*/
    getDepartmentOptions(context) {
        if (context.state.departmentOptions.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.departmentOptions).then(data => {
            context.commit('modifyDepartmentOptions', data)
        }).catch(() => {})
    },
    /* ------- tax rate data -------*/
    getTaxRateOptions(context) {
        if (context.state.taxRateOptions.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.allTaxRates).then(data => {
            const rates = []
            for (const option of data) {
                rates.push(Number(option))
            }
            context.commit('modifyTaxRateOptions', rates)
        }).catch(() => {})
    },
    /* ------ /bankAccount ------*/
    getBankAccounts(context) {
        if (context.state.allBankAccounts.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.allBankAccounts).then(data => {
            context.commit('modifyBankAccounts', data)
        }).catch(() => {})
    },
    /* ------- company data -------*/
    getCompanyList(context) {
        if (context.state.companyCategoryList.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.companyAreas).then(data => {
            context.commit('modifyCompanyList', data)
        }).catch(() => {})
    },
    /* ------- relative company data -------*/
    getRelevantCompanyCategoryList(context) {
        if (context.state.relevantCompanyCategoryList.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.relevantCompanyCategories).then(data => {
            context.commit('modifyRelevantCompanyList', data)
        }).catch(() => {})
    },
    /* ------- all supplier data -------*/
    getAllSuppliers(context) {
        if (context.state.taxRateOptions.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.allSuppliers).then(data => {
            context.commit('modifySupplierData', data)
        }).catch(() => {})
    },
    /* ------- model data -------*/
    getModelCategoryList(context) {
        if (context.state.modelCategoryList.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.modelCategories).then(data => {
            context.commit('modifyModelList', data)
        }).catch(() => {})
    },

    /* ------- factory brand data -------*/
    getFactoryBrands(context) {
        if (context.state.factoryBrands.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.allFactoryBrands).then(data => {
            context.commit('modifyFactoryBrands', data)

        }).catch(() => {})
    },
    /* ------- measurement unit data -------*/
    getMeasurementUnits(context) {
        if (context.state.measurementUnits.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.allUnits).then(data => {
            context.commit('modifyMeasurementUnits', data)
        }).catch(() => {})
    }
}

export default actions
