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

    selfCompany: '/company/getSelfCompany', // GET

    relevantCompanyCategories: '/company/getRelevantCompanyCategories', // GET
    relevantCompaniesByCategory: '/company/getRelevantCompaniesByCategory/', // GET {id}

    /* ------ /department ------*/
    departmentOptions: '/department/getDepartmentOptions', // GET

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
    notYetCheckoutSummary: '/inboundEntry/getNotYetCheckoutSummary', // GET
    notYetCheckoutDetail: '/inboundEntry/getNotYetCheckoutDetailByCompanyID/', // GET companyID
    notYetInvoiceSummary: '/inboundEntry/getNotYetInvoiceSummary', // GET
    notYetInvoiceDetail: '/inboundEntry/getNotYetInvoiceDetailByCompanyID/', // GET companyID

    /* ------ /invoiceEntry ------*/
    createInvoiceEntry: '/invoiceEntry/createEntry', // PUT
    // GET isInbound, startDate, endDate, companyID, invoiceDate, invoiceNumber,
    // invoiceType, isFollowUpIndication, forModify
    invoiceEntriesInDateRange: '/invoiceEntry/getEntriesInDateRange',
    modifyInvoiceEntry: '/invoiceEntry/modifyEntry', // PATCH

    /* ------ /model ------*/
    modelCategories: '/model/getModelCategories', // GET
    modelsByCategory: '/model/getModelsByCategory/', // GET {id}
    modelsByName: '/model/getModelsByName', // GET name= category= method=

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

    /* ------ /warehouse ------*/
    warehouseOptions: '/warehouse/getWarehouseOptions',

    /* ------ /warehouseStock ------*/
    warehouseStockBySKu: '/warehouseStock/getWarehouseStocksBySku/', // GET {id}
    productsByWarehouseStock: '/warehouseStock/getProductsByWarehouseStockID/', // GET {id}

}

export default api
