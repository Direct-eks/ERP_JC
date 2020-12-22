/* --------- api ----------*/

const api = {
    userAuthentication: '/user/userAuthentication',
    userLogout: '/user/userLogout',

    /*------ /company ------*/
    companyList: '/company/companyList', //company category tree structure
    companyByCategory: '/company/companyByCategory', //get company by category in tree structure
    companyByPhone: '/company/companyByPhone', //get company by phone number
    companyByName: '/company/companyByName', //get company by name (abbreviated)
    companyByNameAndPhone: '/company/companyByNameAndPhone', //get company by both name and phone

    relativeCompanyList: '/company/relativeCompanyList', //relative company tree structure
    relativeCompanyByCategory: '/company/relativeCompanyByCategory', //get relative company by category in tree structure

    /*------ /options ------*/
    warehouseOptions: '/options/warehouseOptions', //get options for warehouse
    departmentOptions: '/options/departmentOptions', //get options for department

    /*------ /product ------*/
    productCategoryList: '/product/productCategoryList', //get product category tree structure
    modelByModelCode: '/product/modelByModelCode', //get model by model code (either of both new and/or old)
    modelByCategory: '/product/modelByCategory', //get model by category in tree structure

    stockByModel: '/product/stockByModel', //get stock by model

    warehouseStockByStock: '/product/warehouseStockByStock', //get warehouse stock by stock

    /*------ /entry ------*/
    purchaseOrderByCompany: '/entry/purchaseOrderByCompany',

    newEntryID: '/entry/newEntryID', //get the entryID for this entry to be uploaded
    newInboundEntry: '/entry/newInboundEntry', //put a new entry

    newPurchaseOrder: '/entry/newPurchaseOrder',

    entryByDateRange: '/entry/entryByDateRange', //query entries with given date range
    purchaseOrderByDateRange: '/entry/purchaseOrderByDateRange',
    shippingInfoByEntry: '/entry/shippingInfoByEntry', //query shipping info of a given entry
    entryProductByEntry: '/entry/entryProductsByEntry', //query entry products of a given entry
    entryBySerial: '/entry/entryBySerial', //query the entry with specified serial

    updateEntryInfo: '/entry/updateEntryInfo',
}

export default api
