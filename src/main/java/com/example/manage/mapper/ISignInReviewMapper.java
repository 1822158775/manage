package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.SignInReview;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2024-08-02 10:59:24
 * 视频签到申请表
 */

public interface ISignInReviewMapper extends BaseMapper<SignInReview> {
    List<SignInReview> queryAll(Map map);
    Integer queryCount(Map map);
}
