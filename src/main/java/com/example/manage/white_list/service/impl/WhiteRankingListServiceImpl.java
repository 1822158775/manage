package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.CardType;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysManagement;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.ranking_list.RankingList;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteRankingListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @avthor 潘小章
 * @date 2023/4/20
 */

@Slf4j
@Service
public class WhiteRankingListServiceImpl implements IWhiteRankingListService {

    @Resource
    private WhiteRankingListMapper whiteRankingListMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ICardTypeMapper iCardTypeMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private WhiteRankingListProgressBarMapper whiteRankingListProgressBarMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("cat_progress_bar")){
                return cat_progress_bar(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    private ReturnEntity cat_progress_bar(HttpServletRequest request) {

        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }

        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该人员不存在");
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

        Object inManagementId = map.get("inManagementId");
        if (!ObjectUtils.isEmpty(inManagementId)){
            map.put("mId","1");
        }

        Object inPersonnelId = map.get("inPersonnelId");
        if (!ObjectUtils.isEmpty(inPersonnelId)){
            map.put("pId","1");
        }
        String name = "全部";
        String cardTypeName = "";
        Object managementId = map.get("managementId");
        if (managementId != null){
            SysManagement management = iSysManagementMapper.selectById(String.valueOf(managementId));
            if (!ObjectUtils.isEmpty(management)){
                name = management.getName();
            }
        }

        Object sysPersonnelId = map.get("sysPersonnelId");
        if (sysPersonnelId != null){
            SysPersonnel personnel = iSysPersonnelMapper.selectById(String.valueOf(sysPersonnelId));
            if (!ObjectUtils.isEmpty(personnel)){
                name = personnel.getName();
            }
        }

        if (ObjectUtils.isEmpty(managementId) && ObjectUtils.isEmpty(sysPersonnelId)){
            map.put("mId","1");
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);

            ArrayList<Integer> integerArrayList = new ArrayList<>();

            for (int i = 0; i < selectList.size(); i++) {
                integerArrayList.add(selectList.get(i).getManagementId());
            }

            Integer[] toArray = integerArrayList.toArray(new Integer[integerArrayList.size()]);
            map.put("inManagementId",toArray);
        }

        Object manageCardTypeId = map.get("manageCardTypeId");
        if (manageCardTypeId != null){
            CardType cardType = iCardTypeMapper.selectById(String.valueOf(manageCardTypeId));
            if (!ObjectUtils.isEmpty(cardType)){
                cardTypeName = cardType.getName();
            }
        }

        List<String> days = PanXiaoZhang.getDays(String.valueOf(thisStartTime), String.valueOf(thisEndTime));
        if (ObjectUtils.isEmpty(map.get("safeOrderByDESC"))){
            Collections.reverse(days);
        }
        map.put("thisStartTime",thisStartTime + " 00:00:00");
        map.put("thisEndTime",thisEndTime + " 23:59:59");

        List<RankingList> rankingLists = whiteRankingListProgressBarMapper.queryAll(map);
        Map<String,RankingList> rankingListMap = new HashMap<>();
        for (int i = 0; i < rankingLists.size(); i++) {
            RankingList rankingList = rankingLists.get(i);
            rankingListMap.put(rankingList.getDayTime(),rankingList);
        }

        List<RankingList> arrayList = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            String s = days.get(i);
            RankingList rankingList = rankingListMap.get(s);
            if (ObjectUtils.isEmpty(rankingList)){
                arrayList.add(new RankingList(
                    null,
                    0,
                    name,
                    s,
                    cardTypeName,
                    name,
                    null,
                    null,
                    0,
                    0,
                    0,
                    0
                ));
            }else {
                arrayList.add(rankingList);
            }
        }

        return new ReturnEntity(CodeEntity.CODE_SUCCEED,arrayList,"");
    }

    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
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

        map.put("thisStartTime",thisStartTime + " 00:00:00");
        map.put("thisEndTime",thisEndTime + " 23:59:59");
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,whiteRankingListMapper.queryAll(map),"");
    }
}
