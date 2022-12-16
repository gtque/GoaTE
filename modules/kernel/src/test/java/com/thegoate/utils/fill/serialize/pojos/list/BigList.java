package com.thegoate.utils.fill.serialize.pojos.list;

import com.thegoate.utils.fill.serialize.Kid;
import com.thegoate.utils.fill.serialize.collections.ListType;

import java.util.ArrayList;
import java.util.List;

public class BigList extends Kid {

    @ListType(type = SimpleContent.class)
    private List<SimpleContent> list = new ArrayList<>();

    public List<SimpleContent> getList() {
        return list;
    }

    public void setList(List<SimpleContent> list) {
        this.list = list;
    }

    public BigList addContent(SimpleContent content){
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(content);
        return this;
    }
}
