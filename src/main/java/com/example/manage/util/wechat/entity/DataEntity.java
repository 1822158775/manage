package com.example.manage.util.wechat.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/3/28
 */

@Data
@ToString
public class DataEntity {
    //内容
    private String value;
    //字体颜色
    private String color;

    public DataEntity(String value ,String color){
        this.value = value;
        this.color = color;
    }
}
