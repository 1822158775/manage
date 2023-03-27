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
 * @date 2023-03-27 11:16:09
 * 余额记录管理
 */

@Data
@ToString
public class BalanceRecordManagementNotNull implements Serializable {
    public String id;//数据编码
    public String managementId;//项目编码
    public String remainingSum;//项目余额
    public String workingCost;//使用费用
    public String startTime;//开始时间
    public String endTime;//结束时间
}
