package org.jc.backend.utils;

import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.entity.DO.InboundEntryModifyDO;
import org.jc.backend.entity.DO.PurchaseOrderEntryModifyDO;
import org.jc.backend.entity.InboundEntryCompleteO;
import org.jc.backend.entity.InboundProductModifyO;
import org.jc.backend.entity.PurchaseOrderProductModifyO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyUtils {

    public static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    /**
     * parse Date to verify passed param
     * @param dateString String to be parsed, format yyyy-MM-dd
     * @return Date parsed
     * @throws GlobalException if there is an error parsing dateString
     */
    public static Date parseAndCheckDateString(String dateString) throws GlobalException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            String errorInfo = "Invalid date String: " + dateString;
            logger.info(errorInfo);
            throw new GlobalException(errorInfo);
        }

        return date;
    }

    /**
     * form a new serial out of provided params
     * @param base beginning of the serial, like "采订"
     * @param currentCount the number of entries of today, of such kind
     * @return new formed serial String
     */
    public static String formNewSerial(String base, int currentCount) {

        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dateString = dateString.substring(2).replaceAll("-", "");
        String newSerial = String.format("%s%s-%03d", base, dateString, currentCount + 1);
        logger.info("New serial: " + newSerial);

        return newSerial;
    }

    /**
     * compare shipping info fields and form modification record
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedShippingInfo the modified shippingInfo object to be compared with @originEntry
     * @param originShippingInfo the original shippingInfo object to be compared with @originEntry
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static boolean shippingInfoCompareAndFormModificationRecord(
            StringBuilder record,
            InboundEntryCompleteO modifiedShippingInfo,
            InboundEntryCompleteO originShippingInfo
    ) {
        boolean bool = false;
        if (modifiedShippingInfo.getShippingCost() != originShippingInfo.getShippingCost()) {
            bool = true;
            record.append(String.format("运费: %f -> %f; ",
                    originShippingInfo.getShippingCost(), modifiedShippingInfo.getShippingCost()));
        }
        if (!modifiedShippingInfo.getShippingCostType().equals(originShippingInfo.getShippingCostType())) {
            bool = true;
            record.append(String.format("类型: %s -> %s",
                    originShippingInfo.getShippingCostType(), modifiedShippingInfo.getShippingCostType()));
        }
        if (modifiedShippingInfo.getShippingQuantity() != originShippingInfo.getShippingQuantity()) {
            bool = true;
            record.append(String.format("数量: %d -> %d",
                    originShippingInfo.getShippingQuantity(), modifiedShippingInfo.getShippingQuantity()));
        }
        if (!modifiedShippingInfo.getShippingNumber().equals(originShippingInfo.getShippingNumber())) {
            bool = true;
            record.append(String.format("运单号: %s -> %s",
                    originShippingInfo.getShippingNumber(), modifiedShippingInfo.getShippingNumber()));
        }
        if (modifiedShippingInfo.getShippingMethodID() != originShippingInfo.getShippingMethodID()) {
            bool = true;
            record.append(String.format("承运: %s -> %s",
                    originShippingInfo.getRelevantCompanyName(), modifiedShippingInfo.getRelevantCompanyName()));
        }

        return bool;
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
            InboundEntryModifyDO modifiedEntry,
            InboundEntryModifyDO originEntry
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
            record.append(String.format("备注: %s -> %s", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }

    /**
     * compare entry fields and form modification record,
     * this method is for PurchaseOrderEntryModifyDO, which has two more fields than
     * InboundEntryModifyDO, so those two fields are handled here, and then objects
     * will be transformed to InboundEntryModifyDO and call overloaded method
     * entryCompareAndFormModificationRecord()
     * @param record the StringBuilder where the modification record is appended
     * @param modifiedEntry the modified entry object to be compared with @originEntry
     * @param originEntry the origin entry queried from database for compare
     * @return true if any of the fields is changed, false if everything remains unchanged
     */
    public static boolean entryCompareAndFormModificationRecord(
            StringBuilder record,
            PurchaseOrderEntryModifyDO modifiedEntry,
            PurchaseOrderEntryModifyDO originEntry
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

        InboundEntryModifyDO modifyDO = new InboundEntryModifyDO();
        InboundEntryModifyDO originDO = new InboundEntryModifyDO();
        BeanUtils.copyProperties(modifiedEntry, modifyDO);
        BeanUtils.copyProperties(originEntry, originDO);

        boolean bool2 = entryCompareAndFormModificationRecord(record, modifyDO, originDO);

        return bool || bool2;
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
            InboundProductModifyO modifiedProduct,
            InboundProductModifyO originProduct
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
            PurchaseOrderProductModifyO modifiedProduct,
            PurchaseOrderProductModifyO originProduct
    ) {
        InboundProductModifyO product1 = new InboundProductModifyO();
        InboundProductModifyO product2 = new InboundProductModifyO();
        BeanUtils.copyProperties(modifiedProduct, product1);
        BeanUtils.copyProperties(originProduct, product2);
        return productsCompareAndFormModificationRecord(record, product1, product2);
    }
}
