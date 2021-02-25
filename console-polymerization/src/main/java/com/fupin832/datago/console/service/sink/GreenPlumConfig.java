package com.fupin832.datago.console.service.sink;

/**
 * GP config
 *
 * @author zy
 * @date 2021/02/04
 */
public class GreenPlumConfig {
    private String driver;
    private String url;
    private String user;
    private String passwd;
    private Integer maxPool;

    public GreenPlumConfig() {}

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPasswd() {
        return passwd;
    }

    public Integer getMaxPool() {
        return maxPool;
    }

    public GreenPlumConfig setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public GreenPlumConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public GreenPlumConfig setUser(String user) {
        this.user = user;
        return this;
    }

    public GreenPlumConfig setPasswd(String passwd) {
        this.passwd = passwd;
        return this;
    }

    public GreenPlumConfig setMaxPool(Integer maxPool) {
        this.maxPool = maxPool;
        return this;
    }

    @Override
    public String toString() {
        return "GreenPlum{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", passwd='" + passwd + '\'' +
                ", maxPool=" + maxPool +
                '}';
    }
}
