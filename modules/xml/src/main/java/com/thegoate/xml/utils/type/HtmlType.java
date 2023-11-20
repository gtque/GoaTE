package com.thegoate.xml.utils.type;

import com.thegoate.utils.type.FindType;
import com.thegoate.utils.type.IsType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@IsType
public class HtmlType extends FindType {

    @Override
    public boolean isType(Object check){
        return check instanceof Document || parseHtml(check);
    }

    public boolean parseHtml(Object check){
        boolean result = false;
        try {
            if((""+check).startsWith("<")) {
                Document document = Jsoup.parse(""+check);
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
