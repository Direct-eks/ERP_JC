import Vue from 'vue'
import VueRouter from "vue-router";

Vue.use(VueRouter)


// Dynamic Loading Modules

//Pages
// const Login = resolve => { require.ensure(['../views/login/Login.vue            '], ()=>{ resolve(require('../views/login/Login.vue')); }); };
// const Register = resolve => { require.ensure(['../views/register/Register.vue'], ()=>{ resolve(require('../views/register/Register.vue')); }); };
// const Page404 = resolve => { require.ensure(['../views/Page404.vue'], ()=>{ resolve(require('../views/Page404.vue')); }); };
// const Page500 = resolve => { require.ensure(['../views/Page500.vue'], ()=>{ resolve(require('../views/Page500.vue')); }); };

const routes = [
    {
        path: '/',
        redirect: { name: 'home' }
    },
    {
        path: '/login',
        component: () => import(/* webpackChunkName: "Login" */'../views/Login')
    },
    {
        path : '/home',
        name: 'home',
        component: () => import(/* webpackChunkName: "Start" */'../components/Start')
    },
    {
        path: '/inbound_management',
        name: '',
        component: () => import(/* webpackChunkName: "1_Page_inbound_management" */'../views/1_Page_inbound_management'),
        children: [
            {
                path: 'entry_in',
                name: '入库单录入',
                component: () => import(/* webpackChunkName: "Inbound_Management1_Entry_In" */'../views/Inbound_Management/1_Entry_In')
            },
            {
                path: 'completion_in',
                name: '入库单完善',
                component: () => import(/* webpackChunkName: "Inbound_Management2_Completion_In" */'../views/Inbound_Management/2_Completion_In')
            },
            {
                path: 'modify_in',
                name: '入库单修改',
                component: () => import(/* webpackChunkName: "Inbound_Management3_Modify_In" */'../views/Inbound_Management/3_Modify_In')
            },
            {
                path: 'return_in',
                name: '入库单退货',
                component: () => import(/* webpackChunkName: "Inbound_Management4_Return_In" */'../views/Inbound_Management/4_Return_In')
            },
            {
                path: 'query_in',
                name: '入库单查询',
                component: () => import(/* webpackChunkName: "Inbound_Management5_Query_In" */'../views/Inbound_Management/5_Query_In')
            },
            {
                path: 'purchase_order_entry',
                name: '采购订单录入',
                component: () => import(/* webpackChunkName: "Inbound_Management6_Purchase_Order_Entry" */'../views/Inbound_Management/6_Purchase_Order_Entry')
            },
            {
                path: 'purchase_order_query',
                name: '采购订单查询',
                component: () => import(/* webpackChunkName: "Inbound_Management7_Purchase_Order_Query" */'../views/Inbound_Management/7_Purchase_Order_Query')
            },
            {
                path: 'purchase_order_modify',
                name: '采购订单修改',
                component: () => import(/* webpackChunkName: "Inbound_Management8_Purchase_Order_Modify" */'../views/Inbound_Management/8_Purchase_Order_Modify')
            }
        ]
    },
    {
        path: '/outbound_management',
        name: '',
        component: () => import(/* webpackChunkName: "2_Page_outbound_management" */'../views/2_Page_outbound_management'),
        children: [
            {
                path: 'entry_out',
                name: '出库单录入',
                component: () => import(/* webpackChunkName: "Outbound_Management1_Entry_Out" */'../views/Outbound_Management/1_Entry_Out')
            },
            {
                path: 'completion_out',
                name: '出库单完善',
                component: () => import(/* webpackChunkName: "Outbound_Management2_Completion_Out" */'../views/Outbound_Management/2_Completion_Out')
            },
            {
                path: 'modify_out',
                name: '出库单修改',
                component: () => import(/* webpackChunkName: "Outbound_Management3_Modify_Out" */'../views/Outbound_Management/3_Modify_Out')
            },
            {
                path: 'return_out',
                name: '出库单退货',
                component: () => import(/* webpackChunkName: "Outbound_Management4_Return_Out" */'../views/Outbound_Management/4_Return_Out')
            },
            {
                path: 'query_out',
                name: '出库单查询',
                component: () => import(/* webpackChunkName: "Outbound_Management5_Query_Out" */'../views/Outbound_Management/5_Query_Out')
            },
            {
                path: 'quota_entry',
                name: '报价单录入',
                component: () => import(/* webpackChunkName: "Outbound_Management6_Quota_Entry" */'../views/Outbound_Management/6_Quota_Entry')
            },
            {
                path: 'quota_query',
                name: '报价单查询',
                component: () => import(/* webpackChunkName: "Outbound_Management7_Quota_Query" */'../views/Outbound_Management/7_Quota_Query')
            },
            {
                path: 'quota_modify',
                name: '报价单修改',
                component: () => import(/* webpackChunkName: "Outbound_Management8_Quota_Modify" */'../views/Outbound_Management/8_Quota_Modify')
            },
            {
                path: 'sales_order_entry',
                name: '销售订单录入',
                component: () => import(/* webpackChunkName: "Outbound_Management9_Sales_Order_Entry" */'../views/Outbound_Management/9_Sales_Order_Entry')
            },
            {
                path: 'sales_order_query',
                name: '销售订单查询',
                component: () => import(/* webpackChunkName: "Outbound_Management10_Sales_Order_Query" */'../views/Outbound_Management/10_Sales_Order_Query')
            },
            {
                path: 'sales_order_modify',
                name: '销售订单修改',
                component: () => import(/* webpackChunkName: "Outbound_Management11_Sales_Order_Modify" */'../views/Outbound_Management/11_Sales_Order_Modify')
            }
        ]
    },
    {
        path: '/inbound_invoicing',
        name: '',
        component: () => import(/* webpackChunkName: "3_Page_inbound_invoicing" */'../views/3_Page_inbound_invoicing'),
        children: [
            {
                path: 'inbound_checkout_entry',
                name: '入库结账单录入',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing1_Checkout_Entry" */'../views/Inbound_Invoicing/1_Checkout/1_Checkout_Entry')
            },
            {
                path: 'inbound_checkout_query',
                name: '入库结账单查询',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing2_Checkout_Query" */'../views/Inbound_Invoicing/1_Checkout/2_Checkout_Query')
            },
            {
                path: 'inbound_checkout_modify',
                name: '入库结账单修改',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing3_Checkout_Modify" */'../views/Inbound_Invoicing/1_Checkout/3_Checkout_Modify')
            },
            {
                path: 'inbound_not_checkout_query',
                name: '入库未结账查询',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing4_Not_Checkout_Query" */'../views/Inbound_Invoicing/1_Checkout/4_Not_Checkout_Query')
            },
            {
                path: 'inbound_checkout_return',
                name: '入库结账后退货',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing5_Checkout_Return" */'../views/Inbound_Invoicing/1_Checkout/5_Checkout_Return')
            },
            {
                path: 'inbound_invoice_entry',
                name: '入库结账单开票录入',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing1_Invoice_Entry" */'../views/Inbound_Invoicing/2_Invoice/1_Invoice_Entry')
            },
            {
                path: 'inbound_invoice_query',
                name: '入库结账单开票查询',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing2_Invoice_Query" */'../views/Inbound_Invoicing/2_Invoice/2_Invoice_Query')
            },
            {
                path: 'inbound_invoice_modify',
                name: '入库结账单开票修改',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing3_Invoice_Modify" */'../views/Inbound_Invoicing/2_Invoice/3_Invoice_Modify')
            },
            {
                path: 'inbound_not_invoice_query',
                name: '入库结账未开票查询',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing4_Not_Invoice_Query" */'../views/Inbound_Invoicing/2_Invoice/4_Not_Invoice_Query')
            },
            {
                path: 'payment_entry',
                name: '付款单录入',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing1_Payment_Entry" */'../views/Inbound_Invoicing/3_Payment/1_Payment_Entry')
            },
            {
                path: 'payment_query',
                name: '付款单查询',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing3_Payment_Query" */'../views/Inbound_Invoicing/3_Payment/3_Payment_Query')
            },
            {
                path: 'payment_modify',
                name: '付款单修改',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing2_Payment_Modify" */'../views/Inbound_Invoicing/3_Payment/2_Payment_Modify')
            },
            {
                path: 'shipping_cost_checkout',
                name: 'IN付运费结账',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing1_Shipping_Cost_Checkout" */'../views/Inbound_Invoicing/4_Shipping_Cost/1_Shipping_Cost_Checkout')
            },
            {
                path: 'shipping_cost_query',
                name: 'IN付运费查询',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing2_Shipping_Cost_Query" */'../views/Inbound_Invoicing/4_Shipping_Cost/2_Shipping_Cost_Query')
            },
            {
                path: 'shipping_cost_modify',
                name: 'IN付运费修改',
                component: () => import(/* webpackChunkName: "Inbound_Invoicing3_Shipping_Cost_Modify" */'../views/Inbound_Invoicing/4_Shipping_Cost/3_Shipping_Cost_Modify')
            },
        ]
    },
    {
        path: '/outbound_invoicing',
        name: '',
        component: () => import(/* webpackChunkName: "4_Page_outbound_invoicing" */'../views/4_Page_outbound_invoicing'),
        children: [
            {
                path: 'outbound_check_entry',
                name: '出库结账单录入',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing1_Out_Check_Entry" */'../views/Outbound_Invoicing/1_Outbound_Check/1_Checkout_Entry')
            },
            {
                path: 'outbound_check_query',
                name: '出库结账单查询',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing2_Out_Check_Query" */'../views/Outbound_Invoicing/1_Outbound_Check/2_Checkout_Query')
            },
            {
                path: 'outbound_check_modify',
                name: '出库结账单修改',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing3_Out_Check_Modify" */'../views/Outbound_Invoicing/1_Outbound_Check/3_Checkout_Modify')
            },
            {
                path: 'outbound_not_checkout_query',
                name: '出库未结账查询',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing4_Not_Checkout_Query" */'../views/Outbound_Invoicing/1_Outbound_Check/4_Not_Checkout_Query')
            },
            {
                path: 'outbound_checkout_return',
                name: '出库结账后退货',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing5_Checkout_Return" */'../views/Outbound_Invoicing/1_Outbound_Check/5_Checkout_Return')
            },
            {
                path: 'outbound_check_invoicing_entry',
                name: '出库结账单开票录入',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing1_Invoice_Entry" */'../views/Outbound_Invoicing/2_Outbound_Invoicing/1_Invoice_Entry')
            },
            {
                path: 'outbound_check_invoicing_query',
                name: '出库结账单开票查询',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing2_Invoice_Query" */'../views/Outbound_Invoicing/2_Outbound_Invoicing/2_Invoice_Query')
            },
            {
                path: 'outbound_check_invoicing_modify',
                name: '出库结账单开票修改',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing3_Invoice_Modify" */'../views/Outbound_Invoicing/2_Outbound_Invoicing/3_Invoice_Modify')
            },
            {
                path: 'outbound_not_invoice_query',
                name: '出库结账未开票查询',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing4_Not_Invoice_Query" */'../views/Outbound_Invoicing/2_Outbound_Invoicing/4_Not_Invoice_Query')
            },
            {
                path: 'receipt_entry',
                name: '收款单录入',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing1_Rec_Entry" */'../views/Outbound_Invoicing/3_Receipt/1_Rec_Entry')
            },
            {
                path: 'receipt_query',
                name: '收款单查询',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing3_Rec_Query" */'../views/Outbound_Invoicing/3_Receipt/3_Rec_Query')
            },
            {
                path: 'receipt_modify',
                name: '收款单修改',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing2_Rec_Modify" */'../views/Outbound_Invoicing/3_Receipt/2_Rec_Modify')
            },
            {
                path: 'cost_bill',
                name: 'OUT收运费结账',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing1_Out_Cost_Bill" */'../views/Outbound_Invoicing/4_Shipping_Cost/1_Shipping_Cost_Checkout')
            },
            {
                path: 'cost_query',
                name: 'OUT收运费查询',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing2_Out_Cost_Query" */'../views/Outbound_Invoicing/4_Shipping_Cost/2_Shipping_Cost_Query')
            },
            {
                path: 'cost_modify',
                name: 'OUT收运费修改',
                component: () => import(/* webpackChunkName: "Outbound_Invoicing3_Out_Cost_Modify" */'../views/Outbound_Invoicing/4_Shipping_Cost/3_Shipping_Cost_Modify')
            },
        ]
    },
    {
        path: '/stock_management',
        name: '',
        component: () => import('../views/5_Page_stock_management'),
        children: [
            {
                path: 'product_detail',
                name: '商品明细',
                component: () => import('../views/Stock_Management/Product_Detail')
            },
            {
                path: 'stock_report',
                name: '库存报表',
                component: () => import('../views/Stock_Management/Stock_Report')
            },
            {
                path: 'product_pricing',
                name: '商品定价',
                component: () => import('../views/Stock_Management/Product_Pricing')
            },
            {
                path: 'stock_alert',
                name: '库存报警',
                component: () => import('../views/Stock_Management/Stock_Alert')
            },
            {
                path: 'detail_stats',
                name: '明细统计',
                component: () => import('../views/Stock_Management/Detail_Stats')
            },
            {
                path: 'inventory',
                name: '库存盘点',
                component: import('../views/Stock_Management/Inventory')
            },
            {
                path: 'presales_query',
                name: '预销售资源',
                component: () => import('../views/Stock_Management/Presales_Query')
            },
            {
                path: 'stock_resources',
                name: '库存资源',
                component: () => import('../views/Stock_Management/Stock_Resources')
            }
        ]
    },
    {
        path: '/production_management',
        name: '',
        component: () => import('../views/6_Page_production_management'),
        children: [
            {
                path: 'material_apply_entry',
                name: '领料单录入',
                component: () => import('../views/Production_Management/Material_Apply/MA_Entry')
            },
            {
                path: 'material_apply_query',
                name: '领料单查询',
                component: () => import('../views/Production_Management/Material_Apply/MA_Query')
            },
            {
                path: 'material_apply_modify',
                name: '领料单修改',
                component: () => import('../views/Production_Management/Material_Apply/MA_Modify')
            },
            {
                path: 'material_return_entry',
                name: '退料单录入',
                component: () => import('../views/Production_Management/Material_Return/MR_Entry')
            },
            {
                path: 'material_return_query',
                name: '退料单查询',
                component: () => import('../views/Production_Management/Material_Return/MR_Query')
            },
            {
                path: 'material_return_modify',
                name: '退料单修改',
                component: () => import('../views/Production_Management/Material_Return/MR_Modify')
            },
            {
                path: 'warehouse_entry_entry',
                name: '产成品入库单录入',
                component: () => import('../views/Production_Management/Warehousing_Entry/WE_Entry')
            },
            {
                path: 'warehouse_entry_query',
                name: '产成品入库单查询',
                component: () => import('../views/Production_Management/Warehousing_Entry/WE_Query')
            },
            {
                path: 'warehouse_entry_modify',
                name: '产成品入库单修改',
                component: () => import('../views/Production_Management/Warehousing_Entry/WE_Modify')
            },
            {
                path: 'assembly_entry',
                name: '拆/组装单录入',
                component: () => import('../views/Production_Management/Assembly_Entry/AE_Entry')
            },
            {
                path: 'assembly_query',
                name: '拆/组装单查询',
                component: () => import('../views/Production_Management/Assembly_Entry/AE_Query')
            },
            {
                path: 'assembly_modify',
                name: '拆/组装单修改',
                component: () => import('../views/Production_Management/Assembly_Entry/AE_Modify')
            },
            {
                path: 'sporadic_profit_entry',
                name: '零星盘盈单录入',
                component: () => import('../views/Production_Management/Sporadic_Profit/SP_Entry')
            },
            {
                path: 'sporadic_profit_query',
                name: '零星盘盈单查询',
                component: () => import('../views/Production_Management/Sporadic_Profit/SP_Query')
            },
            {
                path: 'sporadic_profit_modify',
                name: '零星盘盈单修改',
                component: () => import('../views/Production_Management/Sporadic_Profit/SP_Modify')
            },
            {
                path: 'sporadic_loss_entry',
                name: '零星盘亏单录入',
                component: () => import('../views/Production_Management/Sporadic_Loss/SL_Entry')
            },
            {
                path: 'sporadic_loss_query',
                name: '零星盘亏单查询',
                component: () => import('../views/Production_Management/Sporadic_Loss/SL_Query')
            },
            {
                path: 'sporadic_loss_modify',
                name: '零星盘亏单修改',
                component: () => import('../views/Production_Management/Sporadic_Loss/SL_Modify')
            },
            {
                path: 'distribution_entry',
                name: '调拨单录入',
                component: () => import('../views/Production_Management/Distribution/D_Entry')
            },
            {
                path: 'distribution_query',
                name: '调拨单查询',
                component: () => import('../views/Production_Management/Distribution/D_Query')
            },
            {
                path: 'distribution_modify',
                name: '调拨单修改',
                component: () => import('../views/Production_Management/Distribution/D_Modify')
            },
            {
                path: 'scrap_entry',
                name: '报废单录入',
                component: () => import('../views/Production_Management/Scrap/S_Entry')
            },
            {
                path: 'scrap_query',
                name: '报废单查询',
                component: () => import('../views/Production_Management/Scrap/S_Query')
            },
            {
                path: 'scrap_modify',
                name: '报废单修改',
                component: () => import('../views/Production_Management/Scrap/S_Modify')
            }
        ]
    },
    {
        path: '/accounts_management',
        name: '账目管理',
        component: () => import('../views/7_Page_accounts_management')
    },
    {
        path: '/assets_management',
        name: '资产管理',
        component: () => import('../views/8_Page_assets_management')
    },
    {
        path: '/query_stats',
        name: '查询统计',
        component: () => import('../views/9_Page_query_stats'),
        children: [
            {
                path: 'in_summary_stats',
                name: '入库单汇总统计',
                component: () => import('../views/Query_Stats/In_or_Pay/In_Summary_Stats')
            },
            {
                path: 'in_check_summary_stats',
                name: '入库结账单汇总统计',
                component: () => import('../views/Query_Stats/In_or_Pay/In_Check_Summary_Stats')
            },
            {
                path: 'in_check_audit',
                name: '入库结账单审核',
                component: () => import('../views/Query_Stats/In_or_Pay/In_Check_Audit')
            },
            {
                path: 'pay_summary_query',
                name: '应付款汇总查询',
                component: () => import('../views/Query_Stats/In_or_Pay/Pay_Summary_Query')
            },
            {
                path: 'pay_detail',
                name: '应付款明细',
                component: () => import('../views/Query_Stats/In_or_Pay/Pay_Detail')
            },
            {
                path: 'pay_ledger',
                name: '应付款总账',
                component: () => import('../views/Query_Stats/In_or_Pay/Pay_Ledger')
            },
            {
                path: 'out_summary_stats',
                name: '出库单汇总统计',
                component: () => import('../views/Query_Stats/Out_or_Receive/Out_Summary_Stats')
            },
            {
                path: 'out_check_summary_stats',
                name: '出库结账单汇总统计',
                component: () => import('../views/Query_Stats/Out_or_Receive/Out_Check_Summary_Stats')
            },
            {
                path: 'out_check_audit',
                name: '出库结账单审核',
                component: () => import('../views/Query_Stats/Out_or_Receive/Out_Check_Audit')
            },
            {
                path: 'rec_summary_query',
                name: '应收款汇总查询',
                component: () => import('../views/Query_Stats/Out_or_Receive/Rec_Summary_Stats')
            },
            {
                path: 'rec_detail',
                name: '应收款明细',
                component: () => import('../views/Query_Stats/Out_or_Receive/Rec_Detail')
            },
            {
                path: 'rec_ledger',
                name: '应收款总账',
                component: () => import('../views/Query_Stats/Out_or_Receive/Rec_Ledger')
            },
            {
                path: 'price_diff_stats',
                name: '销售价差统计',
                component: () => import('../views/Query_Stats/Others/Price_Diff_Stats')
            },
            {
                path: 'yearly_stats',
                name: '年度出入库统计',
                component: () => import('../views/Query_Stats/Others/Yearly_Stats')
            },
        ]
    },
    {
        path: '/maintenance',
        name: '维护',
        component: () => import('../views/10_Page_maintenance')
    },
    {
        path: '/resources_entry',
        name: '资源录入',
        component: () => import('../views/11_Page_resources_entry')
    },
    {
        path: '/accessibility',
        name: '辅助功能',
        component: () => import('../views/12_Page_accessibility')
    },
    {
        path: '/help',
        name: '帮助',
        component: () => import('../views/13_Page_help')
    },

    //Redirect to Home
    // { path: '/', redirect: { name: 'home' } },

    // 404 redirect to home
    { path: '*', redirect: { name: 'home'}  }
];

/*--------------Vue Router------------*/
const router = new VueRouter({
    routes,
    linkActiveClass: 'open active',
    scrollBehavior: () => ({ y: 0 }),
    mode: 'hash'
});

router.beforeEach((to, from, next) => {
    console.log(from.path, to.path)


    // if path to /login and is not authenticated (for logout, or debug with no authen info)
    if (to.path === '/login' &&
        (!sessionStorage.getItem('isAuthenticated') ||
            sessionStorage.getItem('isAuthenticated') === "false")) {
        console.log('to login router page')
        next()
        return
    }
    if (to.path === '/login') { //forbid going to login
        console.log('forbid to login page')
        next('/home')
        return
    }
    /* ----------- disabled for debugging ----------- */
    // if (!sessionStorage.getItem('isAuthenticated') ||
    //             sessionStorage.getItem('isAuthenticated') === 'false') {
    //     console.log('not authenticated')
    //     next('/login')
    //     return
    // }
    console.log('ddd')
    next()
})

export default router
