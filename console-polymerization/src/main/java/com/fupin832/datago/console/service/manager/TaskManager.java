package com.fupin832.datago.console.service.manager;

import com.fupin832.datago.cexception.basic.MyNullPointerException;
import com.fupin832.datago.console.Application;
import com.fupin832.datago.console.pojos.TaskStatus;
import com.fupin832.datago.console.service.manager.sqlstr.SqlDispose;
import com.fupin832.datago.console.service.manager.tasks.CpolymerizationTask;
import com.fupin832.datago.console.service.sink.GreenPlumSink;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务管理
 *
 * @author zy
 * @date 2021/02/03
 */
public class TaskManager {
    private static final Map<String, Date> lastTaskEndTime = new ConcurrentHashMap<>();
    private static final Map<String, TaskStatus> taskTable = new HashMap<>();
    /** 新增 taskTable 使用 */
    private static Lock lock = new ReentrantLock();

    public static final LinkedBlockingDeque taskQueue = new LinkedBlockingDeque();
    private static ThreadPoolExecutor threadPoolExecutor = null;
    public static volatile Integer count = 0;


    public static void init(int coreTaskNum, int maxTaskNum, long keepAliveTime) {
        if (null == threadPoolExecutor) {
            synchronized (TaskManager.class) {
                if (null == threadPoolExecutor) {
                    threadPoolExecutor = new ThreadPoolExecutor(coreTaskNum, maxTaskNum, keepAliveTime, TimeUnit.SECONDS, taskQueue);
                }
            }
        }
    }

    /**
     * 执行任务
     * @param cpolymerizationTask
     */
    public static void executeTask(CpolymerizationTask cpolymerizationTask) {
        threadPoolExecutor.execute(cpolymerizationTask);
    }

    /**
     * 获取新的任务id
     * @return
     */
    public static String taskId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 添加任务状态
     * 返回值:
     *      true 可以执行;
     *      false 该任务已存在,不执行;
     * @param key
     * @param status
     * @return
     */
    public static boolean addTaskTable(String key,TaskStatus status) {
        TaskStatus taskStatus = taskTable.get(key);
        boolean result = false;
        /** 已存在任务，无需执行 */
        if (null == taskStatus) {
            /** 不存在任务 再次判断 */
            lock.lock();
            try {
                taskStatus = taskTable.get(key);
                if (null == taskStatus) {
                    taskTable.put(key,status);
                    result = true;
                }
            } finally {
                lock.unlock();
            }
        }
        return result;
    }

    public static TaskStatus removeTaskTable(String taskName) {
        if (StringUtils.isEmpty(taskName)) {
            throw new MyNullPointerException("taskName is empty");
        }
        lock.lock();
        try {
            TaskStatus remove = taskTable.remove(taskName);
            return remove;
        } finally {
            lock.unlock();
        }
    }

    public static void updateTaskLastRunTime(String taskName,Date date) {
        lastTaskEndTime.put(taskName,date);
    }

    public static Date findTaskLastRunTime(String taskName) {
        return lastTaskEndTime.get(taskName);
    }

    public static void showLastTaskEndTime() {
        for (Map.Entry<String, Date> entry : lastTaskEndTime.entrySet()) {
            String key = entry.getKey();
            Date value = entry.getValue();
            Application.CpolymerizationTaskLog.info("TaskList: {} -> {}",key, SqlDispose.formateDate(value));
        }
    }
}
