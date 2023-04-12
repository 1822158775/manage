package com.example.manage.white_list.service.impl;

import com.example.manage.mapper.IWhiteManageReimbursementCategoryMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteManageReimbursementCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/4/12
 * 类目管理
 */

@Slf4j
@Service
public class WhiteManageReimbursementCategoryServiceImpl implements IWhiteManageReimbursementCategoryService {

    @Resource
    private IWhiteManageReimbursementCategoryMapper iWhiteManageReimbursementCategoryMapper;

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

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iWhiteManageReimbursementCategoryMapper.queryAll(map),MsgEntity.CODE_SUCCEED);
    }
}
