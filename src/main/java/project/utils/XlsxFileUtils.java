package project.utils;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import project.Page;

import static project.constants.WikiParserConstants.dataXlsxFile;

/**
 * This class contains utility methods for XLSX file handling
 *
 * @author Nathanaël Bayle
 */
public class XlsxFileUtils {

    /**
     * Creates the given file if it doesn't exist
     * @param page the page to write to the file
     */
    public static void writeData(Sheet sheet, Page page) {
        int rowNumber = sheet.getLastRowNum() + 1;

        Row row = sheet.createRow(rowNumber);
        Cell cell = row.createCell(0);
        cell.setCellValue(page.getTitle());

        Cell cell1 = row.createCell(1);
        cell1.setCellValue(page.getCategory());

        Cell cell2 = row.createCell(2);
        cell2.setCellValue(page.getMainIngredientsAsString());

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    /**
     * Creates the given file if it doesn't exist
     * @param workbook the workbook to write to the file
     */
    public static void writeWorkbookToFile(XSSFWorkbook workbook) {
        try (FileOutputStream outputStream = new FileOutputStream(dataXlsxFile)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Define the cell style for the header
     * @param workbook the workbook to use
     * @param sheet the sheet to use
     */
    public static void setHeaderStyle(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 13);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Title");
        Cell cell1 = row.createCell(1);
        cell1.setCellValue("Category");
        Cell cell2 = row.createCell(2);
        cell2.setCellValue("Ingredients");

        cell.setCellStyle(headerStyle);
        cell1.setCellStyle(headerStyle);
        cell2.setCellStyle(headerStyle);
    }
}
