package com.example.manage.white_list.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.manage.entity.SignInReview;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhitePunchingCardRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @avthor 潘小章
 * @date 2023/5/5
 * 打卡记录表
 */

@RestController
@RequestMapping(value = "/api/white_list/punching_card_record/")
public class WhitePunchingCardRecordController {

    @Resource
    private IWhitePunchingCardRecordService iWhitePunchingCardRecordService;

    // 添加打卡记录表
    @PostMapping(value = "add")
    public ReturnEntity add(HttpServletRequest request){
        synchronized (this.getClass()){
            return iWhitePunchingCardRecordService.methodMasterT(request,"add");
        }
    }

    // 查询当天打卡记录
    @PostMapping(value = "cat")
    public ReturnEntity cat(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"cat");
    }

    // 查询指定日期的打卡记录
    @PostMapping(value = "cat_day")
    public ReturnEntity cat_day(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"cat_day");
    }

    // 查询历史打卡记录
    @PostMapping(value = "cat_list")
    public ReturnEntity cat_list(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"cat_list");
    }

    // 查询是否在位置
    @PostMapping(value = "area")
    public ReturnEntity area(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"area");
    }

    // 查询该部门打卡情况
    @PostMapping(value = "clocking_situation")
    public ReturnEntity clocking_situation(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"clocking_situation");
    }

    // 查询该部门人员打卡情况
    @PostMapping(value = "clocking_situation_particulars")
    public ReturnEntity clocking_situation_particulars(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMaster(request,"clocking_situation_particulars");
    }

    // 视频打卡
    @PostMapping(value = "video_check_in")
    public ReturnEntity video_check_in(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMasterT(request,"video_check_in");
    }

    // 提交视频签到
    @PostMapping(value = "video_check_in_add")
    public ReturnEntity video_check_in_add(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMasterT(request,"video_check_in_add");
    }

    // 查询视频签到申请列表
    @PostMapping(value = "video_check_in_cat")
    public ReturnEntity video_check_in_cat(HttpServletRequest request){
        return iWhitePunchingCardRecordService.methodMasterT(request,"video_check_in_cat");
    }

    // 审核视频签到
    @PostMapping(value = "video_check_in_edit")
    public ReturnEntity video_check_in_edit(HttpServletRequest request) {
        return iWhitePunchingCardRecordService.methodMasterT(request, "video_check_in_edit");
    }
    public static void main(String[] args) {
        //BigDecimal originalValue = new BigDecimal("123.0000");
        //BigDecimal roundedValue = originalValue.setScale(4, RoundingMode.HALF_UP);
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("roundedValue",roundedValue);
        //System.out.println(JSON.toJSON(jsonObject)); // 输出: 123.4567
        SignInReview signInReview = new SignInReview();
        signInReview.setBigDecimal(BigDecimal.valueOf(123.0000));
        System.out.println(JSON.toJSONString(signInReview));
        String toJSONString = JSON.toJSONString(signInReview);

        SignInReview inReview = JSONObject.parseObject(toJSONString, SignInReview.class);
        System.out.println(inReview.getBigDecimal());
    }
}
