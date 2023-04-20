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
public class DataStatisticsTodayYester implements Serializable {

    public Integer activation;//昨日激活数据
    public Integer approved;//昨日批核
    public Integer artificial;//昨日转人工
    public Integer refuse;//昨日拒绝
    public Integer all;//昨日所有数据
}
