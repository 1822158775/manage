package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/5/9
 */

@Data
@ToString
public class PunchingCardRecordTime implements Serializable {
    public String checkIn;//上班签到
    public String checkOut;//下班签到
    public String furloughRecordName;//请假类型
    public String furloughRecordTime;//请假时间

    public PunchingCardRecordTime() {
    }

    public PunchingCardRecordTime(String checkIn, String checkOut, String furloughRecordName, String furloughRecordTime) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.furloughRecordName = furloughRecordName;
        this.furloughRecordTime = furloughRecordTime;
    }
}
