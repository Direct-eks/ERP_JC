/* --------- api ----------*/

const api = {
    userAuthentication: '/user/userAuthentication',
    userLogout: '/user/userLogout',

    /* ------ /bankAccount ------*/
    visibleBankAccounts: '/bankAccount/getVisibleAccounts', // GET

    /* ------ /checkoutEntry ------*/
    createCheckoutEntry: '/checkoutEntry/createEntry', // PUT
    // GET startDate= endDate=  companyID= invoiceType=
    checkoutEntriesInDateRange: '/checkoutEntry/getEntriesInDateRange',
    modifyCheckoutEntry: '/checkoutEntry/modifyEntry', // PATCH

    /* ------ /company ------*/
    companyFuzzySearch: '/company/getCompanyByFuzzySearch', // GET name= phone=
    companyAreas: '/company/getCompanyAreas', // GET
    companiesByCategory: '/company/getCompaniesByCategory/', // GET

    relevantCompanyCategories: '/company/getRelevantCompanyCategories', // GET
    relevantCompaniesByCategory: '/company/getRelevantCompaniesByCategory/', // GET {id}

    /* ------ /department ------*/
    departmentOptions: '/department/getDepartmentOptions', // GET

    /* ------ /inboundEntry ------*/
    completeInboundEntry: '/inboundEntry/completeEntry', // PATCH
    createInboundEntry: '/inboundEntry/createEntry', // PUT
    // GET startDate= endDate= type= forModify=bool companyID=int
    inboundEntriesInDateRange: '/inboundEntry/getEntriesInDateRange',
    modifyEntry: '/inboundEntry/modifyEntry', // PATCH
    inboundProductsByCompanyAndInvoiceType: '/inboundEntry/getProductsByCompanyAndInvoiceType', // GET companyID= invoiceType=

    /* ------ /model ------*/
    modelCategories: '/model/getModelCategories', // GET
    modelsByCategory: '/model/getModelsByCategory/', // GET {id}

    /* ------ /modificationRecord ------*/
    modificationRecordsBySerial: '/modificationRecord/getRecordsBySerial/', // GET {serial}

    /* ------ /purchaseOrder ------*/
    createPurchaseOrder: '/purchaseOrder/createOrder', // PUT
    // GET startDate= endDate= companyID=int
    purchaseOrdersInDateRangeByCompanyID: '/purchaseOrder/getOrdersInDateRangeByCompanyID', // GET
    modifyPurchaseOrder: '/purchaseOrder/modifyOrder', // PATCH

    /* ------ /sku ------*/
    fullSkuByModel: '/sku/getFullSkuByModel/', // GET {id}

    /* ------ /warehouse ------*/
    warehouseOptions: '/warehouse/getWarehouseOptions',

    /* ------ /warehouseStock ------*/
    warehouseStockBySKu: '/warehouseStock/getWarehouseStocksBySku/', // GET {id}

}

export default api
