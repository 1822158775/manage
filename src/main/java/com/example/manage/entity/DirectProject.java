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
 * @date 2023-06-25 17:25:34
 * 部门直管项目表
 */

@Data
@ToString
@TableName(value = "direct_project")
public class DirectProject implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "division_management_id")
    public Integer divisionManagementId;//部门id
    @TableField(value = "management_id")
    public Integer managementId;//项目id
}
