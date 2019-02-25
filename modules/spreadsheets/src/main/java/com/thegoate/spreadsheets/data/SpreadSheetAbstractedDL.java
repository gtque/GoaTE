package com.thegoate.spreadsheets.data;

import com.thegoate.Goate;
import com.thegoate.utils.fill.FillString;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;


/**
 * Reads an abstract data location of a spreadsheet
 * from a json name / value pair, populates the interpolated
 * path segments and updates fileName to the actual path.
 *
 * Created by Brian Hill on 2019-02-22
 */
public class SpreadSheetAbstractedDL extends SpreadSheetDL {

    private String fileName;

    @Override
    public SpreadSheetDL fileName(String file){
        fileName = file;
        Goate rd = new ToGoate(new Get(file).from("file::")).convert();

        if (rd.get("spreadsheet") != null)
            fileName= "" + rd.get("spreadsheet");

        if(fileName.contains("${"))
            fileName = ""+new FillString(fileName).with(parameters);

        setParameter("fileName", fileName);

        return this;
    }


    public String getFileName() {
        return fileName;
    }
}
