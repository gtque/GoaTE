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
package com.thegoate.barn.staff;

import com.thegoate.Goate;
import com.thegoate.barn.data.BarnDataLoader;
import com.thegoate.json.utils.tojson.GoateToJSON;
import com.thegoate.staff.Employee;
import com.thegoate.staff.GoateJob;
import com.thegoate.utils.fill.FillString;
import com.thegoate.utils.get.GetFileAsString;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Generates a preview of the evaluated barn json.
 * Created by Eric Angeli on 5/22/2017.
 */
@GoateJob(jobs = {"barn summary"})
public class BarnSummaryReportEmployee extends Employee {
    enum SUMMARY_TYPE {
        API("end point,method","get:/","${method}:${end point}");
        String defaultId = "";
        String idPattern = "";
        String[] criticalInfo;
        SUMMARY_TYPE(String criticalList, String defaultId, String idPattern){
            criticalInfo = criticalList.split(",");
            this.defaultId = defaultId;
            this.idPattern = idPattern;
        }
        public String[] getCriticalInfo(){
            return criticalInfo;
        }
        public String getDefaultId(){
            return defaultId;
        }
        public String getIdPattern(){
            return idPattern;
        }
    }
    String barnRoot = "";
    String groups = "";
    String exclude = "";
    String type = "";
    boolean simple = false;

    public BarnSummaryReportEmployee root(String root){
        initData();
        data.put("barn root", root);
        return this;
    }

    public BarnSummaryReportEmployee groups(String groups){
        initData();
        data.put("barn groups", groups);
        return this;
    }

    public BarnSummaryReportEmployee exclude(String exclude){
        initData();
        data.put("barn exclude", exclude);
        return this;
    }

    public BarnSummaryReportEmployee jobType(String type){
        initData();
        data.put("barn type", type);
        return this;
    }

    public BarnSummaryReportEmployee simple(){
        this.simple = true;
        return this;
    }

    @Override
    public Employee init() {
        barnRoot = data.get(parameterName("barn root"),"", String.class);
        groups = data.get(parameterName("barn groups"),"", String.class);
        exclude = data.get(parameterName("barn exclude"),"", String.class);
        type = data.get(parameterName("barn type"),"", String.class).toUpperCase(Locale.ROOT);
        return this;
    }

    @Override
    protected Object doWork() {
        Goate summary = new Goate();
        BarnDataLoader bdl = new BarnDataLoader();
        bdl.testCaseDirectory(barnRoot).groups(groups).excludes(exclude);
        List<Goate> testCases = bdl.load();
        if(testCases!=null){
            String[] criticalInfo = SUMMARY_TYPE.valueOf(type).getCriticalInfo();
            String defaultId = SUMMARY_TYPE.valueOf(type).getDefaultId();
            String idPattern = SUMMARY_TYPE.valueOf(type).getIdPattern();
            for(Goate testCase : testCases){
                String identifier = null;
                Goate extraInfo = new Goate();
                int index;
                Goate tempSummary;
                for(String info:criticalInfo) {
                    if(identifier==null) {
                        identifier = ""+new FillString(idPattern).with(testCase);
                        if(identifier.equals(idPattern)){
                            identifier = defaultId;
                        }
                        tempSummary = summary.get(identifier, new Goate(), Goate.class);
                        tempSummary.put("_summary_count_",tempSummary.get("_summary_count_",0, Integer.class)+1);
                        index = tempSummary.get("_summary_count_",0, Integer.class);
                        if(criticalInfo.length>1&&!simple) {
                            extraInfo = tempSummary.get("additional info." + index, new Goate(), Goate.class);
                        }
                    } else {
                        extraInfo.put(info,testCase.get(info));
                    }
                }
            }

        }
        int total = 0;
        Goate temp = new Goate();
        for(String key:summary.keys()){
            total += summary.get(key, temp, Goate.class).get("_summary_count_",0,Integer.class);
        }
        summary.put(type+"_total_count", summary.size());
        summary.put("test total", total);
        return new GoateToJSON(summary).convert();
    }

    @Override
    public String[] detailedScrub() {
        String[] scrub = {};
        return scrub;
    }
}
