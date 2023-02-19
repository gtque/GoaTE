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

package com.thegoate.utils;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.annotations.IsDefault;
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.get.NotFound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * Base Class for staff. Determines the type of util to use.
 * The util(s) should have a method called isType that checks to see if the input
 * is of the type that service can process. All generic utility classes should extend this class
 * and call buildUtil in the constructor.
 * Created by gtque on 5/4/2017.
 */
@UtilCache
public abstract class UnknownUtilType<T extends UnknownUtilType> implements Utility {
    protected final BleatBox LOG = BleatFactory.getLogger(getClass());
    protected Goate health = new Goate();
    protected Goate data = null;
    protected static Goate pokedex = new Goate();
    protected String region = "kanto";
    protected boolean resetCache = false;
    protected boolean useCache = false;
    protected boolean nameIsHash = false;
    protected Class declaredType = null;

    public UnknownUtilType() {
        UtilCache uc = getClass().getAnnotation(UtilCache.class);
        if (uc != null) {
            region = uc.name();
            resetCache = uc.clear();
            useCache = uc.useCache();
        }
    }

    public T type(Class type) {
        this.declaredType = type;
        return (T) this;
    }

    @Override
    public boolean isType(Object check) {
        return false;
    }

    @Override
    public UnknownUtilType setData(Goate data) {
        this.data = data;
        return this;
    }

    public Goate healthCheck() {
        return health;
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util) {
        return buildUtil(obj, util, obj);
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, Object val) {
        return buildUtil(obj, util, val, null, null);
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, String id, Method identifier) {
        return buildUtil(obj, util, obj, id, identifier);
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, String id, Method identifier, String isType) {
        return buildUtil(obj, util, obj, id, identifier, isType);
    }

    /**
     * Attempts to find and build the correct implementation of the utility.
     *
     * @param obj        the obj
     * @param util       the type of utility
     * @param val        the value to be operated on
     * @param id         the id of the utility to use
     * @param identifier the method to use to identify the utility
     * @return the utility that was found.
     */
    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, Object val, String id, Method identifier) {
        return buildUtil(obj, util, val, id, identifier, "isType");
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, Object val, String id, Method identifier, Class type) {
        return buildUtil(obj, util, val, id, identifier, "isType", type);
    }

    /**
     * Attempts to find and build the correct implementation of the utility.
     *
     * @param obj        the obj
     * @param util       the type of utility
     * @param val        the value to be operated on
     * @param id         the id of the utility to use
     * @param identifier the method to use to identify the utility
     * @param isType     the type to look for.
     * @return the utility that was found.
     */
    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, Object val, String id, Method identifier, String isType) {
        return buildUtil(obj, util, val, id, identifier, isType, declaredType == null ? NotFound.class : declaredType);
    }

    protected Object buildUtil(Object obj, Class<? extends java.lang.annotation.Annotation> util, Object val, String id, Method identifier, String isType, Class type) {
        Object utility = null;
        Object[] args = {val};
        Object[] checkArgs = {obj};
        utility = cached(util, args, obj, id, type);
        if (utility == null) {
            utility = uncached(util, args, checkArgs, id, identifier, isType, type);
            if (utility != null) {
                if (obj != null && useCache) {// && !(obj instanceof String && type != String.class)) {
                    Goate cache = pokedex.get(region, new Goate(), Goate.class);
                    if (cache != null) {
                        cache.put(key(util, obj, id, type), utility.getClass());
                    } else {
                        cache = new Goate().put(key(util, obj, id, type), utility.getClass());
                        pokedex.put(region, cache);
                    }
                } else {
                    LOG.debug("buildUtil", "seems like cache was not enabled.");
                }
            } else {
                LOG.debug("buildUtil", "could not find an uncached util implementation." + key(util, obj, id, type));
            }
        }
        if(utility instanceof NotFound){
            utility = null;
        }
        return utility;
    }

    public static void clearCache(Class<? extends UnknownUtilType> klass) {
        clearCache(klass, null);
    }

    public static void clearCache(Class<? extends UnknownUtilType> klass, Class annotation){
        UtilCache uc = klass.getAnnotation(UtilCache.class);
        if (uc.clear()) {
            if(annotation != null) {
                new AnnotationFactory().annotatedWith(annotation).clearDirectory();
            }
            pokedex.put(uc.name(), new Goate());
        }
    }

    public void clearFromCache(Class<? extends java.lang.annotation.Annotation> util, Object obj, String id, Class isType) {
        pokedex.drop(key(util, obj, id, isType));
    }

    protected String key(Class<? extends java.lang.annotation.Annotation> util, Object obj, String id, Class isType) {
        return (util.getName() + ":" + getName(obj, isType) + ":" + id + ":" + isType).toLowerCase();
    }

    protected String getName(Object obj, Class isType) {
        String name = null;
        if (nameIsHash) {
            name = obj != null ? ("" + obj.hashCode()) : null;
        } else {
//            GoateReflection gr = new GoateReflection();
            if (isType != null) {
                name = isType.getTypeName();
            }
        }
        return name;
    }

    protected Object cached(Class<? extends java.lang.annotation.Annotation> util, Object[] args, Object obj, String id, Class isType) {
        Object utility = null;
        if (useCache) {
            Goate cache = pokedex.get(region, null, Goate.class);
            if (obj != null && cache != null) {
                String _key = key(util, obj, id, isType);
                Class c = cache.get(_key, null, Class.class);
                if (c != null) {
                    LOG.debug("util cache", "found a cached implementation: " + _key);
                    AnnotationFactory af = new AnnotationFactory();
                    af.constructorArgs(args);
                    try {
                        utility = af.constructor(null).build(c);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        LOG.debug("The class (" + c.getName() + ") could not be build from the util cache for some reason: " + e.getMessage(), e);
                    }
                } else {
                    LOG.debug("util cache", "not found in cache: " + _key);
                }
            }
        }
        return utility;
    }

    protected Class getDefault(String isType, Class c) {
        Method check = null;
        Class def = null;
        Class[] types = {Object.class};
        try {
            check = c.getMethod(isType, types);
        } catch (NoSuchMethodException nsme) {
            LOG.debug("Unknown Util", "No type method: " + isType);
        }
        if (check != null) {
            def = c;
        }
        return def;
    }

    protected boolean isDefault(Class c, Class def) {
        IsDefault d = (IsDefault) c.getAnnotation(IsDefault.class);
        return d != null && !d.forType() && def == null;
    }

    protected Object getUtility(Class c, Class[] _def, AnnotationFactory af, String isType, Class type, Object[] checkArgs) {
        Object utility = null;
        Class[] types = {Object.class};
        Method check = null;
        IsDefault d = (IsDefault) c.getAnnotation(IsDefault.class);
        try {
            if (type != null && type.equals(NotFound.class)) {
                try {
                    check = c.getMethod(isType, types);
                } catch (NoSuchMethodException nsme) {
                    LOG.debug("Unknown Util", "No type method: " + isType);
                }
                Object u = af.constructor(null).build(c);
                if (check != null && Boolean.parseBoolean("" + check.invoke(u, checkArgs))) {
                    if (d != null && d.forType()) {
                        _def[0] = c;
                    } else {
                        utility = u;
//                    break;
                    }
                } else {
                    LOG.debug("Util Look up by type", "not: " + c.getName());
                }
            } else {
                if (checkType(c, type)) {
                    if (d != null && d.forType()) {
                        _def[0] = c;
                    } else {
                        utility = af.constructor(null).build(c);
//                    break;
                    }
                } else {
                    LOG.debug("Util Look up by checkType", "not: " + c.getName());
                }
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOG.debug("The class (" + c.getName() + ") did not have isType, cannot determine if that class is the correct type. " + e.getMessage(), e);
        }
        return utility;
    }

    protected boolean checkUtility(Class c, Class[] _def, String isType, AnnotationFactory af, Class type, Object[] checkArgs) {
        Object util = null;
        if (isDefault(c, _def[0])) {
            _def[0] = getDefault(isType, c);
        } else {
            util = getUtility(c, _def, af, isType, type, checkArgs);
        }
        return util != null;
    }

    protected Object uncached(Class<? extends java.lang.annotation.Annotation> util, Object[] args, Object[] checkArgs, String id, Method identifier, String isType, Class type) {
        Object utility = null;
        Class def = null;
        AnnotationFactory af = new AnnotationFactory();
        af.constructorArgs(args);
        Map<String, Class> utils = af.annotatedWith(util).getDirectory(util.getCanonicalName(), id, identifier);
        if (utils != null) {
            final Class[] _def = new Class[1];
//            final Object[] _util = new Object[1];
            _def[0] = null;
//            _util[0] = null;

            Optional<String> opt = utils.keySet().stream().filter(key -> checkUtility(utils.get(key), _def, isType, af, type, checkArgs)).findFirst();
            if(opt.isPresent()) {
                try {
                    utility = af.constructor(null).build(utils.get(opt.get()));//af.constructor(null).build(c);;
                } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                    LOG.warn("uncached", "problem building utility: " + e.getMessage(), e);
                }
            } else {
                LOG.debug("uncached", "failed to find the utility");
            }
            def = _def[0];
            /**
             for (String key : utils.keySet()) {
             Class c = Object.class;
             try {
             c = utils.get(key);
             IsDefault d = (IsDefault) c.getAnnotation(IsDefault.class);
             Class[] types = {Object.class};
             Method check = null;
             if (d != null && !d.forType() && def == null) {
             try {
             check = c.getMethod(isType, types);
             } catch (NoSuchMethodException nsme) {
             LOG.debug("Unknown Util", "No type method: " + isType);
             }
             if (check != null) {
             def = c;
             }
             } else {
             if (type != null && type.equals(NotFound.class)) {
             try {
             check = c.getMethod(isType, types);
             } catch (NoSuchMethodException nsme) {
             LOG.debug("Unknown Util", "No type method: " + isType);
             }
             Object u = af.constructor(null).build(c);
             if (check != null && Boolean.parseBoolean("" + check.invoke(u, checkArgs))) {
             if (d != null && d.forType()) {
             def = c;
             } else {
             utility = u;
             break;
             }
             } else {
             LOG.debug("Util Look up by type", "not: " + c.getName());
             }
             } else {
             if (checkType(c, type)) {
             if (d != null && d.forType()) {
             def = c;
             } else {
             utility = af.constructor(null).build(c);
             break;
             }
             } else {
             LOG.debug("Util Look up by checkType", "not: " + c.getName());
             }
             }
             }
             } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
             LOG.debug("The class (" + c.getName() + ") did not have isType, cannot determine if that class is the correct type. " + e.getMessage(), e);
             }
             }/**/
        } else {
            LOG.info("The utility directory was null for some reason: " + util.getCanonicalName() + ":" + id + ":" + (identifier != null ? identifier.getName() : null));
        }
        if (utility == null) {
            if (def != null) {
                try {
                    utility = af.constructor(null).build(def);
                    ((Utility) utility).setData(data);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    LOG.debug("Problem instantiating the default utility (" + def.getName() + "): " + e.getMessage(), e);
                }
            } else {
                LOG.debug("no specific utility found, and no default implementation detected either: " + util.getCanonicalName() + ":" + id + ":" + (identifier != null ? identifier.getName() : null));
            }
        }
        return utility;
    }

    public abstract boolean checkType(Class tool, Class type);
}
