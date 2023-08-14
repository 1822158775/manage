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
 * @date 2023-08-01 10:54:39
 * 反馈记录表
 */

@Data
@ToString
@TableName(value = "feedback_record_sheet")
public class FeedbackRecordSheet implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//反馈记录表编码
    @TableField(value = "personnel_id")
    public Integer personnelId;//反馈人唯一标识
    @TableField(value = "content")
    public String content;//反馈内容
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "submission_time")
    public String submissionTime;//提交时间
}
