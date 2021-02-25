package com.fupin832.datago.console.pojos;


import com.fupin832.datago.console.service.manager.sqlstr.SqlDispose;
import com.fupin832.datago.console.service.sink.GreenPlumSink;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 任务状态记录
 *
 * @author zy
 * @date 2021/02/03
 */
public class TaskStatus {
    /** 任务名 */
    private String name;
    /** 是否成功 */
    private boolean isSuccess;
    /** 失败原因 */
    private String failedInfo;
    /** start时间 */
    private Date startTime;
    /** end时间 */
    private Date endTime;
    /** 存储任务执行信息 */
    private StringBuilder taskInfo = new StringBuilder();

    public TaskStatus() {}

    public TaskStatus(String name, boolean isSuccess, String failedInfo, Date startTime, Date endTime) {
        this.name = name;
        this.isSuccess = isSuccess;
        this.failedInfo = failedInfo;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /** Getter */
    public boolean isSuccess() {
        return isSuccess;
    }

    public String getFailedInfo() {
        return failedInfo;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getName() {
        return name;
    }

    public StringBuilder getTaskInfo() {
        return taskInfo;
    }

    /** Setter */
    public TaskStatus setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public TaskStatus setFailedInfo(String filerInfo) {
        this.failedInfo = filerInfo;
        this.setEndTime(new Date());
        return this;
    }

    public TaskStatus setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public TaskStatus setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public TaskStatus setName(String name) {
        this.name = name;
        return this;
    }

    /** common methods */
    public TaskStatus success() {
        this.setSuccess(true);
        return this;
    }

    public TaskStatus failed(String info) {
        this.setSuccess(false);
        this.setFailedInfo(info);
        return this;
    }

    /** 添加执行任务过程信息 */
    public TaskStatus addTaskInfo(String str) {
        this.taskInfo.append(str);
        return this;
    }

    public String getStartTimeStr() {
        return SqlDispose.formateDate(getStartTime());
    }

    public String getEndTimeStr() {
        return SqlDispose.formateDate(getEndTime());
    }

    @Override
    public String toString() {
        return "TaskStatus{" +
                "isSuccess=" + isSuccess +
                ", failedInfo='" + failedInfo + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
