package com.example.manage.entity.is_not_null;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Data
@ToString
public class SysPersonnelNotNull {
    public String id;//数据编码
    public String phone;//手机号
    public String placeOfDomicile;//户籍地
    public String personnelCode;//资源代码
    public String username;//账号
    public String roleId;//角色数据编码
    public String password;//密码
    public String name;//人员名称
    public String positionPost;//任职岗位,1：业务岗，2服务岗
    public String image;//头像：链接
    public String birthday;//生日
    public String employmentStatus;//任职状态(true：在职，false：离职)
    public String chiefStewardId;//上级领导编码
    public String salary;//工资
    public String standbyApplication;//备用
    public String officialOrTraineeStaff;//1：正式职员，2：实习职员
    public String socialSecurityPayment;//1：正常缴纳，2：未缴纳
    public String commercialInsurance;//1：正常缴纳商业保险，2：未缴纳商业保险
    public String entryTime;//入职时间
    public String idNumber;//身份证号
    public String emergencyContactName;//紧急联系人名称
    public String emergencyContactPhone;//紧急联系人手机号
    public String permanentResidence;//常住地

    public SysPersonnelNotNull() {
    }

    public SysPersonnelNotNull(String id) {
        this.id = id;
    }

    public SysPersonnelNotNull(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SysPersonnelNotNull(String phone, String placeOfDomicile, String personnelCode, String username, String roleId, String password, String name, String positionPost, String birthday, String employmentStatus, String salary, String managementId, String officialOrTraineeStaff, String socialSecurityPayment, String commercialInsurance, String entryTime, String idNumber, String emergencyContactName, String emergencyContactPhone, String permanentResidence) {
        this.phone = phone;
        this.placeOfDomicile = placeOfDomicile;
        this.personnelCode = personnelCode;
        this.username = username;
        this.roleId = roleId;
        this.password = password;
        this.name = name;
        this.positionPost = positionPost;
        this.birthday = birthday;
        this.employmentStatus = employmentStatus;
        this.salary = salary;
        this.officialOrTraineeStaff = officialOrTraineeStaff;
        this.socialSecurityPayment = socialSecurityPayment;
        this.commercialInsurance = commercialInsurance;
        this.entryTime = entryTime;
        this.idNumber = idNumber;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.permanentResidence = permanentResidence;
    }

    public SysPersonnelNotNull(String id, String phone, String placeOfDomicile, String personnelCode, String username, String roleId, String password, String name, String positionPost, String image, String birthday, String employmentStatus,  String salary, String standbyApplication, String officialOrTraineeStaff, String socialSecurityPayment, String commercialInsurance, String entryTime, String idNumber, String emergencyContactName, String emergencyContactPhone, String permanentResidence) {
        this.id = id;
        this.phone = phone;
        this.placeOfDomicile = placeOfDomicile;
        this.personnelCode = personnelCode;
        this.username = username;
        this.roleId = roleId;
        this.password = password;
        this.name = name;
        this.positionPost = positionPost;
        this.image = image;
        this.birthday = birthday;
        this.employmentStatus = employmentStatus;
        this.chiefStewardId = chiefStewardId;
        this.salary = salary;
        this.standbyApplication = standbyApplication;
        this.officialOrTraineeStaff = officialOrTraineeStaff;
        this.socialSecurityPayment = socialSecurityPayment;
        this.commercialInsurance = commercialInsurance;
        this.entryTime = entryTime;
        this.idNumber = idNumber;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.permanentResidence = permanentResidence;
    }
}
