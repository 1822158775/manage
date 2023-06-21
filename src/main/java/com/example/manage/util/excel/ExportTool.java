package com.example.manage.util.excel;

import com.example.manage.util.PanXiaoZhang;
import com.example.manage.util.entity.GetExcel;
import com.example.manage.util.file.config.UploadFilePathConfig;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @avthor 潘小章
 * @date 2023/6/19
 */
@Component
public class ExportTool {

    /*上传目录*/
    @Value("${magicalcoder.file.upload.useDisk.mapping.ap:}")
    private String ap;

    @Resource
    private UploadFilePathConfig uploadFilePathConfig;

    /**
     * 导出xlsx
     * @param getExcels
     * @return
     * @throws IOException
     */
    public String xlsx(List<GetExcel> getExcels) throws IOException {
        String format = DateFormatUtils.format(new Date(), PanXiaoZhang.yMd(1));
        String path = ap + format;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File tmpFile = File.createTempFile(format,".xlsx",new File(path));
        ExcelExportUtil.writeExcelToTemp(new ArrayList<>(),getExcels,tmpFile);
        return uploadFilePathConfig.getFileExtraAddPrefix() + format + "/" + tmpFile.getName();
    }
}
