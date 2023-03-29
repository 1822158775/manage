package com.example.manage.service.impl;

import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.service.ISysPersonnelService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/23
 */
@Slf4j
@Service
public class SysPersonnelServiceImpl implements ISysPersonnelService {
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (IOException ioException) {
            log.info("捕获异常方法{},捕获异常{}",name,ioException.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, ioException.getMessage());
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    //修改人员信息
    private ReturnEntity edit(HttpServletRequest request, SysPersonnel jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        if (!ObjectUtils.isEmpty(jsonParam.getPassword())){
            //密码进行加密
            jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        }
        int updateById = iSysPersonnelMapper.updateById(jsonParam);
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED

        );
    }


    //添加人员信息
    private ReturnEntity add(HttpServletRequest request, SysPersonnel jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        jsonParam.setId(null);
        //密码进行加密
        jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        int insert = iSysPersonnelMapper.insert(jsonParam);
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                jsonParam,
                request,
                MsgEntity.CODE_SUCCEED
        );
    }

    //查询人员信息
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                iSysPersonnelMapper.queryAll(map),
                request,
                MsgEntity.CODE_SUCCEED,
                iSysPersonnelMapper.queryCount(map));
    }
}
