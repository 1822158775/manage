package com.example.manage.entity.data_statistics;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/5/9
 */

@Data
@ToString
public class Personnel implements Serializable {
    public String name;//人员名称
    public String sex;//性别

    public Personnel() {
    }

    public Personnel(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }
}
