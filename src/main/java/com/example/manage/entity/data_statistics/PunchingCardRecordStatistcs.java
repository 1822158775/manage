package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/5/9
 */

@Data
@ToString
public class PunchingCardRecordStatistcs implements Serializable {

    public String name;//人员名称

    public List<Personnel> personnelName;//主管人员名称
    public List<Management> managementName;//项目名称

    public Integer dutyDays;//执勤天数
    public Integer lateArrivals;//迟到次数
    public Integer earlyDepartures;//早退次数
    public Integer accomodate;//缺卡次数

    public String sumTime;//总时长

    public List<PunchingCardRecordTime> punchingCardRecordList;//签到时间

    public PunchingCardRecordStatistcs() {
    }

    public PunchingCardRecordStatistcs(String name, List<Personnel> personnelName, List<Management> managementName, Integer dutyDays, Integer lateArrivals, Integer earlyDepartures, Integer accomodate, String sumTime, List<PunchingCardRecordTime> punchingCardRecordList) {
        this.name = name;
        this.personnelName = personnelName;
        this.managementName = managementName;
        this.dutyDays = dutyDays;
        this.lateArrivals = lateArrivals;
        this.earlyDepartures = earlyDepartures;
        this.accomodate = accomodate;
        this.sumTime = sumTime;
        this.punchingCardRecordList = punchingCardRecordList;
    }
}
