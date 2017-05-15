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
import com.thegoate.data.GoateProvider;
import com.thegoate.reflection.GoateReflection;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
public class TestNGEngineMethodDL extends TestNGEngine {

    public TestNGEngineMethodDL() {
        super();
    }

    public TestNGEngineMethodDL(Goate data) {
        super(data);
    }

    @DataProvider(name = "methodLoader")
    public Object[][] dataLoader(ITestContext context, Method method) throws Exception {
        number = 0;//resets the count, assume TestNG loads all the runs before processing the next class.
        this.testContext = context;
        if (context != null) {
            xt = context.getCurrentXmlTest();
        }
        Goate rdl = new Goate();
        Goate cdl = new Goate();
        buildDataLoaders(rdl, cdl, method);
        return TestNGRunFactory.loadRuns(rdl, cdl, false);
    }

    protected void buildDataLoaders(Goate rdl, Goate cdl, Method method) {
        GoateProvider gp = method.getAnnotation(GoateProvider.class);
        if (gp != null) {
            try {
                AnnotationFactory af = new AnnotationFactory();
                DLProvider provider = (DLProvider) af.find(gp.name()).annotatedWith(GoateDLP.class)
                        .using(GoateDLP.class.getMethod("name"))
                        .build();
                if (provider == null) {
                    try {
                        provider = (DLProvider) Class.forName(gp.name()).newInstance();
                    } catch (Exception e) {
                        LOG.warn("trying to find data loader method provider: " + gp.name());
                    }
                }
                if (provider != null) {
                    provider.init();
                    rdl.merge(provider.getRunDataLoaders(), true);
                    cdl.merge(provider.getConstantDataLoaders(), true);
                } else {
                    Goate[] providers = buildMethodProviders(gp.name());
                    if (providers == null) {
                        throw new Exception("Failed to find the DLProvider: " + gp.name());
                    }else{
                        rdl.merge(providers[0], true);
                        cdl.merge(providers[1], true);
                    }
                }
            } catch (Exception e) {
                LOG.error("There was a problem building the providers: " + e.getMessage(), e);
            }
        }
    }

    protected Goate[] buildMethodProviders(String name) {
        Goate[] providers = null;
        GoateReflection gr = new GoateReflection();
        List<Method> methods = new ArrayList<>();
        gr.getAllMethods(getClass(), methods);
        for (Method m : methods) {
            GoateDLP dlp = m.getAnnotation(GoateDLP.class);
            if (m.getName().equals(name) || (dlp != null && dlp.name().equals(name))) {
                try {
                    providers = (Goate[]) m.invoke(this);
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOG.error("Problem defining data loaders for a method: " + name + "\n" + e.getMessage(), e);
                }
            }
        }
        return providers;
    }

    @Override
    public void defineDataLoaders() {
    }

    @Override
    public void startUp(Method method) {
        super.startUp(method);
    }

    @BeforeMethod(alwaysRun = true)
    public void initDataMethod(Object[] d, Method m) {
        if (d != null) {
            init((Goate)d[0]);
            startUp(m);
        }
    }
}
