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
 * @date 2023-06-21 17:21:22
 * 总经理关联部门
 */

@Data
@ToString
public class DivisionPersonnelNotNull implements Serializable {
    public String id;//数据编码
    public String divisionManagementId;//部门管理id
    public String personnelId;//人员id
}
