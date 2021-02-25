package com.fupin832.datago.odscanal.convert.sqlstr;

import com.fupin832.datago.odscanal.pojo.FlatMessage;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;

import java.io.Serializable;

/**
 * ddl 接口
 *
 * @author zy
 * @date 2021/01/27
 */
public interface Ddl extends Serializable {
    public String alterSql(FlatMessage fmg, OdsConfigTableManager oct);

    public String dropTableSql(FlatMessage fmg, OdsConfigTableManager oct);

    public String truncateTableSql(FlatMessage fmg, OdsConfigTableManager oct);

    public String createSql(FlatMessage fmg, OdsConfigTableManager oct);
}
