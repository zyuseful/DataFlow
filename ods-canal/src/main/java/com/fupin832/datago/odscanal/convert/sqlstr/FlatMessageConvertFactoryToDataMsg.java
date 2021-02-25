package com.fupin832.datago.odscanal.convert.sqlstr;

import com.alibaba.fastjson.JSON;
import com.fupin832.datago.odscanal.convert.basic.ConvertFactory;
import com.fupin832.datago.odscanal.logs.MyLOG;
import com.fupin832.datago.odscanal.pojo.FlatMessage;
import com.fupin832.datago.odscanal.service.ConfigReceiveAndDispose;
import com.fupin832.datago.odscanal.tables.OdsConfigTable;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;
import org.apache.flink.api.java.tuple.Tuple20;
import org.apache.flink.table.api.TableColumn;
import org.apache.flink.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * FlatMessage 转换为 Sql String
 *
 * @author zy
 * @date 2021/01/25
 */
public class FlatMessageConvertFactoryToDataMsg implements ConvertFactory<String,SqlResult> {
//    private Logger LOG = LoggerFactory.getLogger(FlatMessageConvertFactoryToDataMsg.class);
    private Logger LOG = MyLOG.getLogger(FlatMessageConvertFactoryToDataMsg.class);

    private OdsConfigTableManager odsConfigTableManager;
    private Dml flatMessageDML;
    private Ddl flatMessageDDL;

    /**
     * 静态方法，创建 FlatMessageConvertFactoryToDataMsg 对象
     * @return
     */
    public static FlatMessageConvertFactoryToDataMsg createInstance() {
        return new FlatMessageConvertFactoryToDataMsg();
    }

    /** 结合 OdsConfigTableManager */
    public FlatMessageConvertFactoryToDataMsg setOdsConfigTableManager(OdsConfigTableManager odsConfigTableManager) {
        this.odsConfigTableManager = odsConfigTableManager;
        return this;
    }
    /** 结合 DML */
    public FlatMessageConvertFactoryToDataMsg setDML(Dml flatMessageDML) {
        this.flatMessageDML = flatMessageDML;
        return this;
    }
    /** 结合 DDL */
    public FlatMessageConvertFactoryToDataMsg setDDL(Ddl flatMessageDDL) {
        this.flatMessageDDL = flatMessageDDL;
        return this;
    }

    /** Getter */
    public Dml getFlatMessageDML() {
        return flatMessageDML;
    }

    public Ddl getFlatMessageDDL() {
        return flatMessageDDL;
    }

    /**
     * 核心转换业务
     * @param jsonStr
     * @return
     * @throws Exception
     */
    @Override
    public SqlResult convertTo(String jsonStr) throws Exception {
        FlatMessage msg = JSON.parseObject(jsonStr,FlatMessage.class);
        Boolean ddl = msg.getDdl();
        OdsConfigTableManager odsConfigTableManagerTemp = null;
        if (null == this.odsConfigTableManager) {
            odsConfigTableManagerTemp = ConfigReceiveAndDispose.findOdsConfigTableManager();
        } else {
            odsConfigTableManagerTemp = this.odsConfigTableManager;
        }

        /** if ddl / else dml */
        if (ddl) {
            return doDdl(msg,odsConfigTableManagerTemp);
        } else {
            return doDml(msg,odsConfigTableManagerTemp);
        }
    }

    private SqlResult doDdl(FlatMessage msg, OdsConfigTableManager odsConfigTableManager) {
        SqlResult result = new SqlResult();
        result.setSourceDBName(msg.getDatabase()).setSourceTableName(msg.getTable());
        switch (msg.getType()) {
            // alter
            case SQLOptions.ALTER: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.ALTER));
                try {
                    String sql = this.getFlatMessageDDL().alterSql(msg, odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            // drop
            case SQLOptions.DROP: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.DROP));
                try {
                    String sql = this.getFlatMessageDDL().dropTableSql(msg, odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            // truncate
            case SQLOptions.TRUNCATE: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.TRUNCATE));
                try {
                    String sql = this.getFlatMessageDDL().truncateTableSql(msg,odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            // create
            case SQLOptions.CREATE: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.CREATE));
                try {
                    String sql = this.getFlatMessageDDL().createSql(msg, odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            default: {
                result.setError(true).setErrInfo("未知类型,不支持处理 FlatMessage"+msg);
                break;
            }
        }
        return result;
    }

    private SqlResult doDml(FlatMessage msg, OdsConfigTableManager odsConfigTableManager) {
        SqlResult result = new SqlResult();
        result.setSourceDBName(msg.getDatabase()).setSourceTableName(msg.getTable());
        switch (msg.getType()) {
            //insert
            case SQLOptions.INSERT: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.INSERT));
                try {
                    String sql = this.getFlatMessageDML().insertSql(msg, odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            //update
            case SQLOptions.UPDATE: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.UPDATE));
                try {
                    String sql = this.getFlatMessageDML().updateSql(msg,odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            // delete
            case SQLOptions.DELETE: {
                result.setSqlOption(SQLOptions.findByName(SQLOptions.DELETE));
                try {
                    String sql = this.getFlatMessageDML().deleteSql(msg,odsConfigTableManager);
                    result.setSql(sql);
                } catch (Exception e) {
                    result.setError(true).setErrInfo(e.getMessage());
                }
                break;
            }
            default: {
                result.setError(true).setErrInfo("未知类型,不支持处理 FlatMessage"+msg);
                break;
            }
        }
        return result;
    }
}
