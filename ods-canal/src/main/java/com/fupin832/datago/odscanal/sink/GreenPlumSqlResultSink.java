package com.fupin832.datago.odscanal.sink;

import com.fupin832.datago.odscanal.convert.sqlstr.SqlResult;
import com.fupin832.datago.odscanal.logs.MyLOG;
import com.fupin832.datago.odscanal.service.ConfigReceiveAndDispose;
import com.fupin832.datago.odscanal.tables.OdsConfigTable;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.jdbc.internal.connection.JdbcConnectionProvider;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;

/**
 * GreenPlum sink
 *
 * @author zy
 * @date 2021/01/18
 */
//public class GreenPlumSqlResultSink extends RichSinkFunction<SqlResult> implements CheckpointedFunction {
public class GreenPlumSqlResultSink extends RichSinkFunction<SqlResult> {
//    private Logger LOG = LoggerFactory.getLogger(GreenPlumSqlResultSink.class);
    private Logger LOG = MyLOG.getLogger(GreenPlumSqlResultSink.class);
    /** db connection */
    private JdbcConnectionProvider jdbcConnectionProvider;
    private Connection connection;
    /** OdsConfigTableManager */
    private OdsConfigTableManager odsConfigTableManager;

    public static GreenPlumSqlResultSink getInstance() {
        return new GreenPlumSqlResultSink();
    }

    public GreenPlumSqlResultSink addJdbcConnectionProvider(JdbcConnectionProvider jdbcConnectionProvider) {
        this.jdbcConnectionProvider = jdbcConnectionProvider;
        return this;
    }
    public GreenPlumSqlResultSink addOdsConfigTableManager(OdsConfigTableManager odsConfigTableManager) {
        this.odsConfigTableManager = odsConfigTableManager;
        return this;
    }

    // ---------------------------RichSinkFunction---------------------------
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = jdbcConnectionProvider.getConnection();
    }


    @Override
    public void close() throws Exception {
        super.close();
        try {
            if (null != connection) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection = null;
        }
    }

    @Override
    public void invoke(SqlResult value, Context context) throws Exception {
        OdsConfigTable bySourceTableName = null;
        if (null == this.odsConfigTableManager) {
            bySourceTableName = ConfigReceiveAndDispose.findOdsConfigTableManager().findBySourceTableName(value.getSourceTableName());
        } else {
            bySourceTableName = odsConfigTableManager.findBySourceTableName(value.getSourceTableName());
        }

        if (null == bySourceTableName) {
            LOG.info("GP_SINK bySourceTableName is empty! Value="+value+"---> return");
            return;
        }

        try {
            if (null == connection) {
                connection = jdbcConnectionProvider.getConnection();
            }
            //关键 设置为手动提交
            //connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            String[] sinkDdl = bySourceTableName.getSinkDdl();

            boolean ddl = value.getSqlOption().isDDL();
            String sql = value.getSql();
            if (ddl) {
                return;
            } else {
                boolean sqlIsEmpty = StringUtils.isEmpty(sql);
                //空数据不做判断
                if (sqlIsEmpty) {
                    LOG.info("GP_SINK sql is empty! Value="+value+"---> return");
                    return;
                }
                LOG.info(sql);
                st.execute(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // ---------------------------CheckpointedFunction---------------------------
}
