package com.example.manage.entity;

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
@TableName(value = "sys_personnel")
public class SysPersonnel {
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
    public Boolean employmentStatus;//任职状态(true：在职，false：离职)
    @TableField(value = "chief_steward_id")
    public Integer chiefStewardId;//上级领导编码
    @TableField(value = "salary")
    public String salary;//工资
    @TableField(value = "management_id")
    public Integer managementId;//项目编码
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
    @TableField(exist = false)
    public SysRole sysRole;//权限名称
    @TableField(exist = false)
    public SysManagement sysManagement;//项目组名称

    public SysPersonnel() {
    }

    public SysPersonnel(Integer id) {
        this.id = id;
    }
}
