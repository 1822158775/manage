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
 * @date 2023-03-24 15:26:30
 * 角色权限
 */

@Data
@ToString
@TableName(value = "sys_table_authority")
public class SysTableAuthority implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//
    @TableField(value = "table_name_id")
    public Integer tableNameId;//表名id
    @TableField(value = "authority")
    public Integer authority;//1：查询，2：添加，3：修改，4删除
    @TableField(value = "authority_state")
    public Integer authorityState;//权限状态
    @TableField(value = "role_id")
    public Integer roleId;
    @TableField(exist = false)
    public SysTableName sysTableName;//表名称
    @TableField(exist = false)
    public SysRole sysRole;//权限名称
}
