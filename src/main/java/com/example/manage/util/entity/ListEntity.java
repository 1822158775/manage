package com.example.manage.util.entity;

import com.example.manage.entity.ranking_list.RankingList;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/6/20
 */
@Data
@ToString
public class ListEntity implements Serializable {
    public List<String> stringList;
    public List<RankingList> rankingList;

    public ListEntity() {
    }

    public ListEntity(List<String> stringList, List<RankingList> rankingList) {
        this.stringList = stringList;
        this.rankingList = rankingList;
    }
}
