package com.example.manage.entity.is_not_null;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 * 角色实体类
 */
@Data
@ToString
public class SysRoleNotNull implements Serializable {
    public String id;//数据编码
    public String name;//角色名称
    public String number;//可申报倍数
    public String levelSorting;//级别排序

    public SysRoleNotNull() {
    }

    public SysRoleNotNull(String id) {
        this.id = id;
    }

    public SysRoleNotNull(String name, String number, String levelSorting) {
        this.name = name;
        this.number = number;
        this.levelSorting = levelSorting;
    }

    public SysRoleNotNull(String id, String name, String number, String levelSorting) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.levelSorting = levelSorting;
    }
}
