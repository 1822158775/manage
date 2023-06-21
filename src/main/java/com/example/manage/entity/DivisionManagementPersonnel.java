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
 * @date 2023-06-19 17:04:29
 * 部门类型关联部门管理表
 */

@Data
@ToString
@TableName(value = "division_management_personnel")
public class DivisionManagementPersonnel implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//部门关联人员表
    @TableField(value = "division_management_id")
    public Integer divisionManagementId;//部门id
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员id
}
