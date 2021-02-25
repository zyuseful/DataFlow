package com.fupin832.datago.cflink.kafkas;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * kafka消费者封装创建类
 *
 * @author zy
 * @date 2021/01/22
 */
public class MyKafkaConsumer {
    /**
     * 创建kafka消费者
     * @param kafkaBootstrapServer kafka server url
     * @param kafpaGroupId         groupId
     * @param kafkaTopic           topic
     * @return
     */
    public static FlinkKafkaConsumer createFlinkKafkaConsumer(String kafkaBootstrapServer,String kafpaGroupId,String kafkaTopic) {
        //for kafka properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", kafkaBootstrapServer);
        properties.setProperty("group.id", kafpaGroupId);

        return new FlinkKafkaConsumer(kafkaTopic,new SimpleStringSchema(),properties);
    }

    /**
     * 创建kafka消费者
     * @param kafkaBootstrapServer kafka server url
     * @param kafpaGroupId         groupId
     * @param kafkaTopic           topic
     * @return
     */
    /**
     * 创建kafka消费者
     * @param kafkaBootstrapServer  kafka server url
     * @param kafpaGroupId          groupId
     * @param kafkaTopic            topic
     * @param deserializationSchema 反序列化方式
     * @return
     */
    public static FlinkKafkaConsumer createFlinkKafkaConsumer(String kafkaBootstrapServer, String kafpaGroupId, String kafkaTopic, DeserializationSchema deserializationSchema) {
        //for kafka properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", kafkaBootstrapServer);
        properties.setProperty("group.id", kafpaGroupId);

        return new FlinkKafkaConsumer(kafkaTopic,deserializationSchema,properties);
    }

    /**
     * 创建kafka消费者
     * @param kafkaTopic    topic
     * @param properties    自行封装的kafka properties
     * @return
     */
    public static FlinkKafkaConsumer createFlinkKafkaConsumer(String kafkaTopic,Properties properties) {
        //for kafka properties
        return new FlinkKafkaConsumer(kafkaTopic,new SimpleStringSchema(),properties);
    }

}
