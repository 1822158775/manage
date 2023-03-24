package com.example.manage.entity.is_not_null;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/3/24
 */
@Data
@ToString
public class SysAuditManagementNotNull implements Serializable {
    public String id;//数据编码
    public String username;//审核人账号
    public String managementId;//项目唯一标识
    public String reimbursementId;//类目唯一标识

    public SysAuditManagementNotNull() {
    }

    public SysAuditManagementNotNull(String id) {
        this.id = id;
    }

    public SysAuditManagementNotNull(String username, String managementId, String reimbursementId) {
        this.username = username;
        this.managementId = managementId;
        this.reimbursementId = reimbursementId;
    }
}
