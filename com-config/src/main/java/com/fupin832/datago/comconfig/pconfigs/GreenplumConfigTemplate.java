package com.fupin832.datago.comconfig.pconfigs;

import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItem;
import com.fupin832.datago.comconfig.pconfigs.basic.ConfigItemTemplate;
import com.fupin832.datago.comconfig.pconfigs.basic.ItemDataType;

/**
 * greenplum 参数配置类
 *
 * @author zy
 * @date 2021/01/29
 */
public class GreenplumConfigTemplate extends ConfigItemTemplate {
    public final static String P_DB_GROUP = "greenplum";
    /** 加载驱动程序 */
    public final static String P_DB_DRIVER = "gp_driver";
    /** 数据库连接 */
    public final static String P_DB_URL = "gp_url";
    /** 用户名 */
    public final static String P_DB_USER = "gp_user";
    /** 密码 */
    public final static String P_DB_PWD = "gp_passwd";

    @Override
    public ConfigItemTemplate getInstance(String configItemArrName) {
        this.configItemTemplateName = configItemArrName;
        init();
        return this;
    }

    private void init() {
        /*DB*/
        ConfigItem db_driver = new ConfigItem();
        db_driver.setItemName(P_DB_DRIVER)
                .setGroupName(P_DB_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);

        ConfigItem db_url = new ConfigItem();
        db_url.setItemName(P_DB_URL)
                .setGroupName(P_DB_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);

        ConfigItem db_user = new ConfigItem();
        db_user.setItemName(P_DB_USER)
                .setGroupName(P_DB_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);

        ConfigItem db_password = new ConfigItem();
        db_password.setItemName(P_DB_PWD)
                .setGroupName(P_DB_GROUP)
                .setItemDataType(ItemDataType.STRING)
                .setRequired(true);
        this.addConfigItem(db_driver,db_url,db_user,db_password);
    }
}
