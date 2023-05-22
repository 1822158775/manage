package com.example.manage.util.mysql;

import java.sql.Connection;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2022/5/24
 */

public class App {
    public static void main(String[] args) throws Exception {
        Connection connection = DbConfig.getConnection();
        List<DBEntity> tableField = DbConfig.getTableField(connection, "manage", "card_replacement_record");
        for (int i = 0;i < tableField.size();i++){
            DBEntity dbEntity = tableField.get(i);
            String entity = DbConfig.entity(dbEntity);
        }
        System.out.println("---------------------------");
        for (int i = 0;i < tableField.size();i++){
            DBEntity dbEntity = tableField.get(i);
            String entity_is_not_null = DbConfig.isNotNullEntity(dbEntity);
        }
        //System.out.println(connection);
        //List<String> dataBases = DbConfig.getDataBases(connection);
        //System.out.println("数据库个数：" + dataBases.size());
        //for (String dataBase : dataBases) {
        //    List<String> tables = DbConfig.getTables(connection, dataBase);
        //    String log = "数据库[%s]中共有[%s]张表";
            //System.out.println(String.format(log, dataBase, tables.size()));
            //for (String table : tables) {
            //    List<String> tableField = DbConfig.getTableField(connection, dataBase, "refund_table");
                String log2 = "数据库[%s]->[%s]表中共有[%s]个字段";
                //System.out.println(String.format(log2, dataBase, "refund_table", tableField.size()));
            //}
        //}
    }
}
