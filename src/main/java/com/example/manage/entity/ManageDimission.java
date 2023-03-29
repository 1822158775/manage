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
 * @date 2023-03-29 11:16:50
 * 离职申请管理
 */

@Data
@ToString
@TableName(value = "manage_dimission")
public class ManageDimission implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "applicant")
    public String applicant;//申请人
    @TableField(value = "personnel_code")
    public String personnelCode;//申请人资源代码
    @TableField(value = "reasons_for_leaving")
    public String reasonsForLeaving;//离职原因
    @TableField(value = "submission_time")
    public String submissionTime;//提交时间
    @TableField(value = "resignation_time")
    public String resignationTime;//离职时间
    @TableField(value = "applicant_state")
    public String applicantState;//审核状态
    @TableField(exist = false)
    public SysManagement sysManagement;

    public ManageDimission() {
    }

    public ManageDimission(Integer id, String applicantState) {
        this.id = id;
        this.applicantState = applicantState;
    }
}
