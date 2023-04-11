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
 * @date 2023-04-10 18:27:51
 * 类目关联审批人管理
 */

@Data
@ToString
@TableName(value = "manage_r_m")
public class ManageRM implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "reimbursement_code")
    public String reimbursementCode;//类目编码
    @TableField(value = "role_id")
    public Integer roleId;//审批人编码

    public ManageRM() {
    }

    public ManageRM(String reimbursementCode, Integer roleId) {
        this.reimbursementCode = reimbursementCode;
        this.roleId = roleId;
    }
}
