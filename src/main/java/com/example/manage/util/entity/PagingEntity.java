package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2022/4/15
 */
@Data
@ToString
public class PagingEntity {
    public Integer index;
    public Integer pageNum;

    public PagingEntity() {
    }

    public PagingEntity(Integer index, Integer pageNum) {
        this.index = index;
        this.pageNum = pageNum;
    }
}
