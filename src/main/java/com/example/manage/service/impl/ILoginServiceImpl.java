package com.example.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.SysPersonnelNotNull;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.service.ILoginService;
import com.example.manage.util.HttpUtil;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.TokenUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.Token;
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
            Map<String,Object> map = new HashMap<>();
            map.put("username",jsonParam.getUsername().replaceAll(" ",""));
            map.put("password",PanXiaoZhang.getPassword(jsonParam.getPassword()));
            //查询账号信息
            List<SysPersonnel> sysPersonnels = iSysPersonnelMapper.queryAll(map);
            SysPersonnel sysPersonnel = sysPersonnels.get(0);
            //判断如果账号查到唯一个就成功登录
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR, "账号或密码异常");
            }
            //添加会话
            session.setAttribute("user",sysPersonnel.getId());
            //添加添加角色id
            session.setAttribute("roleId",sysPersonnel.getRoleId());
            //生成token参数设置
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
            if (sysPersonnel.equals(PanXiaoZhang.getPassword(jsonParam.getPassword()))){
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
    }
}
