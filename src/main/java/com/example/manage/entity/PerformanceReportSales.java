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
 * @date 2023-06-16 11:43:54
 * 业绩关联权益
 */

@Data
@ToString
@TableName(value = "performance_report_sales")
public class PerformanceReportSales implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "sales")
    public Integer sales;//权益
    @TableField(value = "type")
    public String type;//权益名称
    @TableField(value = "report_coding")
    public String reportCoding;//业绩代码
    @TableField(value = "comment")
    public String comment;//备注
}
