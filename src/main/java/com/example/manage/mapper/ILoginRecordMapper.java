package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.LoginRecord;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-07-14 15:14:23
 * 登入表
 */

public interface ILoginRecordMapper extends BaseMapper<LoginRecord> {
    List<LoginRecord> queryAll(Map map);
    Integer queryCount(Map map);
}
