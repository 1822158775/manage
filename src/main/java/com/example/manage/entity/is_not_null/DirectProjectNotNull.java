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
 * @date 2023-06-25 17:25:35
 * 部门直管项目表
 */

@Data
@ToString
public class DirectProjectNotNull implements Serializable {
    public String id;//数据编码
    public String divisionManagementId;//部门id
    public String managementId;//项目id
}
