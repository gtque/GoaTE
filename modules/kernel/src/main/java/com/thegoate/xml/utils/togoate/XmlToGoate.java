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
package com.thegoate.xml.utils.togoate;

import com.thegoate.Goate;
import com.thegoate.utils.togoate.ToGoateUtil;
import com.thegoate.utils.togoate.ToGoateUtility;
import com.thegoate.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.thegoate.xml.XmlHelper.*;

/**
 * Converts from json to goate.
 * Created by Eric Angeli on 5/19/2017.
 */
@ToGoateUtil(type = Document.class)
public class XmlToGoate extends XmlUtil implements ToGoateUtility {
    Goate result;
    boolean autoIncrement = true;

    public XmlToGoate(Object val) {
        super(val);
        takeActionOn = val;
        nested = null;
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
        Goate o = new Goate();
        try {
            if(takeActionOn instanceof Document){
                o = processXml((Document) takeActionOn);
            } else {
                o = processXml("" + takeActionOn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public Goate processXml(String xml) throws Exception {
        Goate o = new Goate();
        try {
            Document xmlDoc = generateDocument(xml);
            o = processXml(xmlDoc);
        } catch (Exception e) {
            throw new Exception("failed to load xml: " + e);
        }
        return o;
    }

    public Goate processXml(Document xmlDoc) {
        NodeList nodes = xmlDoc.getChildNodes();
        return processChildNodes(nodes, "");
    }

    private Goate processChildNodes(NodeList nodes, String preLabel) {
        Goate nodeInformation = buildNodeListInformation(nodes);
        Goate o = new Goate().autoIncrement(autoIncrement);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                String label = preLabel + (preLabel.equals("") ? "" : ".") + generatePreLable(node.getNodeName(), nodeInformation);
                o.put(label, node.getTextContent());
                if (node.getChildNodes().getLength() > 0) {
                    //new Merge().merge(o, processChildNodes(node.getChildNodes(), label));
                    o.merge(processChildNodes(node.getChildNodes(), label),false);
                }
            }
        }
        return o;
    }
}
