package com.example.manage.entity.number;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/7/3
 */

@Data
@ToString
public class ManagementPunching implements Serializable {
    public Integer id;//项目id
    public String name;//项目名称
    public String personnelId;//人员编号
    public String personnelName;//人员名称
    public Integer personnel;//项目组人数
    public Integer punchInCount;//签到人数
    public String punchInState;//签到状态
    public String clockInState;//下班签到状态
    public Integer employmentStatus;//当前人员状态
    public Integer numberOfPeople;//人的数量
    public Integer numberOfLeave;//请假数量
    public List<ManagementPunchingNumber> punchingNumberList;//各个状态的数量

    public ManagementPunching() {
    }

    public ManagementPunching(Integer id, String name, Integer punchInCount, Integer numberOfPeople, List<ManagementPunchingNumber> punchingNumberList) {
        this.id = id;
        this.name = name;
        this.punchInCount = punchInCount;
        this.numberOfPeople = numberOfPeople;
        this.punchingNumberList = punchingNumberList;
    }

    public ManagementPunching(Integer id, String name, Integer punchInCount, Integer numberOfPeople, Integer numberOfLeave, List<ManagementPunchingNumber> punchingNumberList) {
        this.id = id;
        this.name = name;
        this.punchInCount = punchInCount;
        this.numberOfPeople = numberOfPeople;
        this.numberOfLeave = numberOfLeave;
        this.punchingNumberList = punchingNumberList;
    }
}
