package com.example.manage.util.excel;

import com.example.manage.entity.CardType;
import com.example.manage.entity.data_statistics.DataStatisticsTodayDay;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.GetExcel;
import org.apache.commons.collections4.Get;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletOutputStream;
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
                    //int fontSize = 0;
                    //if (!ObjectUtils.isEmpty(excel.getName()) && ObjectUtils.isEmpty(excel.getWidthSwitch())){
                    //    fontSize = (excel.getName().length() - 5) * 500;
                    //    if (fontSize > 0){
                    //        fontSize = fontSize + 3000;
                    //    }
                    //}
                    //if (fontSize == 0){
                    //    fontSize = 3000;
                    //}
                    //sh.setColumnWidth(excel.getFirstCol(), fontSize);
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

    /**
     * 创建标题样式
     *
     * @param wb
     * @return
     */
    private static HSSFCellStyle createTitleCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        // 创建字体样式
        HSSFFont headerFont1 = (HSSFFont) wb.createFont();
        //字体加粗
        headerFont1.setBold(true);
        // 设置字体类型
        headerFont1.setFontName("黑体");
        // 设置字体大小
        headerFont1.setFontHeightInPoints((short) 15);
        // 为标题样式设置字体样式
        cellStyle.setFont(headerFont1);

        return cellStyle;
    }

    /**
     * 创建表头样式
     *
     * @param wb
     * @return
     */
    private static HSSFCellStyle createHeadCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置自动换行
        cellStyle.setWrapText(true);
        //背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        //下边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        //上边框
        cellStyle.setBorderTop(BorderStyle.THIN);
         //创建字体样式
        HSSFFont headerFont = (HSSFFont) wb.createFont();
        //字体加粗
        headerFont.setBold(true);
        // 设置字体类型
        headerFont.setFontName("黑体");
        // 设置字体大小
        headerFont.setFontHeightInPoints((short) 12);
        // 为标题样式设置字体样式
        cellStyle.setFont(headerFont);

        return cellStyle;
    }

    //导出模版2
    public static void writeExcelTo(List<List<DataStatisticsTodayDay>> pageList, List<GetExcel> getExcels, File tmpFile) {
            /** 第一步，创建一个Workbook，对应一个Excel文件  */
            HSSFWorkbook wb = new HSSFWorkbook();

            /** 第二步，在Workbook中添加一个sheet,对应Excel文件中的sheet  */
            HSSFSheet sheet = wb.createSheet("excel导出标题");

            /** 第三步，设置样式以及字体样式*/
            HSSFCellStyle titleStyle = createTitleCellStyle(wb);
            HSSFCellStyle headerStyle = createHeadCellStyle(wb);


            ////记录最长的行数
            //int rowCount = 0;
            ////记录最长的列数
            //int colCount = 0;
            //for (int i = 0; i < getExcels.size(); i++) {
            //    GetExcel getExcel = getExcels.get(i);
            //    List<GetExcel> excels = getExcel.getGetExcels();
            //    for (int j = 0; j < excels.size(); j++) {
            //        GetExcel excel = excels.get(j);
            //        if (rowCount < excel.getLastRow()) {
            //            rowCount = excel.getLastRow();
            //        }
            //        if (colCount < excel.getLastCol()) {
            //            colCount = excel.getLastCol();
            //        }
            //
            //        if (excel.getMergeSwitch()) {
            //            //添加要合并地址到表格
            //            sheet.addMergedRegion(new CellRangeAddress(
            //                    excel.getFirstRow()
            //                    , excel.getLastRow()
            //                    , excel.getFirstCol()
            //                    , excel.getLastCol()));
            //        }
            //
            //        Row headRow = sheet.getRow(excel.getFirstRow());
            //        if (ObjectUtils.isEmpty(headRow)) {
            //            headRow = sheet.createRow(excel.getFirstRow());
            //        }
            //        // 创建工作表
            //        //sheet.setDefaultRowHeight((short) 500);
            //        //int fontSize = 0;
            //        //if (!ObjectUtils.isEmpty(excel.getName()) && ObjectUtils.isEmpty(excel.getWidthSwitch())){
            //        //    fontSize = (excel.getName().length() - 5) * 500;
            //        //    if (fontSize > 0){
            //        //        fontSize = fontSize + 3000;
            //        //    }
            //        //}
            //        //if (fontSize == 0){
            //        //    fontSize = 3000;
            //        //}
            //        //sh.setColumnWidth(excel.getFirstCol(), fontSize);
            //        //创建单元格，指定起始列号，从0开始
            //        Cell cell = headRow.createCell(excel.getFirstCol());
            //        //设置单元格内容
            //        cell.setCellValue(excel.getName());
            //        cell.setCellStyle(headerStyle);
            //    }
            //}

            /** 第四步，创建标题 ,合并标题单元格 */
            // 行号
            int rowNum = 0;
            // 创建第一页的第一行，索引从0开始
            HSSFRow row0 = sheet.createRow(rowNum++);
            row0.setHeight((short) 800);// 设置行高

            String title = "标题名";
            HSSFCell c00 = row0.createCell(0);
            c00.setCellValue(title);
            c00.setCellStyle(titleStyle);
            // 合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
            //标题合并单元格操作，7为总列数
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            // 第二行
            HSSFRow row1 = sheet.createRow(rowNum++);
            //第二行表头名称
            String[] row_first = {"", "", "", "", "", "", "", ""};
            for (int i = 0; i < row_first.length; i++) {
                HSSFCell tempCell = row1.createCell(i);
                tempCell.setCellStyle(headerStyle);
                tempCell.setCellValue(row_first[i]);
            }

            sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 5));

            sheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(3, 4, 4, 5));

            //加多一条list
            //for (GetExcel excelData : getExcels) {
            //    HSSFRow tempRow = sheet.createRow(rowNum++);
            //    tempRow.setHeight((short) 500);
            //    // 循环单元格填入数据
            //    for (int j = 0; j < 8; j++) {
            //        HSSFCell tempCell = tempRow.createCell(j);
            //        String tempValue = excelData.getName();
            //        tempCell.setCellValue(tempValue);
            //    }
            //}
            //
            ////sheet.addMergedRegion(new CellRangeAddress(8, 10, 0, 0));
            ////sheet.addMergedRegion(new CellRangeAddress(11, 13, 0, 0));
            ////sheet.addMergedRegion(new CellRangeAddress(14, 18, 0, 0));
            //
            ////根据业务需求对单元格进行合并   参数依次为起始行，结束行，起始列，结束列 （索引0开始）
            //sheet.addMergedRegion(new CellRangeAddress(8, 10, 0, 0));
            //sheet.addMergedRegion(new CellRangeAddress(8, 10, 2, 2));
            //sheet.addMergedRegion(new CellRangeAddress(8, 10, 3, 3));
            //sheet.addMergedRegion(new CellRangeAddress(8, 10, 4, 4));
            //sheet.addMergedRegion(new CellRangeAddress(12, 13, 0, 0));
            //sheet.addMergedRegion(new CellRangeAddress(12, 13, 2, 2));
            //sheet.addMergedRegion(new CellRangeAddress(12, 13, 3, 3));
            //sheet.addMergedRegion(new CellRangeAddress(12, 13, 4, 4));
            //sheet.addMergedRegion(new CellRangeAddress(7, 8, 6, 6));
            //sheet.addMergedRegion(new CellRangeAddress(7, 8, 7, 7));
            //sheet.addMergedRegion(new CellRangeAddress(10, 12, 6, 6));
            //sheet.addMergedRegion(new CellRangeAddress(10, 12, 7, 7));


            FileOutputStream out = null;
            try {
                out = new FileOutputStream(tmpFile);
                wb.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (wb != null) {
                        wb.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    //导出3
    public static void getExcel3(List<List<DataStatisticsTodayDay>> pageList, List<GetExcel> getExcels, File tmpFile){
        String excelCon = "<table ><tr><td colspan='23' align='center'>分县局刑侦支(大)队技术工作情况统计表</td></tr></table><table><tr><td align='left'>主办单位：</td><td></td> <td  colspan='21' align='right'>统计时间:2023年4月-2023年5月</td></tr></table> <table border='2' width='100%' >   <tr><td rowspan='2' colspan='2'>现场接报<br/>总数</td><td rowspan='2' colspan='2'>现场勘验<br/>总数</td><td rowspan='2' colspan='2'>         现场录入<br/>总数</td><td rowspan='2' colspan='2'>未立案刑<br/>案勘验总<br/>数</td> <td colspan='4'>立案刑案勘验案数</td> <td rowspan='2'>        写出<br/>分析<br/>案数</td><td rowspan='2'>痕迹<br/>提取<br/>案数</td><td rowspan='2'>制作<br/>记录<br/>案数</td><td rowspan='2'>痕迹<br/>建档<br/>案数</td><td rowspan='2'>       受理<br/>检案<br/>案数</td><td rowspan='2'>得出<br/>结论<br/>案数</td>   <td colspan='2'>技术破<br/>案案数</td>   <td colspan='2'>     鉴定书</td> <td rowspan='2'>在岗技<br/>术员数</td></tr> <tr><td>总<br/>计</td><td>九<br/>类</td><td>入室<br/>盗窃</td><td>其<br/>它</td><td>痕迹</td><td>DNA</td><td>案<br/>数</td><td>   份<br/>数</td></tr> <tr><td colspan='2'>5</td><td colspan='2'>11</td><td colspan='2'>11</td><td colspan='2'>3</td><td>          9</td><td>8</td><td>4</td><td>3</td><td>11</td><td>        6</td><td>6</td><td>6</td><td>7</td><td>      9</td><td>4</td><td>8</td><td>2</td><td>6</td><td>3</td></tr> <tr><td colspan='8'>痕迹提取种类</td> <td colspan='6'>发挥作用破案</td> <td colspan='2'>指纹正查档</td> <td colspan='4'>    指纹、足迹倒查档</td> <td colspan='2'>串并案数</td><td rowspan='3'>嫌疑人<br/>十指纹<br/>建档数</td></tr>   <tr> <td colspan='2' >指纹案数</td> <td colspan='2' >足迹案数</td>         <td colspan='2' >DNA提取<br/>案数</td>   <td >工具<br/>案数</td><td >其<br/>它</td>          <td rowspan='2'>总<br/>数</td> <td rowspan='2'>查<br/>档<br/>认<br/>定<br/>数</td> <td rowspan='2'>证<br/>实<br/>认<br/>定<br/>数</td> <td rowspan='2'>      确<br/>定<br/>性<br/>质<br/>数</td> <td rowspan='2'>串<br/>并<br/>破<br/>案<br/>数</td> <td rowspan='2'>提<br/>取<br/>证<br/>据<br/>数</td> <td rowspan='2'>     案<br/>数</td> <td rowspan='2'>查<br/>破<br/>案<br/>数</td> <td rowspan='2'>人<br/>数</td> <td rowspan='2'>查<br/>破<br/>人<br/>数</td>  <td rowspan='2' colspan='2'>    查<br/>破<br/>案<br/>数</td>   <td rowspan='2'>串</td><td rowspan='2'>起</td>  </tr>  <tr><td>全部<br/>刑案</td><td>十类<br/>案件</td><td>全部<br/>案件</td><td>十类<br/>案件</td><td>全部<br/>案件</td><td>十类<br/>案件</td><td>     全部<br/>刑案</td><td>全部<br/>刑案</td>  </tr>            <tr><td>10</td><td>7</td><td>9</td><td>8</td><td>6</td><td>3</td><td>3</td><td>2</td><td>5</td><td>9</td><td>6</td><td>8</td><td>9</td><td>3</td><td>8</td><td>3</td><td>7</td><td >2</td><td colspan='2'>2</td><td>11</td><td>2</td><td>6</td></tr>  </table>\n";
        BufferedOutputStream buff = null;

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tmpFile);
            buff = new BufferedOutputStream(out);
            buff.write(excelCon.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
        } finally {
            try {
                buff.close();
                out.close();
            } catch (Exception e) {
            }
        }
    }
    public static void main(String[] args) throws IOException {
        //ArrayList<List<DataStatisticsTodayDay>> arrayList = new ArrayList<>();
        //ArrayList<DataStatisticsTodayDay> list = new ArrayList<>();
        //DataStatisticsTodayDay dataStatisticsTodayDay = new DataStatisticsTodayDay(
        //        1,
        //        2,
        //        3
        //);
        //list.add(dataStatisticsTodayDay);
        //list.add(dataStatisticsTodayDay);
        //list.add(dataStatisticsTodayDay);
        //list.add(dataStatisticsTodayDay);
        //arrayList.add(list);
        //arrayList.add(list);

        //File tmpFile = File.createTempFile("ceshi",".xlsx",new File(fileExtraAddPrefix));
        //writeExcelToTemp(arrayList,init(),tmpFile);

        String excelCon = "<table ><tr><td colspan='23' align='center'>分县局刑侦支(大)队技术工作情况统计表</td></tr></table><table><tr><td align='left'>主办单位：</td><td></td> <td  colspan='21' align='right'>统计时间:2023年4月-2023年5月</td></tr></table> <table border='2' width='100%' >   <tr><td rowspan='2' colspan='2'>现场接报<br/>总数</td><td rowspan='2' colspan='2'>现场勘验<br/>总数</td><td rowspan='2' colspan='2'>         现场录入<br/>总数</td><td rowspan='2' colspan='2'>未立案刑<br/>案勘验总<br/>数</td> <td colspan='4'>立案刑案勘验案数</td> <td rowspan='2'>        写出<br/>分析<br/>案数</td><td rowspan='2'>痕迹<br/>提取<br/>案数</td><td rowspan='2'>制作<br/>记录<br/>案数</td><td rowspan='2'>痕迹<br/>建档<br/>案数</td><td rowspan='2'>       受理<br/>检案<br/>案数</td><td rowspan='2'>得出<br/>结论<br/>案数</td>   <td colspan='2'>技术破<br/>案案数</td>   <td colspan='2'>     鉴定书</td> <td rowspan='2'>在岗技<br/>术员数</td></tr> <tr><td>总<br/>计</td><td>九<br/>类</td><td>入室<br/>盗窃</td><td>其<br/>它</td><td>痕迹</td><td>DNA</td><td>案<br/>数</td><td>   份<br/>数</td></tr> <tr><td colspan='2'>5</td><td colspan='2'>11</td><td colspan='2'>11</td><td colspan='2'>3</td><td>          9</td><td>8</td><td>4</td><td>3</td><td>11</td><td>        6</td><td>6</td><td>6</td><td>7</td><td>      9</td><td>4</td><td>8</td><td>2</td><td>6</td><td>3</td></tr> <tr><td colspan='8'>痕迹提取种类</td> <td colspan='6'>发挥作用破案</td> <td colspan='2'>指纹正查档</td> <td colspan='4'>    指纹、足迹倒查档</td> <td colspan='2'>串并案数</td><td rowspan='3'>嫌疑人<br/>十指纹<br/>建档数</td></tr>   <tr> <td colspan='2' >指纹案数</td> <td colspan='2' >足迹案数</td>         <td colspan='2' >DNA提取<br/>案数</td>   <td >工具<br/>案数</td><td >其<br/>它</td>          <td rowspan='2'>总<br/>数</td> <td rowspan='2'>查<br/>档<br/>认<br/>定<br/>数</td> <td rowspan='2'>证<br/>实<br/>认<br/>定<br/>数</td> <td rowspan='2'>      确<br/>定<br/>性<br/>质<br/>数</td> <td rowspan='2'>串<br/>并<br/>破<br/>案<br/>数</td> <td rowspan='2'>提<br/>取<br/>证<br/>据<br/>数</td> <td rowspan='2'>     案<br/>数</td> <td rowspan='2'>查<br/>破<br/>案<br/>数</td> <td rowspan='2'>人<br/>数</td> <td rowspan='2'>查<br/>破<br/>人<br/>数</td>  <td rowspan='2' colspan='2'>    查<br/>破<br/>案<br/>数</td>   <td rowspan='2'>串</td><td rowspan='2'>起</td>  </tr>  <tr><td>全部<br/>刑案</td><td>十类<br/>案件</td><td>全部<br/>案件</td><td>十类<br/>案件</td><td>全部<br/>案件</td><td>十类<br/>案件</td><td>     全部<br/>刑案</td><td>全部<br/>刑案</td>  </tr>            <tr><td>10</td><td>7</td><td>9</td><td>8</td><td>6</td><td>3</td><td>3</td><td>2</td><td>5</td><td>9</td><td>6</td><td>8</td><td>9</td><td>3</td><td>8</td><td>3</td><td>7</td><td >2</td><td colspan='2'>2</td><td>11</td><td>2</td><td>6</td></tr>  </table>\n";
        BufferedOutputStream buff = null;
        FileOutputStream outStr = null;
        File tmpFile = File.createTempFile(DateFormatUtils.format(new Date(),PanXiaoZhang.yMdHms()),".xlsx",new File("D:\\home\\equity\\manage\\target\\classes\\upload\\20230721"));
        try {
            outStr = new FileOutputStream(tmpFile);
            buff = new BufferedOutputStream(outStr);
            buff.write(excelCon.getBytes("UTF-8"));
            buff.flush();
            buff.close();
        } catch (Exception e) {
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
            }
        }
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
    public static List<GetExcel> initMonth(Map<String, String> map,String type,List<String> list){
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
        stringList.add("本" + type + "进件");
        stringList.add("本" + type + "批核");
        stringList.add("本" + type + "有效");
        stringList.add("指标");
        stringList.add("指标完成率");
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            stringList.add(s);
        }
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
                false
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
