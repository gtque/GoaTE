package com.thegoate.spreadsheets.utils;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.GoateUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

/**
 * Provides support for csv files and is the default.
 * Opens using RFC4180
 * Created by Eric Angeli on 9/14/2017.
 */
@IsDefault
@GoateSheet(fileTypes = {"csv"})
public class CSVSheet extends SheetUtils{

    @Override
    public Goate load() {
        data = new Goate();
        if(fileName!=null&&!fileName.isEmpty()){
            fileName = GoateUtils.getFilePath(fileName);
            try {
                Reader in = new FileReader(fileName);
                Iterable<CSVRecord> records =CSVFormat.RFC4180.parse(in);
                boolean headersFound = false;
                int row = 0;
                for(CSVRecord record : records){
                    Iterator iterator = record.iterator();
                    int count = 0;
                    Goate theRow = new Goate();
                    while(iterator.hasNext()) {
                        Object value = iterator.next();
                        if (firstRowIsHeader && !headersFound) {
                            headers.add(""+value);
                        }else{
                            String id = ""+count;
                            if(firstRowIsHeader){
                                id = headers.get(count);
                            }
                            theRow.put(id, value);
                        }
                        count++;
                    }
                    if(headersFound||!firstRowIsHeader) {
                        data.put(""+row, theRow);
                        row++;
                    }
                    headersFound = true;
                }
            }catch(Exception e){
                LOG.warn("problem loading csv file ("+fileName+"):"+e.getMessage(), e);
            }
        }
        return data;
    }

    @Override
    public Object get(int col, int row, Object def) {
        return get((headers.size()>col?headers.get(col):(""+col)), row, def);
    }

    @Override
    public Object get(String col, int row, Object def) {
        return data.get(""+row,new Goate(), Goate.class).get(col,def);
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
