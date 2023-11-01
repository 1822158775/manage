package com.example.manage.util;

import com.alibaba.fastjson.JSONObject;
import com.example.manage.util.mysql.DBEntity;
import com.example.manage.util.mysql.DbConfig;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/3/24
 */

public class CodeGeneration {
    // 项目路径
    private static String url = "D:\\home\\equity\\manage\\";
    // 包的路径
    private static String package_url = "com.example.manage.";
    // 创建文件路径
    private static String add_file_java = "src\\main\\java\\com\\example\\manage\\";
    // 创建文件路径
    private static String add_file_resources = "src\\main\\resources\\";
    // mysql 字段名称
    private static List<DBEntity> tableField = new ArrayList<>();
    /**
     * 计算x占y的百分比
     * @param x
     * @param y
     * @return
     */
    public static int percent(int x, int y) {
        Double d_x = x * 1.0;
        Double d_y = y * 1.0;
        double number = (d_x / d_y) * 100;
        return (int) number;
    }
    /**
     * 返回进度条
     * @param x 当前已完成的部分
     * @param y 当前未完成的部分
     */
    public static String progressBar(int x,int y,String msg){
        int number = percent(x, y);
        char incomplete = '░'; // U+2591 Unicode Character 表示还没有完成的部分
        char complete = '█'; // U+2588 Unicode Character 表示已经完成的部分
        StringBuilder builder = new StringBuilder();
        for (int i = 0;i <= number;i++){
            builder.replace(i, i + 1, String.valueOf(complete));
        }
        for (int i = number;i < 100;i++){
            builder.replace(i, i + 1, String.valueOf(incomplete));
        }
        String percent = " " + (number) + "%";
        String s = "\r" + builder + percent + "-----" + msg;
        System.out.print(s);
        return s;
    }

    /**
     * 生成指定文件
     * @param fileNames 文件夹路径
     * @param fileName 文件名
     * @param data 数据
     * @param type 文件类型
     */
    public static void addValue(String fileNames,String fileName,String data,String type){
        File file_mk = new File(fileNames);
        if(!file_mk.exists()){
            file_mk.mkdirs();
        }
        File file = new File(fileNames + fileName + "." + type); // 相对路径，如果没有则要建立一个新的output.txt文件
        try {
            FileOutputStream fos = new FileOutputStream(file,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(data);
            bw.newLine();
            bw.flush();
            bw.close();
            osw.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Java 文件添加
     * @param package_name 包名
     * @param msg
     * @param class_name
     * @param class_type
     */
    public static void add_package_file(String package_name,String msg,String class_name,String class_type){
        addValue(url + add_file_java + package_name + "\\", class_name,msg,class_type);
    }
    /**
     * xml 配件添加
     * @param package_name 包名
     * @param msg
     * @param class_name
     * @param class_type
     */
    public static void add_xml_file(String package_name,String msg,String class_name,String class_type){
        addValue(url + add_file_resources + package_name + "\\", class_name,msg,class_type);
    }
    /**
     * 获取表的字段名
     * @param table_name
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void mysql_get(String table_name) throws SQLException, ClassNotFoundException, IOException {
        Connection connection = DbConfig.getConnection();
        tableField = DbConfig.getTableField(connection, "manage", table_name);
    }
    /**
     * Java 实体类代码生成
     * @param remark 数据库表的名称以及备注
     * @param table_name 表名
     * @param class_name 类名
     */
    public static void addJavaNotNullEntity(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("package " + package_url + "entity.is_not_null;");
        arrayList.add("");
        arrayList.add("import com.baomidou.mybatisplus.annotation.IdType;");
        arrayList.add("import com.baomidou.mybatisplus.annotation.TableField;");
        arrayList.add("import com.baomidou.mybatisplus.annotation.TableId;");
        arrayList.add("import com.baomidou.mybatisplus.annotation.TableName;");
        arrayList.add("import lombok.Data;");
        arrayList.add("import lombok.ToString;");
        arrayList.add("import java.io.Serializable;");
        arrayList.add("");
        arrayList.add("/**");
        arrayList.add(" * @avthor 潘小章");
        arrayList.add(" * @date " + DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        arrayList.add(" * " + remark);
        arrayList.add(" */");
        arrayList.add("");
        arrayList.add("@Data");
        arrayList.add("@ToString");
        arrayList.add("public class " + class_name + " implements Serializable {");
        for (int i = 0;i < tableField.size();i++){

            DBEntity dbEntity = tableField.get(i);
            String entity = DbConfig.isNotNullEntity(dbEntity);
            arrayList.add("    " + entity);
        }
        arrayList.add("}");
        for (int i = 0; i < arrayList.size(); i++) {
            String s = arrayList.get(i);
            add_package_file(
                    "entity\\is_not_null",
                    s,
                    class_name,
                    "java"
            );
        }
    }
    /**
     * Java 实体类代码生成
     * @param remark 数据库表的名称以及备注
     * @param table_name 表名
     * @param class_name 类名
     */
    public static void addJavaEntity(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("package " + package_url + "entity;");
        arrayList.add("");
        arrayList.add("import com.baomidou.mybatisplus.annotation.IdType;");
        arrayList.add("import com.baomidou.mybatisplus.annotation.TableField;");
        arrayList.add("import com.baomidou.mybatisplus.annotation.TableId;");
        arrayList.add("import com.baomidou.mybatisplus.annotation.TableName;");
        arrayList.add("import lombok.Data;");
        arrayList.add("import lombok.ToString;");
        arrayList.add("import java.io.Serializable;");
        arrayList.add("");
        arrayList.add("/**");
        arrayList.add(" * @avthor 潘小章");
        arrayList.add(" * @date " + DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        arrayList.add(" * " + remark);
        arrayList.add(" */");
        arrayList.add("");
        arrayList.add("@Data");
        arrayList.add("@ToString");
        arrayList.add("@TableName(value = \"" + table_name + "\")");
        arrayList.add("public class " + class_name + " implements Serializable {");
        for (int i = 0;i < tableField.size();i++){
            progressBar(i,tableField.size() - 1,"加载数据");
            DBEntity dbEntity = tableField.get(i);
            JSONObject entitys = DbConfig.entitys(dbEntity);
            arrayList.add("    " + entitys.get("key"));
            arrayList.add("    " + entitys.get("value"));
        }
        arrayList.add("}");
        for (int i = 0; i < arrayList.size(); i++) {
            progressBar(i,arrayList.size() - 1,"创建中");
            String s = arrayList.get(i);
            add_package_file(
                    "entity",
                    s,
                    class_name,
                    "java"
            );
        }
    }
    //创建mapper层
    public static void addJavaMapper(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("package " + package_url + "mapper;");
        arrayList.add("import com.baomidou.mybatisplus.core.mapper.BaseMapper;");
        arrayList.add("import com.example.manage.entity." + class_name + ";");
        arrayList.add("");
        arrayList.add("import java.util.List;");
        arrayList.add("import java.util.Map;");
        arrayList.add("/**");
        arrayList.add(" * @avthor 潘小章");
        arrayList.add(" * @date " + DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        arrayList.add(" * " + remark);
        arrayList.add(" */");
        arrayList.add("");
        arrayList.add("public interface I" + class_name + "Mapper extends BaseMapper<" + class_name + "> {");
        arrayList.add("    List<" + class_name + "> queryAll(Map map);");
        arrayList.add("    Integer queryCount(Map map);");
        arrayList.add("}");
        for (int i = 0; i < arrayList.size(); i++) {
            progressBar(i,arrayList.size() - 1,"创建中");
            String s = arrayList.get(i);
            add_package_file(
                    "mapper",
                    s,
                    "I" + class_name + "Mapper",
                    "java"
            );
        }
    }
    // 创建MyBatis xml文件
    public static void addMapperXml(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        arrayList.add("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        arrayList.add("<mapper namespace=\"com.example.manage.mapper.I" + class_name +"Mapper\">");
        arrayList.add("    <resultMap id=\"entity\" type=\"com.example.manage.entity." + class_name + "\">");
        for (int i = 0;i < tableField.size();i++){
            progressBar(i,tableField.size() - 1,"准备中");
            DBEntity dbEntity = tableField.get(i);
            JSONObject entitys = DbConfig.mysql_key_value(dbEntity);
            arrayList.add("        <result column=\"" + entitys.get("key") + "\" property=\"" + entitys.get("value") + "\" />");
        }
        arrayList.add("    </resultMap>");
        arrayList.add("    <select id=\"queryCount\" resultType=\"int\" parameterType=\"hashmap\">");
        arrayList.add("        select count(<include refid=\"tableSql\"/>.id) as id from");
        arrayList.add("        <include refid=\"tableSql\"/>");
        arrayList.add("        <include refid=\"whereSqlModel\"/>");
        arrayList.add("    </select>");
        arrayList.add("    <select id=\"queryAll\" resultMap=\"entity\" parameterType=\"hashmap\">");
        arrayList.add("        select <include refid=\"tableSql\"/>.*");
        arrayList.add("        from");
        arrayList.add("        <include refid=\"tableSql\"/>");
        arrayList.add("        <include refid=\"whereSqlModel\" />");
        arrayList.add("        <include refid=\"safeOrderBy\"/>");
        arrayList.add("        <include refid=\"safeLIMIT\"/>");
        arrayList.add("    </select>");
        arrayList.add("    <sql id=\"tableSql\">");
        arrayList.add("        manage." + table_name);
        arrayList.add("    </sql>");
        arrayList.add("    <sql id=\"whereSqlModel\">");
        arrayList.add("        where 1 = 1");
        arrayList.add("        <if test=\"id != null \"> and `id` = #{id}</if>");
        arrayList.add("    </sql>");
        arrayList.add("    <sql id=\"safeLIMIT\">");
        arrayList.add("        <if test=\"queryAll != 'yes'\">");
        arrayList.add("            <if test=\"pageNum != null and pageNum != ''\">");
        arrayList.add("                LIMIT #{index},#{pageNum}");
        arrayList.add("            </if>");
        arrayList.add("        </if>");
        arrayList.add("    </sql>");
        arrayList.add("    <sql id=\"safeOrderBy\">");
        arrayList.add("        order by <include refid=\"tableSql\"/>.id desc");
        arrayList.add("    </sql>");
        arrayList.add("</mapper>");
        for (int i = 0; i < arrayList.size(); i++) {
            progressBar(i,arrayList.size() - 1,"创建中");
            String s = arrayList.get(i);
            add_xml_file(
                    "mapper",
                    s,
                    "I" + class_name + "Mapper",
                    "xml"
            );
        }
    }
    // 创建service层
    public static void addService(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("package " + package_url + "service;");
        arrayList.add("");
        arrayList.add("import com.example.manage.util.entity.ReturnEntity;");
        arrayList.add("import javax.servlet.http.HttpServletRequest;");
        arrayList.add("");
        arrayList.add("/**");
        arrayList.add(" * @avthor 潘小章");
        arrayList.add(" * @date " + DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        arrayList.add(" * " + remark);
        arrayList.add(" */");
        arrayList.add("");
        arrayList.add("public interface I" + class_name + "Service {");
        arrayList.add("    ReturnEntity methodMaster(HttpServletRequest request, String name);");
        arrayList.add("}");
        for (int i = 0; i < arrayList.size(); i++) {
            progressBar(i,arrayList.size() - 1,"创建中");
            String s = arrayList.get(i);
            add_package_file(
                    "service",
                    s,
                    "I" + class_name + "Service",
                    "java"
            );
        }
    }
    // 创建service实现层层
    public static void addServiceImpl(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("package " + package_url + "service.impl;");
        arrayList.add("");
        arrayList.add("import com.example.manage.util.PanXiaoZhang;");
        arrayList.add("import com.example.manage.util.entity.ReturnEntity;");
        arrayList.add("import com.example.manage.entity." + class_name + ";");
        arrayList.add("import com.example.manage.mapper.I" + class_name + "Mapper;");
        arrayList.add("import com.example.manage.service.I" + class_name + "Service;");
        arrayList.add("import com.example.manage.util.entity.CodeEntity;");
        arrayList.add("import com.example.manage.util.entity.MsgEntity;");
        arrayList.add("import lombok.extern.slf4j.Slf4j;");
        arrayList.add("import org.springframework.stereotype.Service;");
        arrayList.add("import javax.annotation.Resource;");
        arrayList.add("import javax.servlet.http.HttpServletRequest;");
        arrayList.add("import java.util.Map;");
        arrayList.add("");
        arrayList.add("/**");
        arrayList.add(" * @avthor 潘小章");
        arrayList.add(" * @date " + DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        arrayList.add(" * " + remark);
        arrayList.add(" */");
        arrayList.add("");
        arrayList.add("@Slf4j");
        arrayList.add("@Service");
        arrayList.add("public class " + class_name + "ServiceImpl implements I" + class_name + "Service {");
        arrayList.add("    @Resource");
        arrayList.add("    private I"  + class_name +  "Mapper i" + class_name +  "Mapper;");
        arrayList.add("");
        arrayList.add("    //方法总管");
        arrayList.add("    @Override");
        arrayList.add("    public ReturnEntity methodMaster(HttpServletRequest request, String name) {");
        arrayList.add("        try {");
        arrayList.add("            if (name.equals(\"cat\")){");
        arrayList.add("                return cat(request);");
        arrayList.add("            }else if (name.equals(\"add\")){");
        arrayList.add("                " + class_name + " jsonParam = PanXiaoZhang.getJSONParam(request, " + class_name + ".class);");
        arrayList.add("                return add(request,jsonParam);");
        arrayList.add("            }else if (name.equals(\"edit\")){");
        arrayList.add("                " + class_name + " jsonParam = PanXiaoZhang.getJSONParam(request, " + class_name + ".class);");
        arrayList.add("                return edit(request,jsonParam);");
        arrayList.add("            }");
        arrayList.add("            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);");
        arrayList.add("        }catch (Exception e){");
        arrayList.add("            log.info(\"捕获异常方法{},捕获异常{}\",name,e.getMessage());");
        arrayList.add("            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);");
        arrayList.add("        }");
        arrayList.add("    }");
        arrayList.add("");
        arrayList.add("    // 修改" + remark);
        arrayList.add("    private ReturnEntity edit(HttpServletRequest request, " + class_name + " jsonParam) {");
        arrayList.add("        int updateById = i" + class_name + "Mapper.updateById(jsonParam);");
        arrayList.add("        //当返回值不为1的时候判断修改失败\n" +
                "        if (updateById != 1){\n" +
                "            return new ReturnEntity(\n" +
                "                    CodeEntity.CODE_ERROR,\n" +
                "                    jsonParam,\n" +
                "                    MsgEntity.CODE_ERROR\n" +
                "            );\n" +
                "        }");
        arrayList.add("        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);");
        arrayList.add("    }");
        arrayList.add("");
        arrayList.add("    // 添加" + remark);
        arrayList.add("    private ReturnEntity add(HttpServletRequest request, " + class_name + " jsonParam) {");
        arrayList.add("        //将数据唯一标识设置为空，由系统生成\n" +
                "        jsonParam.setId(null);\n" +
                "        //没有任何问题将数据录入进数据库\n" +
                "        int insert = i" + class_name + "Mapper.insert(jsonParam);\n" +
                "        //如果返回值不能鱼1则判断失败\n" +
                "        if (insert != 1){\n" +
                "            return new ReturnEntity(\n" +
                "                    CodeEntity.CODE_ERROR,\n" +
                "                    jsonParam,\n" +
                "                    MsgEntity.CODE_ERROR\n" +
                "            );\n" +
                "        }");
        arrayList.add("        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);");
        arrayList.add("    }");
        arrayList.add("");
        arrayList.add("    // 查询模块");
        arrayList.add("    private ReturnEntity cat(HttpServletRequest request) {");
        arrayList.add("        Map map = PanXiaoZhang.getJsonMap(request);");
        arrayList.add("        return new ReturnEntity(CodeEntity.CODE_SUCCEED,i" + class_name +  "Mapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,i" + class_name +  "Mapper.queryCount(map));");
        arrayList.add("    }");
        arrayList.add("}");
        for (int i = 0; i < arrayList.size(); i++) {
            progressBar(i,arrayList.size() - 1,"创建中");
            String s = arrayList.get(i);
            add_package_file(
                    "service\\impl",
                    s,
                    class_name + "ServiceImpl",
                    "java"
            );
        }
    }
    // 创建接口层
    public static void addController(String remark,String table_name,String class_name){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("package " + package_url + "controller;");
        arrayList.add("");
        arrayList.add("import com.example.manage.service.I" + class_name + "Service;");
        arrayList.add("import com.example.manage.util.entity.ReturnEntity;");
        arrayList.add("import org.springframework.web.bind.annotation.PostMapping;");
        arrayList.add("import org.springframework.web.bind.annotation.RequestMapping;");
        arrayList.add("import org.springframework.web.bind.annotation.RestController;");
        arrayList.add("");
        arrayList.add("import javax.annotation.Resource;");
        arrayList.add("import javax.servlet.http.HttpServletRequest;");
        arrayList.add("");
        arrayList.add("/**");
        arrayList.add(" * @avthor 潘小章");
        arrayList.add(" * @date " + DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        arrayList.add(" * " + remark);
        arrayList.add(" */");
        arrayList.add("");
        arrayList.add("@RestController");
        arrayList.add("@RequestMapping(value = \"/api/" + table_name + "/\")");
        arrayList.add("public class " + class_name + "Controller {");
        arrayList.add("    @Resource");
        arrayList.add("    private I" + class_name + "Service i" + class_name + "Service;");
        arrayList.add("");
        arrayList.add("    // 查询" + remark);
        arrayList.add("    @PostMapping(value = \"cat\")");
        arrayList.add("    public ReturnEntity cat(HttpServletRequest request){\n" +
                "        return i" + class_name + "Service.methodMaster(request,\"cat\");\n" +
                "    }");
        arrayList.add("");
        arrayList.add("    // 添加" + remark);
        arrayList.add("    @PostMapping(value = \"add\")");
        arrayList.add("    public ReturnEntity add(HttpServletRequest request){\n" +
                "        return i" + class_name + "Service.methodMaster(request,\"add\");\n" +
                "    }");
        arrayList.add("");
        arrayList.add("    // 修改" + remark);
        arrayList.add("   @PostMapping(value = \"edit\")");
        arrayList.add("    public ReturnEntity exit(HttpServletRequest request){\n" +
                "        return i" + class_name + "Service.methodMaster(request,\"edit\");\n" +
                "    }");
        arrayList.add("}");
        for (int i = 0; i < arrayList.size(); i++) {
            progressBar(i,arrayList.size() - 1,"创建中");
            String s = arrayList.get(i);
            add_package_file(
                    "controller",
                    s,
                    class_name + "Controller",
                    "java"
            );
        }
    }
    // 创建总管
    public static void methodMaster(String remark,String table_name,String class_name) throws SQLException, IOException, ClassNotFoundException {
        mysql_get(table_name);
        //实体类
        progressBar(0,100,"准备创建实体类");
        addJavaEntity(remark,table_name,class_name);
        progressBar(0,100,"准备创建非空实体类");
        //非空实体类
        addJavaNotNullEntity(
                remark,
                table_name,
                class_name + "NotNull"
        );
        //progressBar(0,100,"准备创建mapper层");
        ////mapper层创建
        //addJavaMapper(remark,table_name,class_name);
        //progressBar(0,100,"准备创建MyBatis xml文件");
        // //创建MyBatis xml文件
        //addMapperXml(remark,table_name,class_name);
        //progressBar(0,100,"准备创建service层");
        ////service层创建
        //addService(remark,table_name,class_name);
        //progressBar(0,100,"准备创建service实现层");
        ////service实现层创建
        //addServiceImpl(remark,table_name,class_name);
        //progressBar(0,100,"准备创建controller层");
        ////controller层创建
        //addController(remark,table_name,class_name);
    }
    public static void main(String[] args) {
        try {
            methodMaster("解绑记录表","unbind_record","UnbindRecord");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
