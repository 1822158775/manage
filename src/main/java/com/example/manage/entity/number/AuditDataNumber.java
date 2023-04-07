package com.example.manage.entity.number;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/4/4
 */
@Data
@ToString
public class AuditDataNumber {
    public Integer all;//全部数量
    public Integer pending;//待审核
    public Integer agree;//审核通过
    public Integer refuse;//审核拒绝
}
