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
 * @date 2023-04-10 18:27:51
 * 类目关联审批人管理
 */

@Data
@ToString
public class ManageRMNotNull implements Serializable {
    public String id;//数据编码
    public String reimbursementCode;//类目编码
    public String roleId;//审批人编码
}
