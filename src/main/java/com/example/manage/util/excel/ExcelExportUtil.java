package com.example.manage.util.excel;

import com.example.manage.entity.CardType;
import com.example.manage.entity.data_statistics.DataStatisticsTodayDay;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.GetExcel;
import org.apache.commons.collections4.Get;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.*;


/**
 * @program：潘小章
 * @create：2022-04_17 18:51
 **/
public class ExcelExportUtil {

    /*文件上传额外添加的前缀*/
    //@Value("${magicalcoder.file.upload.useDisk.mapping.fileExtraAddPrefix:}")
    //private static String fileExtraAddPrefix;
    private static String fileExtraAddPrefix = "/www/springboot/manage/upload";

    public List<String> riqi(Integer tianshu){
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Date today = new Date();
        String endDate = sdf .format( today ); //当前日期
        //获取三十天前日期
        Calendar theCa = Calendar. getInstance ();
        List<String> datefor30List = new ArrayList<>();
        datefor30List.add("");
        for (int i = tianshu * -1;i < 1;i++){
            theCa .setTime( today );
            theCa .add(Calendar. DATE, i); //最后一个数字30可改，30天的意思
            Date start = theCa .getTime();
            String startDate = sdf .format( start ); //三十天之前日期
            datefor30List.add(startDate);
        }
        return datefor30List;
    }
    public List<String> riqi_zhan(Integer tianshu){
        List<String> datefor30List = new ArrayList<>();
        for (int i = tianshu * -1;i < 1;i++){
            datefor30List.add("");
        }
        return datefor30List;
    }
    //public static void main(String[] args) {
    //    //获取当前日期
    //    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
    //    Date today = new Date();
    //    String endDate = sdf .format( today ); //当前日期
    //    //获取三十天前日期
    //    Calendar theCa = Calendar. getInstance ();
    //    theCa .setTime( today );
    //    for (int i = -7;i < 1;i++){
    //        theCa .add(Calendar. DATE, i); //最后一个数字30可改，30天的意思
    //        Date start = theCa .getTime();
    //        String startDate = sdf .format( start ); //三十天之前日期
    //        System.out.println(startDate);
    //    }
    //}

    /**
     * 带边框的样式+
     */
    public static XSSFCellStyle setDefaultStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        // 边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(new XSSFColor(new java.awt.Color(191, 1, 1)));
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(new XSSFColor(new java.awt.Color(191, 1, 1)));
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(new XSSFColor(new java.awt.Color(191, 1, 1)));
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(new XSSFColor(new java.awt.Color(191, 1, 1)));
        return cellStyle;
    }
    /**
     * 为合并的单元格设置样式（可根据需要自行调整）
     */
    public static List<XSSFCellStyle> setRegionStyle(XSSFSheet sheet, CellRangeAddress region, XSSFCellStyle cs) {
        List<XSSFCellStyle> cellStyles = new ArrayList<>();
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                row = sheet.createRow(i);
            }
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (null == cell) {
                    cell = row.createCell(j);
                }
                //cell.setCellStyle(cs);
                cellStyles.add(cs);
            }
        }
        return cellStyles;
    }
    /**
     * 潘Sir
     * @param pageList 主干内容
     * @param getExcels 抬头内容
     * @param tmpFile 输出文件
     */
    public static void writeExcelToTemp(List<List<DataStatisticsTodayDay>> pageList, List<GetExcel> getExcels, File tmpFile) {
        FileOutputStream out = null;
        XSSFWorkbook wb = new XSSFWorkbook();; // keep 100 rows in memory, exceeding rows will be flushed to disk
        XSSFSheet sh = wb.createSheet("Sheet1");
        //记录最长的行数
        int rowCount = 0;
        //记录最长的列数
        int colCount = 0;
        try {
            for (int i = 0; i < getExcels.size(); i++) {
                GetExcel getExcel = getExcels.get(i);
                List<GetExcel> excels = getExcel.getGetExcels();
                //边框线设置
                for (int j = 0; j < excels.size(); j++) {
                    GetExcel excel = excels.get(j);
                    if (rowCount < excel.getLastRow()){
                        rowCount = excel.getLastRow();
                    }
                    if (colCount < excel.getLastCol()){
                        colCount = excel.getLastCol();
                    }
                    //指定合并开始行、合并结束行 合并开始列、合并结束列
                    CellRangeAddress rangeAddress = new CellRangeAddress(
                            excel.getFirstRow()
                            , excel.getLastRow()
                            , excel.getFirstCol()
                            , excel.getLastCol()
                    );
                    if (excel.getMergeSwitch()){
                        //添加要合并地址到表格
                        sh.addMergedRegion(rangeAddress);
                    }

                    Row headRow = sh.getRow(excel.getFirstRow());
                    if (ObjectUtils.isEmpty(headRow)){
                        headRow = sh.createRow(excel.getFirstRow());
                    }

                    //设置样式
                    XSSFCellStyle cellStyle = wb.createCellStyle();
                    if (getExcel.getRowBorderSwitch()){
                        RegionUtil.setBorderBottom(BorderStyle.THICK,rangeAddress,sh);
                    }
                    // 创建工作表
                    sh.setDefaultRowHeight((short) 500);
                    int fontSize = 0;
                    if (!ObjectUtils.isEmpty(excel.getName()) && ObjectUtils.isEmpty(excel.getWidthSwitch())){
                        fontSize = (excel.getName().length() - 5) * 500;
                        if (fontSize > 0){
                            fontSize = fontSize + 3000;
                        }
                    }
                    if (fontSize == 0){
                        fontSize = 3000;
                    }
                    sh.setColumnWidth(excel.getFirstCol(), fontSize);
                    //创建单元格，指定起始列号，从0开始
                    Cell cell = headRow.createCell(excel.getFirstCol());
                    //设置单元格内容
                    cell.setCellValue(excel.getName());
                    //设置单元格类型
                    cell.setCellType(CellType.STRING);

                    if (!excel.getMergeSwitch()){
                        //边框线
                        cellStyle = setDefaultStyle(wb);
                    }
                    //居中
                    cellStyle.setAlignment(excel.getHorizontalAlignment());
                    cellStyle.setVerticalAlignment(excel.getVerticalAlignment());
                    if (excel.getType().equals("image")){
                        // 创建 Drawing 对象
                        Drawing drawing = sh.createDrawingPatriarch();
                        // 创建 ClientAnchor 对象
                        byte[] byteArray = Files.readAllBytes(Paths.get(excel.getName()));
                        int pictureIndex = wb.addPicture(byteArray, Workbook.PICTURE_TYPE_JPEG);
                        //col1 ：从左向右数 | 的线（从0）
                        //row1 ：从上到下数 —— 的线（从0）
                        //col2 ：同col1
                        //row2 ：同row1
                        ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, excel.getFirstCol(), getExcel.getFirstRow(), excel.getLastCol(), getExcel.getLastRow());
                        // 创建 Picture 对象，并将其添加到 Drawing 中
                        Picture picture = drawing.createPicture(anchor, pictureIndex);
                        // 设置 Picture 的大小
                        picture.resize(1.5,1.5);
                    }else if (excel.getType().equals("text")){
                        //设置背景颜色
                        cellStyle.setFillForegroundColor(excel.getBgcolor());
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        //设置字体颜色
                        XSSFFont font = wb.createFont();
                        font.setColor(excel.getColor());
                        font.setFontHeightInPoints(excel.getFontSize());
                        font.setBold(excel.getBold());
                        cellStyle.setFont(font);
                        //将设置好的加入单元格中
                        cell.setCellStyle(cellStyle);
                    }
                }
            }
            //数据
            //for(int i = 0; i < rowCount;i++){
            //
            //    Row headRow = sh.getRow(i);
            //    if (ObjectUtils.isEmpty(headRow)){
            //        headRow = sh.createRow(i);
            //    }
                //创建行，指定起始行号，从0开始
                //for (int j = 0; j < colCount; j++) {
                //    Cell cell = sh.getRow(i).getCell(j);
                //    if (ObjectUtils.isEmpty(cell)){
                //        //创建单元格，指定起始列号，从0开始
                //        cell = headRow.createCell(j);
                //    }
                //    // 获取单元格样式
                //    CellStyle cellStyle = cell.getCellStyle();
                //    // 更改边框线
                //    cellStyle.setBorderTop(BorderStyle.THIN);
                //    cellStyle.setBorderBottom(BorderStyle.THIN);
                //    cellStyle.setBorderLeft(BorderStyle.THIN);
                //    cellStyle.setBorderRight(BorderStyle.THIN);
                //    //将设置好的加入单元格中
                //    cell.setCellStyle(cellStyle);
                //}
            //    Row shRow = sh.createRow(i + 5);
            //    for(int j = 0;j < pageList.get(i).size();j++){
            //        //获取当前内容
            //        DataStatisticsTodayDay dataStatisticsTodayDay = pageList.get(i).get(j);
            //        //创建单元格，指定起始列号，从0开始
            //        Cell cell = shRow.createCell(j);
            //        //设置单元格内容
            //        cell.setCellValue(dataStatisticsTodayDay.getActivation());
            //        //设置单元格类型
            //        cell.setCellType(CellType.STRING);
            //    }
            //}
            out = new FileOutputStream(tmpFile);
            wb.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
                if(wb!=null){
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        ArrayList<List<DataStatisticsTodayDay>> arrayList = new ArrayList<>();
        ArrayList<DataStatisticsTodayDay> list = new ArrayList<>();
        DataStatisticsTodayDay dataStatisticsTodayDay = new DataStatisticsTodayDay(
                1,
                2,
                3
        );
        list.add(dataStatisticsTodayDay);
        list.add(dataStatisticsTodayDay);
        list.add(dataStatisticsTodayDay);
        list.add(dataStatisticsTodayDay);
        arrayList.add(list);
        arrayList.add(list);

        //File tmpFile = File.createTempFile("ceshi",".xlsx",new File(fileExtraAddPrefix));
        //writeExcelToTemp(arrayList,init(),tmpFile);
    }
    //红色
    public static XSSFColor colorRed(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(191, 1, 1));
        return bgRed;
    }
    //白色
    public static XSSFColor colorWhite(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(255, 255, 255));
        return bgRed;
    }
    //黑色
    public static XSSFColor colorBlack(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(0, 0, 0));
        return bgRed;
    }
    //黄色
    public static XSSFColor colorYellow(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(246, 193, 0));
        return bgRed;
    }
    //黄色
    public static XSSFColor color249(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(249,202,172));
        return bgRed;
    }
    //蓝色
    public static XSSFColor color117(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(117, 141, 253));
        return bgRed;
    }
    //蓝色
    public static XSSFColor color93(){
        XSSFColor bgRed = new XSSFColor(new java.awt.Color(93, 121, 253));
        return bgRed;
    }
    //抬头标题
    public static List<GetExcel> title(Map<String,String> map,String type){
        List<GetExcel> getExcels = new ArrayList<>();

        String dayTime = DateFormatUtils.format(new Date(),PanXiaoZhang.yMd());

        String thisStartTime = map.get("thisStartTime");
        String thisEndTime = map.get("thisEndTime");
        if (!ObjectUtils.isEmpty(thisStartTime) && !ObjectUtils.isEmpty(thisEndTime)){
            dayTime = thisStartTime + '~' + thisEndTime;
        }
        if (type.equals("月")){
            LocalDate today = LocalDate.now();
            LocalDate firstDayOfMonth = today.withDayOfMonth(1);
            LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String firstDay = firstDayOfMonth.format(formatter);
            String lastDay = lastDayOfMonth.format(formatter);
            dayTime = firstDay + '~' + lastDay;
        }
        getExcels.add(new GetExcel(
                "场景项目业绩统计" + type + "报表",
                null,
                "text",
                colorWhite(),
                colorRed(),
                0,
                0,
                1,
                0,
                8,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 18,
                true,
                true
        ));
        getExcels.add(new GetExcel(
                dayTime,
                null,
                "text",
                colorWhite(),
                colorRed(),
                0,
                0,
                1,
                9,
                11,
                HorizontalAlignment.RIGHT,
                VerticalAlignment.BOTTOM,
                (short) 11,
                true,
                true,
                false
        ));
        return getExcels;
    }
    public static List<GetExcel> cardType(List<CardType> cardTypes,Map<String, String> map,Integer startRow,Integer endRow,String key,String type){
        List<GetExcel> getExcels = new ArrayList<>();
        Integer cell = 0;
        for (int i = 0; i < cardTypes.size(); i++) {
            CardType cardType = cardTypes.get(i);
            getExcels.add(new GetExcel(
                    cardType.getType(),
                    null,
                    "text",
                    colorRed(),
                    colorWhite(),
                    0,
                    startRow,
                    endRow,
                    cell,
                    cell += 1,
                    HorizontalAlignment.CENTER,
                    VerticalAlignment.CENTER,
                    (short) 12,
                    true,
                    true
            ));
            getExcels.add(new GetExcel(
                    type + "有效",
                    null,
                    "text",
                    colorWhite(),
                    colorRed(),
                    2,
                    startRow,
                    endRow,
                    cell += 1,
                    cell,
                    HorizontalAlignment.CENTER,
                    VerticalAlignment.CENTER,
                    (short) 12,
                    true,
                    true
            ));
            getExcels.add(new GetExcel(
                    (ObjectUtils.isEmpty(map.get(key + cardType.getId()))) ? "0" : map.get(key + cardType.getId()),
                    null,
                    "text",
                    colorWhite(),
                    colorRed(),
                    3,
                    startRow,
                    endRow,
                    cell += 1,
                    cell,
                    HorizontalAlignment.CENTER,
                    VerticalAlignment.CENTER,
                    (short) 12,
                    true,
                    true
            ));
            cell += 1;
        }
        return getExcels;
    }
    public static List<GetExcel> tableName(List<String> stringList,Integer startRow,Integer endRow){
        List<GetExcel> getExcels = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            String s = stringList.get(i);
            getExcels.add(new GetExcel(
                    s,
                    null,
                    "text",
                    colorRed(),
                    colorWhite(),
                    0,
                    startRow,
                    endRow,
                    i,
                    i,
                    HorizontalAlignment.CENTER,
                    VerticalAlignment.CENTER,
                    (short) 11,
                    true,
                    false
            ));
        }
        return getExcels;
    }
    //月报表
    public static List<GetExcel> initMonth(Map<String, String> map,String type){
        List<GetExcel> getExcels = new ArrayList<>();
        getExcels.add(new GetExcel(
                0,
                1,
                title(map,type),
                true
        ));
        List<String> stringList = new ArrayList<>();
        stringList.add("部门");
        stringList.add("区域");
        stringList.add("项目类型");
        stringList.add("场景");
        stringList.add("本月进件");
        stringList.add("本月批核");
        stringList.add("本月有效");
        stringList.add("指标");
        stringList.add("指标完成率");
        //主要内容
        getExcels.add(new GetExcel(
                2,
                2,
                tableName(
                        stringList,
                        2,
                        2),
                false)
        );
        return getExcels;
    }
    //日报表
    public static List<GetExcel> init(Map<String,String> map,String path,List<CardType> cardTypes,String type){
        List<GetExcel> getExcels = new ArrayList<>();
        getExcels.add(new GetExcel(
                0,
                1,
                title(map,type),
                true
        ));
        getExcels.add(new GetExcel(
                2,
                3,
                cardType(
                        cardTypes,
                        map,
                        2,
                        3,
                        "dayCardType",
                        type
                ),
                true
        ));

        getExcels.add(new GetExcel(
                4,
                5,
                cardType(
                        cardTypes,
                        map,
                        4,
                        5,
                        "monthCardType",
                        "累计"
                ),
                true)
        );
        List<String> stringList = new ArrayList<>();
        stringList.add("业务类型");
        stringList.add("项目类别");
        stringList.add("项目主管");
        stringList.add("应出勤");
        stringList.add("实出勤");
        stringList.add("当日进件量");
        stringList.add("当日批核量");
        stringList.add("有效客户量");
        stringList.add("权益");
        stringList.add("现场开卡率");
        stringList.add("人均单产");
        stringList.add("备注");
        //主要内容
        getExcels.add(new GetExcel(
                6,
                6,
                tableName(
                        stringList,
                        6,
                        6),
                false)
        );
    //
    //    //主要内容
    //    getExcels.add(new GetExcel(
    //            7,
    //            7,
    //            new GetExcel[]{new GetExcel(
    //                    "中信银行",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    9,
    //                    0,
    //                    0,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    true,
    //                    data()
    //            ),new GetExcel(
    //                    "深圳南航",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    1,
    //                    1,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "方筠思",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    2,
    //                    2,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "15",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    3,
    //                    3,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "7",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    4,
    //                    4,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "101",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    5,
    //                    5,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "80",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    6,
    //                    6,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "22",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    7,
    //                    7,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "34",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    8,
    //                    8,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "28%",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    9,
    //                    9,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "11.4",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    10,
    //                    10,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    7,
    //                    7,
    //                    11,
    //                    11,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            )},
    //            false)
    //    );
    //
    //    //主要内容
    //    getExcels.add(new GetExcel(
    //            8,
    //            8,
    //            new GetExcel[]{new GetExcel(
    //                    "深圳深航",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    1,
    //                    1,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "邹霞",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    2,
    //                    2,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "11",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    3,
    //                    3,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "11",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    4,
    //                    4,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "67",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    5,
    //                    5,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "55",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    6,
    //                    6,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "24",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    7,
    //                    7,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "43",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    8,
    //                    8,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "44%",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    9,
    //                    9,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "5.0",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    10,
    //                    10,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "",
    //                    null,
    //                    "text",
    //                    colorWhite(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    8,
    //                    8,
    //                    11,
    //                    11,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            )},
    //            true)
    //    );
    //
    //    //合计
    //    getExcels.add(new GetExcel(
    //            9,
    //            9,
    //            new GetExcel[]{new GetExcel(
    //                    "中信银行合计",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    1,
    //                    2,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    true,
    //                    data()
    //            ),new GetExcel(
    //                    "26",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    3,
    //                    3,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "18",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    4,
    //                    4,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "168",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    5,
    //                    5,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "135",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    6,
    //                    6,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "46",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    7,
    //                    7,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "77",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    8,
    //                    8,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "待计算",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    9,
    //                    9,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "待计算",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    10,
    //                    10,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            ),new GetExcel(
    //                    "",
    //                    null,
    //                    "text",
    //                    ExcelExportUtil.colorYellow(),
    //                    ExcelExportUtil.colorBlack(),
    //                    0,
    //                    9,
    //                    9,
    //                    11,
    //                    11,
    //                    HorizontalAlignment.CENTER,
    //                    VerticalAlignment.CENTER,
    //                    (short) 11,
    //                    false,
    //                    false,
    //                    data()
    //            )},
    //            true)
    //    );
        return getExcels;
    }
    public void writeExcelToTempFile(List<List<String>> pageList, String[] titles, String[] mappingArr, File tmpFile) {
        FileOutputStream out = null;
        SXSSFWorkbook wb = new SXSSFWorkbook(1000); // keep 100 rows in memory, exceeding rows will be flushed to disk
        Sheet sh = wb.createSheet();
        try {
            //头部 第1行
            Row headRow = sh.createRow(0);
            for(int i=0;i<titles.length;i++){
                Cell cell = headRow.createCell(i);
                cell.setCellValue(titles[i]);
            }
            //数据
            for(int i=0;i<pageList.size();i++){
                Row row = sh.createRow(i+1);
                for(int j=0;j < pageList.get(i).size();j++){
                    Cell cell = row.createCell(j);
                    cell.setCellValue(pageList.get(i).get(j));
                    cell.setCellType(CellType.STRING);
                }
            }
            out = new FileOutputStream(tmpFile);
            wb.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(wb!=null){
                wb.dispose();
            }
        }
    }
    /**
     * 文件流输出
     * @param response
     * @param file 文件
     */
    public void toFile(HttpServletResponse response, File file) {
        try {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[256];
            out = response.getOutputStream();
            while((len = in.read(buffer)) > 0) {
                out.write(buffer,0,len);
            }
        }catch(Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(in != null) {
                try {
                    in.close();
                }catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //public static void main(String[] args) {
    //    List<List> list = new ArrayList();
    //    List list1 = new ArrayList();
    //    list1.add(1);
    //    list.add(list1);
    //    list.add(list1);
    //    for (int i = 0;i < list.size();i++){
    //        for (int j = 0;j < list.get(i).size();j++){
    //            System.out.println(list.get(i).get(j));
    //        }
    //    }
    //}
}
