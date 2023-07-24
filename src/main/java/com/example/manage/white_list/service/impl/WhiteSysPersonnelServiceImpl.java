package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteSysPersonnelService;
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
import java.time.ZoneOffset;
import java.util.Date;
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

    @Value("${phone.birthday}")
    private String birthdayPhone;

    @Value("${phone.personnel}")
    private String personnelPhone;

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${role.manage}")
    private Integer manage;

    @Value("${role.manage3}")
    private Integer manage3;

    @Value("${url.employer_list}")
    private String employerList;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ISysRoleMapper iSysRoleMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private IDispatchApplicationManagementMapper iDispatchApplicationManagementMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("cat_list")){
                return cat_list(request);
            }else if (name.equals("cat_new_list")){
                return cat_new_list(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //方法总管外加事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("add")){
                ReturnEntity returnEntity = add(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("edit")){
                ReturnEntity returnEntity = edit(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("edit_password")){
                return edit_password(request);
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    // 查询同项目组下一级人员
    private ReturnEntity cat_new_list(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }
        SysPersonnel personnel = whiteSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        SysRole sysRole = iSysRoleMapper.selectById(personnel.getRoleId());
        map.remove("personnelId");
        map.put("employmentStatus",1);
        map.put("gtLevelSorting",sysRole.getLevelSorting());
        List<SysPersonnel> personnels = whiteSysPersonnelMapper.queryAll(map);
        personnels.add(personnel);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,personnels,"");
    }

    //修改个人密码
    private ReturnEntity edit_password(HttpServletRequest request) throws IOException {
        SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);

        if (ObjectUtils.isEmpty(jsonParam.getPersonnelId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"用户编码不可为空");
        }

        boolean password = PanXiaoZhang.isPassword(jsonParam.getPassword());
        boolean updatePassword = PanXiaoZhang.isPassword(jsonParam.getUpdatePassword());
        if (!password || !updatePassword){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请输入大于5位小于17位的密码");
        }

        //查询当前填写信息的人
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());

        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        String s = sysPersonnel.getId() + "password";
        Object o = redisUtil.get(s);
        if (!ObjectUtils.isEmpty(o)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请在" + redisUtil.getTime(s) + "秒后操作");
        }
        redisUtil.set(s,sysPersonnel.getPassword(),10);
        String ago_password = PanXiaoZhang.getPassword(jsonParam.getPassword());
        if (!ago_password.equals(sysPersonnel.getPassword())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"密码错误");
        }
        SysPersonnel personnel = new SysPersonnel();
        personnel.setId(sysPersonnel.getId());
        personnel.setPassword(PanXiaoZhang.getPassword(jsonParam.getUpdatePassword()));
        int updateById = iSysPersonnelMapper.updateById(personnel);
        if (updateById != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"密码修改失败");
        }
        PanXiaoZhang.postWechatFer(
                sysPersonnel.getOpenId(),
                "",
                "",
                "最新密码：" + jsonParam.getUpdatePassword(),
                "",
                ""
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"密码修改成功");
    }

    //修改人员信息
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询当前填写信息的人
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询主管的等级
        SysRole sysRole = iSysRoleMapper.selectById(manage);
        //查询当前登录等级
        SysRole selectById = iSysRoleMapper.selectById(sysPersonnel.getRoleId());
        if (sysRole.getLevelSorting() < selectById.getLevelSorting()){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"权限不足");
        }
        if (!ObjectUtils.isEmpty(jsonParam.getPassword())){
            jsonParam.setPassword(PanXiaoZhang.replaceBlank(jsonParam.getPassword()));
            boolean password = PanXiaoZhang.isPassword(jsonParam.getPassword());
            if (!password){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"请输入大于5位小于17位的密码");
            }
            jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        }
        SysPersonnel personnel = new SysPersonnel(
                jsonParam.getId(),
                jsonParam.getPhone(),
                jsonParam.getPlaceOfDomicile(),
                jsonParam.getPassword(),
                jsonParam.getName(),
                jsonParam.getPositionPost(),
                jsonParam.getImage(),
                jsonParam.getBirthday(),
                jsonParam.getSex(),
                jsonParam.getOfficialOrTraineeStaff(),
                jsonParam.getSocialSecurityPayment(),
                jsonParam.getCommercialInsurance(),
                jsonParam.getIdNumber(),
                jsonParam.getEmergencyContactName(),
                jsonParam.getEmergencyContactPhone(),
                jsonParam.getPermanentResidence()
        );
        SysPersonnel byId = iSysPersonnelMapper.selectById(jsonParam.getId());
        if (byId.getEmploymentStatus().equals(2)){
            personnel.setEmploymentStatus(jsonParam.getEmploymentStatus());
        }
        int updateById = iSysPersonnelMapper.updateById(personnel);
        if (updateById != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"信息修改失败");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"信息修改成功");
    }

    //查询人员列表
    private ReturnEntity cat_list(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        //查询当前填写信息的人
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        map.put("inRoleId", new Integer[]{manage5,manage});
        map.remove("personnelId");
        List<SysPersonnel> personnels = whiteSysPersonnelMapper.queryAll(map);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,personnels,"");
    }

    //添加员工所属项目组员工
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new SysPersonnelNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询当前填写信息的人
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        if (!sysPersonnel.getRoleId().equals(manage)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非主管不可添加人员");
        }
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询主管所属项目
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
        if (selectList.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请联系后台人员关联自己的项目");
        }
        //查询资源代码
        wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",jsonParam.getPersonnelCode());
        SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(personnel)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该资源代码已绑定人员");
        }
        //查询账号
        wrapper = new QueryWrapper();
        wrapper.eq("username",jsonParam.getPhone());
        if (!ObjectUtils.isEmpty(iSysPersonnelMapper.selectOne(wrapper))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该账号已存在");
        }
        if (ObjectUtils.isEmpty(jsonParam.getMId())){
            ManagementPersonnel managementPersonnel = selectList.get(0);
            jsonParam.setMId(managementPersonnel.getManagementId());
        }
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getMId());
        if (!management.getManagementState().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
        }
        jsonParam.setUsername(PanXiaoZhang.replaceBlank(jsonParam.getPhone()));
        jsonParam.setPassword(PanXiaoZhang.replaceBlank(jsonParam.getPassword()));
        //设置员工账号
        jsonParam.setUsername(jsonParam.getPhone());
        //设置手机号
        jsonParam.setPhone(jsonParam.getPhone());
        //设置员工职位
        jsonParam.setRoleId(manage5);
        //密码加密
        jsonParam.setPassword(PanXiaoZhang.getPassword(jsonParam.getPassword()));
        //设置员工任职状态
        jsonParam.setEmploymentStatus(2);
        //设置唯一标识
        jsonParam.setPersonnelCode(PanXiaoZhang.getID());
        //设置员工所属项目
        int insert = iManagementPersonnelMapper.insert(new ManagementPersonnel(
                jsonParam.getMId(),
                jsonParam.getPersonnelCode()
        ));
        if (insert != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目关联失败");
        }
        int insertPersonnel = iSysPersonnelMapper.insert(jsonParam);
        if (insertPersonnel != 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"员工录入失败");
        }
        wrapper = new QueryWrapper();
        String[] strings = {personnelPhone, "zgceshi"};
        wrapper.in("username",strings);
        List<SysPersonnel> list = iSysPersonnelMapper.selectList(wrapper);
        for (int i = 0; i < list.size(); i++) {
            SysPersonnel selectOne = list.get(i);
            String openId = "o_QtX5g0Eem4D41v6pR-LRXleSO4";
            if (!ObjectUtils.isEmpty(selectOne)){
                openId = selectOne.getOpenId();
            }
            PanXiaoZhang.postWechatFer(
                    openId,
                    "",
                    "",
                    management.getName() + ":" + jsonParam.getName() + "提交了入职申请",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + employerList + jsonParam.getMId()
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"录入成功");
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

    @Override
    public void ceshi() {
        SysPersonnel personnel = iSysPersonnelMapper.selectById(4);
        PanXiaoZhang.postWechatFer(
                personnel.getOpenId(),
                "",
                "",
                "提交了入职申请",
                "",
                urlTransfer + "?from=zn&redirect_url=" + employerList + 1
        );
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
