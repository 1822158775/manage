package com.example.manage.entity.ranking_list;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @avthor 潘小章
 * @date 2023/4/20
 */

@Data
@ToString
public class RankingList implements Serializable {
    public Integer countNumber;//项目数据数量
    public String name;//项目名称
    public String cardTypeName;//卡名称名称
    public String personnelName;//人员名称
    public String reportStatus;//状态名称
    public String alreadyReportStatus;//已拥有的状态名称
    public Integer activation;//激活
    public Integer approved;//批核
    public Integer artificial;//转人工
    public Integer refuse;//拒绝
}
