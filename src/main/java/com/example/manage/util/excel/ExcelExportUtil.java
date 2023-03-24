package com.example.manage.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
