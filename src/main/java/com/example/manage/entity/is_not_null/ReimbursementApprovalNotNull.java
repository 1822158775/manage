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
 * @date 2023-04-11 15:22:19
 * 报销记录关联审批人进行审批
 */

@Data
@ToString
public class ReimbursementApprovalNotNull implements Serializable {
    public String id;//数据编码
    public String reimbursementRecordCode;//报销记录编码
    public String personnelCode;//资源代码
    public String personnelName;//审核人名称
    public String number;//审批顺序
    public String maxNumber;//最高的数字
    public String approvalTime;//审批时间
    public String approvalState;//审批状态
    public String remark;//备注
}
