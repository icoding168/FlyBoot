package xyz.icoding168.flyboot.common.helper;


import java.text.SimpleDateFormat;
import java.util.Date;


public class DateHelper {



    /**
     * 将日期字符串转换为日期类型
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date formatStringToDate(String dateString, String pattern) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateString);
    }


    /**
     * 将日期类型转换为字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String fromDateToString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    }
