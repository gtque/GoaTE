package com.thegoate.spreadsheets.data;
  
import com.thegoate.Goate;
import com.thegoate.spreadsheets.data.SpreadSheetDL;
import com.thegoate.utils.fill.FillString;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;

/**
 * Created by Brian Hill on 2019-02-22
 */
public class SpreadSheetAbstractedDL extends SpreadSheetDL {
    @Override
    public SpreadSheetDL fileName(String file){
        String eutData = file;
        Goate rd = new ToGoate(new Get(file).from("file::")).convert();

        if (rd.get("spreadsheet") != null)
            eutData= "" + rd.get("spreadsheet");

        if(eutData.contains("${"))
            eutData = ""+new FillString(eutData).with(parameters);

        setParameter("fileName", eutData);

        return this;
    }
}
