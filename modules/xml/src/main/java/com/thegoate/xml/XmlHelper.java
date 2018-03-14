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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * Created by Eric Angeli on 2/9/2018.
 */
public class XmlHelper {
    public static Document generateDocument(String xml)
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            document = db.parse(is);
        }
        catch(Exception e)
        {
            System.out.println("problem generating the document for scanning: " + e);
        }
        return document;
    }

    public static Goate buildNodeListInformation(NodeList nodes)
    {
        Goate nodeListInfo = new Goate();
        for(int i = 0; i<nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                String nodeName = node.getNodeName();
                Object nodeCount = nodeListInfo.get(nodeName + ".count");
                //TestObject nodeIndex = nodeListInfo.getTestObject(nodeName+"_count");
                if(nodeCount == null)
                {
                    nodeCount = nodeListInfo.put(nodeName+".count", 1).get(nodeName+".count");
                }
                else
                {
                    int count = Integer.parseInt("" + nodeCount)+1;
                    nodeListInfo.put(nodeName+".count", count);
                }
                //nodeListInfo.addObject(nodeCount);
            }
        }
        return nodeListInfo;
    }

    public static String generatePreLable(String name, Goate nodeInfo)
    {
        String label = name;
        Object nodeIndex = nodeInfo.get(name+".index");
        if(nodeIndex==null)
            nodeIndex = nodeInfo.put(name+".index", 0).get(name+".index");
        else
            nodeIndex = nodeInfo.put(name+".index", Integer.parseInt(""+nodeIndex)+1).get(name+".index");
        label += "."+nodeIndex;
        return label;
    }
}
