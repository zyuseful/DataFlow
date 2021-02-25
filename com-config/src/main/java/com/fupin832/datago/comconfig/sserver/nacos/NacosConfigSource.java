package com.fupin832.datago.comconfig.sserver.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fupin832.datago.cexception.configs.MyConfigNotFoundOrMessingException;
import com.fupin832.datago.comconfig.sserver.basic.ConfigSource;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * Nacos 配置服务
 * 单元测试 {com.fupin832.datago.comconfig.ways.TestNacosConfigService}
 * <p>
 * projectArgs:[nacoscs_server_addr, nacoscs_data_id, nacoscs_group, nacoscs_server_addr, nacoscs_data_id, nacoscs_group]
 * canalConfigItems:[source_db_driver, source_db_url, source_db_user, source_db_password, canal_kafka_bootstrap_server, canal_kafka_group_id, canal_kafka_topic, canal_kafka_zk]
 *
 * @author zy
 * @date 2021/01/18
 */
public class NacosConfigSource extends ConfigSource {
    public static final String SOURCE_CONFIG_NAME = "nacoscs";
    public static final String nacosServerAddrKey = "server_addr";
    public static final String nacosDataIdKey = "id";
    public static final String nacosGroupKey = "group";
    public static final String nacosNameSpace = "namespace";

    public static final long nacosConnectTimeOut = 10000;

    /**
     * nacos server address
     */
    private String nacosServerAddress;
    private com.alibaba.nacos.api.config.ConfigService nacosConfigService = null;

    private NacosConfigSource() {
    }

    /**
     * Getter And Setter
     */
    public String getNacosServerAddress() {
        return nacosServerAddress;
    }

    private NacosConfigSource setNacosServerAddress(String nacosServerAddress) {
        this.nacosServerAddress = nacosServerAddress;
        return this;
    }

    public ConfigService getNacosConfigService() {
        return nacosConfigService;
    }

    private NacosConfigSource setNacosConfigService(ConfigService nacosConfigService) {
        this.nacosConfigService = nacosConfigService;
        return this;
    }

    /**
     * 创建 NacosConfigSource(configServiceName,nacosServerAddrKey)
     *
     * @param configServiceName
     * @param nacosServiceAddress
     * @return
     * @throws NacosException
     */
    public static NacosConfigSource getInstance(String configServiceName, String nacosServiceAddress) throws NacosException {
        return getInstance(configServiceName, nacosServiceAddress, null);
    }

    /**
     * 创建 NacosConfigSource(configServiceName,nacosServerAddrKey,nacosNameSpace)
     * 
     * @param configServiceName
     * @param nacosServiceAddress
     * @return
     * @throws NacosException
     */
    public static NacosConfigSource getInstance(String configServiceName, String nacosServiceAddress, String nacosNameSpace) throws NacosException {
        if (StringUtils.isEmpty(configServiceName)) {
            throw new MyConfigNotFoundOrMessingException("configServiceName is null");
        }

        NacosConfigSource nacosConfigSource = new NacosConfigSource();
        nacosConfigSource.setConfigServiceName(configServiceName);

        //创建 nacos config service
        ConfigService nacoscomConfigService = createNacosConfigService(nacosServiceAddress, nacosNameSpace);
        nacosConfigSource.setNacosConfigService(nacoscomConfigService);

        return nacosConfigSource;
    }

    /**
     * 添加 NacosConfigSource 监听器
     *
     * @param dataId
     * @param group
     * @param listener
     * @return
     * @throws NacosException
     */
    public NacosConfigSource addListener(String dataId, String group, Listener listener) throws NacosException {
        this.getNacosConfigService().addListener(dataId, group, listener);
        return this;
    }

    /** 重载 */
    /**
     * 创建 nacos 连接服务
     *
     * @param nacosServiceAddr
     * @return
     */
    public static ConfigService createNacosConfigService(String nacosServiceAddr, String nacosNameSpace) throws NacosException {
        Properties propertiesForNacosInit = new Properties();
        propertiesForNacosInit.put("serverAddr", nacosServiceAddr);
        if (!StringUtils.isEmpty(nacosNameSpace)) {
            propertiesForNacosInit.put("namespace", nacosNameSpace);
        }
        ConfigService configService = NacosFactory.createConfigService(propertiesForNacosInit);
        return configService;
    }

    public static ConfigService createNacosConfigService(Properties propertiesForNacosInit) throws NacosException {
        ConfigService configService = NacosFactory.createConfigService(propertiesForNacosInit);
        return configService;
    }
}
