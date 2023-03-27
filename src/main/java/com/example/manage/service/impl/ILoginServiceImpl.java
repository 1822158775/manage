package com.example.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.service.ILoginService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.TokenUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2022/4/14
 */
@Slf4j
@Service
public class ILoginServiceImpl implements ILoginService {
    /**
     * 引入账号管理表的服务层
     */
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;
    @Override
    public ReturnEntity login(HttpServletRequest request, HttpSession session) {
        try {
            SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
            //判断账号和密码是否符合条件
            if (!PanXiaoZhang.isAccount(jsonParam.getUsername()) || !PanXiaoZhang.isPassword(jsonParam.getPassword())){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "账号或密码不存在");
            }
            QueryWrapper<SysPersonnel> wrapper = new QueryWrapper<>();
            wrapper.eq("username",jsonParam.getUsername());
            wrapper.eq("password",PanXiaoZhang.getPassword(jsonParam.getPassword()));
            //查询账号信息
            SysPersonnel sysPersonnel = iSysPersonnelMapper.selectOne(wrapper);
            //判断如果账号查到唯一个就成功登录
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "账号或密码异常");
            }
            //添加会话
            session.setAttribute("user",sysPersonnel.getId());
            //添加添加角色id
            session.setAttribute("roleId",sysPersonnel.getRoleId());
            //生成token参数设置
            Map map = new HashMap();
            map.put("id",sysPersonnel.getId());
            map.put("username",sysPersonnel.getUsername());
            return new ReturnEntity(
                    CodeEntity.CODE_SUCCEED,
                    sysPersonnel,//返回用户所有信息
                    TokenUtil.getoken(map),//进行生成token
                    MsgEntity.CODE_SUCCEED,//进行生成token
                    1,//进行生成token
                    true);
        }catch (Exception e){
            log.info("捕获异常{}",e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }
}
