package com.fupin832.datago.comconfig.sserver.basic;

import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItemTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

/**
 * 配置源服务类
 * 目的:提供读取配置文件方式以及配置内容类
 * @author zy
 * @date 2021/01/19
 */
public class ConfigSource {
    /** 配置源名称 */
    protected String configServiceName;

    /** 读取配置源 服务启动 所需ConfigService 配置参数参数 */
    protected List<ConfigItemTemplate> configServiceStartItems;

    /** Getter And Setter */
    public String getConfigServiceName() {
        return configServiceName;
    }
    public ConfigSource setConfigServiceName(String configServiceName) {
        this.configServiceName = configServiceName;
        return this;
    }
    public List<ConfigItemTemplate> getConfigServiceStartItems() {
        return configServiceStartItems;
    }
    public ConfigSource setConfigServiceStartItems(List<ConfigItemTemplate> configServiceStartItems) {
        this.configServiceStartItems = configServiceStartItems;
        return this;
    }

    /** Common Method */
    /**
     * 将 字符串读取到Properties中
     * @param str
     * @return
     */
    public static Properties confStringToProperties(String str) {
        Properties properties = null;
        StringReader stringReader = null;
        try {
            stringReader = new StringReader(str);
            properties = new Properties();
            properties.load(stringReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != stringReader) {
                stringReader.close();
                stringReader = null;
            }
        }
        return properties;
    }

    /** Abstract method */
}
