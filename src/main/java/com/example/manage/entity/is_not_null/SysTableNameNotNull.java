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
 * @date 2023-03-24 13:53:41
 * 数据表名称管理
 */

@Data
@ToString
public class SysTableNameNotNull implements Serializable {
public String id;//数据编码
public String name;//表的名称
}
