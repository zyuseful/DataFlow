package com.fupin832.datago.odscanal.convert.sqlstr;

import com.fupin832.datago.odscanal.pojo.FlatMessage;
import com.fupin832.datago.odscanal.tables.OdsConfigColumn;
import com.fupin832.datago.odscanal.tables.OdsConfigTable;
import com.fupin832.datago.odscanal.tables.OdsConfigTableManager;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FlatMessage Dml 转sql
 *
 * @author zy
 * @date 2021/01/27
 */
public class FlatMessageDML implements Dml {
    StringBuffer sb = new StringBuffer();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String insertSql(FlatMessage fmg, OdsConfigTableManager oct) {
        sb.setLength(0);
        //记录 insert sql : value ...
        StringBuffer insertSqlSB = new StringBuffer();
        //返回值
        StringBuilder resultStringBuilder = new StringBuilder();
        OdsConfigTable bySourceTableName = oct.findBySourceTableName(fmg.getTable());

        List<LinkedHashMap<String, String>> flatMessageDatas = fmg.getData();

        //1.获取所有 needInsert=true && usingMySelfValue=false 的列集合
        List<OdsConfigColumn> needInsertAndNoMySelfValueColumns = bySourceTableName.findNeedInsertAndNoMySelfValueColumns();
        //2.获取所有 needInsert=true && usingMySelfValue=true 的列集合
        List<OdsConfigColumn> needInsertAndMySelfValueColumns = bySourceTableName.findNeedInsertAndMySelfValueColumns();
        //3.数据字段拼接 <==
        int upSql = 0;
        int finalIndex = 0; //用于查询data角标
        for (int i = 0; i < flatMessageDatas.size(); i++) {
            upSql = 0;
            //==>构建一个 insert 语句
            LinkedHashMap<String, String> insertKV = flatMessageDatas.get(i);
            sb.setLength(0);
            insertSqlSB.setLength(0);
            //拼接sql
            sb.append("insert into ");
            sb.append(bySourceTableName.getSinkDBAndTableNameIfNull());
            sb.append(" ");
            sb.append("(");
            insertSqlSB.append(" values (");

            //=========FlatMessage更新值拼接=========
            finalIndex = i;

            //4.数据值转换  <==
            for (Map.Entry<String, String> entry : insertKV.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Object tmpVal = fmg.convertThisValue(key, value);
                //限定范围内的insert 否则根据更新值修改为现有更新值
                if (null != needInsertAndNoMySelfValueColumns && !needInsertAndNoMySelfValueColumns.isEmpty()) {
                    for (OdsConfigColumn sqlColumn : needInsertAndNoMySelfValueColumns) {
                        if (sqlColumn.getSourceColumnName().equals(key)) {
                            ++upSql;
                            String columnName = sqlColumn.getSinkColumnNameIfNull();
                            sb.append(columnName);
                            sb.append(",");
                            if (null == tmpVal) {
                                insertSqlSB.append("null,");
                            } else {
                                insertSqlSB.append(tmpVal);
                                insertSqlSB.append(",");
                            }
                            break;
                        }
                    }
                } else {
                    ++upSql;
                    sb.append(key);
                    sb.append(",");
                    if (null == tmpVal) {
                        insertSqlSB.append("null,");
                    } else {
                        insertSqlSB.append(tmpVal);
                        insertSqlSB.append(",");
                    }
                }
            }
            //自有列处理
            if (null != needInsertAndMySelfValueColumns && !needInsertAndMySelfValueColumns.isEmpty()) {
                for (OdsConfigColumn mySqlColumn : needInsertAndMySelfValueColumns) {
                    String sinkColumn = mySqlColumn.getSinkColumnNameIfNull();
                    String sinkValue = mySqlColumn.getSinkValue();
                    Object sinkVal = convertSinkValue(fmg, finalIndex, sinkValue);
                    ++upSql;
                    sb.append(sinkColumn);
                    sb.append(",");
                    if (null == sinkVal) {
                        insertSqlSB.append("null,");
                    } else {
                        insertSqlSB.append(sinkVal);
                        insertSqlSB.append(",");
                    }
                }
            }

            // sql拼接是否有效
            if (upSql > 0) {
                deleteLastStrForStringBuffer(sb, ",");
                deleteLastStrForStringBuffer(insertSqlSB, ",");

                sb.append(")");
                insertSqlSB.append(")");
                sb.append(insertSqlSB.toString());
                sb.append(";");
                resultStringBuilder.append(sb.toString());
            } else {
                sb.setLength(0);
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public String updateSql(FlatMessage fmg, OdsConfigTableManager oct) {
        OdsConfigTable bySourceTableName = oct.findBySourceTableName(fmg.getTable());
        StringBuffer resultStringBuffer = new StringBuffer();
        //1.检查主键 <==
        List<String> pkNames = bySourceTableName.findSourcePKs();
        //2.数据拼接 <==
        List<LinkedHashMap<String, String>> flatMessageOldValues = fmg.getOld();
        //  2.1.数据表拼接 <==
        //自有更新列 -- 更新追加
        List<OdsConfigColumn> mySelfUpdateColumns = bySourceTableName.findNeedToUpdateAndMySelfValueColumns();
        //原有更新列 -- 跟随需要更新值更新
        List<OdsConfigColumn> srcUpdateColumns = bySourceTableName.findNeedToUpdateAndNoMySelfValueColumns();
        //  2.2.数据字段拼接 <==
        int upSql = 0;
        int finalIndex = 0; //用于查询data角标
        for (int i = 0; i < flatMessageOldValues.size(); i++) {
            upSql = 0;
            //==>构建一个 update 语句
            LinkedHashMap<String, String> changeKV = flatMessageOldValues.get(i);
            sb.setLength(0);
            //拼接sql
            sb.append("update ");
            sb.append(bySourceTableName.getSinkDBAndTableNameIfNull());
            sb.append(" set ");

            //=========FlatMessage更新值拼接=========
            finalIndex = i; //用于查询data角标

            //  2.3.数据值转换  <==
            for (Map.Entry<String, String> entry : changeKV.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String tempVal = fmg.getData().get(finalIndex).get(key);
//                Object tempVal = convertValue(fmg, key, value);
                //如果设定范围则使用范围值 否则根据更新值修改为现有更新值
                if (null != srcUpdateColumns && !srcUpdateColumns.isEmpty()) {
                    for (OdsConfigColumn sqlColumn : srcUpdateColumns) {
                        if (sqlColumn.getSourceColumnName().equals(key)) {
                            ++upSql;
                            String columnName = sqlColumn.getSinkColumnNameIfNull();
                            sb.append(columnName);
                            Object columnValue = fmg.convertThisValue(key, tempVal);
                            if (null == columnValue) {
                                sb.append("=null");
                            } else {
                                sb.append("=");
                                sb.append(columnValue);
                            }
                            sb.append(",");
                        }
                    }
                } else {
                    ++upSql;
                    sb.append(key);
                    Object columnValue = fmg.convertThisValue(key, tempVal);
                    if (null == columnValue) {
                        sb.append("=null");
                    } else {
                        sb.append("=");
                        sb.append(columnValue);
                    }
                    sb.append(",");
                }
            }
            //自有列处理
            if (null != mySelfUpdateColumns && !mySelfUpdateColumns.isEmpty()) {
                for (OdsConfigColumn mySqlColumn : mySelfUpdateColumns) {

                    String sinkColumn = mySqlColumn.getSinkColumnNameIfNull();
                    String sinkValue = mySqlColumn.getSinkValue();
                    Object sinkVal = convertSinkValue(fmg, finalIndex, sinkValue);
                    upSql++;
                    sb.append(sinkColumn);
                    if (null == sinkVal) {
                        sb.append("=null");
                    } else {
                        sb.append("=");
                        sb.append(sinkVal);
                    }
                    sb.append(",");
                }
            }

            // sql拼接是否有效
            if (upSql > 0) {
                deleteLastStrForStringBuffer(sb, ",");
                buildWhereSql(fmg, finalIndex, sb, bySourceTableName);
                sb.append(";");
                resultStringBuffer.append(sb);
            } else {
                sb.setLength(0);
            }
        }
        return resultStringBuffer.toString();
    }

    @Override
    public String deleteSql(FlatMessage fmg, OdsConfigTableManager oct) {
        //主键集合 ,如果FlatMessage 主键可用则使用FlayMessage 做主键，否则使用 -- 均为fromTable 字段，这里不做转换，待取值完毕再做转换
        OdsConfigTable bySourceTableName = oct.findBySourceTableName(fmg.getTable());
        List<String> pkNames = bySourceTableName.findSourcePKs();
        //循环更新值
        List<LinkedHashMap<String, String>> data = fmg.getData();

        StringBuilder result = new StringBuilder();
        int index;
        int sqlCount = 0;
        for (int i = 0; i < data.size(); i++) {
            LinkedHashMap<String, String> stringStringLinkedHashMap = data.get(i);
            index = i;
            if (null != stringStringLinkedHashMap && !stringStringLinkedHashMap.isEmpty()) {
                sb.setLength(0);

                sb.append("delete from ");
                sb.append(bySourceTableName.getSinkDBAndTableNameIfNull());
                buildWhereSql(fmg, index, sb, bySourceTableName);
                sb.append(";");
                result.append(sb.toString());
                sqlCount++;
            }
        }
        if (sqlCount > 0) {
            return result.toString();
        } else {
            throw new RuntimeException("build where error in deleteSql(...) FlatMessage:"+fmg);
        }
    }


    /**
     * 功能方法
     * sink字段转换
     * FlatMessage:支持 #{FMG.database},#{FMG.table},#{FMG.isDDL},#{FMG.type},#{FMG.es},#{FMG.ts},#{FMG.sql}
     * 支持 #{FDA.xxx} 方式获取 变更data中的值
     *
     * @return
     */
    public Object convertSinkValue(FlatMessage fmg, int dataIndex, String sinkValue) {
        String result = null;
        if (null == sinkValue || sinkValue.length() <= 0) {
            return result;
        } else {
            sinkValue = sinkValue.trim();
        }

        String regFMG = "#\\{FMG.[\\w]+\\}";//定义正则表达式
        String regFDA = "#\\{FDA.[\\w]+\\}";//定义正则表达式

        Pattern pattenForFMG = Pattern.compile(regFMG);//编译正则表达式
        Pattern pattenForFDA = Pattern.compile(regFDA);//编译正则表达式
        Matcher matcherForFMG = pattenForFMG.matcher(sinkValue);// 指定要匹配的字符串
        Matcher matcherForFDA = pattenForFDA.matcher(sinkValue);// 指定要匹配的字符串

        StringBuilder vb = new StringBuilder();

        if (matcherForFMG.find()) {
            for (int i = 0; i <= matcherForFMG.groupCount(); i++) {
                String groupVal = matcherForFMG.group(i);
                groupVal = groupVal.substring(6, groupVal.length() - 1);
                String findFMGValue = null;
                switch (groupVal) {
                    case "id":
                        findFMGValue = String.valueOf(fmg.getId());
                        break;
                    case "database":
                        findFMGValue = String.valueOf(fmg.getDatabase());
                        break;
                    case "table":
                        findFMGValue = String.valueOf(fmg.getTable());
                        break;
                    case "isDDL":
                        findFMGValue = String.valueOf(fmg.getDdl());
                        break;
                    case "type":
                        findFMGValue = String.valueOf(fmg.getType());
                        break;
                    case "es":
                        findFMGValue = sdf.format(new Date(fmg.getEs()));
                        break;
                    case "ts":
                        findFMGValue = String.valueOf(fmg.getTs());
                        break;
                    case "sql":
                        findFMGValue = String.valueOf(fmg.getSql());
                        break;
                    default:
                        break;
                }
                if (findFMGValue != null && sinkValue != null) {
//                    sinkValue = sinkValue.replace("#{FMG." + groupVal + "}", findFMGValue);
                    vb.setLength(0);
                    vb.append("'");vb.append(findFMGValue);vb.append("'");
                    sinkValue = sinkValue.replace("#{FMG." + groupVal + "}", vb.toString());
                }
            }
            result = sinkValue;
        }

        if (matcherForFDA.find()) {
            LinkedHashMap<String, String> stringStringLinkedHashMap = fmg.getData().get(dataIndex);
            for (int i = 0; i <= matcherForFDA.groupCount(); i++) {
                String groupVal = matcherForFDA.group(i);
                groupVal = groupVal.substring(6, groupVal.length() - 1);
                String flatMessageDataStr = stringStringLinkedHashMap.get(groupVal);
                if (null != flatMessageDataStr && sinkValue != null) {
                    vb.setLength(0);
                    vb.append("'");vb.append(flatMessageDataStr);vb.append("'");
                    sinkValue = sinkValue.replace("#{FDA." + groupVal + "}", vb.toString());
                }
            }
            result = sinkValue;
        }

        return result;
    }

    /**
     * 删除最后一个 del 字符
     *
     * @param sb
     * @param del
     */
    private void deleteLastStrForStringBuffer(StringBuffer sb, String del) {
        if (sb.lastIndexOf(del) <= sb.length() - 1 && sb.lastIndexOf(del) >= 0) {
            sb.delete(sb.lastIndexOf(del), sb.length());
        }
    }

    /**
     * 拼装where段sql (涉及原有column 向新 column 装换)
     * <p>
     * 注意在update时，如果主键发生变更，需要对比一下old值，这里已处理
     * 注意在delete时，old值为空，需要判定空指针，这里已处理
     *
     * @param fmg
     * @param index
     * @param sb
     * @param odsConfigTable
     */
    private void buildWhereSql(FlatMessage fmg, int index, StringBuffer sb, OdsConfigTable odsConfigTable) {
        boolean inFor = false;
        List<String> sourcePKs = odsConfigTable.findSourcePKs();
        sb.append(" where ");
        for (int i = 0; i < sourcePKs.size(); i++) {
            inFor = true;
            String pk = sourcePKs.get(i);
            LinkedHashMap<String, String> newValueLinkedHashMap = null;
            LinkedHashMap<String, String> oldValueLinkedHashMap = null;
            String newValue = null;
            String oldValue = null;

            if (null != fmg.getData()) {
                newValueLinkedHashMap = fmg.getData().get(index);
            }
            if (null != fmg.getOld()) {
                oldValueLinkedHashMap = fmg.getOld().get(index);
            }

            if (null != newValueLinkedHashMap) {
                newValue = newValueLinkedHashMap.get(pk);
            }
            if (null != oldValueLinkedHashMap) {
                oldValue = oldValueLinkedHashMap.get(pk);
            }

            String value = (!StringUtils.isEmpty(oldValue)) ? oldValue : newValue;
            sb.append(odsConfigTable.findSourceColumn(pk).getSinkColumnNameIfNull());
            sb.append("=");
            sb.append(fmg.convertThisValue(pk, value));
            sb.append(" and ");
        }
        if (inFor) {
            deleteLastStrForStringBuffer(sb, " and ");
        }
    }
}
