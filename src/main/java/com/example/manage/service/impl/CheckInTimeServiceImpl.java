package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.is_not_null.CheckInTimeNotNull;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.CheckInTime;
import com.example.manage.mapper.ICheckInTimeMapper;
import com.example.manage.service.ICheckInTimeService;
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
 * @date 2023-05-10 17:53:30
 * 打卡时间表
 */

@Slf4j
@Service
public class CheckInTimeServiceImpl implements ICheckInTimeService {
    @Resource
    private ICheckInTimeMapper iCheckInTimeMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                CheckInTime jsonParam = PanXiaoZhang.getJSONParam(request, CheckInTime.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                CheckInTime jsonParam = PanXiaoZhang.getJSONParam(request, CheckInTime.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    // 修改打卡时间表
    private ReturnEntity edit(HttpServletRequest request, CheckInTime jsonParam) {
        int updateById = iCheckInTimeMapper.updateById(jsonParam);
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

    // 添加打卡时间表
    private ReturnEntity add(HttpServletRequest request, CheckInTime jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new CheckInTimeNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name",jsonParam.getName());
        wrapper.eq("management_id",jsonParam.getManagementId());
        CheckInTime checkInTime = iCheckInTimeMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(checkInTime)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该规则" + checkInTime.getName() + "已存在");
        }
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iCheckInTimeMapper.insert(jsonParam);
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
        if (ObjectUtils.isEmpty(map.get("managementId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非法请求");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iCheckInTimeMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iCheckInTimeMapper.queryCount(map));
    }
}
