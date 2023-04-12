package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.ManageReimbursementRecordNotNull;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageReimbursementRecordService;
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
import java.util.*;

/**
 * @avthor 潘小章
 * @date 2023/4/11
 */

@Slf4j
@Service
public class WhiteManageReimbursementRecordServiceImpl implements IWhiteManageReimbursementRecordService {

    @Value("${url.reimburse}")
    private String urlReimburse;

    //添加相关项目
    @Resource
    private IReimbursementProjectMapper iReimbursementProjectMapper;

    //添加类目关联
    @Resource
    private IReimbursementCategoryMapper iReimbursementCategoryMapper;

    //添加审核人关联
    @Resource
    private IReimbursementApprovalMapper iReimbursementApprovalMapper;

    //添加抄送人关联
    @Resource
    private IReimbursementCopyMapper iReimbursementCopyMapper;

    //添加关联附件
    @Resource
    private IReimbursementImageMapper iReimbursementImageMapper;

    //项目mapper
    @Resource
    private ISysManagementMapper iSysManagementMapper;

    //类目mapper
    @Resource
    private IManageReimbursementCategoryMapper iManageReimbursementCategoryMapper;

    //审核人员mapper
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    //报销记录mapper
    @Resource
    private IManageReimbursementRecordMapper iManageReimbursementRecordMapper;

    //人员管理账号
    @Resource
    private IManageBankAccountMapper iManageBankAccountMapper;

    //职位关联类目
    @Resource
    private IManageRMMapper iManageRMMapper;

    //抄送人关联类目
    @Resource
    private ICategoryCopyMapper iCategoryCopyMapper;

    //查询审核人
    @Resource
    private WhiteManageRMMapper whiteManageRMMapper;

    //查询抄送人
    @Resource
    private WhiteCategoryCopyMapper whiteCategoryCopyMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                return add(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

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
                return add(request);
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }
    //申请报销信息
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        ManageReimbursementRecord jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageReimbursementRecordNotNull(
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断该状态是否进行提交数据
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(personnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //判断是否有项目
        List<ReimbursementProject> reimbursementProjects = jsonParam.getReimbursementProjects();
        if (reimbursementProjects.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请选择项目");
        }
        //判断是否有类目关联
        List<ReimbursementCategory> reimbursementCategories = jsonParam.getReimbursementCategories();
        if (reimbursementCategories.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"请选择类目");
        }
        //附件集合
        List<ReimbursementImage> reimbursementImages = jsonParam.getReimbursementImages();
        //生成编码
        jsonParam.setDeclarationCode(System.currentTimeMillis() + PanXiaoZhang.ran(2));
        //存储项目id
        List<Integer> sysManagementList = new ArrayList<>();
        try {
            //定义项目数组
            Map<Integer,Integer> integerMap = new HashMap<>();
            //添加相关项目
            for (int i = 0; i < reimbursementProjects.size(); i++) {
                //获取当前数据
                ReimbursementProject reimbursementProject = reimbursementProjects.get(i);
                //查询项目是否存在
                SysManagement management = iSysManagementMapper.selectById(reimbursementProject.getManageManagementId());
                if (ObjectUtils.isEmpty(management)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,reimbursementProject.getName() + "该项目不存在");
                }
                //获取Map存储的内容
                Integer integer = integerMap.get(management.getId());
                //如果不存在，将值存储进integerMap和sysManagementList中
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(management.getId(),management.getId());
                    sysManagementList.add(management.getId());
                }
                //进行添加
                iReimbursementProjectMapper.insert(new ReimbursementProject(
                        management.getName(),//设置项目名称
                        jsonParam.getDeclarationCode(),//设置报销编码
                        management.getId()//设置项目编码
                ));
            }
        }catch (Exception e){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目录入失败");
        }
        //存储类目编码
        List<String> manageReimbursementCategoryList = new ArrayList<>();
        //存储职位id
        List<Integer> sysRoleList = new ArrayList<>();
        //存储抄送人id
        List<Integer> copyList = new ArrayList<>();
        //添加类目关联
        try {
            //定义项目数组
            Map<Integer,Integer> integerMap = new HashMap<>();
            for (int i = 0; i < reimbursementCategories.size(); i++) {
                //获取当前数据
                ReimbursementCategory reimbursementCategory = reimbursementCategories.get(i);
                //查询当前类目是否存在
                ManageReimbursementCategory manageReimbursementCategory = iManageReimbursementCategoryMapper.selectById(reimbursementCategory.getReimbursementCategoryId());
                //判断类目是否存在
                if (ObjectUtils.isEmpty(manageReimbursementCategory)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,reimbursementCategory.getName() + "该类目不存在");
                }
                //获取Map存储的内容
                Integer integer = integerMap.get(reimbursementCategory.getId());
                //如果不存在，将值存储进integerMap和sysManagementList中
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(reimbursementCategory.getId(),reimbursementCategory.getId());
                    manageReimbursementCategoryList.add(manageReimbursementCategory.getCategoryCoding());
                }
                //将当前金额设置小数点为2位
                Double aDouble = PanXiaoZhang.doubleD(reimbursementCategory.getAmout(), 2);
                //进行添加进总金额
                jsonParam.setAmountDeclared(PanXiaoZhang.doubleD(aDouble + jsonParam.getAmountDeclared(),2));
                //添加报销和类目
                iReimbursementCategoryMapper.insert(new ReimbursementCategory(
                        jsonParam.getDeclarationCode(),//设置报销编码
                        manageReimbursementCategory.getId(),//设置类目编码
                        aDouble,//设置当前金额
                        manageReimbursementCategory.getName(),//设置类目名称
                        manageReimbursementCategory.getReimbursementType(),//设置类目类型
                        reimbursementCategory.getOccurrenceTime()
                ));
            }
            //转化为数组
            String[] strings = manageReimbursementCategoryList.toArray(new String[manageReimbursementCategoryList.size()]);
            //查询类目和审核人的关联表
            QueryWrapper<ManageRM> rmQueryWrapper = new QueryWrapper<>();
            rmQueryWrapper.in("reimbursement_code",strings);
            List<ManageRM> manageRMS = iManageRMMapper.selectList(rmQueryWrapper);
            //存储职位map
            Map<Integer,Integer> roleMap = new HashMap<>();
            for (int i = 0; i < manageRMS.size(); i++) {
                ManageRM manageRM = manageRMS.get(i);
                Integer integer = roleMap.get(manageRM.getRoleId());
                //如果不存在，将值存储进roleMap和sysRoleList中
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(manageRM.getRoleId(),manageRM.getRoleId());
                    sysRoleList.add(manageRM.getRoleId());
                }
            }
            //查询类目和抄送人的关联表
            QueryWrapper<CategoryCopy> copyQueryWrapper = new QueryWrapper<>();
            copyQueryWrapper.in("reimbursement_code",strings);
            List<CategoryCopy> categoryCopies = iCategoryCopyMapper.selectList(copyQueryWrapper);
            //存储抄送人map
            Map<Integer,Integer> copyMap = new HashMap<>();
            for (int i = 0; i < categoryCopies.size(); i++) {
                CategoryCopy categoryCopy = categoryCopies.get(i);
                Integer integer = roleMap.get(categoryCopy.getRoleId());
                //如果不存在，将值存储进roleMap和sysRoleList中
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(categoryCopy.getRoleId(),categoryCopy.getRoleId());
                    sysRoleList.add(categoryCopy.getRoleId());
                }
            }
        }catch (Exception e){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"报销细节录入失败");
        }

        //转化为项目id数组
        Integer[] sysManagements = sysManagementList.toArray(new Integer[sysManagementList.size()]);
        //转化为职位id数组
        Integer[] sysRoles = sysRoleList.toArray(new Integer[sysRoleList.size()]);
        //转化为抄送人职位id数组
        Integer[] copyCopies = copyList.toArray(new Integer[copyList.size()]);
        //查询map存储
        Map hashMap = new HashMap();
        //设置已选项目id
        hashMap.put("sysManagements",sysManagements);
        //设置审核职位关联
        hashMap.put("sysRoles",sysRoles);
        //设置抄送人职位关联
        hashMap.put("copyCopies",copyCopies);
        //查询审核人
        List<SysPersonnel> rmPersonnels = whiteManageRMMapper.queryAll(hashMap);
        //判断是否有审核人
        if (rmPersonnels.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前没有审核人");
        }
        //查询抄送人
        List<SysPersonnel> copyPersonnels = whiteCategoryCopyMapper.queryAll(hashMap);
        //判断是否有抄送人
        if (copyPersonnels.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前没有抄送人");
        }
        //储存需要通知的人
        Map<String, Integer> map = new HashMap();
        //储存第一个审批人手机号
        String approvalPhone = "";
        //添加审核人
        for (int i = 0; i < rmPersonnels.size(); i++) {
            //获取当前数据
            SysPersonnel sysPersonnel = rmPersonnels.get(i);
            //添加审核人
            iReimbursementApprovalMapper.insert(new ReimbursementApproval(
                jsonParam.getDeclarationCode(),
                sysPersonnel.getPersonnelCode(),
                sysPersonnel.getName(),
                sysPersonnel.getSysRole().getNumber(),
                "pending"
            ));
            //获取当前存储
            Integer integer = map.get("approvalInt");
            if (!ObjectUtils.isEmpty(integer)){
                if (integer > sysPersonnel.getSysRole().getNumber()){
                    map.put("approvalInt",sysPersonnel.getSysRole().getNumber());
                    approvalPhone = sysPersonnel.getPhone();
                }
            }else {
                map.put("approvalInt",sysPersonnel.getSysRole().getNumber());
                approvalPhone = sysPersonnel.getPhone();
            }
        }
        //添加抄送人
        for (int i = 0; i < copyPersonnels.size(); i++) {
            //获取当前数据
            SysPersonnel sysPersonnel = copyPersonnels.get(i);
            //添加抄送人
            iReimbursementCopyMapper.insert(new ReimbursementCopy(
                jsonParam.getDeclarationCode(),
                sysPersonnel.getPersonnelCode(),
                sysPersonnel.getName()
            ));
        }
        //查询当前人员账号
        if (!ObjectUtils.isEmpty(jsonParam.getBankAccount())){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",personnel.getPersonnelCode());
            wrapper.eq("bank_account",jsonParam.getBankAccount());
            //查询当前银行账号是否存在
            ManageBankAccount manageBankAccount = iManageBankAccountMapper.selectOne(wrapper);
            //判断当前银行账号是否存在
            if (ObjectUtils.isEmpty(manageBankAccount)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"请先绑定银行账号");
            }
            //设置银行账号
            jsonParam.setBankAccount(manageBankAccount.getBankAccount());
            //设置银行开户行地址
            jsonParam.setBankOfDeposit(manageBankAccount.getBankOfDeposit());
        }
        //添加附件
        for (int i = 0; i < reimbursementImages.size(); i++) {
            //获取当前数据
            ReimbursementImage reimbursementImage = reimbursementImages.get(i);
            iReimbursementImageMapper.insert(new ReimbursementImage(
                    reimbursementImage.getPathUrl(),
                    jsonParam.getDeclarationCode()
            ));
        }
        //将id设为空
        jsonParam.setId(null);
        //添加手机号
        jsonParam.setPhone(personnel.getPhone());
        //添加账号
        jsonParam.setAccount(personnel.getUsername());
        //添加申请人
        jsonParam.setApplicant(personnel.getName());
        //添加申请时间
        jsonParam.setDeclarationTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        //添加当前状态
        jsonParam.setApproverState("pending");
        //添加记录
        int insert = iManageReimbursementRecordMapper.insert(jsonParam);
        //如果添加失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        //发送给第一个审批人
        PanXiaoZhang.postWechat(
                approvalPhone,
                personnel.getName() + "提交了报销信息",
                "审核信息",
                "请前往审核",
                "",
                urlReimburse + "?code=" + jsonParam.getDeclarationCode()
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,MsgEntity.CODE_SUCCEED);
    }

    private ReturnEntity edit(HttpServletRequest request) {
        return null;
    }

    //查询当前数据
    private ReturnEntity cat(HttpServletRequest request) {
        return null;
    }
}
