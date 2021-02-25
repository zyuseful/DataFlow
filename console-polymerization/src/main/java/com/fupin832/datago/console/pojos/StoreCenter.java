package com.fupin832.datago.console.pojos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.fupin832.datago.cexception.basic.MyNullPointerException;
import com.fupin832.datago.cexception.basic.MyRunTimeException;
import com.fupin832.datago.comconfig.sserver.nacos.NacosConfigSource;
import com.fupin832.datago.console.Application;
import com.fupin832.datago.console.service.manager.sqlstr.SqlDispose;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行计划存储中心
 *
 * @author zy
 * @date 2021/02/03
 */
public class StoreCenter {
    /**
     * 全局执行计划
     */
    public static final Map<Integer, List<PlanItemTask>> planStore = new ConcurrentHashMap();
    /**
     * 全局配置
     * KEY          VAL
     * CFG_STR      nacos 主配置json String
     */
    public static final Map<String,String> otherStore = new ConcurrentHashMap();
    public static final String CFG_STR = "CFG_STR";
    /**
     * 获取最大key 即执行时间
     *
     * @return
     */
    public static Integer maxTimeKey() {
        Set<Integer> integers = planStore.keySet();
        Integer result = 0;
        Iterator<Integer> iterator = integers.iterator();
        Integer next = null;
        while (iterator.hasNext()) {
            next = iterator.next();
            if (null == next) {
                continue;
            }
            if (next > result) {
                result = next;
            }
        }

        return result;
    }

    /**
     * 获取值
     * @param key
     * @return
     */
    public static List<PlanItemTask> getFromPlanStore(Integer key) {
        return planStore.get(key);
    }
    
    public static void updatePlanStore(NacosConfigSource nacos, List<Plan> plans) {
        if (null == plans || plans.isEmpty()) {
            return;
        }
        ConfigService nacosConfigService = nacos.getNacosConfigService();
        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            if (null == plan) {
                continue;
            }
            /** update store */
            try {
                getPlanAndStore(nacosConfigService, plan);
            } catch (MyRunTimeException e) {
                e.printStackTrace();
            } catch (NacosException e) {
                e.printStackTrace();
            }
        }
    }
    public static void updatePlanStore(ConfigService nacosConfigService, List<Plan> plans) {
        if (null == plans || plans.isEmpty()) {
            return;
        }
        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            if (null == plan) {
                continue;
            }
            /** update store */
            try {
                getPlanAndStore(nacosConfigService, plan);
            } catch (MyRunTimeException e) {
                e.printStackTrace();
            } catch (NacosException e) {
                e.printStackTrace();
            }
        }
    }
    public static void updatePlanStore(ConfigService nacosConfigService, String jsonStr) {
        if (null == jsonStr || jsonStr.isEmpty()) {
            return;
        }

        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (null == jsonObject) {
            return;
        }
        String configInfo = jsonObject.getString(Application.CONFIG_TASKS);
        List<Plan> plans = JSON.parseArray(configInfo, Plan.class);
        if (null == plans || plans.isEmpty()) {
            return;
        }
        updatePlanStore(nacosConfigService,plans);
    }

    /**
     * 将 Plan 信息提取 + 转换为PlanItemTask + 缓存
     * @param nacosConfigService
     * @param plan
     * @throws MyRunTimeException
     * @throws NacosException
     */
    private static void getPlanAndStore(ConfigService nacosConfigService, Plan plan) throws MyRunTimeException, NacosException {
        if (null == plan || null == nacosConfigService) {
            Application.ApplicationLog.error("plan or nacosConfigService is empty");
            throw new MyNullPointerException("plan or nacosConfigService is empty");
        }
        /** KEY 分钟个数 */
        Integer period = plan.getPeriod();
        List<PlanItem> setups = plan.getSetups();

        /** 构建 PlanItemTask */
        List<PlanItemTask> planItemTasks = new ArrayList<>(setups.size());
        PlanItem planItem = null;
        for (int i = 0; i < setups.size(); i++) {
            planItem = setups.get(i);
            if (null == planItem) {
                continue;
            }

            PlanItemTask planItemTask = new PlanItemTask()
                    .setTaskName(planItem.getName())
                    .setSqlList(new ArrayList<String>());

            String str = planItem.getStr();
            String[] split = str.split("->");

            String nacosStr = null;
            String[] nacosArr = null;
            String nacos_id = null;
            String nacos_group = null;
            for (int j = 0; j < split.length; j++) {
                nacosStr = split[j];
                nacosArr = nacosStr.split(",");
                nacos_id = nacosArr[0];
                nacos_group = nacosArr[1];
                /** 通过 id,group 同步获取nacos 配置信息 */
                String config = nacosConfigService.getConfig(nacos_id, nacos_group, 5000);
                planItemTask.addSqlStr(config);
            }
            planItemTasks.add(planItemTask);
        }

        /** 加入缓存 */
        planStore.put(period,planItemTasks);
    }

    public static void printPlanStore() {
        PlanItemTask planItemTask = null;
        for (Map.Entry<Integer, List<PlanItemTask>> entry : planStore.entrySet()) {
            Application.ApplicationLog.info("Key = {}",entry.getKey());
            List<PlanItemTask> planItemTasks = entry.getValue();
            if (null == planItemTasks) {
                continue;
            }
            for(int i=0;i<planItemTasks.size();i++) {
                planItemTask = planItemTasks.get(i);
                if (null == planItemTask) {
                    continue;
                }
                String taskName = planItemTask.getTaskName();
                Application.ApplicationLog.info("name:{}",taskName);
                List<String> sqlList = planItemTask.getSqlList();
                if (null == sqlList) {
                    continue;
                }
                String sql = null;
                for (int j=0;j<sqlList.size();j++) {
                    sql = sqlList.get(j);
                    Application.ApplicationLog.info("sql{}:{}",j,sql);
                }
            }
        }
    }
}
