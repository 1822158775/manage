package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.PerformanceReportSales;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.data_statistics.*;
import com.example.manage.mapper.IManagementPersonnelMapper;
import com.example.manage.mapper.IPerformanceReportSalesMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.WhiteDataStatisticsMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteDataStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

/**
 * @avthor 潘小章
 * @date 2023/4/19
 * 数据统计
 */

@Slf4j
@Service
public class WhiteDataStatisticsServiceImpl implements IWhiteDataStatisticsService {

    @Resource
    private WhiteDataStatisticsMapper whiteDataStatisticsMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IPerformanceReportSalesMapper iPerformanceReportSalesMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat1(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //数据查看
    private ReturnEntity cat1(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }

        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员不存在");
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (int i = 0; i < selectList.size(); i++) {
            integerArrayList.add(selectList.get(i).getManagementId());
        }

        Integer[] toArray = integerArrayList.toArray(new Integer[integerArrayList.size()]);

        if (selectList.size() < 1){
            map.put("inManagementId",null);
        }else {
            map.put("inManagementId",toArray);
            map.put("mId","1");
        }

        Object thisStartTime = map.get("startTime");
        if (ObjectUtils.isEmpty(thisStartTime)){
            thisStartTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
        }

        Object thisEndTime = map.get("endTime");
        if (ObjectUtils.isEmpty(thisEndTime)){
            thisEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), 0);
        }

        Long dayTime = PanXiaoZhang.getDayTime(String.valueOf(thisStartTime), String.valueOf(thisEndTime));

        if (dayTime > 31){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"查询天数最多一个月");
        }

        if (dayTime < 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"开始时间不可以大于结束时间");
        }

        Object thatEndTime = map.get("thatEndTime");
        if (ObjectUtils.isEmpty(thatEndTime)){
            thatEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), -1);
        }

        Object thatStartTime = map.get("thatStartTime");
        if (ObjectUtils.isEmpty(thatStartTime)){
            thatStartTime = PanXiaoZhang.GetNextDay(String.valueOf(thatEndTime), Integer.valueOf((dayTime * -1) + ""));
        }


        map.put("thisStartTime",thisStartTime + " 00:00:00");
        map.put("thisEndTime",thisEndTime + " 23:59:59");
        map.put("thatStartTime",thatStartTime + " 00:00:00");
        map.put("thatEndTime",thatEndTime + " 23:59:59");
        map.put("startTime",thisStartTime);
        map.put("endTime",thisEndTime);

        //获取上个月的同比天数
        // 获取上个月的日期
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);

        // 获取上个月的天数
        int lastMonthDays = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonthDate = currentDate.minusMonths(1);
        //获取当前是几号
        int day = currentDate.getDayOfMonth();
        //获取上个月年份
        int year = lastMonthDate.getYear();
        //获取上个月月份
        int month = lastMonthDate.getMonthValue();
        if (day > lastMonthDays){
            day = lastMonthDays;
        }

        //同比上周
        Map<String, String> date = getDate();
        String lastMondayStart = date.get("lastMonday") + " 00:00:00";
        String lastMondayEnd = date.get("lastWeekSameDay") + " " + date.get("hmsTime");
        map.put("lastMondayStart",lastMondayStart);
        map.put("lastMondayEnd",lastMondayEnd);

        //同比上个月
        String lastMonthTimeStart = year + "-" + month + "-1 00:00:00";
        String lastMonthTimeEnd = year + "-" + month + "-" + day + " " + date.get("hmsTime");
        map.put("lastMonthTimeStart",lastMonthTimeStart);
        map.put("lastMonthTimeEnd",lastMonthTimeEnd);

        DataStatistics dataStatistics = whiteDataStatisticsMapper.queryAll(map);

        //权益
        map.put("approverState","agree");
        //自定义
        map.put("type","自定义");
        DataStatisticsTodayCustom customs = dataStatistics.getDataStatisticsTodayCustoms();
        customs.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        //今日
        map.put("type","day");
        DataStatisticsTodayDay days = dataStatistics.getDataStatisticsTodayDays();
        days.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        //本周
        map.put("type","week");
        DataStatisticsTodayWeek weeks = dataStatistics.getDataStatisticsTodayWeeks();
        weeks.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        //本月
        map.put("type","month");
        DataStatisticsTodayMonth months = dataStatistics.getDataStatisticsTodayMonths();
        months.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(customs);
        arrayList.add(days);
        arrayList.add(weeks);
        arrayList.add(months);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,arrayList,"");
    }

    //查询数据
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }

        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员不存在");
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (int i = 0; i < selectList.size(); i++) {
            integerArrayList.add(selectList.get(i).getManagementId());
        }

        Integer[] toArray = integerArrayList.toArray(new Integer[integerArrayList.size()]);

        if (selectList.size() < 1){
            map.put("inManagementId",null);
        }else {
            map.put("inManagementId",toArray);
            map.put("mId","1");
        }

        Object thisStartTime = map.get("startTime");
        if (ObjectUtils.isEmpty(thisStartTime)){
            thisStartTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
        }

        Object thisEndTime = map.get("endTime");
        if (ObjectUtils.isEmpty(thisEndTime)){
            thisEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), 0);
        }

        Long dayTime = PanXiaoZhang.getDayTime(String.valueOf(thisStartTime), String.valueOf(thisEndTime));

        if (dayTime > 31){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"查询天数最多一个月");
        }

        if (dayTime < 0){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"开始时间不可以大于结束时间");
        }

        Object thatEndTime = map.get("thatEndTime");
        if (ObjectUtils.isEmpty(thatEndTime)){
            thatEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), -1);
        }

        Object thatStartTime = map.get("thatStartTime");
        if (ObjectUtils.isEmpty(thatStartTime)){
            thatStartTime = PanXiaoZhang.GetNextDay(String.valueOf(thatEndTime), Integer.valueOf((dayTime * -1) + ""));
        }


        map.put("thisStartTime",thisStartTime + " 00:00:00");
        map.put("thisEndTime",thisEndTime + " 23:59:59");
        map.put("thatStartTime",thatStartTime + " 00:00:00");
        map.put("thatEndTime",thatEndTime + " 23:59:59");
        map.put("startTime",thisStartTime);
        map.put("endTime",thisEndTime);

        DataStatistics dataStatistics = whiteDataStatisticsMapper.queryAll(map);

        //权益
        map.put("approverState","agree");
        //自定义
        map.put("type","自定义");
        DataStatisticsTodayCustom customs = dataStatistics.getDataStatisticsTodayCustoms();
        customs.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        //今日
        map.put("type","day");
        DataStatisticsTodayDay days = dataStatistics.getDataStatisticsTodayDays();
        days.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        //本周
        map.put("type","week");
        DataStatisticsTodayWeek weeks = dataStatistics.getDataStatisticsTodayWeeks();
        weeks.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        //本月
        map.put("type","month");
        DataStatisticsTodayMonth months = dataStatistics.getDataStatisticsTodayMonths();
        months.setSalesList(iPerformanceReportSalesMapper.queryList(map));
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(customs);
        arrayList.add(days);
        arrayList.add(weeks);
        arrayList.add(months);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,arrayList,"");
    }

    /**
     * 获取上周一时间，本周一时间，上周同周时间
     * @return
     */
    public static Map<String, String> getDate(){
        Map<String, String> map = new HashMap<>();
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();

        // 获取上周一的日期
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String lastMonday = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        // 获取这周一的日期
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String thisMonday = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        calendar = Calendar.getInstance();
        // 获取当前日期的周几
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 将日期减去7天
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        // 获取减去7天后的日期的周几
        int lastWeekDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 计算日期的差值
        int dayDiff = lastWeekDayOfWeek - currentDayOfWeek;

        // 将日期加上差值
        calendar.add(Calendar.DAY_OF_YEAR, dayDiff);

        // 获取上周的当前同周的日期
        String lastWeekSameDay = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        //获取当前的时分秒
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String hmsTime = hour + ":" + minute + ":" + second;

        map.put("lastWeekSameDay",lastWeekSameDay);
        map.put("lastMonday",lastMonday);
        map.put("hmsTime",hmsTime);
        return map;
    }
    public static void main(String[] args) {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();

        // 获取上周一的日期
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String lastMonday = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        // 获取这周一的日期
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String thisMonday = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        calendar = Calendar.getInstance();
        // 获取当前日期的周几
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 将日期减去7天
        calendar.add(Calendar.DAY_OF_YEAR, -7);

        // 获取减去7天后的日期的周几
        int lastWeekDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 计算日期的差值
        int dayDiff = lastWeekDayOfWeek - currentDayOfWeek;

        // 将日期加上差值
        calendar.add(Calendar.DAY_OF_YEAR, dayDiff);

        // 获取上周的当前同周的日期
        String lastWeekSameDay = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        //获取当前的时分秒
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String hmsTime = hour + ":" + minute + ":" + second;
        System.out.println("这周一的日期：" + thisMonday);
        System.out.println("上周一的日期：" + lastMonday);
        System.out.println("上周的当前同周的日期：" + lastWeekSameDay);
        System.out.println("当前时间：" + hmsTime);
    }
}
