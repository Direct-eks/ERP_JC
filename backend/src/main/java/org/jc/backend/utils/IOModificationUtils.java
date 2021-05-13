package org.jc.backend.utils;

import org.jc.backend.entity.DO.*;
import org.jc.backend.entity.InboundProductO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class IOModificationUtils {

    /**
     * compare shipping info fields and form modification record,
     * since inbound and outbound entry share the same shipping info fields,
     * T will be always treated as InboundEntryDO.
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedInfo the modified shippingInfo object to be compared with @originEntry
     * @param originInfo the original shippingInfo object to be compared with @originEntry
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static <T> boolean shippingInfoCompareAndFormModificationRecord(
            StringBuilder record, T modifiedInfo, T originInfo
    ) {
        InboundEntryDO modifiedShippingInfo = new InboundEntryDO();
        InboundEntryDO originShippingInfo = new InboundEntryDO();
        BeanUtils.copyProperties(modifiedInfo, modifiedShippingInfo);
        BeanUtils.copyProperties(originInfo, originShippingInfo);

        boolean bool = false;

        if (new BigDecimal(modifiedShippingInfo.getShippingCost())
                .compareTo(new BigDecimal(originShippingInfo.getShippingCost())) != 0) {
            bool = true;
            record.append(String.format("运费: %s -> %s; ",
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

    /**
     * compare entry fields and form modification record,
     * the different fields in objects will be compared first, then objects will
     * be transformed into InboundEntryDO, and compare the fields that they all share.
     * Note that totalCost only exist in PurchaseEntry and InboundEntry, but for
     * SalesOrder and OutboundEntry, those two will always equal, thus does not
     * matter even if it is not put in a if statement separately
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedEntry the modified entry object to be compared with @originEntry
     * @param originEntry the origin entry queried from database for compare
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static <T> boolean entryCompareAndFormModificationRecord(
            StringBuilder record, T modifiedEntry, T originEntry
    ) {
        boolean bool = false;

        if (modifiedEntry instanceof OutboundEntryDO) {
            if (!((OutboundEntryDO)modifiedEntry).getDeliveryMethod()
                    .equals(((OutboundEntryDO)originEntry).getDeliveryMethod())) {
                bool = true;
                record.append(String.format("提货方式: %s -> %s; ",
                        ((OutboundEntryDO)originEntry).getDeliveryMethod(),
                        ((OutboundEntryDO)modifiedEntry).getDeliveryMethod()));
            }
        }

        try {
            // purchaseOrder or salesOrder
            modifiedEntry.getClass().getDeclaredField("executionStatus");

            // compare executionStatus and warehouseID fields
            PurchaseOrderEntryDO modifiedDO = new PurchaseOrderEntryDO();
            PurchaseOrderEntryDO originDO = new PurchaseOrderEntryDO();
            BeanUtils.copyProperties(modifiedEntry, modifiedDO);
            BeanUtils.copyProperties(originEntry, originDO);

            if (!modifiedDO.getExecutionStatus().equals(originDO.getExecutionStatus())) {
                bool = true;
                record.append(String.format("状态: %s -> %s; ",
                        originDO.getExecutionStatus(), modifiedDO.getExecutionStatus()));
            }
            if (modifiedDO.getWarehouseID() != originDO.getWarehouseID()) {
                bool = true;
                record.append(String.format("仓库: %s -> %s; ",
                        originDO.getWarehouseName(), modifiedDO.getWarehouseName()));
            }

        } catch (NoSuchFieldException e) {
            // do nothing
        }

        try {
            // outboundEntry or salesOrder
            modifiedEntry.getClass().getDeclaredField("totalAmount");

            OutboundEntryDO modifyDO = new OutboundEntryDO();
            OutboundEntryDO originDO = new OutboundEntryDO();
            BeanUtils.copyProperties(modifiedEntry, modifyDO);
            BeanUtils.copyProperties(originEntry, originDO);

            if (new BigDecimal(modifyDO.getTotalAmount()).compareTo(new BigDecimal(originDO.getTotalAmount())) != 0) {
                bool = true;
                record.append(String.format("总金额: %s -> %s; ", originDO.getTotalAmount(), modifyDO.getTotalAmount()));
            }

        } catch (NoSuchFieldException e) {
            // do nothing
        }

        try {
            // inboundEntry or purchaseOrder
            modifiedEntry.getClass().getDeclaredField("totalCost");

            InboundEntryDO modifyDO = new InboundEntryDO();
            InboundEntryDO originDO = new InboundEntryDO();
            BeanUtils.copyProperties(modifiedEntry, modifyDO);
            BeanUtils.copyProperties(originEntry, originDO);

            if (new BigDecimal(originDO.getTotalCost())
                    .compareTo(new BigDecimal(modifyDO.getTotalCost())) != 0) {
                bool = true;
                record.append(String.format("总金额: %s -> %s; ", originDO.getTotalCost(), modifyDO.getTotalCost()));
            }

        } catch (NoSuchFieldException e) {
            // do nothing
        }

        InboundEntryDO modifyDO = new InboundEntryDO();
        InboundEntryDO originDO = new InboundEntryDO();
        BeanUtils.copyProperties(modifiedEntry, modifyDO);
        BeanUtils.copyProperties(originEntry, originDO);

        if (!(modifiedEntry instanceof QuotaEntryDO)) {
            if (originDO.getDepartmentID() != modifyDO.getDepartmentID()) {
                bool = true;
                record.append(String.format("部门: %s -> %s; ", originDO.getDepartmentName(), modifyDO.getDepartmentName()));
            }
        }
        if (!originDO.getInvoiceType().equals(modifyDO.getInvoiceType())) {
            bool = true;
            record.append(String.format("单据类型: %s -> %s; ", originDO.getInvoiceType(), modifyDO.getInvoiceType()));
        }
        if (!originDO.getRemark().equals(modifyDO.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originDO.getRemark(), modifyDO.getRemark()));
        }

        return bool;
    }

    /**
     * compare product fields and form modification record,
     * all purchaseProduct, inboundProduct, salesOrderProducts, outboundProduct
     * and quotaProduct are compared by transforming into InboundProductO
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedO the modified product object to be compared with @originO
     * @param originO the origin product queried from database for compare
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static <T> boolean productsCompareAndFormModificationRecord(
            StringBuilder record,
            T modifiedO,
            T originO
    ) {
        InboundProductO modifiedProduct = new InboundProductO();
        InboundProductO originProduct = new InboundProductO();
        BeanUtils.copyProperties(modifiedO, modifiedProduct);
        BeanUtils.copyProperties(originO, originProduct);

        String modelCode = modifiedProduct.getCode();
        //compare product
        boolean bool = false;
        if (!modifiedProduct.getQuantity().equals(originProduct.getQuantity())) {
            bool = true;
            record.append(String.format("型号(%s) 数量: %d -> %d; ", modelCode,
                    originProduct.getQuantity(), modifiedProduct.getQuantity()));
        }
        if (!modifiedProduct.getRemark().equals(originProduct.getRemark())) {
            bool = true;
            record.append(String.format("型号(%s) 备注: %s -> %s; ", modelCode,
                    originProduct.getRemark(), modifiedProduct.getRemark()));
        }
        if (!modifiedProduct.getTaxRate().equals(originProduct.getTaxRate())) {
            bool = true;
            record.append(String.format("型号(%s) 税率: %s -> %s; ", modelCode,
                    originProduct.getTaxRate(), modifiedProduct.getTaxRate()));
        }
        if (new BigDecimal(modifiedProduct.getUnitPriceWithoutTax())
                .compareTo(new BigDecimal(originProduct.getUnitPriceWithoutTax())) != 0) {
            bool = true;
            record.append(String.format("型号(%s) 单价: %s -> %s; ", modelCode,
                    originProduct.getUnitPriceWithoutTax(), modifiedProduct.getUnitPriceWithoutTax()));
        }
        if (new BigDecimal(modifiedProduct.getUnitPriceWithTax())
                .compareTo(new BigDecimal(originProduct.getUnitPriceWithTax())) != 0) {
            bool = true;
            record.append(String.format("型号(%s) 税价: %s -> %s; ", modelCode,
                    originProduct.getUnitPriceWithTax(), modifiedProduct.getUnitPriceWithTax()));
        }

        return bool;
    }

}
