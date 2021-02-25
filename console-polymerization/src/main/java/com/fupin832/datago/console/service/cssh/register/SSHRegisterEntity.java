//package com.fupin832.datago.console.service.cssh.register;
//
//import ch.ethz.ssh2.Connection;
//import ch.ethz.ssh2.Session;
//import com.fupin832.datago.console.service.cssh.pcs.LinuxOSEntity;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//
///**
// * Linux ssh 注册连接工具类
// *
// * @author zy
// * @date 2021/02/02
// */
//public class SSHRegisterEntity {
//    /* 连接器 */
//    private Connection connect;
//    /* 主机（IP） */
//    private String host;
//    /* 连接端口 */
//    private  int port;
//    /* 编码 */
//    private Charset charset;
//    /* 用户 */
//    private String user;
//    /* 密码 */
//    private String password;
//
//    public SSHRegisterEntity(LinuxOSEntity centOSEntity) {
//        this.host = centOSEntity.getHost();
//        this.port=centOSEntity.getPort();
//        this.user = centOSEntity.getUser();
//        this.charset = centOSEntity.getCharset();
//        this.password = centOSEntity.getPassword();
//    }
//
//    /**
//     * 登录Centos主机
//     */
//    private boolean login() {
//        connect = new Connection(host,port);
//        try {
//            connect.connect();
//            return connect.authenticateWithPassword(user, password);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 获取返回信息
//     */
//    public StringBuilder processStdout(InputStream in) {
//        byte[] buf = new byte[1024];
//        StringBuilder builder = new StringBuilder();
//        try {
//            int length;
//            while ((length = in.read(buf)) != -1) {
//                builder.append(new String(buf, 0, length));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return builder;
//    }
//
//    /**
//     * exec shell命令
//     */
//    public StringBuilder exec(String shell) throws IOException {
//        InputStream inputStream = null;
//        StringBuilder result = new StringBuilder();
//        try {
//            // 认证登录信息
//            if (this.login()) {
//                // 登陆成功则打开一个会话
//                Session session = connect.openSession();
//                session.execCommand(shell);
//                inputStream = session.getStdout();
//                result = this.processStdout(inputStream);
//                connect.close();
//            }
//        } finally {
//            if (null != inputStream) {
//                inputStream.close();
//            }
//        }
//        return result;
//    }
//}
