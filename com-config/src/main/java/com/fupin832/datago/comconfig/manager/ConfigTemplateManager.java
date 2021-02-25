package com.fupin832.datago.comconfig.manager;

import com.fupin832.datago.comconfig.sserver.basic.ConfigSource;
import com.fupin832.datago.comconfig.store.ConfigStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理者 负责关联 store、config server、config items
 *
 * @author zy
 * @date 2021/01/19
 */

public class ConfigTemplateManager {
    /* 程序启动需要指定的顶级配置参数，目的用于指导需要使用哪种配置服务建立连接 */
    public final static String CONFIG_WAY = "configway";
    /**
     * 存储接收到的配置顶级参数
     */
    private String configComeParams;
    /**
     * 程序启动参数
     */
    private static Map<String, ConfigStore> storeMap = new ConcurrentHashMap<>();
    /**
     * key 业务决定 value ConfigStore
     */
    private static Properties paramProperties;

    private static HashMap<String, ConfigSource> configSourceMap = new HashMap<>();
    private static Object lockForConfigSourceMap = new Object();


    public static ConfigStore findConfigStore(String key) {
        return storeMap.get(key);
    }

    public static boolean hasConfigStore(String key) {
        return storeMap.containsKey(key);
    }

    public static void addConfigStore(String key, ConfigStore cs) {
        storeMap.put(key, cs);
    }

    public static void setParamProperties(Properties p) {
        if (paramProperties == null) {
            synchronized (ConfigTemplateManager.class) {
                if (paramProperties == null) {
                    paramProperties = p;
                }
            }
        }
    }

    public static Properties getParamProperties() {
        return paramProperties;
    }


    public static void addConfigSource(String key, ConfigSource cc) {
        synchronized (lockForConfigSourceMap) {
            boolean b = configSourceMap.containsKey(key);
            if (!b) {
                configSourceMap.put(key, cc);
            }
        }
    }

    public static ConfigSource findConfigSource(String key) {
        return configSourceMap.get(key);
    }
}
