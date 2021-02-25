package com.fupin832.datago.comconfig.pconfigs.basic;

import java.util.Objects;

/**
 * 单独配置项
 *
 * @author zy
 * @date 2021/01/18
 */
public class ConfigItem {
    /* item 配置名称、配置key 同一组item配置时保障 itemName 不重复*/
    public String itemName;
    public String groupName;
    /* 0 string, 1 int, 2 float, 3 double */
    public ItemDataType itemDataType;
    /* 是否必选 默认 false 必选 */
    public boolean required = false;
    /* item 默认值*/
    public String defaultVal;

    public ConfigItem() {}

    public ConfigItem(String itemName, String groupName, ItemDataType itemDataType, boolean required, String defaultVal) {
        this.itemName = itemName;
        this.groupName = groupName;
        this.itemDataType = itemDataType;
        this.required = required;
        this.defaultVal = defaultVal;
    }

    public String getItemName() {
        return itemName;
    }

    public ConfigItem setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public ItemDataType getItemDataType() {
        return itemDataType;
    }

    public ConfigItem setItemDataType(ItemDataType itemDataType) {
        this.itemDataType = itemDataType;
        return this;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public ConfigItem setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public ConfigItem setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public boolean isRequired() {
        return required;
    }

    public ConfigItem setRequired(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigItem that = (ConfigItem) o;
        return  Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName);
    }
}
