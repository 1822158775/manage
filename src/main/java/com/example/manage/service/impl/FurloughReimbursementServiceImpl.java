package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.FurloughReimbursement;
import com.example.manage.mapper.IFurloughReimbursementMapper;
import com.example.manage.service.IFurloughReimbursementService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-05-17 11:45:41
 * 请假审核表
 */

@Slf4j
@Service
public class FurloughReimbursementServiceImpl implements IFurloughReimbursementService {
    @Resource
    private IFurloughReimbursementMapper iFurloughReimbursementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                FurloughReimbursement jsonParam = PanXiaoZhang.getJSONParam(request, FurloughReimbursement.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                FurloughReimbursement jsonParam = PanXiaoZhang.getJSONParam(request, FurloughReimbursement.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改请假审核表
    private ReturnEntity edit(HttpServletRequest request, FurloughReimbursement jsonParam) {
        int updateById = iFurloughReimbursementMapper.updateById(jsonParam);
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

    // 添加请假审核表
    private ReturnEntity add(HttpServletRequest request, FurloughReimbursement jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iFurloughReimbursementMapper.insert(jsonParam);
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iFurloughReimbursementMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iFurloughReimbursementMapper.queryCount(map));
    }
}
