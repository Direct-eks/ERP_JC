package org.jc.backend.utils;

import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.InvoiceStatDO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyUtils {

    public static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    /**
     * parse Date to verify passed param
     * @param dateString String to be parsed, format yyyy-MM-dd
     * @return Date parsed
     * @throws GlobalParamException if there is an error parsing dateString
     */
    public static Date parseAndCheckDateString(String dateString) throws GlobalParamException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            String errorInfo = "Invalid date String: " + dateString;
            logger.info(errorInfo);
            throw new GlobalParamException(errorInfo);
        }

        return date;
    }

    /**
     * form a new serial out of provided params
     * @param base beginning of the serial, like "采订"
     * @param currentCount the number of entries of passed 'date', of such kind
     * @param date the date
     * @return new formed serial String
     */
    public static String formNewSerial(String base, int currentCount, String date) {

//        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateString = date;
        dateString = dateString.substring(2).replaceAll("-", "");
        String newSerial = String.format("%s%s-%03d", base, dateString, currentCount + 1);
        logger.info("New serial: " + newSerial);

        return newSerial;
    }

    /**
     * group entries by companyID, and calculate the sum of total amounts, return VO
     * @param rawStats ungrouped and summed entry data
     * @return grouped and summed entry data
     */
    public static List<InvoiceStatVO> summingUpTotalAmountForEachCompany(List<InvoiceStatDO> rawStats) {

        List<InvoiceStatVO> statVOs = new ArrayList<>();
        rawStats.stream()
                .collect(Collectors.groupingBy(InvoiceStatDO::getCompanyID))
                .forEach((k, v) -> {
                    InvoiceStatVO tempVO = new InvoiceStatVO();
                    tempVO.setCompanyID(k);
                    tempVO.setCompanyAbbreviatedName(v.get(0).getCompanyAbbreviatedName());
                    tempVO.setCompanyFullName(v.get(0).getCompanyFullName());
                    BigDecimal totalAmount = new BigDecimal("0");
                    for (var e : v) {
                        totalAmount = totalAmount.add(new BigDecimal(e.getUnitPriceWithTax())
                                .multiply(new BigDecimal(e.getQuantity())));
                    }
                    tempVO.setTotalAmount(totalAmount.toPlainString());
                    statVOs.add(tempVO);
                });
        // todo sort
        return statVOs;
    }

}
