package com.fupin832.datago.cflink.envs;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 快速创建 Flink Env
 *
 * @author zy
 * @date 2021/01/21
 */
public class MyEnvCreater {
    public static StreamExecutionEnvironment BuildStreamExecutionEnvironment() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        return env;
    }

    public static EnvironmentSettings BuildEnvironmentSettings() {
        EnvironmentSettings blinkStreamSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        return blinkStreamSettings;
    }

    public static StreamTableEnvironment BuildStreamTableEnvironment(StreamExecutionEnvironment env) {
        StreamTableEnvironment sEnv = StreamTableEnvironment.create(env);
        return sEnv;
    }
    public static StreamTableEnvironment BuildStreamTableEnvironment(StreamExecutionEnvironment env, EnvironmentSettings blinkStreamSettings) {
        StreamTableEnvironment sEnv = StreamTableEnvironment.create(env, blinkStreamSettings);
        return sEnv;
    }
}
