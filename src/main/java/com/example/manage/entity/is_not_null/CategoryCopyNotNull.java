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
 * @date 2023-04-10 18:28:40
 * 类目关联抄送人管理
 */

@Data
@ToString
public class CategoryCopyNotNull implements Serializable {
    public String id;//数据编码
    public String reimbursementCode;//类目编码
    public String roleId;//审批人编码
}
