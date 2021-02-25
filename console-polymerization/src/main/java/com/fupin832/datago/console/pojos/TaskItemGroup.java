package com.fupin832.datago.console.pojos;

import java.util.List;

/**
 * TaskItem 集合
 *
 * @author zy
 * @date 2021/02/03
 */
public class TaskItemGroup {
    private int minutes;
    private List<PlanItem> planItemList;

    public TaskItemGroup() {}

    public TaskItemGroup(int minutes, List<PlanItem> planItemList) {
        this.minutes = minutes;
        this.planItemList = planItemList;
    }

    /** Getter And Setter */
    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public List<PlanItem> getPlanItemList() {
        return planItemList;
    }

    public void setPlanItemList(List<PlanItem> planItemList) {
        this.planItemList = planItemList;
    }

    public PlanItem findByTaskName(String taskName) {
        if (null == this.planItemList || this.planItemList.isEmpty()) {
            return null;
        }
        PlanItem result = null;
        for(int i = 0; i<this.planItemList.size(); i++) {
            PlanItem planItem = this.planItemList.get(i);
            if (null == planItem && planItem.getName() != null && planItem.getName().equals(taskName)) {
                result = planItem;
                break;
            }
        }
        return result;
    }
}
