package com.fupin832.datago.console.service.manager.threads;

/**
 * 定时 Thread 父类
 * 将定时轮行部分进行提取，子类自行实现执行方法即可
 * @author zy
 * @date 2021/02/04
 */
public abstract class BaseTimingThread extends Thread{
    /** 内部调用 */
    protected boolean flag = false;
    protected boolean down = false;


    /** 构造属性 */
    /** 0:不运行 <0:永久运行 >0:运行有限次数 */
    protected int runTime = 0;
    /** 定时毫秒 */
    protected long sleepMilliseconds = 5000;

    public BaseTimingThread() {}

    public BaseTimingThread(int runTime, long sleepMilliseconds) {
        this.runTime = runTime;
        if (sleepMilliseconds > 1000) {
            this.sleepMilliseconds = sleepMilliseconds;
        }
    }

    /** Setter */
    public BaseTimingThread setRunTime(int runTime) {
        this.runTime = runTime;
        return this;
    }
    public BaseTimingThread setSleepMilliseconds(long sleepMilliseconds) {
        this.sleepMilliseconds = sleepMilliseconds;
        return this;
    }

    @Override
    public void run() {
        if (runTime < 0) {
            flag = true;
        } else if (runTime == 0) {
            flag = false;
        } else {
            down = true;
        }
        while (flag) {
            if (down) {
                runTime--;
                if (runTime == 0) {
                    flag = false;
                }
            }

            try {
                Thread.sleep(sleepMilliseconds);
                doMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自行实现定时工作
     */
    public abstract void doMethod();
}
