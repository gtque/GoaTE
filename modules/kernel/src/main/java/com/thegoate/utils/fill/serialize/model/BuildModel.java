package com.thegoate.utils.fill.serialize.model;

import com.thegoate.Goate;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.fill.serialize.Serializer;

import java.lang.reflect.InvocationTargetException;

public class BuildModel {

    BleatBox LOG = BleatFactory.getLogger(getClass());

    public Goate model(Class template, Class source, boolean asSourced) throws InstantiationException, IllegalAccessException {
        Goate model = null;
        try {
            model = new Serializer<>(template.getDeclaredConstructor().newInstance(), source).asSourced(asSourced).toGoate();
        } catch (InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Build Model", "Problem instantiating new instance: " + e.getMessage(), e);
        }
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
