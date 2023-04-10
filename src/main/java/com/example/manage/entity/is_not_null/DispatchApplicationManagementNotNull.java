package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023-03-29 11:28:23
 * 调派管理
 */

@Data
@ToString
public class DispatchApplicationManagementNotNull implements Serializable {
    public String id;//数据编码
    public String applicant;//申请人
    public String phone;//手机号
    public String personnelCode;//申请人资源代码
    public String remark;//调派原因
    public String agoManagementId;//调派前项目数据编码
    public String laterManagementId;//调派后项目数据编码
    public String agoAuditTime;//调派前审核时间
    public String agoPersonnelId;//调派前审核人的数据编码
    public String agoVerifierRemark;//调派前审核人留言
    public String agoVerifierState;//调派前审核人状态
    public String laterAuditTime;//调派后审核时间
    public String laterPersonnelId;//调派后审核人
    public String laterVerifierRemark;//调派后审核人留言
    public String laterVerifierState;//调派后审核人状态
    public String personnelId;//申请人资源代码
    public String dispathchTime;//调派时间
    public String applicantTime;//申请时间

    public DispatchApplicationManagementNotNull() {
    }

    public DispatchApplicationManagementNotNull(String laterManagementId, String laterVerifierRemark, String personnelId,String dispathchTime) {
        this.laterManagementId = laterManagementId;
        this.laterVerifierRemark = laterVerifierRemark;
        this.personnelId = personnelId;
        this.dispathchTime = dispathchTime;
    }
}
