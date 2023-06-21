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
 * @date 2023-06-21 17:21:22
 * 总经理关联部门
 */

@Data
@ToString
@TableName(value = "division_personnel")
public class DivisionPersonnel implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "division_management_id")
    public Integer divisionManagementId;//部门管理id
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员id

    public DivisionPersonnel(Integer id, Integer divisionManagementId, Integer personnelId) {
        this.id = id;
        this.divisionManagementId = divisionManagementId;
        this.personnelId = personnelId;
    }

    public DivisionPersonnel() {
    }
}
