/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package com.thegoate.spreadsheets.utils;

import com.thegoate.Goate;
import com.thegoate.utils.GoateUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides support for excel files (xls and xlsx)
 * Created by Eric Angeli on 9/14/2017.
 */
@GoateSheet(fileTypes = {"xls", "xlsx"})
public class ExcelSheet extends SheetUtils {
    XSSFSheet sheet = null;
    XSSFWorkbook workbook = null;
    FileInputStream in = null;

    public int rowCount(){
        int rowCount = 0;
        if(sheet !=null){
            rowCount = sheet.getLastRowNum();
        }
        return rowCount;
    }

    @Override
    public Goate load() {
        data = new Goate();
        Goate sheetData = new Goate();
        sheet = loadSheet();
        if (sheet == null) {
            LOG.warn("could not load the sheet: " + sheetName);
        } else {
            Iterator<Row> rowIterator = sheet.iterator();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            boolean headersFound = false;
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Goate theRow = new Goate();
                Row row = rowIterator.next();
                if (row.getRowNum() != rowIndex) {
                    if(loadAllData){
                        rowIndex = row.getRowNum();
                    }else {
                        break;
                    }
                }
                Iterator<Cell> col = row.cellIterator();
                List<String> headerList = new ArrayList<>();
                while (col.hasNext()) {
                    Cell cell = col.next();
                    if (cell != null) {
                        CellValue cellValue = evaluator.evaluate(cell);
                        String value = cellValue.formatAsString().replace("\"", "");
                        if (firstRowIsHeader && !headersFound) {
                            headerList.add("" + value);
                        } else {
                            String id = "" + cell.getColumnIndex();
                            if (firstRowIsHeader) {
                                int colId = Integer.parseInt(id);
                                if (colId < headers.get(sheetName).size()) {
                                    id = headers.get(sheetName).get(colId);
                                } else {
                                    value = null;
                                }
                            }
                            if (value != null) {
                                theRow.put(id, value);
                            }
                        }
                    }
                }
                if (headersFound || !firstRowIsHeader) {
                    sheetData.put("" + (firstRowIsHeader ? rowIndex - 1 : rowIndex), theRow);
                } else {
                    headers.put(sheetName, headerList);
                }
                rowIndex++;
                headersFound = true;
            }
            data.put(sheetName, sheetData);
        }
        if(in != null){
            try {
                in.close();
            }catch(Exception e){
                LOG.info("Excel Sheet", "Problem closing input stream: " + e.getMessage(), e);
            }
        }
        return data;
    }

    private XSSFSheet loadSheet() {
        sheet = null;
        try {
            File file = new File(GoateUtils.getFilePath(fileName));
            if (file.exists()) {
                if(in!=null){
                    in.close();
                }
                in = new FileInputStream(file);
                workbook = new XSSFWorkbook(in);
                sheet = workbook.getSheet(sheetName);
            }
        } catch (IOException ioe) {
            LOG.error("The file (" + fileName + ") could not be found: " + ioe.getMessage(), ioe);
        }
        return sheet;
    }

    @Override
    public SheetUtils createNew() {
        workbook = new XSSFWorkbook();
        data = new Goate();
        return this;
    }

    @Override
    public SheetUtils writeToFile() {
        if (workbook != null) {
            for (String pageName : data.keys()) {
                Sheet page = workbook.getSheet(pageName);
                Goate pageData = data.get(pageName, null, Goate.class);
                if(page==null){
                    page = workbook.createSheet(pageName);
                }
                if(pageData!=null) {
                    for(String rowId:pageData.keys()) {
                        Goate rowData = pageData.get(rowId, null, Goate.class);
                        if(rowData!=null) {
                            int rowI = Integer.parseInt(rowId);
                            if (firstRowIsHeader) {
                                rowI++;
                            }
                            Row row = page.getRow(rowI);
                            if (row == null) {
                                row = page.createRow(rowI);
                            }
                            for(String colId:rowData.keys()){
                                int colI = -42;
                                if(firstRowIsHeader){
                                    colI = findColIndex(headers.get(pageName),colId);
                                } else {
                                    colI = Integer.parseInt(colId);
                                }
                                Cell cell = row.getCell(colI);
                                if(cell==null){
                                    cell = row.createCell(colI);
                                }
                                cell.setCellValue(""+rowData.get(colId));
                            }
                        }

                    }
                }
                if(firstRowIsHeader){
                    List<String> headerList = headers.get(pageName);
                    Row headerRow = page.getRow(0);
                    if(headerRow == null){
                        headerRow = page.createRow(0);
                    }
                    for(int i=0;i<headerList.size();i++){
                        Cell cell = headerRow.getCell(i);
                        if(cell == null){
                            cell = headerRow.createCell(i);
                        }
                        cell.setCellValue(""+headerList.get(i));
                    }
                }
            }
            try {
                File destFile = new File(fileName);
                if (destFile.getParentFile().mkdirs())
                    LOG.debug("directory did not exist, had to make it.");
                FileOutputStream fileOut = new FileOutputStream(fileName);
                workbook.write(fileOut);
                fileOut.close();
            } catch (IOException e) {
                LOG.error("Save Excel File", "Failed to save excel to a file: "+ fileName+"\n"+e.getMessage());
                e.printStackTrace();
            }
        }
        return this;
    }

    protected int findColIndex(List<String> headerList, String header){
        int col = 0;
        if(headerList!=null){
            for(int i=0; i<headerList.size(); i++){
                if(headerList.get(i).equals(header)){
                    col = i;
                    break;
                }
            }
        }
        return col;
    }

    @Override
    public SheetUtils close(){
        if(workbook!=null){
            try {
                workbook.close();
            }catch (IOException ioe){
                LOG.debug("Excel Close", "Problem closing workbook: " + ioe.getMessage(), ioe);
            }
        }
        if(in != null){
            try {
                in.close();
            } catch (IOException e) {
                LOG.debug("Excel", "Failed to close input stream: " + e.getMessage(), e);
            }
        }
        return this;
    }
}

