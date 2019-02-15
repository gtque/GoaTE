package com.thegoate.data;

import com.thegoate.Goate;
import com.thegoate.spreadsheets.data.SpreadSheetDL;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by Eric Angeli on 12/6/2018.
 */
public class DLTests {

    @Test(groups = {"unit"})
    public void mergeTruncated(){
        DataLoader merge = new MergeDataLoaders()
                .truncate(true)
                .merge(new SpreadSheetDL().fileName("data/medium.csv"))
                .merge(new SpreadSheetDL().fileName("data/short.csv"))
                .merge(new SpreadSheetDL().fileName("data/long.csv"));
        List<Goate> data = merge.load();
        assertEquals(data.size(), 3);
        assertEquals(data.get(0).get("a"), "42");
        assertEquals(data.get(1).get("m"), "Billy");
        assertEquals(data.get(2).get("z"), "John");
    }

    @Test(groups = {"unit"})
    public void mergeNotTruncated(){
        DataLoader merge = new MergeDataLoaders()
                .truncate(false)
                .merge(new SpreadSheetDL().fileName("data/medium.csv"))
                .merge(new SpreadSheetDL().fileName("data/short.csv"))
                .merge(new SpreadSheetDL().fileName("data/long.csv"));
        List<Goate> data = merge.load();
        assertEquals(data.size(), 5);
        assertEquals(data.get(0).get("a"), "42");
        assertEquals(data.get(1).get("m"), "Billy");
        assertEquals(data.get(2).get("z"), "John");
        assertEquals(data.get(4).get("a"), "1681");
        assertEquals(data.get(3).get("m"), null);
        assertEquals(data.get(4).get("z"), "Stackle");
    }

    @Test(groups = {"unit"})
    public void dataSeriesFlattened(){
        DataLoader series = new DataSeriesLoader()
                .flatten()
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"))
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"))
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"));
        List<Goate> data = series.load();
        assertEquals(data.size(),1);
        assertEquals(data.get(0).filter("key.").size(),6);
    }

    @Test(groups = {"unit"})
    public void dataSeriesMerged(){
        DataLoader series = new DataSeriesLoader()
                .merge()
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"))
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"))
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"));
        List<Goate> data = series.load();
        assertEquals(data.size(),2);
        assertEquals(data.get(0).filter("key.").size(),3);
    }

    @Test(groups = {"unit"})
    public void dataSeriesMergedTruncated(){
        DataLoader series = new DataSeriesLoader()
                .merge()
                .truncate(true)
                .source(new SpreadSheetDL().fileName("data/simpleseriesLong.csv"))
                .source(new SpreadSheetDL().fileName("data/simpleseries.csv"))
                .source(new SpreadSheetDL().fileName("data/simpleseriesLong.csv"));
        List<Goate> data = series.load();
        assertEquals(data.size(),2);
        assertEquals(data.get(0).filter("key.").size(),3);
    }
}
