package com.fupin832.datago.console.service.manager.tasks;

import com.fupin832.datago.console.Application;
import com.fupin832.datago.console.service.manager.TaskManager;
import com.fupin832.datago.console.service.manager.threads.BaseTimingThread;

/**
 * 查看线程最后运行时间
 *
 * @author zy
 * @date 2021/02/04
 */
public class ShowLastTaskEndTime extends BaseTimingThread {
    public ShowLastTaskEndTime(){}
    public ShowLastTaskEndTime(int runTimes, int milliseconds) {
        super(runTimes,milliseconds);
    }

    @Override
    public void doMethod() {
        Application.CpolymerizationTaskLog.info("================================================");
        TaskManager.showLastTaskEndTime();
        Application.CpolymerizationTaskLog.info("================================================");
    }
}
