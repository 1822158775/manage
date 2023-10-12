package com.example.manage.config.handle;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @avthor 潘小章
 * @date 2022/4/14
 */
@Configuration//定义此类为配置类
public class InterceptorConfig  implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns拦截的路径
        String[] addPathPatterns = {
                "/api/**"
        };
        //excludePathPatterns排除的路径
        String[] excludePathPatterns = {
                "/api/login/sign","/api/websocket","/api/error/**","/wx/cp/**","/api/file/**"
        };
        //创建用户拦截器对象并指定其拦截的路径和排除的路径
        registry.addInterceptor(new UserInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }
}