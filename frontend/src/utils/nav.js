export default {
    items: [
        {
            name: '入库管理',
            url: '/inbound_management',
        },
        {
            name: '出库管理',
            url: '/outbound_management',
        },
        {
            name: '入库结账管理',
            url: '/inbound_invoicing',
        },
        {
            name: '出库结账管理',
            url: '/outbound_invoicing',
        },
        {
            name: '库存管理',
            url: '/stock_management',
        },
        {
            name: '生产管理',
            url: '/production_management',
        },
        {
            name: '账目管理',
            url: '/accounts_management',
        },
        {
            name: '固定资产管理',
            url: '/assets_management',
        },
        {
            name: '查询统计',
            url: '/query_stats',
        },
        {
            name: '资源录入',
            url: '/resources',
        },
        {
            name: '系统标准',
            url: '/system',
        },
        {
            name: '系统维护',
            url: '/maintenance',
        },
        {
            name: '辅助功能',
            url: '/accessibility',
        },
        {
            name: '帮助',
            url: '/help',
        }
    ],
    inbound_management_nav: [
        {
            name: '入库单',
            url: '/inbound_management/',
            children: [
                {
                    name: '录入',
                    url: '/inbound_management/entry_in',
                    requiredPermission: 'inboundEntry:Creation',
                },
                {
                    name: '完善',
                    url: '/inbound_management/completion_in',
                    requiredPermission: 'inboundEntry:Completion',
                },
                {
                    name: '修改',
                    url: '/inbound_management/modify_in',
                    requiredPermission: 'inboundEntry:Modification',
                },
                {
                    name: '退货',
                    url: '/inbound_management/return_in',
                    requiredPermission: 'inboundEntry:Return',
                },
                {
                    name: '查询',
                    url: '/inbound_management/query_in',
                    requiredPermission: 'inboundEntry:Query',
                },
            ]
        },
        {
            name: '采购订单',
            url: '/inbound_management/purchase_order',
            children: [
                {
                    name: '录入',
                    url: '/inbound_management/purchase_order_entry',
                    requiredPermission: 'purchaseOrder:Creation',
                },
                {
                    name: '查询',
                    url: '/inbound_management/purchase_order_query',
                    requiredPermission: 'purchaseOrder:Query',
                },
                {
                    name: '修改',
                    url: '/inbound_management/purchase_order_modify',
                    requiredPermission: 'purchaseOrder:Modification',
                }
            ]
        }
    ],
    outbound_management_nav: [
        {
            name: '出库单',
            url: '/outbound_management/',
            children: [
                {
                    name: '录入',
                    url: '/outbound_management/entry_out',
                    requiredPermission: 'outboundEntry:Creation',
                },
                {
                    name: '完善',
                    url: '/outbound_management/completion_out',
                    requiredPermission: 'outboundEntry:Completion',
                },
                {
                    name: '修改',
                    url: '/outbound_management/modify_out',
                    requiredPermission: 'outboundEntry:Modification',
                },
                {
                    name: '退货',
                    url: '/outbound_management/return_out',
                    requiredPermission: 'outboundEntry:Return',
                },
                {
                    name: '查询',
                    url: '/outbound_management/query_out',
                    requiredPermission: 'outboundEntry:Query',
                },
            ]
        },
        {
            name: '销售订单',
            url: '/outbound_management/sales_order',
            children: [
                {
                    name: '录入',
                    url: '/outbound_management/sales_order_entry',
                    requiredPermission: 'salesOrder:Creation',
                },
                {
                    name: '查询',
                    url: '/outbound_management/sales_order_query',
                    requiredPermission: 'salesOrder:Query',
                },
                {
                    name: '修改',
                    url: '/outbound_management/sales_order_modify',
                    requiredPermission: 'salesOrder:Modification',
                },
            ]
        },
        {
            name: '报价单',
            url: '/outbound_management/quota',
            children: [
                {
                    name: '录入',
                    url: '/outbound_management/quota_entry',
                    requiredPermission: 'quota:Creation',
                },
                {
                    name: '查询',
                    url: '/outbound_management/quota_query',
                    requiredPermission: 'quota:Query',
                },
                {
                    name: '修改',
                    url: '/outbound_management/quota_modify',
                    requiredPermission: 'quota:Modification',
                },
            ]
        },
    ],
    inbound_invoicing_nav: [
        {
            name: '入库结账单',
            url: '/inbound_invoicing/inbound_checkout',
            children: [
                {
                    name: '录入',
                    url: '/inbound_invoicing/inbound_checkout_entry',
                    requiredPermission: 'inboundCheckout:Creation',
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/inbound_checkout_query',
                    requiredPermission: 'inboundCheckout:Query',
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/inbound_checkout_modify',
                    requiredPermission: 'inboundCheckout:Modification',
                },
                {
                    name: '入库未结账查询',
                    url: '/inbound_invoicing/inbound_not_checkout_query',
                    requiredPermission: 'inboundCheckout:NotCheckoutQuery',
                },
                {
                    name: '结账后退货',
                    url: '/inbound_invoicing/inbound_checkout_return',
                    requiredPermission: 'inboundCheckout:Return',
                },
            ]
        },
        {
            name: '入库结账单开票',
            url: '/inbound_invoicing/inbound_invoicing',
            children: [
                {
                    name: '录入',
                    url: '/inbound_invoicing/inbound_invoice_entry',
                    requiredPermission: 'inboundInvoice:Creation',
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/inbound_invoice_query',
                    requiredPermission: 'inboundInvoice:Query',
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/inbound_invoice_modify',
                    requiredPermission: 'inboundInvoice:Modification',
                },
                {
                    name: '结账未开票查询',
                    url: '/inbound_invoicing/inbound_not_invoice_query',
                    requiredPermission: 'inboundInvoice:NotInvoiceQuery',
                },
            ]
        },
        {
            name: '付款单',
            url: '/inbound_invoicing/payment',
            children: [
                {
                    name: '录入',
                    url: '/inbound_invoicing/payment_entry',
                    requiredPermission: 'inboundPayment:Creation',
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/payment_query',
                    requiredPermission: 'inboundPayment:Query',
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/payment_modify',
                    requiredPermission: 'inboundPayment:Modification',
                }
            ]
        },
        {
            name: '付运费',
            url: '/inbound_invoicing/shipping_cost',
            children: [
                {
                    name: '结账',
                    url: '/inbound_invoicing/shipping_cost_checkout',
                    requiredPermission: 'inboundShippingCost:Creation',
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/shipping_cost_query',
                    requiredPermission: 'inboundShippingCost:Query',
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/shipping_cost_modify',
                    requiredPermission: 'inboundShippingCost:Modification',
                }
            ]
        },
    ],
    outbound_invoicing_nav: [
        {
            name: '出库结账单',
            url: '/outbound_invoicing/outbound_check',
            children: [
                {
                    name: '录入',
                    url: '/outbound_invoicing/outbound_check_entry',
                    requiredPermission: 'outboundCheckout:Creation',
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/outbound_check_query',
                    requiredPermission: 'outboundCheckout:Query',
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/outbound_check_modify',
                    requiredPermission: 'outboundCheckout:Modification',
                },
                {
                    name: '出库未结账查询',
                    url: '/outbound_invoicing/outbound_not_checkout_query',
                    requiredPermission: 'outboundCheckout:NotCheckoutQuery',
                },
                {
                    name: '结账后退货',
                    url: '/outbound_invoicing/outbound_checkout_return',
                    requiredPermission: 'outboundCheckout:Return',
                }
            ]
        },
        {
            name: '出库结账单开票',
            url: '/outbound_invoicing/outbound_check_invoicing',
            children: [
                {
                    name: '录入',
                    url: '/outbound_invoicing/outbound_check_invoicing_entry',
                    requiredPermission: 'outboundInvoice:Creation',
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/outbound_check_invoicing_query',
                    requiredPermission: 'outboundInvoice:Query',
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/outbound_check_invoicing_modify',
                    requiredPermission: 'outboundInvoice:Modification',
                },
                {
                    name: '结账未开票查询',
                    url: '/outbound_invoicing/outbound_not_invoice_query',
                    requiredPermission: 'outboundInvoice:NotInvoiceQuery',
                },
            ]
        },
        {
            name: '收款单',
            url: '/outbound_invoicing/receipt',
            children: [
                {
                    name: '录入',
                    url: '/outbound_invoicing/receipt_entry',
                    requiredPermission: 'outboundPayment:Creation',
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/receipt_query',
                    requiredPermission: 'outboundPayment:Query',
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/receipt_modify',
                    requiredPermission: 'outboundPayment:Modification',
                }
            ]
        },
        {
            name: '收运费',
            url: '/outbound_invoicing/shipping_cost',
            children: [
                {
                    name: '结账',
                    url: '/outbound_invoicing/cost_bill',
                    requiredPermission: 'outboundShippingCost:Creation',
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/cost_query',
                    requiredPermission: 'outboundShippingCost:Query',
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/cost_modify',
                    requiredPermission: 'outboundShippingCost:Modification',
                }
            ]
        },
    ],
    stock_management_nav: [
        {
            name: '商品明细',
            url: '/stock_management/products_details',
            requiredPermission: 'management:ProductsDetails',
        },
        {
            name: '库存报表',
            url: '/stock_management/stock_report',
            requiredPermission: 'management:StockReport',
        },
        {
            name: '商品定价',
            url: '/stock_management/products_pricing',
            requiredPermission: 'management:ProductsPricing',
        },
        {
            name: '库存报警',
            url: '/stock_management/stock_alert',
            requiredPermission: 'management:StockAlert',
        },
        {
            name: '明细统计',
            url: '/stock_management/detailed_stats',
            requiredPermission: 'management:DetailStats',
        },
        {
            name: '库存盘点',
            url: '/stock_management/inventory',
            requiredPermission: 'management:Inventory',
        },
        {
            name: '预销售资源',
            url: '/stock_management/presales_query',
            requiredPermission: 'management:PresalesQuery',
        },
        {
            name: '库存资源',
            url: '/stock_management/stock_resources',
            requiredPermission: 'management:StockResources',
        },
        {
            name: '架位设置',
            url: '/stock_management/storage_place',
            requiredPermission: 'management:StoragePlace',
        }
    ],
    production_management_nav: [
        {
            name: '领料单',
            url: '/production_management/material_apply',
            children: [
                {
                    name: '录入',
                    url: '/production_management/material_apply_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/material_apply_query'
                },
                {
                    name: '修改',
                    url: '/production_management/material_apply_modify'
                }
            ]
        },
        {
            name: '退料单',
            url: '/production_management/material_return',
            children: [
                {
                    name: '录入',
                    url: '/production_management/material_return_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/material_return_query'
                },
                {
                    name: '修改',
                    url: '/production_management/material_return_modify'
                }
            ]
        },
        {
            name: '产成品入库单',
            url: '/production_management/warehouse_entry',
            children: [
                {
                    name: '录入',
                    url: '/production_management/warehouse_entry_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/warehouse_entry_query'
                },
                {
                    name: '修改',
                    url: '/production_management/warehouse_entry_modify'
                }
            ]
        },
        {
            name: '拆/组装单',
            url: '/production_management/assembly',
            children: [
                {
                    name: '录入',
                    url: '/production_management/assembly_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/assembly_query'
                },
                {
                    name: '修改',
                    url: '/production_management/assembly_modify'
                }
            ]
        },
        {
            name: '零星盘盈单',
            url: '/production_management/sporadic_profit',
            children: [
                {
                    name: '录入',
                    url: '/production_management/sporadic_profit_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/sporadic_profit_query'
                },
                {
                    name: '修改',
                    url: '/production_management/sporadic_profit_modify'
                }
            ]
        },
        {
            name: '零星盘亏单',
            url: '/production_management/sporadic_loss',
            children: [
                {
                    name: '录入',
                    url: '/production_management/sporadic_loss_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/sporadic_loss_query'
                },
                {
                    name: '修改',
                    url: '/production_management/sporadic_loss_modify'
                }
            ]
        },
        {
            name: '调拨单',
            url: '/production_management/distribution',
            children: [
                {
                    name: '录入',
                    url: '/production_management/distribution_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/distribution_query'
                },
                {
                    name: '修改',
                    url: '/production_management/distribution_modify'
                }
            ]
        },
        {
            name: '报废单',
            url: '/production_management/scrap',
            children: [
                {
                    name: '录入',
                    url: '/production_management/scrap_entry'
                },
                {
                    name: '查询',
                    url: '/production_management/scrap_query'
                },
                {
                    name: '修改',
                    url: '/production_management/scrap_modify'
                }
            ]
        }
    ],
    accounts_management_nav: [
        //todo
        {
            name: '进项税金',
            url: '/accounts_management',
        },
        {
            name: '收承兑汇票',
            url: '/accounts_managemen',
        },
        {
            name: '付承兑汇票',
            url: '/accounts_manageme',
        },
        {
            name: '承兑汇票解汇',
            url: '/accounts_managem',
        },
        {
            name: '应收票据',
            url: '/accounts_manage',
        },
        {
            name: '承兑汇票承付',
            url: '/accounts_manag',
        },
        {
            name: '应付票据',
            url: '/accounts_mana',
        },
        {
            name: '银行存取款',
            url: '/accounts_man',
        },
        {
            name: '其他收入',
            url: '/accounts_ma',
        },
        {
            name: '费用开支',
            url: '/accounts_m',
        },
        {
            name: '支付工资',
            url: '/accounts_',
        },
        {
            name: '现金/银行日记账',
            url: '/accounts',
        },
        {
            name: '现金/银行期初',
            url: '/account',
        },
        {
            name: '费用收入工资统计',
            url: '/accoun',
        },
    ],
    assets_management_nav: [
        //todo
        {
            name: '固定资产入库查询',
            url: '/assets_management',
        },
        {
            name: '固定资产入库修改',
            url: '/assets_managemen',
        },
        {
            name: '固定资产领用',
            url: '/assets_manageme',
        },
        {
            name: '固定资产了领用查询',
            url: '/assets_managem',
        },
        {
            name: '固定资产领用修改',
            url: '/assets_manage',
        },
        {
            name: '固定资产折旧表',
            url: '/assets_manag',
        },
    ],
    query_stats_nav: [
        {
            name: '入库单汇总统计',
            url: '/query_stats/in_summary_stats'
        },
        {
            name: '入库结账单',
            url: '/query_stats/in_check',
            children: [
                {
                    name: '汇总统计',
                    url: '/query_stats/in_check_summary_stats'
                },
                {
                    name: '审核',
                    url: '/query_stats/in_check_audit'
                }
            ]
        },
        {
            name: '应付款',
            url: '/query_stats/pay',
            children: [
                {
                    name: '汇总查询',
                    url: '/query_stats/pay_summary_query'
                },
                {
                    name: '明细',
                    url: '/query_stats/pay_detail'
                },
                {
                    name: '总账',
                    url: '/query_stats/pay_ledger'
                }
            ]
        },
        {
            name: '出库单汇总统计',
            url: '/query_stats/out_summary_stats'
        },
        {
            name: '出库结账单',
            url: '/query_stats/out_check',
            children: [
                {
                    name: '汇总统计',
                    url: '/query_stats/out_check_summary_stats'
                },
                {
                    name: '审核',
                    url: '/query_stats/out_check_audit'
                }
            ]
        },
        {
            name: '应收款',
            url: '/query_stats/receipt',
            children: [
                {
                    name: '汇总查询',
                    url: '/query_stats/rec_summary_query'
                },
                {
                    name: '明细',
                    url: '/query_stats/rec_detail'
                },
                {
                    name: '总账',
                    url: '/query_stats/rec_ledger'
                }
            ]
        },
        {
            name: '销售价差统计',
            url: '/query_stats/price_diff_stats'
        },
        {
            name: '年度出入库统计',
            url: '/query_stats/yearly_stats'
        }
    ],
    resources_nav: [
        {
            name: '供货商资源',
            url: '/resources/supplier_resources',
            color: '#7FFFD4'
        },
    ],
    system_nav: [
        {
            name: '商品分类',
            url: '/system/product_categories',
            color: '#A6CAF0',
            requiredPermission: 'system:productCategories',
        },
        {
            name: '商品型号',
            url: '/system/models',
            color: '#A6CAF0',
            requiredPermission: 'system:models',
        },
        {
            name: '生产厂',
            url: '/system/factory_brands',
            color: '#A6CAF0',
            requiredPermission: 'system:factoryBrands',
        },
        {
            name: '商品型号明细',
            url: '/system/sku',
            color: '#A6CAF0',
            requiredPermission: 'system:sku',
        },
        {
            name: '往来单位区划',
            url: '/system/partner_company_categories',
            color: '#C0DCC0',
            requiredPermission: 'system:partnerCompanyCategories',
        },
        {
            name: '往来单位',
            url: '/system/partner_companies',
            color: '#C0DCC0',
            requiredPermission: 'system:partnerCompanies',
        },
        {
            name: '系统及人员信息',
            url: '/system/staff',
            color: '#FF00FF',
            requiredPermission: 'system:staff',
        },
        {
            name: '相关单位分类',
            url: '/system/relevant_company_categories',
            color: '#FFFF00',
            requiredPermission: 'system:relevantCompanyCategories',
        },
        {
            name: '相关单位',
            url: '/system/relevant_companies',
            color: '#FFFF00',
            requiredPermission: 'system:relevantCompanies',
        },
        {
            name: '计量单位',
            url: '/system/measurement_units',
            color: '#00FF00',
            requiredPermission: 'system:measurementUnits',
        },
        {
            name: '仓库',
            url: '/system/warehouses',
            color: '#00FF00',
            requiredPermission: 'system:warehouses'
        },
        {
            name: '部门',
            url: '/system/departments',
            color: '#00FF00',
            requiredPermission: 'system:departments'
        },
        {
            name: '费用收入类别',
            url: '/system/fees',
            color: '#00FFFF',
            requiredPermission: 'system:fees',
        },
    ],
    maintenance_nav: [
        {
            name: '本公司信息',
            url: '/maintenance',
        },
        {
            name: '国标型号维护',
            url: '/maintenan',
        },
        {
            name: '期初库存录入',
            url: '/maintena',
        },
        {
            name: '应收款期初数据录入',
            url: '/mainten',
        },
        {
            name: '应付款期初数据录入',
            url: '/mainte',
        },
        {
            name: '数据库备份',
            url: '/maint',
        },
        {
            name: '数据库矫正',
            url: '/main',
        },
        {
            name: '用户密码更改',
            url: '/mai',
        },
    ],
    accessibility_nav: [
        {
            name: '国标商品型号查询',
            url: '/accessibility',
        },
        {
            name: '往来单位查询',
            url: '/accessibilit',
        },
        {
            name: '往来业务单位',
            url: '/accessibili',
        },
    ],
    help_nav: [
        {
            name: '年度销售统计',
            url: '/help',
        },
        {
            name: '关于我们',
            url: '/hel',
        },
        {
            name: '初始化信息',
            url: '/he',
        },
    ]
}
