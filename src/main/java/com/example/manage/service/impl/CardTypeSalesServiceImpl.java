package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.is_not_null.CardTypeSalesNotNull;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.CardTypeSales;
import com.example.manage.mapper.ICardTypeSalesMapper;
import com.example.manage.service.ICardTypeSalesService;
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
 * @date 2023-06-16 15:07:18
 * 信用卡关联权益
 */

@Slf4j
@Service
public class CardTypeSalesServiceImpl implements ICardTypeSalesService {
    @Resource
    private ICardTypeSalesMapper iCardTypeSalesMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                CardTypeSales jsonParam = PanXiaoZhang.getJSONParam(request, CardTypeSales.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                CardTypeSales jsonParam = PanXiaoZhang.getJSONParam(request, CardTypeSales.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改信用卡关联权益
    private ReturnEntity edit(HttpServletRequest request, CardTypeSales jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new CardTypeSalesNotNull(
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ne("id",jsonParam.getId());
        wrapper.eq("name",jsonParam.getName());
        CardTypeSales cardTypeSales = iCardTypeSalesMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(cardTypeSales)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"名称不可重复");
        }
        int updateById = iCardTypeSalesMapper.updateById(jsonParam);
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

    // 添加信用卡关联权益
    private ReturnEntity add(HttpServletRequest request, CardTypeSales jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new CardTypeSalesNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        CardTypeSales cardTypeSales = iCardTypeSalesMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(cardTypeSales)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,jsonParam.getName() + "名称已存在");
        }
        //设置状态
        jsonParam.setState("使用中");
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iCardTypeSalesMapper.insert(jsonParam);
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
        if (ObjectUtils.isEmpty(map.get("cardTypeId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请选择卡类型");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("card_type_id",map.get("cardTypeId"));
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iCardTypeSalesMapper.selectList(wrapper),request,MsgEntity.CODE_SUCCEED);
    }
}
