package com.example.manage.util.entity;

import com.example.manage.entity.SysManagement;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/6/27
 */

@Data
@ToString
public class UtilEntity implements Serializable {
    public Boolean locationInRange;
    public SysManagement sysManagement;

    public UtilEntity() {
    }

    public UtilEntity(Boolean locationInRange, SysManagement sysManagement) {
        this.locationInRange = locationInRange;
        this.sysManagement = sysManagement;
    }
}
