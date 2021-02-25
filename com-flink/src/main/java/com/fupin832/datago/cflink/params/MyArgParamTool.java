package com.fupin832.datago.cflink.params;

import com.fupin832.datago.cexception.basic.MyNullPointerException;
import com.fupin832.datago.cexception.basic.MyRunTimeException;
import com.fupin832.datago.comconfig.manager.ConfigTemplateManager;
import org.apache.flink.api.java.utils.AbstractParameterTool;
import org.apache.flink.api.java.utils.ParameterTool;

import java.util.Properties;

/**
 * flink arg reading tool
 *
 * @author zy
 * @date 2021/02/02
 */
public class MyArgParamTool {
    /** base flag
     * using like --configway=nacoscs
     */
    public static final String CONFIG_WAY = ConfigTemplateManager.CONFIG_WAY;

//    private MyArgParamTool(){}
//    public MyArgParamTool(String[] args) {
//        loadConfig(args);
//    }

    /**
     * 获取args 配置参数转换为Properties 并进行存储
     * @param args
     * @throws MyRunTimeException
     */
    public static synchronized void loadConfig(String[] args) throws MyRunTimeException {
        if (null == args) {
            throw new MyNullPointerException("Application.args is null")
                    .setStopTheProgramFlag(true);
        }

        AbstractParameterTool abstractParameterTool = null;
        ParameterTool parameters = ParameterTool.fromArgs(args);
        Properties properties = parameters.getProperties();

        if (null == properties || properties.isEmpty()) {
            throw new MyNullPointerException("properties is empty")
                    .setStopTheProgramFlag(true);
        }
        //很关键
        ConfigTemplateManager.setParamProperties(properties);
    }
}
