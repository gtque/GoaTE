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
 * Created by Eric Angeli on 1/16/2019.
 */
public abstract class TruncateDataLoader extends DataLoader {

    protected void truncate(List<Goate> data, List<Goate> dataToMerge){
        if((""+parameters.get("truncate",false)).equalsIgnoreCase("true")) {
            if(data.size()>dataToMerge.size()){
                Iterator it = data.iterator();
                int i = 0;
                List<Object> drop = new ArrayList<>();
                while(it.hasNext()){
                    if(i>=dataToMerge.size()){
                        drop.add(it.next());
                    } else {
                        it.next();
                    }
                    i++;
                }
                for(Object o:drop){
                    data.remove(o);
                }
            }
        }
    }
}
