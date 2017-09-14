package com.thegoate.spreadsheets.utils;

import com.thegoate.Goate;
import com.thegoate.utils.GoateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Provides support for excel files (xls and xlsx)
 * Created by Eric Angeli on 9/14/2017.
 */
@GoateSheet(fileTypes = {"xls","xlsx"})
public class ExcelSheet extends SheetUtils{
    XSSFSheet sheet = null;
    XSSFWorkbook workbook = null;

    @Override
    public Goate load() {
        data = new Goate();
        sheet = loadSheet();
        if(sheet == null){
            LOG.warn("could not load the sheet: " + sheetName);
        }else{
            Iterator<Row> rowIterator = sheet.iterator();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            boolean headersFound = false;
            int rowIndex = 0;
            while(rowIterator.hasNext()){
                Goate theRow = new Goate();
                Row row = rowIterator.next();
                if(row.getRowNum()!=rowIndex){
                    break;
                }
                Iterator<Cell> col = row.cellIterator();
                while(col.hasNext()) {
                    Cell cell = col.next();
                    if(cell!=null) {
                        CellValue cellValue = evaluator.evaluate(cell);
                        String value = cellValue.formatAsString().replace("\"","");
                        if (firstRowIsHeader && !headersFound) {
                            headers.add("" + value);
                        } else {
                            String id = "" + cell.getColumnIndex();
                            if (firstRowIsHeader) {
                                int colId = Integer.parseInt(id);
                                if(colId<headers.size()) {
                                    id = headers.get(colId);
                                }else{
                                    value = null;
                                }
                            }
                            if(value!=null) {
                                theRow.put(id, value);
                            }
                        }
                    }
                }
                if(headersFound||!firstRowIsHeader) {
                    data.put(""+(firstRowIsHeader?rowIndex-1:rowIndex), theRow);
                }
                rowIndex++;
                headersFound = true;
            }
        }
        return data;
    }

    private XSSFSheet loadSheet(){
        sheet = null;
        try{
            File file = new File(GoateUtils.getFilePath(fileName));
            if(file.exists()){
                workbook = new XSSFWorkbook(new FileInputStream(file));
                sheet = workbook.getSheet(sheetName);
            }
        }catch (IOException ioe){
            LOG.error("The file ("+fileName + ") could not be found: " + ioe.getMessage(), ioe);
        }
        return sheet;
    }

    @Override
    public SheetUtils set(int col, int row, Object value) {
        return null;
    }

    @Override
    public SheetUtils set(String col, int row, Object value) {
        return null;
    }

    @Override
    public SheetUtils writeToFile() {
        return null;
    }
}
