package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ReimbursementCopy;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 15:25:56
 * 报销记录抄送人
 */

public interface IReimbursementCopyMapper extends BaseMapper<ReimbursementCopy> {
    List<ReimbursementCopy> queryAll(Map map);
    Integer queryCount(Map map);
}
