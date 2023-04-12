package com.example.manage.white_list.service.impl;

import com.example.manage.entity.CardType;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.WhiteSysPersonnelMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

@Slf4j
@Service
public class WhiteSysPersonnelServiceImpl implements IWhiteSysPersonnelService {

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

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
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    @Override
    public List<SysPersonnel> myLeader(Integer roleId, Integer managementId) {
        Map map = new HashMap();
        map.put("managementId",managementId);
        map.put("roleId",roleId);
        map.put("employmentStatus","1");
        return whiteSysPersonnelMapper.queryAll(map);
    }

    @Override
    public List<SysPersonnel> queryAll(Map map) {
        return iSysPersonnelMapper.queryAll(map);
    }

    //根据人员查询当下的卡种
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        SysPersonnel sysPersonnel = whiteSysPersonnelMapper.queryOne(map);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,sysPersonnel,"");
    }
}
