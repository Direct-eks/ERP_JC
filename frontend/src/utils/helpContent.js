export default {
    items: [
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
