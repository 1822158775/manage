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
 * @date 2023-04-11 15:27:20
 * 报销记录关联项目
 */

@Data
@ToString
public class ReimbursementProjectNotNull implements Serializable {
    public String id;//数据编码
    public String name;//项目名称
    public String reimbursementRecordCode;//报销记录编码
    public String manageManagementId;//项目
}
