package org.jc.backend.utils;

import org.jc.backend.config.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

        return dateString;
    }
}
