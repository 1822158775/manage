package com.example.manage.white_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.*;
import com.example.manage.entity.is_not_null.PerformanceReportNotNull;
import com.example.manage.entity.number.AuditDataNumber;
import com.example.manage.entity.number.PerformanceReportNumber;
import com.example.manage.entity.ranking_list.RankingList;
import com.example.manage.mapper.*;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.*;
import com.example.manage.util.excel.ExcelExportUtil;
import com.example.manage.util.excel.ExportTool;
import com.example.manage.util.excel.XlsxLayoutReader;
import com.example.manage.util.file.config.UploadFilePathConfig;
import com.example.manage.white_list.service.IWhitePerformanceReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 * 业绩上报
 */

@Slf4j
@Service
public class WhitePerformanceReportServiceImpl implements IWhitePerformanceReportService {

    @Value("${role.manage}")
    private Integer roleId;

    @Value("${role.manage3}")
    private Integer manage3;

    @Value("${role.manage5}")
    private Integer manage5;

    @Value("${url.transfer}")
    private String urlTransfer;

    @Value("${url.performance}")
    private String urlPerformance;

    @Resource
    private UploadFilePathConfig uploadFilePathConfig;

    /*上传目录*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.ap:}")
    private String ap;

    @Resource
    private IPerformanceReportMapper iPerformanceReportMapper;

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private NumberPerformanceReportMapper numberPerformanceReportMapper;

    @Resource
    private INumberAuditDataMapper iNumberAuditDataMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    @Resource
    private WhiteSysPersonnelMapper whiteSysPersonnelMapper;

    @Resource
    private ISysManagementMapper iSysManagementMapper;

    @Resource
    private ICardTypeMapper iCardTypeMapper;

    @Resource
    private WhiteRankingListMapper whiteRankingListMapper;

    @Resource
    private IPerformanceReportSalesMapper iPerformanceReportSalesMapper;

    @Resource
    private IDivisionTypeMapper divisionTypeMapper;

    @Resource
    private IDivisionTypeManagementMapper iDivisionTypeManagementMapper;

    @Resource
    private IDivisionManagementMapper iDivisionManagementMapper;

    @Resource
    private IDivisionManagementPersonnelMapper iDivisionManagementPersonnelMapper;

    @Resource
    private ExportTool exportTool;

    @Resource
    private IDirectProjectMapper iDirectProjectMapper;

    @Override
    public ReturnEntity methodMaster(HttpServletRequest request, String name) {
        try {
            if (name.equals("cat")){
                return cat(request);
            }else if (name.equals("cat_number")){
                return cat_number(request);
            }else if (name.equals("cat_audit_number")){
                return cat_audit_number(request);
            }else if (name.equals("cat_audit")){
                return cat_audit(request);
            }else if (name.equals("cat_xlsx")){
                return cat_xlsx(request);
            }else if (name.equals("cat_month_xlsx")){
                return cat_month_xlsx(request);
            }
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //导出月数据
    private ReturnEntity cat_month_xlsx(HttpServletRequest request) throws IOException {

        Map map = new HashMap();
        List<PerformanceReportSales> salesList = iPerformanceReportSalesMapper.queryListManagementCardType(map);
        for (int i = 0; i < salesList.size(); i++) {
            PerformanceReportSales reportSales = salesList.get(i);
            System.out.println(reportSales + "=========================");
        }

        return null;

        //PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        ////字符串Map
        //Map<String, String> stringMap = new HashMap<>();
        //SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        ////判断当前人员状态
        //ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        //if (estimateState.getState()){
        //    return estimateState;
        //}
        ////初始化抬头
        //List<GetExcel> getExcels = ExcelExportUtil.initMonth(stringMap, "月");
        ////通用in集合
        //ArrayList<Integer> arrayList = new ArrayList<>();
        ////查询部门类型
        //List<DivisionType> divisionTypes = divisionTypeMapper.selectList(null);
        ////行数
        //int countRow = 3;
        ////下标
        //int index = 0;
        //int row = countRow;
        //for (int i = 0; i < divisionTypes.size(); i++) {
        //    List<GetExcel> typeExcels = new ArrayList<>();
        //    DivisionType divisionType = divisionTypes.get(i);
        //    //查询部门类型关联的部门
        //    QueryWrapper wrapper = new QueryWrapper();
        //    wrapper.eq("division_type_id",divisionType.getId());
        //    List<DivisionTypeManagement> divisionTypeManagements = iDivisionTypeManagementMapper.selectList(wrapper);
        //    arrayList.clear();
        //    for (int j = 0; j < divisionTypeManagements.size(); j++) {
        //        DivisionTypeManagement divisionTypeManagement = divisionTypeManagements.get(j);
        //        arrayList.add(divisionTypeManagement.getDivisionManagementId());
        //    }
        //    //查询相关部门
        //    wrapper = new QueryWrapper();
        //    wrapper.in("id",arrayList.toArray(new Integer[arrayList.size()]));
        //    List<DivisionManagement> divisionManagements = iDivisionManagementMapper.selectList(wrapper);
        //    //进件
        //    Integer divisionTypeSumCountNumber = 0;
        //    //批核
        //    Integer divisionTypeSumApproved = 0;
        //    //激活
        //    Integer divisionTypeSumActivation = 0;
        //    //项目指标
        //    Integer divisionTypeSumMonthlyIndicators = 0;
        //    //部门关联人员人数
        //    Integer personnelNumber = 0;
        //    for (int j = 0; j < divisionManagements.size(); j++) {
        //        List<GetExcel> excels = new ArrayList<>();
        //        //部门
        //        int divisionRow = countRow;
        //        DivisionManagement divisionManagement = divisionManagements.get(j);
        //        wrapper = new QueryWrapper();
        //        wrapper.eq("division_management_id",divisionManagement.getId());
        //        List<DivisionManagementPersonnel> personnelList = iDivisionManagementPersonnelMapper.selectList(wrapper);
        //        personnelNumber = personnelList.size();
        //        //进件
        //        Integer divisionSumCountNumber = 0;
        //        //批核
        //        Integer divisionSumApproved = 0;
        //        //激活
        //        Integer divisionSumActivation = 0;
        //        //项目指标
        //        Integer divisionSumMonthlyIndicators = 0;
        //        //查询部门关联经理
        //        for (int k = 0; k < personnelList.size(); k++) {
        //            DivisionManagementPersonnel managementPersonnel = personnelList.get(k);
        //            //查询该项目区域经理
        //            Map map = new HashMap();
        //            map.put("roleId",manage3);
        //            map.put("personnelId",managementPersonnel.getPersonnelId());
        //            List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
        //            if (sysPersonnels.size() < 1){
        //                List<SysManagement> managements = new ArrayList<>();
        //                List<DirectProject> list = iDirectProjectMapper.selectList(wrapper);
        //                for (int l = 0; l < list.size(); l++) {
        //                    DirectProject directProject = list.get(l);
        //                    managements.add(new SysManagement(
        //                            directProject.getManagementId(),
        //                            null
        //                    ));
        //                }
        //                SysPersonnel personnel = new SysPersonnel(
        //                        null,
        //                        divisionManagement.getName(),
        //                        null,
        //                        null
        //                );
        //                personnel.setSysManagement(managements);
        //                sysPersonnels.add(personnel);
        //            }
        //            map.clear();
        //
        //            for (int l = 0; l < sysPersonnels.size(); l++) {
        //                SysPersonnel personnel = sysPersonnels.get(l);
        //                List<SysManagement> sysManagement = personnel.getSysManagement();
        //                arrayList.clear();
        //                for (int m = 0; m < sysManagement.size(); m++) {
        //                    SysManagement management = sysManagement.get(m);
        //                    arrayList.add(management.getId());
        //                }
        //                map.put("inManagementId",arrayList.toArray(new Integer[arrayList.size()]));
        //                List<RankingList> queryAllCount = whiteRankingListMapper.queryAllCount(map);
        //                //进件
        //                Integer sumCountNumber = 0;
        //                //批核
        //                Integer sumApproved = 0;
        //                //激活
        //                Integer sumActivation = 0;
        //                //项目指标
        //                Integer sumMonthlyIndicators = 0;
        //                for (int m = 0; m < queryAllCount.size(); m++) {
        //                    int col = 2;
        //                    RankingList rankingList = queryAllCount.get(m);
        //                    //进件divisionS
        //                    Integer countNumber = rankingList.getCountNumber();
        //                    countNumber = ObjectUtils.isEmpty(countNumber) ? 0 : countNumber;
        //                    sumCountNumber += countNumber;
        //                    divisionSumCountNumber += countNumber;
        //                    divisionTypeSumCountNumber += countNumber;
        //                    //批核
        //                    Integer approved = rankingList.getApproved();
        //                    approved = ObjectUtils.isEmpty(approved) ? 0 : approved;
        //                    sumApproved += approved;
        //                    divisionSumApproved += approved;
        //                    divisionTypeSumApproved += approved;
        //                    //激活
        //                    Integer activation = rankingList.getActivation();
        //                    activation = ObjectUtils.isEmpty(activation) ? 0 : activation;
        //                    sumActivation += activation;
        //                    divisionSumActivation += activation;
        //                    divisionTypeSumActivation += activation;
        //                    //项目指标
        //                    Integer monthlyIndicators = rankingList.getMonthlyIndicators();
        //                    monthlyIndicators = ObjectUtils.isEmpty(monthlyIndicators) ? 0 : monthlyIndicators;
        //                    sumMonthlyIndicators += monthlyIndicators;
        //                    divisionSumMonthlyIndicators += monthlyIndicators;
        //                    divisionTypeSumMonthlyIndicators += monthlyIndicators;
        //
        //                    excels.add(XlsxLayoutReader.text(rankingList.getName(), countRow, countRow, col, col));
        //                    col++;
        //                    excels.add(XlsxLayoutReader.text(rankingList.getCardTypeName(), countRow, countRow, col, col));
        //                    col++;
        //                    excels.add(XlsxLayoutReader.text(String.valueOf(countNumber), countRow, countRow, col, col));
        //                    col++;
        //                    excels.add(XlsxLayoutReader.text(String.valueOf(approved), countRow, countRow, col, col));
        //                    col++;
        //                    excels.add(XlsxLayoutReader.text(String.valueOf(activation), countRow, countRow, col, col));
        //                    col++;
        //                    excels.add(XlsxLayoutReader.text(String.valueOf(monthlyIndicators), countRow, countRow, col, col));
        //                    col++;
        //                    excels.add(XlsxLayoutReader.text(PanXiaoZhang.percent(activation,monthlyIndicators,1,100)+"%", countRow, countRow, col, col));
        //                    countRow++;
        //                }
        //
        //                excels.add(XlsxLayoutReader.text(personnel.getName() + "合计", countRow, countRow, 2, 3,ExcelExportUtil.color249()));
        //                excels.add(XlsxLayoutReader.text(String.valueOf(sumCountNumber), countRow, countRow, 4, 4,ExcelExportUtil.color249()));
        //                excels.add(XlsxLayoutReader.text(String.valueOf(sumApproved), countRow, countRow, 5, 5,ExcelExportUtil.color249()));
        //                excels.add(XlsxLayoutReader.text(String.valueOf(sumActivation), countRow, countRow, 6, 6,ExcelExportUtil.color249()));
        //                excels.add(XlsxLayoutReader.text(String.valueOf(sumMonthlyIndicators), countRow, countRow, 7, 7,ExcelExportUtil.color249()));
        //                excels.add(XlsxLayoutReader.text(PanXiaoZhang.percent(sumActivation,sumMonthlyIndicators,1,100)+"%", countRow, countRow, 8, 8,ExcelExportUtil.color249()));
        //
        //                excels.add(index,XlsxLayoutReader.text(personnel.getName(), row, countRow, 1, 1));
        //                countRow++;
        //                row = countRow;
        //            }
        //        }
        //        //部门
        //        excels.add(index, XlsxLayoutReader.text(divisionManagement.getName(), divisionRow, countRow, 0, 0));
        //
        //        if (personnelNumber > 1){
        //            excels.add(XlsxLayoutReader.text(divisionManagement.getName() + "总合计", countRow, countRow, 2, 3,ExcelExportUtil.color249()));
        //            excels.add(XlsxLayoutReader.text(String.valueOf(divisionSumCountNumber), countRow, countRow, 4, 4,ExcelExportUtil.color249()));
        //            excels.add(XlsxLayoutReader.text(String.valueOf(divisionSumApproved), countRow, countRow, 5, 5,ExcelExportUtil.color249()));
        //            excels.add(XlsxLayoutReader.text(String.valueOf(divisionSumActivation), countRow, countRow, 6, 6,ExcelExportUtil.color249()));
        //            excels.add(XlsxLayoutReader.text(String.valueOf(divisionSumMonthlyIndicators), countRow, countRow, 7, 7,ExcelExportUtil.color249()));
        //            excels.add(XlsxLayoutReader.text(PanXiaoZhang.percent(divisionSumActivation,divisionSumMonthlyIndicators,1,100)+"%", countRow, countRow, 8, 8,ExcelExportUtil.color249()));
        //            countRow++;
        //        }
        //        getExcels.add(new GetExcel(
        //                divisionRow,
        //                countRow,
        //                excels,
        //                false
        //        ));
        //    }
        //    if (divisionTypeManagements.size() > 1){
        //        typeExcels.add(XlsxLayoutReader.text(divisionType.getName() + "总合计", countRow, countRow, 2, 3,ExcelExportUtil.color249()));
        //        typeExcels.add(XlsxLayoutReader.text(String.valueOf(divisionTypeSumCountNumber), countRow, countRow, 4, 4,ExcelExportUtil.color249()));
        //        typeExcels.add(XlsxLayoutReader.text(String.valueOf(divisionTypeSumApproved), countRow, countRow, 5, 5,ExcelExportUtil.color249()));
        //        typeExcels.add(XlsxLayoutReader.text(String.valueOf(divisionTypeSumActivation), countRow, countRow, 6, 6,ExcelExportUtil.color249()));
        //        typeExcels.add(XlsxLayoutReader.text(String.valueOf(divisionTypeSumMonthlyIndicators), countRow, countRow, 7, 7,ExcelExportUtil.color249()));
        //        typeExcels.add(XlsxLayoutReader.text(PanXiaoZhang.percent(divisionTypeSumActivation,divisionTypeSumMonthlyIndicators,1,100)+"%", countRow, countRow, 8, 8,ExcelExportUtil.color249()));
        //        countRow++;
        //        getExcels.add(new GetExcel(
        //                countRow,
        //                countRow,
        //                typeExcels,
        //                false
        //        ));
        //    }
        //}
        //String xlsx = exportTool.xlsx(getExcels);
        //System.out.println(xlsx + "=================");
        //return new ReturnEntity(CodeEntity.CODE_SUCCEED,MsgEntity.CODE_SUCCEED);
    }

    //导出日数据
    private ReturnEntity cat_xlsx(HttpServletRequest request) throws IOException {
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        QueryWrapper wrapper = new QueryWrapper();
        //查询所有卡种
        wrapper.ne("name","测试信用卡");
        List<CardType> cardTypes = iCardTypeMapper.selectList(wrapper);
        //查询当前人员关联项目组
        wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
        if (managementPersonnels.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目组");
        }
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = 0; i < managementPersonnels.size(); i++) {
            ManagementPersonnel managementPersonnel = managementPersonnels.get(i);
            integerArrayList.add(managementPersonnel.getManagementId());
        }
        Integer[] toArray = integerArrayList.toArray(new Integer[integerArrayList.size()]);
        //存储内容
        HashMap<String, String> stringMap = new HashMap<>();
        //存储内容
        HashMap<String, Integer> integerMap = new HashMap<>();
        //查询该项目主管
        Map map = new HashMap();
        map.put("inManagementId",toArray);
        map.put("roleId",roleId);
        List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < sysPersonnels.size(); i++) {
            SysPersonnel personnel = sysPersonnels.get(i);
            List<SysManagement> sysManagement = personnel.getSysManagement();
            for (int j = 0; j < sysManagement.size(); j++) {
                SysManagement management = sysManagement.get(j);
                stringMap.put("personnel" + management.getId(),personnel.getName());
                stringMap.put("management" + management.getId(),management.getName());
                arrayList.add(management.getId());
            }
        }
        map.put("inManagementId",arrayList.toArray(new Integer[arrayList.size()]));
        map.put("type","month");
        //本月数据
        List<RankingList> rankingLists = whiteRankingListMapper.queryCount(map);
        for (int i = 0; i < rankingLists.size(); i++) {
            RankingList rankingList = rankingLists.get(i);
            stringMap.put("monthCardType" + rankingList.getCardTypeId(),String.valueOf(rankingList.getActivation()));
        }
        if (ObjectUtils.isEmpty(jsonParam.getRiqiDay())){
            map.put("type","day");
            jsonParam.setRiqiDay("今日");
        }else if (jsonParam.getRiqiDay().equals("day")){
            jsonParam.setRiqiDay("今日");
        }else if (jsonParam.getRiqiDay().equals("week")){
            jsonParam.setRiqiDay("本周");
        }else if (jsonParam.getRiqiDay().equals("month")){
            jsonParam.setRiqiDay("本月");
        }else {
            jsonParam.setRiqiDay("自定义时间");
            map.put("type",jsonParam.getRiqiDay());

            Object thisStartTime = map.get("startTime");
            if (ObjectUtils.isEmpty(thisStartTime)){
                thisStartTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());
            }

            Object thisEndTime = map.get("endTime");
            if (ObjectUtils.isEmpty(thisEndTime)){
                thisEndTime = PanXiaoZhang.GetNextDay(String.valueOf(thisStartTime), 0);
            }

            Long dayTime = PanXiaoZhang.getDayTime(String.valueOf(thisStartTime), String.valueOf(thisEndTime));

            if (dayTime > 31){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"查询天数最多一个月");
            }

            if (dayTime < 0){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"开始时间不可以大于结束时间");
            }

            map.put("thisStartTime",thisStartTime + " 00:00:00");
            map.put("thisEndTime",thisEndTime + " 23:59:59");
            stringMap.put("thisStartTime",thisStartTime + " 00:00:00");
            stringMap.put("thisEndTime",thisEndTime + " 23:59:59");
        }

        //List<PerformanceReportSales> salesList = iPerformanceReportSalesMapper.queryListManagementCardType(map);
        //for (int i = 0; i < salesList.size(); i++) {
        //    PerformanceReportSales reportSales = salesList.get(i);
        //    System.out.println(reportSales + "=========================");
        //}
        //
        //return null;

        //查询签到人数
        List<RankingList> queryPunchingCount = whiteRankingListMapper.queryPunchingCount(map);
        for (int i = 0; i < queryPunchingCount.size(); i++) {
            RankingList rankingList = queryPunchingCount.get(i);
            integerMap.put("attendance" + rankingList.getId(),rankingList.getAttendance());
            integerMap.put("numberOfPeople" + rankingList.getId(),rankingList.getNumberOfPeople());
        }
        //本月数据
        List<RankingList> queryCount = whiteRankingListMapper.queryCount(map);
        for (int i = 0; i < queryCount.size(); i++) {
            RankingList rankingList = queryCount.get(i);
            stringMap.put("dayCardType" + rankingList.getCardTypeId(),String.valueOf(rankingList.getActivation()));
        }
        List<GetExcel> getExcels = ExcelExportUtil.init(stringMap,ap,cardTypes,jsonParam.getRiqiDay());
        int row = 7;
        //详情数据
        for (int i = 0; i < cardTypes.size(); i++) {
            CardType cardType = cardTypes.get(i);
            map.put("manageCardTypeId",cardType.getId());
            List<RankingList> queryAllCount = whiteRankingListMapper.queryAllCount(map);

            Integer countNumber = 0;
            Integer approved = 0;
            Integer activation = 0;

            Integer countAttendance = 0;
            Integer countNumberOfPeople = 0;
            for (int j = 0; j < queryAllCount.size(); j++) {
                RankingList rankingList = queryAllCount.get(j);

                countNumber += rankingList.getCountNumber();
                approved += rankingList.getApproved();
                activation += rankingList.getActivation();


                Integer integerAttendance = integerMap.get("attendance" + rankingList.getId());
                Integer attendance = (ObjectUtils.isEmpty(integerAttendance)) ? 0 : integerAttendance;
                countAttendance += attendance;
                Integer integerNumberOfPeople = integerMap.get("numberOfPeople" + rankingList.getId());
                Integer numberOfPeople = (ObjectUtils.isEmpty(integerNumberOfPeople)) ? 0 : integerNumberOfPeople;
                countNumberOfPeople += numberOfPeople;
                if (j == 0){
                        getExcels.add(new GetExcel(
                                row,
                                row,
                                XlsxLayoutReader.template1(
                                        cardType,
                                        row,
                                        queryAllCount,
                                        stringMap,
                                        rankingList,
                                        numberOfPeople,
                                        attendance
                                ),
                                false
                        ));
                    }else {
                        getExcels.add(new GetExcel(
                                row,
                                row,
                                XlsxLayoutReader.template2(
                                        cardType,
                                        row,
                                        queryAllCount,
                                        stringMap,
                                        rankingList,
                                        numberOfPeople,
                                        attendance
                                ),
                                false
                        ));
                }
                row++;
            }
            //合计
            getExcels.add(new GetExcel(
                    row,
                    row,
                    XlsxLayoutReader.template3(
                        cardType,
                        row,
                        countNumber,
                        approved,
                        activation,
                        countAttendance,
                        countNumberOfPeople
                    ),
                    true)
            );
            row++;
        }

        String xlsx = exportTool.xlsx(getExcels);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,xlsx,MsgEntity.CODE_SUCCEED);
    }


    //方法总管外加事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnEntity methodMasterT(HttpServletRequest request, String name) {
        try {
            if (name.equals("all_edit")){
                ReturnEntity returnEntity = all_edit(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("edit")){
                ReturnEntity returnEntity = edit(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("add")){
                ReturnEntity returnEntity = add(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }else if (name.equals("update")){
                ReturnEntity returnEntity = update(request);
                if (!returnEntity.getCode().equals("0")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                return returnEntity;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR, MsgEntity.CODE_ERROR);
        }catch (Exception e){
            log.info("捕获异常方法{},捕获异常{}",name,e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
    }

    //一键审核所有数据
    private ReturnEntity all_edit(HttpServletRequest request) throws IOException {
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }

        if (ObjectUtils.isEmpty(jsonParam.getApproverState())){
            jsonParam.setApproverState("agree");
        }

        QueryWrapper wrapper = new QueryWrapper();

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> selectList = iManagementPersonnelMapper.selectList(wrapper);
            for (int i = 0; i < selectList.size(); i++) {
                integerArrayList.add(selectList.get(i).getManagementId());
            }
        }else {
            integerArrayList.add(jsonParam.getManagementId());
        }

        wrapper = new QueryWrapper();
        wrapper.eq("approver_state","pending");
        wrapper.in("management_id",integerArrayList);
        List<PerformanceReport> performanceReports = iPerformanceReportMapper.selectList(wrapper);
        if (performanceReports.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"没有需要审核的数据");
        }
        int numberUpdate = 0;
        for (int i = 0; i < performanceReports.size(); i++) {
            PerformanceReport performanceReport = performanceReports.get(i);
            //进行审批修改
            int updateById = iPerformanceReportMapper.updateById(new PerformanceReport(
                    performanceReport.getId(),
                    jsonParam.getCommentsFromReviewers(),
                    jsonParam.getApproverState(),
                    DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms())
            ));
            //当返回值不为1的时候判断修改失败
            if (updateById != 1){
                return new ReturnEntity(
                        CodeEntity.CODE_ERROR,
                        "报告编码为" + performanceReport.getId() + "审核失败"
                );
            }
            numberUpdate += updateById;
        }
        for (int i = 0; i < performanceReports.size(); i++) {
            PerformanceReport performanceReport = performanceReports.get(i);
            wrapper = new QueryWrapper();
            wrapper.eq("personnel_code",performanceReport.getPersonnelCode());
            SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
            PanXiaoZhang.postWechatFer(
                    personnel.getOpenId(),
                    "业绩信息",
                    "",
                    performanceReport.getReportTime() + "提交的业绩信息,已审核",
                    "",
                    urlTransfer + "?from=zn&redirect_url=" + urlPerformance
            );
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功(" + numberUpdate + "/" + performanceReports.size() + ")");
    }

    //更正
    private ReturnEntity update(HttpServletRequest request) throws IOException {
        //获取提交数据
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        if (
                jsonParam.getEntryNumber() < 1
                        || jsonParam.getApprovedNumber() < 0
                        || jsonParam.getValidNumber() < 0
                        || jsonParam.getRefuseNumber() < 0
        ){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非法填写参数");
        }
        //判断填报数量是否正确
        if (jsonParam.getEntryNumber() < (jsonParam.getApprovedNumber() + jsonParam.getRefuseNumber())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"批核加拒绝总数量不能大于进件数");
        }
        if (jsonParam.getApprovedNumber() < jsonParam.getValidNumber()){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"有效数不能大于批核数");
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //查询该数据
        PerformanceReport performanceReport = iPerformanceReportMapper.selectById(jsonParam.getId());
        if (ObjectUtils.isEmpty(performanceReport.getId())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该业绩信息不存在");
        }
        //if (performanceReport.getApproverState().equals("pending")){
        //    return new ReturnEntity(CodeEntity.CODE_ERROR,"该业绩信息处于审核状态中，不可修改");
        //}
        //查询当前是否有提交的数据
        QueryWrapper wrapper = new QueryWrapper();
        //wrapper.ne("id",jsonParam.getId());
        //wrapper.eq("personnel_code",performanceReport.getPersonnelCode());
        //wrapper.apply(true, "report_time = '" + performanceReport.getReportTime() + "'");
        //wrapper.eq("card_type_id",jsonParam.getCardTypeId());
        //wrapper.ne("approver_state","refuse");
        //List<PerformanceReport> selectList = iPerformanceReportMapper.selectList(wrapper);
        //if (selectList.size() > 0){
        //    return new ReturnEntity(CodeEntity.CODE_ERROR,"数据不可重复");
        //}
        //将当前所属项目加入
        //wrapper = new QueryWrapper();
        if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
            wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
            List<ManagementPersonnel> managementPersonnels = iManagementPersonnelMapper.selectList(wrapper);
            if (managementPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目");
            }
            ManagementPersonnel managementPersonnel = managementPersonnels.get(0);
            jsonParam.setManagementId(managementPersonnel.getManagementId());
        }
        SysManagement management = iSysManagementMapper.selectById(jsonParam.getManagementId());
        if (!management.getManagementState().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
        }
        //查询该项目主管
        Map map = new HashMap();
        map.put("managementId",jsonParam.getManagementId());
        map.put("roleId",roleId);
        map.put("employmentStatus","1");
        List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
        if (sysPersonnels.size() < 1){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
        }
        SysPersonnel personnel = sysPersonnels.get(0);

        jsonParam.setApproverPersonnelId(personnel.getId());

        jsonParam.setReportTime(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()));

        jsonParam.setApproverState("pending");

        //修改
        if (!ObjectUtils.isEmpty(jsonParam.getSalesList())){
            for (int i = 0; i < jsonParam.getSalesList().size(); i++) {
                PerformanceReportSales performanceReportSales = jsonParam.getSalesList().get(i);
                int updateById = iPerformanceReportSalesMapper.updateById(performanceReportSales);
                //当返回值不为1的时候判断修改失败
                if (updateById != 1){
                    return new ReturnEntity(
                            CodeEntity.CODE_ERROR,
                            jsonParam,
                            "修改失败"
                    );
                }
            }
        }

        //没有任何问题将数据录入进数据库
        int updateById = iPerformanceReportMapper.updateById(new PerformanceReport(
            performanceReport.getId(),
            null,
            null,
            DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()),//提交修改报告时间
            null,
            null,
            personnel.getId(),//添加最新审核人编码
            null,
            null,
            "pending",//添加默认状态
            null,
            null,
            null,
            jsonParam.getEntryNumber(),//进件数
            jsonParam.getApprovedNumber(),//批核数
            jsonParam.getValidNumber(),//有效数
            jsonParam.getRefuseNumber()//拒绝数
        ));
        //如果返回值不能鱼1则判断失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
       PanXiaoZhang.postWechatFer(
                personnel.getOpenId(),
                "业绩信息",
                "",
                sysPersonnel.getName() + ":修改了业绩信息,请前往审核",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlPerformance
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"修改成功,请等待审核");
    }

    //查询审核列表
    private ReturnEntity cat_audit(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        map.put("approverPersonnelId",map.get("personnelId"));
        map.remove("personnelId");
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, iPerformanceReportMapper.queryAll(map),"");
    }

    //审核状态数据统计
    private ReturnEntity cat_audit_number(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        List<MapEntity> entityList = new ArrayList<>();
        AuditDataNumber auditDataNumber = iNumberAuditDataMapper.queryOne(map);
        //全部
        entityList.add(new MapEntity(
                "",
                "全部",
                auditDataNumber.getAll()
        ));
        //批核未激活
        entityList.add(new MapEntity(
                "pending",
                "等待审核",
                auditDataNumber.getPending()
        ));
        //激活
        entityList.add(new MapEntity(
                "agree",
                "审核通过",
                auditDataNumber.getAgree()
        ));
        //拒绝
        entityList.add(new MapEntity(
                "refuse",
                "审核拒绝",
                auditDataNumber.getRefuse()
        ));
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, entityList,"");
    }

    //更改信息
    private ReturnEntity edit(HttpServletRequest request) throws IOException {
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new PerformanceReportNotNull(
                        "isNotNullAndIsLengthNot0"
                        ,"isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        PerformanceReport performanceReport = iPerformanceReportMapper.selectById(jsonParam.getId());
        if (!performanceReport.getApproverState().equals("pending")){
            return new ReturnEntity(CodeEntity.CODE_SUCCEED,"该状态下不可修改信息");
        }
        //进行审批修改
        int updateById = iPerformanceReportMapper.updateById(new PerformanceReport(
                jsonParam.getId(),
                jsonParam.getCommentsFromReviewers(),
                jsonParam.getApproverState(),
                DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms())
        ));
        //当返回值不为1的时候判断修改失败
        if (updateById != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    "修改失败"
            );
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",performanceReport.getPersonnelCode());
        SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
        ReturnEntity entity = PanXiaoZhang.postWechatFer(
                personnel.getOpenId(),
                "业绩信息",
                "",
                performanceReport.getReportTime() + "提交的业绩信息,已审核",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlPerformance
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"审核成功");
    }

    //查询各个数据状态
    private ReturnEntity cat_number(HttpServletRequest request) {
        Map map = PanXiaoZhang.getMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }

        SysPersonnel personnel = iSysPersonnelMapper.selectById(String.valueOf(map.get("personnelId")));

        List<List<MapEntity>> listList = new ArrayList<>();
        List<MapEntity> entityList = new ArrayList<>();

        Object startTime = map.get("startTime");
        if (!ObjectUtils.isEmpty(startTime)){
            map.put("startTime",startTime + " 00:00:00");
        }
        Object endTime = map.get("endTime");
        if (!ObjectUtils.isEmpty(endTime)){
            map.put("endTime",endTime + " 23:59:59");
        }

        PerformanceReportNumber performanceReportNumber = numberPerformanceReportMapper.queryOne(map);
        //全部
        entityList.add(new MapEntity(
                "进件",
                "进件",
                performanceReportNumber.getAll()
        ));
        //批核未激活
        entityList.add(new MapEntity(
                "批核",
                "批核",
                performanceReportNumber.getApprove()
        ));
        //激活
        entityList.add(new MapEntity(
                "有效",
                "有效",
                performanceReportNumber.getActive()
        ));
        //拒绝
        entityList.add(new MapEntity(
                "拒绝",
                "拒绝",
                performanceReportNumber.getRefuse()
        ));
        map.put("personnelCode",personnel.getPersonnelCode());
        map.put("tyoe","自定义");
        //权益
        List<PerformanceReportSales> performanceReportSales = iPerformanceReportSalesMapper.queryList(map);
        for (int i = 0; i < performanceReportSales.size(); i++) {
            PerformanceReportSales reportSales = performanceReportSales.get(i);
            //权益
            entityList.add(new MapEntity(
                    reportSales.getType(),
                    reportSales.getType(),
                    reportSales.getSales()
            ));
        }
        //转人工
        entityList.add(new MapEntity(
                "转人工",
                "转人工",
                performanceReportNumber.getPendding()
        ));
        listList.add(entityList);
        List<MapEntity> list = new ArrayList<>();
        //当月激活
        list.add(new MapEntity(
                "当月进件",
                "当月进件",
                performanceReportNumber.getThisMonthActive()
        ));
        listList.add(list);
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, listList,"");
    }

    //员工提交报告
    private ReturnEntity add(HttpServletRequest request) throws IOException {
        //获取提交数据
        PerformanceReport jsonParam = PanXiaoZhang.getJSONParam(request, PerformanceReport.class);
        ReturnEntity returnEntity = PanXiaoZhang.isNull(
                jsonParam,
                new PerformanceReportNotNull(
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0",
                        "isNotNullAndIsLengthNot0"
                )
        );
        if (returnEntity.getState()){
            return returnEntity;
        }
        if (
            jsonParam.getEntryNumber() < 1
            || jsonParam.getApprovedNumber() < 0
            || jsonParam.getValidNumber() < 0
            || jsonParam.getRefuseNumber() < 0
        ){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非法填写参数");
        }
        //判断填报数量是否正确
        if (jsonParam.getEntryNumber() < (jsonParam.getApprovedNumber() + jsonParam.getRefuseNumber())){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"批核加拒绝总数量不能大于进件数");
        }
        if (jsonParam.getApprovedNumber() < jsonParam.getValidNumber()){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"有效数不能大于批核数");
        }
        SysPersonnel sysPersonnel = iSysPersonnelMapper.selectById(jsonParam.getPersonnelId());
        //判断当前人员状态
        ReturnEntity estimateState = PanXiaoZhang.estimateState(sysPersonnel);
        if (estimateState.getState()){
            return estimateState;
        }
        //如果角色不是员工或者不是销售岗位
        if (!sysPersonnel.getPositionPost().equals(1)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非业务岗人员不可提交");
        }
        //判断是否为员工
        if (!sysPersonnel.getRoleId().equals(manage5) && !sysPersonnel.getRoleId().equals(roleId)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"非员工主管不可提交");
        }
        //查询是否有该卡种
        CardType cardType = iCardTypeMapper.selectById(jsonParam.getCardTypeId());
        if (ObjectUtils.isEmpty(cardType)){
            return new ReturnEntity(CodeEntity.CODE_ERROR,"该卡种不存在");
        }
        QueryWrapper wrapper = new QueryWrapper();
        //查询当前个人的信息
        if (!returnEntity.getState()){
            //如果查不到人员信息
            if (ObjectUtils.isEmpty(sysPersonnel)){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"人员信息不存在");
            }
            //将人员资源代码加入进去
            jsonParam.setPersonnelCode(sysPersonnel.getPersonnelCode());
            //将当前所属项目加入
            if (ObjectUtils.isEmpty(jsonParam.getManagementId())){
                wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
                List<ManagementPersonnel> list = iManagementPersonnelMapper.selectList(wrapper);
                if (list.size() < 1){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"未关联项目组");
                }
                ManagementPersonnel managementPersonnel = list.get(0);
                SysManagement management = iSysManagementMapper.selectById(managementPersonnel.getManagementId());
                if (!management.getManagementState().equals(1)){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"该项目已停止运营");
                }
                jsonParam.setManagementId(managementPersonnel.getManagementId());
            }
            //查询该项目主管
            Map map = new HashMap();
            map.put("managementId",jsonParam.getManagementId());
            map.put("roleId",roleId);
            map.put("employmentStatus","1");
            List<SysPersonnel> sysPersonnels = whiteSysPersonnelMapper.queryAll(map);
            if (sysPersonnels.size() < 1){
                return new ReturnEntity(CodeEntity.CODE_ERROR,"当前项目无人审核，无法提交");
            }
            SysPersonnel personnel = sysPersonnels.get(0);
            //添加审核人编码
            jsonParam.setApproverPersonnelId(personnel.getId());
            jsonParam.setSysPersonnel(personnel);
            //设置该条数据唯一编码
            jsonParam.setReportCoding("coding" + System.currentTimeMillis() + PanXiaoZhang.ran(2));
        }
        //查询当前是否有提交的数据
        wrapper = new QueryWrapper();
        wrapper.eq("personnel_code",sysPersonnel.getPersonnelCode());
        //返回提示语
        String msg = "当天";
        if (!ObjectUtils.isEmpty(jsonParam.getTimeType()) && jsonParam.getTimeType()){
            msg = "昨天";
            wrapper.apply(true, "DATE(report_time) = DATE(NOW() - INTERVAL 1 DAY)");
        }else {
            wrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(report_time) = 0");
        }
        wrapper.eq("card_type_id",jsonParam.getCardTypeId());
        wrapper.eq("management_id",jsonParam.getManagementId());
        //wrapper.ne("approver_state","refuse");
        List<PerformanceReport> selectList = iPerformanceReportMapper.selectList(wrapper);
        if (selectList.size() > 0){
            PerformanceReport performanceReport = selectList.get(0);
            return new ReturnEntity(CodeEntity.CODE_ERROR,performanceReport,msg + "已提交过" + cardType.getName() + "数据");
        }
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMdHms());
        if (!ObjectUtils.isEmpty(jsonParam.getTimeType()) && jsonParam.getTimeType()){
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1); // 获取昨天的日期时间
            LocalTime time = LocalTime.of(15, 0); // 设置下午3点时间
            LocalDateTime yesterday3pm = LocalDateTime.of(yesterday.toLocalDate(), time); // 将昨天的日期和下午3点时间合并为一个 LocalDateTime 对象
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定义日期时间格式
            String formattedDateTime = yesterday3pm.format(formatter); // 格式化日期时间为字符串类型
            //添加当前报告时间
            jsonParam.setReportTime(formattedDateTime);
        }else {
            //添加当前报告时间
            jsonParam.setReportTime(format);
        }
        //添加修改时间
        jsonParam.setUpdateTime(format);
        //添加默认状态
        jsonParam.setApproverState("pending");
        //关联权益
        if (!ObjectUtils.isEmpty(jsonParam.getSalesList())){
            for (int i = 0; i < jsonParam.getSalesList().size(); i++) {
                PerformanceReportSales performanceReportSales = jsonParam.getSalesList().get(i);
                if (ObjectUtils.isEmpty(performanceReportSales.getType())){
                    return new ReturnEntity(CodeEntity.CODE_ERROR,"权益名称不可为空");
                }
                performanceReportSales.setReportCoding(jsonParam.getReportCoding());
                int insert = iPerformanceReportSalesMapper.insert(
                        new PerformanceReportSales(
                                performanceReportSales.getSales(),
                                performanceReportSales.getType(),
                                performanceReportSales.getReportCoding(),
                                performanceReportSales.getComment()
                        )
                );
                //如果返回值不能鱼1则判断失败
                if (insert != 1){
                    return new ReturnEntity(
                            CodeEntity.CODE_ERROR,
                            MsgEntity.CODE_ERROR
                    );
                }
            }
        }
        //没有任何问题将数据录入进数据库
        int insert = iPerformanceReportMapper.insert(jsonParam);
        //如果返回值不能鱼1则判断失败
        if (insert != 1){
            return new ReturnEntity(
                    CodeEntity.CODE_ERROR,
                    jsonParam,
                    MsgEntity.CODE_ERROR
            );
        }
        ReturnEntity entity = PanXiaoZhang.postWechatFer(
                jsonParam.getSysPersonnel().getOpenId(),
                "业绩信息",
                "",
                sysPersonnel.getName() + ":提交业绩信息,请前往审核",
                "",
                urlTransfer + "?from=zn&redirect_url=" + urlPerformance
        );
        return new ReturnEntity(CodeEntity.CODE_SUCCEED,"上报成功");
    }
    //查询提交的表单
    private ReturnEntity cat(HttpServletRequest request) {
        Map map = PanXiaoZhang.getJsonMap(request);
        if (ObjectUtils.isEmpty(map.get("personnelId"))){
            return new ReturnEntity(CodeEntity.CODE_ERROR,MsgEntity.CODE_ERROR);
        }
        Object startTime = map.get("startTime");
        if (!ObjectUtils.isEmpty(startTime)){
            map.put("startTime",startTime + " 00:00:00");
        }
        Object endTime = map.get("endTime");
        if (!ObjectUtils.isEmpty(endTime)){
            map.put("endTime",endTime + " 23:59:59");
        }
        return new ReturnEntity(CodeEntity.CODE_SUCCEED, iPerformanceReportMapper.queryAll(map),"");
    }

    public static void main(String[] args) {
        String filePath = "D:\\home\\equity\\manage\\target\\classes\\upload\\ceshi5125891660107796633.xlsx";
        String binaryFilePath = "path/to/binary/file";
        try {
            // 创建 FileInputStream 对象
            FileInputStream fileInputStream = new FileInputStream(filePath);

            // 创建 FileOutputStream 对象
            //FileOutputStream fileOutputStream = new FileOutputStream(binaryFilePath);

            // 读取文件并写入 FileOutputStream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                System.out.println(buffer);
                //fileOutputStream.write(buffer, 0, bytesRead);
            }

            // 关闭流
            fileInputStream.close();
            //fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
