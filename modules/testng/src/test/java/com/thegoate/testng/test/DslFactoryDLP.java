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
package com.thegoate.testng.test;

import com.thegoate.data.DLProvider;
import com.thegoate.data.GoateDLP;
import com.thegoate.data.StaticDL;

/**
 * Simple sample data loader provider.
 * Created by Eric Angeli on 5/12/2017.
 */
@GoateDLP(name = "dsl factory")
public class DslFactoryDLP extends DLProvider {
    @Override
    public void init() {
        runData.put("dl##", new StaticDL().add("a","x").add("Scenario", "use DLProvider.").add("on run", 1));
        runData.put("dl##", new StaticDL().add("a","x").add("Scenario", "use DLProvider 2.").add("on run", 2));
        runData.put("dl##", new StaticDL().add("a","x").add("Scenario", "use DLProvider 3.").add("on run", 3));
        constantData.put("dl##", new StaticDL().add("b","y"));
    }
}
