package com.fupin832.datago.comconfig.pconfigs;

import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItem;
import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItemTemplate;
import com.fupin832.datago.comconfig.pconfigs.basic.ItemDataType;

/**
 * Ods Table Config 参数配置类
 *
 * @author zy
 * @date 2021/01/29
 */
public class OdsTableConfigTemplate extends ConfigItemTemplate {
    public final static String P_ODS_GROUP = "ods_tab";
    public final static String P_ODS_JSON = "ods_json";


    @Override
    public ConfigItemTemplate getInstance(String configItemArrName) {
        this.configItemTemplateName = configItemArrName;
        init();
        return this;
    }

    private void init() {
        ConfigItem ods = new ConfigItem();
        ods.setItemName(P_ODS_JSON)
                .setGroupName(P_ODS_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);
        this.addConfigItem(ods);
    }
}
