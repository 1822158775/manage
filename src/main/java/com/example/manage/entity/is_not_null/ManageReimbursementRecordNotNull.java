package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.manage.entity.ReimbursementCategory;
import com.example.manage.entity.ReimbursementCopy;
import com.example.manage.entity.ReimbursementImage;
import com.example.manage.entity.ReimbursementProject;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:00:11
 * 申请报销记录管理
 */

@Data
@ToString
public class ManageReimbursementRecordNotNull implements Serializable {
    public String id;//数据编码
    public String phone;//手机号
    public String declarationCode;//申报编码
    public String account;//申报账号
    public String applicant;//申请人
    public String reimbursementType;//报销类型:固定(gd),可变(kb)
    public String amountDeclared;//申报总金额
    public String declarationTime;//申报时间
    public String approverTime;//审核时间
    public String remark;//备注
    public String approverState;//状态审核状态:pending,agree,refuse
    public String bankAccount;//银行账户
    public String maxNumber;//最高的数字

    public String reimbursementApprovals;//报销记录关联审批人进行审批
    public String reimbursementCategories;//报销记录关联类目
    public String reimbursementCopies;//报销记录抄送人
    public String reimbursementProjects;//报销记录关联项目
    public String reimbursementImages;//图片存储

    public ManageReimbursementRecordNotNull() {
    }

    public ManageReimbursementRecordNotNull(String reimbursementType, String amountDeclared, String remark, String bankAccount, String reimbursementApprovals, String reimbursementCategories, String reimbursementCopies, String reimbursementProjects, String maxNumber) {
        this.reimbursementType = reimbursementType;
        this.amountDeclared = amountDeclared;
        this.remark = remark;
        this.bankAccount = bankAccount;
        this.reimbursementApprovals = reimbursementApprovals;
        this.reimbursementCategories = reimbursementCategories;
        this.reimbursementCopies = reimbursementCopies;
        this.reimbursementProjects = reimbursementProjects;
        this.maxNumber = maxNumber;
    }
}
