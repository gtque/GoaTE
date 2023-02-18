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
package com.thegoate.testng;

import com.thegoate.Goate;
import com.thegoate.annotations.AnnotationFactory;
import com.thegoate.data.DLProvider;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.GoateNullClass;
import com.thegoate.data.GoateProvider;
import com.thegoate.reflection.GoateReflection;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.thegoate.testng.TestNGRunFactory.*;

/**
 * This can be used to define data provider at the method level.
 * The test method should be annotated with {@literal @}GoateProvider <br>
 * and the {@literal @}Test annotation should also specify the dataProvider = "methodLoader"
 * and the data loader/provider class or method native to the test class that defines the data should be annotated with {@literal @}GoateDLP <br>
 * if you define a method in the test class to define the data loaders it must return a Goate[2] array.
 * Index 0 should be the run data loader(s) and index 1 should be the constant data loader(s).
 * The name fields in each should match.
 * Created by Eric Angeli on 5/12/2017.
 */
public class TestNGEngineMethodDL extends TestNGEngineAnnotatedDL {

    public TestNGEngineMethodDL() {
        super();
    }

    public TestNGEngineMethodDL(Goate data) {
        super(data);
    }

    @DataProvider(name = "methodLoader")
    public Object[][] dataLoader(ITestContext context, Method method) throws Exception {
        number.put(""+method.getDeclaringClass().getCanonicalName()+":"+method.getName(), 0);
        //number = 0;//resets the count, assume TestNG loads all the runs before processing the next class.
        setTestClass(method.getDeclaringClass());
        this.testContext = context;
        if (context != null) {
            xt = context.getCurrentXmlTest();
        }
        Goate rdl = new Goate();
        Goate cdl = new Goate();
        buildDataLoaders(rdl, cdl, method);
        return TestNGRunFactory.loadRuns(method, provider, rdl, cdl, false,testContext.getIncludedGroups(),testContext.getExcludedGroups());
    }

    protected void buildDataLoaders(Goate rdl, Goate cdl, Method method) {
        GoateProvider gp = method.getAnnotation(GoateProvider.class);
        this.provider = gp;
        if (gp != null && (!runCacheEnabled || (runCacheEnabled && !providerCache.containsKey(providerCacheDefaultId(gp))))) {
            try {
                AnnotationFactory af = new AnnotationFactory();
                DLProvider dlProvider = (DLProvider) af.find(gp.name()).annotatedWith(GoateDLP.class)
                        .using(GoateDLP.class.getMethod("name"))
                        .build();
                if (dlProvider == null) {
                    try {
                        dlProvider = (DLProvider) Class.forName(gp.name()).getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        LOG.warn("trying to find data loader method provider: " + gp.name());
                    }
                }
                if (dlProvider != null) {
                    dlProvider.init();
//                    dlProvider.getConstantDataLoaders().put("_goate:method", method);
                    rdl.merge(dlProvider.getRunDataLoaders(), true);
                    cdl.merge(dlProvider.getConstantDataLoaders(), true);
                } else {
                    Goate[] providers = buildMethodProviders(gp.name(), method, gp.container());
                    if (providers == null) {
                        throw new Exception("Failed to find the DLProvider: " + gp.name());
                    }else{
                        if(providers[1]==null){
                            providers[1] = new Goate();
                        }
//                        providers[1].put("_goate:method", method);
                        rdl.merge(providers[0], true);
                        cdl.merge(providers[1], true);
                    }
                }
            } catch (Exception e) {
                LOG.error("There was a problem building the providers: " + e.getMessage(), e);
            }
        }
    }

    protected Goate[] buildMethodProviders(String name, Method method, Class container) {
        Goate[] providers = null;
        GoateReflection gr = new GoateReflection();
        List<Method> methods = new ArrayList<>();
        Class declaring_class = method.getDeclaringClass();
        if(!container.equals(GoateNullClass.class)) {
            declaring_class = container;
        }
        gr.getAllMethods(declaring_class, methods);

        for (Method m : methods) {
            GoateDLP dlp = m.getAnnotation(GoateDLP.class);
            if (m.getName().equals(name) || (dlp != null && dlp.name().equals(name))) {
                try {
                    providers = (Goate[]) m.invoke(declaring_class.getDeclaredConstructor().newInstance());
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOG.error("Problem defining data loaders for a method: " + name + "\n" + e.getMessage(), e);
                } catch (InstantiationException e) {
                    LOG.error("Problem defining data loaders for a method, make sure the test class as a default/empty constructor: " + name + "\n" + e.getMessage(), e);
                } catch (NoSuchMethodException e) {
                    LOG.error("Defining Data Loaders", "Problem instantiating new instance: " + e.getMessage(), e);
                }
            }
        }
        return providers;
    }

    @Override
    public void defineDataLoaders() {
        try {
            Constructor constructor = getClass().getConstructor(Goate.class);
            if (constructor!=null){
                Annotation factor = constructor.getAnnotation(Factory.class);
                if(factor!=null){
                    super.defineDataLoaders();
                }
            }
        }catch(Exception e){
            LOG.info("Define Data Loaders", "Not using factory");
        }
    }
}
