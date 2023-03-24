package com.example.manage.config.handle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.util.GetSpringBean;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.TokenUtil;
import com.example.manage.util.entity.TokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object reglister = session.getAttribute("user");
        log.info("{}会话请求地址{} token{}",reglister,request.getRequestURI(),request.getHeader("token"));
        String url = "/api/error/getBack";
        if (reglister != null && !StringUtils.isEmpty(request.getHeader("token"))) {
            String token = request.getHeader("token");
            TokenEntity tokenEntity = TokenUtil.tokenToOut(token,session);
            //ISysAdminService adminService = GetSpringBean.getBean(ISysAdminService.class);
            if (ObjectUtils.isEmpty(tokenEntity)){
                log.info("token过期");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            //if (sysAdmins.size() != 1 || !sysAdmins.get(0).getState().equals("0")){
            //    logger.info("账号异常");
            //    session.removeAttribute("user");
            //    request.setAttribute("msg", "请重新登录!");
            //    request.getRequestDispatcher(url).forward(request,response);
            //    return false;
            //}
            Integer integer = PanXiaoZhang.tokenExpiration(tokenEntity.getUserAddTime(), tokenEntity.getUserRemove());
            if (integer == 2){
                log.info("token过期");
                request.setAttribute("msg", "请重新登录!");
                request.getRequestDispatcher(url).forward(request,response);
                return false;
            }
            //QueryWrapper<SysRight> sysRightQueryWrapper = new QueryWrapper<>();
            //sysRightQueryWrapper.eq("role_id",sysAdmins.get(0).getRole());
            //sysRightQueryWrapper.eq("right_name",request.getRequestURI());
            //ISysRightService iSysRightService = GetSpringBean.getBean(ISysRightService.class);
            //List<SysRight> sysRights = iSysRightService.selectList(sysRightQueryWrapper);
            //if (sysRights.size() != 1){
            //    logger.info("没有权限");
            //    request.getRequestDispatcher("/api/error/getRight").forward(request,response);
            //    return false;
            //}
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
