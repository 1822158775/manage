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
 * @date 2023-10-31 09:24:57
 * 解绑记录表
 */

@Data
@ToString
public class UnbindRecordNotNull implements Serializable {
    public String id;//
    public String username;//解绑账号
    public String frontTokenCode;//解绑前秘钥
    public String afterTokenCode;//解绑后秘钥
    public String unbindingTime;//解绑时间
    public String videoUrl;//视频路径

    public UnbindRecordNotNull() {
    }

    public UnbindRecordNotNull(String id, String username, String frontTokenCode, String afterTokenCode, String unbindingTime,String videoUrl) {
        this.id = id;
        this.username = username;
        this.frontTokenCode = frontTokenCode;
        this.afterTokenCode = afterTokenCode;
        this.unbindingTime = unbindingTime;
        this.videoUrl = videoUrl;
    }
}
