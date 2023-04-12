package com.example.manage.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.manage.entity.ManageBankAccount;

import java.util.List;
import java.util.Map;
/**
 * @avthor 潘小章
 * @date 2023-04-11 16:02:00
 * 银行账户管理
 */

public interface IManageBankAccountMapper extends BaseMapper<ManageBankAccount> {
    List<ManageBankAccount> queryAll(Map map);
    Integer queryCount(Map map);
}
