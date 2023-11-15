package com.example.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.FurloughRecord;
import com.example.manage.entity.SysManagement;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.data_statistics.Management;
import com.example.manage.entity.data_statistics.PunchingCardRecordReturn;
import com.example.manage.entity.data_statistics.PunchingCardRecordStatistcs;
import com.example.manage.entity.data_statistics.PunchingCardRecordTime;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.PunchingCardRecord;
import com.example.manage.service.IPunchingCardRecordService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;

/**
 * @avthor 潘小章
 * @date 2023-05-05 15:41:39
 * 打卡记录表
 */

@Slf4j
@Service
public class PunchingCardRecordServiceImpl implements IPunchingCardRecordService {

    @Value("${role.manage}")
    private Integer roleId;

    @Value("${role.manage5}")
    private Integer manage5;

    @Resource
    private IPunchingCardRecordMapper iPunchingCardRecordMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private StatisticsPunchingCardRecordMapper statisticsPunchingCardRecordMapper;

    @Resource
    private StatisticsFurloughRecordMapper statisticsFurloughRecordMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                PunchingCardRecord jsonParam = PanXiaoZhang.getJSONParam(request, PunchingCardRecord.class);
                return edit(request,jsonParam);
            }else if (name.equals("statistics")){
                ReturnEntity statistics = statistics(request);
                return statistics;
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    @Override
    public ReturnEntity ceshi(Map map, String name) {
        try {
            if (name.equals("statistics")){
                ReturnEntity statistics = statistics(map);
                return statistics;
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //打卡数据统计
    private ReturnEntity statistics(Map map) throws ParseException {
        return new ReturnEntity(CodeEntity.CODE_ERROR,"最多查询一个月的数据");
    }

    //打卡数据统计
    private ReturnEntity statistics(HttpServletRequest request) throws ParseException {
        Map map = PanXiaoZhang.getJsonMap(request);
        String startTime = String.valueOf(map.get("startTime"));
        String endTime = String.valueOf(map.get("endTime"));
        if (!PanXiaoZhang.isLegalDate(10,String.valueOf(startTime),PanXiaoZhang.yMd())){
            startTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
            map.put("startTime",startTime);
        }
        if (!PanXiaoZhang.isLegalDate(10,String.valueOf(endTime),PanXiaoZhang.yMd())){
            endTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
            map.put("endTime",endTime);
        }
        //获取相差天数
        Integer integer = PanXiaoZhang.differentDays(startTime, endTime,PanXiaoZhang.yMd()) + 1;
        //存储整体数据
        ArrayList<PunchingCardRecordStatistcs> punchingCardRecordStatistcs = new ArrayList<>();
        if (integer < 32 && integer > 0){
            map.put("dateFormat","start");
            map.put("inRoleId", new Integer[]{roleId, manage5});
            map.put("sysRoleId",roleId);
            //查询当前人员打卡记录
            List<SysPersonnel> personnels = statisticsPunchingCardRecordMapper.queryAll(map);
            Integer queryCount = statisticsPunchingCardRecordMapper.queryCount(map);
            //获取天数
            List<String> days = PanXiaoZhang.getDays(startTime, endTime);
            //进行存储打卡记录
            Map<String,PunchingCardRecord> cardRecordMap = new HashMap<>();
            //进行存储请假记录
            Map<String,FurloughRecord> recordMap = new HashMap<>();
            //遍历人员
            for (int i = 0; i < personnels.size(); i++) {
                SysPersonnel personnel = personnels.get(i);
                //获取请假记录
                List<FurloughRecord> furloughRecords = personnel.getFurloughRecords();
                for (int j = 0; j < furloughRecords.size(); j++) {
                    FurloughRecord furloughRecord = furloughRecords.get(j);
                    List<String> stringList = PanXiaoZhang.getDays(DateFormatUtils.format(furloughRecord.getStartTime(), PanXiaoZhang.yMd()), DateFormatUtils.format(furloughRecord.getEndTime(), PanXiaoZhang.yMd()));
                    for (int k = 0; k < stringList.size(); k++) {
                        String dateTime = stringList.get(k);
                        //存入map
                        recordMap.put(
                                dateTime + personnel.getPersonnelCode()
                                , furloughRecord);
                    }
                }

                //获取打卡记录
                List<PunchingCardRecord> punchingCardRecords = personnel.getPunchingCardRecords();
                for (int j = 0; j < punchingCardRecords.size(); j++) {
                    //获取打卡信息
                    PunchingCardRecord punchingCardRecord = punchingCardRecords.get(j);
                    //存入map
                    cardRecordMap.put(
                            punchingCardRecord.getClockingDayTime() + punchingCardRecord.getPersonnelCode()
                            , punchingCardRecord);
                }
                //执勤天数
                Integer dutyDays = 0;
                //迟到次数
                Integer lateArrivals = 0;
                //早退次数
                Integer earlyDepartures = 0;
                //缺卡次数
                Integer accomodate = 0;
                //存储打卡记录
                List<PunchingCardRecordTime> recordList = new ArrayList<>();
                //总时长
                Long sumTime = 0L;
                //遍历日期
                for (int j = 0; j < days.size(); j++) {
                    String day = days.get(j);
                    String key = day + personnel.getPersonnelCode();
                    //信息实体类
                    PunchingCardRecordTime punchingCardRecordTime = new PunchingCardRecordTime();
                    //获取请假信息
                    FurloughRecord furloughRecord = recordMap.get(key);
                    if (!ObjectUtils.isEmpty(furloughRecord)){
                        punchingCardRecordTime.setFurloughRecordName(furloughRecord.getReissueType());
                        punchingCardRecordTime.setFurloughRecordTime(
                                DateFormatUtils.format(furloughRecord.getStartTime(),PanXiaoZhang.yMdHms())+ "~" +
                                DateFormatUtils.format(furloughRecord.getEndTime(),PanXiaoZhang.yMdHms()));
                    }
                    //获取打卡信息
                    PunchingCardRecord punchingCardRecord = cardRecordMap.get(key);
                    if (!ObjectUtils.isEmpty(punchingCardRecord)) {
                        //附上备注
                        punchingCardRecordTime.setWorkingCheckRemark(punchingCardRecord.getWorkingCheckRemark());
                        punchingCardRecordTime.setClosedCheckRemark(punchingCardRecord.getClosedCheckRemark());

                        if (!ObjectUtils.isEmpty(punchingCardRecord.getWorkingAttendanceTime()) && !ObjectUtils.isEmpty(punchingCardRecord.getClosedAttendanceTime())){
                            String[] startSplit = punchingCardRecord.getManagementStartTime().split(":");
                            String[] endSplit = punchingCardRecord.getManagementEndTime().split(":");
                            sumTime = sumTime + PanXiaoZhang.getMinuteTime(
                                    LocalTime.of(
                                            Integer.valueOf(startSplit[0]),
                                            Integer.valueOf(startSplit[1])
                                    ),
                                    LocalTime.of(
                                            Integer.valueOf(endSplit[0]),
                                            Integer.valueOf(endSplit[1])
                                    )
                            );
                        }
                        //执勤天数
                        dutyDays++;
                        String dayTime = "";
                        //如果该条数据打卡状态为迟到
                        if (punchingCardRecord.getWorkingClockInState().equals("迟到")) {
                            if (!ObjectUtils.isEmpty(punchingCardRecord.getManagementStartTime())){
                                String[] startSplit = punchingCardRecord.getManagementStartTime().split(":");
                                String format = punchingCardRecord.getWorkingAttendanceTime();
                                String[] endSplit = format.split(":");
                                dayTime = PanXiaoZhang.getDayTime(
                                        LocalTime.of(Integer.valueOf(startSplit[0]), Integer.valueOf(startSplit[1]), Integer.valueOf(startSplit[2])),
                                        LocalTime.of(Integer.valueOf(endSplit[0]), Integer.valueOf(endSplit[1]), Integer.valueOf(endSplit[2]))
                                );
                            }
                            lateArrivals++;
                        }
                        //存储上班打卡时间
                        punchingCardRecordTime.setCheckIn(punchingCardRecord.getWorkingClockInState() + dayTime);
                        //清空
                        dayTime = "";
                        //如果该条数据下班打卡状态为迟到
                        if (ObjectUtils.isEmpty(punchingCardRecord.getClosedClockInState())) {
                            accomodate++;
                            //下班设置为缺卡
                            punchingCardRecordTime.setCheckOut("缺卡");
                        } else {
                            if (punchingCardRecord.getClosedClockInState().equals("早退")) {
                                if (!ObjectUtils.isEmpty(punchingCardRecord.getManagementEndTime())){
                                    String[] startSplit = punchingCardRecord.getManagementEndTime().split(":");
                                    String format = punchingCardRecord.getClosedAttendanceTime();
                                    String[] endSplit = format.split(":");
                                    dayTime = PanXiaoZhang.getDayTime(
                                            LocalTime.of(Integer.valueOf(endSplit[0]), Integer.valueOf(endSplit[1])),
                                            LocalTime.of(Integer.valueOf(startSplit[0]), Integer.valueOf(startSplit[1]))
                                    );
                                }
                                earlyDepartures++;
                            }
                            punchingCardRecordTime.setCheckOut(punchingCardRecord.getClosedClockInState() + dayTime);
                        }
                        recordList.add(punchingCardRecordTime);
                    }else {
                        punchingCardRecordTime.setCheckIn("缺勤");
                        punchingCardRecordTime.setCheckOut("缺勤");
                        recordList.add(punchingCardRecordTime);
                    }
                }
                punchingCardRecordStatistcs.add(new PunchingCardRecordStatistcs(
                        personnel.getName(),
                        personnel.getPersonnels(),
                        personnel.getManagements(),
                        dutyDays,
                        lateArrivals,
                        earlyDepartures,
                        accomodate,
                        (sumTime / 60) + "小时" + (sumTime % 60) + "分钟",
                        personnel.getWorkingAgoOpenNumber(),
                        recordList
                ));
            }
            //Object[] title = {"主管","项目","员工","执勤天数","迟到次数","早退次数","缺卡次数",days};
            //Object[] title = {days};
            ArrayList<Object> arrayList = new ArrayList<>();
            for (int i = 0; i < days.size(); i++) {
                arrayList.add(new Management(days.get(i)));
            }
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,new PunchingCardRecordReturn(punchingCardRecordStatistcs,arrayList),request,MsgEntity.CODE_SUCCEED,queryCount);
        }
        return new ReturnEntity(CodeEntity.CODE_ERROR,"最多查询一个月的数据");
    }

    // 修改打卡记录表
    private ReturnEntity edit(HttpServletRequest request, PunchingCardRecord jsonParam) {
        int updateById = iPunchingCardRecordMapper.updateById(jsonParam);
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

    // 添加打卡记录表
    private ReturnEntity add(HttpServletRequest request, PunchingCardRecord jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iPunchingCardRecordMapper.insert(jsonParam);
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iPunchingCardRecordMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iPunchingCardRecordMapper.queryCount(map));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 310000; i++) {
            System.out.println(i);
        }
    }
}
