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
 * @date 2023/3/23
 * 角色实体类
 */
@Data
@ToString
@TableName(value = "sys_role")
public class SysRole implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//角色名称
    @TableField(value = "number")
    public Integer number;//可申报倍数
    @TableField(value = "level_sorting")
    public Integer levelSorting;//级别排序

    public SysRole() {
    }

    public SysRole(Integer id, Integer levelSorting) {
        this.id = id;
        this.levelSorting = levelSorting;
    }
}
