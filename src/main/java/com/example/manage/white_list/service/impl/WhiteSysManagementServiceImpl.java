package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysManagement;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.IManagementPersonnelMapper;
import com.example.manage.mapper.ISysManagementMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.WhiteSysManagementMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteSysManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/7
 */

@Slf4j
@Service
public class WhiteSysManagementServiceImpl implements IWhiteSysManagementService {

    @Value("${role.manage5}")
    private String manage5;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private WhiteSysManagementMapper whiteSysManagementMapper;

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
            }else if (name.equals("cat_number")){
                return cat_number(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    //项目人编
    private ReturnEntity cat_number(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);

        SysPersonnel personnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));

        if (ObjectUtils.isEmpty(personnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该员工不存在");
        }

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",personnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (int i = 0; i < selectList.size(); i++) {
            integerArrayList.add(selectList.get(i).getManagementId());
        }

        Integer[] toArray = integerArrayList.toArray(new Integer[integerArrayList.size()]);


        if (selectList.size() < 1){
            map.put("inId",null);
        }else {
            map.put("inId",toArray);
        }

        map.put("roleId",manage5);

        List<SysManagement> sysManagements = whiteSysManagementMapper.queryAll(map);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,sysManagements,"");
    }

    // 查询模块
    public ReturnEntity cat(HttpServletRequest request) {
        return new ReturnEntity(
                CodeEntity.CODE_SUCCEED,
                iSysManagementMapper.selectList(null),
                "");
    }
}
