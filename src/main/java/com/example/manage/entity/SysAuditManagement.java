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
 * @date 2023/3/24
 * 审批报销人管理
 */
@Data
@ToString
@TableName(value = "sys_audit_management")
public class SysAuditManagement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "username")
    public String username;//审核人账号
    @TableField(value = "management_id")
    public Integer managementId;//项目唯一标识
    @TableField(value = "reimbursement_id")
    public Integer reimbursementId;//类目唯一标识
}
