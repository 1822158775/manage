package com.example.manage.entity.number;

import lombok.Data;
import lombok.ToString;

/**
 * @avthor 潘小章
 * @date 2023/7/3
 */
@Data
@ToString
public class ManagementPunchingNumber {
    public Integer id;//项目编码
    public String punchInState;//签到状态
    public Integer number;//数量

    public ManagementPunchingNumber() {
    }

    public ManagementPunchingNumber(Integer id, String punchInState, Integer number) {
        this.id = id;
        this.punchInState = punchInState;
        this.number = number;
    }
}
