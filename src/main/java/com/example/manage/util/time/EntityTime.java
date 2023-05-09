package com.example.manage.util.time;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * @avthor 潘小章
 * @date 2023/5/8
 */

@Data
@ToString
public class EntityTime implements Serializable {
    public LocalTime workStartTime;
    public LocalTime workEndTime;

    public EntityTime() {
    }

    public EntityTime(LocalTime workStartTime, LocalTime workEndTime) {
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
    }
}
