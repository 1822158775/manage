package com.example.manage.mapper;


import com.example.manage.entity.FurloughReimbursement;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/17
 */

public interface WhiteFurloughReimbursementMapper {
    List<FurloughReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
    Integer queryMax(Map map);
}
