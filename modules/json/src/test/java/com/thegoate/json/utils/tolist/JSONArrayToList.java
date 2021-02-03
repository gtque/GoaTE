package com.thegoate.json.utils.tolist;

import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.tolist.ToList;
import org.json.JSONArray;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Angeli on 8/11/2019.
 */
public class JSONArrayToList extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void jsonArray() {
        JSONArray json = new JSONArray("[\"hello\",\"world\",\"how\",\"are\", \"you?\"]");

        List result = new ToList(json).list();
        List<String> expected = new ArrayList<>();
        expected.add("hello");
        expected.add("world");
        expected.add("how");
        expected.add("are");
        expected.add("you?");
        expect(Expectation.build()
                .actual(result)
                .isEqualTo(expected));
    }

    @Test(groups = {"unit"})
    public void simpleArray() {
        String json = "[\"hello\",\"world\",\"how\",\"are\", \"you?\"]";

        List result = new ToList(json).list();
        List<String> expected = new ArrayList<>();
        expected.add("hello");
        expected.add("world");
        expected.add("how");
        expected.add("are");
        expected.add("you?");
        expect(Expectation.build()
                .actual(result)
                .isEqualTo(expected));
    }

    @Test(groups = {"unit"})
    public void simpleArrayInt() {
        String json = "[42,84,21]";

        List result = new ToList(json).list();
        List<Integer> expected = new ArrayList<>();
        expected.add(42);
        expected.add(84);
        expected.add(21);
        expect(Expectation.build()
                .actual(result)
                .isEqualTo(expected));
    }
}
