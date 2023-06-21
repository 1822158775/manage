package com.example.manage.util.entity;

import lombok.Data;
import lombok.ToString;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * @avthor 潘小章
 * @date 2022/8/31
 */
@Data
@ToString
public class GetExcel implements Serializable {
    public String name;//标题
    public List<String> value;//内容
    public String type;//类型
    public XSSFColor bgcolor;//背景颜色
    public XSSFColor color;//背景颜色
    public int startCell;//起始列
    public int firstRow;//合并行开头
    public int lastRow;//合并行结束
    public int firstCol;//合并列开始
    public int lastCol;//合并列结束
    public HorizontalAlignment horizontalAlignment;//左右居中
    public VerticalAlignment verticalAlignment;//上下居中
    public short fontSize;//字体大小
    public Boolean bold;//字体粗细
    public List<GetExcel> getExcels;
    public Boolean mergeSwitch;//合并开关
    public Boolean rowBorderSwitch;//合并开关
    public List<GetExcel> getExcelList;//内容
    public Boolean widthSwitch;//宽度变动开关

    public GetExcel() {
    }

    public GetExcel(String name, List<String> value) {
        this.name = name;
        this.value = value;
    }

    public GetExcel(String name, List<String> value, String type, XSSFColor bgcolor, XSSFColor color) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.bgcolor = bgcolor;
        this.color = color;
    }

    public GetExcel(int firstRow, List<GetExcel> getExcels) {
        this.firstRow = firstRow;
        this.getExcels = getExcels;
    }

    public GetExcel(int firstRow, int lastRow, List<GetExcel> getExcels, Boolean rowBorderSwitch) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.getExcels = getExcels;
        this.rowBorderSwitch = rowBorderSwitch;
    }

    public GetExcel(String name, List<String> value, String type, XSSFColor bgcolor, XSSFColor color, int startCell, int firstRow, int lastRow, int firstCol, int lastCol, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, short fontSize, Boolean bold, Boolean mergeSwitch) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.bgcolor = bgcolor;
        this.color = color;
        this.startCell = startCell;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.fontSize = fontSize;
        this.bold = bold;
        this.mergeSwitch = mergeSwitch;
    }

    public GetExcel(String name, List<String> value, String type, XSSFColor bgcolor, XSSFColor color, int startCell, int firstRow, int lastRow, int firstCol, int lastCol, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, short fontSize, Boolean bold, Boolean mergeSwitch, Boolean widthSwitch) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.bgcolor = bgcolor;
        this.color = color;
        this.startCell = startCell;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.fontSize = fontSize;
        this.bold = bold;
        this.mergeSwitch = mergeSwitch;
        this.widthSwitch = widthSwitch;
    }

    public GetExcel(String name, List<String> value, String type, XSSFColor bgcolor, XSSFColor color, int startCell, int firstRow, int lastRow, int firstCol, int lastCol, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, short fontSize, Boolean bold, Boolean mergeSwitch,List<GetExcel> getExcelList) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.bgcolor = bgcolor;
        this.color = color;
        this.startCell = startCell;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.fontSize = fontSize;
        this.bold = bold;
        this.mergeSwitch = mergeSwitch;
        this.getExcelList = getExcelList;
    }
    public GetExcel(String name, List<String> value, String type, XSSFColor bgcolor, XSSFColor color, int startCell, int firstRow, int lastRow, int firstCol, int lastCol, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, short fontSize, Boolean bold, List<GetExcel> getExcels) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.bgcolor = bgcolor;
        this.color = color;
        this.startCell = startCell;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.fontSize = fontSize;
        this.bold = bold;
        this.getExcels = getExcels;
    }
}
