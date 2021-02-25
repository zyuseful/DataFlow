package com.fupin832.datago.odscanal.convert.basic;

import java.io.Serializable;

/**
 * sql 转换管理类
 * 实现子类自行维护自己所需属性，自行维护是否单例
 * <I> 输入泛型
 * <O> 输出泛型
 *
 * @author zy
 * @date 2021/01/25
 */
public interface ConvertFactory<I,O> extends Serializable {
    /**
     * 接口编程
     * 衔接ETL过程后调用，由ETL主导通知调用
     * 泛型I 输入类型
     * 泛型O 输出类型
     * @param i
     * @return
     */
    O convertTo(I i) throws Exception;
}
