package com.fupin832.datago.odscanal.tables;

/**
 * dml & ddl 枚举类
 *
 * @author zy
 * @date 2021/01/26
 */
public enum CanalDDLAndDMLType {

    /**
     * DELETE
     */
    DELETE(CanalDDLAndDMLType.DML_DELETE, CanalDDLAndDMLType.DML_DELETE_VAL, CanalDDLAndDMLType.DML),
    /**
     * INSERT
     */
    INSERT(CanalDDLAndDMLType.DML_INSERT, CanalDDLAndDMLType.DML_INSERT_VAL, CanalDDLAndDMLType.DML),
    /**
     * UPDATE
     */
    UPDATE(CanalDDLAndDMLType.DML_UPDATE, CanalDDLAndDMLType.DML_UPDATE_VAL, CanalDDLAndDMLType.DML),

    /**
     * DROP
     */
    DROP(CanalDDLAndDMLType.DDL_DROP, CanalDDLAndDMLType.DDL_DROP_VAL, CanalDDLAndDMLType.DDL),
    /**
     * CREATE
     */
    CREATE(CanalDDLAndDMLType.DDL_CREATE, CanalDDLAndDMLType.DDL_CREATE_VAL, CanalDDLAndDMLType.DDL),
    /**
     * ALTER
     */
    ALTER(CanalDDLAndDMLType.DDL_ALTER, CanalDDLAndDMLType.DDL_ALTER_VAL, CanalDDLAndDMLType.DDL),
    /**
     * TRUNCATE
     */
    TRUNCATE(CanalDDLAndDMLType.DDL_TRUNCATE, CanalDDLAndDMLType.DDL_TRUNCATE_VAL, CanalDDLAndDMLType.DDL);




    /**
     * DML String And Value
     */
    public static final String DML = "DML";
    public static final String DML_INSERT = "INSERT";
    public static final String DML_UPDATE = "UPDATE";
    public static final String DML_DELETE = "DELETE";

    public static final byte DML_INSERT_VAL = 1;
    public static final byte DML_UPDATE_VAL = 2;
    public static final byte DML_DELETE_VAL = 0;


    /**
     * DDL String And Value
     */
    public static final String DDL = "DDL";
    public static final String DDL_DROP = "ERASE";
    public static final String DDL_CREATE = "CREATE";
    public static final String DDL_ALTER = "ALTER";
    public static final String DDL_TRUNCATE = "TRUNCATE";

    public static final byte DDL_DROP_VAL = 10;
    public static final byte DDL_CREATE_VAL = 11;
    public static final byte DDL_ALTER_VAL = 12;
    public static final byte DDL_TRUNCATE_VAL = 13;


    /**
     * 成员属性
     */
    private final String name;
    private final byte value;
    private final String type;

    CanalDDLAndDMLType(String dmlName, byte value, String type) {
        this.name = dmlName;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }
    public byte getValue() {
        return this.value;
    }

    /**
     * 根据给定 名称查找 枚举对象
     * @param name
     * @return
     */
    public static CanalDDLAndDMLType findName(String name) {
        switch (name) {
            case DML_INSERT: return INSERT;
            case DML_UPDATE: return UPDATE;
            case DML_DELETE: return DELETE;

            case DDL_DROP: return DROP;
            case DDL_CREATE: return CREATE;
            case DDL_ALTER: return ALTER;
            case DDL_TRUNCATE: return TRUNCATE;
            default: throw new UnsupportedOperationException(
                    "Unsupported name '" + name + "' for CanalDDLAndDMLType.");
        }
    }

    /**
     * 根据给定 值查找 枚举对象
     * @param value
     * @return
     */
    public static CanalDDLAndDMLType findValue(byte value) {
        switch (value) {
            case DML_INSERT_VAL: return INSERT;
            case DML_UPDATE_VAL: return UPDATE;
            case DML_DELETE_VAL: return DELETE;

            case DDL_DROP_VAL: return DROP;
            case DDL_CREATE_VAL: return CREATE;
            case DDL_ALTER_VAL: return ALTER;
            case DDL_TRUNCATE_VAL: return TRUNCATE;
            default: throw new UnsupportedOperationException(
                    "Unsupported value '" + value + "' for CanalDDLAndDMLType.");
        }
    }
}
