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
 * @date 2023-04-26 11:17:41
 * 调派关联审批人
 */

@Data
@ToString
public class DispatchApplicationReimbursementNotNull implements Serializable {
    public String id;//数据编码
    public String personnelId;//人员数据编码
    public String verifierRemark;//备注
    public String verifierState;//审核状态
    public String verifierTime;//审核通过时间
    public String dispatchCode;//调派编码
}
