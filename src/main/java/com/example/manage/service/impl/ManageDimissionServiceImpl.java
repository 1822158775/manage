package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.ManageDimissionNotNull;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.ISysRoleMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.ManageDimission;
import com.example.manage.mapper.IManageDimissionMapper;
import com.example.manage.service.IManageDimissionService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-03-29 11:16:51
 * 离职申请管理
 */

@Slf4j
@Service
public class ManageDimissionServiceImpl implements IManageDimissionService {

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    // 修改离职申请管理
    private ReturnEntity edit(HttpServletRequest request, ManageDimission jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageDimissionNotNull(
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断状态是否待审批
        if (jsonParam.getApplicantState() != null){
            ManageDimission manageDimission = iManageDimissionMapper.selectById(jsonParam.getId());
            if (!manageDimission.getApplicantState().equals("pending")){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        MsgEntity.CODE_ERROR
                );
            }
            if (jsonParam.getApplicantState().equals("agree")){
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("personnel_code",manageDimission.getPersonnelCode());
                iSysPersonnelMapper.update(new SysPersonnel(
                0,
                            new Date()
                ),
                wrapper);
            }
        }
        int updateById = iManageDimissionMapper.updateById(new ManageDimission(
            jsonParam.getId(),
            jsonParam.getApplicantState()
        ));
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

    // 添加离职申请管理
    private ReturnEntity add(HttpServletRequest request, ManageDimission jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iManageDimissionMapper.insert(jsonParam);
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iManageDimissionMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iManageDimissionMapper.queryCount(map));
    }
}
