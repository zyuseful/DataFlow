package com.fupin832.datago.sqltools;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Database;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ods json 模板辅助生成
 *
 * @author zy
 * @date 2021/02/19
 */
public class OdsTable {
    public static void createOdsJsonStr(Scanner scanner) throws Exception {
        System.out.println("输入ddl文件路径");
        String inFilePath = scanner.nextLine();
        System.out.println("输入转换输出文件路径");
        String outFilePath = scanner.nextLine();
        System.out.println("开始转换请稍后....");
        transform(inFilePath,outFilePath);
    }

    public static void transform(String inFilePath,String outFilePath) throws JSQLParserException, IOException {
        File inFile = new File(inFilePath);
        File outFile = new File(outFilePath);

        FileReader fr = null;
        BufferedReader br = null;

        FileWriter fw = null;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new FileReader(inFile);
            br = new BufferedReader(fr);
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fr) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("读取输入:");
        System.out.println(sb.toString());
        String odsTableJsonStr = getDDLOdsJsonStr(sb.toString());
        if (StringUtils.isEmpty(outFilePath)) {
            System.out.println("打印输出:");
            System.out.println(odsTableJsonStr);
        } else {
            System.out.println("文件输出:"+outFilePath);
            FileUtils.writeStringToFile(outFile,odsTableJsonStr,"utf-8");
        }
    }

    public static String getDDLOdsJsonStr(String sql) throws JSQLParserException {
        Statements statements = CCJSqlParserUtil.parseStatements(sql);
        List<Statement> statementsArr = statements.getStatements();
        Statement statement = statementsArr.get(0);
        CreateTable ct =  (CreateTable) statement;

        Table table = ct.getTable();
        /** 表名 */
        String tableName = table.getName().trim().replaceAll("`", "");
        Database database = table.getDatabase();
        /** DataBaseName */
        String databaseName = null;
        if (null != database) {
            databaseName = database.getDatabaseName();
            if (!StringUtils.isEmpty(databaseName)) {
                databaseName = databaseName.trim().replaceAll("`", "");
            }
        }
        /** 列 */
        List<ColumnDefinition> columnDefinitions = ct.getColumnDefinitions();
        List<JSONObject> columnJoArr = new ArrayList<>(columnDefinitions.size());
        for (int i=0;i<columnDefinitions.size();i++) {
            ColumnDefinition columnDefinition = columnDefinitions.get(i);
            JSONObject js = new JSONObject();
            js.put("sourceColumnName",columnDefinition.getColumnName().trim().replaceAll("`",""));
            js.put("sinkColumnName",null);
            js.put("sinkValue",null);
            js.put("usingMySelfValue",false);
            js.put("needUpdate",true);
            js.put("needInsert",true);
            js.put("isPK",false);
            columnJoArr.add(js);
        }

        JSONObject jsonO = new JSONObject();
        jsonO.put("sourceDBName",databaseName);
        jsonO.put("sourceTableName",tableName);
        jsonO.put("sinkDBName",null);
        jsonO.put("sinkTableName","TODO");
        jsonO.put("ddl","INSERT,UPDATE,DELETE");
        jsonO.put("insertDML",null);
        jsonO.put("updateDML",null);
        jsonO.put("deleteDML",null);
        jsonO.put("columns",columnJoArr);

        String result = JSONObject.toJSONString(jsonO, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
//        String result = JSONObject.toJSONString(jsonO,true);
//        String result = JSONObject.toJSONString(jsonO,false);
///        String result = jsonO.toJSONString();
        return result;
    }
}
