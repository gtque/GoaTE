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
package com.thegoate.utils.fill.serialize;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import com.thegoate.utils.togoate.ToGoate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eric Angeli on 7/10/2018.
 */
public class Cereal {
    protected BleatBox LOG = BleatFactory.getLogger(getClass());
    protected final String FLATTEN = ":!:";
//    protected boolean allSources = false;
//    protected Class dataSource;
//
//    public Cereal from(Class dataSource){
//        this.dataSource = dataSource;
//        return this;
//    }

    protected Object doCast(Object o, Class castTo) throws InstantiationException, IllegalAccessException {
        return new Cast(new ToGoate(o).convert(), o.getClass()).cast(o, castTo);
    }

    protected Object doCast(Object o, Class klass, Class castTo) throws InstantiationException, IllegalAccessException {
        return new Cast(new ToGoate(o).convert(), klass).cast(o, castTo);
    }

    public static GoateSource findGoateSource(Field field, Class dataSource) {
        GoateSource[] annotations = field.getAnnotationsByType(GoateSource.class);
        GoateSource gs = null;
        if (dataSource != null) {
            for (GoateSource source : annotations) {
                if (source.source().equals(dataSource)) {
                    gs = source;
                    break;
                }
            }
        }
        return gs;
    }

    private GoateSource defineDefaultSource(String fieldName, boolean flatten) {
        return new GoateSource() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return GoateSource.class;
            }

            @Override
            public Class source() {
                return DefaultSource.class;
            }

            @Override
            public String key() {
                return fieldName;
            }

            @Override
            public boolean flatten() {
                return flatten;
            }

            @Override
            public Class serializeTo() {
                return GoateSource.class;
            }

            @Override
            public boolean skipInModel() {
                return false;
            }

            @Override
            public int priority() {
                return Integer.MAX_VALUE;
            }
        };
    }

    protected boolean checkSourceType(Class source, Class sourceType) {
        return sourceType == AllOrderedSource.class || source == sourceType;
    }

    public List<String> sourcePrefixList(Class type, Class sourceType, boolean isEutConfig) {
        List<String> sources = new ArrayList<>();
        GoateSource[] sourceLabels = (GoateSource[])type.getAnnotationsByType(GoateSource.class);
        GoateSourceNode head = sortedSources(new GoateSourceNode(), sourceLabels, sourceType, isEutConfig?type.getSimpleName():"", !isEutConfig);
        while (head != null) {
            String prefix = head.getTheSource().flatten() ? "" : head.getTheSource().key();
            if (!sources.contains(prefix)) {
                sources.add(prefix);
            }
            head = head.getNext();
        }
        return sources;
    }

    protected GoateSourceNode sortedSources(GoateSourceNode head, GoateSource[] sources, Class sourceType, String name, boolean flatten) {
        Arrays.stream(sources).filter(source -> checkSourceType(source.source(), sourceType)).forEach(source -> head.addSource(source));
        head.addSource(defineDefaultSource(name, flatten));
        return head;
    }

    public List<String> getAlternateNames(Field field, String name, List<String> sources, Class sourceType) {
        List<String> alternate = new ArrayList<>();
        GoateSource[] altProperties = field.getAnnotationsByType(GoateSource.class);
        GoateSourceNode head = sortedSources(new GoateSourceNode(), altProperties, sourceType, name, false);
        while (head != null) {
            String base = head.getTheSource().key() + FLATTEN + head.getTheSource().flatten();
            //base2 is recommended to be used for setting as environment properties and is all uppercase with - and . replaced with _
            //example: if original is hello-world.greeting then use HELLO_WORLD_GREETING as the property name for the environment variable.
            //although technically it HELLO_WORLD_GREETING would also work if used in the properties file, it is recommended to only use
            //that pattern when setting it through environment variables to help preserve your sanity.
            String base2 = base.toUpperCase().replace("-", "_").replace(".", "_");
            String base3 = base.toLowerCase().replace("_", ".");
            if (head.getTheSource().flatten()) {
                if(sourceType == AllOrderedSource.class) {
                    if (!alternate.contains(base3)) {
                        alternate.add(base3);
                    }
                }
                if (!alternate.contains(base)) {
                    alternate.add(base);
                }
                if(sourceType == AllOrderedSource.class) {
                    if (!alternate.contains(base2)) {
                        alternate.add(base2);
                    }
                }
            } else {
                sources.stream().forEach(source -> addAlternates(alternate, source, base, base2, sourceType == AllOrderedSource.class));
            }
            head = head.getNext();
        }
        return alternate;
    }

    private void addAlternates(List<String> alternates, String prefix, String base, String base2, boolean fullSet) {
        String base3 = base.toLowerCase().replace("_", ".");
        String full1 = prefix + (prefix.isEmpty() ? "" : ".") + base;
        String full3 = prefix.toLowerCase() + (prefix.isEmpty() ? "" : ".") + base3;
        String prefix2 = prefix.toUpperCase().toUpperCase().replace("-", "_").replace(".", "_");
        String full2 = prefix2 + (prefix2.isEmpty() ? "" : "_") + base2;
        if(fullSet) {
            if (!alternates.contains(full3)) {
                alternates.add(full3);
            }
        }
        if (!alternates.contains(full1)) {
            alternates.add(full1);
        }
        if(fullSet) {
            if (!alternates.contains(full2)) {
                alternates.add(full2);
            }
        }
    }

    protected boolean checkNotPrimitive(Class type) {
        GoateReflection reflection = new GoateReflection();
        return !(reflection.isPrimitive(type) || (type.equals(String.class) || reflection.classIsNumber(type)));
    }
}
