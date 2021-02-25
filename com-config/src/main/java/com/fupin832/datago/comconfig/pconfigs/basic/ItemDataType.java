package com.fupin832.datago.comconfig.pconfigs.basic;

/**
 * 配置项类型
 *
 * @author zy
 * @date 2021/01/19
 */
public enum ItemDataType {
    STRING("String",0),INT("Integer",1),
    FLOAT("Float",2),DOUBLE("Double",3),
    JSON_STRING("String",4);


    // 成员变量
    private String name;
    private int index;
    // 构造方法
    private ItemDataType(String name, int index) {
        this.name = name;
        this.index = index;
    }
    // 普通方法
    public static String getName(int index) {
        for (ItemDataType c : ItemDataType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
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
}
