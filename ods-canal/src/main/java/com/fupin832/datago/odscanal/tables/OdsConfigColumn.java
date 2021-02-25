package com.fupin832.datago.odscanal.tables;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.table.types.DataType;

import java.io.Serializable;

/**
 * canal ods 配置列
 * 参看flink TableColumn 源码，采用 DataType
 *
 * @author zy
 * @date 2021/01/26
 */
public class OdsConfigColumn implements Serializable {
    /** 提取原始列名称 */
    private String sourceColumnName = null;
    /** 目标列名称 */
    private String sinkColumnName = null;

    /** 目标列使用值 允许null */
    private String sinkValue = null;
    /** 是否使用 sinkValue 默认为false */
    private boolean usingMySelfValue = false;

    /** 是否使用 更新此字段 */
    private boolean needUpdate = false;
    /** 是否使用 更新此字段 */
    private boolean needInsert = false;

    /** 该字段是否是主键 */
    private boolean isPK = false;
    /** 数据类型 暂时不需要使用，预留用于扩展使用 */
    private DataType sinkColumnDataType = null;

    public OdsConfigColumn() {}

    public OdsConfigColumn(String sourceColumnName, String sinkColumnName, String sinkValue, boolean usingMySelfValue, boolean needUpdate, boolean needInsert, boolean isPK) {
        this.sourceColumnName = sourceColumnName;
        this.sinkColumnName = sinkColumnName;
        this.sinkValue = sinkValue;
        this.usingMySelfValue = usingMySelfValue;
        this.needUpdate = needUpdate;
        this.needInsert = needInsert;
        this.isPK = isPK;
    }

    public OdsConfigColumn(String sourceColumnName, String sinkColumnName, String sinkValue, boolean usingMySelfValue, boolean needUpdate, boolean needInsert, boolean isPK, DataType sinkColumnDataType) {
        this.sourceColumnName = sourceColumnName;
        this.sinkColumnName = sinkColumnName;
        this.sinkValue = sinkValue;
        this.usingMySelfValue = usingMySelfValue;
        this.needUpdate = needUpdate;
        this.needInsert = needInsert;
        this.isPK = isPK;
        this.sinkColumnDataType = sinkColumnDataType;
    }

    /** Getter and Setter */
    public String getSourceColumnName() {
        return sourceColumnName;
    }

    public OdsConfigColumn setSourceColumnName(String sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
        return this;
    }

    public String getSinkColumnName() {
        return sinkColumnName;
    }

    public OdsConfigColumn setSinkColumnName(String sinkColumnName) {
        this.sinkColumnName = sinkColumnName;
        return this;
    }

    public String getSinkValue() {
        return sinkValue;
    }

    public OdsConfigColumn setSinkValue(String sinkValue) {
        this.sinkValue = sinkValue;
        return this;
    }

    public boolean isUsingMySelfValue() {
        return usingMySelfValue;
    }

    public OdsConfigColumn setUsingMySelfValue(boolean usingMySelfValue) {
        this.usingMySelfValue = usingMySelfValue;
        return this;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public OdsConfigColumn setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
        return this;
    }

    public boolean isNeedInsert() {
        return needInsert;
    }

    public OdsConfigColumn setNeedInsert(boolean needInsert) {
        this.needInsert = needInsert;
        return this;
    }

    public boolean isPK() {
        return isPK;
    }

    public OdsConfigColumn setPK(boolean PK) {
        isPK = PK;
        return this;
    }

    public DataType getSinkColumnDataType() {
        return sinkColumnDataType;
    }

    /** 对外调用 */
    public String getSinkColumnNameIfNull() {
        if (StringUtils.isEmpty(sinkColumnName)) {
            return this.sourceColumnName;
        } else {
            return sinkColumnName;
        }
    }

    public OdsConfigColumn setSinkColumnDataType(DataType sinkColumnDataType) {
        this.sinkColumnDataType = sinkColumnDataType;
        return this;
    }

}
