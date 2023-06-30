package com.example.manage.config.handle;


import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.SysTableAuthority;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.ISysTableAuthorityMapper;
import com.example.manage.service.ISysPersonnelService;
import com.example.manage.service.ISysTableAuthorityService;
import com.example.manage.util.GetSpringBean;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.TokenUtil;
import com.example.manage.util.entity.TokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        HttpSession session = request.getSession();
        Object reglister = session.getAttribute("user");
        String url = "/api/error/getBack";
        if (!ObjectUtils.isEmpty(reglister) && !reglister.equals("null") && !StringUtils.isEmpty(request.getHeader("token"))) {
            String roleId = String.valueOf(session.getAttribute("roleId"));
            String userId = String.valueOf(reglister);
            String requestURI = request.getRequestURI();
            log.info("账号编码:{}会话请求地址:{},token:{}",userId,requestURI,request.getHeader("token"));
            session.setMaxInactiveInterval(240 * 60);
            String token = request.getHeader("token");
            TokenEntity tokenEntity = TokenUtil.tokenToOut(token,session);
            if (ObjectUtils.isEmpty(tokenEntity)){
                log.info("token过期");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            ISysPersonnelMapper iSysPersonnelMapper = GetSpringBean.getBean(ISysPersonnelMapper.class);
            SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(userId);
            if (ObjectUtils.isEmpty(sysPersonnel)){
                log.info("账号异常");
                session.removeAttribute("user");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            Integer integer = PanXiaoZhang.tokenExpiration(tokenEntity.getUserAddTime(), tokenEntity.getUserRemove());
            if (integer == 2){
                log.info("token过期");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            ISysTableAuthorityMapper iSysTableAuthorityMapper = GetSpringBean.getBean(ISysTableAuthorityMapper.class);
            String[] split = requestURI.split("/");
            Map<String,String> map = new HashMap<>();
            map.put("authorityState","1");
            map.put("roleId",roleId);
            map.put("tableName",split[2]);
            List<SysTableAuthority> sysTableAuthorities = iSysTableAuthorityMapper.queryAll(map);
            if (sysTableAuthorities.size() != 1){
                log.info("没有权限");
                request.getRequestDispatcher("/api/error/getRight").forward(request,response);
                return false;
            }
            return true;
        } else {
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
