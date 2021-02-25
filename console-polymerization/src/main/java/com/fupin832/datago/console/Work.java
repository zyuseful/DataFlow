package com.fupin832.datago.console;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.fupin832.datago.cexception.basic.MyNullPointerException;
import com.fupin832.datago.cflink.params.MyArgParamTool;
import com.fupin832.datago.comconfig.manager.ConfigTemplateManager;
import com.fupin832.datago.comconfig.sserver.nacos.NacosConfigSource;
import com.fupin832.datago.console.pojos.PlanItemTask;
import com.fupin832.datago.console.pojos.StoreCenter;
import com.fupin832.datago.console.service.manager.tasks.CpolymerizationTask;
import com.fupin832.datago.console.service.manager.tasks.ShowConfigStoreThread;
import com.fupin832.datago.console.service.manager.TaskManager;
import com.fupin832.datago.console.service.manager.tasks.ShowLastTaskEndTime;
import com.fupin832.datago.console.service.sink.CpolymerizationCount;
import com.fupin832.datago.console.service.sink.GreenPlumConfig;
import com.fupin832.datago.console.service.sink.GreenPlumSink;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工作创建类
 *
 * @author zy
 * @date 2021/02/04
 */
public class Work {
    public static void nacosConf(String[] args) {
        /** 获取配置参数 && 存储 */
        MyArgParamTool.loadConfig(args);
        Application.ApplicationLog.info("main启动参数 {}",args.toString());

        /** Nacos server */
        String nacos_server_addr = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosServerAddrKey);
        String nacos_id = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosDataIdKey);
        String nacos_group = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosGroupKey);
        String nacos_namespace = ConfigTemplateManager.getParamProperties().getProperty(NacosConfigSource.SOURCE_CONFIG_NAME + "_" + NacosConfigSource.nacosNameSpace);
        Application.ApplicationLog.info("nacos配置文件 address={} id={} group={} namespace={}",nacos_server_addr,nacos_id,nacos_group,nacos_namespace);

        /** 服务名 */
        final String nacosServerName = "nacos_console_polymerization";
        try {
            NacosConfigSource nacos = NacosConfigSource.getInstance(nacosServerName, nacos_server_addr, nacos_namespace);
            ConfigService nacosConfigService = nacos.getNacosConfigService();
            nacos.addListener(nacos_id, nacos_group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    if (StringUtils.isEmpty(configInfo)) {
                        return;
                    }
                    Application.NacosLog.info("感知Nacos主配置Plan数据变更{}{}",System.lineSeparator(),configInfo);
                    /** 存入全局配置 */
                    StoreCenter.otherStore.put(StoreCenter.CFG_STR,configInfo);
                    /** 更新全局Plan缓存 写 */
                    StoreCenter.updatePlanStore(nacosConfigService,configInfo);
                }
            });
            String config = nacosConfigService.getConfig(nacos_id, nacos_group,5000);
            /** 存入全局配置 */
            StoreCenter.otherStore.put("CFG_STR",config);
            Application.NacosLog.info("启动后读取配置 {}",config);
            Application.ApplicationLog.info("将主配置Plan数据予以存储");
            StoreCenter.updatePlanStore(nacosConfigService,config);

        } catch (Exception e) {
            e.printStackTrace();
            Application.ApplicationLog.error(e.getMessage());
        }
    }

    public static void gpConfig(String[] args) {
        /** 获取配置参数 && 存储 */
        String gpStr = StoreCenter.otherStore.get(StoreCenter.CFG_STR);
        if (StringUtils.isEmpty(gpStr)) {
            throw new MyNullPointerException("GP 配置无法获取").setStopTheProgramFlag(true);
        }
        JSONObject jsonObject = JSON.parseObject(gpStr);
        if (null == jsonObject) {
            throw new MyNullPointerException("GP 配置无法反序列化").setStopTheProgramFlag(true);
        }


        String gpConfigStr = jsonObject.getString(Application.CONFIG_SINK);
        Integer dwd = jsonObject.getInteger(Application.CONFIG_DWD);
        Integer dws = jsonObject.getInteger(Application.CONFIG_DWS);

        GreenPlumConfig greenPlumConfig = JSON.parseObject(gpConfigStr, GreenPlumConfig.class);
        GreenPlumSink.init(greenPlumConfig,
                new CpolymerizationCount()
                    .setDwd(dwd)
                    .setDws(dws));
    }

    public static Thread buildConsoleShowAndRun(int runTimes,int milliseconds) {
        ShowConfigStoreThread result = new ShowConfigStoreThread(runTimes,milliseconds);
        result.setDaemon(true);
        result.start();
        return result;
    }

    public static Thread buildTaskShowAndRun(int runTimes, int milliseconds) {
        ShowLastTaskEndTime result = new ShowLastTaskEndTime(runTimes,milliseconds);
        result.setDaemon(true);
        result.start();
        return result;
    }

    public static void buildTaskManagerAndRun(int coreTaskNum, int maxTaskNum, long periodSecond) {
        /** TaskManager 准备工作 */
        Application.ApplicationLog.info("初始化TashManager...");
        TaskManager.init(coreTaskNum,maxTaskNum,1000*60*10);
        Application.ApplicationLog.info("TashManager 线程池建立 启动进程={},最大进程数={},轮询间隔={}秒",coreTaskNum,maxTaskNum,periodSecond);

        //定时任务轮询 -> 获取最大任务时间间隔 -> 求余+添加任务 -> 任务进队列
        /** 效果一样 ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();*/
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            /** 当前最大时间key */
            Integer maxTimeKey = 1;
            @Override
            public void run() {
                TaskManager.count++;
                maxTimeKey = StoreCenter.maxTimeKey();
                Application.TaskCreaterLog.info("TaskManager.count={}, MaxTimeKey={}",TaskManager.count,maxTimeKey);
                /** 没有配置 无需后续 */
                if (null == maxTimeKey) {
                    return;
                }

                /** 求余计算*/
                TaskManager.count = TaskManager.count%maxTimeKey;
                Application.TaskCreaterLog.info("TaskManager.count取余后={}",TaskManager.count);

                /** 接收任务队列指针 */
                List<PlanItemTask> fromPlanStore = null;

                /** if -> 最大值即为key; else -> 余数为key + 取值判空 */
                if (!TaskManager.count.equals(0)) {
                    fromPlanStore = StoreCenter.getFromPlanStore(TaskManager.count);
                } else {
                    fromPlanStore = StoreCenter.getFromPlanStore(maxTimeKey);
                }

                /** 为空 则继续循环增加时间key */
                if (fromPlanStore == null || fromPlanStore.isEmpty()) {
                    return;
                }

                /** 任务进队列 */
                PlanItemTask planItemTask = null;
                for (int i=0;i<fromPlanStore.size();i++) {
                    if (null == fromPlanStore.get(i) || StringUtils.isEmpty(fromPlanStore.get(i).getTaskName())) {
                        continue;
                    }

                    Application.TaskCreaterLog.info("{} 加入队列",fromPlanStore.get(i).getTaskName());
                    TaskManager.executeTask(new CpolymerizationTask().setPlanItem(fromPlanStore.get(i)));
                }
            }
        }, 0, periodSecond, TimeUnit.SECONDS);
        Application.ApplicationLog.info("任务添加进程启动");
    }

}
