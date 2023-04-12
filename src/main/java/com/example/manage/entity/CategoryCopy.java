package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:28:40
 * 类目关联抄送人管理
 */

@Data
@ToString
@TableName(value = "category_copy")
public class CategoryCopy implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "reimbursement_code")
    public String reimbursementCode;//类目编码
    @TableField(value = "role_id")
    public Integer roleId;//审批人编码
    @TableField(exist = false)
    public List<SysPersonnel> sysPersonnels;//人员集合

    public CategoryCopy() {
    }

    public CategoryCopy(String reimbursementCode, Integer roleId) {
        this.reimbursementCode = reimbursementCode;
        this.roleId = roleId;
    }
}
