package com.example.manage.entity.is_not_null;

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
public class SysTableAuthorityNotNull implements Serializable {
    public String id;//
    public String tableNameId;//表名id
    public String authority;//1：查询，2：添加，3：修改，4删除
    public String authorityState;//权限状态
    public String roleId;//角色标识
    public String tableNameIds;//表名id数组

    public SysTableAuthorityNotNull() {
    }

    public SysTableAuthorityNotNull(String id) {
        this.id = id;
    }

    public SysTableAuthorityNotNull(String tableNameIds, String roleId) {
        this.tableNameIds = tableNameIds;
        this.roleId = roleId;
    }

    public SysTableAuthorityNotNull(String tableNameId, String authority, String authorityState, String roleId) {
        this.tableNameId = tableNameId;
        this.authority = authority;
        this.authorityState = authorityState;
        this.roleId = roleId;
    }
}
