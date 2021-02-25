package com.fupin832.datago.comconfig.pconfigs;

import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItem;
import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItemTemplate;
import com.fupin832.datago.comconfig.pconfigs.basic.ItemDataType;

/**
 * canal 参数配置类
 *
 * 配置内容样例: [source_db_driver, source_db_url, source_db_user, source_db_password, canal_kafka_bootstrap_server, canal_kafka_group_id, canal_kafka_topic, canal_kafka_zk]
 * 配置内容必填选填情况: 所有项均为必填项
 *
 * 获取当前配置可单元测试获取: {com.fupin832.datago.comconfig.configs.TestCanalConfigItems}
 *
 * @author zy
 * @date 2021/01/18
 */
public class CanalConfigTemplate extends ConfigItemTemplate {
    //Canal Kafka
    public final static String P_KAFKA_GROUP = "canal_kafka";
    /** kafka server */
    public final static String P_KAFKA_BOOTSTRAP_SERVER = "canal_kafka_bootstrap_server";
    /*group id*/
    public final static String P_KAFKA_GROUP_ID = "canal_kafka_group_id";
    /** topic */
    public final static String P_KAFKA_TOPIC = "canal_kafka_topic";
    /** zookeeper connect */
    public final static String P_KAFKA_ZOOKEEPER_CONNECT = "canal_kafka_zk";
    /** zookeeper connect */
    public final static String P_DEBUG = "debug";


    @Override
    public ConfigItemTemplate getInstance(String configItemArrName) {
        this.configItemTemplateName = configItemArrName;
        init();
        return this;
    }

    private void init() {
        /*Kafka*/
        ConfigItem kafka_bootstrap_server = new ConfigItem();
        kafka_bootstrap_server.setItemName(P_KAFKA_BOOTSTRAP_SERVER)
                .setGroupName(P_KAFKA_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);
        ConfigItem kafka_group_id = new ConfigItem();
        kafka_group_id.setItemName(P_KAFKA_GROUP_ID)
                .setGroupName(P_KAFKA_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);
        ConfigItem kafka_topic = new ConfigItem();
        kafka_topic.setItemName(P_KAFKA_TOPIC)
                .setGroupName(P_KAFKA_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);
        ConfigItem kafka_zk = new ConfigItem();
        kafka_zk.setItemName(P_KAFKA_ZOOKEEPER_CONNECT)
                .setGroupName(P_KAFKA_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);

        this.addConfigItem(kafka_bootstrap_server,kafka_group_id,kafka_topic,kafka_zk);
    }
}
