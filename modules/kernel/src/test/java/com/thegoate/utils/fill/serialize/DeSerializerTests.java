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
import com.thegoate.json.utils.fill.serialize.string.JsonStringConverter;
import com.thegoate.json.utils.fill.serialize.to.JsonString;
import com.thegoate.json.utils.tojson.GoateToJSON;
import com.thegoate.utils.fill.serialize.pojos.*;
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by Eric Angeli on 6/26/2018.
 */
public class DeSerializerTests {

    @Test(groups = {"unit"})
    public void testListPojo(){
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
    public void simpleDeserializeStringObjectTest(){
        Map<String,Object> data = new HashMap<>();
        data.put("expanded name", 42);
        data.put("fieldName", "Hello, world!");
        data.put("big decimal", "3.14159");
        data.put("double D", "42.42");
        data.put("long l", 42L);
        SimplePojo pojo = new DeSerializer().data(data).from(SimpleSource.class).build(SimplePojo.class);
        assertTrue(new CastBoolean("1").cast(Boolean.class));
        assertFalse(new CastBoolean("0").cast(Boolean.class));
        assertEquals(pojo.getFieldName(),"Hello, world!");
        assertEquals(pojo.getSomeInt(),42);
        assertEquals(pojo.getBigD(),new BigDecimal("3.14159"));
        assertEquals(pojo.getD(), new Double("42.42"));
        assertEquals(pojo.getC(),'c');
        assertEquals(pojo.getL(),42L);
        assertEquals(pojo.getaByte(),new Byte("1"));
        assertEquals(pojo.getF(), 4F);
        assertTrue(pojo.isBool());
        Goate d = new Serializer<>(pojo,SimpleSource.class).toGoate();
        String json = new GoateToJSON(d).convert();
        System.out.println("serialized: " + json);
    }

    @Test(groups = {"unit", "deserialize"})
    public void simpleDeserializeGoateTest(){
        Goate data = new Goate();
        data.put("expanded name", 42);
        data.put("fieldName", "Hello, world!");
        data.put("big decimal", "3.14159");
        data.put("double D", "42.42");
        data.put("long l", 42L);
        SimplePojo pojo = new DeSerializer().data(data).from(SimpleSource.class).build(SimplePojo.class);
        assertEquals(pojo.getFieldName(),"Hello, world!");
        assertEquals(pojo.getSomeInt(),42);
        assertEquals(pojo.getBigD(),new BigDecimal("3.14159"));
        assertEquals(pojo.getD(), new Double("42.42"));
        assertEquals(pojo.getC(),'c');
        assertEquals(pojo.getL(),42L);
        assertEquals(pojo.getaByte(),new Byte("1"));
        assertEquals(pojo.getF(), 4F);
        assertTrue(pojo.isBool());
    }

    @Test(groups = {"unit", "deserialize"})
    public void complexDeserializeGoateTest(){
        Goate data = new Goate();
        data.put("date", "11-24-2009");
        data.put("nested.expanded name", 42);
        data.put("nested.fieldName", "Hello, world!");
        data.put("nested.big decimal", "3.14159");
        data.put("nested.double D", "42.42");
        data.put("nested.long l", 42L);
        ComplexPojo pojo = (ComplexPojo)data.get("complex pojo", "pojo::complex pojo,simple expected");//new DeSerializer().data(data).from(SimpleSource.class).build(ComplexPojo.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(pojo.getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(pojo.getNested().getFieldName(),"Hello, world!");
        assertEquals(pojo.getNested().getSomeInt(),42);
        assertEquals(pojo.getNested().getBigD(),new BigDecimal("3.14159"));
        assertEquals(pojo.getNested().getD(), new Double("42.42"));
        assertEquals(pojo.getNested().getC(),'c');
        assertEquals(pojo.getNested().getL(),42L);
        assertEquals(pojo.getNested().getaByte(),new Byte("1"));
        assertEquals(pojo.getNested().getF(), 4F);
        assertTrue(pojo.getNested().isBool());
    }

    @Test(groups = {"unit", "deserialize"})
    public void nestedDeserializeGoateTest(){
        Goate data = new Goate();
        int[] nums = {42,8};
        int[] expectNums = {42,8};
        int[] expectNums2 = {21,4,2009};
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
        data.put("map.0.key","bryan");
        data.put("map.0.value", true);
        data.put("map.1.key","tarun");
        data.put("map.1.value", false);
        data.put("map2.0.key","bryan");
        data.put("map2.0.value.class", "com.thegoate.utils.fill.serialize.pojos.SimplePojo");
        data.put("map2.0.value.long l", "long::84");
        data.put("map2.1.key","tarun");
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
        assertEquals(((SimplePojo)pojo.getList().get(1)).getFieldName(), "Paula");
        assertTrue(Boolean.parseBoolean(""+pojo.getMap().get("bryan")));
        assertFalse(Boolean.parseBoolean(""+pojo.getMap().get("tarun")));
        assertEquals(((SimplePojo)(pojo.getMap2().get("bryan"))).getL(), 84L);
        assertTrue(Boolean.parseBoolean(""+pojo.getMap2().get("tarun")));
        assertEquals(pojo.getCp()[0].getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(pojo.getCp()[0].getNested().getFieldName(),"Hello, world!");
        assertEquals(pojo.getCp()[0].getNested().getSomeInt(),42);
        assertEquals(pojo.getCp()[0].getNested().getBigD(),new BigDecimal("3.14159"));
        assertEquals(pojo.getCp()[0].getNested().getD(), new Double("42.42"));
        assertEquals(pojo.getCp()[0].getNested().getC(),'c');
        assertEquals(pojo.getCp()[0].getNested().getL(),42L);
        assertEquals(pojo.getCp()[0].getNested().getaByte(),new Byte("1"));
        assertEquals(pojo.getCp()[0].getNested().getF(), 4F);
        assertEquals(pojo.getCp()[1].getMap().get("Peter"),"Parker");
        assertEquals(pojo.getCp()[1].getMap().get("Tony"),"Stark");
        assertTrue(pojo.getCp()[0].getNested().isBool());
        Goate d = new Serializer<>(pojo,SimpleSource.class).toGoate();

        Map<String,Object> data2 = new Serializer<>(pojo,SimpleSource.class).toMap(HashMap.class);

        assertTrue(d.size()>=50);

        NestedPojos pojo2 = new DeSerializer()
                .data(data)
                .from(SimpleSource.class)
                .build(NestedPojos.class);
        String jstring = ""+new Serializer<>(pojo2, Cheese.class).to(new JsonString());
        System.out.println(jstring);
        assertEquals(pojo.getNumbers(), pojo2.getNumbers());
        assertEquals(pojo.getNumbers2(), pojo2.getNumbers2());
        assertEquals(pojo.getList().get(0), pojo2.getList().get(0));
        assertEquals(((SimplePojo)pojo2.getList().get(1)).getFieldName(), "Paula");
        assertTrue(Boolean.parseBoolean(""+pojo2.getMap().get("bryan")));
        assertFalse(Boolean.parseBoolean(""+pojo2.getMap().get("tarun")));
        assertEquals(((SimplePojo)(pojo2.getMap2().get("bryan"))).getL(), 84L);
        assertTrue(Boolean.parseBoolean(""+pojo2.getMap2().get("tarun")));
        assertEquals(pojo2.getCp()[0].getDate(), LocalDate.parse("2009-11-24", formatter));
        assertEquals(pojo2.getCp()[0].getNested().getFieldName(),"Hello, world!");
        assertEquals(pojo2.getCp()[0].getNested().getSomeInt(),42);
        assertEquals(pojo2.getCp()[0].getNested().getBigD(),new BigDecimal("3.14159"));
        assertEquals(pojo2.getCp()[0].getNested().getD(), new Double("42.42"));
        assertEquals(pojo2.getCp()[0].getNested().getC(),'c');
        assertEquals(pojo2.getCp()[0].getNested().getL(),42L);
        assertEquals(pojo2.getCp()[0].getNested().getaByte(),new Byte("1"));
        assertEquals(pojo2.getCp()[0].getNested().getF(), 4F);
        assertEquals(pojo2.getCp()[1].getMap().get("Peter"),"Parker");
        assertEquals(pojo2.getCp()[1].getMap().get("Tony"),"Stark");
        assertTrue(pojo2.getCp()[0].getNested().isBool());
    }

    @Test(groups = {"unit"})
    public void nestedMapped(){
        SimpleNested pojo2 = new SimpleNested();
        SimpleInt si = new SimpleInt();
        si.setValue(42);
        pojo2.setInnerField(si);
        String jstring = ""+new Serializer<>(pojo2, Cheese.class).to(new JsonString());
        System.out.println(jstring);
        assertEquals(new Get("chuck.value").from(jstring), 42);
    }

    @Test(groups = {"unit"})
    public void skipSerializingNested(){
        String jstring = "{\"ld\":\"04-22-2019\", \"innerField\":{\"value\":42}}";
        SimpleNested pojo2 = new DeSerializer().data(new ToGoate(jstring).convert()).build(SimpleNested.class);
        Goate data = new Serializer<>(pojo2, Cheese.class).skipSerializingObjects().skipSerializingGoatePojos().toGoate();
        assertTrue(data.get("ld") instanceof LocalDate);
    }
}
