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
 * @date 2023-06-16 11:43:54
 * 业绩关联权益
 */

@Data
@ToString
public class PerformanceReportSalesNotNull implements Serializable {
    public String id;//数据编码
    public String sales;//权益
    public String type;//权益名称
    public String reportCoding;//业绩代码
}
