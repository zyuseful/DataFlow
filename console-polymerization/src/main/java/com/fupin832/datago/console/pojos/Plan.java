package com.fupin832.datago.console.pojos;

import java.util.List;

/**
 * 执行计划对象 对应 plan配置文件
 *
 * @author zy
 * @date 2021/02/03
 */
public class Plan {
    /** 执行时间间隔数(Minute) */
    private Integer period;
    private List<PlanItem> setups;

    public Plan() {}

    public Plan(Integer period, List<PlanItem> setups) {
        this.period = period;
        this.setups = setups;
    }

    /** Getter And Setter */
    public Integer getPeriod() {
        return period;
    }

    public List<PlanItem> getSetups() {
        return setups;
    }

    public Plan setPeriod(Integer period) {
        this.period = period;
        return this;
    }

    public Plan setSetups(List<PlanItem> setups) {
        this.setups = setups;
        return this;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "period=" + period +
                ", setups=" + setups +
                '}';
    }
}
