package com.pentagon.app.serviceImpl;


import com.pentagon.app.entity.Student;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelGenerator {

    public ByteArrayInputStream getAllStudentExcel(List<Student> students) throws IOException {
        String[] headers = {
            "Student ID", "Name", "Gender", "Email", "Mobile", "WhatsApp No",
            "10th Pass Out Year", "10th Percentage",
            "12th Pass Out Year", "12th Percentage",
            "Graduation Course", "Graduation Branch",
            "Graduation Percentage", "Graduation CGPA"
        };

        try (
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet("Students");

            // Header Style
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Header Row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowIdx = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                row.createCell(col++).setCellValue(student.getStudentId());
                row.createCell(col++).setCellValue(student.getName());
                row.createCell(col++).setCellValue(student.getGender());
                row.createCell(col++).setCellValue(student.getEmail());
                row.createCell(col++).setCellValue(student.getMobile());
                row.createCell(col++).setCellValue(student.getWhatsappNo());

                row.createCell(col++).setCellValue(student.getTenthPassOutYear() != null ? student.getTenthPassOutYear() : 0);
                row.createCell(col++).setCellValue(student.getTenthPercentage() != null ? student.getTenthPercentage() : 0.0);

                row.createCell(col++).setCellValue(student.getTwelvePassOutYear() != null ? student.getTwelvePassOutYear() : 0);
                row.createCell(col++).setCellValue(student.getTwelvePercentage() != null ? student.getTwelvePercentage() : 0.0);

                row.createCell(col++).setCellValue(student.getGradCourse() != null ? student.getGradCourse() : "");
                row.createCell(col++).setCellValue(student.getGradBranch() != null ? student.getGradBranch() : "");
                row.createCell(col++).setCellValue(student.getGradPercentage() != null ? student.getGradPercentage() : 0.0);
                row.createCell(col++).setCellValue(student.getGradCgpa() != null ? student.getGradCgpa() : 0.0);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
