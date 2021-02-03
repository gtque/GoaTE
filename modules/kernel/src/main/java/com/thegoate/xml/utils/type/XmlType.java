package com.thegoate.xml.utils.type;

import com.thegoate.annotations.IsDefault;
import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@IsType
@IsDefault(forType = true)
public class XmlType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof Document || parseXml(check);
    }

    public boolean parseXml(Object check){
        boolean result = false;
        try {
            String co = (""+check).trim();
            if(co.startsWith("<")&&co.endsWith(">")) {
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
    @Override
    public Class type(Object check){
        return Document.class;
    }
}
