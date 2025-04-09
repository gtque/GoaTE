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

import com.thegoate.logging.Bleat;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.reflection.GoateReflection;

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

    public static final Map<String, Map<String, Class<?>>> directory = new ConcurrentHashMap<>();
    protected BleatBox LOG;

    public AnnotationFactory() {
        AnnotationScanner.getAnnotations();
        LOG = BleatFactory.getLogger(AnnotationFactory.class);
    }

    public AnnotationFactory(Class<?> klass) {
        if (klass.equals(Bleat.class)) {
            LOG = null;
        } else {
            AnnotationScanner.getAnnotations();
            LOG = BleatFactory.getLogger(AnnotationFactory.class);
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

    public Map<String, Class<?>> getDirectory(String dir) {
        return getDirectory(dir, null, null);
    }

    public Map<String, Class<?>> getDirectory(String dir, String id, Method identifier) {
        buildDirectory(dir);
        Map<String, Class<?>> unfiltered = directory.get(dir);
        Map<String, Class<?>> filtered = new ConcurrentHashMap<>();
        if (id != null && identifier != null) {
            if (directory.containsKey(dir + ":" + id + ":" + identifier.getName())) {
                filtered = directory.get(dir + ":" + id + ":" + identifier.getName());
            } else {
                for (String key : unfiltered.keySet()) {
                    try {
                        Class temp = unfiltered.get(key);
                        Annotation service = temp.getAnnotation(annotation);
                        if (identifier != null) {
                            Object theCheck = identifier.invoke(service);
                            if (theCheck != null && theCheck.equals(id)) {
                                LOG.debug("adding to filtered list: " + temp.getCanonicalName());
                                Class typeClass = addToDefaults(filtered, temp, service);
                                String filterKey = typeClass != null ? typeClass.getCanonicalName() : key;
                                filtered.put(filterKey, temp);
                            }
                        }
                    } catch (Exception e) {
                        LOG.error("Problem checking the class: " + e.getMessage(), e);
                    }
                }
                directory.put(dir + ":" + id + ":" + identifier.getName(), filtered);
            }
//            LOG.debug("directory: (" + dir + ":" + id + ":" + identifier.getName() + ") = " + filtered.size());
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

    public Class<?> lookUp() {
        buildDirectory();
        Class<?> c = null;
        LOG.debug("looking for " + annotation.getName());
        String theClass = "" + id;
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
     *
     * @return
     */
    public synchronized AnnotationFactory clearDirectory() {
        directory.remove(annotation.getCanonicalName());
        return this;
    }

    public String getDefaultName(Class type) {
        return "default: " + (type == null ? "null" : type.getName());
    }

    public String listingName(Class listing, Class type) {
        return listingName(listing.getCanonicalName(), type);
    }

    public String listingName(String listing, Class type) {
        return listing;// + (type != null ? (":" + type.getCanonicalName()) : "");
    }

    /**
     * The listings for each annotation is generated only one time, the first time they are looked up.
     *
     * @return The instance of itself, syntactic sugar for stringing calls together.
     */
    public synchronized AnnotationFactory buildDirectory() {
        return buildDirectory(annotation.getCanonicalName());
    }

    public AnnotationFactory buildDirectory(String dir) {
        synchronized (directory) {
            if (!directory.containsKey(dir)) {
                directory.put(dir, new ConcurrentHashMap<>());
            }
            Map<String, Class<?>> listing = directory.get(dir);
            Map<String, List<Class<?>>> scans = AnnotationScanner.getAnnotations();
            //this is the best place to log errors scanning because BleatBox implementations require annotation scanning to be complete
            //and getAnnotations is synchronized so it can be there otherwise you will end up blocked forever.
            if (LOG != null) {
                String scanErrors = GoateScanner.getErrors();
                if (scanErrors != null && !scanErrors.isEmpty()) {
                    LOG.warn("There was a problem found while scanning for classes, this may cause some unforeseen issues if the classes in question are necessary.\n%s".formatted(scanErrors));
                }
            }
            if (listing.isEmpty() && scans.containsKey(annotation.getName())) {
                if (LOG != null) {
                    LOG.debug("Building Directory " + dir, "the listing was empty, trying to build it.");
                }
                List<Class<?>> klasses = scans.get(annotation.getName());
//                Iterable<Class<?>> klasses = ClassIndex.getAnnotated(annotation);
                int count = 0;
                for (Class<?> klass : klasses) {
                    count++;
                    String theClass = klass.getCanonicalName();
                    if (LOG != null) {
                        LOG.debug("Adding to directory", theClass);
                    }
                    try {
//                        Class temp = Class.forName(theClass);
                        Annotation service = klass.getAnnotation(annotation);
//                    String aid = theClass;
                        Class<?> typeClass = null;
                        typeClass = addToDefaults(listing, klass, service);
                        if (check != null) {
                            Object theCheck = check.invoke(service);
                            if (theCheck != null && theCheck.getClass().isArray()) {
                                listing.put("" + theClass, klass);
                                for (Object aido : (Object[]) theCheck) {
                                    listing.put(listingName("" + aido, typeClass), klass);
                                }
                            } else {
                                listing.put(listingName("" + theCheck, typeClass), klass);
                            }
                        } else {
                            listing.put(listingName(klass, typeClass), klass);//default to using the full class name.
                        }
                    } catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
                        if (LOG != null) {
                            LOG.error("could not get the class: " + theClass + "; " + e.getMessage(), e);
                        }
                    } catch (NoClassDefFoundError ncdfe) {
                        if (LOG != null) {
                            LOG.error("Build Directory", "This shouldn't have happened, but a class in the list was not found." + ncdfe.getMessage(), ncdfe);
                        }
                    }
                }
                if (LOG != null) {
                    LOG.debug("Build Directory", "Directory: " + dir + ";Classes in directory: " + count);
                }
            } else {
                if (LOG != null) {
//                LOG.debug("Build Directory", "directory already defined: " + listing.size());
                }
            }
//            if(!annotation.equals(Bleat.class)) {
//                Map<String, List<Class<?>>> annotationCache = AnnotationScanner.getAnnotations();
//                int i = 0;
//            }
        }
        return this;
    }

    private Class<?> addToDefaults(Map<String, Class<?>> listing, Class<?> klass, Annotation service) {
        IsDefault def = (IsDefault) klass.getAnnotation(IsDefault.class);
        Class<?> typeClass = null;
        try {
            Object[] args = {};
            Method type = service.getClass().getMethod("type");
            Object typeCheck = type.invoke(service, args);
            if (typeCheck instanceof Class) {
                typeClass = (Class<?>) typeCheck;
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            if (LOG != null) {
                LOG.debug("annotation does not have the 'type' field");
            }
        }
        if (setDefault) {
            if (def != null) {
                if (def.forType()) {
                    if (typeClass != null) {
                        listing.put(getDefaultName(typeClass), klass);
                    } else {
                        if (LOG != null) {
                            LOG.debug("not storing a default by type for: " + klass.getCanonicalName());
                        }
                    }
//								listing.put("default", klass);
//                                if (LOG != null) {
//                                    LOG.debug("Build Directory", "can't track defaults for specific types here, this is only for generic default for all.");
//                                }
                } else {
                    if (!listing.containsKey("default")) {
                        listing.put("default", klass);
                    }
                }
            }
        }
        return typeClass;
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
                    if (constructor == null) {
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
//                            o = constructor.getDeclaredConstructor().newInstance();
//                        }
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        LOG.debug("Building Class", "Problem instantiating a new instances: " + e.getMessage(), e);
                        throw e;
                    }
                } else {
                    LOG.info("Building Class", "Could not find the constructor, wil check for a default constructor...");

                }
            } else {
                try {
                    o = klass.getDeclaredConstructor().newInstance();
                } catch (NoSuchMethodException e) {
                    LOG.error("Build Class", "Problem instantiating new instance: " + e.getMessage(), e);
                }
            }
        } else {
            LOG.debug("Building Class", "The class was not specified, definitely could not build it.");
        }
        return o;
    }

    public Class lookUpByAnnotatedMethod() {
        buildDirectory(annotation.getCanonicalName());
        LOG.debug("Look Up Annotated Method", "Trying to look up an annotated method: " + methodId);
        Class<?> klass = null;
        Map<String, Class<?>> listings = directory.get(annotation.getCanonicalName());
        for (String theClass : listings.keySet()) {
            LOG.debug("Checking Class", theClass);
            List<Method> methods = new GoateReflection().getDeclaredMethods(listings.get(theClass));
            LOG.debug("Methods to check (" + methods.size() + ")", methods.toString());
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
        buildDirectory(annotation.getCanonicalName());
        Map<String, Class<?>> listings = directory.get(annotation.getCanonicalName());
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
