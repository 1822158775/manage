package com.example.manage.mapper;


import com.example.manage.entity.ranking_list.RankingList;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/20
 */

public interface WhiteRankingListMapper {
    List<RankingList> queryAll(Map map);
    List<RankingList> queryCount(Map map);
    List<RankingList> queryAllCount(Map map);
    List<RankingList> queryPunchingCount(Map map);
    List<RankingList> queryAllCountPersonnel(Map map);
}
