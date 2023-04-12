package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ReimbursementImage;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 15:34:07
 * 图片存储
 */

public interface IReimbursementImageMapper extends BaseMapper<ReimbursementImage> {
    List<ReimbursementImage> queryAll(Map map);
    Integer queryCount(Map map);
}
