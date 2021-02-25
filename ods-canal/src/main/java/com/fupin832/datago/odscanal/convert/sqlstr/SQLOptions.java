package com.fupin832.datago.odscanal.convert.sqlstr;

import org.apache.commons.lang3.StringUtils;

/**
 * sql ddl + dml enum
 *
 * @author zy
 * @date 2021/01/27
 */
public enum SQLOptions {

    DDL_ALTER(SQLOptions.ALTER,0,SQLOptions.DDL),
    DDL_DROP(SQLOptions.DROP,1,SQLOptions.DDL),
    DDL_TRUNCATE(SQLOptions.TRUNCATE,2,SQLOptions.DDL),
    DDL_CREATE(SQLOptions.CREATE,3,SQLOptions.DDL),

    DML_INSERT(SQLOptions.INSERT,10,SQLOptions.DML),
    DML_UPDATE(SQLOptions.UPDATE,11,SQLOptions.DML),
    DML_DELETE(SQLOptions.DELETE,12,SQLOptions.DML);


    public static final String DDL = "DDL";
    public static final String ALTER = "ALTER";
    public static final String DROP = "DROP";
    public static final String TRUNCATE = "TRUNCATE";
    public static final String CREATE = "CREATE";

    public static final String DML = "DML";
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";


    // 成员变量
    private String name;
    private int index;
    private String type;
    // 构造方法
    private SQLOptions(String name, int index, String type) {
        this.name = name;
        this.index = index;
        this.type = type;
    }

    // 普通方法
    public static SQLOptions findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new NullPointerException("com.fupin832.datago.odscanal.convert.sqlstr.SQLOptions: null name");
        }
        for (SQLOptions c : SQLOptions.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public boolean isDDL() {
        return this.getType().equals(DDL);
    }

    public boolean isDML() {
        return this.getType().equals(DML);
    }

    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
