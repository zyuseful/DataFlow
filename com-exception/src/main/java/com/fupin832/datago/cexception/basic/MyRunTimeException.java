package com.fupin832.datago.cexception.basic;

/**
 * 自定义异常父类
 *
 * @author zy
 * @date 2021/01/28
 */
public class MyRunTimeException extends RuntimeException{
    protected boolean needStop = false;
    /**
     * 设置是否需要停止程序标识
     * @return
     */
    public MyRunTimeException setStopTheProgramFlag(boolean flat) {
        this.needStop = flat;
        return this;
    }

    public boolean getStopTheProgramFlag() {
        return needStop;
    }

    public MyRunTimeException() {
        super();
    }

    public MyRunTimeException(String s) {
        super(s);
    }

    public static String getMethodName(String info) {
        /**
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ");
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        sb.append(stackTraceElement.getClassName());
        sb.append(" Method: ");
        sb.append(stackTraceElement.getMethodName());
        sb.append(" Lines: ");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(" MSG: ");
        sb.append(info);
        return sb.toString();
        */
        return info;
    }
}
