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
package com.thegoate.spreadsheets.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.map.EnumMap;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.spreadsheets.utils.SheetUtils;
import com.thegoate.spreadsheets.utils.SpreadSheetUtil;
import com.thegoate.utils.fill.serialize.DeSerializer;
import com.thegoate.utils.fill.serialize.DefaultSource;
import com.thegoate.utils.fill.serialize.Serializer;
import com.thegoate.utils.togoate.ToGoateUtil;
import com.thegoate.utils.togoate.ToGoateUtility;

import java.util.Map;

/**
 * Created by Eric Angeli on 12/6/2018.
 */
@ToGoateUtil
public class SpreadSheetToGoate extends SpreadSheetUtil implements ToGoateUtility {

    boolean autoIncrement = false;
    Class<?> theEnum = null;
    Map<String, String> theMap = null;
    Class pojo = null;
    Class csvSource = null;
    Class csvDestination = null;

    public SpreadSheetToGoate(Object val) {
        super(val);

    }

    public <E extends Enum<E>> SpreadSheetToGoate mapTo(Class<E> enumClass) {
        this.theEnum = enumClass;
        return this;
    }

    public SpreadSheetToGoate mapTo(Map<String, String> map) {
        this.theMap = map;
        return this;
    }

    public SpreadSheetToGoate mapToPojo(Class pojo) {
        return mapToPojo(pojo, DefaultSource.class, DefaultSource.class);
    }

    public SpreadSheetToGoate mapToPojo(Class pojo, Class source) {
        return mapToPojo(pojo, source, DefaultSource.class);
    }

    public SpreadSheetToGoate mapToPojo(Class pojo, Class source, Class destination) {
        this.pojo = pojo;
        this.csvSource = source;
        this.csvDestination = destination;
        return this;
    }

    @Override
    protected void init(Object val){
        processNested = false;
        super.init(val);
    }

    @Override
    protected Object processNested(Object subContainer) {
        return subContainer;
    }

    @Override
    public ToGoateUtility autoIncrement(boolean increment) {
        this.autoIncrement = increment;
        return this;
    }

    @Override
    public Goate convert() {
        //JSONArray ja = new JSONArray("[]");
        Goate goate = new Goate();

        SheetUtils sheet = (SheetUtils) takeActionOn;
        for (int rowIndex = 0; rowIndex < sheet.rowCount(); rowIndex++) {
            Goate row = sheet.getRow(rowIndex);
            if(pojo!=null){
                {
                    Object rPojo = new DeSerializer().data(row).from(csvSource).build(pojo);
                    row = new Serializer<>(rPojo,csvDestination).toGoate();
                }
            }
            //JSONObject rjo = new JSONObject("{}");
            Goate rjo = new Goate();
            for (String key : row.keys()) {
                String colId = generateKey(key, theEnum, theMap);
                rjo.put(colId, checkPrimitive(row.get(key)));
                goate.put(""+rowIndex+"."+colId,rjo.getStrict(colId));
            }
            goate.put(""+rowIndex,rjo);
//            goate.merge(rjo,false);
        }
        return goate;
    }

    private Object checkPrimitive(Object check) {
        GoateReflection gr = new GoateReflection();
        if (gr.isBoolean(check)) {
            check = Boolean.parseBoolean("" + check);
        } else if (gr.isInteger(check)) {
            check = Integer.parseInt("" + check);
        } else if (gr.isDouble(check)) {
            check = Double.parseDouble("" + check);
        } else if (gr.isFloat(check)) {
            check = Float.parseFloat("" + check);
        } else if (gr.isLong(check)) {
            check = Long.parseLong("" + check);
        }
        return check;
    }

    private String generateKey(final String key, Class map, Map<String, String> altMap) {
        String mappedKey = key;
        if (altMap != null) {
            mappedKey = altMap.entrySet().parallelStream().filter(e -> e.getValue().equals(key)).map(Map.Entry::getKey).findFirst().orElse(key);
        } else {
            try {
                mappedKey = EnumMap.map(key).to(map).toString();
            } catch (Exception e) {
                LOG.debug("Spreadsheet To Goate", "Failed to map: " + key);
            }
        }
        return mappedKey;
    }
}
