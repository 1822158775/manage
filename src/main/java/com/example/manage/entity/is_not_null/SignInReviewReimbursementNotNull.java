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
 * @date 2024-08-02 11:00:15
 * 视频签到审核表
 */

@Data
@ToString
public class SignInReviewReimbursementNotNull implements Serializable {
    public String id;//
    public String personnelId;//人员数据编码
    public String verifierRemark;//备注
    public String verifierState;//审核状态:pending,agree,refuse
    public String verifierTime;//审核通过时间
    public String dispatchCode;//编码
    public String dispatchState;//审核标识
    public String number;//流转层级
}
