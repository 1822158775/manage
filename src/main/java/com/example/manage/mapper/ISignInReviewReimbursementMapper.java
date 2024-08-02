package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SignInReviewReimbursement;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2024-08-02 11:00:15
 * 视频签到审核表
 */

public interface ISignInReviewReimbursementMapper extends BaseMapper<SignInReviewReimbursement> {
    List<SignInReviewReimbursement> queryAll(Map map);
    Integer queryCount(Map map);
    Integer queryMax(Map map);
}
