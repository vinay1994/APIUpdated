package com.flipLearn.bl.xlsreader;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class XlsReader {
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private Workbook workbook = null;
    private Sheet sheet = null;
    private Row row = null;
    private Cell cell = null;

    public XlsReader(String path) {
        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // returns the row count in a sheet
    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }

    }

    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            int col_Num = -1;
            if (rowNum <= 0)
                return "";
            sheet = workbook.getSheet(sheetName);

            for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                if (sheet.getRow(0).getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                    col_Num = i;
                    break;
                }
            }

            if (col_Num == -1)
                return "";

            row = sheet.getRow(rowNum - 1);
            if (row == null)
                return "";

            cell = row.getCell(col_Num);
            if (cell == null)
                return "";

            switch (cell.getCellTypeEnum()) {
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case STRING:
                    return cell.getRichStringCellValue().getString();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
                        return dateFormat.format(date);
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case FORMULA:
                    return cell.getStringCellValue();
                default:
                    return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Row " + rowNum + " or Column " + colName + " does not exist in xls";
        }
    }

    public String getCellData(String sheetName, int colNum, int rowNum) {
        try {
            if (rowNum <= 0 || colNum <= 0)
                return "";

            sheet = workbook.getSheet(sheetName);
            row = sheet.getRow(rowNum - 1);
            cell = row.getCell(colNum - 1);

            switch (cell.getCellType()) {
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case STRING:
                    return cell.getRichStringCellValue().getString();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
                        return dateFormat.format(date);
                    } else {
                        return String.valueOf(cell.getNumericCellValue());
                    }
                case FORMULA:
                    return cell.getStringCellValue();
                default:
                    return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean setCellData(String sheetName, int colNum, int rowNum, String data) {
        try {
            if (rowNum <= 0)
                return false;
            if (colNum <= 0)
                return false;

            rowNum = rowNum - 1;
            colNum = colNum - 1;

            sheet = workbook.getSheet(sheetName);
            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum);

            if (row == null)
                row = sheet.createRow(rowNum);

            cell = row.getCell(colNum);

            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        try {
            if (rowNum <= 0)
                return false;

            int colNum = -1;
            sheet = workbook.getSheet(sheetName);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addSheet(String sheetname) {
        FileOutputStream fileOut;
        try {
            workbook.createSheet(sheetname);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeSheet(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return false;

        FileOutputStream fileOut;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean isSheetExist(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            return index != -1;
        } else
            return true;
    }

    public int getColumnCount(String sheetName) {
        if (!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null)
            return -1;

        return row.getLastCellNum();

    }

    // to run this on stand alone
    public int getCellRowNum(String sheetName, String colName, String cellValue) {
        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;
    }

    public List<Object> convertToDataMap(String sheetName) {
        List<Object> dataList = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        for (int i = 1; i <= sheet.getRow(1).getLastCellNum(); i++) {
            headers.add(getCellData(sheetName, i, 1));
        }

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            final int rowNo = i;
            HashMap<String, String> dataMap = new LinkedHashMap<>();
            for (String colName : headers) {
                dataMap.put(colName, getCellData(sheetName, colName, rowNo));
            }
            dataList.add(dataMap);
        }

        System.out.println(dataList);
        return dataList;
    }

}

