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
 * @date 2023-03-29 16:50:54
 * 业绩记录管理
 */

@Data
@ToString
public class PerformanceReportNotNull implements Serializable {
    public String id;//数据编码
    public String personnelCode;//资源代码
    public String reportTime;//报告时间
    public String reportStatus;//拒绝，转人工，批核,激活
    public String managementId;//项目数据编码
    public String reportCoding;//报告编码
    public String approverPersonnelCode;//审批人资源代码
    public String remark;//报告备注
    public String commentsFromReviewers;//审核人的留言
    public String approverState;//状态
    public String cardTypeId;//办卡类型id
    public String personnelId;//当前人员的数据编码

    public PerformanceReportNotNull() {
    }

    public PerformanceReportNotNull(String reportStatus, String cardTypeId, String personnelId) {
        this.reportStatus = reportStatus;
        this.cardTypeId = cardTypeId;
        this.personnelId = personnelId;
    }
}
