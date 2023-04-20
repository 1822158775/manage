package com.example.manage.white_list.service.impl;

import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.ranking_list.RankingList;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.WhiteRankingListMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteRankingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        List<RankingList> rankingLists = whiteRankingListMapper.queryAll(map);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,rankingLists,"");
    }
}
