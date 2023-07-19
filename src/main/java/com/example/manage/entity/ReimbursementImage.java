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
 * @date 2023-04-11 15:34:07
 * 图片存储
 */

@Data
@ToString
@TableName(value = "reimbursement_image")
public class ReimbursementImage implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "path_url")
    public String pathUrl;//路径
    @TableField(value = "path_code")
    public String pathCode;//代码
    @TableField(value = "http_url")
    public String httpUrl;//前缀

    public ReimbursementImage() {
    }

    public ReimbursementImage(String pathUrl, String pathCode) {
        this.pathUrl = pathUrl;
        this.pathCode = pathCode;
    }

    public ReimbursementImage(String pathUrl, String pathCode, String httpUrl) {
        this.pathUrl = pathUrl;
        this.pathCode = pathCode;
        this.httpUrl = httpUrl;
    }
}
