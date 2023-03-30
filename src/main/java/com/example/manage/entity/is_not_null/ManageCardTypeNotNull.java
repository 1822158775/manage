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
 * @date 2023-03-30 16:23:53
 * 项目关联卡种
 */

@Data
@ToString
public class ManageCardTypeNotNull implements Serializable {
    public String id;//数据编码
    public String manageId;//项目编码
    public String cardTypeId;//卡种类型编码
}
