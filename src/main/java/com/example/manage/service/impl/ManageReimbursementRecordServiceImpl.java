package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.ManageReimbursementRecord;
import com.example.manage.mapper.IManageReimbursementRecordMapper;
import com.example.manage.service.IManageReimbursementRecordService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-04-11 15:00:11
 * 申请报销记录管理
 */

@Slf4j
@Service
public class ManageReimbursementRecordServiceImpl implements IManageReimbursementRecordService {
    @Resource
    private IManageReimbursementRecordMapper iManageReimbursementRecordMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                ManageReimbursementRecord jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementRecord.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                ManageReimbursementRecord jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementRecord.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    // 修改申请报销记录管理
    private ReturnEntity edit(HttpServletRequest request, ManageReimbursementRecord jsonParam) {
        int updateById = iManageReimbursementRecordMapper.updateById(jsonParam);
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

    // 添加申请报销记录管理
    private ReturnEntity add(HttpServletRequest request, ManageReimbursementRecord jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iManageReimbursementRecordMapper.insert(jsonParam);
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iManageReimbursementRecordMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iManageReimbursementRecordMapper.queryCount(map));
    }
}
