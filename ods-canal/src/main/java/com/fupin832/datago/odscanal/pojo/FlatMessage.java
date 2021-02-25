package com.fupin832.datago.odscanal.pojo;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * alibaba canal 接收数据封装bean
 *
 * @author zy
 * @date 2021/01/18
 */
public class FlatMessage implements Serializable {
    private long id;
    private String database;
    private String table;
    private List<String> pkNames;
    private Boolean isDdl;
    private String type;
    // binlog executeTime
    private Long es;
    // dml build timeStamp
    private Long ts;
    private String sql;
    private LinkedHashMap<String, Integer> sqlType;
    private LinkedHashMap<String, String> mysqlType;
    private List<LinkedHashMap<String, String>> data;
    private List<LinkedHashMap<String, String>> old;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getPkNames() {
        return pkNames;
    }

    public void setPkNames(List<String> pkNames) {
        this.pkNames = pkNames;
    }

    public Boolean getDdl() {
        return isDdl;
    }

    public void setDdl(Boolean ddl) {
        isDdl = ddl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getEs() {
        return es;
    }

    public void setEs(Long es) {
        this.es = es;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public LinkedHashMap<String, Integer> getSqlType() {
        return sqlType;
    }

    public void setSqlType(LinkedHashMap<String, Integer> sqlType) {
        this.sqlType = sqlType;
    }

    public LinkedHashMap<String, String> getMysqlType() {
        return mysqlType;
    }

    public void setMysqlType(LinkedHashMap<String, String> mysqlType) {
        this.mysqlType = mysqlType;
    }

    public List<LinkedHashMap<String, String>> getData() {
        return data;
    }

    public void setData(List<LinkedHashMap<String, String>> data) {
        this.data = data;
    }

    public List<LinkedHashMap<String, String>> getOld() {
        return old;
    }

    public void setOld(List<LinkedHashMap<String, String>> old) {
        this.old = old;
    }

    @Override
    public String toString() {
        return "FlatMessage{" +
                "id=" + id +
                ", database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", pkNames=" + pkNames +
                ", isDdl=" + isDdl +
                ", type='" + type + '\'' +
                ", es=" + es +
                ", ts=" + ts +
                ", sql='" + sql + '\'' +
                ", sqlType=" + sqlType +
                ", mysqlType=" + mysqlType +
                ", data=" + data +
                ", old=" + old +
                '}';
    }


    /**
     * 通过 当前FlatMessage key 获取 指定value的返回类型
     * @param fromTablekey
     * @param value
     * @return
     */
    public Object convertThisValue(String fromTablekey, String value) {
        LinkedHashMap<String, String> mysqlType = this.getMysqlType();
        LinkedHashMap<String, Integer> sqlType = this.getSqlType();
        return typeConvertStr(value, sqlType.get(fromTablekey), mysqlType.get(fromTablekey));
    }

    /**
     * 通过 FlatMessage 、key 获取 指定value的返回类型
     *
     * @param fmg
     * @param fromTablekey
     * @param value
     * @return
     */
    public static Object convertValue(FlatMessage fmg, String fromTablekey, String value) {
        LinkedHashMap<String, String> mysqlType = fmg.getMysqlType();
        LinkedHashMap<String, Integer> sqlType = fmg.getSqlType();
//        return typeConvert(value, sqlType.get(fromTablekey), mysqlType.get(fromTablekey));
        return typeConvertStr(value, sqlType.get(fromTablekey), mysqlType.get(fromTablekey));
    }

    /**
     * 数值类型转换，将string根据jdbc类型进行转换
     *
     * @param value
     * @param sqlType
     * @param mysqlType
     * @return
     */
    public static Object typeConvertStr(String value, int sqlType, String mysqlType) {
        StringBuilder tsb = new StringBuilder();
        try {
            Object res;
            switch (sqlType) {
                case Types.INTEGER:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }
                    res = Integer.parseInt(value);
                    break;
                case Types.CHAR:
                case Types.VARCHAR:
                    if (null == value) {
                        res = null;
                        break;
                    }
                    value = value.trim();
                    if (value.length() == 0) {
                        res = "''";
                        break;
                    }

                    tsb.setLength(0);
//                    tsb.append(value);
//                    tsb.append("'");

                    //转义
                    int i = value.indexOf("'");
                    value.getBytes();
                    StringBuilder ssb = new StringBuilder();
                    if (i >= 0) {
                        //postgresql E 转义
                        tsb.append("E");
                        char[] chars = value.toCharArray();
                        for (char c : chars) {
                            if (c == '\'') {
                                ssb.append("\\");
                                ssb.append(c);
                            } else {
                                ssb.append(c);
                            }
                        }
                        tsb.append("'");
                        tsb.append(ssb);
                    } else {
                        tsb.append("'");
                        tsb.append(value);
                    }

                    tsb.append("'");

                    res = tsb.toString();
                    break;
                /** MySQL5.7 text 类型出现 */
                case Types.CLOB:
                    if (StringUtils.isEmpty(value)) {
                        res = null;
                    } else {
                        tsb.setLength(0);
                        tsb.append("'");
                        tsb.append(value);
                        tsb.append("'");
                        res = tsb.toString();
                    }
                    break;
                case Types.SMALLINT:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    res = Short.parseShort(value);
                    break;
                case Types.BIT:
                case Types.TINYINT:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    res = Byte.parseByte(value);
                    break;
                case Types.BIGINT:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    if (mysqlType.startsWith("bigint") && mysqlType.endsWith("unsigned")) {
                        res = new BigInteger(value);
                    } else {
                        res = Long.parseLong(value);
                    }
                    break;
                // case Types.BIT:
                case Types.BOOLEAN:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    res = !"0".equals(value);
                    break;
                case Types.DOUBLE:
                case Types.FLOAT:
                    if (null == value || value.length() <= 0) {
                        return null;
                    }

                    res = Double.parseDouble(value);
                    break;
                case Types.REAL:
                    if (null == value || value.length() <= 0) {
                        return null;
                    }

                    res = Float.parseFloat(value);
                    break;
                case Types.DECIMAL:
                case Types.NUMERIC:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    res = new BigDecimal(value);
                    break;
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                case Types.BLOB:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    res = value.getBytes(StandardCharsets.ISO_8859_1);
                    break;
                case Types.DATE:
                    if (null == value || value.length() <= 0) {
                        res = null;
                        break;
                    }

                    if (!value.startsWith("0000-00-00")) {
                        Date date = parseDate(value);
                        if (date != null) {
//                            res = new Date(date.getTime());

                            tsb.setLength(0);
                            tsb.append("'");
                            res = new Date(date.getTime()).toString();
                            tsb.append(res);
                            tsb.append("'");
                            res = tsb.toString();
                        } else {
                            res = null;
                        }
                    } else {
                        res = null;
                    }
                    break;
                case Types.TIME: {
                    Date date = parseDate(value);
                    if (date != null) {
//                        res = new Time(date.getTime());

                        tsb.setLength(0);
                        tsb.append("'");
                        res = new Time(date.getTime()).toString();
                        tsb.append(res);
                        tsb.append("'");
                        res = tsb.toString();
                    } else {
                        res = null;
                    }
                    break;
                }
                case Types.TIMESTAMP:
                    if (!value.startsWith("0000-00-00")) {
                        Date date = parseDate(value);
                        if (date != null) {
//                            res = new Timestamp(date.getTime());
                            tsb.append("'");
                            res = new Timestamp(date.getTime()).toString();
                            tsb.append(res);
                            tsb.append("'");
                            res = tsb.toString();
                        } else {
                            res = null;
                        }
                    } else {
                        res = null;
                    }
                    break;
                default:
                    res = value;
                    break;
            }
            return res;
        } catch (Exception e) {
            return value;
        }
    }

    private static Date parseDate(String datetimeStr) {
        if (StringUtils.isEmpty(datetimeStr)) {
            return null;
        }
        datetimeStr = datetimeStr.trim();
        if (datetimeStr.contains("-")) {
            if (datetimeStr.contains(":")) {
                datetimeStr = datetimeStr.replace(" ", "T");
            }
        } else if (datetimeStr.contains(":")) {
            datetimeStr = "T" + datetimeStr;
        }

        DateTime dateTime = new DateTime(datetimeStr);

        return dateTime.toDate();
    }
}
