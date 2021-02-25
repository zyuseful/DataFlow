package com.fupin832.datago.comconfig.store;


import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置常量类
 * 明确请求参数 与 配置参数
 *
 * @author zy
 * @date 2021/01/18
 */
public class ConfigStore {
    private String name;
    /**
     * 配置常量
     */
    private final ConcurrentHashMap<String, Object> configMap = new ConcurrentHashMap<String, Object>();

    private ConfigStore() {
    }

    /**
     * 获取实例化 ConfigStore 对象
     *
     * @return
     */
    public static ConfigStore getInstance(String name) {
        return new ConfigStore().setName(name);
    }

    /**
     * Getter And Setter
     */
    public String getName() {
        return name;
    }

    private ConfigStore setName(String name) {
        this.name = name;
        return this;
    }


    /**
     * 获取配置 key 对应的 value
     *
     * @param key
     * @param defaultVal
     * @return
     */
    public Object getConfigValue(String key, Object defaultVal) {
        Object result = configMap.get(key);
        if (null == result) {
            return defaultVal;
        } else {
            return result;
        }
    }

    /**
     * 获取配置 key 对应的 value
     *
     * @param key
     * @param val
     * @return
     */
    public ConfigStore setConfigVal(String key, Object val) {
        configMap.put(key, val);
        return this;
    }

    public ConcurrentHashMap<String, Object> getConfigMap() {
        return configMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigStore that = (ConfigStore) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
