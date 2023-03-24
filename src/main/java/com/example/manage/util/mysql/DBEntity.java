package com.example.manage.util.mysql;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2022/5/24
 */
@Data
@ToString
public class DBEntity implements Serializable {
    public String dbName;//数据库名称
    public String refundTablel;//表名称
    public String theFieldNames;//字段名称
    public String type;//字段类型
    public String remark;//解释

    public DBEntity() {
    }

    public DBEntity(String dbName, String refundTablel, String theFieldNames, String type, String remark) {
        this.dbName = dbName;
        this.refundTablel = refundTablel;
        this.theFieldNames = theFieldNames;
        this.type = type;
        this.remark = remark;
    }
}
