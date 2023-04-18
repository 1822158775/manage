package com.example.manage.white_list.service.impl;

import com.example.manage.entity.ManageBankAccount;
import com.example.manage.entity.is_not_null.ManageBankAccountNotNull;
import com.example.manage.mapper.IManageBankAccountMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageBankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/12
 */

@Slf4j
@Service
public class WhiteManageBankAccountServiceImpl implements IWhiteManageBankAccountService {

    @Resource
    private IManageBankAccountMapper iManageBankAccountMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                ManageBankAccount jsonParam = PanXiaoZhang.getJSONParam(request, ManageBankAccount.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                ManageBankAccount jsonParam = PanXiaoZhang.getJSONParam(request, ManageBankAccount.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    private ReturnEntity edit(HttpServletRequest request, ManageBankAccount jsonParam) {
        return null;
    }

    //添加自己的银行卡号
    private ReturnEntity add(HttpServletRequest request, ManageBankAccount jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageBankAccountNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //将id设置为空
        jsonParam.setId(null);
        //进行添加数据
        int insert = iManageBankAccountMapper.insert(jsonParam);
        if (insert != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"数据添加失败");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"添加成功");
    }

    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iManageBankAccountMapper.queryAll(map),"");
    }
}
