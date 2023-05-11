package com.example.manage.entity.number;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/5/9
 */

@Data
@ToString
public class WorkingAgoOpenNumber implements Serializable {
    public String agoNumber;
    public String laterNumber;
}
