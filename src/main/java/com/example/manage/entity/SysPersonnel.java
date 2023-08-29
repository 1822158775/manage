package com.example.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.manage.entity.data_statistics.Management;
import com.example.manage.entity.data_statistics.Personnel;
import com.example.manage.entity.number.WorkingAgoOpenNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Data
@ToString
@TableName(value = "sys_personnel")
public class SysPersonnel implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    public Integer id;//数据编码
    @TableField(value = "phone")
    public String phone;//手机号
    @TableField(value = "place_of_domicile")
    public String placeOfDomicile;//户籍地
    @TableField(value = "personnel_code")
    public String personnelCode;//资源代码
    @TableField(value = "username")
    public String username;//账号
    @TableField(value = "role_id")
    public Integer roleId;//角色数据编码
    @TableField(value = "password")
    public String password;//密码
    @TableField(value = "name")
    public String name;//人员名称
    @TableField(value = "position_post")
    public Integer positionPost;//任职岗位,1：业务岗，2服务岗
    @TableField(value = "image")
    public String image;//头像：链接
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "birthday")
    public Date birthday;//生日
    @TableField(value = "employment_status")
    public Integer employmentStatus;//任职状态(1：在职，0：离职,2:待入职)
    @TableField(value = "chief_steward_id")
    public Integer chiefStewardId;//上级领导编码
    @TableField(value = "salary")
    public String salary;//工资
    @TableField(value = "sex")
    public String sex;//性别
    @TableField(value = "standby_application")
    public String standbyApplication;//备用
    @TableField(value = "official_or_trainee_staff")
    public Integer officialOrTraineeStaff;//1：正式职员，2：实习职员
    @TableField(value = "social_security_payment")
    public Integer socialSecurityPayment;//1：正常缴纳，2：未缴纳
    @TableField(value = "commercial_insurance")
    public Integer commercialInsurance;//1：正常缴纳商业保险，2：未缴纳商业保险
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "entry_time")
    public Date entryTime;//入职时间
    @TableField(value = "id_number")
    public String idNumber;//身份证号
    @TableField(value = "emergency_contact_name")
    public String emergencyContactName;//紧急联系人名称
    @TableField(value = "emergency_contact_phone")
    public String emergencyContactPhone;//紧急联系人手机号
    @TableField(value = "permanent_residence")
    public String permanentResidence;//常住地
    @TableField(value = "open_id")
    public String openId;//用户openid
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "leave_time")
    public Date leaveTime;//离职时间

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "trial_date")
    public Date trialDate;//试岗时间

    @TableField(exist = false)
    public SysRole sysRole;//权限名称
    @TableField(exist = false)
    public List<SysManagement> sysManagement;//项目组名称
    @TableField(exist = false)
    public Integer[] managementId;//项目组数组
    @TableField(exist = false)
    public Integer mId;//项目组编码
    @TableField(exist = false)
    public Integer personnelId;//人员信息
    @TableField(exist = false)
    public List<PunchingCardRecord> punchingCardRecords;//打卡信息
    @TableField(exist = false)
    public List<FurloughRecord> furloughRecords;//请假信息


    @TableField(exist = false)
    public String personnelName;//主管人员名称
    @TableField(exist = false)
    public String managementName;//项目名称

    @TableField(exist = false)
    public Integer dutyDays;//执勤天数
    @TableField(exist = false)
    public Integer lateArrivals;//迟到次数
    @TableField(exist = false)
    public Integer earlyDepartures;//迟到次数
    @TableField(exist = false)
    public Integer accomodate;//缺卡次数

    @TableField(exist = false)
    public List<Personnel> personnels;//上级信息
    @TableField(exist = false)
    public List<Management> managements;//项目名称

    @TableField(exist = false)
    public String updatePassword;//密码

    @TableField(exist = false)
    public String model;//手机型号

    @TableField(exist = false)
    public WorkingAgoOpenNumber workingAgoOpenNumber;//几个不同的微信号

    public SysPersonnel() {
    }

    public SysPersonnel(Integer id) {
        this.id = id;
    }

    public SysPersonnel(Integer id, String name, String image, String openId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.openId = openId;
    }

    public SysPersonnel(Integer employmentStatus, Date leaveTime) {
        this.employmentStatus = employmentStatus;
        this.leaveTime = leaveTime;
    }

    public SysPersonnel(Integer id, Integer employmentStatus, Date leaveTime) {
        this.id = id;
        this.employmentStatus = employmentStatus;
        this.leaveTime = leaveTime;
    }

    public SysPersonnel(Integer id, String phone, String password, String name, Date birthday, String sex, String idNumber) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.idNumber = idNumber;
    }

    public SysPersonnel(Integer id, String phone, String placeOfDomicile, String password, String name, Integer positionPost, String image, Date birthday, String sex, Integer officialOrTraineeStaff, Integer socialSecurityPayment, Integer commercialInsurance, String idNumber, String emergencyContactName, String emergencyContactPhone, String permanentResidence) {
        this.id = id;
        this.phone = phone;
        this.placeOfDomicile = placeOfDomicile;
        this.password = password;
        this.name = name;
        this.positionPost = positionPost;
        this.image = image;
        this.birthday = birthday;
        this.sex = sex;
        this.officialOrTraineeStaff = officialOrTraineeStaff;
        this.socialSecurityPayment = socialSecurityPayment;
        this.commercialInsurance = commercialInsurance;
        this.idNumber = idNumber;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.permanentResidence = permanentResidence;
    }

    public SysPersonnel(Integer id, String phone, String placeOfDomicile, String password, String name, Integer positionPost, String image, Date birthday, String sex, Integer officialOrTraineeStaff, Integer socialSecurityPayment, Integer commercialInsurance, String idNumber, String emergencyContactName, String emergencyContactPhone, String permanentResidence, String standbyApplication) {
        this.id = id;
        this.phone = phone;
        this.placeOfDomicile = placeOfDomicile;
        this.password = password;
        this.name = name;
        this.positionPost = positionPost;
        this.image = image;
        this.birthday = birthday;
        this.sex = sex;
        this.officialOrTraineeStaff = officialOrTraineeStaff;
        this.socialSecurityPayment = socialSecurityPayment;
        this.commercialInsurance = commercialInsurance;
        this.idNumber = idNumber;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.permanentResidence = permanentResidence;
        this.standbyApplication = standbyApplication;
    }

    public SysPersonnel(Integer id, String phone, String placeOfDomicile, String personnelCode, String username, Integer roleId, String password, String name, Integer positionPost, String image, Date birthday, Integer employmentStatus, Integer chiefStewardId, String salary, String sex, String standbyApplication, Integer officialOrTraineeStaff, Integer socialSecurityPayment, Integer commercialInsurance, Date entryTime, String idNumber, String emergencyContactName, String emergencyContactPhone, String permanentResidence, String openId, Date leaveTime) {
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
        this.sex = sex;
        this.standbyApplication = standbyApplication;
        this.officialOrTraineeStaff = officialOrTraineeStaff;
        this.socialSecurityPayment = socialSecurityPayment;
        this.commercialInsurance = commercialInsurance;
        this.entryTime = entryTime;
        this.idNumber = idNumber;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.permanentResidence = permanentResidence;
        this.openId = openId;
        this.leaveTime = leaveTime;
    }
}
