package com.thegoate.utils.fill.serialize.model;

import com.thegoate.Goate;
import com.thegoate.utils.fill.serialize.Serializer;

public class BuildModel {

    public Goate model(Class template, Class source, boolean asSourced) throws InstantiationException, IllegalAccessException {
        Goate model = new Serializer<>(template.newInstance(), source).asSourced(asSourced).toGoate();
//        GoateReflection gr = new GoateReflection();
//        Goate model = new Goate();
//        Map<String, Field> fields = gr.findFields(template);
//        for (Map.Entry<String, Field> field : fields.entrySet()) {
//            GoateSource gs = findGoateSource(field.getValue(), source);
//            if (gs != null) {
//                if (!gs.skipInModel() || !asSourced) {
//                    model.put(field.getValue().)
//                }
//            }
//        }
//        return model;
        return model;
    }
}
