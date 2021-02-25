package com.fupin832.datago.comconfig.sserver.nacos;

import com.alibaba.nacos.api.config.listener.Listener;

import java.util.concurrent.Executor;

/**
 * Nacos Lister
 *
 * @author zy
 * @date 2021/01/29
 */
public class NacosLister implements Listener {
    protected String name;

    private NacosLister setName(String name) {
        this.name = name;
        return this;
    }

    public static NacosLister getInstance(String name) {
        NacosLister nacosLister = new NacosLister().setName(name);
        return new NacosLister();
    }
    
    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {

    }
}
