package com.example.manage.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/3/27
 */
@Data
@ToString
public class TableAuthorityName {
    public List<SysTableAuthority> sysTableAuthorities;//权限
    public List<SysTableName> sysTableNames;//名称

    public TableAuthorityName() {
    }

    public TableAuthorityName(List<SysTableAuthority> sysTableAuthorities, List<SysTableName> sysTableNames) {
        this.sysTableAuthorities = sysTableAuthorities;
        this.sysTableNames = sysTableNames;
    }
}
