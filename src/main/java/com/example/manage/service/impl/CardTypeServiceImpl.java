package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.CardType;
import com.example.manage.mapper.ICardTypeMapper;
import com.example.manage.service.ICardTypeService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-03-30 14:11:54
 * 卡种管理
 */

@Slf4j
@Service
public class CardTypeServiceImpl implements ICardTypeService {
    @Resource
    private ICardTypeMapper iCardTypeMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                CardType jsonParam = PanXiaoZhang.getJSONParam(request, CardType.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                CardType jsonParam = PanXiaoZhang.getJSONParam(request, CardType.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    // 修改卡种管理
    private ReturnEntity edit(HttpServletRequest request, CardType jsonParam) {
        int updateById = iCardTypeMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加卡种管理
    private ReturnEntity add(HttpServletRequest request, CardType jsonParam) {
        //判断当前卡种名是否存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        CardType cardType = iCardTypeMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(cardType)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    MsgEntity.CODE_ERROR
            );
        }
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iCardTypeMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iCardTypeMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iCardTypeMapper.queryCount(map));
    }
}
