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
package com.thegoate.data;

import com.thegoate.Goate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Load the data defined statically into a single run.
 * Created by Eric Angeli on 5/5/2017.
 */
public class MergeDataLoaders extends TruncateDataLoader{

    @Override
    public List<Goate> load() {
        List<Goate> data = null;
        Goate listOfDLs = parameters.filter("merge");
        for(String key:listOfDLs.keys()){
            List<Goate> mergeData = getDataToMerge(listOfDLs.get(key, null, DataLoader.class));
            if(data==null){
                data = mergeData;
            } else {
                mergeData(data, mergeData);
            }
        }
        return data;
    }

    public MergeDataLoaders truncate(boolean truncate){
        setParameter("truncate", truncate);
        return this;
    }
    public MergeDataLoaders merge(DataLoader dl){
        setParameter("merge##", dl);
        return this;
    }

    protected List<Goate> getDataToMerge(DataLoader dl){
        return dl!=null?dl.load():null;
    }

    protected void mergeData(List<Goate> data, List<Goate> dataToMerge){
        if(dataToMerge!=null) {
            for (int i = 0; i < dataToMerge.size(); i++) {
                if(i<data.size()) {
                    data.get(i).merge(dataToMerge.get(i), false);
                }else{
                    if((""+parameters.get("truncate",false)).equalsIgnoreCase("false")) {
                        data.add(dataToMerge.get(i));
                    }
                }
            }
        }
        truncate(data, dataToMerge);
    }
}
