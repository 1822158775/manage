package com.example.manage.util.excel;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.GetExcel;
import com.example.manage.util.file.config.UploadFilePathConfig;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2023/6/13
 */

public class ExcelUtil {

    /**
     * 导出xlsx文件
     *
     * @param filePath     导出文件路径
     * @param sheetName    工作表名称
     * @param headers      表头
     * @param data         数据
     * @param mergeRows    需要合并的行数
     * @param mergeColumns 需要合并的列数
     * @param bgColor      背景色
     * @param fontColor    字体颜色
     * @throws IOException IO异常
     */
    public static void exportExcel(String filePath, String sheetName, String[] headers, List<String[]> data,
                                   int[] mergeRows, int[] mergeColumns, short bgColor, short fontColor) throws IOException {
        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置默认行高
        sheet.setDefaultRowHeightInPoints(20);
        // 设置默认列宽
        sheet.setDefaultColumnWidth(20);

        // 创建表头样式
        XSSFCellStyle headerStyle = createCellStyle(workbook, bgColor, fontColor, true);

        // 创建表头行
        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 创建数据样式
        XSSFCellStyle dataStyle = createCellStyle(workbook, bgColor, fontColor, false);

        // 填充数据
        int rowIndex = 1;
        for (String[] rowData : data) {
            XSSFRow row = sheet.createRow(rowIndex++);
            for (int i = 0; i < rowData.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(rowData[i]);
                cell.setCellStyle(dataStyle);
            }
        }

        // 合并行
        if (mergeRows != null && mergeRows.length > 0) {
            for (int i = 0; i < mergeRows.length; i += 2) {
                sheet.addMergedRegion(new CellRangeAddress(mergeRows[i], mergeRows[i + 1], 0, headers.length - 1));
            }
        }

        // 合并列
        if (mergeColumns != null && mergeColumns.length > 0) {
            for (int i = 0; i < mergeColumns.length; i += 2) {
                sheet.addMergedRegion(new CellRangeAddress(0, data.size(), mergeColumns[i], mergeColumns[i + 1]));
            }
        }

        // 输出文件
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        }
    }

    /**
     * 创建单元格样式
     *
     * @param workbook  工作簿
     * @param bgColor   背景色
     * @param fontColor 字体颜色
     * @param isHeader  是否是表头
     * @return 单元格样式
     */
    private static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, short bgColor, short fontColor, boolean isHeader) {
        // 创建单元格样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置背景色
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        // 创建字体
        XSSFFont font = workbook.createFont();
        font.setColor(fontColor);
        font.setBold(isHeader);
        // 设置字体
        style.setFont(font);
        // 设置水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 判断路径是否存在，如果不存在则创建
     *
     * @param path 路径
     * @return 是否创建成功
     */
    public static boolean createIfNotExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    /**
     * 判断文件是否存在，如果不存在则创建
     *
     * @param filePath 文件路径
     * @return 是否创建成功
     * @throws IOException IO异常
     */
    public static boolean createIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file.createNewFile();
    }

    public static void main(String[] args) throws IOException {
        // 表头
        String[] headers = {"姓名", "年龄", "性别", "成绩"};
        // 数据
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"张三", "18", "男", "80"});
        data.add(new String[]{"李四", "19", "女", "90"});
        data.add(new String[]{"王五", "20", "男", "85"});
        // 合并行
        int[] mergeRows = {1, 2, 3, 4};
        // 合并列
        int[] mergeColumns = {0, 1};
        //判断文件是否存在
        boolean ifNotExist = createIfNotExist("./upload");
        boolean ifNotExists = createIfNotExists("./upload/test.xlsx");
        System.out.println(ifNotExists);
        // 导出xlsx文件
        ExcelUtil.exportExcel("./upload/test.xlsx", "Sheet1", headers, data, mergeRows, mergeColumns, IndexedColors.LIGHT_GREEN.getIndex(), (short) 1);
    }
}

