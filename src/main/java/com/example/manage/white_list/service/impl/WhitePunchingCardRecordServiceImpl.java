package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.PunchingCardRecordNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.Token;
import com.example.manage.white_list.service.IWhitePunchingCardRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/5/5
 */

@Slf4j
@Service
public class WhitePunchingCardRecordServiceImpl implements IWhitePunchingCardRecordService {

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${role.manage}")
    private Integer manage;

    @Resource
    private IPunchingCardRecordMapper iPunchingCardRecordMapper;

    @Resource
    private IWhitePunchingCardRecordMapper iWhitePunchingCardRecordMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private ICheckInTimeMapper iCheckInTimeMapper;

    //方法总管外加事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("edit")){
                ReturnEntity returnEntity = edit(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("add")){
                ReturnEntity returnEntity = add(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("area")){
                return area(request);
            }else if (name.equals("cat_list")){
                return cat_list(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    //查看当前人员打卡历史记录
    private ReturnEntity cat_list(HttpServletRequest request) {
        Map jsonMap = PanXiaoZhang.getJsonMap(request);
        SysPersonnel personnel = iSysPersonnelMapper.selectById(String.valueOf(jsonMap.get("personnelId")));
        jsonMap.put("personnelCode",personnel.getPersonnelCode());
        List<PunchingCardRecord> punchingCardRecords = iWhitePunchingCardRecordMapper.queryAll(jsonMap);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,punchingCardRecords,"");
    }

    //查询是否在打卡范围
    private ReturnEntity area(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断账户是否正常
        ReturnEntity entity = PanXiaoZhang.estimateState(personnel);
        if (entity.getState()){
            return entity;
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员未关联项目组");
        }
        ManagementPersonnel managementPersonnel = selectList.get(0);
        SysManagement management = iSysManagementMapper.selectById(managementPersonnel.getManagementId());
        //获取项目打卡坐标
        String[] splitSouthLatitude = management.getSouthLatitude().replaceAll("，",",").split(",");
        String[] splitNorthernLatitude = management.getNorthernLatitude().replaceAll("，",",").split(",");
        String[] splitEastLongitude = management.getEastLongitude().replaceAll("，",",").split(",");
        String[] splitWestLongitude = management.getWestLongitude().replaceAll("，",",").split(",");
        //判断项目的坐标是否有误
        if (
                splitEastLongitude.length < 2 ||
                        splitNorthernLatitude.length < 2 ||
                        splitSouthLatitude.length < 2 ||
                        splitWestLongitude.length < 2
        ){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目坐标有误,请联系管理员");
        }
        //获取当前用户的地址
        double[] user = {jsonParam.getX(),jsonParam.getY()};
        //获取项目的X轴的坐标
        double[] polyX = {
                PanXiaoZhang.stringDouble(splitEastLongitude[0])
                ,PanXiaoZhang.stringDouble(splitSouthLatitude[0])
                ,PanXiaoZhang.stringDouble(splitNorthernLatitude[0])
                ,PanXiaoZhang.stringDouble(splitWestLongitude[0])
        };
        //获取项目的Y轴的坐标
        double[] polyY = {
                PanXiaoZhang.stringDouble(splitEastLongitude[1]),
                PanXiaoZhang.stringDouble(splitSouthLatitude[1]),
                PanXiaoZhang.stringDouble(splitNorthernLatitude[1]),
                PanXiaoZhang.stringDouble(splitWestLongitude[1])};
        //判断是否在范围内
        boolean locationInRange = PanXiaoZhang.isLocationInRange(user, polyX, polyY);
        //返回提示语
        String msg = "";
        if (locationInRange){
            msg = "您已进入打卡范围";
        }else {
            msg = "未进入打卡范围";
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,locationInRange,msg);
    }

    //查询当天打卡记录
    private ReturnEntity cat(HttpServletRequest request) throws IOException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        ""
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断账户是否正常
        ReturnEntity entity = PanXiaoZhang.estimateState(personnel);
        if (entity.getState()){
            return entity;
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());


        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联所属项目");
        }

        PunchingCardRecord  punchingCardRecord = new PunchingCardRecord();
        wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(clocking_day_time) = 0");
        PunchingCardRecord selectOne = iPunchingCardRecordMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(selectOne)){
            punchingCardRecord = selectOne;
            CheckInTime checkInTime = iCheckInTimeMapper.selectById(punchingCardRecord.getCheckInTimeId());
            punchingCardRecord.setAcCheckInTime(checkInTime);
        }


        punchingCardRecord.setSysPersonnel(personnel);

        ManagementPersonnel managementPersonnel = selectList.get(0);

        SysManagement management = iSysManagementMapper.selectById(managementPersonnel.getManagementId());

        punchingCardRecord.setManagement(management);


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("management_id",management.getId());
        punchingCardRecord.setCheckInTimes(iCheckInTimeMapper.selectList(queryWrapper));


        return new ReturnEntity(CodeEntity.CODE_SUCCEED,punchingCardRecord,"");
    }

    //进行打卡
    private ReturnEntity add(HttpServletRequest request) throws IOException, ParseException {
        PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new PunchingCardRecordNotNull(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        //获取用户openID
        String token = request.getHeader("Http-X-User-Access-Token");
        Token parseObject = JSONObject.parseObject(PanXiaoZhang.postOpenId(token), Token.class);
        String openid = parseObject.getResponse().getOpenid();
        jsonParam.setOpenId(openid);

        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断员工是否符合打卡条件
        if (!personnel.getRoleId().equals(manage5) && !personnel.getRoleId().equals(manage)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该功能只针对员工或主管开放");
        }
        //判断账户是否正常
        ReturnEntity entity = PanXiaoZhang.estimateState(personnel);
        if (entity.getState()){
            return entity;
        }
        //查询人员所在项目组
        SysManagement management;
        if (!ObjectUtils.isEmpty(personnel)){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",personnel.getPersonnelCode());
            List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
            if (selectList.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员未关联项目组");
            }
            ManagementPersonnel managementPersonnel = selectList.get(0);
            management = iSysManagementMapper.selectById(managementPersonnel.getManagementId());
        }else {
            return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
        }
        PunchingCardRecord punchingCardRecord;
        //判断当前是上班打卡还是下班打卡
        if (!ObjectUtils.isEmpty(personnel)){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(clocking_day_time) = 0");
            wrapper.eq("personnel_code",personnel.getPersonnelCode());
            punchingCardRecord = iPunchingCardRecordMapper.selectOne(wrapper);
        }else {
            return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
        }
        //获取项目打卡坐标
        String[] splitSouthLatitude = management.getSouthLatitude().replaceAll("，",",").split(",");
        String[] splitNorthernLatitude = management.getNorthernLatitude().replaceAll("，",",").split(",");
        String[] splitEastLongitude = management.getEastLongitude().replaceAll("，",",").split(",");
        String[] splitWestLongitude = management.getWestLongitude().replaceAll("，",",").split(",");
        //判断项目的坐标是否有误
        if (
                splitEastLongitude.length < 2 ||
                        splitNorthernLatitude.length < 2 ||
                        splitSouthLatitude.length < 2 ||
                        splitWestLongitude.length < 2
        ){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目坐标有误");
        }
        //获取当前用户的地址
        double[] user = {jsonParam.getX(),jsonParam.getY()};
        //获取项目的X轴的坐标
        double[] polyX = {
                PanXiaoZhang.stringDouble(splitEastLongitude[0])
                ,PanXiaoZhang.stringDouble(splitSouthLatitude[0])
                ,PanXiaoZhang.stringDouble(splitNorthernLatitude[0])
                ,PanXiaoZhang.stringDouble(splitWestLongitude[0])
        };
        //获取项目的Y轴的坐标
        double[] polyY = {
                PanXiaoZhang.stringDouble(splitEastLongitude[1]),
                PanXiaoZhang.stringDouble(splitSouthLatitude[1]),
                PanXiaoZhang.stringDouble(splitNorthernLatitude[1]),
                PanXiaoZhang.stringDouble(splitWestLongitude[1])};
        //判断是否在范围内
        boolean locationInRange = PanXiaoZhang.isLocationInRange(user, polyX, polyY);
        if (!locationInRange){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"不在服务范围内，打卡失败");
        }
        //获取当前时分秒
        LocalTime localTime = LocalTime.now();
        //获取当前时间
        Date date = new Date();
        String format = DateFormatUtils.format(date, PanXiaoZhang.Hms());
        Integer checkInTimeId = 0;//打卡类型的id
        String startPunchIn = "";//上班打卡时间开始时间
        String endPunchIn = "";//上班打卡时间结束时间
        String startClockOut = "";//下班打卡时间开始时间
        String endClockOut = "";//下班打卡时间结束时间
        String checkInTimeName = "";//打卡项目
        //查询打卡时间
        if (!ObjectUtils.isEmpty(management.getId())){
            CheckInTime checkInTime = iCheckInTimeMapper.selectById(jsonParam.getCheckInId());
            System.out.println(checkInTime+"=================");
            if (ObjectUtils.isEmpty(checkInTime)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未设置该类型的打卡时间");
            }
            startPunchIn = checkInTime.getStartPunchIn();
            startClockOut = checkInTime.getStartClockOut();
            endClockOut = checkInTime.getEndClockOut();
            checkInTimeId = checkInTime.getId();
            checkInTimeName = checkInTime.getName();
            endPunchIn = checkInTime.getEndPunchIn();
        }
        if (ObjectUtils.isEmpty(punchingCardRecord)){//如果不存在则判定为上班打卡
            //不让他打卡
            int time = PanXiaoZhang.compareTime(PanXiaoZhang.dateLocalTime(startPunchIn), localTime);
            if (time == 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"超出上班打卡时间，无法打卡");
            }
            //如果 time1 在 time2 之前，返回-1；如果 time1 在 time2 之后，返回1；如果 time1 和 time2 相等，返回0。
            int compareTime = PanXiaoZhang.compareTime(localTime, PanXiaoZhang.dateLocalTime(endPunchIn));
            //设置当前状态
            String workingClockInState = "打卡成功";
            if (compareTime > 0){
                workingClockInState = "迟到";
            }
            PunchingCardRecord cardRecord = new PunchingCardRecord(
                    null,
                    personnel.getName(),
                    personnel.getPersonnelCode(),
                    null,
                    management.getId(),
                    personnel.getOpenId(),
                    jsonParam.getOpenId(),
                    workingClockInState,
                    format,
                    null,
                    null,
                    null,
                    null,
                    DateFormatUtils.format(date, PanXiaoZhang.yMd()),
                    endPunchIn,
                    startClockOut,
                    checkInTimeId,
                    checkInTimeName
            );
            int insert = iPunchingCardRecordMapper.insert(cardRecord);
            if (insert != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        MsgEntity.CODE_ERROR
                );
            }
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,workingClockInState);
        }else {//如果存在则判定为下班打卡
            //不让他打卡
            int time = PanXiaoZhang.compareTime(localTime, PanXiaoZhang.dateLocalTime(endClockOut));
            if (time == 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"超出下班打卡时间，无法打卡");
            }
            //如果 time1 在 time2 之前，返回-1；如果 time1 在 time2 之后，返回1；如果 time1 和 time2 相等，返回0。
            int compareTime = PanXiaoZhang.compareTime(PanXiaoZhang.dateLocalTime(startClockOut), localTime);
            //设置当前状态
            String workingClockInState = "打卡成功";
            if (compareTime > 0){
                workingClockInState = "早退";
            }
            //进行下班打卡
            PunchingCardRecord cardRecord = new PunchingCardRecord(
                    punchingCardRecord.getId(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    personnel.getOpenId(),
                    jsonParam.getOpenId(),
                    workingClockInState,
                    format,
                    null,
                    null,
                    management.getStartClockOut(),
                    null,
                    null
            );
            int insert = iPunchingCardRecordMapper.updateById(cardRecord);
            if (insert != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        jsonParam,
                        MsgEntity.CODE_ERROR
                );
            }
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,workingClockInState);
        }
    }

    private ReturnEntity edit(HttpServletRequest request) {
        return null;
    }
}
