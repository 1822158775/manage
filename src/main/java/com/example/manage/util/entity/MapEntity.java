package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 */

@Data
@ToString
public class MapEntity {
    public String key;
    public String value;
    public Integer count;

    public MapEntity() {
    }

    public MapEntity(String key, String value, Integer count) {
        this.key = key;
        this.value = value;
        this.count = count;
    }
}
