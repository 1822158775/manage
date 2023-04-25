package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/4/19
 */

@Data
@ToString
public class DataStatisticsLastMonth implements Serializable {

    public Integer activation;//上月批核已激活数据
    public Integer approved;//上月批核未激活
    public Integer artificial;//上月转人工
    public Integer refuse;//上月拒绝
    public Integer all;//上月所有数据
}
