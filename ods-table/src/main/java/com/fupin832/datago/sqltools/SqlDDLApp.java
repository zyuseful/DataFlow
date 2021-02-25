package com.fupin832.datago.sqltools;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;
import java.util.Scanner;

/**
 * 将sql create table -> ods table config json str
 * /Users/zys/ZySoftWare/WorkSpace/HJW/T_test/test/t1.sql
 * @author zy
 * @date 2021/02/18
 */
public class SqlDDLApp {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("选择功能:");
        System.out.println("1 获取转换ods json");
        System.out.println("2 获取创建 ddl");
        String select = scanner.nextLine();
        switch (select) {
            case "1": {
                OdsTable.createOdsJsonStr(scanner);
                break;
            }
            case "2": {
                MySQLToGP.createDDLStr(scanner);
                break;
            }
            default: {
                System.out.println("退出");
                return;
            }
        }

    }





    public static List<String> getDMLTableName(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
        return tableList;
    }

}
