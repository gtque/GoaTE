package com.thegoate.spreadsheets.utils.type;

import com.thegoate.spreadsheets.csv.CSVRecord;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

@IsType
public class CSVRecordType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof CSVRecord;
    }

    @Override
    public Class type(Object check){
        return CSVRecord.class;
    }
}
