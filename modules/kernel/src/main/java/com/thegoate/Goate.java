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
package com.thegoate;

import com.thegoate.dsl.Interpreter;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.compare.Compare;
import com.thegoate.utils.togoate.ToGoate;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The manager for the collection of test data.
 * Created by gtque on 4/19/2017.
 */
public class Goate implements HealthMonitor{
    Map<String, Object> data = new ConcurrentHashMap<>();
    Interpreter dictionary;
    boolean increment = true;

    public Goate() {
        init();
    }

    public Goate(Map<String, Object> initialData) {
        init();
        data.putAll(initialData);
    }

    protected void init() {
        dictionary = new Interpreter(this);
    }

    public Goate autoIncrement(boolean increment) {
        this.increment = increment;
        return this;
    }

    public int size() {
        int size = 0;
        if (data != null) {
            size = data.size();
        }
        return size;
    }

    public Map<String, Object> data() {
        Map<String, Object> mapped = new ConcurrentHashMap<>();
        for (String key : keys()) {
            mapped.put(key, get(key));
        }
        return mapped;
    }

    public Set<String> keys() {
        return new TreeSet<>(data.keySet());
    }

    public String[] keysArray() {
        String[] keys = new String[data.keySet().size()];
        int i = 0;
        for (String key : data.keySet()) {
            keys[i] = key;
            i++;
        }
        return keys;
    }

    public Goate put(String key, Object value) {
        if (data == null) {
            data = new ConcurrentHashMap<>();
        }
//        if(value!=null) {
        if (key.contains("##")) {
            key = buildKey(key);
        }
        data.put(key, value == null ? "null::" : value);//can't put null in a map, so wrap in the null dsl.
//        }
        return this;
    }

    public boolean filterOnKey(String key){
        return key.contains("##");
    }

    public String buildKey(String key) {
        String fullKey = key;
        if (increment) {
            while (fullKey.contains("##")) {
                Goate billy = filter(fullKey.substring(0, fullKey.indexOf("##")) + "[0-9]+");
                fullKey = fullKey.replaceFirst("##", "" + billy.size());
            }
        } else {
            fullKey = key.replace("##", "" + System.nanoTime());
        }
        return fullKey;
    }

    public Object getStrict(String key) {
        return get(key, null, false);
    }

    public Object get(int index) {
        Iterator<String> keys = data.keySet().iterator();
        String key = "";
        while (index >= 0) {
            key = keys.next();
            index--;
        }
        return data.get(key);
    }

    public Object get(String key) {
        return get(key, null, true);
    }

    public Object get(String key, Object def) {
        return get(key, def, true);
    }

    public Object get(String key, Object def, boolean dsl) {
        return get(key, def, dsl, Object.class);
    }

    public <T> T get(String key, Object def, Class<T> type) {
        return get(key, def, true, type);
    }

    public <T> T get(String key, Object def, boolean dsl, Class<T> type) {
        Object value = System.getProperty(key);
        if(filterOnKey(key)){
            value = filter(key.replace("##","[0-9]*"));
        } else {
            if (value == null) {
                if (key.equals("username")) {//username is a special key name, in most cases we want to use the one set in the collection
                    //so it is checked first, and then if it is null it, check using the normal flow.
                    value = data.get(key);
                }
                if (value == null) {
                    value = System.getenv(key);
                    if (value == null) {
                        if (data.containsKey(key)) {
                            value = data.get(key);
                        } else if (def != null) {
                            data.put(key, def);
                            value = def;
                        }
                    }
                }
            }
            if (value == null && !data.containsKey(key)) {
                value = def;
            }
            if (value != null && dsl) {
                value = processDSL(key, value);
            }
        }
        return new GoateReflection().isPrimitive(type) ? doCastPrimitive(value, type) : type.cast(value);
    }

    public <T> T doCastPrimitive(Object value, Class<T> type) {
        GoateReflection gr = new GoateReflection();
        if (gr.isBooleanType(type)&&gr.isBoolean(value)) {
            value = new Boolean("" + value);
        } else if (gr.isByteType(type)&&gr.isByte(value)) {
            value = new Byte("" + value);
        } else if (gr.isIntegerType(type)&&gr.isInteger(value)) {
            value = new Integer("" + value);
        } else if (gr.isDoubleType(type)&&gr.isDouble(value)) {
            value = new Double("" + value);
        } else if (gr.isFloatType(type)&&gr.isFloat(value)) {
            value = new Float("" + value);
        } else if (gr.isLongType(type)&&gr.isLong(value)) {
            value = new Long("" + value);
        } else if (gr.isCharacterType(type)&&gr.isCharacter(value)) {
            value = new Character(("" + value).charAt(0));
        } else if (gr.isShortType(type)&&gr.isShort(value)) {
            value = new Short("" + value);
        }
        return type.cast(value);
    }

    public Object processDSL(Object value) {
        return processDSL("", value);
    }

    public Object processDSL(String key, Object value) {
        if (value != null) {
            String check = "" + value;
            if (check.contains("::")) {
                check = check.substring(0, check.indexOf("::"));
            } else {
                check = null;
            }
            if (check != null) {
                value = dictionary.translate(key, check, value);
            }
        }
        return value;
    }

    public Goate drop(String key) {
        data.remove(key);
        return this;
    }

    public Goate scrub(String pattern) {
        Goate scrub = filter(pattern);
        if (scrub != null) {
            for (String key : scrub.keys()) {
                drop(key);
            }
        }
        return this;
    }

    public String findKeyIgnoreCase(String find){
        String key = find;
        Optional<String> found = keys().parallelStream().filter(k -> k.equalsIgnoreCase(find)).findFirst();
        if(found.isPresent()){
            key = found.get();
        }
        return key;
    }

    /**
     * Simple filter, matches if key starts with the given pattern.
     *
     * @param pattern The pattern to match
     * @return A Goate collection containing matching elements.
     */
    public Goate filter(String pattern) {
        return filterStrict(pattern + ".*");
    }

    /**
     * Simple filter, matches if key matches the given pattern
     *
     * @param pattern The pattern to match
     * @return A Goate collection containing matching elements.
     */
    public Goate filterStrict(String pattern) {
        Goate filtered = new Goate();
        StringBuilder keyList = new StringBuilder();
        if (data != null) {
            for (String key : keys()) {
//                keyList.append("-!").append(key).append("!-");
                if (key.matches(pattern)) {
                    filtered.put(key, getStrict(key));
                }
            }
//            StringBuilder goatePattern = new StringBuilder();
//            int countEnding = pattern.endsWith(")")?1:0;
//            int lastCurlyOpen = pattern.lastIndexOf("{");
//            int lastCurlyClose = pattern.lastIndexOf("}");
//            if((countEnding == 1 && lastCurlyClose!=pattern.length()-2)||lastCurlyOpen<0){
//                lastCurlyOpen = pattern.length();
//            }
//
//            goatePattern.append(pattern).insert(countEnding,"(-!").insert(lastCurlyOpen+3,"!-)");
//            Pattern p = Pattern.compile(goatePattern.toString());
//            Matcher m = p.matcher(keyList.toString());
//            while (m.find()) {
//                String key = m.group().replace("-!","").replace("!-","");
//                filtered.put(key, getStrict(key));
//                //m = p.matcher(pattern);
//            }
        }
        return filtered;
    }

    /**
     * Simple filter, matches if key does start with the given pattern; ie excludes anything matching the pattern
     *
     * @param pattern The pattern to match
     * @return A Goate collection containing matching elements.
     */
    public Goate filterExclude(String pattern) {
        Goate filtered = new Goate();
        if (data != null) {
            for (String key : keys()) {
                if (!key.matches(pattern + ".*")) {
                    filtered.put(key, getStrict(key));
                }
            }
        }
        return filtered;
    }

    public Goate scrubKeys(String pattern) {
        Goate scrubbed = new Goate();
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                scrubbed.put(entry.getKey().replaceFirst(pattern, ""), entry.getValue());
            }
        }
        return scrubbed;
    }

    public Goate filterAndSplitKeyValuePairs(String filter) {
        return filterAndSplitKeyValuePairs(filter, ":=");
    }

    public Goate filterAndSplitKeyValuePairs(String filter, String split) {
        Goate filtered = new Goate();
        if (data != null) {
            Goate info = filter(filter);
            for (String key : info.keys()) {
                Object strictDef = info.getStrict(key);
                String def = "" + (strictDef == null ? "" : strictDef);
                if (!def.contains(split)) {
                    def = "" + processDSL(def);
                }
                if (def.contains(split)) {
                    String k = def.substring(0, def.indexOf(split));
                    String v = def.substring(def.indexOf(split) + split.length());
                    filtered.put("" + processDSL(k), processDSL(v));
                }
            }
        }
        return filtered;
    }

    public Goate merge(Goate merge, boolean replace) {
        if (merge != null) {
            Set<String> myKeys = keys();
            for (String key : merge.keys()) {
                if (replace) {
                    put(key, merge.getStrict(key));
                } else {
                    if (!myKeys.contains(key)) {
                        put(key, merge.getStrict(key));
                    }
                }
            }
        }
        return this;
    }

    public String toString() {
        return toString("", "");
    }

    public String toString(String prepadding, String postpadding) {
        StringBuilder sb = new StringBuilder("");
        boolean appendNewLine = false;
        for (String key : keys()) {
            if (appendNewLine) {
                sb.append("\n");
            }
            appendNewLine = true;
            sb.append(prepadding).append(key).append(":").append(data.get(key)).append(postpadding);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object check) {
        return compare(check) == 0;
    }

    private Object health = null;
    private Object keySet = null;

    private Goate keySet() {
        return (Goate) keySet;
    }

    public Goate healthCheck() {
        return (Goate) health;
    }

    public int compare(Object check) {
        boolean resetSet = true;
//        if(keySet == null){
        health = new Goate();
        keySet = new Goate();
//            resetSet = true;
//        }

        int result = size();
        if (!(check instanceof Goate)) {
            Goate castCheck = new ToGoate(check).convert();
            if (castCheck != null) {
                check = castCheck;
            }
        }
        if (check instanceof Goate) {
            Goate gCheck = (Goate) check;
            result = 0;
            for (String key : keys()) {
                boolean found = false;
                Object o = get(key);
                String keyPattern = key.replaceAll("[0-9]+", "[0-9]+").replace(".", "\\.");
                Set<String> keySet = keySet().get(keyPattern, null, Set.class);
                if (keySet == null) {
                    keySet = gCheck.filterStrict(keyPattern).keys();
                    keySet().put(keyPattern, keySet);
                }
                for (String checkKey : keySet) {
                    if (new Compare(o).to(gCheck.get(checkKey)).using("==").evaluate()) {
                        found = true;
                        break;
                    } else {
                        healthCheck().put("not equal##", checkKey + ": " + o + "!=" + gCheck.get(checkKey));
                    }
                }
                if (!found) {
                    result++;
                    healthCheck().put("not found##", key + ": " + o);
                }
            }
        }
        if (resetSet) {
            keySet = null;
        }

        return result;
    }
}
