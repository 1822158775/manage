package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2022/8/31
 */
@Data
@ToString
public class GetExcel implements Serializable {
    public String name;//标题
    public List<String> value;//内容

    public GetExcel() {
    }

    public GetExcel(String name, List<String> value) {
        this.name = name;
        this.value = value;
    }
}
