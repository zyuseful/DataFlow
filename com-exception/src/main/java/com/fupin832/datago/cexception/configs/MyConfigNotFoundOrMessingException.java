package com.fupin832.datago.cexception.configs;

import com.fupin832.datago.cexception.basic.MyNullPointerException;

/**
 * 用于 com-config 项目
 * 配置项未找到异常
 *
 * @author zy
 * @date 2021/01/29
 */
public class MyConfigNotFoundOrMessingException extends MyNullPointerException {
    public MyConfigNotFoundOrMessingException() {
        super();
    }

    public MyConfigNotFoundOrMessingException(String s) {
        super(s);
    }
}
