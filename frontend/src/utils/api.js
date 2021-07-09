/* --------- api ----------*/

const api = {
    userNameList: '/user/getUserNameList',
    userAuthentication: '/user/userAuthentication',
    userLogout: '/user/userLogout',

    allUsers: '/user/getAllUsers',
    allRoles: '/user/getAllRoles',
    allPermissions: '/user/getAllPermissions',

    updateUser: '/user/updateUser', // POST
    createNewUser: '/user/createNewUser', // PUT
    deleteUser: '/user/deleteUser', // DELETE

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
    companiesByAreaID: '/company/getCompaniesByAreaID/', // GET
    updateCompanyAreas: '/company/updateCompanyAreas', // POST
    updateCompaniesWithAreaID: '',

    selfCompany: '/company/getSelfCompany', // GET

    relevantCompanyCategories: '/company/getRelevantCompanyCategories', // GET
    relevantCompaniesByCategory: '/company/getRelevantCompaniesByCategory/', // GET {id}
    updateRelevantCompanyCategories: '/company/updateRelevantCompanyCategories', // POST
    updateRelevantCompaniesWithCategoryID: '/company/updateRelevantCompanyWithCategory/', // POST {id}

    /* ------ /department ------*/
    departmentOptions: '/department/getDepartmentOptions', // GET
    updateDepartments: '/department/updateDepartments',

    /* ------ /factoryBrand ------*/
    allFactoryBrands: '/factoryBrand/allFactoryBrands', // GET
    updateFactoryBrands: '/factoryBrand/updateAllFactoryBrands',

    /* ------ /inboundEntry ------*/
    completeInboundEntry: '/inboundEntry/completeEntry', // PATCH
    createInboundEntry: '/inboundEntry/createEntry', // PUT
    // GET startDate= endDate= type= forModify=bool companyID=int
    inboundEntriesInDateRange: '/inboundEntry/getEntriesInDateRange',
    modifyInboundEntry: '/inboundEntry/modifyEntry', // PATCH
    inboundProductsNotCheckedOutByEntryID: '/inboundEntry/getNotCheckedOutProductsByEntryID', // GET entryID= invoiceType=
    inboundProductsNotCheckedOut: '/inboundEntry/getNotCheckedOutProducts', // GET companyID= invoiceType=
    inboundProductsCheckoutAndNotInvoicedByEntryID: '/inboundEntry/getCheckoutAndNotInvoicedProductsByEntryID',
    // GET companyID= invoiceType=
    inboundProductsCheckoutAndNotInvoiced: '/inboundEntry/getCheckoutAndNotInvoicedProducts', // GET companyID= invoiceType=
    returnInboundEntry: '/inboundEntry/returnEntryProducts', // POST
    // GET companyID= shippingCostType=
    inboundEntriesByCompanyAndShippingCostType: '/inboundEntry/getEntriesByCompanyAndShippingCostType',
    inboundNotYetCheckoutSummary: '/inboundEntry/getNotYetCheckoutSummary', // GET
    inboundNotYetCheckoutDetail: '/inboundEntry/getNotYetCheckoutDetailByCompanyID/', // GET companyID
    inboundNotYetInvoiceSummary: '/inboundEntry/getNotYetInvoiceSummary', // GET
    inboundNotYetInvoiceDetail: '/inboundEntry/getNotYetInvoiceDetailByCompanyID/', // GET companyID
    inboundSummary: '/inboundEntry/getInboundSummary', // GET

    /* ------ /invoiceEntry ------*/
    createInvoiceEntry: '/invoiceEntry/createEntry', // PUT
    // GET isInbound, startDate, endDate, companyID, invoiceDate, invoiceNumber,
    // invoiceType, isFollowUpIndication, forModify
    invoiceEntriesInDateRange: '/invoiceEntry/getEntriesInDateRange',
    modifyInvoiceEntry: '/invoiceEntry/modifyEntry', // PATCH

    /* ------ /model ------*/
    modelCategories: '/model/getModelCategories', // GET
    modelsByCategory: '/model/getModelsByCategory/', // GET {id}
    modelsByName: '/model/getModelsByName', // GET name= method=
    exportModels: '/model/exportAllModels', // GET
    updateModelsWithCategory: '/model/updateModelsWithCategory', // POST category= brands=
    updateCategoryOfModel: '/model/updateCategoryOfModel', // POST modelID, oldCategoryID, newCategoryID
    updateModelCategories: '/model/updateModelCategories',

    /* ------ /miscellaneousData ------*/
    allTaxRates: '/miscellaneousData/getAllTaxRateOptions', // GET

    /* ------ /modificationRecord ------*/
    modificationRecordsBySerial: '/modificationRecord/getRecordsBySerial/', // GET {serial}

    /* ------ /moneyEntry ------*/
    createMoneyEntry: '/moneyEntry/createEntry', // PUT
    // GET isInbound, startDate, endDate, companyID, bankAccountID, paymentMethod
    moneyEntriesInDateRange: '/moneyEntry/getEntriesInDateRange',
    moneyEntryBySerial: '/moneyEntry/getEntryBySerial', // GET
    modifyMoneyEntry: '/moneyEntry/modifyEntry', // PATCH

    /* ------ /outboundEntry ------*/
    completeOutboundEntry: '/outboundEntry/completeEntry',
    createOutboundEntry: '/outboundEntry/createEntry',
    outboundEntriesInDateRange: '/outboundEntry/getEntriesInDateRange',
    outboundProductsNotCheckedOutByEntryID: '/outboundEntry/getNotCheckedOutProductsByEntryID', // GET entryID= invoiceType=
    outboundProductsNotCheckedOut: '/outboundEntry/getNotCheckedOutProducts',
    outboundProductsCheckoutAndNotInvoicedByEntryID: '/outboundEntry/getCheckoutAndNotInvoicedProductsByEntryID',
    // GET entryID invoiceType=
    outboundProductsCheckoutAndNotInvoiced: '/outboundEntry/getCheckoutAndNotInvoicedProducts',
    modifyOutboundEntry: '/outboundEntry/modifyEntry',
    returnOutboundEntry: '/outboundEntry/returnEntryProducts', // POST
    // GET companyID= shippingCostType=
    outboundEntriesByCompanyAndShippingCostType: '/outboundEntry/getEntriesByCompanyAndShippingCostType',
    outboundNotYetCheckoutSummary: '/outboundEntry/getNotYetCheckoutSummary', // GET
    outboundNotYetCheckoutDetail: '/outboundEntry/getNotYetCheckoutDetailByCompanyID/', // GET companyID
    outboundNotYetInvoiceSummary: '/outboundEntry/getNotYetInvoiceSummary', // GET
    outboundNotYetInvoiceDetail: '/outboundEntry/getNotYetInvoiceDetailByCompanyID/', // GET companyID
    presaleProducts: '/outboundEntry/getPresaleProducts', // GET
    exportPresaleProducts: '/outboundEntry/exportPresaleProducts', // GET

    /* ------ /purchaseOrder ------*/
    createPurchaseOrder: '/purchaseOrder/createOrder', // PUT
    // GET startDate= endDate= companyID=int
    purchaseOrdersInDateRangeByCompanyID: '/purchaseOrder/getOrdersInDateRangeByCompanyID', // GET
    purchaseOrdersByCompanyID: '/purchaseOrder/getOrdersByCompanyID/', // GET companyID
    modifyPurchaseOrder: '/purchaseOrder/modifyOrder', // PATCH

    /* ------ /quoteEntry ------*/
    createQuote: '/quoteEntry/createQuote', // PUT
    // GET startDate= endDate= companyID=int
    quotesInDateRangeByCompanyID: '/quoteEntry/getQuotesInDateRangeByCompanyID', // GET
    quotesByCompanyID: '/quoteEntry/getQuotesByCompanyID/', // GET companyID
    modifyQuote: '/quoteEntry/modifyQuote', // PATCH

    /* ------ /salesOrder ------*/
    createSalesOrder: '/salesOrder/createOrder', // PUT
    // GET startDate= endDate= companyID=int
    salesOrdersInDateRangeByCompanyID: '/salesOrder/getOrdersInDateRangeByCompanyID', // GET
    salesOrdersByCompanyID: '/salesOrder/getOrdersByCompanyID/', // GET companyID
    modifySalesOrder: '/salesOrder/modifyOrder', // PATCH

    /* ------ /shippingCostEntry ------*/
    createShippingCostEntry: '/shippingCostEntry/createEntry',
    shippingCostEntryInDateRange: '/shippingCostEntry/getEntriesInDateRange',
    modifyShippingCostEntry: '/shippingCostEntry/modifyEntry',

    /* ------ /sku ------*/
    fullSkuByModel: '/sku/getFullSkuByModel/', // GET {id}
    skuByCategoryAndFactoryBrand: '/sku/getSkusByCategoryAndFactoryBrand', // GET categoryID, factoryBrandID
    modifySkuPricing: '/sku/modifySkuPricing', // POST
    updateSku: '/sku/updateSku', // POST
    updateSkuBulk: '/sku/updateSkuBulk', // POST

    /* ------ /supplier ------*/
    allSuppliers: '/supplier/getAllSuppliers', // GET
    supplierInfo: '/supplier/getSupplier/', // GET
    resourceBySupplier: '/supplier/getResourcesBySupplier/', // GET {id}
    supplierResourcesBySku: '/supplier/getSupplierResourcesBySku/', // GET {id}
    deleteResourcesBySupplierID: '/supplier/deleteResourcesBySupplierID', // DELETE {id}

    resourcesByCategoryAndFactoryBrand: '/supplier/resourcesByCategoryAndFactoryBrand', // GET categoryID, factoryBrandID
    createSupplierWithResources: '/supplier/createSupplierWithResources', // POST supplierID, list

    /* ------ /unit ------*/
    allUnits: '/unit/getAllUnits', // GET
    updateUnits: '/unit/updateUnits', // POST

    /* ------ /warehouse ------*/
    warehouseOptions: '/warehouse/getWarehouseOptions',
    updateWarehouses: '/warehouse/updateWarehouses',

    /* ------ /warehouseStock ------*/
    warehouseStockBySKu: '/warehouseStock/getWarehouseStocksBySku/', // GET {id}
    productsByWarehouseStock: '/warehouseStock/getProductsByWarehouseStockID/', // GET {id}

}

export default api
