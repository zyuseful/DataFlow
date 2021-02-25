package com.fupin832.datago.console.pojos;

/**
 * 配置文件 item
 *
 * @author zy
 * @date 2021/02/03
 */
public class PlanItem {
    private String name;
    private String str;

    public PlanItem() {}

    /** Getters */
    public String getName() {
        return name;
    }

    public String getStr() {
        return str;
    }

    /** Setters */
    public PlanItem setName(String name) {
        this.name = name;
        return this;
    }

    public PlanItem setStr(String str) {
        this.str = str;
        return this;
    }

    @Override
    public String toString() {
        return "PlanItem{" +
                "name='" + name + '\'' +
                ", str='" + str + '\'' +
                '}';
    }
}
