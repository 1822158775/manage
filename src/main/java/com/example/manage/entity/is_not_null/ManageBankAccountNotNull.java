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
 * @date 2023-04-11 16:02:00
 * 银行账户管理
 */

@Data
@ToString
public class ManageBankAccountNotNull implements Serializable {
    public String id;//数据编码
    public String personnelCode;//人员资源代码
    public String bankAccount;//银行账户
    public String bankOfDeposit;//开户银行

    public ManageBankAccountNotNull() {
    }

    public ManageBankAccountNotNull(String bankAccount, String bankOfDeposit) {
        this.bankAccount = bankAccount;
        this.bankOfDeposit = bankOfDeposit;
    }
}
