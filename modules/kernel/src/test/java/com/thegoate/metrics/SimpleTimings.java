package com.thegoate.metrics;

import com.thegoate.Goate;
import com.thegoate.expect.ExpectEvaluator;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.ExpectationThreadBuilder;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.testng.TestNGEngineAnnotatedDL;
import org.testng.annotations.Test;

import static com.thegoate.dsl.words.LoadFile.fileAsAString;
import static org.testng.Assert.*;

/**
 * Created by Eric Angeli on 2/27/2019.
 */
public class SimpleTimings extends TestNGEngineAnnotatedDL {
    BleatBox LOG = BleatFactory.getLogger(getClass());

    @Test(groups = {"unit"})
    public void smallArray() {
        Goate data = new Goate();
        String json = fileAsAString("really_small_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.*.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneTrue() {
        Goate data = new Goate();
        String json = fileAsAString("really_small_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneNestedSecondIndexTrue() {
        Goate data = new Goate();
        String json = fileAsAString("nested_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.*.+.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void expectSizeInnerNestedOffsetTrue() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json).put("path1", "a.*.[*1,1].b");
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>o::path1,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void expectSizeInnerNestedTrue() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.[*2].b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void nestedMoreThanOneTrue() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.+.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void expectSizeNestedMoreThanOneTrue() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.[*1].+.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void expectSizeNestedMoreThanOneLess() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.[*3].+.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertFalse(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void expectSizeNestedMoreThanOneMore() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        data.put("path1", "a.+.[*0,1].b");
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>o::path1,>,int::41");
        etb.expect("check json>a.0.0.b,!=,int::42");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertFalse(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void nestedNestedNested() {
        Goate data = new Goate();
        String json = fileAsAString("nested_nested_nested_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        data.put("path1", "a.+.[*0,1].*.b");
        data.put("path2", "a.[*1].[*0,1].*.b");
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>o::path1,>,int::41");
        etb.expect("check json>o::path2,>,int::41");
        etb.expect("check json>a.+.+.+.b,>,int::41");
        etb.expect("check json>a.*.+.*.b,>,int::41");
        etb.expect("check json>a.+.+.*.b,>,int::41");
        etb.expect("check json>a.*.+.+.b,>,int::41");
        etb.expect("check json>a.*.*.+.b,>,int::41");
        etb.expect("check json>a.*.+.[*2].b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneNestedFirstIndexTrue() {
        Goate data = new Goate();
        String json = fileAsAString("nested_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.*.b,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneNestedFirstIndexZeroSecondIndex() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.*.z,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneNestedSecondIndexFalse() {
        Goate data = new Goate();
        String json = fileAsAString("nested_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.*.+.z,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertFalse(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneNestedFirstIndexFalse() {
        Goate data = new Goate();
        String json = fileAsAString("nested_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.*.z,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneNestedBiggerFalse() {
        Goate data = new Goate();
        String json = fileAsAString("nested_just_abit_bigger_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.*.+.z,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertFalse(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void moreThanOneFalse() {
        Goate data = new Goate();
        String json = fileAsAString("really_small_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.+.z,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertFalse(result, ev.failed());
    }

    @Test
    public void regexTest(){
        String s = "a.+.*.[*15].[*9].[*3,88].[*22,7].[*4,6]";
        String a = s.replaceAll("\\[*\\*[0-9]*,*[0-9]*\\]*","*").replaceAll("\\+","*");
        assertEquals(a,"a.*.*.*.*.*.*.*");
    }

    @Test(groups = {"unit"})
    public void zeroOrMore() {
        Goate data = new Goate();
        String json = fileAsAString("really_small_array.json");
        data.put("check json.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.*.z,>,int::41");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        logStatuses(ev);
        assertTrue(result, ev.failed());
    }

    @Test(groups = {"unit"})
    public void timeWildcard() {
        Goate data = new Goate();
        String json = fileAsAString("really_big_array.json");
        data.put("check json.value", json);
        data.put("check paul.value", json);
        data.put("sleep", true);
        ExpectationThreadBuilder etb = new ExpectationThreadBuilder(data);
        etb.expect("check json>a.*.b,>,int::41");
        etb.expect("check json>a.*.c,==,true");
        etb.expect("check json>a.*.c,!=,false");
        etb.expect(Expectation.build().actual("a.*.b").from("check json").isNull(false));
        etb.expect("check paul>a.*.b,>,int::41");
        etb.expect("check paul>a.*.c,==,true");
        ExpectEvaluator ev = new ExpectEvaluator(etb);
        boolean result = ev.evaluate();
        assertTrue(result, ev.failed());
        LOG.debug("failed:\n" + ev.failed());
        logStatuses(ev);
    }

//    @Test
    public void printlnTimings(){
        Stopwatch timer = new Stopwatch();
        timer.start();
        timer.start("individual");
        for(int i=0; i<100000; i++){
            LOG.info("what you looking at?");
        }
        timer.stop("individual");
        timer.start("concat");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<100000; i++){
            sb.append("what you looking at?\n");
//            try{
//                Thread.sleep(50L);
//            }catch(Exception e){
//
//            }
        }
        LOG.info(sb.toString());
        timer.stop("concat");
        timer.stop();
        System.out.println(timer.getTimers().toString());
        System.out.println("individual: " + timer.lap("individual").getTime());
        System.out.println("concatted : " + timer.lap("concat").getTime());
    }
}
