const state = {

    /*  ------- global user data ------- */
    currentUser: null, // current username
    currentUserRole: null,
    currentUserPermissions: [],
    permittedRoundingAmount: '0',
    /* ------- global data for snack bar -------*/
    snackbar: {
        message: '',
        color: '',
    },
    /* ------- global data for overlay -------*/
    overlay: false,
    /* ------- warehouse data -------*/
    warehouseOptions: [],
    /* ------- department data -------*/
    departmentOptions: [],
    /* ------- tax rate data -------*/
    taxRateOptions: [],
    /* ------- invoice type data -------*/
    invoiceTypeOptions: [
        { value: '增值税票', label: '增值税票' },
        { value: '普票', label: '普票' },
        { value: '收据', label: '收据' }
    ],
    /* ------ /bankAccount ------*/
    allBankAccounts: [],
    visibleBankAccounts: [],
    /* ------ /fees data ------*/
    feeCategoryList: [],
    /* ------- company data -------*/
    companyCategoryList: [], // stores the tree structure of company category
    companies: new Map(), // key: categoryID, value: companies
    /* ------- relative company data -------*/
    relevantCompanyCategoryList: [],
    relevantCompanies: new Map(), // key: categoryID, value: relevant companies
    /* ------- all supplier data -------*/
    suppliers: [],
    /* ------- model data -------*/
    modelCategoryList: [],
    models: new Map(), // key: categoryID, value: companies
    /* ------- sku data -------*/
    skus: new Map(), // key modelID, value: skus
    /* ------- factory brand data -------*/
    factoryBrands: [],
    /* ------- measurement unit data -------*/
    measurementUnits: []
}

export default state
