package com.example.manage.entity.number;

import com.example.manage.entity.PerformanceReport;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 * 订单个个状态
 */
@Data
@ToString
public class PerformanceReportNumber implements Serializable {
    public Integer all;//全部数量
    public Integer approve;//批核未激活
    public Integer active;//批核已激活
    public Integer refuse;//拒绝
    public Integer pendding;//转人工
    public Integer thisMonthActive;//本月批核已激活
    public Integer thisMonthApprove;//本月批核未激活
    public String approverState;//状态
    public List<PerformanceReport> performanceReport;//业绩申报
}
