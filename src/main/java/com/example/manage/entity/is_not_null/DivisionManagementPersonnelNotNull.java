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
 * @date 2023-06-19 17:04:30
 * 部门类型关联部门管理表
 */

@Data
@ToString
public class DivisionManagementPersonnelNotNull implements Serializable {
    public String id;//部门关联人员表
    public String divisionManagementId;//部门id
    public String personnelId;//人员id
}
