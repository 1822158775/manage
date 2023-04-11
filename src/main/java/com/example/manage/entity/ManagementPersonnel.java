package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023-04-06 14:05:06
 * 项目关联人员
 */

@Data
@ToString
@TableName(value = "management_personnel")
public class ManagementPersonnel implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "management_id")
    public Integer managementId;//项目id
    @TableField(value = "personnel_code")
    public String personnelCode;//人员资源代码

    public ManagementPersonnel() {
    }

    public ManagementPersonnel(Integer managementId) {
        this.managementId = managementId;
    }

    public ManagementPersonnel(Integer managementId, String personnelCode) {
        this.managementId = managementId;
        this.personnelCode = personnelCode;
    }
}
