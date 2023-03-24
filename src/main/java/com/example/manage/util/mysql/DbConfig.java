package com.example.manage.util.mysql;

import com.alibaba.fastjson.JSONObject;
import com.example.manage.util.PanXiaoZhang;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2022/5/24
 */

public class DbConfig {
    @Value("${spring.shardingsphere.datasource.master.url}")
    private String mysql_url;

    @Value("${spring.shardingsphere.datasource.master.username}")
    private String username;

    @Value("${spring.shardingsphere.datasource.master.password}")
    private String password;

    /**
     * 获取连接
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/saas?useUnicode=true&charactemerchantfullnamerEncoding=utf8&useSSL=false", "root", "znrlsp123");
        return conn;
    }


    /**
     * 获取所有的数据库
     * @param connection
     * @return
     * @throws Exception
     */
    public static List<String> getDataBases(Connection connection) throws Exception {

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        ArrayList<String> dbs = new ArrayList<>();
        while (catalogs.next()) {
            String db = catalogs.getString(".TABLE_CAT");
            dbs.add(db);
        }

        return dbs;
    }

    /**
     * 获取当前数据库的所有表
     * @param connection 数据库连接对象
     * @param dataBase 数据库名称
     * @return
     * @throws SQLException
     */
    public static List<String> getTables(Connection connection, String dataBase) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
//        最后一个参数TABLE 表示用户表 见 DatabaseMetaData.getTableTypes()方法
        ResultSet resultSet = metaData.getTables(dataBase, null, null, new String[]{"TABLE"});
        ArrayList<String> tables = new ArrayList<>();
        while (resultSet.next()) {
            String table = resultSet.getString("TABLE_NAME");
            tables.add(table);
        }
        return tables;
    }

    /**
     * 获取数据库表的所有字段
     * @param connection 数据库连接对象
     * @param dataBase 数据库名
     * @param tableName 表名
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static List<DBEntity> getTableField(Connection connection, String dataBase, String tableName) throws SQLException, IOException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getColumns(dataBase, null, tableName, null);
        ArrayList<DBEntity> columns = new ArrayList<>();
        while (resultSet.next()) {
//            数据库名
            String db_name = resultSet.getString(".TABLE_CAT");
//            表名
            String table_name = resultSet.getString(".TABLE_NAME");
//            获取字段名
            String field = resultSet.getString(".COLUMN_NAME");
//            获取字段类型
            String fieldType = resultSet.getString(".TYPE_NAME");
            String fieldLength = resultSet.getString(".COLUMN_SIZE");
            String fieldDESC = resultSet.getString(".REMARKS");
            columns.add(new DBEntity(
                    db_name,
                    table_name,
                    field,
                    fieldType,
                    fieldDESC
            ));
            //String info = String.format("[%s->%s->%s->%s->%s->%s]", db_name, table_name, field, fieldType, fieldLength, fieldDESC);
            //System.out.println(info);
            //columns.add(field);
        }
        return columns;
    }
    //解析普通字段和id字段
    public static String notId(String str){
        if (str.equals("id")){
            return "@TableId(value = \""+str+"\",type = IdType.AUTO)";
        }else {
            return "@TableField(value = \""+str+"\")";
        }
    }
    //解析字段类型
    public static String notType(String str){
        if (str.equals("INT")){
            return "Integer";
        }else {
            return "String";
        }
    }
    //截取特定的符号后1位转化为大写
    public static String interceptToUppercase(String symbol,String str){
        String[] split = str.split(symbol);
        if (split.length > 1){
            str = split[0];
            for (int i = 1;i < split.length;i++){
                String value = split[i];
                if (value.length() > 0){
                    str += PanXiaoZhang.toUpperCase(value.charAt(0));
                    str += value.substring(1);

                }
            }
            return str;
        }
        return str;
    }
    //实体类字段拼装
    public static String entity(DBEntity dbEntity){
        String str = notId(dbEntity.getTheFieldNames());
        System.out.println(str);
        String notType = notType(dbEntity.getType());
        System.out.println("public "+notType+" "+interceptToUppercase("_",dbEntity.theFieldNames)+";//"+dbEntity.getRemark());
        return str;
    }
    //实体类字段拼装
    public static JSONObject entitys(DBEntity dbEntity){
        JSONObject object = new JSONObject();
        String str = notId(dbEntity.getTheFieldNames());
        object.put("key",str);
        String notType = notType(dbEntity.getType());
        object.put("value","public "+notType+" "+interceptToUppercase("_",dbEntity.theFieldNames)+";//"+dbEntity.getRemark());
        return object;
    }
    //mysql字段
    public static JSONObject mysql_key_value(DBEntity dbEntity){
        JSONObject object = new JSONObject();
        object.put("key",dbEntity.theFieldNames);
        object.put("value",interceptToUppercase("_",dbEntity.theFieldNames));
        return object;
    }
    //实体类字段拼装
    public static String isNotNullEntity(DBEntity dbEntity){
        String notType = notType(dbEntity.getType());
        return "public String "+interceptToUppercase("_",dbEntity.theFieldNames)+";//"+dbEntity.getRemark();
    }
}
