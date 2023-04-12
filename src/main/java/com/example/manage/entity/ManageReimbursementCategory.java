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
 * @date 2023-04-10 18:23:07
 * 类目管理
 */

@Data
@ToString
@TableName(value = "manage_reimbursement_category")
public class ManageReimbursementCategory implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "name")
    public String name;//类目名称
    @TableField(value = "reimbursement_type")
    public String reimbursementType;//固定(gd),可变(kb)
    @TableField(value = "amount")
    public String amount;//金额
    @TableField(value = "particular_conditions")
    public Boolean particularConditions;//是否有特殊条件：0没有，1有
    @TableField(value = "category_coding")
    public String categoryCoding;//类目编码
    @TableField(exist = false)
    public Integer[] categoryCopyNumber;//关联抄送人
    @TableField(exist = false)
    public Integer[] manageRmNumber;//关联审核人
    @TableField(exist = false)
    public Integer[] conditionNumber;//关联特殊条件
    @TableField(exist = false)
    public List<SysRole> sysRoleManage;//审核人职位
    @TableField(exist = false)
    public List<SysRole> sysRoleCategory;//抄送人职位
    @TableField(exist = false)
    public List<ManageCondition> manageConditions;//特殊条件关联
}
