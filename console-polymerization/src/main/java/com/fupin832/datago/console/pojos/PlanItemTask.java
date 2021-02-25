package com.fupin832.datago.console.pojos;

import java.util.List;

/**
 * PlanItem 对应的执行Task 类
 *
 * @author zy
 * @date 2021/02/03
 */
public class PlanItemTask{
    private String taskName;
    private List<String> sqlList;

    public PlanItemTask() {}

    /** Getters */
    public String getTaskName() {
        return taskName;
    }
    public List<String> getSqlList() {
        return sqlList;
    }

    /** Setters */
    public PlanItemTask setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public PlanItemTask setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
        return this;
    }

    public void addSqlStr(String sql) {
        this.sqlList.add(sql);
    }

    @Override
    public String toString() {
        return "PlanItemTask{" +
                "taskName='" + taskName + '\'' +"\n"+
                ", sqlList=" + sqlList +
                '}';
    }
}

