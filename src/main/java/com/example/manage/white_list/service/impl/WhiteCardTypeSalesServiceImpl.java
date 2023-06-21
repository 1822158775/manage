package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.CardTypeSales;
import com.example.manage.entity.is_not_null.CardTypeSalesNotNull;
import com.example.manage.mapper.ICardTypeSalesMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteCardTypeSalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @avthor 潘小章
 * @date 2023/6/19
 * 信用卡关联权益
 */

@Slf4j
@Service
public class WhiteCardTypeSalesServiceImpl implements IWhiteCardTypeSalesService {
    @Resource
    private ICardTypeSalesMapper iCardTypeSalesMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    private ReturnEntity cat(HttpServletRequest request) throws IOException {
        CardTypeSales jsonParam = PanXiaoZhang.getJSONParam(request, CardTypeSales.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new CardTypeSalesNotNull(
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        ""
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if (!ObjectUtils.isEmpty(jsonParam.getState())){
            wrapper.eq("state",jsonParam.getState());
        }else {
            wrapper.eq("state","使用");
        }
        wrapper.eq("card_type_id",jsonParam.getCardTypeId());
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iCardTypeSalesMapper.selectList(wrapper),"");
    }
}
