package com.example.manage.white_list.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManageDimission;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.entity.is_not_null.ManageDimissionNotNull;
import com.example.manage.mapper.IManageDimissionMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.service.IManageDimissionService;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.util.entity.Token;
import com.example.manage.util.wechat.WechatMsg;
import com.example.manage.white_list.service.IWhiteManageDimissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/4/3
 */

@Slf4j
@Service
public class WhiteManageDimissionServiceImpl implements IWhiteManageDimissionService {

    @Resource
    private IManageDimissionMapper iManageDimissionMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private WechatMsg wechatMsg;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                return add(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, e.getMessage());
        }
    }
    //添加数据
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        ManageDimission jsonParam = PanXiaoZhang.getJSONParam(request, ManageDimission.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new ManageDimissionNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());

        //查询当前个人的信息
        if (!returnEntity.getState()){
            //如果查不到人员信息
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
            }
            //将人员资源代码加入进去
            jsonParam.setPersonnelCode(sysPersonnel.getPersonnelCode());
            //将当前所属项目加入
            jsonParam.setManagementId(sysPersonnel.getManagementId());
            //查询该项目主管
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("management_id",jsonParam.getManagementId());
            wrapper.eq("role_id","1");
            List<SysPersonnel> selectList = iSysPersonnelMapper.selectList(wrapper);
            if (selectList.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
            }
            SysPersonnel personnel = selectList.get(0);
            //添加审核人编码
            jsonParam.setApproverPersonnelId(personnel.getId());
            //设置该条数据唯一编码
            jsonParam.setReportCoding("coding" + System.currentTimeMillis() + PanXiaoZhang.ran(2));
        }
        //添加当前报告时间
        jsonParam.setSubmissionTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));
        //添加默认状态
        jsonParam.setApproverState("pending");
        //没有任何问题将数据录入进数据库
        int insert = iManageDimissionMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        Token token = JSONObject.parseObject(PanXiaoZhang.getToken(), Token.class);
        Token phone = JSONObject.parseObject(PanXiaoZhang.getOpenId(jsonParam.getSysPersonnel().getPhone()), Token.class);
        wechatMsg.tuiSongXiaoXi(
                phone.getResponse().getAccess_token(),
                sysPersonnel.getName() + "提交信息",
                "离职审核信息",
                "请前往审核",
                "",
                "5_XBlqDRj5EQpliJcjCBoYrrKNiZAdOU54ZTX8H1Dvg",
                token.getResponse().getAccess_token(),
                "/pages/activities/show/show?id=" + jsonParam.getReportCoding()
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"上报成功");
    }

    private ReturnEntity cat(HttpServletRequest request) {
        return null;
    }
}
