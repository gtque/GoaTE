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
package com.thegoate.xml.utils.get;

import com.thegoate.utils.get.GetUtil;
import com.thegoate.utils.get.GetUtility;
import com.thegoate.xml.HtmlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Get the field from the given json.
 * Created by Eric Angeli on 5/19/2017.
 */
@GetUtil(type = Document.class)
public class GetHtmlField extends HtmlUtil implements GetUtility {

    Document doc = null;

    public GetHtmlField(Object val) {
        super(val);
    }

    @Override
    protected Object processNested(Object subContainer) {
        Object result = subContainer;
        if (nested != null) {
//            result = new Get(nested).from(subContainer);
        }
        return result;
    }

    @Override
    public Object from(Object container) {
        Object result = null;
        if (doc == null) {
            doc = Jsoup.parse(""+container);
        }
        if (doc != null) {
            result = doc.select("" + takeActionOn);
//            if(((Elements)result).size()==0){
//                result = new NotFound("" + takeActionOn);
//            }
        }
        result = processNested(result);
        return result;
    }

    public Object get(Object id){
        takeActionOn = id;
        return from(id);
    }
}
