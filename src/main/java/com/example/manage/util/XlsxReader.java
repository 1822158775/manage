package com.example.manage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.entity.ManagementPersonnel;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.IManagementPersonnelMapper;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.util.entity.GetExcelEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;


/**
 * @avthor 潘小章
 * @date 2023/6/8
 */
@Slf4j
@Component
public class XlsxReader {

    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;

    @Resource
    private IManagementPersonnelMapper iManagementPersonnelMapper;

    public void add(Integer managemenId,String managemenName){
        String file_url = "D:\\user\\WeChat Files\\WeChat Files\\wxid_efhc3urrnl0x22\\FileStorage\\File\\2023-06\\人员管理信息表202305.xlsx";
        try {
            File file = new File(file_url);
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("在职人员信息表"); //选择Sheet1
            int rows = sheet.getLastRowNum() + 1; //获取行数
            int columns = sheet.getRow(0).getLastCellNum(); //获取列数
            //返回所有内容
            List<GetExcelEntity> stringList = new ArrayList<>();
            for (int i = 1; i < rows; i++) {
                //for (int j = 0; j < columns; j++) {
                //    System.out.println(sheet.getRow(i).getCell(j));
                //}
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                log.info("添加:{}",cell);
                if (cell != null && String.valueOf(cell).replaceAll(" ","").equals(managemenName)){
                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    Cell cell1 = row.getCell(1);
                    cell1.setCellType(Cell.CELL_TYPE_STRING);


                    Cell cell2 = row.getCell(2);
                    cell2.setCellType(Cell.CELL_TYPE_STRING);


                    Cell cell4 = row.getCell(4);
                    cell4.setCellType(Cell.CELL_TYPE_STRING);


                    Cell cell5 = row.getCell(5);
                    cell5.setCellType(Cell.CELL_TYPE_STRING);


                    Cell cell9 = row.getCell(9);


                    Cell cell17 = row.getCell(17);
                    cell17.setCellType(Cell.CELL_TYPE_STRING);


                    Cell cell18 = row.getCell(18);
                    cell18.setCellType(Cell.CELL_TYPE_STRING);


                    Cell cell19 = row.getCell(19);
                    cell19.setCellType(Cell.CELL_TYPE_STRING);

                    String value0 = String.valueOf(cell);
                    String value1 = String.valueOf(cell1);
                    Integer value2 = getRole(String.valueOf(cell2));
                    String value3 = String.valueOf(cell4);
                    String value4 = String.valueOf(cell5);
                    String value6 = String.valueOf(cell17);
                    String value7 = String.valueOf(cell18);
                    String value8 = String.valueOf(cell19);
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //日期格式化
                    Date dateCellValue = cell9.getDateCellValue();

                    GetExcelEntity entity = new GetExcelEntity(
                            value0,
                            value1,
                            value2,
                            PanXiaoZhang.getID(),
                            value3,
                            value4,
                            "",
                            "",
                            dateCellValue,
                            value6,
                            value7,
                            value8
                    );
                    entity.setPositionPost(positionPost(String.valueOf(cell2)));
                    stringList.add(entity);
                }
            }
            for (GetExcelEntity entity :
                    stringList) {
                entity.getIdNumber();
                Date birthDate = new Date();
                if (entity.getIdNumber() != null && entity.getIdNumber().length() > 0){
                    birthDate = getBirthDate(entity.getIdNumber(), PanXiaoZhang.yMd());
                }
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("username",entity.getPhone());
                SysPersonnel personnel = iSysPersonnelMapper.selectOne(wrapper);
                if (ObjectUtils.isEmpty(personnel)){
                    int insert = iSysPersonnelMapper.insert(new SysPersonnel(
                            null,
                            entity.getPhone(),
                            null,
                            entity.getPersonnelCode(),
                            entity.getPhone(),
                            entity.getRoleId(),
                            PanXiaoZhang.getPassword("123456"),
                            entity.getUserName(),
                            1,
                            null,
                            birthDate,
                            1,
                            null,
                            null,
                            "女",
                            null,
                            1,
                            3,
                            3,
                            entity.getEntryTime(),
                            entity.getIdNumber(),
                            entity.getEmergencyContactName(),
                            entity.getEmergencyContactPhone(),
                            entity.getPermanentResidence(),
                            null,
                            null
                    ));
                    log.info("添加:{},结果:{}",entity.getUserName(),insert);
                    int insert1 = iManagementPersonnelMapper.insert(new ManagementPersonnel(
                            managemenId,
                            entity.getPersonnelCode()
                    ));
                    log.info("添加:{},绑定结果:{}",entity.getUserName(),insert1);
                }
            }
            //for (Row row : sheet) {
            //    for (Cell cell : row) {
            //        System.out.println(cell);
            //    }
            //}
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 从身份证号码中获取出生日期
     * @param idCard 身份证号码
     * @param format 日期格式，例如"yyyy-MM-dd"
     * @return 出生日期字符串
     */
    public static Date getBirthDate(String idCard, String format) {
        String birthday = idCard.substring(6, 14); // 截取出生日期，即第7位到第14位
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        try {
            Date birthDate = sdf1.parse(birthday);
            //String format1 = sdf2.format(birthDate);
            return birthDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Integer getRole(String str){
        if (str.equals("主管") || str.equals("副主管")){
            return 1;
        }else {
            return 5;
        }
    }
    public static Integer positionPost(String str){
        if (str.equals("服务岗") || str.equals("服务组长")){
            return 2;
        }else {
            return 1;
        }
    }
}
