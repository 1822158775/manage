package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-06-19 11:30:35
 * 部门管理表
 */

@Data
@ToString
public class DivisionManagementNotNull implements Serializable {
    public String id;//部门编码
    public String name;//部门名称
    public String type;//部门类型
}
