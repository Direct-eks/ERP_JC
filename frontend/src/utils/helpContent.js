export default {
    items: [
        /* ---------- 入库管理 ---------- */
        {
            name: '录入',
            url: '/inbound_management/entry_in',
            helpContent: [
                '1. 在“电话”和“供货单位简称”输入框中输入任意内容后' +
                '将会激活“单位检索”按钮，此时检索内容为输入内容，直接' +
                '按下“回车”键也可直接搜索',
                '2. 当所选单位是资源单位时，只有拥有改价权限的操作者才能' +
                '修改导入的型号价格，否则价格默认为资源录入价格。当所选' +
                '单位时资源单位，但是所选型号没有对应的录入价格时，无权限者' +
                '导入价格默认为 0。所选单位不是资源单位时，所有操作者都可以改价',
                '3. 当更改税率时，若已有入库单商品，则以无税价为基准重新计算价格'
            ]
        },
        /* ---------- 出库管理 ---------- */
        /* ---------- 查询统计 ---------- */
        {
            name: '',
            url: '/query_stats/check_audit',
            helpContent: [
                '1. 选择月份后，单击“查询入库”或“查询出库”按钮查询相应单据',
                '2. 当表格中不为空时（有查询结果时），不能更改月份，如需更改，' +
                '先点击“清空”按钮清空表格内容，再按照步骤1重新查询'
            ]
        },
        /* ---------- 系统标准 ---------- */
        {
            name: '供货商资源',
            url: '/resources/supplier_resources',
            helpContent: [
            ]
        },
        {
            name: '商品分类',
            url: '/system/product_categories',
            helpContent: [
            ]
        },
        {
            name: '商品型号',
            url: '/system/models',
            helpContent: [
                '1. 当搜索时，修改功能被锁定，只有当选择分类之后修改功能才重新可用',
                '2. 所有修改仅对当前分类下所有型号有效，当重新选择型号后，所有未保存的修改' +
                '将会被丢弃',
                '3. 最右侧的厂牌表格，用作在新增型号时，同时为此型号（可以有多个新增型号）新' +
                '增的厂牌（如有多个新增型号，则所有新增型号都会新增所选厂牌）'
            ]
        },
        {
            name: '生产厂',
            url: '/system/factory_brands',
            helpContent: [
            ]
        },
        {
            name: '商品型号明细',
            url: '/system/sku',
            helpContent: [
                '界面分为左中右三个表格（不算最左侧的分类选择）',
                '1. 在选择完型号后，中间表格会出现现有的此型号的所有厂牌',
                '2. 在右侧表格中“所有厂牌”选择要新增的厂牌',
                '3. 点击新增，将右侧表格中选中的厂牌全部导入当前型号（去重）',
                '4. 如要修改现有此型号厂牌，在中间表格单选要修改的厂牌‘' +
                '并在右侧表格中选择要修改为的厂牌（注意不要多选），并点击修改',
                '5. 所有的修改，仅对当前型号有效，请立刻保存，不然对于当前型' +
                '号的所有修改会在重新选择另一型号后丢失',
                '6. 多选下，右侧表格仅用作显示新增型号（选择步骤同上），选中的' +
                '厂牌会被增加到所有选中型号上（去重）',
                '7. 多选下，修改仅对当前分类有效，重选分类后所有修改会丢失'
            ]
        },
        {
            name: '往来单位区划',
            url: '/system/partner_company_categories',
            helpContent: [
            ]
        },
        {
            name: '往来单位',
            url: '/system/partner_companies',
            helpContent: [
                '1. 窗口上部的“电话”和“简称”只能在当前分类的所有单位内搜索'
            ]
        },
        {
            name: '系统及人员信息',
            url: '/system/staff',
            helpContent: [
            ]
        },
        {
            name: '相关单位分类',
            url: '/system/relevant_company_categories',
            helpContent: [
            ]
        },
        {
            name: '相关单位',
            url: '/system/relevant_companies',
            helpContent: [
            ]
        },
        {
            name: '仓库/计量单位',
            url: '/system/measurement_units',
            helpContent: [
            ]
        },
        {
            name: '费用收入类别',
            url: '/system/fees',
            helpContent: [
            ]
        },
    ]
}
