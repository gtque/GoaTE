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
package com.thegoate.xml;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.Utility;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Base json util class that implements the isType method.
 * Created by Eric Angeli on 5/19/2017.
 */
public abstract class XmlUtil implements Utility {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Object takeActionOn = null;
    protected Object nested = null;
    protected Goate health = new Goate();
    protected Goate data;

    public XmlUtil(Object val){
        if(val instanceof String){
            String select = ""+val;
            if((select).contains(">")){
                nested = select.substring(select.indexOf(">")+1);
                val = select.substring(0,select.indexOf(">"));
            }
        }
        this.takeActionOn = val;
    }

    @Override
    public Goate healthCheck(){
        return health;
    }

    @Override
    public XmlUtil setData(Goate data){
        this.data = data;
        return this;
    }

    @Override
    public boolean isType(Object check) {
        boolean result = false;
        try {
            if((""+check).startsWith("<")) {
                Document document = null;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader("" + check));
                document = db.parse(is);
                if (document != null)
                    result = true;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
    protected abstract Object processNested(Object subContainer);
}
