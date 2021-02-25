package com.fupin832.datago.console.service.manager.tasks;

import com.fupin832.datago.cexception.basic.MyNullPointerException;
import com.fupin832.datago.console.Application;
import com.fupin832.datago.console.pojos.PlanItemTask;
import com.fupin832.datago.console.pojos.TaskStatus;
import com.fupin832.datago.console.service.manager.TaskManager;
import com.fupin832.datago.console.service.sink.GreenPlumSink;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 任务执行
 *
 * @author zy
 * @date 2021/02/03
 */
public class CpolymerizationTask implements Runnable {

    private PlanItemTask planItem;

    public CpolymerizationTask() {}

    /** Getter And Setter */
    public PlanItemTask getPlanItem() {
        return planItem;
    }

    public CpolymerizationTask setPlanItem(PlanItemTask planItem) {
        this.planItem = planItem;
        return this;
    }


    @Override
    public void run() {
        TaskStatus result = new TaskStatus();
        result.setStartTime(new Date());

        if (null == planItem) {
            return;
        }

        String taskName = null;
        taskName = planItem.getTaskName();
        boolean needRun = TaskManager.addTaskTable(taskName, result);
        if (!needRun) {
            Application.CpolymerizationTaskLog.info("任务:{} 已运行,进行忽略  运行详情! 开始时间:{}",taskName,result.getStartTimeStr());
            return;
        }
        result.setName(taskName);
        /** 获取上次存储的运行时间 */
        Date taskLastRunTime = TaskManager.findTaskLastRunTime(taskName);
        /** 当日零点作为起始时间开始运行 运行后由程序记录本次执行开始时间作为下次跨度的开始时间 */
        if (null == taskLastRunTime) {
            long current=System.currentTimeMillis();//当前时间毫秒数
            long todayZero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset() -1;
//            long todayZero=current/(1000*3600*24)*(1000*3600*24) -1;
            taskLastRunTime = new Date(todayZero);
        }

        // 目的 最后需要清除任务列表
        try {
            disposeSql(planItem.getSqlList(),taskLastRunTime,result);
            result.setEndTime(new Date());
            /** 计时间 */
            TaskManager.updateTaskLastRunTime(taskName,result.getStartTime());
        } catch (Exception e) {
            result.failed(e.getMessage());
            Application.CpolymerizationTaskLog.error(result.toString());
        } finally {
            /** 清任务 */
            TaskManager.removeTaskTable(taskName);
        }

    }

    /**
     * 执行任务
     * @param sqlList
     * @param lastStartDate
     * @param result
     * @throws Exception
     */
    private void disposeSql(List<String> sqlList,Date lastStartDate,TaskStatus result) throws Exception{
        if (null == sqlList || sqlList.isEmpty()) {
            throw new MyNullPointerException("sqlList is empty");
        }
        if (null == lastStartDate) {
            throw new MyNullPointerException("lastStartDate is empty");
        }
        GreenPlumSink.executeSql(sqlList,lastStartDate,new Date(),result);
    }

}
