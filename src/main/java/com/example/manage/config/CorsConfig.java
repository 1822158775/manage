//package com.example.manage.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @avthor 潘小章
// * @date 2022/4/15
// * 全局跨域处理
// */
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")             // 匹配所有接口
//                .allowCredentials(true)     // 是否发送 Cookie
//                .allowedOriginPatterns("*") // 支持域
//                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"}) // 支持方法
//                .allowedHeaders("*")
//                .exposedHeaders("*");
//    }
//}
//
//
