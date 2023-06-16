package com.example.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.CardType;
import com.example.manage.entity.ManageCardType;
import com.example.manage.entity.SysManagement;
import com.example.manage.entity.is_not_null.SysManagementNotNull;
import com.example.manage.mapper.ICardTypeMapper;
import com.example.manage.mapper.IManageCardTypeMapper;
import com.example.manage.mapper.ISysManagementMapper;
import com.example.manage.service.ISysManagementService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Slf4j
@Service
public class SysManagementServiceImpl implements ISysManagementService {
    @Resource
    private ISysManagementMapper iSysManagementMapper;
    @Resource
    private ICardTypeMapper iCardTypeMapper;
    @Resource
    private IManageCardTypeMapper iManageCardTypeMapper;
    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                ReturnEntity cat = cat(request);
                return cat;
            }else if (name.equals("add")){
                SysManagement jsonParam = PanXiaoZhang.getJSONParam(request, SysManagement.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysManagement jsonParam = PanXiaoZhang.getJSONParam(request, SysManagement.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //编辑板块
    private ReturnEntity edit(HttpServletRequest request,SysManagement jsonParam) {
        //此处判断id不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysManagementNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //通过id查询数据是否存在
        SysManagement sysManagement = iSysManagementMapper.selectById(jsonParam.getId());
        if (ObjectUtils.isEmpty(sysManagement)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "该数据" + sysManagement.getName() + "不存在"
            );
        }
        //通过名称查询数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        SysManagement management = iSysManagementMapper.selectOne(wrapper);
        //将可用余额设置为空，进行不修改操作
        jsonParam.setAvailableBalance(null);
        //如果这条数据不存在并且两条数据id相同则返回项目名称已存在
        if (!ObjectUtils.isEmpty(management) && !management.getId().equals(sysManagement.getId())){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "项目名称" + management.getName() + "已存在"
            );
        }
        //编辑关联卡种表单
        if (!ObjectUtils.isEmpty(jsonParam.getIntegers()) && !ObjectUtils.isEmpty(sysManagement)){
            //用于存储数据
            Map<Integer, ManageCardType> map = new HashMap<>();
            //查询当前相关的卡种
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("manage_code",sysManagement.getManagementCode());
            List<ManageCardType> list = iManageCardTypeMapper.selectList(queryWrapper);
            for (int i = 0; i < list.size(); i++) {
                ManageCardType manageCardType = list.get(i);
                map.put(manageCardType.getCardTypeId(),manageCardType);
            }
            //遍历选中的卡种
            for (int i = 0; i < jsonParam.getIntegers().length; i++) {
                Integer integer = jsonParam.getIntegers()[i];
                map.put(integer * -1,new ManageCardType(
                        "",
                        integer
                ));
            }
            //遍历所有卡种
            List<CardType> cardTypes = iCardTypeMapper.selectList(null);
            for (int i = 0; i < cardTypes.size(); i++) {
                CardType cardType = cardTypes.get(i);
                ManageCardType manageCardType = map.get(cardType.getId());
                ManageCardType sManageCardType = map.get(cardType.getId() * -1);
                //符合(sManageCardType不为空但manageCardType为空)该条件则为添加，反之为删除
                if (!ObjectUtils.isEmpty(sManageCardType) && ObjectUtils.isEmpty(manageCardType)){
                    iManageCardTypeMapper.insert(new ManageCardType(
                        sysManagement.getManagementCode(),
                        cardType.getId()
                    ));
                }else if (ObjectUtils.isEmpty(sManageCardType)&& !ObjectUtils.isEmpty(manageCardType)){
                    iManageCardTypeMapper.deleteById(manageCardType.getId());
                }
            }
        }
        //如果没有问题将进行数据修改
        int updateById = iSysManagementMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        //至此业务逻辑执行成功
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED
        );
    }

    //添加板块
    private ReturnEntity add(HttpServletRequest request,SysManagement jsonParam) {
        //判断那些字段不可为空
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysManagementNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        ""
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断是否有重复的项目名称
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        SysManagement sysManagement = iSysManagementMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(sysManagement)){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "项目名称" + sysManagement.getName() + "已存在"
            );
        }
        //给项目设置代码
        jsonParam.setManagementCode(System.currentTimeMillis() + PanXiaoZhang.ran(4));
        //关联卡种数组如果有值则进行添加
        if (!ObjectUtils.isEmpty(jsonParam.getIntegers())){
            for (int i = 0; i < jsonParam.getIntegers().length; i++) {
                Integer integer = jsonParam.getIntegers()[i];
                //添加项目关联卡种水
                int insert = iManageCardTypeMapper.insert(new ManageCardType(
                        jsonParam.getManagementCode(),
                        integer
                ));
                if (insert != 1){
                    return new ReturnEntity(
                            CodeEntity.CODE_ERROR,
                            jsonParam,
                            "第" + (i + 1) + "个卡种添加失败"
                    );
                }
            }
        }
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iSysManagementMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        //到此业务逻辑结束
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED
        );
    }

    // 查询模块
    public ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        List<SysManagement> managements = iSysManagementMapper.queryAll(map);
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                managements,
                iCardTypeMapper.selectList(null),
                request,
                MsgEntity.CODE_SUCCEED,
                iSysManagementMapper.queryCount(map));
    }
}
