package com.thegoate.utils.fill.serialize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.thegoate.Goate;
import com.thegoate.HealthMonitor;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.compare.CompareUtility;

/**
 * Created by Eric Angeli on 7/3/2020.
 */
public class Nanny implements HealthMonitor, TypeT, Cloneable {

    @GoateIgnore
    protected BleatBox LOG = BleatFactory.getLogger(getClass());

    @GoateIgnore
    protected String equalityCheck = "==";

    @GoateIgnore
    private List<Class> goateType = new ArrayList<>();

    @GoateIgnore
    private Goate health = new Goate();

    public void setGoateType(List<Class> type) {
        this.goateType = type;
    }

    public Nanny equalityCheck(String check) {
        this.equalityCheck = check;
        return this;
    }

    @Override
    public Class goateType(int index) {
        Class c = Object.class;
        if (index < goateType.size()) {
            c = goateType.get(index);
        }
        return c;
    }

    /**
     * Resets the health collection to a new empty one.
     */
    protected void resetHealth() {
        health = new Goate();
    }

    public void reportHealth(String id, Object message) {
        health.put(id, message);
    }

    /**
     * This returns and resets the current health report.
     *
     * @return Goate collection containing any reported health issues.
     */
    public Goate healthCheck() {
        Goate report = health;
        resetHealth();
        return report;
    }

    @Override
    public boolean equals(Object compare) {
        boolean result = true;
        if (compare == null) {
            result = false;
            reportHealth(getClass().getSimpleName(), "{ initialized instance != null }");
        }
        if (getClass().isInstance(compare)) {
            GoateReflection gr = new GoateReflection();
            for (Map.Entry<String, Field> field : gr.findFields(getClass()).entrySet()) {
                IgnoreOnCompare ignore = field.getValue().getAnnotation(IgnoreOnCompare.class);
                if (ignore == null) {
                    boolean accessible = field.getValue().isAccessible();
                    try {
                        field.getValue().setAccessible(true);
                        Object actual = field.getValue().get(this);
                        Object expected = field.getValue().get(compare);
                        CompareUtility check = new Compare(actual).to(expected).using(equalityCheck);
                        if (!check.evaluate()) {
                            result = false;
                            String checkHealth = check.healthCheck().toString("\t\t", "", true);
                            String label = actual.getClass().getSimpleName() + " " + field.getKey();
                            if (checkHealth.isEmpty()) {
                                //reportHealth("field mismatch ##",  label + " {" + actual + " != " + expected + "}");
                                reportHealth(label, " {" + actual + " != " + expected + "}");
                            } else {
                                //checkHealth.replace("%_goate_t_%", "%_goate_t_%%_goate_t_%").replace("\t","%_goate_t_%");
                                checkHealth = check.healthCheck().pad(checkHealth + "\n\t}", "\t");
                                //reportHealth("field mismatch ##", label + " {\n" + checkHealth);
                                reportHealth(label, " {\n" + checkHealth);
                            }
                        }
                    } catch (Exception e) {
                        reportHealth("field access ##", "problem accessing field: " + e.getMessage());
                    } finally {
                        field.getValue().setAccessible(accessible);
                    }
                } else {
                    LOG.debug("Pojo Equals", "Ignored field: " + field.getKey());
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(":{");
        GoateReflection gr = new GoateReflection();
        boolean notFirst = false;
        for (Map.Entry<String, Field> field : gr.findFields(getClass()).entrySet()) {
            if (notFirst) {
                sb.append(", ");
            }
            sb.append(field.getKey()).append(":");
            boolean accessible = field.getValue().isAccessible();
            Object fieldValue = "Exception: could not get toString";
            try {
                field.getValue().setAccessible(true);
                fieldValue = field.getValue().get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sb.append(fieldValue);
            notFirst = true;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * I apologize for what comes below. This is a crazy idea, even if it works, and the code is a mess. I got it to a point
     * where the test cases are passing and did not bother to clean up the code to make it more readable. I am sorry.
     * Especially to my future self who will like be the one that has to touch this code.
     * The gist of the below is to provide a clone of the object, but setting the "ignored" fields to either
     * a default primitive value or null. Nested fields are only processed for lists, collections, arrays, maps, or kid objects.
     * @param fieldNames the list of Strings of fields to ignore
     * @return the cloned object with ignored fields set to a defined default.
     * @throws CloneNotSupportedException
     */
    public Object cloneButIgnore(List<String> fieldNames) throws CloneNotSupportedException {
        Object clone = this.clone();
        GoateReflection gr = new GoateReflection();
        Map<String, Field> fields = gr.findFields(getClass());
        if (fieldNames != null && fieldNames.size() > 0) {
            for (String fieldName : fieldNames) {
                if (fieldName.contains(".")) {
                    String baseFieldName = fieldName.substring(0, fieldName.indexOf("."));
                    if (fields.containsKey(baseFieldName)) {
                        Field field = fields.get(baseFieldName);
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        List<String> ignore = new ArrayList<>();
                        ignore.add(fieldName.replaceFirst(baseFieldName + "\\.", "").replaceFirst("\\*\\.", ""));
                        try {
                            Object fieldValue = field.get(clone);
                            if (gr.isCollectionOrMap(field)) {
                                //set support not actually implemented yet, so set field types are simply not processed at this time.
                                if (Map.class.isAssignableFrom(field.getType())) {
                                    for (Object entryO : ((Map) fieldValue).entrySet()) {
                                        if (entryO instanceof Map.Entry) {
                                            Map.Entry entry = (Map.Entry) entryO;
                                            if (gr.isPrimitiveOrNumerical(entry.getValue())) {
                                                entry.setValue(gr.getDefaultPrimitive(entry.getValue().getClass()));
                                            } else {
                                                if (ignore.get(0).isEmpty()) {
                                                    entry.setValue(null);
                                                } else {
                                                    if (entry.getValue() instanceof Kid) {
                                                        entry.setValue(((Kid) entry.getValue()).cloneButIgnore(ignore));
                                                    } else {
                                                        LOG.warn("Clone but ignore", "sorry, I can only ignore nested fields in maps if the containing object is an instance of Kid: " + fieldName);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (Collection.class.isAssignableFrom(field.getType())) {
                                    for (Object entry : (Collection) fieldValue) {
                                        if (gr.isPrimitiveOrNumerical(entry)) {
                                        } else {
                                            if (ignore.get(0).isEmpty()) {
                                            } else {
                                                if (entry instanceof Kid) {
                                                    String secondBase = ignore.get(0).split("\\.")[0];
                                                    Map<String, Field> secondField = gr.findFields(entry.getClass());//entry.setValue(((Kid) entry.getValue()).cloneButIgnore(ignore));
                                                    if(secondField.containsKey(secondBase)){
                                                        Field field2 = secondField.get(secondBase);
                                                        boolean accessible2 = field2.isAccessible();
                                                        field2.setAccessible(true);
                                                        if(ignore.get(0).contains(".")){
                                                            Object field2Value = field2.get(entry);
                                                            if(field2Value instanceof Kid) {
                                                                List<String> ignore2 = new ArrayList<>();
                                                                ignore2.add(ignore.get(0).replaceFirst(secondBase+"\\.", ""));
                                                                field2.set(entry, ((Kid)field2Value).cloneButIgnore(ignore2));
                                                            }
                                                        } else {
                                                            if(gr.isPrimitiveNumber(field2.getType())){
                                                                field2.set(entry, gr.getDefaultPrimitive(field2.getType()));
                                                            } else {
                                                                field2.set(entry, null);
                                                            }
                                                        }
                                                        field2.setAccessible(accessible2);
                                                    }
                                                } else {
                                                    LOG.warn("Clone but ignore", "sorry, I can only ignore nested fields in maps if the containing object is an instance of Kid: " + fieldName);
                                                }
                                            }
                                        }
                                    }
                                } else if (field.getType().isArray()) {
                                    for (Object entry : (Object[])fieldValue) {
                                        if (gr.isPrimitiveOrNumerical(entry)) {
                                        } else {
                                            if (ignore.get(0).isEmpty()) {
                                            } else {
                                                if (entry instanceof Kid) {
                                                    String secondBase = ignore.get(0).split("\\.")[0];
                                                    Map<String, Field> secondField = gr.findFields(entry.getClass());//entry.setValue(((Kid) entry.getValue()).cloneButIgnore(ignore));
                                                    if(secondField.containsKey(secondBase)){
                                                        Field field2 = secondField.get(secondBase);
                                                        boolean accessible2 = field2.isAccessible();
                                                        field2.setAccessible(true);
                                                        if(ignore.get(0).contains(".")){
                                                            Object field2Value = field2.get(entry);
                                                            if(field2Value instanceof Kid) {
                                                                List<String> ignore2 = new ArrayList<>();
                                                                ignore2.add(ignore.get(0).replaceFirst(secondBase+"\\.", ""));
                                                                field2.set(entry, ((Kid)field2Value).cloneButIgnore(ignore2));
                                                            }
                                                        } else {
                                                            if(gr.isPrimitiveNumber(field2.getType())){
                                                                field2.set(entry, gr.getDefaultPrimitive(field2.getType()));
                                                            } else {
                                                                field2.set(entry, null);
                                                            }
                                                        }
                                                        field2.setAccessible(accessible2);
                                                    }
                                                } else {
                                                    LOG.warn("Clone but ignore", "sorry, I can only ignore nested fields in maps if the containing object is an instance of Kid: " + fieldName);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (fieldValue instanceof Kid) {
                                    field.set(clone, ((Kid) fieldValue).cloneButIgnore(ignore));
                                } else {
                                    LOG.warn("Clone but ignore", "sorry, I can only ignore nested fields if the containing object is an instance of Kid: " + fieldName);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            LOG.debug("Cloning and trying to ignore fields (primitive)", "This should never happen because it was forced to be accessible: " + e.getMessage(), e);
                        }
                        field.setAccessible(accessible);
                    }
                } else {
                    if (fields.containsKey(fieldName)) {
                        Field field = fields.get(fieldName);
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        //this is a crude hack, instead of trying to programatically ignore fields, just set the ignored fields to default value.
                        if (gr.isPrimitive(field.getType())) {
                            try {
                                field.set(clone, gr.getDefaultPrimitive(gr.primitiveType(field.get(clone))));
                            } catch (IllegalAccessException e) {
                                LOG.debug("Cloning and trying to ignore fields (primitive)", "This should never happen because it was forced to be accessible: " + e.getMessage(), e);
                            }
                        } else {
                            try {
                                field.set(clone, null);
                            } catch (IllegalAccessException e) {
                                LOG.debug("Cloning and trying to ignore fields", "This should never happen because it was forced to be accessible: " + e.getMessage(), e);
                            }
                        }
                        field.setAccessible(accessible);
                    }
                }
            }
        }
        return clone;
    }
}
