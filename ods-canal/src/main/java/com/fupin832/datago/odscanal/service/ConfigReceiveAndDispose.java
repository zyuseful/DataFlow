package com.fupin832.datago.odscanal.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.fupin832.datago.comconfig.manager.ConfigTemplateManager;
import com.fupin832.datago.comconfig.pconfigs.CanalConfigTemplate;
import com.fupin832.datago.comconfig.pconfigs.GreenplumConfigTemplate;
import com.fupin832.datago.comconfig.sserver.nacos.NacosConfigSource;
import com.fupin832.datago.comconfig.sserver.nacos.NacosIdGroupBean;
import com.fupin832.datago.comconfig.store.ConfigStore;
import com.fupin832.datago.odscanal.tables.OdsConfigTable;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 配置接收 + 处理类
 *
 * @author zy
 * @date 2021/02/09
 */
public class ConfigReceiveAndDispose {
    public static final String  ODS_TABLE_KEY = "otables";
    public static final String  DEBUG_FLAG = "debug";
    public static final String CANAL = "canal";
    public static final String SINK = "sink";


    public void convertCanalAndStore(JSONObject jsonObject) {
        Properties canalProperties = new Properties();
        JSONObject jsonCanal = jsonObject.getJSONObject(CANAL);
        canalProperties.put(CanalConfigTemplate.P_KAFKA_BOOTSTRAP_SERVER,jsonCanal.getString(CanalConfigTemplate.P_KAFKA_BOOTSTRAP_SERVER));
        canalProperties.put(CanalConfigTemplate.P_KAFKA_TOPIC,jsonCanal.getString(CanalConfigTemplate.P_KAFKA_TOPIC));
        canalProperties.put(CanalConfigTemplate.P_KAFKA_GROUP_ID,jsonCanal.getString(CanalConfigTemplate.P_KAFKA_GROUP_ID));
        canalProperties.put(CanalConfigTemplate.P_KAFKA_ZOOKEEPER_CONNECT,jsonCanal.getString(CanalConfigTemplate.P_KAFKA_ZOOKEEPER_CONNECT));

        ConfigStore canalConfigStore = ConfigStore.getInstance(CANAL);
        new CanalConfigTemplate().getInstance(CANAL).upToStore(null,null,canalProperties,canalConfigStore);
        ConfigTemplateManager.addConfigStore(CANAL,canalConfigStore);
    }

    public void convertGPAndStore(JSONObject jsonCfgObj) {
        Properties sinkProperties = new Properties();
        JSONObject jsonCanal = jsonCfgObj.getJSONObject(SINK);
        sinkProperties.put(GreenplumConfigTemplate.P_DB_URL,jsonCanal.getString(GreenplumConfigTemplate.P_DB_URL));
        sinkProperties.put(GreenplumConfigTemplate.P_DB_DRIVER,jsonCanal.getString(GreenplumConfigTemplate.P_DB_DRIVER));
        sinkProperties.put(GreenplumConfigTemplate.P_DB_USER,jsonCanal.getString(GreenplumConfigTemplate.P_DB_USER));
        sinkProperties.put(GreenplumConfigTemplate.P_DB_PWD,jsonCanal.getString(GreenplumConfigTemplate.P_DB_PWD));

        ConfigStore sinkConfigStore = ConfigStore.getInstance(SINK);
        new GreenplumConfigTemplate().getInstance(SINK).upToStore(null,null,sinkProperties,sinkConfigStore);
        ConfigTemplateManager.addConfigStore(SINK,sinkConfigStore);
    }

    public void convertOdsTables(JSONObject jsonCfgObj, NacosConfigSource instance) throws NacosException {
        String jsonOtabs = jsonCfgObj.getString(ODS_TABLE_KEY);
        List<NacosIdGroupBean> nids = JSON.parseArray(jsonOtabs, NacosIdGroupBean.class);

        NacosIdGroupBean nacosIdGroupBean = null;

        ConfigStore odsTableConfigStore = ConfigStore.getInstance(ODS_TABLE_KEY);
        ArrayList<OdsConfigTable> octArr = new ArrayList();
        OdsConfigTableManager result = new OdsConfigTableManager(octArr);
        //更新全局 ods 表配置
        for (int i=0;i<nids.size();i++) {
            System.out.println(i);
            nacosIdGroupBean = nids.get(i);
            if (null == nacosIdGroupBean) {
                continue;
            }
            String config = instance.getNacosConfigService().getConfig(nacosIdGroupBean.getId(), nacosIdGroupBean.getGroup(), 3000);
            OdsConfigTable odsConfigTable = OdsConfigTable.getInstanceByJsonStr(config);

            String sourceTableName = odsConfigTable.getSourceTableName();
            //主键检查
            List<String> sourcePKs = odsConfigTable.findSourcePKs();
            if (null == sourcePKs || sourcePKs.isEmpty()) {
                throw new RuntimeException(sourceTableName+"需要填写主键");
            }
            octArr.add(odsConfigTable);
        }

        odsTableConfigStore.setConfigVal(ConfigReceiveAndDispose.ODS_TABLE_KEY,result);
        ConfigTemplateManager.addConfigStore(ConfigReceiveAndDispose.ODS_TABLE_KEY,odsTableConfigStore);
    }


    public void convertDebugFlagAndStore(JSONObject jsonCfgObj) {
        Boolean aBoolean = jsonCfgObj.getBoolean(DEBUG_FLAG);
        ConfigStore odsDebugFlag = ConfigStore.getInstance(DEBUG_FLAG);
        odsDebugFlag.setConfigVal(DEBUG_FLAG,aBoolean);
        ConfigTemplateManager.addConfigStore(ConfigReceiveAndDispose.DEBUG_FLAG,odsDebugFlag);
    }

    public static OdsConfigTableManager findOdsConfigTableManager() {
        OdsConfigTableManager result = (OdsConfigTableManager)ConfigTemplateManager.findConfigStore(ConfigReceiveAndDispose.ODS_TABLE_KEY).getConfigValue(ConfigReceiveAndDispose.ODS_TABLE_KEY, null);
        return result;
    }
}
