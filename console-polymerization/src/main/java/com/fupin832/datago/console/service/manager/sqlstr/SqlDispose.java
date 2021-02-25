package com.fupin832.datago.console.service.manager.sqlstr;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * sql 处理
 *
 * @author zy
 * @date 2021/02/08
 */
public class SqlDispose {
    public static String disposeSQL (String sql, Date lastStartDate, Date now) {
        String result = sql;
        if (result.contains("${startTime}")) {
            result = result.replace("${startTime}",formateDate(lastStartDate));
        }
        if (result.contains("${endTime}")) {
            result = result.replace("${endTime}",formateDate(now));
        }
        return result;
    }
    public static String formateDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
