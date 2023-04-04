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
 * @date 2023-03-29 11:16:50
 * 离职申请管理
 */

@Data
@ToString
public class ManageDimissionNotNull implements Serializable {
    public String id;//数据编码
    public String applicant;//申请人
    public String personnelCode;//申请人资源代码
    public String reasonsForLeaving;//离职原因
    public String submissionTime;//提交时间
    public String resignationTime;//离职时间
    public String personnelId;//当前人员的数据编码

    public ManageDimissionNotNull(String id) {
        this.id = id;
    }

    public ManageDimissionNotNull() {
    }

    public ManageDimissionNotNull(String applicant, String personnelCode, String resignationTime, String personnelId) {
        this.applicant = applicant;
        this.personnelCode = personnelCode;
        this.resignationTime = resignationTime;
        this.personnelId = personnelId;
    }
}
