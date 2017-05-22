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

package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.data.PropertyFileDL;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;

import java.util.List;

/**
 * Checks for a properties file in a folder called eut
 * whith a file name using the pattern eut.properties, where env is set or passed as a system property or environment
 * variable where eut is replaced with the name of the environment under test.<br>
 * example: -Deut=local<br>
 *      then in the resource directory:<br>
 *          resources/eut/local.properties<br>
 * You can override the properties file by setting the same parameter in your run data.
 * Created by gtque on 5/19/2017.
 */
@GoateDSL(word = "eut")
public class EutConfigDSL extends DSL {
    public EutConfigDSL(Object value) {
        super(value);
    }

    @Override
    public Object evaluate(Goate data) {
        Goate eutConfig = (Goate)data.get("_goate_:eutConfig", new Goate());
        if(eutConfig==null||eutConfig.size()==0) {
            String eut = "" + data.get("eut", "local");
            List<Goate> d = new PropertyFileDL().file("eut/"+eut+".properties").load();
            if(d!=null&&d.size()>0){
                eutConfig = d.get(0);
                data.put("_goate_:eutConfig", eutConfig);
            }
        }
        String key = ""+get(1,data);
        return data.get(key, eutConfig!=null?eutConfig.get(key):null);
    }
}
