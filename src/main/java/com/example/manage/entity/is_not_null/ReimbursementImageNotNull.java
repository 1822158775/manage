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
 * @date 2023-04-11 15:34:07
 * 图片存储
 */

@Data
@ToString
public class ReimbursementImageNotNull implements Serializable {
    public String pathUrl;//路径
    public String pathCode;//代码
    public String httpUrl;//前缀
}
