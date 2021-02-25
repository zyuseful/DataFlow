package com.fupin832.datago.console.service.sink;

import com.fupin832.datago.console.Application;
import com.fupin832.datago.console.pojos.TaskStatus;
import com.fupin832.datago.console.service.manager.sqlstr.SqlDispose;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 读写 GreenPlum
 *
 * @author zy
 * @date 2021/02/04
 */
public class GreenPlumSink {
    private static GreenPlumConfig greenPlumConfig;
    private static CpolymerizationCount cpolymerizationCount;

    public static void init(GreenPlumConfig cgp, CpolymerizationCount cpc) {
        if (null == greenPlumConfig) {
            synchronized (GreenPlumSink.class) {
                if (null == greenPlumConfig) {
                    greenPlumConfig = cgp;
                }
                if (null == cpolymerizationCount) {
                    cpolymerizationCount = cpc;
                }
            }
        }
    }

    /**
     * 创建gp连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(greenPlumConfig.getDriver());
            con = DriverManager.getConnection(greenPlumConfig.getUrl(), greenPlumConfig.getUser(), greenPlumConfig.getPasswd());
        } catch (Exception e) {
            System.out.println("-----------postgre get connection has exception , msg = " + e.getMessage());
        }
        return con;
    }

    /**
     * 校验信号量
     *
     * @return 信号量状态（true | false）
     * @throws SQLException
     */
    public static boolean checkStatus(String tag) throws SQLException {
        boolean res = true;
        int statusSum = 0;
        int sum = 0;
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            String sql = "select state from job_status where tag=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, tag);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                sum = sum + Integer.parseInt(resultSet.getString(1));
            }
            System.out.println("state sum: " + sum);
            switch (tag) {
                case "dwd":
                    statusSum = cpolymerizationCount.getDwd();
                    break;
                case "dws":
                    statusSum = cpolymerizationCount.getDws();
                    break;
            }
            if (sum != statusSum) {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            //回收资源
            if (null != connection) {
                connection.close();
            }
            if (null != ps) {
                ps.close();
            }
        }
        return res;
    }

    public static void executeSql(List<String> sqlList, Date lastStartDate,Date now, TaskStatus result) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();

            for (int i = 0; i < sqlList.size(); i++) {
                String sql = sqlList.get(i);
                if (StringUtils.isEmpty(sql)) {
                    continue;
                }
                sql = sql.trim();
                String[] split = sql.split(";");
                if (split.length > 1) {
                    for (int j=0;j<split.length;j++) {
                        /** 记录 */
                        String doSql = SqlDispose.disposeSQL(split[j], lastStartDate, now);
                        Application.CpolymerizationTaskLog.info("SINK:{}",doSql);
                        result.addTaskInfo("do ").addTaskInfo(doSql).addTaskInfo(System.lineSeparator());
                        statement.addBatch(doSql);
                    }
                } else {
                    /** 记录 */
                    String doSql = SqlDispose.disposeSQL(sql, lastStartDate, now);
                    Application.CpolymerizationTaskLog.info("SINK:{}",doSql);
                    result.addTaskInfo("do ").addTaskInfo(doSql).addTaskInfo(System.lineSeparator());
                    statement.addBatch(doSql);
                }
            }
            statement.executeBatch();
        } catch (Exception e) {
            result.setFailedInfo(e.getMessage());
            Application.CpolymerizationTaskLog.error(e.getMessage());
            throw e;
        } finally {
            if (null != statement) {
                statement.close();
            }
            if (null != connection) {
                connection.close();
            }
        }

    }
}
