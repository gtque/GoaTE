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

package com.thegoate.testng.dsl;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.utils.get.Get;

/**
 * Checks if the run time version is greater than or equal to the specified version..
 * Created by gtque on 10/08/2021.
 */
@GoateDSL(word = "supported since")
@GoateDescription(description = "returns true if the run time version is greater than or equal to the specified version.",
        parameters = {"The specified minimum supported version",
                "The run time version, or how to get the run time version (eut::sut.version)"})
public class SupportedSinceVersionDSL extends DSL {

    public SupportedSinceVersionDSL(Object value) {
        super(value);
    }

    public static String supportedSinceDef(String key, String source) {
        return "supported since::" + key + (source != null ? ("," + source) : "");
    }

    @Override
    public Object evaluate(Goate data) {
        boolean supported = true;
        ;
        String specified = "" + get(1, data);
        String version = "" + get(2, data);
        String[] specifiedParts = specified.replace("-", ".").replaceAll("[a-zA-Z]","0").split("\\.");
        String[] versionParts = version.replace("-", ".").replaceAll("[a-zA-Z]","0").split("\\.");
        for (int i = 0; i < specifiedParts.length; i++) {
            if (i < versionParts.length) {
                try{
                    if((Integer.parseInt(specifiedParts[i]) < Integer.parseInt(versionParts[i]))){
                        break;
                    } else {
                        if((Integer.parseInt(specifiedParts[i]) > Integer.parseInt(versionParts[i]))){
                            supported = false;
                            break;
                        }
                    }
                } catch (NumberFormatException nfe) {
                    LOG.debug("the version must be in the format of a number.");
                }
            } else {
                break;
            }
        }
        return supported;
    }
}
