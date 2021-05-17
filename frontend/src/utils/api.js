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
    companiesByCategory: '/company/getCompaniesByCategory/', // GET

    selfCompany: '/company/getSelfCompany', // GET

    relevantCompanyCategories: '/company/getRelevantCompanyCategories', // GET
    relevantCompaniesByCategory: '/company/getRelevantCompaniesByCategory/', // GET {id}

    /* ------ /department ------*/
    departmentOptions: '/department/getDepartmentOptions', // GET

    /* ------ /factoryBrand ------*/
    allFactoryBrands: '/factoryBrand/allFactoryBrands', // GET

    /* ------ /inboundEntry ------*/
    completeInboundEntry: '/inboundEntry/completeEntry', // PATCH
    createInboundEntry: '/inboundEntry/createEntry', // PUT
    // GET startDate= endDate= type= forModify=bool companyID=int
    inboundEntriesInDateRange: '/inboundEntry/getEntriesInDateRange',
    modifyInboundEntry: '/inboundEntry/modifyEntry', // PATCH
    inboundProductsNotCheckedOut: '/inboundEntry/getNotCheckedOutProducts', // GET companyID= invoiceType=
    inboundProductsCheckoutAndNotInvoiced: '/inboundEntry/getCheckoutAndNotInvoicedProducts', // GET companyID= invoiceType=
    returnInboundEntry: '/inboundEntry/returnEntryProducts', // POST
    // GET companyID= shippingCostType=
    inboundEntriesByCompanyAndShippingCostType: '/inboundEntry/getEntriesByCompanyAndShippingCostType',
    inboundNotYetCheckoutSummary: '/inboundEntry/getNotYetCheckoutSummary', // GET
    inboundNotYetCheckoutDetail: '/inboundEntry/getNotYetCheckoutDetailByCompanyID/', // GET companyID
    inboundNotYetInvoiceSummary: '/inboundEntry/getNotYetInvoiceSummary', // GET
    inboundNotYetInvoiceDetail: '/inboundEntry/getNotYetInvoiceDetailByCompanyID/', // GET companyID

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
    outboundProductsNotCheckedOut: '/outboundEntry/getNotCheckedOutProducts',
    outboundProductsCheckoutAndNotInvoiced: '/outboundEntry/getCheckoutAndNotInvoicedProducts',
    modifyOutboundEntry: '/outboundEntry/modifyEntry',
    returnOutboundEntry: '/outboundEntry/returnEntryProducts', // POST
    // GET companyID= shippingCostType=
    outboundEntriesByCompanyAndShippingCostType: '/outboundEntry/getEntriesByCompanyAndShippingCostType',
    outboundNotYetCheckoutSummary: '/outboundEntry/getNotYetCheckoutSummary', // GET
    outboundNotYetCheckoutDetail: '/outboundEntry/getNotYetCheckoutDetailByCompanyID/', // GET companyID
    outboundNotYetInvoiceSummary: '/outboundEntry/getNotYetInvoiceSummary', // GET
    outboundNotYetInvoiceDetail: '/outboundEntry/getNotYetInvoiceDetailByCompanyID/', // GET companyID

    /* ------ /purchaseOrder ------*/
    createPurchaseOrder: '/purchaseOrder/createOrder', // PUT
    // GET startDate= endDate= companyID=int
    purchaseOrdersInDateRangeByCompanyID: '/purchaseOrder/getOrdersInDateRangeByCompanyID', // GET
    purchaseOrdersByCompanyID: '/purchaseOrder/getOrdersByCompanyID/', // GET companyID
    modifyPurchaseOrder: '/purchaseOrder/modifyOrder', // PATCH

    /* ------ /quotaEntry ------*/
    createQuota: '/quotaEntry/createOrder', // PUT
    // GET startDate= endDate= companyID=int
    quotaInDateRangeByCompanyID: '/quotaEntry/getOrdersInDateRangeByCompanyID', // GET
    quotaByCompanyID: '/quotaEntry/getOrdersByCompanyID/', // GET companyID
    modifyQuota: '/quotaEntry/modifyOrder', // PATCH

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

    /* ------ /supplier ------*/
    allSuppliers: '/supplier/getAllSuppliers', // GET
    resourceBySupplier: '/supplier/getResourcesBySupplier/', // GET {id}
    supplierResourcesBySku: '/supplier/getSupplierResourcesBySku/', // GET {id}

    /* ------ /unit ------*/
    allUnits: '/unit/getAllUnits', // GET

    /* ------ /warehouse ------*/
    warehouseOptions: '/warehouse/getWarehouseOptions',

    /* ------ /warehouseStock ------*/
    warehouseStockBySKu: '/warehouseStock/getWarehouseStocksBySku/', // GET {id}
    productsByWarehouseStock: '/warehouseStock/getProductsByWarehouseStockID/', // GET {id}

}

export default api
