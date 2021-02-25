package com.fupin832.datago.console;

import com.fupin832.datago.comconfig.manager.ConfigTemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * 主程序启动类
 * 流程:
 * 开始 --> 读取配置 --> PlanStore
 *                        /|\
 *         变更配置 --------|
 *
 * 线程1：开始定时  -->  分钟计数  --> 与最大执行时间取余 --> 将所需任务放入队列执行
 * 线程2：执行任务与任务表对比 --> 执行任务
 *
 * PARAM: --nacoscs_server_addr http://10.1.102.236:8848 --nacoscs_id tob_plan --nacoscs_group cpolymerization --nacoscs_namespace console-polymerization --task_manager '10,20,60' --console_show 'T,2000' --task_show 'T,5000'
 *
 * @author zy
 * @date 2021/02/02
 */
public class Application {
    /** 配置文件 */
    public static final String CONFIG_SINK = "sink";
    public static final String CONFIG_TASKS = "tasks";
    public static final String CONFIG_DWD = "dwd";
    public static final String CONFIG_DWS = "dws";


    /** Application Log */
    public static final Logger ApplicationLog = LogManager.getLogger("ApplicationLog");
    /** Nacos Log 配置变更记录 */
    public static final Logger NacosLog = LogManager.getLogger("NacosLog");
    /** Task Creater Log 任务日志 */
    public static final Logger TaskCreaterLog = LogManager.getLogger("TaskCreaterLog");
    /** Cpolymerization Task Log 任务日志 */
    public static final Logger CpolymerizationTaskLog = LogManager.getLogger("CpolymerizationTaskLog");

    public static void main2(String[] args) {
        ApplicationLog.info("Hello");
        ApplicationLog.info("Hello");
        ApplicationLog.info("Hello");
        ApplicationLog.info("Hello");
        ApplicationLog.info("Hello");
    }
    public static void main(String[] args) throws InterruptedException {
        /** 初始化配置文件 */
        configInit(args);
        /** console 监控 */
        runConsoleShow();
        runTaskManager(10,20,60);
        /** 任务监控 注意，一定要在TasmManager 初始化后运行 */
        runTasksShow();
    }



    /**
     * 读取配置文件
     * @param args
     */
    private static void configInit(String[] args) {
        Work.nacosConf(args);
        Work.gpConfig(args);
    }

    /**
     * 配置监控
     * @return
     */
    private static Thread runConsoleShow() {
        Properties paramProperties = ConfigTemplateManager.getParamProperties();
        String[] cs = ConfigTemplateManager.getParamProperties().getProperty("console_show").replaceAll("'","").split(",");
        Integer p1 = 1;
        if (cs[0].equalsIgnoreCase("T")) {
            p1 = -1;
        } else {
            p1 = Integer.parseInt(cs[0]);
        }
        return Work.buildConsoleShowAndRun(p1,Integer.parseInt(cs[1]));
    }

    /**
     * TASK监控
     * @return
     */
    private static Thread runTasksShow() {
        String[] ts = ConfigTemplateManager.getParamProperties().getProperty("task_show").replaceAll("'","").split(",");
        Integer p1 = 1;
        if (ts[0].equalsIgnoreCase("T")) {
            p1 = -1;
        } else {
            p1 = Integer.parseInt(ts[0]);
        }
        return Work.buildTaskShowAndRun(p1,Integer.parseInt(ts[1]));
    }

    /**
     * 开启任务处理线程
     * @param coreTaskNum
     * @param maxTaskNum
     * @param periodSecond
     * @return
     */
    private static void runTaskManager(int coreTaskNum,int maxTaskNum,long periodSecond) {
        String[] ts = ConfigTemplateManager.getParamProperties().getProperty("task_manager").replaceAll("'","").split(",");
        Work.buildTaskManagerAndRun(Integer.parseInt(ts[0]),Integer.parseInt(ts[1]),Long.parseLong(ts[2]));
    }
}
