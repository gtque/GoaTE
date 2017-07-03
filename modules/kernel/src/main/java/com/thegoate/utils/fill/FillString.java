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

package com.thegoate.utils.fill;

import com.thegoate.Goate;
import com.thegoate.annotations.IsDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fills the given string from the given data collection.
 * Does not currently support nested fill.
 * Created by Eric Angeli on 5/5/2017.
 */
@FillUtil
@IsDefault
public class FillString implements FillUtility{

    String fill = "";
    List<String> fillList = new ArrayList<>();

    public FillString(Object fill){
        this.fill = ""+fill;
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof String;
    }

    @Override
    public Object with(Goate data) {
        String result = fill.replace("\\$","(!dollar_bills!)");
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("\\$\\{.*?\\}")
                .matcher(result);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String id:allMatches){
            String d = id.replace("${","").replace("}","");
            result = result.replace(id,""+data.get(d,d));
        }
        return result.replace("(!dollar_bills!)","\\$");
    }
}
