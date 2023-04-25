package com.example.manage.util.excel;

import com.example.manage.entity.data_statistics.DataStatisticsTodayDay;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program：潘小章
 * @create：2022-04_17 18:51
 **/
public class ExcelExportUtil {

    /*文件上传额外添加的前缀*/
    //@Value("${magicalcoder.file.upload.useDisk.mapping.fileExtraAddPrefix:}")
    //private static String fileExtraAddPrefix;
    private static String fileExtraAddPrefix = "D:\\home\\equity\\manage\\target\\classes\\upload\\";

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
     * 潘Sir
     * @param pageList
     * @param titles
     * @param tmpFile
     */
    public static void writeExcelToTemp(List<List<DataStatisticsTodayDay>> pageList, String[] titles, File tmpFile) {
        FileOutputStream out = null;
        SXSSFWorkbook wb = new SXSSFWorkbook(1000); // keep 100 rows in memory, exceeding rows will be flushed to disk
        Sheet sh = wb.createSheet();
        try {
            //头部 第1行
            Row headRow = sh.createRow(0);
            //第几列
            Integer titleCellNumber = 0;
            for(int i = 0;i < titles.length;i++){
                // 创建 Drawing 对象
                Drawing drawing = sh.createDrawingPatriarch();
                // 创建 ClientAnchor 对象
                int row = 0;
                int col = 0;
                byte[] byteArray = Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Downloads\\1.png"));
                int pictureIndex = wb.addPicture(byteArray, Workbook.PICTURE_TYPE_JPEG);
                //col1 ：从左向右数 | 的线（从0）
                //row1 ：从上到下数 —— 的线（从0）
                //col2 ：同col1
                //row2 ：同row1
                ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, titleCellNumber, row, col, row);
                anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
                // 创建 Picture 对象，并将其添加到 Drawing 中
                Picture picture = drawing.createPicture(anchor, pictureIndex);

                // 设置 Picture 的大小
                picture.resize(0.2);
                //指定合并开始行、合并结束行 合并开始列、合并结束列
                CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, titleCellNumber, (titleCellNumber + 2) - 1);
                //添加要合并地址到表格
                sh.addMergedRegion(rangeAddress);
                //创建单元格，指定起始列号，从0开始
                Cell cell = headRow.createCell(titleCellNumber);
                //设置单元格内容
                cell.setCellValue(titles[i]);
                //设置单元格类型
                cell.setCellType(Cell.CELL_TYPE_STRING);
                //设置样式
                XSSFCellStyle style = (XSSFCellStyle)wb.createCellStyle();
                //style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                //设置背景颜色
                XSSFColor color = new XSSFColor(new java.awt.Color(111, 111, 111));
                //将颜色加入样式中
                style.setFillForegroundColor(color);
                //让这个设置生效
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                //设置字体颜色
                Font font = wb.createFont();
                font.setColor(IndexedColors.BLACK.getIndex());
                style.setFont(font);
                //将设置好的加入单元格中
                cell.setCellStyle(style);
                //设置下一个是第几列
                titleCellNumber = titleCellNumber + 2;
            }
            //数据
            for(int i = 0; i < pageList.size();i++){
                //创建行，指定起始行号，从0开始
                Row row = sh.createRow(i + 1);
                for(int j = 0;j < pageList.get(i).size();j++){
                    //获取当前内容
                    DataStatisticsTodayDay dataStatisticsTodayDay = pageList.get(i).get(j);
                    //创建单元格，指定起始列号，从0开始
                    Cell cell = row.createCell(j);
                    //设置单元格内容
                    cell.setCellValue(dataStatisticsTodayDay.getActivation());
                    //设置单元格类型
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);

                    System.out.println(Cell.CELL_TYPE_NUMERIC + "============");
                }
            }
            out = new FileOutputStream(tmpFile);
            System.out.println(tmpFile.getParentFile());
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
        String[] strings = {"名称","成功","失败","等待","重申"};
        File tmpFile = File.createTempFile("ceshi",".xlsx",new File(fileExtraAddPrefix));
        writeExcelToTemp(arrayList,strings,tmpFile);
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
                    cell.setCellType(Cell.CELL_TYPE_STRING);
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
