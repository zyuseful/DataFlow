package com.fupin832.datago.odscanal.convert.sqlstr;

import java.util.Objects;

/**
 * SQL 解析数据返回结果
 *
 * @author zy
 * @date 2021/01/27
 */
public class SqlResult {
    private String sourceDBName;
    private String sourceTableName;
    private String sql;
    private SQLOptions sqlOption;
    private boolean isError;
    private String errInfo;

    public SqlResult() {}

    /** Getter */
    public String getSql() {
        return sql;
    }

    public SQLOptions getSqlOption() {
        return sqlOption;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public String getSourceDBName() {
        return sourceDBName;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    /** Setter */
    public SqlResult setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public SqlResult setSqlOption(SQLOptions sqlOption) {
        this.sqlOption = sqlOption;
        return this;
    }

    public SqlResult setError(boolean error) {
        isError = error;
        return this;
    }

    public SqlResult setErrInfo(String errInfo) {
        this.errInfo = errInfo;
        return this;
    }

    public SqlResult setSourceDBName(String sourceDBName) {
        this.sourceDBName = sourceDBName;
        return this;
    }

    public SqlResult setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
        return this;
    }

    @Override
    public String toString() {
        return "SqlResult{" +
                "sql='" + sql + '\'' +
                ", sqlOption=" + sqlOption +
                ", isError=" + isError +
                ", errInfo='" + errInfo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        SqlResult sqlResult = (SqlResult) o;
        return Objects.equals(sourceTableName, sqlResult.sourceTableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceTableName);
    }
}
