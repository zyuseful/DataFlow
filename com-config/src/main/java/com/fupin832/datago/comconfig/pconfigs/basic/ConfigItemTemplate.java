package com.fupin832.datago.comconfig.pconfigs.basic;

import com.fupin832.datago.cexception.configs.MyConfigNotFoundOrMessingException;
import com.fupin832.datago.comconfig.store.ConfigStore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 配置项集 模板设计模式
 * 子类继承后:
 * 1、实现 getInstance 方法 -- 实例化 同时 添加自己的配置项
 * 2、实现 checkConfigTemplate 方法 -- 检查自己的配置项
 * 3、子类将配置key 设置为静态 String
 * 子类注释:
 * 1、配置内容样例
 * 2、标注配置内容必填选填情况说明
 * 参见 {@link com.fupin832.datago.comconfig.pconfigs.CanalConfigTemplate}
 *
 * @author zy
 * @date 2021/01/18
 */
public abstract class ConfigItemTemplate {
    /**
     * 模板集合名称  甄别服务使用
     */
    protected String configItemTemplateName;
    /**
     * 模板集合
     */
    protected List<ConfigItem> configItems = new ArrayList<ConfigItem>();

    /**
     * Getter
     */
    public List<ConfigItem> getConfigItems() {
        return this.configItems;
    }

    public String getConfigItemTemplateName() {
        return configItemTemplateName;
    }

    /**
     * Hashcode And Equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigItemTemplate that = (ConfigItemTemplate) o;
        return Objects.equals(configItemTemplateName, that.configItemTemplateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configItemTemplateName);
    }

    /** Common Method */
    /**
     * 重载
     * 根据传入item 查询item集合是否包含从item
     * 当传入 item.itemName 为null 时 抛出 RuntimeException
     *
     * @return
     * @throws RuntimeException
     */
    public boolean hasConfigItem(String itemName) {
        if (StringUtils.isEmpty(itemName)) {
            throw new NullPointerException("itemName is null");
        }
        boolean result = false;
        for (ConfigItem ti : configItems) {
            if (null != ti) {
                String tempName = ti.getItemName();
                if (tempName.equals(itemName)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean hasConfigItem(ConfigItem item) throws RuntimeException {
        return hasConfigItem(item.getItemName());
    }


    /**
     * 根据ItemName 获取item
     *
     * @param itemName
     * @return
     */
    public ConfigItem findByItemName(String itemName) {
        ConfigItem result = null;
        String tempItemName = null;
        if (null == this.configItems || this.configItems.isEmpty()) {
            return null;
        }
        for (ConfigItem ti : configItems) {
            if (null != ti) {
                tempItemName = ti.getItemName();
                if (itemName.equals(tempItemName)) {
                    result = ti;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取必填项 ConfigItem集合
     *
     * @return
     */
    public List<ConfigItem> findRequiredItems() {
        List<ConfigItem> result = new ArrayList<ConfigItem>();
        if (null == this.configItems || this.configItems.isEmpty()) {
            return null;
        }
        for (ConfigItem ti : configItems) {
            if (null != ti) {
                boolean required = ti.isRequired();
                if (required) {
                    result.add(ti);
                }
            }
        }
        return result;
    }


    /**
     * 获取itemName 列表
     *
     * @return
     */
    public List<String> findAllitemNames() {
        if (null == this.configItems || this.configItems.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<String>(this.configItems.size());
        ConfigItem configItem = null;
        for (int i = 0; i < this.configItems.size(); i++) {
            configItem = this.configItems.get(i);
            if (null != configItem && null != configItem.getItemName()) {
                result.add(configItem.getItemName());
            }
        }
        return result;
    }

    /**
     * 添加 ConfigItem 配置元素
     *
     * @param item
     */
    public void addConfigItem(ConfigItem item) {
        boolean hasFlag = false;
        try {
            hasFlag = hasConfigItem(item);
        } catch (Exception e) {
        }

        if (!hasFlag) {
            this.configItems.add(item);
        }
    }

    /**
     * 批量添加 ConfigItem 配置元素
     *
     * @param items
     */
    public void addConfigItem(ConfigItem... items) {
        for (ConfigItem comeItem : items) {
            boolean hasFlag = false;
            try {
                hasFlag = false;
                hasFlag = hasConfigItem(comeItem);
            } catch (Exception e) {
            }
            if (!hasFlag) {
                this.configItems.add(comeItem);
            }
        }
    }


    /** Abstract Method */
    /**
     * 实例化
     *
     * @param configItemArrName <p>
     *                          ConfigItemTemplate configItemTemplate = new ConfigItemTemplate();
     *                          configItemTemplate.configItemArrName = configItemArrName;
     *                          return configItemTemplate;
     * @return
     */
    public abstract ConfigItemTemplate getInstance(String configItemArrName);

    /**
     * 所有必填项不能为空
     * 思路：获取必填配置项(或所需项) 与提供 Properties 对比，获取结果不为空
     *
     * @param properties
     * @throws MyConfigNotFoundOrMessingException
     */
    public void checkConfigTemplate(Properties properties) throws MyConfigNotFoundOrMessingException {
        List<ConfigItem> requiredItems = findRequiredItems();
        Object checkVal = null;
        for (ConfigItem ci : requiredItems) {
            checkVal = null;
            String itemName = ci.getItemName();
            checkVal = properties.get(itemName);
            if (null == checkVal) {
                MyConfigNotFoundOrMessingException myConfigNotFoundOrMessingException = new MyConfigNotFoundOrMessingException(getConfigItemTemplateName() + " " + ci.getItemName() + " is Null");
                myConfigNotFoundOrMessingException.setStopTheProgramFlag(true);
                throw myConfigNotFoundOrMessingException;
            }
        }
    }

    /**
     * 模板根据给定的数据 向 指定的 store存储值
     * @param findPrefix
     * @param savePrefix
     * @param properties
     * @param configStore
     */
    public void upToStore(String findPrefix, String savePrefix, Properties properties, ConfigStore configStore) {
        List<ConfigItem> configItems = getConfigItems();
        Object storeVal = null;
        String itemName = null;
        for (int i = 0; i < configItems.size(); i++) {
            storeVal = null;

            ConfigItem configItem = configItems.get(i);

            //查询key 前缀处理
            if (!StringUtils.isEmpty(findPrefix)) {
                itemName = findPrefix+configItem.getItemName();
            } else {
                itemName = configItem.getItemName();
            }

            //default value
            String defaultVal = configItem.getDefaultVal();

            storeVal = properties.get(itemName);
            storeVal = storeVal == null ? defaultVal : storeVal;

            //保存key 前缀处理
            if (!StringUtils.isEmpty(savePrefix)) {
                configStore.setConfigVal(savePrefix+itemName, storeVal);
            } else {
                configStore.setConfigVal(itemName, storeVal);
            }
        }
    }
}
