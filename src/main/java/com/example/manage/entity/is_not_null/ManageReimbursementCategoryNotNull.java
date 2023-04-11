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
 * @date 2023-04-10 18:23:08
 * 类目管理
 */

@Data
@ToString
public class ManageReimbursementCategoryNotNull implements Serializable {
    public String id;//数据编码
    public String name;//类目名称
    public String reimbursementType;//固定(gd),可变(kb)
    public String amount;//金额
    public String particularConditions;//是否有特殊条件：0没有，1有
    public String categoryCoding;//类目编码
    public String categoryCopyNumber;//关联抄送人
    public String manageRm;//关联审核人

    public ManageReimbursementCategoryNotNull() {
    }

    public ManageReimbursementCategoryNotNull(String id) {
        this.id = id;
    }

    public ManageReimbursementCategoryNotNull(String name, String reimbursementType, String amount, String particularConditions, String categoryCopyNumber, String manageRm) {
        this.name = name;
        this.reimbursementType = reimbursementType;
        this.amount = amount;
        this.particularConditions = particularConditions;
        this.categoryCopyNumber = categoryCopyNumber;
        this.manageRm = manageRm;
    }
}
