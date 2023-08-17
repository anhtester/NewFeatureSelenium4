package com.anhtester.handles.HandleExcelFile;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class HandleExcelFile {
    @Test
    public void createExcelFile() {
        //Tạo Sheet mới và set Text vào ô
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Anh Tester");
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[]{
                "" //Tạo data rỗng vào ô Cell đầu tiên trong sheet chỉ định bên trên
        });

        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        }

        try {
            //Muốn .xlsx hay .xls thì đổi cái đuôi file lại
            FileOutputStream out = new FileOutputStream(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\demo.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Create Excel file successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
