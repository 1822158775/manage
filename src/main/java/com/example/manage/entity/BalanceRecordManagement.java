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
 * @date 2023-03-27 11:16:09
 * 余额记录管理
 */

@Data
@ToString
@TableName(value = "balance_record_management")
public class BalanceRecordManagement implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "management_id")
    public Integer managementId;//项目编码
    @TableField(value = "remaining_sum")
    public String remainingSum;//项目余额
    @TableField(value = "working_cost")
    public String workingCost;//使用费用
    @TableField(value = "start_time")
    public String startTime;//开始时间
    @TableField(value = "end_time")
    public String endTime;//结束时间
}
