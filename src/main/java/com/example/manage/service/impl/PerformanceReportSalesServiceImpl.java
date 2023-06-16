package com.example.manage.service.impl;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.ReturnEntity;
import com.example.manage.entity.PerformanceReportSales;
import com.example.manage.mapper.IPerformanceReportSalesMapper;
import com.example.manage.service.IPerformanceReportSalesService;
import com.example.manage.util.entity.CodeEntity;
import com.example.manage.util.entity.MsgEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023-06-16 11:43:54
 * 业绩关联权益
 */

@Slf4j
@Service
public class PerformanceReportSalesServiceImpl implements IPerformanceReportSalesService {
    @Resource
    private IPerformanceReportSalesMapper iPerformanceReportSalesMapper;

    //方法总管
    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("add")){
                PerformanceReportSales jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReportSales.class);
                return add(request,jsonParam);
            }else if (name.equals("edit")){
                PerformanceReportSales jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReportSales.class);
                return edit(request,jsonParam);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }
    }

    // 修改业绩关联权益
    private ReturnEntity edit(HttpServletRequest request, PerformanceReportSales jsonParam) {
        int updateById = iPerformanceReportSalesMapper.updateById(jsonParam);
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 添加业绩关联权益
    private ReturnEntity add(HttpServletRequest request, PerformanceReportSales jsonParam) {
        //将数据唯一标识设置为空，由系统生成
        jsonParam.setId(null);
        //没有任何问题将数据录入进数据库
        int insert = iPerformanceReportSalesMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,jsonParam,request,MsgEntity.CODE_SUCCEED);
    }

    // 查询模块
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,iPerformanceReportSalesMapper.queryAll(map),request,MsgEntity.CODE_SUCCEED,iPerformanceReportSalesMapper.queryCount(map));
    }
}
