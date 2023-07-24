package com.example.manage.util.excel;

import java.io.FileInputStream;
import java.io.IOException;

import com.example.manage.entity.CardType;
import com.example.manage.entity.ranking_list.RankingList;
import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.GetExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/6/13
 */

public class XlsxLayoutReader {
    public static List<GetExcel> template1(CardType cardType, int row, int rowEnd, List<RankingList> queryAllCount, Map<String, String> stringMap,RankingList rankingList,Integer numberOfPeople,Integer attendance){
            //List<GetExcel> arrayList = template2(
            //        cardType,
            //        row,
            //        queryAllCount,
            //        stringMap,
            //        rankingList,
            //        numberOfPeople,
            //        attendance
            //);
            List<GetExcel> arrayList = new ArrayList<>();
            arrayList.add(
                new GetExcel(
                    cardType.getName(),
                    null,
                    "text",
                    ExcelExportUtil.colorWhite(),
                    ExcelExportUtil.colorBlack(),
                    0,
                    row,
                    rowEnd,
                    0,
                    0,
                    HorizontalAlignment.CENTER,
                    VerticalAlignment.CENTER,
                    (short) 11,
                    false,
                    true
            ));
        return arrayList;
    }
    public static List<GetExcel> template2(CardType cardType, int row, List<RankingList> queryAllCount, Map<String, String> stringMap,RankingList rankingList,Integer numberOfPeople,Integer attendance){
        List<GetExcel> arrayList = new ArrayList<>();
        arrayList.add(new GetExcel(
                stringMap.get("management" + rankingList.getId()),
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                1,
                1,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                stringMap.get("personnel" + rankingList.getId()),
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                2,
                2,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                rankingList.getApproved() + "",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                5,
                5,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                rankingList.getApproved() + "",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                6,
                6,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                rankingList.getActivation() + "",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                7,
                7,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                numberOfPeople + "",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                3,
                3,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                attendance + "",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                4,
                4,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                PanXiaoZhang.percent(rankingList.getActivation(),rankingList.getApproved()) + "%",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                9,
                9,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));

        arrayList.add(new GetExcel(
                String.valueOf(PanXiaoZhang.percent(rankingList.getApproved(),attendance,1)),
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                10,
                10,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                "",
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                8,
                8,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        return arrayList;
    }

    public static List<GetExcel> template3(CardType cardType, int row,Integer countNumber,Integer approved,Integer activation,Integer countAttendance,Integer countNumberOfPeople){
        List<GetExcel> arrayList = new ArrayList<>();
        arrayList.add(new GetExcel(
                cardType.getName() + "合计",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                1,
                2,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                true
        ));
        arrayList.add(new GetExcel(
                countNumber + "",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                5,
                5,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                approved + "",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                6,
                6,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                activation + "",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                7,
                7,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                countNumberOfPeople + "",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                3,
                3,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                countAttendance + "",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                4,
                4,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                PanXiaoZhang.percent(activation,approved) + "%",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                9,
                9,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                String.valueOf(PanXiaoZhang.percent(approved,countAttendance,1)),
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                10,
                10,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        arrayList.add(new GetExcel(
                "",
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                row,
                row,
                8,
                8,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                false
        ));
        return arrayList;
    }

    /**
     * 一个单元格
     * @param name
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @return
     */
    public static GetExcel text(String name,int startRow,int endRow,int startCol,int endCol){
        Boolean mergerSwitch = false;
        if (startRow != endRow || startCol != endCol){
            mergerSwitch = true;
        }
        return new GetExcel(
                name,
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                startRow,
                endRow,
                startCol,
                endCol,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                mergerSwitch
        );
    }

    /**
     * 
     * @param name
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @param xssfColor 背景颜色
     * @return
     */
    public static GetExcel text(String name,int startRow,int endRow,int startCol,int endCol,XSSFColor xssfColor){
        Boolean mergerSwitch = false;
        if (startRow != endRow || startCol != endCol){
            mergerSwitch = true;
        }
        return new GetExcel(
                name,
                null,
                "text",
                xssfColor,
                ExcelExportUtil.colorBlack(),
                0,
                startRow,
                endRow,
                startCol,
                endCol,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                mergerSwitch
        );
    }

    /**
     *
     * @param name
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @param xssfColor 背景颜色
     * @param color 字体颜色
     * @return
     */
    public static GetExcel text(String name,int startRow,int endRow,int startCol,int endCol,XSSFColor xssfColor,XSSFColor color){
        Boolean mergerSwitch = false;
        if (startRow != endRow || startCol != endCol){
            mergerSwitch = true;
        }
        return new GetExcel(
                name,
                null,
                "text",
                xssfColor,
                color,
                0,
                startRow,
                endRow,
                startCol,
                endCol,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                mergerSwitch
        );
    }
    /**
     * 合并单元格
     * @param name
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @return
     */
    public static GetExcel textColorYellow(String name,int startRow,int endRow,int startCol,int endCol){
        Boolean mergerSwitch = false;
        if (startRow != endRow || startCol != endCol){
            mergerSwitch = true;
        }
        return new GetExcel(
                name,
                null,
                "text",
                ExcelExportUtil.colorYellow(),
                ExcelExportUtil.colorBlack(),
                0,
                startRow,
                endRow,
                startCol,
                endCol,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                true,
                mergerSwitch
        );
    }
    /**
     * 合并单元格
     * @param name
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @return
     */
    public static GetExcel textSwitch(String name,int startRow,int endRow,int startCol,int endCol){
        return new GetExcel(
                name,
                null,
                "text",
                ExcelExportUtil.colorWhite(),
                ExcelExportUtil.colorBlack(),
                0,
                startRow,
                endRow,
                startCol,
                endCol,
                HorizontalAlignment.CENTER,
                VerticalAlignment.CENTER,
                (short) 11,
                false,
                true
        );
    }
    public static void main(String[] args) {

    }
}
