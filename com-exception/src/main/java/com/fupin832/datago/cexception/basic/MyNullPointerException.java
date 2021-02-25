package com.fupin832.datago.cexception.basic;

import org.apache.commons.lang3.StringUtils;

/**
 * 自定义空指针异常 继承自 MyRunTimeException
 * @author zy
 * @date 2021/01/28
 */
public class MyNullPointerException extends MyRunTimeException {
    public MyNullPointerException() {
        super();
    }

    public MyNullPointerException(String s) {
        super(MyRunTimeException.getMethodName(s));
    }

    public static void throwIfEmptyStr(String checkStr,String throwInfo) {
        boolean empty = StringUtils.isEmpty(checkStr);
        if (empty) {
            throw new MyNullPointerException(throwInfo);
        }
    }
}
