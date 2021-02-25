package com.fupin832.datago.cexception.basic;

/**
 * 功能不支持 Exception
 *
 * @author zy
 * @date 2021/01/29
 */
public class MyNotSupportException extends MyRunTimeException {
    public MyNotSupportException() {
        super();
    }

    public MyNotSupportException(String s) {
        super(MyRunTimeException.getMethodName(String.format("目前程序还不支持 {} !", s)));
    }
}
