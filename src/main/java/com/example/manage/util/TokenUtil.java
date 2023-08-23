package com.example.manage.util;

import com.example.manage.util.entity.TokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2022/4/15
 */
@Slf4j
public class TokenUtil {
    public static String getoken(Map map) {
        //Jwts.builder()生成
        //Jwts.parser()验证
        JwtBuilder jwtBuilder =  Jwts.builder()
                .setId(map.get("id").toString())
                .setSubject(map.get("username").toString())    //用户名
                .setIssuedAt(new Date())//登录时间
                .signWith(SignatureAlgorithm.HS256, "my-123")
                .setExpiration(new Date(System.currentTimeMillis()+20800000))
                .setAudience(map.get("role").toString());
        //设置过期时间
        //前三个为载荷playload 最后一个为头部 header
        //System.out.println(jwtBuilder.compact());
        return  jwtBuilder.compact();
    }
    public static TokenEntity tokenToOut(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("my-123")
                    .parseClaimsJws(token)
                    .getBody();
            //System.out.println("用户id:"+claims.getId());
            //System.out.println("用户名:"+claims.getSubject());
            //System.out.println("用户时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
            //        format(claims.getIssuedAt()));
            //System.out.println("过期时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
            //        format(claims.getExpiration()));
            //System.out.println("用户角色:"+claims.get("role"));
            //if (!user.toString().equals(claims.getId())){
            //    log.info("token和预期不符");
            //    return null;
            //}
            return new TokenEntity(
                    claims.getId(),
                    claims.getSubject(),
                    claims.getIssuedAt(),
                    claims.getExpiration(),
                    claims.getAudience());
        }catch (Exception e){
            log.info("Token过期");
            return null;
        }
    }

    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis() + 20800000);
        System.out.println(DateFormatUtils.format(date,PanXiaoZhang.yMdHms()));
    }
    public static Boolean StringTokenOut(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("my-123")
                    .parseClaimsJws(token)
                    .getBody();
            //System.out.println("用户id:"+claims.getId());
            //System.out.println("用户名:"+claims.getSubject());
            //System.out.println("用户时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
            //        format(claims.getIssuedAt()));
            //System.out.println("过期时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
            //        format(claims.getExpiration()));
            //System.out.println("用户角色:"+claims.get("role"));
            Integer integer = PanXiaoZhang.tokenExpiration(claims.getIssuedAt(), claims.getExpiration());
            if (integer == 2){
                log.info("token过期");
                return false;
            }else {
                return true;
            }
            //return null;
        }catch (Exception e){
            log.info("Token过期");
            return false;
        }
    }
}
