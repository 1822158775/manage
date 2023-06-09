package com.example.manage.util.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.manage.entity.SysRole;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023/6/8
 */
@Data
@ToString
public class GetExcelEntity implements Serializable {
    public String managementName;//项目组名称
    public String userName;//用户名称
    public Integer roleId;//角色数据编码
    public Integer positionPost;//任职岗位,1：业务岗，2服务岗
    public String personnelCode;//资源代码
    public String idNumber;//身份证号
    public String phone;//手机号
    public String eManil;//邮箱
    public String officialOrTraineeStaff;//1：正式职员，2：实习职员
    public Date entryTime;//入职时间
    public String emergencyContactName;//紧急联系人名称
    public String emergencyContactPhone;//紧急联系人手机号
    public String permanentResidence;//常住地

    public GetExcelEntity() {
    }

    public GetExcelEntity(String managementName, String userName, Integer roleId, String personnelCode, String idNumber, String phone, String eManil, String officialOrTraineeStaff, Date entryTime, String emergencyContactName, String emergencyContactPhone, String permanentResidence) {
        this.managementName = managementName;
        this.userName = userName;
        this.roleId = roleId;
        this.personnelCode = personnelCode;
        this.idNumber = idNumber;
        this.phone = phone;
        this.eManil = eManil;
        this.officialOrTraineeStaff = officialOrTraineeStaff;
        this.entryTime = entryTime;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.permanentResidence = permanentResidence;
    }
}
