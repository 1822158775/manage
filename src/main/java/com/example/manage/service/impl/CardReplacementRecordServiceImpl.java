package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.CardReplacementRecord;
import com.example.manage.mapper.ICardReplacementRecordMapper;
import com.example.manage.service.ICardReplacementRecordService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-05-16 09:47:03
 * 补卡
 */

@Slf4j
@Service
public class CardReplacementRecordServiceImpl implements ICardReplacementRecordService {
    @Resource
    private ICardReplacementRecordMapper iCardReplacementRecordMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                CardReplacementRecord jsonParam = PanXiaoZhang.getJSONParam(request, CardReplacementRecord.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                CardReplacementRecord jsonParam = PanXiaoZhang.getJSONParam(request, CardReplacementRecord.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改补卡
    private ReturnEntity edit(HttpServletRequest request, CardReplacementRecord jsonParam) {
        int updateById = iCardReplacementRecordMapper.updateById(jsonParam);
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

    // 添加补卡
    private ReturnEntity add(HttpServletRequest request, CardReplacementRecord jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iCardReplacementRecordMapper.insert(jsonParam);
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iCardReplacementRecordMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iCardReplacementRecordMapper.queryCount(map));
    }
}
