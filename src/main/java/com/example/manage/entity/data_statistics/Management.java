package com.example.manage.entity.data_statistics;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/5/9
 */
@Data
@ToString
public class Management {
    public String name;

    public Management() {
    }

    public Management(String name) {
        this.name = name;
    }
}
