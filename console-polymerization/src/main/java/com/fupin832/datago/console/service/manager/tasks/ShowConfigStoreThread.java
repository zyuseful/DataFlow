package com.fupin832.datago.console.service.manager.tasks;

import com.fupin832.datago.console.Application;
import com.fupin832.datago.console.pojos.StoreCenter;
import com.fupin832.datago.console.service.manager.threads.BaseTimingThread;

/**
 * 定时显示 配置文件项
 *
 * @author zy
 * @date 2021/02/04
 */
public class ShowConfigStoreThread extends BaseTimingThread {
    public ShowConfigStoreThread(){}
    public ShowConfigStoreThread(int runTimes, int milliseconds) {
        super(runTimes,milliseconds);
    }

    @Override
    public void doMethod() {
        Application.CpolymerizationTaskLog.info("================================================");
        StoreCenter.printPlanStore();
        Application.CpolymerizationTaskLog.info("================================================");
    }
}
