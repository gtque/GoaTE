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
import java.util.List;

/**
 * Load the data as a series, can also perform a merge or flatten the data to a single run.
 * Created by Eric Angeli on 5/5/2017.
 */
public class DataSeriesLoader extends TruncateDataLoader {

    protected boolean loadedFirst = false;
    @Override
    public List<Goate> load() {
        List<Goate> data = new ArrayList<>();
        Goate listOfDLs = parameters.filter("data");
        for (String key : listOfDLs.keys()) {
            List<Goate> mergeData = getDataToMerge(listOfDLs.get(key, null, DataLoader.class));
            generateSeries(data, mergeData);
            loadedFirst = true;
        }
        return data;
    }

    public DataSeriesLoader flatten() {
        setParameter("flatten", true);
        return this;
    }

    public DataSeriesLoader merge() {
        setParameter("flatten", false);
        return this;
    }

    public DataSeriesLoader truncate(boolean truncate) {
        setParameter("truncate", truncate);
        return this;
    }

    public DataSeriesLoader source(DataLoader dl) {
        setParameter("data##", dl);
        return this;
    }

    protected void generateSeries(List<Goate> dataList, List<Goate> dataToMerge){
        if((boolean)parameters.get("flatten", true)){
            flattenSeries(dataList, dataToMerge);
        } else {
            mergeData(dataList, dataToMerge);
        }
    }

    protected void flattenSeries(List<Goate> dataList, List<Goate> dataToMerge){
        if(dataList.size()==0){
            dataList.add(new Goate());
        }
        Goate data = dataList.get(0);
        for(Goate dtm:dataToMerge){
            seriesData(data, dtm);
        }
    }

    protected Goate seriesData(Goate data, Goate dtm){
        for(String key:dtm.keys()){
            data.put(key+"##",dtm.get(key));
        }
        return data;
    }

    protected List<Goate> getDataToMerge(DataLoader dl) {
        return dl != null ? dl.load() : null;
    }

    protected void mergeData(List<Goate> data, List<Goate> dataToMerge) {
        if (dataToMerge != null) {
            for (int i = 0; i < dataToMerge.size(); i++) {
                if (i < data.size()) {
                    seriesData(data.get(i), dataToMerge.get(i));//data.get(i).merge(dataToMerge.get(i), false);
                } else {
                    if (("" + parameters.get("truncate", false)).equalsIgnoreCase("false")||!loadedFirst) {
                        data.add(seriesData(new Goate(),dataToMerge.get(i)));
                    }
                }
            }
        }
        truncate(data, dataToMerge);
    }
}
