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
 * @date 2023-04-11 16:01:59
 * 银行账户管理
 */

@Data
@ToString
@TableName(value = "manage_bank_account")
public class ManageBankAccount implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "personnel_code")
    public String personnelCode;//人员资源代码
    @TableField(value = "bank_account")
    public String bankAccount;//银行账户
    @TableField(value = "bank_of_deposit")
    public String bankOfDeposit;//开户银行
}
