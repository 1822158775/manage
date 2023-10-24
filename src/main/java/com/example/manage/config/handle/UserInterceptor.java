package com.example.manage.config.handle;


import com.alibaba.fastjson.JSONObject;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.SysTableAuthority;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.ISysTableAuthorityMapper;
import com.example.manage.service.IRedisUtilService;
import com.example.manage.service.ISysPersonnelService;
import com.example.manage.service.ISysTableAuthorityService;
import com.example.manage.util.*;
import com.example.manage.util.entity.TokenEntity;
import com.example.manage.util.entity.TokenPersonnel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2022/4/14
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = "/api/error/getBack";
        String token_error = "/api/error/token_error";
        String token_error2 = "/api/error/token_error2";
        String white_list = "/api/white_list/";
        String requestURI = request.getRequestURI();
        log.info("requestURI:{}" + requestURI);
        if (requestURI.length() > white_list.length() && requestURI.substring(0, white_list.length()).contains(white_list)){
            //进行json数据转化
            TokenPersonnel tokenPersonnel = new TokenPersonnel(
                    String.valueOf(request.getHeader("personnelId")),
                    String.valueOf(request.getHeader("token_code"))
            );
            log.info("数据打印:{}",tokenPersonnel);
            if (ObjectUtils.isEmpty(tokenPersonnel)){
                log.info("缺少关键数据");
                request.getRequestDispatcher(token_error).forward(request,response);
                return false;
            }
            //进行判断关键的数据是否有值
            if (ObjectUtils.isEmpty(tokenPersonnel.getToken_code()) || ObjectUtils.isEmpty(tokenPersonnel.getPersonnelId())){
                log.info("token_code:{},personnelId:{}",tokenPersonnel.getToken_code(),tokenPersonnel.getPersonnelId());
                request.getRequestDispatcher(token_error2).forward(request,response);
                return false;
            }
            //进行连接数据库进行操作
            ISysPersonnelMapper sysPersonnelMapper = GetSpringBean.getBean(ISysPersonnelMapper.class);
            SysPersonnel personnel = sysPersonnelMapper.selectById(tokenPersonnel.getPersonnelId());
            //如果没有查到此用户
            if (ObjectUtils.isEmpty(personnel)){
                log.info("没有查到该用户");
                request.getRequestDispatcher(token_error).forward(request,response);
                return false;
            }
            String token_code = "token_code_personnel" + personnel.getUsername();
            ////查询redis身份绑定code
            RedisUtil redisUtil = GetSpringBean.getBean(RedisUtil.class);
            Object token_code_personnel = redisUtil.get(token_code);
            //如果值不存在
            if (ObjectUtils.isEmpty(token_code_personnel)){
                redisUtil.set(token_code,tokenPersonnel.getToken_code());
                redisUtil.set(tokenPersonnel.getToken_code(),token_code);
                return true;
            }else {//如果值存在
                boolean equals = String.valueOf(token_code_personnel).equals(tokenPersonnel.getToken_code());//进行身份对比
                if (equals){
                    return true;
                }else {
                    log.info("redis身份异常");
                    request.getRequestDispatcher(token_error).forward(request,response);
                    return false;
                }
            }
        }
        log.info("后台");
        if (!StringUtils.isEmpty(request.getHeader("token"))) {
            log.info("会话请求地址:{},token:{}",requestURI,request.getHeader("token"));
            String token = request.getHeader("token");
            TokenEntity tokenEntity = TokenUtil.tokenToOut(token);
            if (ObjectUtils.isEmpty(tokenEntity)){
                log.info("token过期");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            String roleId = String.valueOf(tokenEntity.getUserRole());
            Integer integer = PanXiaoZhang.tokenExpiration(tokenEntity.getUserAddTime(), tokenEntity.getUserRemove());
            if (integer == 2){
                log.info("token过期");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            String[] split = requestURI.split("/");
            Map<String,String> map = new HashMap<>();
            map.put("authorityState","1");
            map.put("roleId",roleId);
            map.put("tableName",split[2]);
            ISysTableAuthorityMapper iSysTableAuthorityMapper = GetSpringBean.getBean(ISysTableAuthorityMapper.class);
            List<SysTableAuthority> sysTableAuthorities = iSysTableAuthorityMapper.queryAll(map);
            if (sysTableAuthorities.size() != 1){
                log.info("没有权限");
                request.getRequestDispatcher("/api/error/getRight").forward(request,response);
                return false;
            }
            return true;
        }else {
            log.info("没有会话或token");
            request.setAttribute("msg", "请先登录!");
            request.getRequestDispatcher(url).forward(request,response);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
