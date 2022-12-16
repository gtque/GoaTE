package com.thegoate.utils.fill.goate;

import com.thegoate.Goate;
import com.thegoate.utils.Utility;
import com.thegoate.utils.fill.FillUtil;
import com.thegoate.utils.fill.FillUtility;

/**
 * Created by Eric Angeli on 3/1/2019.
 */
@FillUtil
public class FillGoate implements FillUtility {
    Goate kid;
    Goate data;

    public FillGoate(Object val) {
        if(val instanceof Goate) {
            this.kid = (Goate) val;
        }
    }

    @Override
    public Object with(Goate data) {
        kid.merge(data,true);
        for(String dkey:data.keys()) {
            Goate filtered = kid.filter(dkey);
            for (String key : filtered.keys()) {
                switch ("" + data.get(dkey)) {
                    case "drop field::":
                        kid.drop(key);
                        break;
                    case "null field::":
                        kid.put(key, "null::");
                        break;
                    case "empty field::":
                        kid.put(key, "empty::");
                        break;
                    default:
                        break;
                }
            }
        }
        return kid;
    }

    @Override
    public Utility setData(Goate data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean isType(Object check) {
        return check instanceof Goate;
    }

    @Override
    public Goate healthCheck() {
        return null;
    }
}
