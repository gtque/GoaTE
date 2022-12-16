package com.thegoate.spreadsheets.utils.type;

import com.thegoate.annotations.IsDefault;
import com.thegoate.spreadsheets.csv.CSVParser;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;

@IsType
@IsDefault(forType = true)
public class CSVParserType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof CSVParser;
    }

    @Override
    public Class type(Object check){
        return CSVParser.class;
    }
}
