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
        })
    },
    /* ------- company data -------*/
    getCompanyList(context) {
        if (context.state.companyCategoryList.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.companyAreas).then(data => {
            context.commit('modifyCompanyList', data)
        })
    },
    /* ------- relative company data -------*/
    getRelevantCompanyCategoryList(context) {
        if (context.state.relevantCompanyCategoryList.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.relevantCompanyCategories).then(data => {
            context.commit('modifyRelevantCompanyList', data)
        })
    },
    /* ------- all supplier data -------*/
    getAllSuppliers(context) {
        if (context.state.taxRateOptions.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.allSuppliers).then(data => {
            context.commit('modifySupplierData', data)
        })
    },
    /* ------- model data -------*/
    getModelCategoryList(context) {
        if (context.state.modelCategoryList.length !== 0) {
            return
        }
        VueMain.$getRequest(VueMain.$api.modelCategories).then(data => {
            context.commit('modifyModelList', data)
        })
    }
}

export default actions
