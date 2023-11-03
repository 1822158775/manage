package com.example.manage.white_list.service.impl;

import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.UnbindRecord;
import com.example.manage.entity.is_not_null.UnbindRecordNotNull;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.mapper.IUnbindRecordMapper;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.RedisUtil;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.white_list.service.IWhiteUnbindRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/10/31
 */
@Slf4j
@Service
public class WhiteUnbindRecordServiceImpl implements IWhiteUnbindRecordService {

    @Resource
    private IUnbindRecordMapper iUnbindRecordMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;
    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                UnbindRecord jsonParam = PanXiaoZhang.getJSONParam(request, UnbindRecord.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                UnbindRecord jsonParam = PanXiaoZhang.getJSONParam(request, UnbindRecord.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改解绑记录表
    private ReturnEntity edit(HttpServletRequest request, UnbindRecord jsonParam) {
        //int updateById = iUnbindRecordMapper.updateById(jsonParam);
        ////当返回值不为1的时候判断修改失败
        //if (updateById != 1){
        //    return new ReturnEntity(
        //            CodeEntity.CODE_ERROR,
        //            jsonParam,
        //            MsgEntity.CODE_ERROR
        //    );
        //}
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加解绑记录表
    private ReturnEntity add(HttpServletRequest request, UnbindRecord jsonParam) {
        ReturnEntity returnEntity = PanXiaoZhang.isNull(jsonParam,
                new UnbindRecordNotNull(
                        "",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "",
                        "",
                        "isNotNullAndIsLengthNot0"
                ));
        if (returnEntity.getState()){
            return returnEntity;
        }
        jsonParam.setAfterTokenCode(jsonParam.getToken_code());
        String token_code = "token_code_personnel" + jsonParam.getUsername();
        Object o = redisUtil.get(token_code);
        jsonParam.setAfterTokenCode(jsonParam.getToken_code());
        redisUtil.set(token_code,jsonParam.getToken_code());
        jsonParam.setUnbindingTime(new Date());
        jsonParam.setFrontTokenCode(String.valueOf(o));
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iUnbindRecordMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
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
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,sysPersonnel,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iUnbindRecordMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iUnbindRecordMapper.queryCount(map));
    }
}