package com.example.manage.mapper;

import com.example.manage.entity.ranking_list.RankingList;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/6/13
 */

public interface WhiteRankingListProgressBarMapper {
    List<RankingList> queryAll(Map map);
}
