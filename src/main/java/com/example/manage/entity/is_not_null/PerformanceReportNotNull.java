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
    public String reportStatus;//拒绝，转人工，批核未激活,批核已激活
    public String managementId;//项目数据编码
    public String reportCoding;//报告编码
    public String approverPersonnelCode;//审批人资源代码
    public String remark;//报告备注
    public String commentsFromReviewers;//审核人的留言
    public String approverState;//状态
    public String cardTypeId;//办卡类型id
    public String personnelId;//当前人员的数据编码
    public String reportNumber;//办卡数

    public String entryNumber;//进件数
    public String approvedNumber;//批核数
    public String validNumber;//有效数
    public String refuseNumber;//拒绝数

    public PerformanceReportNotNull() {
    }

    public PerformanceReportNotNull(String id) {
        this.id = id;
    }

    public PerformanceReportNotNull(String id, String approverState) {
        this.id = id;
        this.approverState = approverState;
    }

    public PerformanceReportNotNull(String reportStatus, String cardTypeId, String personnelId, String reportNumber) {
        this.reportStatus = reportStatus;
        this.cardTypeId = cardTypeId;
        this.personnelId = personnelId;
        this.reportNumber = reportNumber;
    }

    public PerformanceReportNotNull(String reportStatus, String cardTypeId, String personnelId, String reportNumber, String entryNumber, String approvedNumber, String validNumber, String refuseNumber) {
        this.reportStatus = reportStatus;
        this.cardTypeId = cardTypeId;
        this.personnelId = personnelId;
        this.reportNumber = reportNumber;
        this.entryNumber = entryNumber;
        this.approvedNumber = approvedNumber;
        this.validNumber = validNumber;
        this.refuseNumber = refuseNumber;
    }
}
