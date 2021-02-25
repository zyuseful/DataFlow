//package com.fupin832.datago.comconfig.manager;
//
//import com.fupin832.datago.comconfig.store.ConfigStore;
//import com.fupin832.datago.comconfig.sserver.basic.ConfigSource;
//import com.fupin832.datago.comconfig.sserver.nacos.NacosConfigSource;
//import org.apache.flink.test.util.AbstractTestBase;
//import org.junit.Test;
//
//import java.util.Properties;
//import java.util.Set;
//
///**
// * TODO this is description
// *
// * @author zy
// * @date 2021/01/19
// */
//public class TestConfigTemplateManager extends AbstractTestBase {
//    /**
//     * 1 测试注册服务功能
//     * 2 测试注册配置服务名相同的情况
//     */
//    @Test
//    public void testConfigComeParamsDispose() {
//        String serverAddr = "http://10.1.102.236:8848";
//        String dataId = "ods_tob_insert";
//        String group = "ods_tob";
//        long timeoutMs = 3000;
//        Properties properties = new Properties();
//        properties.put(NacosConfigSource.nacosGroupKey,group);
//        properties.put(NacosConfigSource.nacosDataIdKey,dataId);
//        properties.put(NacosConfigSource.nacosServerAddrKey,serverAddr);
//
//        ConfigStore configStoreCenter = new ConfigStore();
//        ConfigTemplateManager instance = ConfigTemplateManager.getInstance(NacosConfigSource.CONFIG_SERVICE_NAME+","+ NacosConfigSource.CONFIG_SERVICE_NAME, configStoreCenter,properties);
//        ConfigTemplateManager instance1 = ConfigTemplateManager.getInstance(NacosConfigSource.CONFIG_SERVICE_NAME+","+ NacosConfigSource.CONFIG_SERVICE_NAME, configStoreCenter,properties);
//
//        System.out.println(instance==instance1);
//        Set<ConfigSource> members = instance.getConfigServiceMembers();
//        System.out.println(members);
//
//        String configComeParams = instance.getConfigComeParams();
//        System.out.println(configComeParams);
//    }
//}
