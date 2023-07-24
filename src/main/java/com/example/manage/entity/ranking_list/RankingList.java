package com.example.manage.entity.ranking_list;

import com.example.manage.entity.PerformanceReportSales;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/4/20
 */

@Data
@ToString
public class RankingList implements Serializable {
    public Integer id;//项目编号
    public Integer countNumber;//项目数据数量
    public String name;//项目名称
    public String dayTime;//日期
    public String cardTypeName;//卡名称名称
    public String personnelName;//人员名称
    public String reportStatus;//状态名称
    public String alreadyReportStatus;//已拥有的状态名称
    public Integer activation;//批核已激活
    public Integer approved;//批核未激活
    public Integer artificial;//转人工
    public Integer refuse;//拒绝
    public Integer cardTypeId;//卡类型id
    public Integer attendance;//实际签到次数
    public Integer numberOfPeople;//应该签到人数
    public Integer monthlyIndicators;//指标完成率
    public List<PerformanceReportSales> performanceReportSales;//业绩权益

    public RankingList() {
    }

    public RankingList(Integer id) {
        this.id = id;
    }

    public RankingList(Integer id, Integer countNumber, String name, String dayTime, String cardTypeName, String personnelName, String reportStatus, String alreadyReportStatus, Integer activation, Integer approved, Integer artificial, Integer refuse) {
        this.id = id;
        this.countNumber = countNumber;
        this.name = name;
        this.dayTime = dayTime;
        this.cardTypeName = cardTypeName;
        this.personnelName = personnelName;
        this.reportStatus = reportStatus;
        this.alreadyReportStatus = alreadyReportStatus;
        this.activation = activation;
        this.approved = approved;
        this.artificial = artificial;
        this.refuse = refuse;
    }
}
