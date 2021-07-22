package org.jc.backend.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.InvoiceStatDO;
import org.jc.backend.entity.StatO.InvoiceStatVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Indexed
@Component
public class MyUtils {

    public static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateFormatNoDay = new SimpleDateFormat("yyyy-MM");

    /**
     * parse Date to verify passed param
     * @param dateString String to be parsed, format yyyy-MM-dd
     * @return Date parsed
     * @throws GlobalParamException if there is an error parsing dateString
     */
    public static Date parseAndCheckDateString(String dateString) throws GlobalParamException {
        try {
            return dateFormat.parse(dateString);

        } catch (ParseException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            String errorInfo = "Invalid date String: " + dateString;
            logger.info(errorInfo);
            throw new GlobalParamException(errorInfo);
        }
    }

    public static Pair<String, String> getFirstAndLastDayOfMonth(String month) throws GlobalParamException {
        try {
            Calendar c = Calendar.getInstance();

            c.setTime(dateFormatNoDay.parse(month));
            c.add(Calendar.MONTH, 0);

            // set to day 1 of the month
            c.set(Calendar.DAY_OF_MONTH, 1);
            String firstDay = dateFormat.format(c.getTime());

            // set to last day of the month
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastDay = dateFormat.format(c.getTime());

            return ImmutablePair.of(firstDay, lastDay);

        } catch (ParseException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            String errorInfo = "Invalid month String: " + month;
            logger.info(errorInfo);
            throw new GlobalParamException(errorInfo);
        }
    }

    /**
     * form a new serial out of provided params
     * @param base beginning of the serial, like "采订"
     * @param currentCount the number of entries of passed 'date', of such kind
     * @param date the date
     * @return new formed serial String
     */
    public static String formNewSerial(String base, int currentCount, String date) {
        String dateString = date;
        dateString = dateString.substring(2).replaceAll("-", "");
        String newSerial = String.format("%s%s-%03d", base, dateString, currentCount + 1);
        logger.info("New serial: " + newSerial);

        return newSerial;
    }

    public static boolean validateSerial(String entryID) {
        Pattern pattern = Pattern.compile("^(购入|销出|入结|出退|出结|入退)\\d{6}-\\d{3}$");
        Matcher matcher = pattern.matcher(entryID);
        return matcher.matches();
    }

    public static String todayDateString() {
        return dateFormat.format(new Date());
    }

    /**
     * // 销出210809-001 -> 210809 -> 2021-08-09
     * @param base string such as 销出20210809-001
     * @return date string in format yyyy-MM-dd
     */
    public static String restoreDateFromString(String base) {
        String unformattedDate = base.substring(2, 8);
        return "20" + unformattedDate.substring(0,2) + "-" + unformattedDate.substring(2, 4) +
                "-" + unformattedDate.substring(4);
    }

    /**
     * BigDecimal format
     */
    public final static RoundingMode myRoundingMode = RoundingMode.HALF_UP;
    public final static int myScale = 8;

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
