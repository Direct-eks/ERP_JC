/* --------- api ----------*/

const api = {
    userAuthentication: '/user/userAuthentication',
    userLogout: '/user/userLogout',

    /*------ /company ------*/
    companyFuzzySearch: '/company/getCompanyByFuzzySearch?', //GET name= phone=
    companyAreas: '/company/getCompanyAreas', //GET
    companiesByCategory: '/company/getCompaniesByCategory/', //GET

    relevantCompanyCategories: '/company/getRelevantCompanyCategories', //GET
    relevantCompaniesByCategory: '/company/getRelevantCompaniesByCategory/', //GET {id}

    /*------ /department ------*/
    departmentOptions: '/department/getDepartmentOptions', //GET

    /*------ /model ------*/
    modelCategories: '/model/getModelCategories', //GET
    modelsByCategory: '/model/getModelsByCategory/', //GET {id}

    /*------ /inboundEntry ------*/
    completeEntry: '/inboundEntry/completeEntry', //PATCH
    createEntry: '/inboundEntry/createEntry', //PUT

    //GET startDate= endDate= type= forModify=bool companyID=int
    entriesInDateRange: '/inboundEntry/getEntriesInDateRange?',
    modifyEntry: '/inboundEntry/modifyEntry', //PATCH

    /*------ /sku ------*/
    fullSkuByModel: '/sku/getFullSkuByModel/', //GET {id}

    /*------ /warehouse ------*/
    warehouseOptions: '/warehouse/getWarehouseOptions',

    /*------ /warehouseStock ------*/
    warehouseStockBySKu: '/warehouseStock/getWarehouseStocksBySku/', //GET {id}

}

export default api
