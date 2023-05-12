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
 * @date 2023-05-10 18:39:34
 * 打卡时间表
 */

@Data
@ToString
public class CheckInTimeNotNull implements Serializable {
    public String id;//数据编码
    public String name;//打卡类型
    public String startPunchIn;//上班打卡时间开始时间
    public String endPunchIn;//上班打卡时间结束时间
    public String startNoonBreakTime;//午休开始时间
    public String endNoonBreakTime;//午休结束时间
    public String startClockOut;//下班打卡时间开始时间
    public String endClockOut;//下班打卡时间结束时间
    public String managementId;//项目编码

    public CheckInTimeNotNull() {
    }

    public CheckInTimeNotNull(String id, String name, String managementId) {
        this.id = id;
        this.name = name;
        this.managementId = managementId;
    }

    public CheckInTimeNotNull(String name, String startPunchIn, String endPunchIn, String startClockOut, String endClockOut, String managementId) {
        this.name = name;
        this.startPunchIn = startPunchIn;
        this.endPunchIn = endPunchIn;
        this.startClockOut = startClockOut;
        this.endClockOut = endClockOut;
        this.managementId = managementId;
    }
}
