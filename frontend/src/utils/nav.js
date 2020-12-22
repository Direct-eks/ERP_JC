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
            url: '/resources_entry',
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
            name: '入库单录入',
            url: '/inbound_management/entry_in'
        },
        {
            name: '入库单完善',
            url: '/inbound_management/completion_in'
        },
        {
            name: '入库单修改',
            url: '/inbound_management/modify_in'
        },
        {
            name: '入库单退货',
            url: '/inbound_management/return_in'
        },
        {
            name: '入库单查询',
            url: '/inbound_management/query_in'
        },
        {
            name: '采购订单录入',
            url: '/inbound_management/purchase_order_entry'
        },
        {
            name: '采购订单查询',
            url: '/inbound_management/purchase_order_query'
        }
    ],
    outbound_management_nav: [
        {
            name: '出库单录入',
            url: '/outbound_management/entry_out'
        },
        {
            name: '出库单完善',
            url: '/outbound_management/completion_out'
        },
        {
            name: '出库单修改',
            url: '/outbound_management/modify_out'
        },
        {
            name: '出库单退货',
            url: '/outbound_management/return_out'
        },
        {
            name: '出库单查询',
            url: '/outbound_management/query_out'
        },
        {
            name: '报价单录入',
            url: '/outbound_management/quote_entry'
        },
        {
            name: '报价单查询',
            url: '/outbound_management/quote_query'
        },
        {
            name: '销售订单录入',
            url: '/outbound_management/sales_order_entry'
        },
        {
            name: '销售订单查询',
            url: '/outbound_management/sales_order_query'
        }
    ],
    inbound_invoicing_nav: [
        {
            name: '入库结账单',
            url: '/inbound_invoicing/inbound_check',
            children: [
                {
                    name: '录入',
                    url: '/inbound_invoicing/inbound_check_entry'
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/inbound_check_query'
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/inbound_check_modify'
                }
            ]
        },
        {
            name: '入库结账单开票',
            url: '/inbound_invoicing/inbound_check_invoicing',
            children: [
                {
                    name: '录入',
                    url: '/inbound_invoicing/inbound_check_invoicing_entry'
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/inbound_check_invoicing_query'
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/inbound_check_invoicing_modify'
                }
            ]
        },
        {
            name: '付款单',
            url: '/inbound_invoicing/payment',
            children: [
                {
                    name: '录入',
                    url: '/inbound_invoicing/pay_entry'
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/pay_query'
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/pay_modify'
                }
            ]
        },
        {
            name: '入库未结账',
            url: '/inbound_invoicing/incomplete',
            children: [
                {
                    name: '明细',
                    url: '/inbound_invoicing/incomplete_pay_detail'
                },
                {
                    name: '总账',
                    url: '/inbound_invoicing/incomplete_pay_ledger'
                }
            ]
        },
        {
            name: '入库结账未开票查询',
            url: '/inbound_invoicing/incomplete_invoice_query'
        },
        {
            name: '付运费',
            url: '/inbound_invoicing/shipping_cost',
            children: [
                {
                    name: '结账',
                    url: '/inbound_invoicing/cost_bill'
                },
                {
                    name: '查询',
                    url: '/inbound_invoicing/cost_query'
                },
                {
                    name: '修改',
                    url: '/inbound_invoicing/cost_modify'
                }
            ]
        },
        {
            name: '入库结账后退货',
            url: '/inbound_invoicing/inbound_check_invoicing_return'
        }
    ],
    outbound_invoicing_nav: [
        {
            name: '出库结账单',
            url: '/outbound_invoicing/outbound_check',
            children: [
                {
                    name: '录入',
                    url: '/outbound_invoicing/outbound_check_entry'
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/outbound_check_query'
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/outbound_check_modify'
                }
            ]
        },
        {
            name: '出库结账单开票',
            url: '/outbound_invoicing/outbound_check_invoicing',
            children: [
                {
                    name: '录入',
                    url: '/outbound_invoicing/outbound_check_invoicing_entry'
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/outbound_check_invoicing_query'
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/outbound_check_invoicing_modify'
                }
            ]
        },
        {
            name: '收款单',
            url: '/outbound_invoicing/receipt',
            children: [
                {
                    name: '录入',
                    url: '/outbound_invoicing/receipt_entry'
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/receipt_query'
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/receipt_modify'
                }
            ]
        },
        {
            name: '出库未结账',
            url: '/outbound_invoicing/incomplete',
            children: [
                {
                    name: '明细',
                    url: '/outbound_invoicing/incomplete_receipt_detail'
                },
                {
                    name: '总账',
                    url: '/outbound_invoicing/incomplete_receipt_ledger'
                }
            ]
        },
        {
            name: '出库结账未开票查询',
            url: '/outbound_invoicing/incomplete_invoice_query'
        },
        {
            name: '付运费',
            url: '/outbound_invoicing/shipping_cost',
            children: [
                {
                    name: '结账',
                    url: '/outbound_invoicing/cost_bill'
                },
                {
                    name: '查询',
                    url: '/outbound_invoicing/cost_query'
                },
                {
                    name: '修改',
                    url: '/outbound_invoicing/cost_modify'
                }
            ]
        },
        {
            name: '出库结账后退货',
            url: '/outbound_invoicing/outbound_check_invoicing_return'
        }
    ],
    stock_management_nav: [
        {
            name: '商品明细',
            url: '/stock_management/product_detail'
        },
        {
            name: '库存报表',
            url: '/stock_management/stock_report'
        },
        {
            name: '商品定价',
            url: '/stock_management/product_pricing'
        },
        {
            name: '库存报警',
            url: '/stock_management/stock_alert'
        },
        {
            name: '明细统计',
            url: '/stock_management/detail_stats'
        },
        {
            name: '库存盘点',
            url: '/stock_management/inventory'
        },
        {
            name: '预销售资源',
            url: '/stock_management/presales_query'
        },
        {
            name: '库存资源',
            url: '/stock_management/stock_resources'
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
    resources_entry_nav: [
        {
            name: '资源录入修改',
            url: '/resources_entry',
        },
    ],
    maintenance_nav: [
        {
            name: '本公司信息',
            url: '/maintenance',
        },
        {
            name: '系统标准表',
            url: '/maintenanc',
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
