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
import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;
import com.thegoate.utils.file.Copy;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Some basic utily helpers.
 * Created by gtque on 5/3/2017.
 */
public class GoateUtils {
    static final BleatBox LOG = BleatFactory.getLogger(GoateUtils.class);

    public static Object getProperty(String key){
        return getProperty(key, null);
    }
    public static Object getProperty(String key, Object def){
        return new Goate().get(key,def);
    }

    public static void sleep(long sleepInMillis){
        sleep(sleepInMillis, LOG);
    }

    public static void sleep(long sleepInMillis, BleatBox logger){
        try{
            Thread.sleep(sleepInMillis);
        } catch (InterruptedException e) {
            logger.warn("Goate Sleep Util","problem sleeping: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static String getFilePath(String file){
        return getFilePath(file, false, false);
    }

    public static String moveUpDir(String fileName){
        while(fileName.contains("../")){
            String temp = fileName.substring(0,fileName.indexOf("../"));
            if(temp.endsWith("/")){
                temp = temp.substring(0,temp.length()-1);
            }
            int upFolderIndex = temp.lastIndexOf("/");
            if(upFolderIndex>0){
                temp = temp.substring(0,upFolderIndex);
            }
            fileName = temp + fileName.substring(fileName.indexOf("../")+2);

        }
        return fileName;
    }
    public static String getFilePath(String fileName,boolean leaveInJar, boolean force) {
        if (fileName.indexOf("/") != 0&&fileName.indexOf("\\")!=0)
            fileName = "/"+fileName;

        fileName = moveUpDir(fileName);

        String path = System.getProperty("user.dir") + fileName;
//        LOG.debug("checking path: " + path);
        LOG.debug("Goate File Util","checking path: " + path);
        try {
            File temp = new File(path);
            if (!temp.exists()) {
                path = fileName;
                LOG.debug("Goate File Util","file did not exist, checking resources: " + path);
                URL opath = GoateUtils.class.getResource(path);
                if(opath!=null) {
                    path = GoateUtils.class.getResource(path).toString();
                }else{
                    LOG.debug("Goate File Util","did not find the resource");
                }
//                LOG.debug("path: " + path);
                if (path.contains("jar:")) {
                    if(!leaveInJar) {
                        File tf = new File("temp"+fileName);
                        if(force||!tf.exists()) {
                            String tempPath = new Copy().file(opath).to("temp" + fileName, force);
                            if (tempPath == null) {
                                path = new File("temp" + path.substring(path.lastIndexOf("/"))).getAbsolutePath();
                            } else {
                                path = new File(tempPath).getAbsolutePath();
                            }
                        } else {
                            path = tf.getAbsolutePath();
                        }
                    }else{
                        path = path.replace("jar:","");
                    }
                }
                path = path.replace("file:/", "");
                path = path.replace("file:", "");
                if(path.indexOf(":")!=1)
                    path = "/"+path;
                if (path.contains(":")) {
                    if (path.indexOf("/") == 0) {
                        path = path.substring(1);
                    }
                }
                LOG.debug("Goate File Util","modified path to look in: "+path);
                temp = new File(path);
//                LOG.debug("full adjust path: " + path);
            }
            path = temp.getAbsolutePath();
            LOG.debug("Goate File Util","file path: " + path);
        } catch (Exception e) {
            LOG.error("Goate File Util","Exception encountered finding file: " + e.getMessage(), e);
        }
        return path;
    }

    /**
     * Borrowed from stack over flow. I have lost the original information, if
     * you know it please let me know so I may give appropriate credit.
     * @param key the key or name of the environment variable to set
     * @param value The value to set it to.
     * @param <K> generic type
     * @param <V> generic type
     */
    public static <K, V> void setEnvironment(String key, String value){
        try {
            /// we obtain the actual environment
            final Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            final Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            final boolean environmentAccessibility = theEnvironmentField.isAccessible();
            theEnvironmentField.setAccessible(true);

            final Map<K, V> env = (Map<K, V>) theEnvironmentField.get(null);

            if (System.getProperty("os.name").contains("Windows")) {
                // This is all that is needed on windows running java jdk 1.8.0_92
                if (value == null) {
                    env.remove(key);
                } else {
                    env.put((K) key, (V) value);
                }
            } else {
                // This is triggered to work on openjdk 1.8.0_91
                // The ProcessEnvironment$Variable is the key of the map
                final Class<K> variableClass = (Class<K>) Class.forName("java.lang.ProcessEnvironment$Variable");
                final Method convertToVariable = variableClass.getMethod("valueOf", String.class);
                final boolean conversionVariableAccessibility = convertToVariable.isAccessible();
                convertToVariable.setAccessible(true);

                // The ProcessEnvironment$Value is the value fo the map
                final Class<V> valueClass = (Class<V>) Class.forName("java.lang.ProcessEnvironment$Value");
                final Method convertToValue = valueClass.getMethod("valueOf", String.class);
                final boolean conversionValueAccessibility = convertToValue.isAccessible();
                convertToValue.setAccessible(true);

                if (value == null) {
                    env.remove(convertToVariable.invoke(null, key));
                } else {
                    // we place the new value inside the map after conversion so as to
                    // avoid class cast exceptions when rerunning this code
                    env.put((K) convertToVariable.invoke(null, key), (V) convertToValue.invoke(null, value));

                    // reset accessibility to what they were
                    convertToValue.setAccessible(conversionValueAccessibility);
                    convertToVariable.setAccessible(conversionVariableAccessibility);
                }
            }
            // reset environment accessibility
            theEnvironmentField.setAccessible(environmentAccessibility);

            // we apply the same to the case insensitive environment
            final Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            final boolean insensitiveAccessibility = theCaseInsensitiveEnvironmentField.isAccessible();
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            // Not entirely sure if this needs to be casted to ProcessEnvironment$Variable and $Value as well
            final Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            if (value == null) {
                // remove if null
                cienv.remove(key);
            } else {
                cienv.put(key, value);
            }
            theCaseInsensitiveEnvironmentField.setAccessible(insensitiveAccessibility);
        } catch (final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed setting environment variable <" + key + "> to <" + value + ">", e);
        } catch (final NoSuchFieldException e) {
            // we could not find theEnvironment
            final Map<String, String> env = System.getenv();
            //Stream.of(Collections.class.getDeclaredClasses())
            List<Class> filteredClass = filterClasses();
            try {
                List<Field> map = mapFields(filteredClass);
                for(Field field:map){
                    try {
                        final boolean fieldAccessibility = field.isAccessible();
                        field.setAccessible(true);
                        // we obtain the environment
                        final Map<String, String> map2 = (Map<String, String>) field.get(env);
                        if (value == null) {
                            // remove if null
                            map.remove(key);
                        } else {
                            map2.put(key, value);
                        }
                        // reset accessibility
                        field.setAccessible(fieldAccessibility);
                    } catch (final ConcurrentModificationException e1) {
                        // This may happen if we keep backups of the environment before calling this method
                        // as the map that we kept as a backup may be picked up inside this block.
                        // So we simply skip this attempt and continue adjusting the other maps
                        // To avoid this one should always keep individual keys/value backups not the entire map
                        LOG.info("Attempted to modify source map: " + field.getDeclaringClass() + "#" + field.getName(), e1);
                    } catch (final IllegalAccessException e1) {
                        throw new IllegalStateException("Failed setting environment variable <" + key + "> to <" + value + ">. Unable to access field!", e1);
                    }
                }
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        LOG.info("Set environment variable <" + key + "> to <" + value + ">. Sanity Check: " + System.getenv(key));
    }

    public static void removeEnvironment(String key){
        setEnvironment(key, null);
    }

    protected static List<Class> filterClasses(){
        List<Class> classes = new ArrayList<>();
        for(Class c : Collections.class.getDeclaredClasses()) {
            // obtain the declared classes of type $UnmodifiableMap
            if("java.util.Collections$UnmodifiableMap".equals(c.getName())){
                classes.add(c);
            }
        }
        return classes;
    }

    protected static List<Field> mapFields(List<Class> classes) throws NoSuchFieldException {
        List<Field> map = new ArrayList<>();
        for(Class c:classes){
            map.add(c.getDeclaredField("m"));
        }
        return map;
    }

    public static String tab(int count){
        StringBuilder tabs = new StringBuilder("");
        for(;count>0;count--){
           tabs.append("\t");
        }
        return tabs.toString();
    }
}
