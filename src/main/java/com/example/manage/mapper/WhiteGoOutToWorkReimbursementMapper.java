package com.example.manage.mapper;


import com.example.manage.entity.GoOutToWorkReimbursement;

import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/6/26
 */

public interface WhiteGoOutToWorkReimbursementMapper {
    List<GoOutToWorkReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
    Integer queryMax(Map map);
}
