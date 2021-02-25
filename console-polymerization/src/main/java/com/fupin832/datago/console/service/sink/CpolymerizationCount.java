package com.fupin832.datago.console.service.sink;

/**
 * dwd dws 聚合任务数
 *
 * @author zy
 * @date 2021/02/04
 */
public class CpolymerizationCount {
    private Integer dwd;
    private Integer dws;

    public CpolymerizationCount() {}

    public Integer getDwd() {
        return dwd;
    }

    public CpolymerizationCount setDwd(Integer dwd) {
        this.dwd = dwd;
        return this;
    }

    public Integer getDws() {
        return dws;
    }

    public CpolymerizationCount setDws(Integer dws) {
        this.dws = dws;
        return this;
    }
}
