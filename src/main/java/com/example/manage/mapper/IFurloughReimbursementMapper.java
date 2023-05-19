package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.FurloughReimbursement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-05-17 11:45:41
 * 请假审核表
 */

public interface IFurloughReimbursementMapper extends BaseMapper<FurloughReimbursement> {
    List<FurloughReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
}
