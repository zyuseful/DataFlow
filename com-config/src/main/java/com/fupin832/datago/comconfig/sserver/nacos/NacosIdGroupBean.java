package com.fupin832.datago.comconfig.sserver.nacos;

/**
 * Nacos Id + Group 配置bean
 *
 * @author zy
 * @date 2021/02/09
 */
public class NacosIdGroupBean {

    private String id;
    private String group;

    public NacosIdGroupBean() {}

    public NacosIdGroupBean(String id, String group) {
        this.id = id;
        this.group = group;
    }

    public String getId() {
        return id;
    }

    public NacosIdGroupBean setId(String id) {
        this.id = id;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public NacosIdGroupBean setGroup(String group) {
        this.group = group;
        return this;
    }

    @Override
    public String toString() {
        return "NacosIdGroupBean{" +
                "id='" + id + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
