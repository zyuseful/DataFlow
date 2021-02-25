//package com.fupin832.datago.odscanal.baks;
//
//import com.fupin832.datago.cflink.envs.MyEnvCreater;
//import com.fupin832.datago.cflink.params.MyArgParamTool;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.table.api.Table;
//import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
//
///**
// * BAK
// *
// * @author zy
// * @date 2021/02/18
// */
//public class BakApp {
//    public static void main(String[] args) {
////        way3(args);
////        way2(args);
//    }
//    public static void way3(String[] args) throws Exception {
//        MyArgParamTool.loadConfig(args);
//
//        final StreamExecutionEnvironment env = MyEnvCreater.BuildStreamExecutionEnvironment();
//        StreamTableEnvironment tenv = MyEnvCreater.BuildStreamTableEnvironment(env);
//
//        String dll =
//                "create table msg_notice_record (" +
//                        "tstc_no varchar(30)," +
//                        "discussed decimal(22,0)," +
//                        "cust_no varchar(50)," +
//                        "status decimal(22,0)," +
//                        "create_time TIMESTAMP," +
//                        "update_time TIMESTAMP," +
//                        "msg varchar(100)," +
//                        "att1 varchar(20)," +
//                        "att2 varchar(20)," +
//                        "att3 varchar(20)" +
//                        ") WITH (" +
//                        "'connector' = 'kafka'," +
//                        "'topic' = 'tob'," +
//                        "'properties.bootstrap.servers' = '10.1.102.236:9092'," +
//                        "'properties.group.id' = 'tob'," +
//                        "'format' = 'canal-json'" +
//                        ")";
//
//        tenv.executeSql(dll).print();
//
//        String dll1 =
//                "create table kafka_info (" +
//                        "tstc_no varchar(30)," +
//                        "discussed decimal(22,0)," +
//                        "cust_no varchar(50)," +
//                        "status decimal(22,0)," +
//                        "create_time TIMESTAMP," +
//                        "update_time TIMESTAMP," +
//                        "msg varchar(100)," +
//                        "att1 varchar(20)," +
//                        "att2 varchar(20)," +
//                        "att3 varchar(20)," +
//                        "PRIMARY KEY (tstc_no) NOT ENFORCED" +
////                        "PRIMARY KEY (tstc_no)" +
//                        ") WITH (" +
//                        "'connector' = 'upsert-kafka'," +
//                        "'topic' = 'tob1'," +
//                        "'properties.bootstrap.servers' = '10.1.102.236:9092'," +
//                        "'properties.group.id' = 'tob'," +
//                        "'key.format' = 'json'," +
//                        "'value.format' = 'json'," +
////                        "'value.format' = 'csv'," +
//                        "'value.fields-include' = 'EXCEPT_KEY'" +
//                        ")";
//        tenv.executeSql(dll1);
//
//        String createSql = "insert into kafka_info select tstc_no,discussed,cust_no,status,create_time,update_time,msg,att1,att2,att3 from msg_notice_record";
//        tenv.executeSql(createSql).await();
//
//        env.execute("Flink add sink");
//    }
//
//    /**
//     * OK
//     * Canal Format 功能 见 https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/dev/table/connectors/formats/canal.html#canal-format
//     * 以kafka 一个topic 作为一张表 存储，同时支持sql查询
//     * 使用感悟：
//     * 1、创建表后 op 字段会记录 +I(Insert), -U +U (一条update 会记录 一个 -U:修改前数据 +U:修改后数据) ,-D (删除操作)
//     * 2、使用此种方式接收数据后 不能支持ddl 操作，如添加字段，会导致程序报错，程序退出(以下例子为做异常捕获，可做验证如果异常处理是否会退出)
//     * 3、此方式即一个topic就是一张表了 如果修改其他上游数据表，FlagMessage 数据会与所创建表的字段表明比较 如果字段名不一致则置null
//     */
//    public static void way2(String[] args) throws Exception {
//        MyArgParamTool.loadConfig(args);
//
//        final StreamExecutionEnvironment env = MyEnvCreater.BuildStreamExecutionEnvironment();
//        StreamTableEnvironment tenv = MyEnvCreater.BuildStreamTableEnvironment(env);
//
//        String dll =
//                "create table msg_notice_record (" +
//                        "tstc_no varchar(30)," +
//                        "discussed decimal(22,0)," +
//                        "cust_no varchar(50)," +
//                        "status decimal(22,0)," +
//                        "create_time TIMESTAMP," +
//                        "update_time TIMESTAMP," +
//                        "msg varchar(100)," +
//                        "att1 varchar(20)," +
//                        "att2 varchar(20)," +
//                        "att3 varchar(20)," +
//                        "PRIMARY KEY (tstc_no) NOT ENFORCED" +
//                        ") WITH (" +
//                        "'connector' = 'kafka'," +
//                        "'topic' = 'tob'," +
//                        "'properties.bootstrap.servers' = '10.1.102.236:9092'," +
//                        "'properties.group.id' = 'tob'," +
//                        "'format' = 'canal-json'" +
//                        ")";
//
//        tenv.executeSql(dll);
//
//        String query1 = "select * from msg_notice_record";
//        Table tab1 = tenv.sqlQuery(query1);
//        tab1.execute().print();
//        //toAppendStream doesn't support consuming update and delete changes which is produced by node TableSourceScan(table=[[default_catalog, default_database, msg_notice_record]], fields=[tstc_no, discussed, cust_no, status, create_time, update_time, msg, att1, att2, att3])
////        tenv.toAppendStream(tab1, Row.class).print();
//
//        env.execute("Flink add sink");
//    }
//}
