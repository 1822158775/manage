package com.example.manage.util.jdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @avthor 潘小章
 * @date 2023/5/11
 */

public class CreateMySQLTable {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/manage?useUnicode=true&charactemerchantfullnamerEncoding=utf8&useSSL=false";
        String user = "root";
        String password = "znrlsp123";
        String className = "com.rongke.firstslotgift.entity.Admin";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // 获取类的字段信息
            Class<?> clazz = Class.forName(className);
            Field[] fields = clazz.getDeclaredFields();

            // 构建SQL语句
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(clazz.getSimpleName()).append(" (");
            for (Field field : fields) {
                sb.append(field.getName()).append(" ").append(getSQLType(field)).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(")");

            // 执行SQL语句
            stmt.executeUpdate(sb.toString());
            System.out.println("Table created successfully");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取字段的SQL数据类型
    private static String getSQLType(Field field) {
        Class<?> type = field.getType();
        if (type == int.class || type == Integer.class) {
            return "INT";
        } else if (type == long.class || type == Long.class) {
            return "BIGINT";
        } else if (type == double.class || type == Double.class) {
            return "DOUBLE";
        } else if (type == float.class || type == Float.class) {
            return "FLOAT";
        } else if (type == boolean.class || type == Boolean.class) {
            return "BOOLEAN";
        } else if (type == String.class) {
            return "VARCHAR(255)";
        } else {
            return "VARCHAR(255)";
        }
    }
}

