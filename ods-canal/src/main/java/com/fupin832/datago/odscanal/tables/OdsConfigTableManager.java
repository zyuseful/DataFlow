package com.fupin832.datago.odscanal.tables;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * canal ods 配置表管理
 *
 * @author zy
 * @date 2021/01/26
 */
public class OdsConfigTableManager implements Serializable {
    private List<OdsConfigTable> odsConfigTableArr;

    public OdsConfigTableManager() {}

    public OdsConfigTableManager(List<OdsConfigTable> odsConfigTableArr) {
        this.odsConfigTableArr = odsConfigTableArr;
    }

    /**
     * 根据 source table name 获取 SqlTable
     * @param sourceTableName
     * @return
     */
    public OdsConfigTable findBySourceTableName(String sourceTableName) {
        if (StringUtils.isEmpty(sourceTableName)) {
            throw new  NullPointerException("sourceTableName is empty");
        }
        if (null == odsConfigTableArr || odsConfigTableArr.isEmpty()) {
            throw new  NullPointerException("odsConfigTable is empty");
        }

        OdsConfigTable result = null;
        for (int i = 0; i< odsConfigTableArr.size(); i++) {
            OdsConfigTable sqlTable = this.odsConfigTableArr.get(i);
            if (null != sqlTable) {
                String stn = sqlTable.getSourceTableName();
                if (null != stn && stn.length() >0 ) {
                    if (stn.equals(sourceTableName)) {
                        result = sqlTable;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 初始化 + 检查 : 如果存在检查不合规则抛出 RuntimeException
     */
    public OdsConfigTableManager create(String configPrpoertiess) {
        odsConfigTableArr = JSON.parseArray(configPrpoertiess, OdsConfigTable.class);

        boolean result = false;
        if (StringUtils.isEmpty(configPrpoertiess)) {
            throw new RuntimeException("无配置数据");
        } else {
            OdsConfigTable odsConfigTable = null;
            for (int i=0;i<odsConfigTableArr.size();i++) {
                odsConfigTable = odsConfigTableArr.get(i);
                String sourceTableName = odsConfigTable.getSourceTableName();
                //主键检查
                List<String> sourcePKs = odsConfigTable.findSourcePKs();
                if (null == sourcePKs || sourcePKs.isEmpty()) {
                    throw new RuntimeException(sourceTableName+"需要填写主键");
                }
            }
        }
        return this;
    }
}
