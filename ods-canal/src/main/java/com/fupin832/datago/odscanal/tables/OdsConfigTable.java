package com.fupin832.datago.odscanal.tables;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * canal ods 配置表
 * TODO 优化 insert、。。。
 *
 * @author zy
 * @date 2021/01/26
 */
public class OdsConfigTable implements Serializable {

    /**
     * 源数据库名
     */
    private String sourceDBName;
    /**
     * 源表名
     */
    private String sourceTableName;
    /**
     * 目标数据库(可为空)
     */
    private String sinkDBName;
    /**
     * 目标表(可为空 + 默认值 this.sourceTableName)
     */
    private String sinkTableName;
    /**
     * sink dml
     */
    private String[] sinkDml;
    /**
     * sink ddl
     */
    private String[] sinkDdl;

    public static final String DML_INSERT = "INSERT";
    public static final String DML_UPDATE = "UPDATE";
    public static final String DML_DELETE = "DELETE";

    /**
     * dlm insert(可为空 + 默认值)
     */
//    private String insertDML = CanalDDLAndDMLType.DML_INSERT;
    private String insertDML;
    /**
     * dlm update(可为空 + 默认值)
     */
//    private String updateDML = CanalDDLAndDMLType.DML_UPDATE;
    private String updateDML;
    /**
     * dlm delete(可为空 + 默认值)
     */
//    private String deleteDML = CanalDDLAndDMLType.DML_DELETE;
    private String deleteDML;

    /**
     * 表列
     */
    List<OdsConfigColumn> columns = new ArrayList<OdsConfigColumn>();

    //业务--需要insert的所有列(包括主键列)
    private List<OdsConfigColumn> needToInsertColumns;
    //业务--需要insert && 非自有列()
    private List<OdsConfigColumn> needToInsertAndNoMySelfValColumns;
    //业务--需要insert && 自有列()
    private List<OdsConfigColumn> needToInsertAndMySelfValColumns;
    //业务--需要主键列
    private List<String> pkColumns;
    //业务--需要update && 非自有列
    private List<OdsConfigColumn> needToUpdateAndNoMySelfValueColumns;
    //业务--需要update && 自有列
    private List<OdsConfigColumn> needToUpdateAndMySelfValueColumns;


    public OdsConfigTable() {
    }
    public OdsConfigTable(String sourceDBName, String sourceTableName, String sinkDBName, String sinkTableName, String[] sinkDml, String[] sinkDdl, String insertDML, String updateDML, String deleteDML, List<OdsConfigColumn> columns) {
        this.sourceDBName = sourceDBName;
        this.sourceTableName = sourceTableName;
        this.sinkDBName = sinkDBName;
        this.sinkTableName = sinkTableName;
        this.sinkDml = sinkDml;
        this.sinkDdl = sinkDdl;
        this.columns = columns;

        this.setDeleteDML(deleteDML);
        this.setInsertDML(insertDML);
        this.setUpdateDML(updateDML);
    }

    public static OdsConfigTable getInstanceByJsonStr(String jsonStr) {
        OdsConfigTable result = JSON.parseObject(jsonStr, OdsConfigTable.class);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String ddlArr = jsonObject.getString("ddl");
        if (StringUtils.isEmpty(ddlArr)) {
            String[] ddls = {"INSERT","DELETE","UPDATE"};
            result.setSinkDdl(ddls);
        } else {
            result.setSinkDdl(ddlArr.split(","));
        }
        return result;
    }

    /**
     * Getter
     */
    public String getSourceDBName() {
        return sourceDBName;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public String getSinkDBName() {
        return sinkDBName;
    }

    public String getSinkTableName() {
        return sinkTableName;
    }

    public String[] getSinkDml() {
        return sinkDml;
    }

    public String[] getSinkDdl() {
        return sinkDdl;
    }

    public String getInsertDML() {
        return insertDML;
    }

    public String getUpdateDML() {
        return updateDML;
    }

    public String getDeleteDML() {
        return deleteDML;
    }

    public List<OdsConfigColumn> getColumns() {
        return columns;
    }

    /**
     * Setter
     */
    public OdsConfigTable setSourceDBName(String sourceDBName) {
        this.sourceDBName = sourceDBName;
        return this;
    }

    public OdsConfigTable setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
        return this;
    }

    public OdsConfigTable setSinkDBName(String sinkDBName) {
        this.sinkDBName = sinkDBName;
        return this;
    }

    public OdsConfigTable setSinkTableName(String sinkTableName) {
        this.sinkTableName = sinkTableName;
        return this;
    }

    public OdsConfigTable setSinkDml(String[] sinkDml) {
        this.sinkDml = sinkDml;
        return this;
    }

    public OdsConfigTable setSinkDdl(String[] sinkDdl) {
        this.sinkDdl = sinkDdl;
        return this;
    }

    public OdsConfigTable setInsertDML(String insertDML) {
        if (StringUtils.isEmpty(insertDML)) {
            this.insertDML = insertDML;
        } else {
            this.insertDML = insertDML.toLowerCase();
        }
        return this;
    }

    public OdsConfigTable setUpdateDML(String updateDML) {
        if (StringUtils.isEmpty(updateDML)) {
            this.updateDML = updateDML;
        } else {
            this.updateDML = updateDML.toLowerCase();
        }
        return this;
    }

    public OdsConfigTable setDeleteDML(String deleteDML) {
        if (StringUtils.isEmpty(deleteDML)) {
            this.deleteDML = deleteDML;
        } else {
            this.deleteDML = deleteDML.toLowerCase();
        }
        return this;
    }

    public OdsConfigTable setColumns(List<OdsConfigColumn> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 返回SinkTableName 如果为空则为 SourceTableName
     *
     * @return
     */
    public String getSinkTableNameIfNull() {
        if (StringUtils.isEmpty(this.sinkTableName)) {
            return sourceTableName;
        } else {
            return sinkTableName;
        }
    }

    /**
     * 返回 SinkDBName.SinkTableName 形式数据
     * 例子： D1.A -> GP1.AA
     * GP1 is empty: AA
     * GP1 is not empty: GP1.AA
     * AA is empty: A
     * AA is empty but GP1 is not empty: A
     *
     * @return
     */
    public String getSinkDBAndTableNameIfNull() {
        if (StringUtils.isEmpty(this.sinkTableName)) {
            return sourceTableName;
        } else {
            if (StringUtils.isEmpty(this.sinkDBName)) {
                return sinkTableName;
            } else {
                return this.sinkDBName + "." + sinkTableName;
            }
        }
    }

    /**
     * 根据给定源列名称 查找 OdsConfigColumn列对象
     *
     * @param findSourceColumnName
     * @return
     */
    public OdsConfigColumn findSourceColumn(String findSourceColumnName) {
        OdsConfigColumn result = null;
        for (OdsConfigColumn configColumn : columns) {
            String sourceColumnName = configColumn.getSourceColumnName();
            if (StringUtils.isEmpty(sourceColumnName)) {
                continue;
            }
            boolean equals = sourceColumnName.equals(findSourceColumnName);
            if (equals) {
                result = configColumn;
                break;
            }
        }
        return result;
    }

    /**
     * 根据给定目标列名称 查找 OdsConfigColumn列对象
     *
     * @param findSinkColumnName
     * @return
     */
    public OdsConfigColumn findSinkColumn(String findSinkColumnName) {
        OdsConfigColumn result = null;
        for (OdsConfigColumn configColumn : columns) {
            String sinkColumnName = configColumn.getSinkColumnName();
            if (StringUtils.isEmpty(sinkColumnName)) {
                continue;
            }
            boolean equals = sinkColumnName.equals(findSinkColumnName);
            if (equals) {
                result = configColumn;
                break;
            }
        }
        return result;
    }

    /**
     * 获取主键集合
     *
     * @return
     */
    public List<String> findSourcePKs() {
        if (pkColumns == null) {
            synchronized (this) {
                pkColumns = new ArrayList<String>();
                for (OdsConfigColumn configColumn : columns) {
                    if (configColumn.isPK()) {
                        /** 注意，因为这里取原表id */
                        pkColumns.add(configColumn.getSourceColumnName());
                    }
                }
            }
        }
        return pkColumns;
    }

    /**
     * 获取需要insert 的列
     *
     * @return
     */
    public List<OdsConfigColumn> findNeedToInsertColumns() {
        if (needToInsertColumns == null) {
            synchronized (this) {
                if (needToInsertColumns == null) {
                    needToInsertColumns = new ArrayList<OdsConfigColumn>();

                    if (null == columns || columns.isEmpty()) {
                        return needToInsertColumns;
                    }

                    for (int i = 0; i < columns.size(); i++) {
                        OdsConfigColumn sqlColumn = columns.get(i);
                        if (sqlColumn.isNeedInsert()) {
                            needToInsertColumns.add(sqlColumn);
                        }
                    }
                }
            }
        }
        return needToInsertColumns;
    }

    /**
     * 查询需要更新的非自有列：
     * 1、需要更新 (needInsert=TRUE)
     * 2、非自有添加列 (usingMySelfValue=FALSE)
     *
     * @return
     */
    public List<OdsConfigColumn> findNeedInsertAndNoMySelfValueColumns() {
        if (needToInsertAndNoMySelfValColumns == null) {
            synchronized (this) {
                if (needToInsertAndNoMySelfValColumns == null) {
                    needToInsertAndNoMySelfValColumns = columns.stream().filter(c -> {
                        return (c.isNeedInsert() && (!c.isUsingMySelfValue()));
                    }).collect(Collectors.toList());
                }
            }
        }
        return needToInsertAndNoMySelfValColumns;
    }

    /**
     * 查询需要更新的自有列：
     * 1、需要更新 (needInsert=TRUE)
     * 2、非自有添加列 (usingMySelfValue=TRUE)
     *
     * @return
     */
    public List<OdsConfigColumn> findNeedInsertAndMySelfValueColumns() {
        if (needToInsertAndMySelfValColumns == null) {
            synchronized (this) {
                if (needToInsertAndMySelfValColumns == null) {
                    needToInsertAndMySelfValColumns = columns.stream().filter(c -> {
                        return (c.isNeedInsert() && (c.isUsingMySelfValue()));
                    }).collect(Collectors.toList());
                }
            }
        }
        return needToInsertAndMySelfValColumns;
    }

    /**
     * 获取需要更新 + 自有字段列 的所有字段集合
     * @return
     */
    public List<OdsConfigColumn> findNeedToUpdateAndMySelfValueColumns() {
        if (needToUpdateAndMySelfValueColumns == null) {
            synchronized (this) {
                if (needToUpdateAndMySelfValueColumns == null) {
                    needToUpdateAndMySelfValueColumns =  columns.stream().filter(c -> {
                        return c.isNeedUpdate() && c.isUsingMySelfValue();
                    }).collect(Collectors.toList());
                }
            }
        }
        return needToUpdateAndMySelfValueColumns;
    }
    /**
     * 获取需要更新 + 非自有字段列 的所有字段集合
     * @return
     */
    public List<OdsConfigColumn> findNeedToUpdateAndNoMySelfValueColumns() {
        if (needToUpdateAndNoMySelfValueColumns == null) {
            synchronized (this) {
                if (needToUpdateAndNoMySelfValueColumns == null) {
                    needToUpdateAndNoMySelfValueColumns =  columns.stream().filter(c -> {
                        return c.isNeedUpdate() && !c.isUsingMySelfValue();
                    }).collect(Collectors.toList());
                }
            }
        }
        return needToUpdateAndNoMySelfValueColumns;
    }

    /**
     * 根据指定列查询
     *
     * @param fromList
     * @param querySqlColumn
     * @return
     */
    public List<OdsConfigColumn> findColumnsByList(List<OdsConfigColumn> fromList, OdsConfigColumn querySqlColumn) {
        if (null == fromList && fromList.isEmpty()) {
            return null;
        }
        List<OdsConfigColumn> result = new ArrayList<OdsConfigColumn>();
        String sinkColumn = querySqlColumn.getSinkColumnName();
        String sinkValue = querySqlColumn.getSinkValue();
        String sourceColumn = querySqlColumn.getSourceColumnName();
        Boolean needUpdate = querySqlColumn.isNeedUpdate();
        Boolean usingMySelfValue = querySqlColumn.isUsingMySelfValue();

        boolean add = true;
        for (int i = 0; i < fromList.size(); i++) {
            OdsConfigColumn sqlColumn = fromList.get(i);
            add = true;
            if (null != sinkColumn) {
                if (!sqlColumn.getSinkColumnName().equals(sinkColumn)) {
                    add &= false;
                }
            }
            if (null != sinkValue) {
                if (!sqlColumn.getSinkValue().equals(sinkValue)) {
                    add &= false;
                }
            }
            if (null != sourceColumn) {
                if (!sqlColumn.getSourceColumnName().equals(sourceColumn)) {
                    add &= false;
                }
            }
            if (null != needUpdate) {
                if (!Boolean.valueOf(sqlColumn.isNeedUpdate()).equals(needUpdate)) {
                    add &= false;
                }
            }
            if (null != usingMySelfValue) {
                if (!Boolean.valueOf(sqlColumn.isUsingMySelfValue()).equals(usingMySelfValue)) {
                    add &= false;
                }
            }

            if (add) {
                result.add(sqlColumn);
            }
        }
        return result;
    }


}
