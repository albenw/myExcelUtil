package com.albenw.excel.base.writer;

import com.albenw.excel.base.IndexingField;
import com.albenw.excel.base.constant.ExcelTypeEnum;
import com.albenw.excel.base.constant.ParserTypeEnum;
import com.albenw.excel.base.context.WriterContext;
import com.albenw.excel.exception.ErrorCode;
import com.albenw.excel.exception.ExcelException;
import com.albenw.excel.util.CollectionUtil;
import com.albenw.excel.util.StringUtil;
import com.albenw.excel.util.WorkBookUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author alben.wong
 * @since 2019-02-05.
 */
@Setter @Getter
@Slf4j
public class ExcelWriter {

    private WriterContext context;

    private <T> ExcelWriter(Class<T> targetClass, ExcelTypeEnum excelType , String sheetName, OutputStream out,
                            List<IndexingField> fields){
        this.context = new WriterContext(targetClass, excelType, sheetName, out, fields);
    }

    public void write(List data) throws ExcelException, IOException{
        Workbook workbook = WorkBookUtil.getWorkbook(context.getExcelType());
        Sheet sheet = workbook.createSheet(context.getSheetName());
        WorkBookUtil.createHeaderRow(workbook, sheet, context.getFields());
        WorkBookUtil.createDataRow(data, sheet, context.getFields());
        writeToStream(workbook, context.getOut());
    }

    public static <T> Builder<T> newBuilder() {
        return new Builder();
    }


    public static class Builder<T> {
        private Class<T> targetClass;
        private ExcelTypeEnum excelType;
        private String fileName;
        private String sheetName;
        private OutputStream out;
        private List<IndexingField> fields;

        public Builder targetClass(Class<T> targetClass){
            this.targetClass = targetClass;
            return this;
        }

        public Builder excelType(ExcelTypeEnum excelType){
            this.excelType = excelType;
            return this;
        }

        public Builder fileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        public Builder sheetName(String sheetName){
            this.sheetName = sheetName;
            return this;
        }

        public Builder outputStream(OutputStream out){
            this.out = out;
            return this;
        }

        public ExcelWriter build() throws ExcelException{
            checkBuildParameter();
            return new ExcelWriter(this.targetClass, this.excelType, this.sheetName, this.out, this.fields);
        }

        private void checkBuildParameter() throws ExcelException {
            if(this.out == null){
                throw new ExcelException(ErrorCode.PARAMETER_ERROR, "缺少outputStream参数");
            }
            if(this.targetClass == null){
                throw new ExcelException(ErrorCode.PARAMETER_ERROR, "缺少targetClass参数");
            }
            if(this.excelType == null){
                throw new ExcelException(ErrorCode.PARAMETER_ERROR, "缺少excelType参数");
            }
            if(StringUtil.isBlank(this.fileName)){
                throw new ExcelException(ErrorCode.PARAMETER_ERROR, "缺少fileName参数");
            }
            if(StringUtil.isBlank(this.sheetName)){
                throw new ExcelException(ErrorCode.PARAMETER_ERROR, "缺少sheetName参数");
            }
            List<IndexingField> indexingFields = CollectionUtil.sortExportFieldByIndex(targetClass);
            if(CollectionUtil.isEmpty(indexingFields)){
                throw new ExcelException(ErrorCode.PARAMETER_ERROR, String.format("%s类缺少ExportField注解", this.targetClass.getName()));
            }
            this.fields = indexingFields;
        }

    }

    private void writeToStream(Workbook workbook, OutputStream os) throws IOException {
        BufferedOutputStream bos = null;
        try{
            bos = new BufferedOutputStream(os);
            workbook.write(bos);
            bos.flush();
        }catch (IOException e){
            log.error("导出异常");
            throw  e;
        }finally {
            try {
                if(os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("关闭流异常");
            }
        }
    }

}
