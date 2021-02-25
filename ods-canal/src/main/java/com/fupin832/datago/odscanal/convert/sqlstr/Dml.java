package com.fupin832.datago.odscanal.convert.sqlstr;

import com.fupin832.datago.odscanal.pojo.FlatMessage;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;

import java.io.Serializable;

/**
 * dml 接口
 *
 * @author zy
 * @date 2021/01/27
 */
public interface Dml extends Serializable {
    public String insertSql(FlatMessage fmg, OdsConfigTableManager oct);

    public String updateSql(FlatMessage fmg, OdsConfigTableManager oct);

    public String deleteSql(FlatMessage fmg, OdsConfigTableManager oct);
}
