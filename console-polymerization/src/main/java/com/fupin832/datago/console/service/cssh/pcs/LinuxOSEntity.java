package com.fupin832.datago.console.service.cssh.pcs;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Linux 主机
 * @author zy
 * @date 2021/02/02
 */
public class LinuxOSEntity {
    protected String name;
    /** 主机（IP） */
    protected String host;
    /** 连接端口 */
    protected  int port;
    /** 编码 */
    protected Charset charset = StandardCharsets.UTF_8;
    /** 用户 */
    protected String user;
    /** 密码 */
    protected String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
