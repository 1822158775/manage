package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-03-24 13:53:40
 * 数据表名称管理
 */

@Data
@ToString
@TableName(value = "sys_table_name")
public class SysTableName implements Serializable {
@TableId(value = "id",type = IdType.AUTO)
public Integer id;//数据编码
@TableField(value = "name")
public String name;//表的名称
}
