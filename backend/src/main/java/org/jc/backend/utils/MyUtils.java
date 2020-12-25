package org.jc.backend.utils;

import org.jc.backend.config.exception.GlobalException;
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
     *
     * @param record
     * @param modifiedProduct
     * @param originProduct
     * @return
     */
    public static boolean entryProductsCompareAndFormModificationRecord(
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

    public static boolean entryProductsCompareAndFormModificationRecord(
            StringBuilder record,
            PurchaseOrderProductModifyO modifiedProduct,
            PurchaseOrderProductModifyO originProduct
    ) {
        InboundProductModifyO product1 = new InboundProductModifyO();
        InboundProductModifyO product2 = new InboundProductModifyO();
        BeanUtils.copyProperties(modifiedProduct, product1);
        BeanUtils.copyProperties(originProduct, product2);
        return entryProductsCompareAndFormModificationRecord(record, product1, product2);
    }
}
