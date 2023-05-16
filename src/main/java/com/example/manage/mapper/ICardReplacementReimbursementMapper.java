package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.CardReplacementReimbursement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-05-16 10:30:12
 * 补卡审核人
 */

public interface ICardReplacementReimbursementMapper extends BaseMapper<CardReplacementReimbursement> {
    List<CardReplacementReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
}
