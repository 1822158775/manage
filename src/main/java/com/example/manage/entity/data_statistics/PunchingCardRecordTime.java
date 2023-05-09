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

    public PunchingCardRecordTime() {
    }

    public PunchingCardRecordTime(String checkIn, String checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
