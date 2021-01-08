package org.jc.backend.utils;

import org.jc.backend.entity.*;
import org.jc.backend.entity.DO.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class IOModificationUtils {

    /**
     * compare shipping info fields and form modification record
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedShippingInfo the modified shippingInfo object to be compared with @originEntry
     * @param originShippingInfo the original shippingInfo object to be compared with @originEntry
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static boolean shippingInfoCompareAndFormModificationRecord(
            StringBuilder record,
            InboundEntryDO modifiedShippingInfo,
            InboundEntryDO originShippingInfo
    ) {
        boolean bool = false;
        if (modifiedShippingInfo.getShippingCost() != originShippingInfo.getShippingCost()) {
            bool = true;
            record.append(String.format("运费: %f -> %f; ",
                    originShippingInfo.getShippingCost(), modifiedShippingInfo.getShippingCost()));
        }
        if (!modifiedShippingInfo.getShippingCostType().equals(originShippingInfo.getShippingCostType())) {
            bool = true;
            record.append(String.format("类型: %s -> %s; ",
                    originShippingInfo.getShippingCostType(), modifiedShippingInfo.getShippingCostType()));
        }
        if (modifiedShippingInfo.getShippingQuantity() != originShippingInfo.getShippingQuantity()) {
            bool = true;
            record.append(String.format("数量: %d -> %d; ",
                    originShippingInfo.getShippingQuantity(), modifiedShippingInfo.getShippingQuantity()));
        }
        if (!modifiedShippingInfo.getShippingNumber().equals(originShippingInfo.getShippingNumber())) {
            bool = true;
            record.append(String.format("运单号: %s -> %s; ",
                    originShippingInfo.getShippingNumber(), modifiedShippingInfo.getShippingNumber()));
        }
        if (modifiedShippingInfo.getShippingMethodID() != originShippingInfo.getShippingMethodID()) {
            bool = true;
            record.append(String.format("承运: %s -> %s;",
                    originShippingInfo.getRelevantCompanyName(), modifiedShippingInfo.getRelevantCompanyName()));
        }
        if (!modifiedShippingInfo.getRemark().equals(originShippingInfo.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s",
                    originShippingInfo.getRemark(), modifiedShippingInfo.getRemark()));
        }

        return bool;
    }

    public static boolean shippingInfoCompareAndFormModificationRecord(
            StringBuilder record,
            OutboundEntryDO modifiedShippingInfo,
            OutboundEntryDO originShippingInfo
    ) {
        InboundEntryDO modifiedO = new InboundEntryDO();
        InboundEntryDO originO = new InboundEntryDO();
        BeanUtils.copyProperties(modifiedShippingInfo, modifiedO);
        BeanUtils.copyProperties(originShippingInfo, originO);

        return shippingInfoCompareAndFormModificationRecord(record, modifiedO, originO);
    }

    /**
     * compare entry fields and form modification record
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedEntry the modified entry object to be compared with @originEntry
     * @param originEntry the origin entry queried from database for compare
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static boolean entryCompareAndFormModificationRecord(
            StringBuilder record,
            InboundEntryDO modifiedEntry,
            InboundEntryDO originEntry
    ) {
        boolean bool = false;
        if (originEntry.getTotalCost() != modifiedEntry.getTotalCost()) {
            bool = true;
            record.append(String.format("总金额: %f -> %f; ", originEntry.getTotalCost(), modifiedEntry.getTotalCost()));
        }
        if (!originEntry.getInvoiceType().equals(modifiedEntry.getInvoiceType())) {
            bool = true;
            record.append(String.format("单据类型: %s -> %s; ", originEntry.getInvoiceType(), modifiedEntry.getInvoiceType()));
        }
        if (originEntry.getDepartmentID() != modifiedEntry.getDepartmentID()) {
            bool = true;
            record.append(String.format("部门: %s -> %s; ", originEntry.getDepartmentName(), modifiedEntry.getDepartmentName()));
        }
        if (!originEntry.getRemark().equals(modifiedEntry.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }

    /**
     * compare entry fields and form modification record,
     * for PurchaseOrderEntryModifyDO, there are one more field than
     * InboundEntryModifyDO, so that field is handled here, and then objects
     * will be transformed to InboundEntryModifyDO and call overloaded method
     * entryCompareAndFormModificationRecord()
     */
    public static boolean entryCompareAndFormModificationRecord(
            StringBuilder record,
            OutboundEntryDO modifiedEntry,
            OutboundEntryDO originEntry
    ) {
        boolean bool = false;
        if (modifiedEntry.getTotalAmount() != originEntry.getTotalAmount()) {
            bool = true;
            record.append(String.format("总金额: %f -> %f; ", originEntry.getTotalAmount(),
                    modifiedEntry.getTotalAmount()));
        }
        if (!modifiedEntry.getDeliveryMethod().equals(originEntry.getDeliveryMethod())) {
            bool = true;
            record.append(String.format("提货方式: %s -> %s; ", originEntry.getDeliveryMethod(),
                    modifiedEntry.getDeliveryMethod()));
        }

        InboundEntryDO modifyDO = new InboundEntryDO();
        InboundEntryDO originDO = new InboundEntryDO();
        BeanUtils.copyProperties(modifiedEntry, modifyDO);
        BeanUtils.copyProperties(originEntry, originDO);

        boolean bool2 = entryCompareAndFormModificationRecord(record, modifyDO, originDO);

        return bool || bool2;
    }

    /**
     * compare entry fields and form modification record,
     * for PurchaseOrderEntryModifyDO, there are two more fields than
     * InboundEntryModifyDO, so those two fields are handled here, and then objects
     * will be transformed to InboundEntryModifyDO and call overloaded method
     * entryCompareAndFormModificationRecord()
     */
    public static boolean entryCompareAndFormModificationRecord(
            StringBuilder record,
            PurchaseOrderEntryDO modifiedEntry,
            PurchaseOrderEntryDO originEntry
    ) {
        boolean bool = false;
        if (!modifiedEntry.getExecutionStatus().equals(originEntry.getExecutionStatus())) {
            bool = true;
            record.append(String.format("状态: %s -> %s; ", originEntry.getExecutionStatus(), modifiedEntry.getExecutionStatus()));
        }
        if (modifiedEntry.getWarehouseID() != originEntry.getWarehouseID()) {
            bool = true;
            record.append(String.format("仓库: %s -> " + "%s; ", originEntry.getWarehouseName(), modifiedEntry.getWarehouseName()));
        }

        InboundEntryDO modifyDO = new InboundEntryDO();
        InboundEntryDO originDO = new InboundEntryDO();
        BeanUtils.copyProperties(modifiedEntry, modifyDO);
        BeanUtils.copyProperties(originEntry, originDO);

        boolean bool2 = entryCompareAndFormModificationRecord(record, modifyDO, originDO);

        return bool || bool2;
    }

    public static boolean entryCompareAndFormModificationRecord(
            StringBuilder record,
            SalesOrderEntryDO modifiedEntry,
            SalesOrderEntryDO originEntry
    ) {
        boolean bool = false;
        if (modifiedEntry.getTotalAmount() != originEntry.getTotalAmount()) {
            bool = true;
            record.append(String.format("总金额: %f -> %f", originEntry.getTotalAmount(), modifiedEntry.getTotalAmount()));
        }

        PurchaseOrderEntryDO modifyDO = new PurchaseOrderEntryDO();
        PurchaseOrderEntryDO originDO = new PurchaseOrderEntryDO();
        BeanUtils.copyProperties(modifiedEntry, modifyDO);
        BeanUtils.copyProperties(originEntry, originDO);

        boolean bool2 = entryCompareAndFormModificationRecord(record, modifyDO, originDO);

        return bool || bool2;
    }

    public static boolean entryCompareAndFormModificationRecord(
            StringBuilder record,
            QuotaEntryDO modifiedEntry,
            QuotaEntryDO originEntry
    ) {
        boolean bool = false;
        if (modifiedEntry.getTotalAmount() != originEntry.getTotalAmount()) {
            bool = true;
            record.append(String.format("总金额: %f -> %f", originEntry.getTotalAmount(), modifiedEntry.getTotalAmount()));
        }
        if (!modifiedEntry.getInvoiceType().equals(originEntry.getInvoiceType())) {
            bool = true;
            record.append(String.format("单据类型: %s -> %s; ", originEntry.getInvoiceType(), modifiedEntry.getInvoiceType()));
        }
        if (!modifiedEntry.getRemark().equals(originEntry.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }

    /**
     * compare product fields and form modification record
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedProduct the modified product object to be compared with @originProduct
     * @param originProduct the origin product queried from database for compare
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static boolean productsCompareAndFormModificationRecord(
            StringBuilder record,
            InboundProductO modifiedProduct,
            InboundProductO originProduct
    ) {
        String modelCode = StringUtils.hasLength(modifiedProduct.getNewCode()) ?
                modifiedProduct.getNewCode() : modifiedProduct.getOldCode();
        //compare product
        boolean bool = false;
        if (modifiedProduct.getQuantity() != originProduct.getQuantity()) {
            bool = true;
            record.append(String.format("型号%s 数量: %d -> %d; ", modelCode,
                    originProduct.getQuantity(), originProduct.getQuantity()));
        }
        if (!modifiedProduct.getRemark().equals(originProduct.getRemark())) {
            bool = true;
            record.append(String.format("型号%s 备注: %s -> %s; ", modelCode,
                    originProduct.getRemark(), modifiedProduct.getRemark()));
        }
        if (modifiedProduct.getTaxRate() != originProduct.getTaxRate()) {
            bool = true;
            record.append(String.format("型号%s 税率: %f -> %f; ", modelCode,
                    originProduct.getTaxRate(), modifiedProduct.getTaxRate()));
        }
        if (modifiedProduct.getUnitPriceWithoutTax() != originProduct.getUnitPriceWithoutTax()) {
            bool = true;
            record.append(String.format("型号%s 单价: %f -> %f; ", modelCode,
                    originProduct.getUnitPriceWithoutTax(), modifiedProduct.getUnitPriceWithoutTax()));
        }
        if (modifiedProduct.getUnitPriceWithTax() != originProduct.getUnitPriceWithTax()) {
            bool = true;
            record.append(String.format("型号%s 税价: %f -> %f; ", modelCode,
                    originProduct.getUnitPriceWithTax(), modifiedProduct.getUnitPriceWithTax()));
        }

        return bool;
    }

    public static boolean productsCompareAndFormModificationRecord(
            StringBuilder record,
            OutboundProductO modifiedProduct,
            OutboundProductO originProduct
    ) {
        InboundProductO product1 = new InboundProductO();
        InboundProductO product2 = new InboundProductO();
        BeanUtils.copyProperties(modifiedProduct, product1);
        BeanUtils.copyProperties(originProduct, product2);
        return productsCompareAndFormModificationRecord(record, product1, product2);
    }

    public static boolean productsCompareAndFormModificationRecord(
            StringBuilder record,
            PurchaseOrderProductO modifiedProduct,
            PurchaseOrderProductO originProduct
    ) {
        InboundProductO product1 = new InboundProductO();
        InboundProductO product2 = new InboundProductO();
        BeanUtils.copyProperties(modifiedProduct, product1);
        BeanUtils.copyProperties(originProduct, product2);
        return productsCompareAndFormModificationRecord(record, product1, product2);
    }

    public static boolean productsCompareAndFormModificationRecord(
            StringBuilder record,
            SalesOrderProductO modifiedProduct,
            SalesOrderProductO originProduct
    ) {
        InboundProductO product1 = new InboundProductO();
        InboundProductO product2 = new InboundProductO();
        BeanUtils.copyProperties(modifiedProduct, product1);
        BeanUtils.copyProperties(originProduct, product2);
        return productsCompareAndFormModificationRecord(record, product1, product2);
    }

    public static boolean productsCompareAndFormModificationRecord(
            StringBuilder record,
            QuotaProductO modifiedProduct,
            QuotaProductO originProduct
    ) {
        InboundProductO product1 = new InboundProductO();
        InboundProductO product2 = new InboundProductO();
        BeanUtils.copyProperties(modifiedProduct, product1);
        BeanUtils.copyProperties(originProduct, product2);
        return productsCompareAndFormModificationRecord(record, product1, product2);
    }

}
