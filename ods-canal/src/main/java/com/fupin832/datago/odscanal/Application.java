package com.fupin832.datago.odscanal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fupin832.datago.cexception.basic.MyNullPointerException;
import com.fupin832.datago.cflink.envs.MyEnvCreater;
import com.fupin832.datago.cflink.kafkas.MyKafkaConsumer;
import com.fupin832.datago.cflink.params.MyArgParamTool;
import com.fupin832.datago.comconfig.manager.ConfigTemplateManager;
import com.fupin832.datago.comconfig.pconfigs.CanalConfigTemplate;
import com.fupin832.datago.comconfig.pconfigs.GreenplumConfigTemplate;
import com.fupin832.datago.comconfig.sserver.nacos.NacosConfigSource;
import com.fupin832.datago.comconfig.store.ConfigStore;
import com.fupin832.datago.odscanal.convert.basic.ConvertFactory;
import com.fupin832.datago.odscanal.convert.sqlstr.FlatMessageConvertFactoryToDataMsg;
import com.fupin832.datago.odscanal.convert.sqlstr.FlatMessageDDL;
import com.fupin832.datago.odscanal.convert.sqlstr.FlatMessageDML;
import com.fupin832.datago.odscanal.convert.sqlstr.SqlResult;
import com.fupin832.datago.odscanal.etl.FlatMessageMap;
import com.fupin832.datago.odscanal.logs.MyLOG;
import com.fupin832.datago.odscanal.service.ConfigReceiveAndDispose;
import com.fupin832.datago.odscanal.sink.GreenPlumSqlResultSink;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.internal.connection.JdbcConnectionProvider;
import org.apache.flink.connector.jdbc.internal.connection.SimpleJdbcConnectionProvider;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.plan.nodes.datastream.TimeCharacteristic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Flink 运行主类
 * Args:
 * --configway nacoscs --nacoscs_server_addr http://10.1.102.236:8848 --nacoscs_data_id ods_tob_insert --nacoscs_group ods_tob
 *
 * @author zy
 * @date 2021/01/20
 */
public class Application {
//    static Logger LOG = LoggerFactory.getLogger(Application.class);
    static Logger LOG = MyLOG.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        LOG.info("START");
        canalRun(args);
    }

    public static void canalRun(String[] args) throws Exception {
        FlinkKafkaConsumer<String> flinkKafkaConsumer = null;
        JdbcConnectionProvider jdbcConnectionProvider = null;
        OdsConfigTableManager odsConfigTableManager = null;
        try {
            MyArgParamTool.loadConfig(args);
            getConfigFromArgs();
            /** 创建 FlinkKafkaConsumer */
            flinkKafkaConsumer = createFlinkKafkaConsumer();
            /** 创建 jdbc连接所需的 JdbcConnectionOptions */
            jdbcConnectionProvider = createJdbcConnectionProvider();
            /** ods 配置表 manager */
            odsConfigTableManager = ConfigReceiveAndDispose.findOdsConfigTableManager();
            if (null == odsConfigTableManager) {
                throw new MyNullPointerException("周宇 OdsConfigTableManager is empty");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage() + "  停止Flink进程");
            throw e;
        }

        /** ods 配置转换器 */
        ConvertFactory<String, SqlResult> flatMessageConvertFactoryToDataMsg = FlatMessageConvertFactoryToDataMsg.createInstance()
                .setDDL(new FlatMessageDDL())
                .setDML(new FlatMessageDML())
                .setOdsConfigTableManager(odsConfigTableManager);

        /** Flink 消息 map: Canal json String -> FlatMessage -> 调用 ods 配置转换器 -> SqlResult */
        FlatMessageMap flatMessageMap = FlatMessageMap.getInstance().setConvertFactory(flatMessageConvertFactoryToDataMsg);

        /** Greenplum SqlResult Sink */
        GreenPlumSqlResultSink greenPlumSqlResultSink = GreenPlumSqlResultSink.getInstance()
                .addJdbcConnectionProvider(jdbcConnectionProvider)
                .addOdsConfigTableManager(odsConfigTableManager);

        final StreamExecutionEnvironment env = MyEnvCreater.BuildStreamExecutionEnvironment();
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                1, // 尝试重启次数
                Time.of(10, TimeUnit.SECONDS) // 延迟时间间隔
        ));
        env.getCheckpointConfig().setCheckpointInterval(10 * 1000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(120 * 1000);

        if (canalIsDebug()) {
            env.addSource(flinkKafkaConsumer)
                    .map(flatMessageMap)
                    .print();
        } else {
            env.addSource(flinkKafkaConsumer).name("kafka_canal").setParallelism(1)
                    .map(flatMessageMap).setParallelism(1)
                    .addSink(greenPlumSqlResultSink).name("greenplum").setParallelism(1);

//            env.addSource(flinkKafkaConsumer).setParallelism(1)
//                    .map(flatMessageMap)
//                    .keyBy(e -> e.getSourceTableName())
//                    .addSink(greenPlumSqlResultSink).setParallelism(4);
        }

        env.execute("Flink add sink");
    }

    /**
     * 创建 FlinkKafkaConsumer
     *
     * @return
     */
    public static FlinkKafkaConsumer createFlinkKafkaConsumer() {
        ConfigStore canal = ConfigTemplateManager.findConfigStore("canal");

        String canal_kafka_bootstrap_server = (String) canal.getConfigValue(CanalConfigTemplate.P_KAFKA_BOOTSTRAP_SERVER, null);
        String canal_kafka_group_id = (String) canal.getConfigValue(CanalConfigTemplate.P_KAFKA_GROUP_ID, null);
        String canal_kafka_topic = (String) canal.getConfigValue(CanalConfigTemplate.P_KAFKA_TOPIC, null);

        MyNullPointerException.throwIfEmptyStr(canal_kafka_bootstrap_server,CanalConfigTemplate.P_KAFKA_BOOTSTRAP_SERVER+" is empty");
        MyNullPointerException.throwIfEmptyStr(canal_kafka_group_id,CanalConfigTemplate.P_KAFKA_GROUP_ID+ " is empty");
        MyNullPointerException.throwIfEmptyStr(canal_kafka_topic,CanalConfigTemplate.P_KAFKA_TOPIC+ " is empty");
        //  kafka
        return MyKafkaConsumer.createFlinkKafkaConsumer(canal_kafka_bootstrap_server, canal_kafka_group_id, canal_kafka_topic);
    }

    /**
     * 创建 是否debug
     * debug true--> sink pring
     *       false-> sink gp
     * @return
     */
    public static Boolean canalIsDebug() {
        Boolean aBoolean = (Boolean)ConfigTemplateManager.findConfigStore(ConfigReceiveAndDispose.DEBUG_FLAG).getConfigValue(ConfigReceiveAndDispose.DEBUG_FLAG, false);
        return aBoolean;
    }

    /**
     * 创建 jdbc连接所需的 JdbcConnectionOptions
     *
     * @return
     */
    public static JdbcConnectionProvider createJdbcConnectionProvider() {
        ConfigStore sink = ConfigTemplateManager.findConfigStore(ConfigReceiveAndDispose.SINK);
        //数据库配置
        final String mppURL = (String)sink.getConfigValue(GreenplumConfigTemplate.P_DB_URL,null);
        final String mppDriverName = (String)sink.getConfigValue(GreenplumConfigTemplate.P_DB_DRIVER,null);
        final String mppUserName = (String)sink.getConfigValue(GreenplumConfigTemplate.P_DB_USER,null);
        final String mppPassword = (String)sink.getConfigValue(GreenplumConfigTemplate.P_DB_PWD,null);

        MyNullPointerException.throwIfEmptyStr(mppURL,GreenplumConfigTemplate.P_DB_URL+" is empty");
        MyNullPointerException.throwIfEmptyStr(mppDriverName,GreenplumConfigTemplate.P_DB_DRIVER+" is empty");
        MyNullPointerException.throwIfEmptyStr(mppUserName,GreenplumConfigTemplate.P_DB_USER+" is empty");
        MyNullPointerException.throwIfEmptyStr(mppPassword,GreenplumConfigTemplate.P_DB_PWD+" is empty");

        // jdbc
        JdbcConnectionOptions connectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl(mppURL)
                .withDriverName(mppDriverName)
                .withUsername(mppUserName)
                .withPassword(mppPassword)
                .build();
        SimpleJdbcConnectionProvider simpleJdbcConnectionProvider = new SimpleJdbcConnectionProvider(connectionOptions);
        return simpleJdbcConnectionProvider;
    }


    /**
     * 配置
     * 思路：
     * 主配置文件 -> CF1
     *          -> CF2
     *          -> CF3
     *          ...
     * --configway nacoscs --nacoscs_server_addr http://10.1.102.236:8848 --nacoscs_id ods_tob_control --group ods_tob
     *
     * @throws NacosException
     */
    public static void getConfigFromArgs() throws NacosException {
        //nacos 通过启动参数
        String nacosServiceAddress = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosServerAddrKey);
        String nacosId = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosDataIdKey);
        String nacosGroupKey = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosGroupKey);
        String nacosNameSpace = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosNameSpace);

        //创建 Nacos 配置读取对象
        NacosConfigSource instance = NacosConfigSource.getInstance(NacosConfigSource.SOURCE_CONFIG_NAME, nacosServiceAddress, nacosNameSpace);
        instance.addListener(nacosId, nacosGroupKey, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                if (StringUtils.isEmpty(configInfo)) {
                    return;
                }
                JSONObject jsonCfgObj = JSON.parseObject(configInfo);
                ConfigReceiveAndDispose configReceiveAndDispose = new ConfigReceiveAndDispose();
                //ods_table 配置获取
                try {
                    configReceiveAndDispose.convertOdsTables(jsonCfgObj,instance);
                } catch (NacosException e) {
                    e.printStackTrace();
                }
            }
        });
        String config = instance.getNacosConfigService().getConfig(nacosId, nacosGroupKey, 3000);
        JSONObject jsonCfgObj = JSON.parseObject(config);
        ConfigReceiveAndDispose configReceiveAndDispose = new ConfigReceiveAndDispose();

        //debug 配置获取
        configReceiveAndDispose.convertDebugFlagAndStore(jsonCfgObj);
        //canal 配置获取
        configReceiveAndDispose.convertCanalAndStore(jsonCfgObj);
        //sink 配置获取
        configReceiveAndDispose.convertGPAndStore(jsonCfgObj);
        //ods_table 配置获取
        configReceiveAndDispose.convertOdsTables(jsonCfgObj,instance);
    }
}
