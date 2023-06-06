package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-06-01 17:40:59
 * 阅读记录
 */

@Data
@ToString
@TableName(value = "reading_record")
public class ReadingRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_id")
    public Integer personnelId;//人员编号
    @TableField(value = "management_id")
    public Integer managementId;//项目编号
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @TableField(value = "day_time")
    public String dayTime;//时间
    @TableField(value = "reading_state")
    public String readingState;//阅读状态
    @TableField(value = "management_name")
    public String managementName;//项目名称
    @TableField(value = "approver_personnel_id")
    public Integer approverPersonnelId;//审核人编号
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "report_time")
    public String reportTime;//报告时间
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "approver_time")
    public String approverTime;//审核时间
    @TableField(value = "entry_number")
    public Integer entryNumber;//进件数
    @TableField(value = "approved_number")
    public Integer approvedNumber;//批核数
    @TableField(value = "valid_number")
    public Integer validNumber;//有效数
    @TableField(value = "refuse_number")
    public Integer refuseNumber;//拒绝数
    @TableField(value = "approver_personnel_name")
    public String approverPersonnelName;//审核人名称
    @TableField(value = "personnel_name")
    public String personnelName;//申请人名称

    public ReadingRecord() {
    }

    public ReadingRecord(Integer id, String readingState) {
        this.id = id;
        this.readingState = readingState;
    }
}