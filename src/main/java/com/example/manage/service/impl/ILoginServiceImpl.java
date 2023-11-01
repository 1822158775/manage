package com.example.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.LoginRecord;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.ILoginRecordMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.service.ILoginService;
import com.example.manage.util.*;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @Resource
    private ILoginRecordMapper iLoginRecordMapper;
    /**
     * 引入账号管理表的服务层
     */
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public ReturnEntity login(HttpServletRequest request) {
        try {
            SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
            //判断账号和密码是否符合条件
            if (!PanXiaoZhang.isAccount(jsonParam.getUsername()) || !PanXiaoZhang.isPassword(jsonParam.getPassword())){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "账号或密码不存在");
            }
            jsonParam.setUsername(PanXiaoZhang.replaceBlank(jsonParam.getUsername()));
            jsonParam.setPassword(PanXiaoZhang.replaceBlank(jsonParam.getPassword()));
            Map<String,Object> map = new HashMap<>();
            map.put("username",jsonParam.getUsername());
            map.put("password",PanXiaoZhang.getPassword(jsonParam.getPassword()));
            //查询账号信息
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            //判断如果账号查到唯一个就成功登录
            if (sysPersonnels.size() != 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "账号或密码异常");
            }
            SysPersonnel sysPersonnel = sysPersonnels.get(0);
            //生成token参数设置
            map.put("id",sysPersonnel.getId());
            map.put("username",sysPersonnel.getUsername());
            map.put("role",sysPersonnel.getRoleId());
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

    @Override
    public ReturnEntity whiteListLogin(HttpServletRequest request) {
        try {
            SysPersonnel jsonParam = PanXiaoZhang.getJSONParam(request, SysPersonnel.class);
            ReturnEntity returnEntity = PanXiaoZhang.isNull(
                    jsonParam,
                    new SysPersonnelNotNull(
                            "isNotNullAndIsLengthNot0",
                            "isNotNullAndIsLengthNot0")
            );
            if (returnEntity.getState()){
                return returnEntity;
            }
            jsonParam.setUsername(PanXiaoZhang.replaceBlank(jsonParam.getUsername()));
            jsonParam.setPassword(PanXiaoZhang.replaceBlank(jsonParam.getPassword()));
            //判断账号和密码是否符合条件
            if (!PanXiaoZhang.isAccount(jsonParam.getUsername())){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"请输入大于5位小于17位的账号");
            }
            if (!PanXiaoZhang.isPassword(jsonParam.getPassword())){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"请输入大于5位小于17位的密码");
            }
            Map<String,Object> map = new HashMap<>();
            map.put("username",jsonParam.getUsername().replaceAll("[^a-zA-Z0-9]",""));
            //map.put("password",PanXiaoZhang.getPassword(jsonParam.getPassword()));
            map.put("login","login");
            //查询账号信息
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            //判断如果账号查到唯一个就成功登录
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "账号不存在");
            }
            SysPersonnel sysPersonnel = sysPersonnels.get(0);
            //判断密码是否正确
            if (!sysPersonnel.getPassword().equals(PanXiaoZhang.getPassword(jsonParam.getPassword()))){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "密码错误");
            }
            //判断当前账户的状态
            if(sysPersonnel.getEmploymentStatus().equals(0)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"已离职");
            }
            //判断当前账户的状态
            if(sysPersonnel.getEmploymentStatus().equals(2)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"待审核");
            }
            String token_code = "token_code_personnel" + sysPersonnel.getUsername();
            Object token_code_personnel = redisUtil.get(token_code);
            //如果值不存在
            if (!ObjectUtils.isEmpty(token_code_personnel)){//如果值存在
                boolean equals = String.valueOf(token_code_personnel).equals(jsonParam.getToken_code());//进行身份对比
                log.info("比对：{}",equals);
                log.info("token1：{}",token_code_personnel);
                log.info("token2：{}",jsonParam.getToken_code());
                if (!equals){
                    log.info("redis身份异常");
                    return new ReturnEntity("405", "请用上传本人视频，进行解绑");
                }
            }
            if (!ObjectUtils.isEmpty(sysPersonnel.getToken_code())){
                redisUtil.set(token_code,sysPersonnel.getToken_code());
            }
            //判断该用户是否有openID
            if (ObjectUtils.isEmpty(sysPersonnel.getOpenId())){
                String token = request.getHeader("Http-X-User-Access-Token");
                Token parseObject = JSONObject.parseObject(PanXiaoZhang.postOpenId(token), Token.class);
                if (!parseObject.getSuccess()){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"请关注常旅通公众号");
                }
                String openid = parseObject.getResponse().getOpenid();
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("open_id",openid);
                SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
                if (ObjectUtils.isEmpty(personnel)){
                    iSysPersonnelMapper.updateById(new SysPersonnel(
                            sysPersonnel.getId(),
                            null,
                            null,
                            openid
                    ));
                }else {
                    return new ReturnEntity(
                            CodeEntity.CODE_ERROR,
                            "当前微信已绑定其他账号，如有疑问请联系管理员"
                    );
                }
            }
            //登入记录表
            iLoginRecordMapper.insert(new LoginRecord(
                null,
                jsonParam.getUsername(),
                DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()),
                jsonParam.getOpenId(),
                jsonParam.getModel(),
                "",
                sysPersonnel.getPersonnelCode()
            ));

            //PanXiaoZhang.postWechatFer(
            //        openid,
            //        "登入信息",
            //        "",
            //        "账号在上登入",
            //        "",
            //        ""
            //);
            return new ReturnEntity(
                    CodeEntity.CODE_SUCCEED,
                    sysPersonnel,//返回用户所有信息
                    "登入成功"
            );
        }catch (Exception e){
            log.info("捕获异常{}",e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    public static void main(String[] args) {
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("token","sRYZMRiAH9dzMxqrYs72");
        //String send = HttpUtil.send("https://www.topvoyage.top/api/miniapp/v1/zhen_ning/get_openid_by_token", jsonObject.toString(), "");
        //Token token = JSONObject.parseObject(send, Token.class);
        //System.out.println(token);
        System.out.println(PanXiaoZhang.getPassword("null"));

        Token parseObject = JSONObject.parseObject(PanXiaoZhang.postOpenId("o_QtX5koly4ZwFla0_cBznevoEZY"), Token.class);
        System.out.println(parseObject);
    }
}
