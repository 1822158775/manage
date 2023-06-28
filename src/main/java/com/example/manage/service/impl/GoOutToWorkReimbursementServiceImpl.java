package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.GoOutToWorkReimbursement;
import com.example.manage.mapper.IGoOutToWorkReimbursementMapper;
import com.example.manage.service.IGoOutToWorkReimbursementService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-06-26 15:54:22
 * 出差审核表
 */

@Slf4j
@Service
public class GoOutToWorkReimbursementServiceImpl implements IGoOutToWorkReimbursementService {
    @Resource
    private IGoOutToWorkReimbursementMapper iGoOutToWorkReimbursementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                GoOutToWorkReimbursement jsonParam = PanXiaoZhang.getJSONParam(request, GoOutToWorkReimbursement.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                GoOutToWorkReimbursement jsonParam = PanXiaoZhang.getJSONParam(request, GoOutToWorkReimbursement.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改出差审核表
    private ReturnEntity edit(HttpServletRequest request, GoOutToWorkReimbursement jsonParam) {
        int updateById = iGoOutToWorkReimbursementMapper.updateById(jsonParam);
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

    // 添加出差审核表
    private ReturnEntity add(HttpServletRequest request, GoOutToWorkReimbursement jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iGoOutToWorkReimbursementMapper.insert(jsonParam);
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iGoOutToWorkReimbursementMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iGoOutToWorkReimbursementMapper.queryCount(map));
    }
}
