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
import org.apache.commons.lang3.StringUtils;
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

    @Value("${url.transfer}")
    private String urlTransfer;

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

    //查询申请报销记录
    @Resource
    private WhiteManageReimbursementRecordMapper whiteManageReimbursementRecordMapper;

    //角色mapper
    @Resource
    private ISysRoleMapper iSysRoleMapper;

    //添加相关项目
    @Resource
    private WhiteReimbursementProjectMapper WhiteReimbursementProjectMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                return add(request);
            }else if (name.equals("approver_cat")){
                return approver_cat(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    //审核列表
    private ReturnEntity approver_cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        Integer integer = whiteManageReimbursementRecordMapper.queryCount(map);
        ReturnEntity returnEntity = new ReturnEntity(CodeEntity.CODE_SUCCEED, whiteManageReimbursementRecordMapper.queryApproverAll(map), "");
        returnEntity.setCount(integer);
        return returnEntity;
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
                ReturnEntity returnEntity = add(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }


    //审核报销表单
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        ManageReimbursementRecord jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageReimbursementRecordNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
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
        //查询该项目信息
        ManageReimbursementRecord manageReimbursementRecord = iManageReimbursementRecordMapper.selectById(jsonParam.getId());
        //获取当前审批状态
        QueryWrapper wrapperApproval = new QueryWrapper();
        wrapperApproval.eq("reimbursement_record_code",manageReimbursementRecord.getDeclarationCode());
        wrapperApproval.eq("personnel_code",personnel.getPersonnelCode());
        wrapperApproval.ne("approval_state","pending");
        ReimbursementApproval reimbursementApproval = iReimbursementApprovalMapper.selectOne(wrapperApproval);
        if (!ObjectUtils.isEmpty(reimbursementApproval)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前状态为" + reimbursementApproval.getApprovalState() + "不可更改");
        }
        //判断数据是否存在
        if (ObjectUtils.isEmpty(manageReimbursementRecord)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据不存在");
        }
        //判断是否可以操作状态
        if (!manageReimbursementRecord.getApproverState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目状态为" + manageReimbursementRecord.getApproverState() + ",无法进行下一步操作");
        }
        //当前可审批的等级
        Integer maxNumber = 0;
        //查询审核人
        if (manageReimbursementRecord.getDeclarationCode() != null){
            Map map = new HashMap();
            map.put("reimbursementRecordCode",manageReimbursementRecord.getDeclarationCode());
            map.put("approvalState","pending");
            maxNumber = iReimbursementApprovalMapper.queryMax(map);
        }
        //查询当前职位等级
        if (!ObjectUtils.isEmpty(personnel.getSysRole())){
            SysRole sysRole = iSysRoleMapper.selectById(personnel.getRoleId());
            if (!sysRole.getNumber().equals(maxNumber)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前职位不可审批");
            }
        }
        //当前时间
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms());
        //如果同意审核
        if (jsonParam.getApproverState().equals("agree")){
            //查询是否还有剩余的
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.ne("personnel_code",personnel.getPersonnelCode());
            wrapper.eq("approval_state","pending");
            wrapper.eq("reimbursement_record_code",manageReimbursementRecord.getDeclarationCode());
            List<ReimbursementApproval> selectList = iReimbursementApprovalMapper.selectList(wrapper);
            //如果没有了审批人
            if (selectList.size() < 1){
                Map map = new HashMap();
                map.put("reimbursementRecordCode",manageReimbursementRecord.getDeclarationCode());
                List<ReimbursementProject> reimbursementProjects = WhiteReimbursementProjectMapper.querySumAmount(map);
                //进行项目余额操作
                for (int i = 0; i < reimbursementProjects.size(); i++) {
                    //获取项目需承担的金额
                    ReimbursementProject reimbursementProject = reimbursementProjects.get(i);
                    //查询当前项目
                    SysManagement management = iSysManagementMapper.selectById(reimbursementProject.getManageManagementId());
                    //进行项目余额计算
                    Double doubleD = PanXiaoZhang.doubleD(management.getAvailableBalance() - reimbursementProject.getAmount(), 2);
                    //判断余额是否够用
                    if (doubleD < 0D){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,management.getName() + "项目" + ",余额不足");
                    }
                    //进行修改金额
                    int updateById = iSysManagementMapper.updateById(new SysManagement(
                            reimbursementProject.getManageManagementId(),
                            doubleD
                    ));
                    if (updateById != 1){
                        return new ReturnEntity(CodeEntity.CODE_ERROR,"项目余额修改失败");
                    }
                }
                //修改当前报销记录的总状态
                int updateById = iManageReimbursementRecordMapper.updateById(new ManageReimbursementRecord(
                        manageReimbursementRecord.getId(),
                        format,
                        jsonParam.getRemark(),
                        jsonParam.getApproverState()
                ));
                if (updateById != 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"报销状态修改失败");
                }
                //发送给申请人
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("username",manageReimbursementRecord.getAccount());
                SysPersonnel sysPersonnel = iSysPersonnelMapper.selectOne(queryWrapper);
                PanXiaoZhang.postWechatFer(
                        sysPersonnel.getOpenId(),
                        "审核信息",
                        "",
                        "审核通过",
                        "",
                        urlTransfer + "?from=zn&redirect_url=" + urlReimburse + "from_verify=" + false
                );
            }else {//如果还有审批人
                Map map = new HashMap();
                map.put("reimbursementRecordCode",manageReimbursementRecord.getDeclarationCode());
                map.put("approvalState","pending");
                map.put("nePersonnelCode",personnel.getPersonnelCode());//不等于当前审核人的资源编码
                //查询当前审核等级
                Integer queryMax = iReimbursementApprovalMapper.queryMax(map);
                //判断审核等级是否相同
                if (!queryMax.equals(maxNumber)){//如果不相等
                    //去除当前审核人的资源代码
                    map.remove("nePersonnelCode");
                    //设置条件
                    map.put("number",maxNumber);
                    List<ReimbursementApproval> reimbursementApprovals = iReimbursementApprovalMapper.queryAll(map);
                    //开始循环通知下一层的审核人员
                    for (int i = 0; i < reimbursementApprovals.size(); i++) {
                        ReimbursementApproval approval = reimbursementApprovals.get(i);
                        //查询人员当前手机号
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.eq("personnel_code",approval.getPersonnelCode());
                        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectOne(queryWrapper);
                        PanXiaoZhang.postWechatFer(
                                sysPersonnel.getOpenId(),
                                "",
                                "",
                                personnel.getName() + ":提交了报销信息,请前往审核",
                                "",
                                urlTransfer + "?from=zn&redirect_url=" + urlReimburse + "from_verify=" + true
                        );
                    }
                    maxNumber = queryMax;
                    iManageReimbursementRecordMapper.updateById(new ManageReimbursementRecord(
                        manageReimbursementRecord.getId(),
                        maxNumber
                    ));
                }
            }
            //更改当前审核状态
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("personnel_code",personnel.getPersonnelCode());
            queryWrapper.eq("reimbursement_record_code",manageReimbursementRecord.getDeclarationCode());
            int update = iReimbursementApprovalMapper.update(new ReimbursementApproval(
                format,
                jsonParam.getApproverState(),
                jsonParam.getRemark()
            ), queryWrapper);
            if (update != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"审核结果修改失败");
            }
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
        }else if (jsonParam.getApproverState().equals("refuse")){//如果拒绝审核
            //修改当前报销记录的总状态
            int updateById = iManageReimbursementRecordMapper.updateById(new ManageReimbursementRecord(
                    manageReimbursementRecord.getId(),
                    format,
                    jsonParam.getRemark(),
                    jsonParam.getApproverState()
            ));
            if (updateById != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"报销状态修改失败");
            }
            //更改当前审核状态
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("personnel_code",personnel.getPersonnelCode());
            queryWrapper.eq("reimbursement_record_code",manageReimbursementRecord.getDeclarationCode());
            int update = iReimbursementApprovalMapper.update(new ReimbursementApproval(
                    DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()),
                    jsonParam.getApproverState(),
                    jsonParam.getRemark()
            ), queryWrapper);
            if (update != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"审核结果修改失败");
            }
            //发送给申请人
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("username",manageReimbursementRecord.getAccount());
            SysPersonnel sysPersonnel = iSysPersonnelMapper.selectOne(wrapper);
            PanXiaoZhang.postWechatFer(
                    sysPersonnel.getOpenId(),
                    "审核信息",
                    "",
                    "审核失败",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlReimburse + "from_verify=" + false
            );
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
        }
        return new ReturnEntity(CodeEntity.CODE_ERROR,"审核状态不正确");
    }

    //申请报销信息
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        ManageReimbursementRecord jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementRecord.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageReimbursementRecordNotNull(
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        ""
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //判断该状态是否进行提交数据
        SysPersonnel personnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //获取当前角色职位
        SysRole sysRole = iSysRoleMapper.selectById(personnel.getRoleId());
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
        //生成编码
        jsonParam.setDeclarationCode(System.currentTimeMillis() + PanXiaoZhang.ran(2));
        //存储类目编码
        List<String> manageReimbursementCategoryList = new ArrayList<>();
        //存储职位id
        List<Integer> sysRoleList = new ArrayList<>();
        //存储抄送人id
        List<Integer> copyList = new ArrayList<>();
        //总金额设置为0
        jsonParam.setAmountDeclared(0D);
        //重复提交类目或项目
        Map mapDistnct = new HashMap();
        //添加类目关联
        try {
            //定义项目数组
            Map<Integer,Integer> integerMap = new HashMap<>();
            for (int i = 0; i < reimbursementCategories.size(); i++) {
                //获取当前数据
                ReimbursementCategory reimbursementCategory = reimbursementCategories.get(i);
                //查询当前类目是否存在
                ManageReimbursementCategory manageReimbursementCategory = iManageReimbursementCategoryMapper.selectById(reimbursementCategory.getReimbursementCategoryId());
                if (mapDistnct.get("mCat" + manageReimbursementCategory.getId()) != null){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"重复提交类目" + manageReimbursementCategory.getName());
                }
                mapDistnct.put("mCat" + manageReimbursementCategory.getId(),manageReimbursementCategory.getId());
                //判断类目是否存在
                if (ObjectUtils.isEmpty(manageReimbursementCategory)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,reimbursementCategory.getName() + "该类目不存在");
                }
                //判断金额限制
                Double doubleD = PanXiaoZhang.doubleD(manageReimbursementCategory.getAmount() * sysRole.getNumber(), 2);
                //判断是否超出限制
                if (reimbursementCategory.getAmout() > doubleD){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,manageReimbursementCategory.getName() + "类目的报销上限为" + manageReimbursementCategory.getAmount());
                }
                //获取Map存储的内容
                Integer integer = integerMap.get(reimbursementCategory.getId());
                //如果不存在，将值存储进integerMap和sysManagementList中
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(reimbursementCategory.getId(),reimbursementCategory.getId());
                    manageReimbursementCategoryList.add(manageReimbursementCategory.getCategoryCoding());
                }
                //判断金额是否为负数
                if (reimbursementCategory.getAmout() <= 0){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,reimbursementCategory.getAmout() + "为数字");
                }
                //将当前金额设置小数点为2位
                Double aDouble = PanXiaoZhang.doubleD(reimbursementCategory.getAmout(), 2);
                //进行添加进总金额
                jsonParam.setAmountDeclared(PanXiaoZhang.doubleD(aDouble + jsonParam.getAmountDeclared(),2));
                //唯一编码
                String uuid = UUID.randomUUID().toString();
                //添加报销和类目
                iReimbursementCategoryMapper.insert(new ReimbursementCategory(
                        jsonParam.getDeclarationCode(),//设置报销编码
                        manageReimbursementCategory.getId(),//设置类目编码
                        aDouble,//设置当前金额
                        manageReimbursementCategory.getName(),//设置类目名称
                        jsonParam.getReimbursementType(),//设置类目类型
                        reimbursementCategory.getOccurrenceTime(),//发生时间
                        uuid,//设置唯一编码
                        reimbursementCategory.getRemark()//备注
                ));
                //附件集合
                String[] imagesNumber = reimbursementCategory.getImagesNumber();
                //添加附件
                for (int j = 0; j < imagesNumber.length; j++) {
                    //获取当前数据
                    String imageString = imagesNumber[j];
                    iReimbursementImageMapper.insert(new ReimbursementImage(
                            imageString,
                            uuid
                    ));
                }
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
                    roleMap.put(manageRM.getRoleId(),manageRM.getRoleId());
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
                Integer integer = copyMap.get(categoryCopy.getRoleId());
                //如果不存在，将值存储进roleMap和sysRoleList中
                if (ObjectUtils.isEmpty(integer)){
                    copyMap.put(categoryCopy.getRoleId(),categoryCopy.getRoleId());
                    copyList.add(categoryCopy.getRoleId());
                }
            }
        }catch (Exception e){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"报销项目录入失败");
        }

        //存储项目id
        List<Integer> sysManagementList = new ArrayList<>();
        try {
            //定义项目数组
            Map<Integer,Integer> integerMap = new HashMap<>();
            //定义当前平均金额
            Double amountAge = PanXiaoZhang.doubleD(jsonParam.getAmountDeclared() / reimbursementProjects.size(),2);
            //定义分摊金额
            Double amountDeclared = jsonParam.getAmountDeclared() - PanXiaoZhang.doubleD(PanXiaoZhang.doubleD((jsonParam.getAmountDeclared() / reimbursementProjects.size()) , 2) * (reimbursementProjects.size() - 1),2);
            //添加相关项目
            for (int i = 0; i < reimbursementProjects.size(); i++) {
                //获取当前数据
                ReimbursementProject reimbursementProject = reimbursementProjects.get(i);
                //查询项目是否存在
                SysManagement management = iSysManagementMapper.selectById(reimbursementProject.getId());
                if (ObjectUtils.isEmpty(management)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,reimbursementProject.getName() + "项目不存在");
                }
                if (mapDistnct.get("sysM" + management.getId()) != null){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,management.getName() + "项目不可重复提交");
                }
                mapDistnct.put("sysM" + management.getId(),management.getId());
                //如果可用余额为空
                if (ObjectUtils.isEmpty(management.getAvailableBalance())){
                    management.setAvailableBalance(0D);
                }
                //判断余额
                if (management.getAvailableBalance() < amountDeclared){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,management.getName() + "项目余额为" + management.getAvailableBalance() + "分摊金额为" + PanXiaoZhang.doubleD(amountDeclared,2));
                }
                //获取Map存储的内容
                Integer integer = integerMap.get(management.getId());
                //如果不存在，将值存储进integerMap和sysManagementList中
                if (ObjectUtils.isEmpty(integer)){
                    integerMap.put(management.getId(),management.getId());
                    sysManagementList.add(management.getId());
                }
                //当前所分担的金额
                Double amount;
                //判断当前项目所分担的金额
                if ((i + 1) == reimbursementProjects.size()){
                    amount = amountDeclared;
                }else {
                    amount = amountAge;
                }
                //进行添加
                iReimbursementProjectMapper.insert(new ReimbursementProject(
                        management.getName(),//设置项目名称
                        jsonParam.getDeclarationCode(),//设置报销编码
                        management.getId(),//设置项目编码
                        amount//设置承担金额
                ));
            }
        }catch (Exception e){
            log.info("捕获异常：{}",e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,"项目录入失败");
        }
        //转化为项目id数组
        Integer[] sysManagements = sysManagementList.toArray(new Integer[sysManagementList.size()]);
        //转化为职位id数组
        Integer[] sysRoles = sysRoleList.toArray(new Integer[sysRoleList.size()]);
        //转化为抄送人职位id数组
        Integer[] copyCopies = copyList.toArray(new Integer[copyList.size()]);
        //查询map存储
        Map<String,Object> hashMap = new HashMap();
        //设置已选项目id
        hashMap.put("sysManagements", sysManagements);
        //设置审核职位关联
        hashMap.put("sysRoles",sysRoles);
        //设置抄送人职位关联
        hashMap.put("copyCopies",copyCopies);
        //设置离职筛选
        hashMap.put("employmentStatus",1);
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
        //储存审批人手机号
        List<String> approvalOpenId = new ArrayList<>();
        //添加审核人
        for (int i = 0; i < rmPersonnels.size(); i++) {
            //获取当前数据
            SysPersonnel sysPersonnel = rmPersonnels.get(i);
            //添加审核人
            iReimbursementApprovalMapper.insert(new ReimbursementApproval(
                jsonParam.getDeclarationCode(),
                sysPersonnel.getPersonnelCode(),
                sysPersonnel.getName(),
                sysPersonnel.getSysRole().getLevelSorting(),
                "pending"
            ));
            //获取当前存储
            Integer integer = map.get("approvalInt");
            if (!ObjectUtils.isEmpty(integer)){
                if (integer < sysPersonnel.getSysRole().getLevelSorting()){
                    approvalOpenId.clear();
                    map.put("approvalInt",sysPersonnel.getSysRole().getLevelSorting());
                    approvalOpenId.add(sysPersonnel.getOpenId());
                }else if (integer.equals(sysPersonnel.getSysRole().getLevelSorting())){
                    approvalOpenId.add(sysPersonnel.getOpenId());
                }
            }else {
                map.put("approvalInt",sysPersonnel.getSysRole().getLevelSorting());
                approvalOpenId.add(sysPersonnel.getOpenId());
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
                if (ObjectUtils.isEmpty(jsonParam.getBankAccount())){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"请填写银行卡号");
                }
                if (ObjectUtils.isEmpty(jsonParam.getBankOfDeposit())){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"请填写开户行");
                }
                iManageBankAccountMapper.insert(new ManageBankAccount(
                        personnel.getPersonnelCode(),
                        jsonParam.getBankAccount(),
                        jsonParam.getBankOfDeposit()
                ));
                //设置银行账号
                jsonParam.setBankAccount(jsonParam.getBankAccount());
                //设置银行开户行地址
                jsonParam.setBankOfDeposit(jsonParam.getBankOfDeposit());
            }else {
                //设置银行账号
                jsonParam.setBankAccount(manageBankAccount.getBankAccount());
                //设置银行开户行地址
                jsonParam.setBankOfDeposit(manageBankAccount.getBankOfDeposit());
            }
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
        //赋值当前流转等级
        jsonParam.setMaxNumber(map.get("approvalInt"));
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
        //发送给第一批审批人
        for (int i = 0; i < approvalOpenId.size(); i++) {
            String openId = approvalOpenId.get(i);
            PanXiaoZhang.postWechatFer(
                    openId,
                    "",
                    "",
                    personnel.getName() + ":提交了报销信息,请前往审核",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlReimburse + "from_verify=" + true
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"申请成功");
    }

    //查询当前数据
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        Integer integer = whiteManageReimbursementRecordMapper.queryCount(map);
        ReturnEntity returnEntity = new ReturnEntity(CodeEntity.CODE_SUCCEED, whiteManageReimbursementRecordMapper.queryAll(map), "");
        returnEntity.setCount(integer);
        return returnEntity;
    }

    public static void main(String[] args) {
        Double amountDeclared = 23.21;
        double v = amountDeclared - PanXiaoZhang.doubleD(PanXiaoZhang.doubleD((amountDeclared / 1), 2) * (1 - 1), 2);
        System.out.println(v);

    }
}
