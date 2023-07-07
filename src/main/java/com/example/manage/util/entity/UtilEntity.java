package com.example.manage.util.entity;

import com.example.manage.entity.SysManagement;
import com.example.manage.util.PanXiaoZhang;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @avthor 潘小章
 * @date 2023/6/27
 */

@Data
@ToString
public class UtilEntity implements Serializable {
    public Boolean locationInRange;
    public SysManagement sysManagement;

    public UtilEntity() {
    }

    public UtilEntity(Boolean locationInRange, SysManagement sysManagement) {
        this.locationInRange = locationInRange;
        this.sysManagement = sysManagement;
    }

    public static void main(String[] args) throws IOException {
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMd(1));
        String path = "D:\\home\\equity\\manage\\target\\classes\\upload\\" + format;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File tmpFile = File.createTempFile(format,".xlsx",new File(path));
        FileOutputStream out = null;
        XSSFWorkbook wb = new XSSFWorkbook();; // keep 100 rows in memory, exceeding rows will be flushed to disk
        XSSFSheet sheet = wb.createSheet("Sheet1");
        try {
            Row headRow = sheet.createRow(0);
            //创建单元格，指定起始列号，从0开始
            Cell cell = headRow.createCell(0);
            //设置单元格内容
            cell.setCellValue("测试");
            sheet.addMergedRegion(new CellRangeAddress(
                    0
                    , 1
                    , 0
                    , 8
            ));
            headRow = sheet.getRow(0);
            //创建单元格，指定起始列号，从0开始
            cell = headRow.createCell(9);
            //设置单元格内容
            cell.setCellValue("测试asdsadsads");
            sheet.addMergedRegion(new CellRangeAddress(
                    0
                    , 1
                    , 9
                    , 11
            ));

            headRow = sheet.createRow(11);
            //创建单元格，指定起始列号，从0开始
            cell = headRow.createCell(2);
            //设置单元格内容
            cell.setCellValue("2-3");
            sheet.addMergedRegion(new CellRangeAddress(
                    11
                    , 11
                    , 2
                    , 3
            ));


            headRow = sheet.createRow(3);
            //创建单元格，指定起始列号，从0开始
            cell = headRow.createCell(0);
            //设置单元格内容
            cell.setCellValue("测试asdsadsads");
            sheet.addMergedRegion(new CellRangeAddress(
                    3
                    , 39
                    , 0
                    , 0
            ));

            //headRow = sheet.getRow(3);
            ////创建单元格，指定起始列号，从0开始
            //cell = headRow.createCell(1);
            ////设置单元格内容
            //cell.setCellValue("测试asdsadsads");
            //sheet.addMergedRegion(new CellRangeAddress(
            //        3
            //        , 11
            //        , 1
            //        , 1
            //));

            //headRow = sheet.createRow(18);
            ////创建单元格，指定起始列号，从0开始
            //cell = headRow.createCell(2);
            ////设置单元格内容
            //cell.setCellValue("测试asdsadsads");
            //sheet.addMergedRegion(new CellRangeAddress(
            //        18
            //        , 18
            //        , 2
            //        , 3
            //));

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
}
