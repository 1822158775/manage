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
 * @date 2023-08-01 10:54:39
 * 反馈记录表
 */

@Data
@ToString
public class FeedbackRecordSheetNotNull implements Serializable {
    public String id;//反馈记录表编码
    public String personnelId;//反馈人唯一标识
    public String content;//反馈内容
    public String submissionTime;//提交时间

    public FeedbackRecordSheetNotNull() {
    }

    public FeedbackRecordSheetNotNull(String id, String personnelId, String content, String submissionTime) {
        this.id = id;
        this.personnelId = personnelId;
        this.content = content;
        this.submissionTime = submissionTime;
    }
}
