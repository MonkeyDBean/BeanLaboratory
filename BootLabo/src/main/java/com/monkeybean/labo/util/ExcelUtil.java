package com.monkeybean.labo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Map;

/**
 * 工具类: 导出Excel文档
 * <p>
 * Created by MonkeyBean on 2019/1/2.
 */
public class ExcelUtil {

    /**
     * 创建Excel文档
     *
     * @param originData  数据列表
     * @param sheetName   表名
     * @param columnNames 列名
     */
    public static Workbook createWorkBook(List<Map<String, Object>> originData, String sheetName, String[] columnNames) {

        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();

        // 创建sheet
        Sheet sheet = wb.createSheet(sheetName);

        // 手动设置列宽
        for (int i = 0; i < originData.size(); i++) {
            sheet.setColumnWidth(i, 30 * 256);
        }

        // 创建两种单元格格式(分别用于列名及数据)
        CellStyle cellStyle1 = wb.createCellStyle();
        CellStyle cellStyle2 = wb.createCellStyle();

        // 创建两种字体
        Font font1 = wb.createFont();
        Font font2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        font1.setFontHeightInPoints((short) 10);
        font1.setColor(IndexedColors.BLACK.getIndex());
        font1.setBold(true);

        // 创建第二种字体样式（用于值）
        font2.setFontHeightInPoints((short) 10);
        font2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cellStyle1.setFont(font1);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.THIN);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);

        // 设置第二种单元格的样式（用于值）
        cellStyle2.setFont(font2);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(cellStyle1);
        }

        // 设置数据
        for (short i = 1; i < originData.size(); i++) {

            // Row(行), Cell(方格), Row 和 Cell 都从0开始计数
            Row rowI = sheet.createRow(i);

            // 在Row上创建一个方格
            for (short j = 0; j < columnNames.length; j++) {
                Cell cell = rowI.createCell(j);
                cell.setCellValue(originData.get(i).get(columnNames[j]) == null ? " " : originData.get(i).get(columnNames[j]).toString());
                cell.setCellStyle(cellStyle2);
            }
        }
        return wb;
    }
}
