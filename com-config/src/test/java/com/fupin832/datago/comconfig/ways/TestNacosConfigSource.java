//package com.fupin832.datago.comconfig.ways;
//
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItem;
//import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItemArr;
//import com.fupin832.datago.comconfig.store.ConfigStore;
//import com.fupin832.datago.comconfig.sserver.nacos.NacosConfigSource;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.Properties;
//
///**
// * 单元测试 CanalConfigItems
// * 被测试类请见 {@link NacosConfigSource}
// * @author zy
// * @date 2021/01/19
// */
//public class TestNacosConfigSource {
//    @Test
//    public void testgetArgs() {
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
//        NacosConfigSource nacosConfigService = NacosConfigSource.getInstance(configStoreCenter,properties);
//        NacosConfigSource nacosConfigService1 = NacosConfigSource.getInstance(configStoreCenter,properties);
//
//        System.out.println("==================================");
//        //测试 CONFIG_SERVICE_NAME
//        System.out.println(NacosConfigSource.CONFIG_SERVICE_NAME);
//        //测试单例
//        System.out.println(">测试单例");
//        System.out.println(nacosConfigService == nacosConfigService1);
//
//        //测试getProjectArgsKeyArr()
//        System.out.println(">测试getProjectArgsKeyArr");
//        List<String> projectArgsKeyArr = nacosConfigService.findAllProjectArgsKeyArr();
//        System.out.println(projectArgsKeyArr);
//
//        //测试getCanalConfigItemsKeyArr()
//        System.out.println(">测试getCanalConfigItemsKeyArr");
//        List<String> canalConfigItemsKeyArr = nacosConfigService.findAllCanalConfigItemsKeyArr();
//        System.out.println(canalConfigItemsKeyArr);
//
//        //测试getServiceConfigItemByItemName
//        System.out.println(">测试getServiceConfigItemByItemName");
//        ConfigItem nacosDataIdKeyConfig = nacosConfigService.findServiceConfigItemByItemName(NacosConfigSource.nacosDataIdKey);
//        ConfigItem nacosGroupKeyKeyConfig = nacosConfigService.findServiceConfigItemByItemName(NacosConfigSource.nacosGroupKey);
//        ConfigItem nacosServerAddrKeyConfig = nacosConfigService.findServiceConfigItemByItemName(NacosConfigSource.nacosServerAddrKey);
//        System.out.println(nacosDataIdKeyConfig);
//        System.out.println(nacosGroupKeyKeyConfig);
//        System.out.println(nacosServerAddrKeyConfig);
//
//        //测试获取配置信息
//        System.out.println(">测试获取配置信息");
//        ConfigService nacos = nacosConfigService.getNacos();
//        String config = null;
//        try {
//            config = nacos.getConfig(dataId, group, timeoutMs);
//        } catch (NacosException e) {
//            e.printStackTrace();
//        }
//        System.out.println(config);
//
//        //测试存储值是否成功
//        System.out.println(">测试存储值是否成功");
//        ConfigItemArr configServiceItems = nacosConfigService.getCanalConfigItems();
//        for (int i=0;i<configServiceItems.getConfigItems().size();i++) {
//            ConfigItem configItem = configServiceItems.getConfigItems().get(i);
//            Object configVal = nacosConfigService.getConfigStoreCenter().getConfigVal(configItem.getItemName(), null);
//            System.out.println(configVal);
//        }
//    }
//}
