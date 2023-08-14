package com.example.manage.white_list.service.impl;

import com.example.manage.entity.FeedbackRecordSheet;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.FeedbackRecordSheetNotNull;
import com.example.manage.mapper.IFeedbackRecordSheetMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.controller.WhiteFeedbackRecordSheetController;
import com.example.manage.white_list.service.IWhiteFeedbackRecordSheetService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/8/1
 * 记录反馈表单
 */
@Service
@Slf4j
public class WhiteFeedbackRecordSheetServiceImpl implements IWhiteFeedbackRecordSheetService {

    @Resource
    private IFeedbackRecordSheetMapper iFeedbackRecordSheetMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                FeedbackRecordSheet jsonParam = PanXiaoZhang.getJSONParam(request, FeedbackRecordSheet.class);
                return add(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    //提交记录表单
    private ReturnEntity add(HttpServletRequest request, FeedbackRecordSheet jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new FeedbackRecordSheetNotNull(
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        ""
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        jsonParam.setSubmissionTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        int insert = iFeedbackRecordSheetMapper.insert(jsonParam);
        if (insert != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"提交失败");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"提交成功");
    }

    //查询记录表单
    private ReturnEntity cat(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        SysPersonnel personnel = iSysPersonnelMapper.selectById(String.valueOf(jsonMap.get("personnelId")));
        if (!personnel.getPhone().equals("13141427399")){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"查询失败");
        }
        List<FeedbackRecordSheet> feedbackRecordSheets = iFeedbackRecordSheetMapper.queryAll(jsonMap);
        ReturnEntity returnEntity = new ReturnEntity(CodeEntity.CODE_SUCCEED, feedbackRecordSheets, "提交成功");
        returnEntity.setCount(iFeedbackRecordSheetMapper.queryCount(jsonMap));
        return returnEntity;
    }
}
