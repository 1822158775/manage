package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManageDimission;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.SysRole;
import com.example.manage.entity.is_not_null.ManageDimissionNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.Token;
import com.example.manage.util.wechat.WechatMsg;
import com.example.manage.white_list.service.IWhiteManageDimissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 */

@Slf4j
@Service
public class WhiteManageDimissionServiceImpl implements IWhiteManageDimissionService {

    @Value("${phone.personnel}")
    private String phone;

    @Value("${role.manage3}")
    private String manage3;

    @Value("${url.dimission}")
    private String urlDimission;

    @Value("${url.system}")
    private String urlSystem;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WhiteManageDimissionMapper whiteManageDimissionMapper;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                return add(request);
            }else if (name.equals("cat_leave")){
                return cat_leave(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    private ReturnEntity cat_leave(HttpServletRequest request) {
        return null;
    }

    //添加数据
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageDimissionNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel selectById = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(selectById);
        if (estimateState.getState()){
            return estimateState;
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getId());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        ManageDimission manageDimission = iManageDimissionMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(manageDimission)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"不可重复提交");
        }
        //查询当前个人的信息
        if (!returnEntity.getState()){
            //如果查不到人员信息
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
            }
            //申请人
            jsonParam.setApplicant(sysPersonnel.getName());
            //将人员资源代码加入进去
            jsonParam.setPersonnelCode(sysPersonnel.getPersonnelCode());
            //将当前所属项目加入
            QueryWrapper<ManagementPersonnel> personnelQueryWrapper = new QueryWrapper<>();
            personnelQueryWrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            ManagementPersonnel managementPersonnel = iManagementPersonnelMapper.selectOne(personnelQueryWrapper);
            jsonParam.setManagementId(managementPersonnel.getManagementId());
            //查询该项目主管
            Map map = new HashMap();
            map.put("managementId",jsonParam.getManagementId());
            map.put("roleId",manage3);
            map.put("employmentStatus","1");
            List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
            }
            SysPersonnel personnel = sysPersonnels.get(0);
            //添加审核人编码
            jsonParam.setApproverPersonnelId(personnel.getId());
            //审核人数据
            jsonParam.setSysPersonnel(personnel);
            //设置该条数据唯一编码
            jsonParam.setReportCoding("coding" + System.currentTimeMillis() + PanXiaoZhang.ran(2));
        }
        //添加当前报告时间
        jsonParam.setSubmissionTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        //添加默认状态
        jsonParam.setApproverState("pending");
        //没有任何问题将数据录入进数据库
        int insert = iManageDimissionMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        // 发送人事
        ReturnEntity entity = PanXiaoZhang.postWechat(
                phone,
                "",
                "",
                sysPersonnel.getName() + "提交了离职申请",
                "",
                ""
        );
        // 如果有上一级
        if (!ObjectUtils.isEmpty(jsonParam.getSysPersonnel().getPhone())){
            // 发送上级领导
            PanXiaoZhang.postWechat(
                    jsonParam.getSysPersonnel().getPhone(),
                    "",
                    "",
                    sysPersonnel.getName() + "提交了离职申请",
                    "",
                    ""
            );
        }
        log.info("entity:{}",entity);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"上报成功");
    }

    private ReturnEntity cat(HttpServletRequest request) throws IOException {
        ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //查询当前用户是否存在
        if (ObjectUtils.isEmpty(sysPersonnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前用户不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        ManageDimission manageDimission = iManageDimissionMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(manageDimission)){
            jsonParam = manageDimission;
        }
        jsonParam.setSysPersonnel(sysPersonnel);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, jsonParam,"");
    }
}
