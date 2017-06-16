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

package com.thegoate.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Some simple custom methods for reflection.
 * Created by gtque on 4/24/2017.
 */
public class GoateReflection {

    public Constructor findConstructor(Constructor<?>[] constructors, Object[] args) {
        Constructor p = null;
        for (Constructor constructor : constructors) {
            Class<?>[] ptypes = constructor.getParameterTypes();
            boolean found = false;
            if (ptypes.length == args.length) {
                found = true;
                for (int i = 0; i < args.length; i++) {
                    if(args[i]!=null&&isPrimitive(args[i].getClass())){
                        if(ptypes[i].equals(Integer.TYPE)){
                            if(!(args[i] instanceof Integer)){
                                found = false;
                            }
                        } else if(ptypes[i].equals(Byte.TYPE)){
                            if(!(args[i] instanceof Byte)){
                                found = false;
                            }
                        } else if(ptypes[i].equals(Double.TYPE)){
                            if(!(args[i] instanceof Double)){
                                found = false;
                            }
                        } else if(ptypes[i].equals(Float.TYPE)){
                            if(!(args[i] instanceof Float)){
                                found = false;
                            }
                        } else if(ptypes[i].equals(Long.TYPE)){
                            if(!(args[i] instanceof Long)){
                                found = false;
                            }
                        } else if(ptypes[i].equals(Boolean.TYPE)){
                            if(!(args[i] instanceof Boolean)){
                                found = false;
                            }
                        } else if(ptypes[i].equals(Character.TYPE)){
                            if(!(args[i] instanceof Character)){
                                found = false;
                            }
                        }else if(!ptypes[i].equals(Object.class)){
                            found = false;
                        }
                    }else {
                        if (args[i]!=null&&!ptypes[i].isAssignableFrom(args[i].getClass())) {
                            found = false;
                        }
                    }
                }
            }
            if (found) {
                p = constructor;
                break;
            }
        }
        return p;
    }

    public boolean isPrimitive(Class c){
        return c.equals(Boolean.class) || c.equals(Byte.class) || c.equals(Integer.class) || c.equals(Double.class)
                || c.equals(Float.class) || c.equals(Long.class) || c.equals(Character.class);
    }

    public List<Method> getDeclaredMethods(Class klass){
        List<Method> methods = new ArrayList<>();
        for(Method m : klass.getDeclaredMethods()){
            if(!m.getName().startsWith("$jacoco")) {
                methods.add(m);
            }
        }
        return methods;
    }

    public List<Method> getAllMethods(Class klass, List<Method> methods){
        if(methods == null){
            methods = new ArrayList<>();
        }
        for(Method m:klass.getDeclaredMethods()){
            if(!m.getName().startsWith("$jacoco")) {
                methods.add(m);
            }
        }
        if(klass.getSuperclass() != null){
            methods = getAllMethods(klass.getSuperclass(), methods);
        }
        return methods;
    }
}
