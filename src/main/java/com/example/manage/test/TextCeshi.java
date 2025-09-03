package com.example.manage.test;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.manage.ManageApplication;
import com.example.manage.entity.SysPersonnel;
import com.example.manage.mapper.ISysPersonnelMapper;
import com.example.manage.util.PanXiaoZhang;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageApplication.class)
@Slf4j
public class TextCeshi {

    @Test
    public void String_aaaa(){
//        System.out.println("11111111");
        li_zhi();
    }
    @Resource
    private ISysPersonnelMapper iSysPersonnelMapper;
    public void li_zhi(){
        try {
            QueryWrapper wrapper = new QueryWrapper();
            // 读取代码示例
            FileInputStream fis = new FileInputStream("/Users/nicolehou/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/826207052f4999abc3d09850a7e821ed/Message/MessageTemp/c45f835a2a5a2bff221efed3bdd197d2/File/离职名单.xls");
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);
            for (Integer i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String stringCellValue = row.getCell(1).getStringCellValue();
                Date dateCellValue = row.getCell(2).getDateCellValue();
                wrapper = new QueryWrapper();
                wrapper.eq("name", stringCellValue);
                List<SysPersonnel> sysPermissions = iSysPersonnelMapper.selectList(wrapper);
                if (sysPermissions != null && sysPermissions.size() < 2) {
                    SysPersonnel sysPersonnel = sysPermissions.get(0);
                    sysPersonnel.setEmploymentStatus(0);
                    sysPersonnel.setLeaveTime(dateCellValue);
                    iSysPersonnelMapper.updateById(sysPersonnel);
                }else {
                    log.info(stringCellValue);
                }
//                for (Cell cell : row) {
//                    System.out.print(cell.getStringCellValue() + "\t");
//                }
//                System.out.printf(DateFormatUtils.format(dateCellValue, "yyyy-MM-dd HH:mm:ss"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}