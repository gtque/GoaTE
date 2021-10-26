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

package com.thegoate.annotations;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;
import org.atteo.classindex.ClassIndex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple factory that can be used to lookup classes that are annotated.
 * Created by gtque on 4/24/2017.
 */
public class AnnotationFactory {

    protected static final BleatBox LOG = BleatFactory.getLogger(AnnotationFactory.class);

    public static volatile Map<String, Map<String, Class>> directory = new ConcurrentHashMap<>();

    public AnnotationFactory() {
        if (directory == null) {
            directory = new ConcurrentHashMap<>();
        }
    }

    Object id;
    String methodId;
    Constructor constructor = null;
    Object[] constructorArgs = new Object[0];
    Method check = null;
    Class<? extends java.lang.annotation.Annotation> annotation;
    Class<? extends java.lang.annotation.Annotation> methodAnnotation;
    boolean setDefault = false;
    String checkName = null;

    public AnnotationFactory clear() {
        id = null;
        methodId = null;
        constructorArgs = new Object[0];
        constructor = null;
        check = null;
        annotation = null;
        methodAnnotation = null;
        setDefault = false;
        return this;
    }

    public AnnotationFactory doDefault() {
        setDefault = true;
        return this;
    }

    public AnnotationFactory findByMethod(String id) {
        this.methodId = id;
        return this;
    }

    public AnnotationFactory find(Object id) {
        this.id = id;
        return this;
    }

    public AnnotationFactory buildUsing(Constructor constructor) {
        this.constructor = constructor;
        return this;
    }

    public AnnotationFactory using(String check) {
        if (annotation != null) {
            try {
                using(annotation.getMethod(check));
            } catch (NoSuchMethodException e) {
                LOG.error("could not find the identifying method: " + check);
            }
        }
        this.checkName = check;
        return this;
    }

    public AnnotationFactory using(Method check) {
        this.check = check;
        return this;
    }

    public AnnotationFactory annotatedWith(Class<? extends java.lang.annotation.Annotation> annotation) {
        this.annotation = annotation;
        if (checkName != null) {
            using(checkName);
        }
        return this;
    }

    public Map<String, Class> getDirectory(String dir) {
        return getDirectory(dir, null, null);
    }

    public Map<String, Class> getDirectory(String dir, String id, Method identifier) {
        buildDirectory();
        Map<String, Class> unfiltered = directory.get(dir);
        Map<String, Class> filtered = new ConcurrentHashMap<>();
        if (id != null && identifier != null) {
            for (String key : unfiltered.keySet()) {
                try {
                    Class temp = unfiltered.get(key);
                    Annotation service = temp.getAnnotation(annotation);
                    if (identifier != null) {
                        Object theCheck = identifier.invoke(service);
                        if (theCheck != null && theCheck.equals(id)) {
                            filtered.put(key, temp);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Problem checking the class: " + e.getMessage(), e);
                }
            }
        }
        return (id != null && identifier != null) ? filtered : unfiltered;
    }

    public AnnotationFactory methodAnnotatedWith(Class<? extends java.lang.annotation.Annotation> annotation) {
        this.methodAnnotation = annotation;
        return this;
    }

    public AnnotationFactory constructorArgs(Object[] args) {
        this.constructorArgs = args;
        return this;
    }

    public Class lookUp() {
        buildDirectory();
        Class c = null;
        LOG.debug("looking for " + annotation.getName());
        String theClass = ""+id;
        try {
            c = directory.get(annotation.getCanonicalName()).get(theClass);
        } catch (NullPointerException e) {
            LOG.error("could not get the class: " + theClass + "; " + e.getMessage(), e);
        }
        return c;
    }

    public Object build() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        buildDirectory();
        Class c = methodId == null ? lookUp() : lookUpByAnnotatedMethod();
        return build(c);
    }

    /**
     * Clear the built directory. Use to force the directory to be rebuilt.
     * @return
     */
    public synchronized AnnotationFactory clearDirectory(){
        directory.remove(annotation.getCanonicalName());
        return this;
    }

    /**
     * The listings for each annotation is generated only one time, the first time they are looked up.
     *
     * @return The instance of itself, syntactic sugar for stringing calls together.
     */
    public synchronized AnnotationFactory buildDirectory() {
        if (!directory.containsKey(annotation.getCanonicalName())) {
            directory.put(annotation.getCanonicalName(), new ConcurrentHashMap<>());
        }
        Map<String, Class> listing = directory.get(annotation.getCanonicalName());
        if (listing.size() == 0) {
            if(LOG!=null) {
                LOG.debug("Building Directory "+annotation.getCanonicalName(), "the listing was empty, trying to build it.");
            }
            Iterable<Class<?>> klasses = ClassIndex.getAnnotated(annotation);
            for (Class<?> klass : klasses) {
                String theClass = klass.getCanonicalName();
                if(LOG!=null) {
                    LOG.debug("Adding to directory", theClass);
                }
                try {
                    Class temp = Class.forName(theClass);
                    Annotation service = temp.getAnnotation(annotation);
                    String aid = theClass;
                    if (check != null) {
                        Object theCheck = check.invoke(service);
                        if (theCheck != null && theCheck.getClass().isArray()) {
                            for (Object aido : (Object[]) theCheck) {
                                listing.put("" + aido, klass.forName(theClass));
                            }
                        } else {
                            listing.put("" + theCheck, klass.forName(theClass));
                        }
                    } else {
                        listing.put(temp.getCanonicalName(), klass.forName(theClass));//default to using the full class name.
                    }
                    if (setDefault) {
                        Annotation def = temp.getAnnotation(IsDefault.class);
                        if (def != null) {
                            listing.put("default", temp);
                        }
                    }
                } catch (ClassNotFoundException | NullPointerException | IllegalAccessException | InvocationTargetException e) {
                    LOG.error("could not get the class: " + theClass + "; " + e.getMessage(), e);
                } catch (NoClassDefFoundError ncdfe) {
                    LOG.error("Build Directory", "This shouldn't have happened, but a class in the list was not found." + ncdfe.getMessage(), ncdfe);
                }
            }
        }
        return this;
    }

    public AnnotationFactory constructor(Constructor constructor) {
        this.constructor = constructor;
        return this;
    }

    public Object build(Class klass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object o = null;
        if (klass != null) {
            if (constructorArgs != null) {
                Object[] ca = constructorArgs;
                if (constructor == null) {
                    constructor = new GoateReflection().findConstructor(klass.getConstructors(), constructorArgs);
                    if(constructor == null){
//                        LOG.debug("Build Class", "didn't find specific constructor, checking for default constructor");
                        constructor = new GoateReflection().findConstructor(klass.getConstructors(), new Object[0]);
                        ca = new Object[0];
                    }
                }
                if (constructor != null) {
                    try {
//                        if(ca!=null) {
                            o = constructor.newInstance(ca);
//                        } else {
//                            o = constructor.newInstance();
//                        }
                    }catch(IllegalAccessException | InstantiationException | InvocationTargetException e){
                        LOG.debug("Building Class", "Problem instantiating a new instances: " + e.getMessage(), e);
                        throw e;
                    }
                } else {
                    LOG.info("Building Class", "Could not find the constructor, wil check for a default constructor...");

                }
            } else {
                o = klass.newInstance();
            }
        } else {
            LOG.debug("Building Class", "The class was not specified, definitely could not build it.");
        }
        return o;
    }

    public Class lookUpByAnnotatedMethod() {
        buildDirectory();
        LOG.debug("Look Up Annotated Method", "Trying to look up an annotated method: " + methodId);
        Class klass = null;
        Map<String, Class> listings = directory.get(annotation.getCanonicalName());
        for (String theClass : listings.keySet()) {
            LOG.debug("Checking Class", theClass);
            List<Method> methods = new GoateReflection().getDeclaredMethods(listings.get(theClass));
            LOG.debug("Methods to check ("+methods.size()+")", methods.toString());
            for (Method m : methods) {
                if (m.isAnnotationPresent(methodAnnotation)) {
                    for (Method am : methodAnnotation.getDeclaredMethods()) {
                        Annotation dam = m.getAnnotation(methodAnnotation);
                        try {
                            LOG.debug("Checking Method", am.getName());
                            if (methodId.equals(am.invoke(dam))) {
                                klass = listings.get(theClass);
                                break;
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            LOG.debug("Look Up Annotated Method", "Problem accessing method: " + e.getMessage(), e);
                        }
                    }
                    if (klass != null) {
                        break;
                    }
                }
            }
        }
        return klass;
    }

    public Method getMethod() {
        buildDirectory();
        Map<String, Class> listings = directory.get(annotation.getCanonicalName());
        Method method = null;
        for (String theClass : listings.keySet()) {
            List<Method> methods = new GoateReflection().getDeclaredMethods(listings.get(theClass));
            for (Method m : methods) {
                if (m.isAnnotationPresent(methodAnnotation)) {
                    for (Method am : methodAnnotation.getDeclaredMethods()) {
                        Annotation dam = m.getAnnotation(methodAnnotation);
                        try {
                            if (methodId.equals(am.invoke(dam))) {
                                method = m;
                                break;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    if (method != null) {
                        break;
                    }
                }
            }
        }
        return method;
    }
}
