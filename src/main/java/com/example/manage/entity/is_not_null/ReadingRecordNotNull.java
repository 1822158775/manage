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
 * @date 2023-06-01 14:43:43
 * 阅读记录
 */

@Data
@ToString
public class ReadingRecordNotNull implements Serializable {
    public String id;//数据编码
    public String personnelId;//人员编号
    public String managementId;//项目编号
    public String dayTime;//时间
    public String readingState;//阅读状态

    public ReadingRecordNotNull() {
    }

    public ReadingRecordNotNull(String id, String personnelId, String managementId, String dayTime, String readingState) {
        this.id = id;
        this.personnelId = personnelId;
        this.managementId = managementId;
        this.dayTime = dayTime;
        this.readingState = readingState;
    }
}
