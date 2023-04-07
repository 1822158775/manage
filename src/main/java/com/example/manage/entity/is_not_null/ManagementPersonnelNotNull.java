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
 * @date 2023-04-06 14:05:07
 * 项目关联人员
 */

@Data
@ToString
public class ManagementPersonnelNotNull implements Serializable {
    public String id;//数据编码
    public String managementId;//项目id
    public String personnelCode;//人员id

    public ManagementPersonnelNotNull() {
    }

    public ManagementPersonnelNotNull(String managementId, String personnelCode) {
        this.managementId = managementId;
        this.personnelCode = personnelCode;
    }
}
