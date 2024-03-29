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

package com.thegoate.utils.fill.serialize;

import com.thegoate.Goate;
import com.thegoate.expect.Expectation;
import com.thegoate.expect.test.NestedObject;
import com.thegoate.expect.test.SimpleObject;
import com.thegoate.json.utils.fill.serialize.to.JsonString;
import com.thegoate.json.utils.tojson.GoateToJSON;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.serialize.model.ModelBuilder;
import com.thegoate.utils.fill.serialize.nullable.primitive.NullableDouble;
import com.thegoate.utils.fill.serialize.pojos.*;
import com.thegoate.utils.fill.serialize.pojos.flat.Child1;
import com.thegoate.utils.fill.serialize.pojos.flat.Parent;
import com.thegoate.utils.fill.serialize.pojos.list.BigList;
import com.thegoate.utils.fill.serialize.pojos.list.SimpleContent;
import com.thegoate.utils.fill.serialize.pojos.nullable.Wrapper;
import com.thegoate.utils.fill.serialize.pojos.nullable.WrapperDouble;
import com.thegoate.utils.fill.serialize.pojos.nullable.WrapperPrimitiveDouble;
import com.thegoate.utils.fill.serialize.primitives.CastBoolean;
import com.thegoate.utils.get.Get;
import com.thegoate.utils.togoate.ToGoate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thegoate.dsl.words.LoadFile.fileAsAString;
import static org.testng.Assert.*;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
public class DeSerializerTests extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void testCastOnSerialization() {
        CastOnSerliazing cos = new CastOnSerliazing();
        String s = new Serializer<CastOnSerliazing, Class<Cheese>, String>(cos, Cheese.class).to(new JsonString());
        expect(Expectation.build()
                .actual("i")
                .from(s)
                .isEqualTo("1"));
    }

    @Test(groups = {"unit"})
    public void testListRootPojo() {
        String ja = "[\"premal\",\"jonathan\",\"anthony\"]";
        Goate g = new ToGoate(ja).convert();
        SimpleList data = new DeSerializer().data(g).from(RootSource.class).build(SimpleList.class);
        data.toString();
        List<String> expected = new ArrayList<>();
        expected.add("premal");
        expected.add("jonathan");
        expected.add("anthony");

        assertEquals(data.getTheList(), expected);
    }

    @Test(groups = {"unit"})
    public void testNestedObjectListRootPojo() {
        String ja = "[{\"values\":[{\"value\":1},{\"value\":2}]},{\"values\":[{\"value\":3},{\"value\":4}]}]";
        Goate g = new ToGoate(ja).convert();
        ComplexList data = new DeSerializer().data(g).from(RootSource.class).build(ComplexList.class);
        data.toString();
        List<NestedList> expected = new ArrayList<>();
        expected.add(new NestedList().addValue(new SimpleInt().setValue(1)).addValue(new SimpleInt().setValue(2)));
        expected.add(new NestedList().addValue(new SimpleInt().setValue(3)).addValue(new SimpleInt().setValue(4)));

        assertEquals(data.getTheList().get(0).getTheList().get(0).getValue(), 1);
        assertEquals(data.getTheList().get(0).getTheList().get(1).getValue(), 2);
        assertEquals(data.getTheList().get(1).getTheList().get(0).getValue(), 3);
        assertEquals(data.getTheList().get(1).getTheList().get(1).getValue(), 4);
        //		assertEquals(data.getTheList(), expected);
    }

    @Test(groups = {"unit"})
    public void testListPojo() {
        String ja = "[\"premal\",\"jonathan\",\"anthony\"]";
        JSONObject jo = new JSONObject();
        jo.put("theList", new JSONArray(ja));
        Goate g = new ToGoate(jo).convert();
        SimpleList data = new DeSerializer().data(g).build(SimpleList.class);
        data.toString();
        List<String> expected = new ArrayList<>();
        expected.add("premal");
        expected.add("jonathan");
        expected.add("anthony");

        assertEquals(data.getTheList(), expected);
    }

    @Test(groups = {"unit", "deserialize"})
    public void simpleDeserializeStringObjectTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("expanded name", 42);
        data.put("fieldName", "Hello, world!");
        data.put("big decimal", "3.14159");
        data.put("double D", "42.42");
        data.put("long l", 42L);
        SimplePojo pojo = new DeSerializer().data(data).from(SimpleSource.class).build(SimplePojo.class);
        assertTrue(new CastBoolean("1").cast(Boolean.class));
        assertFalse(new CastBoolean("0").cast(Boolean.class));
        assertEquals(pojo.getFieldName(), "Hello, world!");
        assertEquals(pojo.getSomeInt(), 42);
        assertEquals(pojo.getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) pojo.getD(), Double.parseDouble("42.42"));
        assertEquals(pojo.getC(), 'c');
        assertEquals(pojo.getL(), 42L);
        assertEquals((byte) pojo.getaByte(), Byte.parseByte("1"));
        assertEquals(pojo.getF(), 4F);
        assertTrue(pojo.isBool());
        pojo.reportHealth("test", "hello");
        Goate d = new Serializer<>(pojo, SimpleSource.class).toGoate();
        String json = new GoateToJSON(d).convert();
        System.out.println("serialized: " + json);
    }

    @Test(groups = {"unit", "deserialize"})
    public void simpleDeserializeGoateTest() {
        Goate data = new Goate();
        data.put("expanded name", 42);
        data.put("fieldName", "Hello, world!");
        data.put("big decimal", "3.14159");
        data.put("double D", "42.42");
        data.put("long l", 42L);
        data.put("ignoreMe", "these are not the droids you are looking for.");
        SimplePojo pojo = new DeSerializer().data(data).from(SimpleSource.class).build(SimplePojo.class);
        SimplePojo pojo2 = new DeSerializer().data(data.drop("ignoreMe")).from(SimpleSource.class).build(SimplePojo.class);
        assertEquals(pojo.getFieldName(), "Hello, world!");
        assertEquals(pojo.getSomeInt(), 42);
        assertEquals(pojo.getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) pojo.getD(), Double.parseDouble("42.42"));
        assertEquals(pojo.getC(), 'c');
        assertEquals(pojo.getL(), 42L);
        assertEquals((byte) pojo.getaByte(), Byte.parseByte("1"));
        assertEquals(pojo.getF(), 4F);
        assertTrue(pojo.isBool());
        expect(Expectation.build()
                .actual(pojo)
                .isEqualTo(pojo2));
    }

    @Test(groups = {"unit"})
    public void serializeAllNullFields() {
        SimpleNested pojo = new SimpleNested();
        pojo.setSomething(null);
        pojo.setInnerField(null);
        String expected = "{\"droid\":42}";

        String actual = "" + new Serializer<>(pojo, DefaultSource.class).to(new JsonString());
        expect(Expectation.build().actual(actual).isEqualTo(expected));
    }

    @Test(groups = {"unit", "deserialize"})
    public void complexDeserializeGoateTest() {
        Goate data = new Goate();
        data.put("date", "11-24-2009");
        data.put("nested.expanded name", 42);
        data.put("nested.fieldName", "Hello, world!");
        data.put("nested.big decimal", "3.14159");
        data.put("nested.double D", "42.42");
        data.put("nested.long l", 42L);
        ComplexPojo pojo = data.get("complex pojo", "goatePojo::complex pojo,simple expected", ComplexPojo.class);//new DeSerializer().data(data).from(SimpleSource.class).build(ComplexPojo.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(pojo.getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(pojo.getNested().getFieldName(), "Hello, world!");
        assertEquals(pojo.getNested().getSomeInt(), 42);
        assertEquals(pojo.getNested().getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) pojo.getNested().getD(), Double.parseDouble("42.42"));
        assertEquals(pojo.getNested().getC(), 'c');
        assertEquals(pojo.getNested().getL(), 42L);
        assertEquals((byte) pojo.getNested().getaByte(), Byte.parseByte("1"));
        assertEquals(pojo.getNested().getF(), 4F);
        assertTrue(pojo.getNested().isBool());
    }

    //	jackson has an interesting behavior that occurs when the field name starts with "is" and so does the getter/setter.
    //	there are ways to work around this that do not require changes to the goate framework, but
    //	if someone finds those workarounds awkward and wants Goate to handle it, this will need to be implemented to test it.
    //	String boolean_json = "{\"isField\":true}";
    //	@Test(groups = {"unit"})
    //	public void testIsInTheName(){
    //		Object result = new Serializer<>(new Bojo(true), DefaultSource.class).to(new JsonString());
    //		assertEquals(new Get("isField").from(result), true);
    //	}

    @Test(groups = {"unit", "deserialize"})
    public void nestedDeserializeGoateTest() {
        Goate data = new Goate();
        int[] nums = {42, 8};
        int[] expectNums = {42, 8};
        int[] expectNums2 = {21, 4, 2009};
        data.put("numbers", nums);
        data.put("numbers2", "i am really an array of ints");
        data.put("numbers2.0", 21);
        data.put("numbers2.1", 4);
        data.put("numbers2.2", 2009);
        data.put("list.0", "nuggets");
        data.put("list.1.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("list.1.fieldName", "Paula");
        data.put("cp.0.date", "11-24-2009");
        data.put("cp.0.nested.expanded name", 42);
        data.put("cp.0.nested.fieldName", "Hello, world!");
        data.put("cp.0.nested.big decimal", "3.14159");
        data.put("cp.0.nested.double D", "42.42");
        data.put("cp.0.nested.long l", 42L);
        data.put("cp.1.nested map.0.key", "Peter");
        data.put("cp.1.nested map.0.value.class", "java.lang.String");
        data.put("cp.1.nested map.0.value", "Parker");
        data.put("cp.1.nested map.1.key", "Tony");
        data.put("cp.1.nested map.1.value", "Stark");
        data.put("cp.1.nested map.1.value.class", "java.lang.String");
        data.put("map.0.key", "bryan");
        data.put("map.0.value", true);
        data.put("map.1.key", "tarun");
        data.put("map.1.value", false);
        data.put("map2.0.key", "bryan");
        data.put("map2.0.value.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("map2.0.value.long l", "long::84");
        data.put("map2.1.key", "tarun");
        data.put("map2.1.value", true);
        data.put("map2.1.value.class", "java.lang.Boolean");

        NestedPojos pojo = new DeSerializer()
                .data(data)
                .from(SimpleSource.class)
                .build(NestedPojos.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(pojo.getNumbers(), expectNums);
        assertEquals(pojo.getNumbers2(), expectNums2);
        assertEquals(pojo.getList().get(0), "nuggets");
        assertEquals(((SimplePojo) pojo.getList().get(1)).getFieldName(), "Paula");
        assertTrue(Boolean.parseBoolean("" + pojo.getMap().get("bryan")));
        assertFalse(Boolean.parseBoolean("" + pojo.getMap().get("tarun")));
        assertEquals(((SimplePojo) (pojo.getMap2().get("bryan"))).getL(), 84L);
        assertTrue(Boolean.parseBoolean("" + pojo.getMap2().get("tarun")));
        assertEquals(pojo.getCp()[0].getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(pojo.getCp()[0].getNested().getFieldName(), "Hello, world!");
        assertEquals(pojo.getCp()[0].getNested().getSomeInt(), 42);
        assertEquals(pojo.getCp()[0].getNested().getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) pojo.getCp()[0].getNested().getD(), Double.parseDouble("42.42"));
        assertEquals(pojo.getCp()[0].getNested().getC(), 'c');
        assertEquals(pojo.getCp()[0].getNested().getL(), 42L);
        assertEquals((byte) pojo.getCp()[0].getNested().getaByte(), Byte.parseByte("1"));
        assertEquals(pojo.getCp()[0].getNested().getF(), 4F);
        assertEquals(pojo.getCp()[1].getMap().get("Peter"), "Parker");
        assertEquals(pojo.getCp()[1].getMap().get("Tony"), "Stark");
        assertTrue(pojo.getCp()[0].getNested().isBool());
//        Goate d = new Serializer<>(pojo, SimpleSource.class).veryDetailed().toGoate();

//        Map<String, Object> data2 = new Serializer<>(pojo, SimpleSource.class).veryDetailed().toMap(HashMap.class);

//        assertTrue(d.size() >= 50);

        NestedPojos pojo2 = new DeSerializer()
                .data(data)
                .from(SimpleSource.class)
                .build(NestedPojos.class);
        String jstring = "" + new Serializer<>(pojo2, Cheese.class).to(new JsonString());
        System.out.println(jstring);
        assertEquals(pojo.getNumbers(), pojo2.getNumbers());
        assertEquals(pojo.getNumbers2(), pojo2.getNumbers2());
        assertEquals(pojo.getList().get(0), pojo2.getList().get(0));
        assertEquals(((SimplePojo) pojo2.getList().get(1)).getFieldName(), "Paula");
        assertTrue(Boolean.parseBoolean("" + pojo2.getMap().get("bryan")));
        assertFalse(Boolean.parseBoolean("" + pojo2.getMap().get("tarun")));
        assertEquals(((SimplePojo) (pojo2.getMap2().get("bryan"))).getL(), 84L);
        assertTrue(Boolean.parseBoolean("" + pojo2.getMap2().get("tarun")));
        assertEquals(pojo2.getCp()[0].getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(pojo2.getCp()[0].getNested().getFieldName(), "Hello, world!");
        assertEquals(pojo2.getCp()[0].getNested().getSomeInt(), 42);
        assertEquals(pojo2.getCp()[0].getNested().getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) pojo2.getCp()[0].getNested().getD(), Double.parseDouble("42.42"));
        assertEquals(pojo2.getCp()[0].getNested().getC(), 'c');
        assertEquals(pojo2.getCp()[0].getNested().getL(), 42L);
        assertEquals((byte) pojo2.getCp()[0].getNested().getaByte(), Byte.parseByte("1"));
        assertEquals(pojo2.getCp()[0].getNested().getF(), 4F);
        assertEquals(pojo2.getCp()[1].getMap().get("Peter"), "Parker");
        assertEquals(pojo2.getCp()[1].getMap().get("Tony"), "Stark");
        assertTrue(pojo2.getCp()[0].getNested().isBool());
    }

    @Test(groups = {"unit", "deserialize"})
    public void nestedListDeserializeGoateTest() {
        Goate data = new Goate();
        int[] nums = {42, 8};
        int[] expectNums = {42, 8};
        int[] expectNums2 = {21, 4, 2009};
        data.put("nestedList.0.numbers", nums);
        data.put("nestedList.0.numbers2", "i am really an array of ints");
        data.put("nestedList.0.numbers2.0", 21);
        data.put("nestedList.0.numbers2.1", 4);
        data.put("nestedList.0.numbers2.2", 2009);
        data.put("nestedList.0.list.0", "nuggets");
        data.put("nestedList.0.list.1.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("nestedList.0.list.1.fieldName", "Paula");
        data.put("nestedList.0.cp.0.date", "11-24-2009");
        data.put("nestedList.0.cp.0.nested.expanded name", 42);
        data.put("nestedList.0.cp.0.nested.fieldName", "Hello, world!");
        data.put("nestedList.0.cp.0.nested.big decimal", "3.14159");
        data.put("nestedList.0.cp.0.nested.double D", "42.42");
        data.put("nestedList.0.cp.0.nested.long l", 42L);
        data.put("nestedList.0.cp.1.nested map.0.key", "Peter");
        data.put("nestedList.0.cp.1.nested map.0.value.class", "java.lang.String");
        data.put("nestedList.0.cp.1.nested map.0.value", "Parker");
        data.put("nestedList.0.cp.1.nested map.1.key", "Tony");
        data.put("nestedList.0.cp.1.nested map.1.value", "Stark");
        data.put("nestedList.0.cp.1.nested map.1.value.class", "java.lang.String");
        data.put("nestedList.0.map.0.key", "bryan");
        data.put("nestedList.0.map.0.value", true);
        data.put("nestedList.0.map.1.key", "tarun");
        data.put("nestedList.0.map.1.value", false);
        data.put("nestedList.0.map2.0.key", "bryan");
        data.put("nestedList.0.map2.0.value.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("nestedList.0.map2.0.value.long l", "long::84");
        data.put("nestedList.0.map2.1.key", "tarun");
        data.put("nestedList.0.map2.1.value", true);
        data.put("nestedList.0.map2.1.value.class", "java.lang.Boolean");
        //add a second entry same as the first...
        data.put("nestedList.1.numbers", nums);
        data.put("nestedList.1.numbers2", "i am really an array of ints");
        data.put("nestedList.1.numbers2.0", 21);
        data.put("nestedList.1.numbers2.1", 4);
        data.put("nestedList.1.numbers2.2", 2009);
        data.put("nestedList.1.list.0", "nuggets");
        data.put("nestedList.1.list.1.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("nestedList.1.list.1.fieldName", "Paula");
        data.put("nestedList.1.cp.0.date", "11-24-2009");
        data.put("nestedList.1.cp.0.nested.expanded name", 42);
        data.put("nestedList.1.cp.0.nested.fieldName", "Hello, world!");
        data.put("nestedList.1.cp.0.nested.big decimal", "3.14159");
        data.put("nestedList.1.cp.0.nested.double D", "42.42");
        data.put("nestedList.1.cp.0.nested.long l", 42L);
        data.put("nestedList.1.cp.1.nested map.0.key", "Peter");
        data.put("nestedList.1.cp.1.nested map.0.value.class", "java.lang.String");
        data.put("nestedList.1.cp.1.nested map.0.value", "Parker");
        data.put("nestedList.1.cp.1.nested map.1.key", "Tony");
        data.put("nestedList.1.cp.1.nested map.1.value", "Stark");
        data.put("nestedList.1.cp.1.nested map.1.value.class", "java.lang.String");
        data.put("nestedList.1.map.0.key", "bryan");
        data.put("nestedList.1.map.0.value", true);
        data.put("nestedList.1.map.1.key", "tarun");
        data.put("nestedList.1.map.1.value", false);
        data.put("nestedList.1.map2.0.key", "bryan");
        data.put("nestedList.1.map2.0.value.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("nestedList.1.map2.0.value.long l", "long::84");
        data.put("nestedList.1.map2.1.key", "tarun");
        data.put("nestedList.1.map2.1.value", true);
        data.put("nestedList.1.map2.1.value.class", "java.lang.Boolean");

        NestedPojosList pojo = new DeSerializer()
                .T(NestedPojos.class)
                .data(data)
                .from(SimpleSource.class)
                .build(NestedPojosList.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getNumbers(), expectNums);
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getNumbers2(), expectNums2);
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getList().get(0), "nuggets");
        assertEquals(((SimplePojo) ((NestedPojos) pojo.getNestedList().get(0)).getList().get(1)).getFieldName(), "Paula");
        assertTrue(Boolean.parseBoolean("" + ((NestedPojos) pojo.getNestedList().get(0)).getMap().get("bryan")));
        assertFalse(Boolean.parseBoolean("" + ((NestedPojos) pojo.getNestedList().get(0)).getMap().get("tarun")));
        assertEquals(((SimplePojo) (((NestedPojos) pojo.getNestedList().get(0)).getMap2().get("bryan"))).getL(), 84L);
        assertTrue(Boolean.parseBoolean("" + ((NestedPojos) pojo.getNestedList().get(0)).getMap2().get("tarun")));
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getFieldName(), "Hello, world!");
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getSomeInt(), 42);
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) ((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getD(), Double.parseDouble("42.42"));
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getC(), 'c');
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getL(), 42L);
        assertEquals((byte) ((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getaByte(), Byte.parseByte("1"));
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().getF(), 4F);
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[1].getMap().get("Peter"), "Parker");
        assertEquals(((NestedPojos) pojo.getNestedList().get(0)).getCp()[1].getMap().get("Tony"), "Stark");
        assertTrue(((NestedPojos) pojo.getNestedList().get(0)).getCp()[0].getNested().isBool());
//        Goate d = new Serializer<>(pojo, SimpleSource.class).veryDetailed().toGoate();

//        Map<String, Object> data2 = new Serializer<>(pojo, SimpleSource.class).veryDetailed().toMap(HashMap.class);

//        assertTrue(d.size() >= 50);

        NestedPojosList pojo2 = new DeSerializer()
                .T(NestedPojos.class)
                .data(data)
                .from(SimpleSource.class)
                .build(NestedPojosList.class);
        String jstring = "" + new Serializer<>(pojo2, Cheese.class).to(new JsonString());
        System.out.println(jstring);
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getNumbers(), ((NestedPojos) pojo2.getNestedList().get(1)).getNumbers());
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getNumbers2(), ((NestedPojos) pojo2.getNestedList().get(1)).getNumbers2());
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getList().get(0), ((NestedPojos) pojo2.getNestedList().get(1)).getList().get(0));
        assertEquals(((SimplePojo) ((NestedPojos) pojo.getNestedList().get(1)).getList().get(1)).getFieldName(), "Paula");
        assertTrue(Boolean.parseBoolean("" + ((NestedPojos) pojo.getNestedList().get(1)).getMap().get("bryan")));
        assertFalse(Boolean.parseBoolean("" + ((NestedPojos) pojo.getNestedList().get(1)).getMap().get("tarun")));
        assertEquals(((SimplePojo) (((NestedPojos) pojo.getNestedList().get(1)).getMap2().get("bryan"))).getL(), 84L);
        assertTrue(Boolean.parseBoolean("" + ((NestedPojos) pojo.getNestedList().get(1)).getMap2().get("tarun")));
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getFieldName(), "Hello, world!");
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getSomeInt(), 42);
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getBigD(), new BigDecimal("3.14159"));
        assertEquals((double) ((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getD(), Double.parseDouble("42.42"));
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getC(), 'c');
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getL(), 42L);
        assertEquals((byte) ((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getaByte(), Byte.parseByte("1"));
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().getF(), 4F);
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[1].getMap().get("Peter"), "Parker");
        assertEquals(((NestedPojos) pojo.getNestedList().get(1)).getCp()[1].getMap().get("Tony"), "Stark");
        assertTrue(((NestedPojos) pojo.getNestedList().get(1)).getCp()[0].getNested().isBool());
        expect(Expectation.build()
                .actual(pojo)
                .isEqualTo(pojo2));
    }

    @Test(groups = {"unit"})
    public void serializeMapFromJson() {
        String json = "{\n" +
                " \"theMap\":{\n" +
                "  \"entry1\":{\"fieldI\":42,\"fieldB\":false,\"fieldO\":{\"fieldS\":\"Hello\", \"fieldS2\":\"World\", \"fieldS3\":\"!\"}},\n" +
                "  \"entry2\":{\"fieldI\":84,\"fieldB\":false,\"fieldO\":{\"fieldS\":\"Mr.\"}},\n" +
                "  \"entry3\":{\"fieldI\":168,\"fieldB\":true,\"fieldO\":{\"fieldS\":\"Pickles\"}}\n" +
                " },\n" +
                " \"theStringMap\":{\n" +
                "  \"name\": \"Mr. Chompers\",\n" +
                "  \"job\": \"Being Cute\",\n" +
                "  \"favorite animal\": \"Kittens\"\n" +
                " }" +
                "}";
        Goate data = new ToGoate(json).convert();
        MappedPojo pojo = new DeSerializer().data(data).from(SimpleSource.class).build(MappedPojo.class);
        assertEquals(pojo.getSomeMapOfStrings().get("name"), "Mr. Chompers");
        assertEquals(pojo.getSomeMap().get("entry1").getFieldO().get("fieldS"), "Hello");
        assertEquals(pojo.getSomeMap().get("entry1").getFieldO().get("fieldS2"), "World");
        assertEquals(pojo.getSomeMap().get("entry1").getFieldO().get("fieldS3"), "!");
        assertEquals(pojo.getSomeMap().get("entry1").isFieldB(), false);
        assertEquals(pojo.getSomeMap().get("entry1").getFieldI(), 42);
        assertEquals(pojo.getSomeMap().get("entry2").getFieldO().get("fieldS"), "Mr.");
        assertEquals(pojo.getSomeMap().get("entry2").isFieldB(), false);
        assertEquals(pojo.getSomeMap().get("entry2").getFieldI(), 84);
        assertEquals(pojo.getSomeMap().get("entry3").getFieldO().get("fieldS"), "Pickles");
        assertEquals(pojo.getSomeMap().get("entry3").isFieldB(), true);
        assertEquals(pojo.getSomeMap().get("entry3").getFieldI(), 168);
        assertEquals(pojo.getSomeMapOfStrings().get("name"), "Mr. Chompers");
        assertEquals(pojo.getSomeMapOfStrings().get("favorite animal"), "Kittens");
        assertEquals(pojo.getSomeMapOfStrings().get("job"), "Being Cute");
        String jstring = "" + new Serializer<>(pojo, SimpleSource.class).to(new JsonString());
        expect(Expectation.build()
                .actual(jstring)
                .isEqualTo(json));
    }

    @Test(groups = {"unit"})
    public void nestedSource() {
        SimpleNested pojo2 = new SimpleNested();
        SimpleInt si = new SimpleInt();
        si.setValue(42);
        pojo2.setInnerField(si);
        String jstring = "" + new Serializer<>(pojo2, Cheese.class).to(new JsonString());
        System.out.println(jstring);
        assertEquals(new Get("chuck.bartowski").from(jstring), 42);
    }

    @Test(groups = {"unit"})
    public void simpleSerializerTest() {
        SerializeMe pojo = new SerializeMe().setField("hello").setExcluded("fart").setNestedExcluded(new SerializeLevel1().setFirstLevel("jelly beans"));
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        String json = new Serializer<SerializeMe, Class, String>(pojo, DefaultSource.class).asSourced(true).to(new JsonString());
        expect(Expectation.build().actual(json).isEqualTo("{\"field\":\"hello\"}"));
    }

    @Test(groups = {"unit"})
    public void simpleSerializerTestGoateToJson() {
        SerializeMe pojo = new SerializeMe().setField("hello").setExcluded("fart").setNestedExcluded(new SerializeLevel1().setFirstLevel("jelly beans"));
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        Goate json = new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).asSourced(true).toGoate();
        expect(Expectation.build().actual(new GoateToJSON(json).convert()).isEqualTo("{\"field\":\"hello\"}"));
    }

    @Test(groups = {"unit"})
    public void simpleSerializerTestGoateToJsonGeneric() {
        SerializeMe pojo = new SerializeMe().setField("hello").setExcluded("fart").setNestedExcluded(new SerializeLevel1().setFirstLevel("jelly beans"));
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        String json = "" + new Serializer<SerializeMe, Class, JSONObject>(pojo, DefaultSource.class).asSourced(true).to(JSONObject.class);
        expect(Expectation.build().actual(json).isEqualTo("{\"field\":\"hello\"}"));
    }

    @Test(groups = {"unit"})
    public void simpleSerializerTestNullField() {
        SerializeMe pojo = new SerializeMe();
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        String json = "" + new Serializer<SerializeMe, Class, JSONObject>(pojo, DefaultSource.class).includeNulls().asSourced(true).to(JSONObject.class);
        expect(Expectation.build().actual(json).isEqualTo("{\"field\":null, \"nested\":null, \"list\":null, \"map\":null}"));
    }

    @Test(groups = {"unit"})
    public void nestedSerializerTest() {
        SerializeNested pojo = new SerializeNested();
        List<Cereals> cereals = new ArrayList<>();
        Cereals cereals1 = new Cereals().setId("fruit loops").setBox(new Box().setSize(14));
        Cereals cereals2 = new Cereals().setId("lucky charms").setBox(new Box().setSize(14));
        Cereals cereals3 = new Cereals().setId("fruit loops").setBox(new Box().setSize(10));
        cereals.add(cereals1);
        cereals.add(cereals2);
        cereals.add(cereals3);
        pojo.setIds(cereals);
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        String json = new Serializer<SerializeNested, DefaultSource, String>(pojo).to(new JsonString());
        expect(Expectation.build().actual(json).isEqualTo(fileAsAString("nested_cereal.json")));
    }

    @Test(groups = {"unit"})
    public void nestedSerializerTestRootArray() {
        SerializeNested pojo = new SerializeNested();
        List<Cereals> cereals = new ArrayList<>();
        Cereals cereals1 = new Cereals().setId("fruit loops").setBox(new Box().setSize(14));
        Cereals cereals2 = new Cereals().setId("lucky charms").setBox(new Box().setSize(14));
        Cereals cereals3 = new Cereals().setId("fruit loops").setBox(new Box().setSize(10));
        cereals.add(cereals1);
        cereals.add(cereals2);
        cereals.add(cereals3);
        pojo.setIds(cereals);
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        String json = "" + new Serializer<>(pojo.getIds(), RootSource.class).to(new JsonString());
        expect(Expectation.build().actual(json).isEqualTo(fileAsAString("nested_cereal_root_array.json")));
    }

    @Test(groups = {"unit", "serialize"})
    public void simpleSerializerTestDifferentSource() {
        List<SerializeLevel1> list = new ArrayList<>();
        list.add(new SerializeLevel1().setFirstLevel("eric"));
        list.add(new SerializeLevel1().setFirstLevel("tracy"));
        list.add(new SerializeLevel1().setFirstLevel("eric"));
        Map<String, SerializeLevel1> map = new HashMap<>();
        map.put("a", new SerializeLevel1().setFirstLevel("richard"));
        map.put("b", new SerializeLevel1().setFirstLevel("karen"));
        SerializeMe pojo = new SerializeMe()
                .setField("hello")
                .setNested(new SerializeLevel1().setFirstLevel("tracy"))
                .setList(list)
                .setMap(map);
//        String json = new GoateToJSON(new Serializer<SerializeMe, Class, Goate>(pojo, DefaultSource.class).toGoate()).convert();//.to(new JsonString());
        String json = new Serializer<SerializeMe, Class<SimpleSource>, String>(pojo, SimpleSource.class).to(new JsonString());
        expect(Expectation.build().actual(json).isEqualTo("{\"athan\":\"hello\", \"parent\":{\"guardian\":\"tracy\"}, \"rents\":[{\"guardian\":\"eric\"},{\"guardian\":\"eric\"},{\"guardian\":\"tracy\"}], \"map\":{\"a\":{\"guardian\":\"richard\"}, \"b\":{\"guardian\":\"karen\"}}}"));
    }

    @Test(groups = {"unit"})
    public void nestedSourcesSameDefaultNameButDifferentSourceName() {
        NotSoSimpleNested pojo2 = new NotSoSimpleNested();
        SimpleInt si = new SimpleInt();
        si.setValue(42);
        pojo2.setInnerField(si);
        SimpleDouble sd = new SimpleDouble();
        sd.setValue(3.14159D);
        pojo2.setInnerDoubleField(sd);
        String jstring = "" + new Serializer<>(pojo2, Cheese.class).to(new JsonString());
        String expected = "{\n" +
                "   \"charles\": {\"yeager\": 3.14159},\n" +
                "   \"chuck\": {\"bartowski\": 42}\n" +
                "}";
        System.out.println(jstring);
        expect(Expectation.build()
                .actual(jstring)
                .isEqualTo(expected));
        LOG.debug("get from json: " + new Get("charles.yeager").from(jstring));
        expect(Expectation.build().actual("chuck.bartowski").from(jstring).isEqualTo(42));
        expect(Expectation.build().actual("charles.yeager").from(jstring).isEqualTo(3.14159D));
        //        assertEquals(new Get("charles.yeager").from(jstring), 3.14159D);
        LOG.debug("place holder");
    }

    @Test(groups = {"unit"})
    public void skipSerializingNested() {
        String jstring = "{\"ld\":\"04-22-2019\", \"innerField\":{\"value\":42}}";
        SimpleNested pojo2 = new DeSerializer().data(new ToGoate(jstring).convert()).build(SimpleNested.class);
        Goate data = new Serializer<>(pojo2, Cheese.class).skipSerializingObjects().skipSerializingGoatePojos().toGoate();
        assertTrue(data.get("ld") instanceof LocalDate);
    }

    @Test(groups = {"unit"})
    public void flatNest() {
        Goate g = new Goate().put("row.0.inner_name", "Tarun").put("row.0.inner_job", "Bringer of Pain");
        FlatNest next = new DeSerializer().data(g.filter("row.0.").scrubKeys("row.0.")).from(FlatNest.class).build(FlatNest.class);

        expect(Expectation.build()
                .actual(next)
                .isNull(false));
        expect(Expectation.build()
                .actual(next.getInner().getName())
                .isEqualTo("Tarun"));
    }

    @Test(groups = {"unit"})
    public void nullableWrapper() {
        Goate g = new Goate().put("d", "42.314159");

        Wrapper wrapper = new DeSerializer().data(g).build(Wrapper.class);
        Wrapper ew = new Wrapper();
        ew.setD(NullableDouble.build("42.314159"));

        expect(Expectation.build().actual(wrapper).isEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void nullableWrapperNullNotEqual() {
        Goate g = new Goate().put("d", "null::");

        Wrapper wrapper = new DeSerializer().data(g).build(Wrapper.class);
        Wrapper ew = new Wrapper();
        ew.setD(NullableDouble.build("42.314159"));

        expect(Expectation.build().actual(wrapper).isNotEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void nullableWrapperNullEqual() {
        Goate g = new Goate().put("d", "null::");

        Wrapper wrapper = new DeSerializer().data(g).build(Wrapper.class);
        Wrapper ew = new Wrapper();

        expect(Expectation.build().actual(wrapper).isEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void doubleWrapper() {
        Goate g = new Goate().put("d", "42.314159");

        WrapperDouble wrapper = new DeSerializer().data(g).build(WrapperDouble.class);
        WrapperDouble ew = new WrapperDouble();
        ew.setD(42.314159D);

        expect(Expectation.build().actual(wrapper).isEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void doubleWrapperJSONNull() {
        Goate g = new ToGoate("{\"d\":null}").convert();

        WrapperDouble wrapper = new DeSerializer().data(g).build(WrapperDouble.class);
        WrapperDouble ew = new WrapperDouble();
        ew.setD(null);

        expect(Expectation.build().actual(wrapper).isEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void doubleWrapperNullNotEqual() {
        Goate g = new Goate().put("d", "null::");

        WrapperDouble wrapper = new DeSerializer().data(g).build(WrapperDouble.class);
        WrapperDouble ew = new WrapperDouble();
        ew.setD(42.314159);

        expect(Expectation.build().actual(wrapper).isNotEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void doubleWrapperNullEqual() {
        Goate g = new Goate().put("d", "null::");

        WrapperDouble wrapper = new DeSerializer().data(g).build(WrapperDouble.class);
        WrapperDouble ew = new WrapperDouble();
        ew.setD(null);
        expect(Expectation.build().actual(wrapper).isEqualTo(ew));
    }

    @Test(groups = {"unit"})
    public void primitiveDoubleWrapper() {
        Goate g = new Goate().put("d", "42.314159");

        WrapperPrimitiveDouble wrapper = new DeSerializer().data(g).build(WrapperPrimitiveDouble.class);
        WrapperPrimitiveDouble ew = new WrapperPrimitiveDouble();
        ew.setD(42.314159D);

        expect(Expectation.build().actual(wrapper).isEqualTo(ew));
    }

    @Test(groups = {"poc"})
    public void jaRootWrappedInJsonObject() {
        JSONArray jad = new JSONArray("[{\"a\":\"hello\"},{\"a\":\"world\"}]");
        Goate ja = new ToGoate(new JSONObject().put("content", jad)).convert();
        ContentJA<Inside> root = new DeSerializer().data(ja).T(Inside.class).build(ContentJA.class);
        List<Inside> in = new ArrayList<>();
        in.add(new Inside().setA("hello"));
        in.add(new Inside().setA("world"));
        ContentJA<Inside> ea = new ContentJA<>();
        ea.setContent(in);
        expect(Expectation.build()
                .actual(root)
                .isEqualTo(ea));
    }

    @Test(groups = {"poc"})
    public void jaRoot() {
        JSONArray jad = new JSONArray("[{\"a\":\"hello\"},{\"a\":\"world\"}]");
        Goate ja = new ToGoate(jad).convert();
        ContentJA<Inside> root = new DeSerializer().data(ja).T(Inside.class).from(Inside.class).build(ContentJA.class);
        List<Inside> in = new ArrayList<>();
        in.add(new Inside().setA("hello"));
        in.add(new Inside().setA("world"));
        ContentJA<Inside> ea = new ContentJA<>();
        ea.setContent(in);
        expect(Expectation.build()
                .actual(root)
                .isEqualTo(ea));
    }

    @Test(groups = {"unit"})
    public void fillPojo() {
        Goate g = new Goate().put("pojo", "fill::o::actual")
                .put("actual", new GenericTypes<String, Boolean, Integer>())
                .put("a", "Hello")
                .put("b", true)
                .put("c", 42);
        GenericTypes<String, Boolean, Integer> e = new GenericTypes<>();
        e.setA("Hello").setB(true).setC(42);
        expect(Expectation.build()
                .actual(g.get("pojo"))
                .isEqualTo(e));

    }

    @Test(groups = {"unit"})
    public void pojoDSLClassObjects() {
        Goate g = new Goate().put("pojo", "pojo::o::actual,o::first,o::second,o::third")
                .put("actual", GenericTypes.class)
                .put("first", String.class)
                .put("second", Boolean.class)
                .put("third", Integer.class)
                .put("a", "Hello")
                .put("b", true)
                .put("c", 42);
        GenericTypes<String, Boolean, Integer> e = new GenericTypes<>();
        e.setA("Hello").setB(true).setC(42);
        expect(Expectation.build()
                .actual(g.get("pojo"))
                .isEqualTo(e));

    }

    @Test(groups = {"unit"})
    public void pojoDSLClassGenericStrings() {
        Goate g = new Goate().put("pojo", "pojo::o::actual,java.lang.String,java.lang.Boolean,java.lang.Integer")
                .put("actual", GenericTypes.class)
                .put("a", "Hello")
                .put("b", true)
                .put("c", 42);
        GenericTypes<String, Boolean, Integer> e = new GenericTypes<>();
        e.setA("Hello").setB(true).setC(42);
        expect(Expectation.build()
                .actual(g.get("pojo"))
                .isEqualTo(e));

    }

    @Test(groups = {"unit"})
    public void pojoDSLClassString() {
        Goate g = new Goate().put("pojo", "pojo::com.thegoate.expect.test.SimpleObject")
                .put("a", "Hello")
                .put("b", true)
                .put("nested.no", "world");
        SimpleObject e = new SimpleObject();
        e.setA("Hello").setB(true).setNested(new NestedObject().setNo("world"));
        expect(Expectation.build()
                .actual(g.get("pojo"))
                .isEqualTo(e));

    }

    @Test(groups = {"unit"})
    public void flatToNested() {
        String flat = "{ \"susy\": \"hello\", \"bobby\": \"world\"}";
        String nested = "{ \"little_susy\": {\"fieldA\":\"hello\"}, \"little_bobby\": {\"fieldA\":\"world\"}}";
        Parent flatParent = new DeSerializer().from(Parent.class).data(new ToGoate(flat).convert()).build(Parent.class);
        Parent nestedParent = new DeSerializer().from(Child1.class).data(new ToGoate(nested).convert()).build(Parent.class);
        expect(Expectation.build()
                .actual(flatParent)
                .isEqualTo(nestedParent));
    }

    @Test(groups = {"unit"})
    public void primitiveModels() {
        JSONObject jo = new ModelBuilder<APrimitivesPojo, JSONObject>(APrimitivesPojo.class).build(JSONObject.class);
        String ejson = "{\"bInt\":0,\"aBoolean\":false,\"aChar\":\"\\u0000\",\"bBoolean\":true,\"aInt\":0,\"aString\":\"\",\"bShort\":0,\"aFloat\":0,\"aDouble\":0,\"bDouble\":0,\"bByte\":0,\"aBigInteger\":0,\"bFloat\":0,\"aShort\":0,\"aBigDecimal\":0,\"bChar\":\"A\",\"aByte\":0}";
        LOG.info("model:\n" + jo);
        expect(Expectation.build()
                .actual(jo)
                .isEqualTo(ejson));
    }


    @Test(groups = {"unit"})
    public void collectionModels() {
        JSONObject jo = new ModelBuilder<ACollectionsPojo, JSONObject>(ACollectionsPojo.class).build(JSONObject.class);
        String ejson = "{\"aList\":[\"\"],\"aMap\":{\"key\":\"\"},\"aIntArray\":[0],\"bMap\":{\"key\":true}}";
        LOG.info("model:\n" + jo);
        expect(Expectation.build()
                .actual(jo)
                .isEqualTo(ejson));
    }

    @Test(groups = {"unit"})
    public void mixedModels() {
        JSONObject jo = new ModelBuilder<AMixedPojo, JSONObject>(AMixedPojo.class).build(JSONObject.class);
        String ejson = "{\"nestedList\":[{\"aList\":[\"\"],\"aMap\":{\"key\":\"\"},\"aIntArray\":[0],\"bMap\":{\"key\":true}}],\"nestedPojo\":{\"bInt\":0,\"aBoolean\":false,\"bBoolean\":true,\"aChar\":\"\\u0000\",\"aInt\":0,\"aString\":\"\",\"bShort\":0,\"aFloat\":0,\"aDouble\":0,\"bDouble\":0,\"bByte\":0,\"bFloat\":0,\"aShort\":0,\"bChar\":\"A\",\"aByte\":0,\"aBigInteger\":0,\"aBigDecimal\":0},\"theDate\":null}";
        LOG.info("model:\n" + jo);
        expect(Expectation.build()
                .actual(jo)
                .isEqualTo(ejson));
    }

    @Test(groups = {"unit"})
    public void mixedSourcedModels() {
        JSONObject jo = new ModelBuilder<AMixedPojo, JSONObject>(AMixedPojo.class).source(ModelBuilder.class).build(JSONObject.class);
        String ejson = "{\"nestedList\":[{\"aList\":[\"\"],\"aMap\":{\"key\":\"\"},\"aIntArray\":[0],\"bimbap\":{\"key\":true}}],\"nestedPojo\":{\"bInt\":0,\"aBoolean\":false,\"bBoolean\":true,\"aChar\":\"\\u0000\",\"aInt\":0,\"aString\":\"\",\"bShort\":0,\"aFloat\":0,\"aDouble\":0,\"bDouble\":0,\"bByte\":0,\"bFloat\":0,\"aShort\":0,\"bChar\":\"A\",\"aByte\":0,\"aBigInteger\":0,\"Trashbinye\":0},\"theDate\":null}";
        LOG.info("model:\n" + jo);
        expect(Expectation.build()
                .actual(jo)
                .isEqualTo(ejson));
    }

    @Test(groups = {"unit"})
    public void reallyBigList() {
        BigList big = new BigList();
        for (int i = 0; i < 100; i++) {
            big.addContent(new SimpleContent().setCount(i).setName("index" + i));
        }

        String bigJ = (String) new Serializer<>(big, DefaultSource.class).to(new JsonString());
        LOG.debug("bigJ: \n" + bigJ);
    }

    @Test(groups = {"unit"})
    public void simpleEmbeddedList() {
        TestFailures body = new TestFailures();
        body.add("a", 1);
        String message = "" + new Serializer<>(body, DefaultSource.class).to(new JsonString());
        LOG.info(message);
    }

    @Test(groups = {"unit"})
    public void funkyKeyTest() {
        FunkyKey funky = new FunkyKey();
        String message = "" + new Serializer<>(funky, FunkyKey.class).to(new JsonString());
        LOG.info("Funky Key", "\n" + message);
    }

    @Test(groups = {"unit"})
    public void usernameSerializationTest() {
        User user = new User();
        user.setUserName("barb");
        String userJ = "" + new Serializer<>(user, DefaultSource.class).to(new JsonString());
        expect(Expectation.build()
                .actual("userName")
                .from(userJ)
                .isEqualTo(user.getUserName()));
    }

    @Test(groups = {"unit"})
    public void localDateSerialize() {
        JustLocalDate ld = new JustLocalDate().setTheDate(LocalDate.of(2009, 11, 24));
        Object act = new Serializer<>(ld).to(new JsonString());
        expect(Expectation.build()
                .actual(act)
                .isEqualTo("{\"theDate\":\"2009-11-24\"}"));
    }

    @Test(groups = {"unit"})
    public void listSourceOrderedDeserializing() {
        String content = "[{\"name\":\"athan\",\"count\":42}]";

        BigList list = new DeSerializer().from(BigList.class)
                .data(new ToGoate(content).convert())
                .build(BigList.class);
        expect(Expectation.build()
                .actual(list.getList().size())
                .isGreaterThanOrEqualTo(1));
    }
}
