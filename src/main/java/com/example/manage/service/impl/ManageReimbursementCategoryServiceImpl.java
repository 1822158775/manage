package com.example.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.CategoryCopy;
import com.example.manage.entity.ManageRM;
import com.example.manage.entity.is_not_null.ManageReimbursementCategoryNotNull;
import com.example.manage.mapper.ICategoryCopyMapper;
import com.example.manage.mapper.IManageRMMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.ManageReimbursementCategory;
import com.example.manage.mapper.IManageReimbursementCategoryMapper;
import com.example.manage.service.IManageReimbursementCategoryService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-04-10 18:23:08
 * 类目管理
 */

@Slf4j
@Service
public class ManageReimbursementCategoryServiceImpl implements IManageReimbursementCategoryService {

    @Resource
    private IManageReimbursementCategoryMapper iManageReimbursementCategoryMapper;

    @Resource
    private ICategoryCopyMapper iCategoryCopyMapper;

    @Resource
    private IManageRMMapper iManageRMMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                ManageReimbursementCategory jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementCategory.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                ManageReimbursementCategory jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementCategory.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                ManageReimbursementCategory jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementCategory.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                ManageReimbursementCategory jsonParam = PanXiaoZhang.getJSONParam(request, ManageReimbursementCategory.class);
                return edit(request,jsonParam);
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }

    // 修改类目管理
    private ReturnEntity edit(HttpServletRequest request, ManageReimbursementCategory jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageReimbursementCategoryNotNull(
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        //查询当前数据
        ManageReimbursementCategory manageReimbursementCategory = iManageReimbursementCategoryMapper.selectById(jsonParam.getId());
        if (ObjectUtils.isEmpty(manageReimbursementCategory)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该数据不存在");
        }
        //如果审核人数组不为空则进行处理
        if (jsonParam.getManageRm() != null){
            //已关联的和已选的集合
            Map<Integer,ManageRM> rmMap = new HashMap<>();
            //存储已关联的集合
            Map<Integer,ManageRM> rmHashMap = new HashMap<>();
            //存储已选中的集合
            Map<Integer,ManageRM> intHashMap = new HashMap<>();
            //查询当前相关连的职位
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("reimbursement_code",manageReimbursementCategory.getCategoryCoding());
            List<ManageRM> list = iManageRMMapper.selectList(wrapper);
            for (int i = 0; i < list.size(); i++) {
                ManageRM manageRM = list.get(i);
                rmMap.put(manageRM.getRoleId(),manageRM);
                rmHashMap.put(manageRM.getRoleId(),manageRM);
            }
            //将已选中的值添加进rmMap
            for (int i = 0; i < jsonParam.getManageRm().length; i++) {
                Integer integer = jsonParam.getManageRm()[i];
                rmMap.put(integer,new ManageRM(
                        manageReimbursementCategory.getCategoryCoding(),
                        integer
                ));
                intHashMap.put(integer,new ManageRM(
                        manageReimbursementCategory.getCategoryCoding(),
                        integer
                ));
            }
            //进行遍历全部
            for(Map.Entry<Integer,ManageRM> entry:rmMap.entrySet()){
                //System.out.println(entry.getKey());
                //System.out.println(entry.getValue());
                //已关联的
                ManageRM rmHash = rmHashMap.get(entry.getKey());
                //已选中的
                ManageRM intHash = intHashMap.get(entry.getKey());
                if (!ObjectUtils.isEmpty(rmHash) && ObjectUtils.isEmpty(intHash)){//如果已关联的存在但是已选中的不存在执行删除操作
                    iManageRMMapper.deleteById(rmHash.getId());
                }else if (ObjectUtils.isEmpty(rmHash) && !ObjectUtils.isEmpty(intHash)){//如果已关联的不存在但是已选中的存在则执行添加
                    iManageRMMapper.insert(intHash);
                }
            }
        }
        //如果抄送人数组不为空
        if (!ObjectUtils.isEmpty(jsonParam.getCategoryCopyNumber())){
            //已关联的和已选的集合
            Map<Integer,CategoryCopy> ccMap = new HashMap<>();
            //存储已关联的集合
            Map<Integer,CategoryCopy> ccHashMap = new HashMap<>();
            //存储已选中的集合
            Map<Integer,CategoryCopy> intHashMap = new HashMap<>();
            //查询当前相关连的职位
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("reimbursement_code",manageReimbursementCategory.getCategoryCoding());
            List<CategoryCopy> list = iCategoryCopyMapper.selectList(wrapper);
            for (int i = 0; i < list.size(); i++) {
                CategoryCopy categoryCopy = list.get(i);
                ccMap.put(categoryCopy.getRoleId(),categoryCopy);
                ccHashMap.put(categoryCopy.getRoleId(),categoryCopy);
            }
            //将已选中的值添加进rmMap
            for (int i = 0; i < jsonParam.getCategoryCopyNumber().length; i++) {
                Integer integer = jsonParam.getCategoryCopyNumber()[i];
                ccMap.put(integer,new CategoryCopy(
                        manageReimbursementCategory.getCategoryCoding(),
                        integer
                ));
                intHashMap.put(integer,new CategoryCopy(
                        manageReimbursementCategory.getCategoryCoding(),
                        integer
                ));
            }
            //进行遍历全部
            for(Map.Entry<Integer,CategoryCopy> entry : ccMap.entrySet()){
                //System.out.println(entry.getKey());
                //System.out.println(entry.getValue());
                //已关联的
                CategoryCopy rmHash = ccHashMap.get(entry.getKey());
                //已选中的
                CategoryCopy intHash = intHashMap.get(entry.getKey());
                if (!ObjectUtils.isEmpty(rmHash) && ObjectUtils.isEmpty(intHash)){//如果已关联的存在但是已选中的不存在执行删除操作
                    iCategoryCopyMapper.deleteById(rmHash.getId());
                }else if (ObjectUtils.isEmpty(rmHash) && !ObjectUtils.isEmpty(intHash)){//如果已关联的不存在但是已选中的存在则执行添加
                    iCategoryCopyMapper.insert(intHash);
                }
            }
        }
        //编码不可修改
        jsonParam.setCategoryCoding(null);
        int updateById = iManageReimbursementCategoryMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加类目管理
    private ReturnEntity add(HttpServletRequest request, ManageReimbursementCategory jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageReimbursementCategoryNotNull(
                        "isNotNullAndIsLengthNot0",
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
        //添加编码
        jsonParam.setCategoryCoding(System.currentTimeMillis() + PanXiaoZhang.ran(2));
        //添加审核人职位
        for (int i = 0; i < jsonParam.getManageRm().length; i++) {
            Integer integer = jsonParam.getManageRm()[i];
            iManageRMMapper.insert(new ManageRM(
                    jsonParam.getCategoryCoding(),
                    integer
            ));
        }
        //添加抄送人
        for (int i = 0; i < jsonParam.getCategoryCopyNumber().length; i++) {
            Integer integer = jsonParam.getCategoryCopyNumber()[i];
            iCategoryCopyMapper.insert(new CategoryCopy(
                    jsonParam.getCategoryCoding(),
                    integer
            ));
        }
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iManageReimbursementCategoryMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iManageReimbursementCategoryMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iManageReimbursementCategoryMapper.queryCount(map));
    }
}
