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

import com.thegoate.annotations.AnnotationDNA;
import com.thegoate.dsl.words.EutConfigDSL;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.fill.serialize.GoateSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric Angeli on 7/12/2019.
 */
public class DNA {

    public static final DNA dna = new DNA();

    public AnnotationDNA annotations;

    protected final Map<String, Object> yml;

    public static void reset() {
        EutConfigDSL.clear();
    }

    protected DNA() {
        String theConfig = readFile("goate.yml");
        if (theConfig != null) {
            yml = parse(tokenize(theConfig));
            annotations = analyzeDna(new AnnotationDNA(), yml, "");
        } else {
            yml = null;
        }
    }

    protected <T> T analyzeDna(T mosquito, Map<String, Object> yml, String primaryKey) {
        GoateSource primary = mosquito.getClass().getAnnotation(GoateSource.class);
        String primaryKey1 = mosquito.getClass().getSimpleName().toLowerCase();
        if (primary != null) {
            primaryKey1 = primary.key().toLowerCase();
        }
        primaryKey = primaryKey.isEmpty() ? primaryKey1 : (primaryKey1.isEmpty() ? primaryKey : (primaryKey + "." + primaryKey1));
        String primaryKeyAlternate = primaryKey.toUpperCase().replace(".", "_");
        final String pKey = primaryKey;
        Map<String, Field> fields = new GoateReflection().findFields(mosquito.getClass());
        fields.forEach((name, field) -> {
            GoateSource source = field.getAnnotation(GoateSource.class);
            String subKey = name;
            if (source != null) {
                subKey = source.key();
            }
            String theKey = pKey + (pKey.isEmpty() ? "" : (subKey.isEmpty() ? "" : ".")) + subKey;
            String theAlternateKey = primaryKeyAlternate + (primaryKeyAlternate.isEmpty() ? "" : "_") + subKey.toUpperCase();
            Object theValue = System.getProperty(theAlternateKey);
            boolean inYaml = false;
            if (theValue == null) {
                theValue = System.getenv(theAlternateKey);
                if (theValue == null) {
                    if (yml.containsKey(theKey)) {
                        theValue = yml.get(theKey);
                        inYaml = true;
                    } else if (yml.containsKey(theAlternateKey)) {
                        theValue = yml.get(theAlternateKey);
                        inYaml = true;
                    } else {
                        inYaml = !yml.keySet().stream().filter(key -> key.startsWith(theKey) || key.startsWith(theAlternateKey)).toList().isEmpty();
                    }
                }
            }
            if (theValue != null || inYaml) {
                if ("null::".equals(theValue)) {
                    theValue = null;
                }

                if (new GoateReflection().isPrimitive(field.getType()) || field.getType().equals(String.class)) {
                    try {
                        field.set(mosquito, new GoateReflection().parseToPrimitive(theValue, field.getType()));
                    } catch (IllegalAccessException e) {
                        //do nothing
                    }
                } else {
                    try {
                        Object[] params = {};
                        Class<?>[] paramTypes = {};
                        theValue = field.getType().getConstructor(paramTypes).newInstance(params);
                        field.set(mosquito, theValue);
                        analyzeDna(theValue, yml, theKey);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                             NoSuchMethodException e) {
                        //do nothing.
                    }
                }
            }
        });
        return mosquito;
    }

    /**
     * This uses a custom yaml parser to avoid including any third party libraries.
     * This should only be used by GoaTE for loading the goate.yml file,
     * of which there should only one in a given project.
     * If one or more goate.yml files is included in the class path,
     * I cannot guarantee which one will be loaded.
     */
    private Map<String, Object> parse(List<String> tokens) {
        Map<String, Object> result = new HashMap<>();
        int indentLevel = 0;
        String levelKey = "";

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.equals("INDENT")) {
                indentLevel++;
            } else {
                String[] mappedKey = {""};
                Arrays.stream(levelKey.split("\\.")).limit(indentLevel).forEach(k -> mappedKey[0] += k + ".");
                if (indentLevel == 0) {
                    levelKey = "";
                }
                levelKey = mappedKey[0];
                if (token.startsWith("KEY:")) {
                    String key = token.substring(4);
                    String valueToken = tokens.get(++i);

                    if (valueToken.startsWith("VALUE:")) {
                        if (valueToken.equals("VALUE:")) {
                            levelKey += key;
                        } else {
                            String value = valueToken.substring(6);
                            result.put(levelKey + key, value);
                        }
                    } else if (valueToken.startsWith("LIST_ITEM:")) {
                        //I am leaving this here, although there is no current use case for it, and it has not been tested
                        //or verified to be correct.
                        List<String> list = new ArrayList<>();
                        list.add(valueToken.substring(10));
                        while (i + 1 < tokens.size() && tokens.get(i + 1).startsWith("LIST_ITEM:")) {
                            list.add(tokens.get(++i).substring(10));
                        }
                    }
                    if (indentLevel > 0) {
                        indentLevel = 0;
                    }
                }
            }
        }
        return result;
    }

    private List<String> tokenize(String yamlContent) {
        List<String> tokens = new ArrayList<>();
        if (yamlContent != null) {
            Pattern pattern = Pattern.compile("(\\w+):[\\t ]*([^\\n\\r]*)|-\\s*(.*)| {2}");
            Matcher matcher = pattern.matcher(yamlContent);

            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    tokens.add("KEY:" + matcher.group(1));
                    tokens.add("VALUE:" + matcher.group(2));
                } else if (matcher.group(2) != null) {
                    tokens.add("LIST_ITEM:" + matcher.group(2));
                } else if (matcher.group(0).equals("  ")) {
                    tokens.add("INDENT");
                }
            }
        }
        return tokens;
    }

    private String readFile(String filePath) {
        try {
            return readFromInputStream(getClass().getClassLoader().getResource(filePath).openStream());
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}