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

import static com.thegoate.testng.TestNGRunFactory.*;

/**
 * Useful when specifying the data loader/provider using annotations.
 * The test class should be annotated with {@literal @}GoateProvider <br>
 * and the data loader/provider class should be annotated with {@literal @}GoateDLP <br>
 * The name fields in each should match.
 * Created by Eric Angeli on 5/12/2017.
 */
public class TestNGEngineAnnotatedDL extends TestNGEngine {

    public TestNGEngineAnnotatedDL(){
        super();
    }

    public TestNGEngineAnnotatedDL(Goate data){
        super(data);
        setTestClass(getClass());
    }

//    @Override
//    public void startUp(Method method) {
//
//    }

    @Override
    public void defineDataLoaders() {
        AnnotationFactory af = new AnnotationFactory();
        GoateProvider gp = getClass().getAnnotation(GoateProvider.class);
        this.provider = gp;
        if(gp!=null && (!runCacheEnabled || (runCacheEnabled && !providerCache.containsKey(providerCacheDefaultId(gp))))){
            try {
                DLProvider provider = (DLProvider) af.find(gp.name()).annotatedWith(GoateDLP.class)
                        .using(GoateDLP.class.getMethod("name"))
                        .build();
                if(provider==null){
                    try {
                        provider = (DLProvider) Class.forName(gp.name()).newInstance();
                    }catch(Exception e){
                        LOG.warn("tried to find DLProvider using class name but could not find or instantiate: " + gp.name(), e);
                    }
                }
                if(provider!=null) {
                    provider.init();
                    defineDataLoaders(provider.getRunDataLoaders(), provider.getConstantDataLoaders());
                }else{
                    throw new Exception("Failed to find the DLProvider: " + gp.name());
                }
            } catch (Exception e) {
                LOG.error("There was a problem building the providers: " + e.getMessage(), e);
            }
        }
    }
}
