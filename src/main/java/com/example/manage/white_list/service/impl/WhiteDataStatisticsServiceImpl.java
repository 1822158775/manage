package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.data_statistics.DataStatistics;
import com.example.manage.mapper.IManagementPersonnelMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
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
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.addAll(dataStatistics.getDataStatisticsTodayCustoms());
        arrayList.addAll(dataStatistics.getDataStatisticsTodayDays());
        arrayList.addAll(dataStatistics.getDataStatisticsTodayWeeks());
        arrayList.addAll(dataStatistics.getDataStatisticsTodayMonths());
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,arrayList,"");
    }
}
